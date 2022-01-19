package service.idm.core;

import service.idm.IDMService;
import service.idm.logger.ServiceLogger;
import service.idm.models.*;
import service.idm.security.Crypto;
import service.idm.security.Session;
import service.idm.security.Token;
import org.apache.commons.codec.binary.Hex;

import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static service.idm.security.Session.ACTIVE;
import static service.idm.security.Session.REVOKED;
import static java.lang.String.format;

public class IDMRecords {
    public static RegisterResponseModel insertAccountToDb(RegisterRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Registering account...");
        try
        {

            String query =
                    "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next())
            {
                ServiceLogger.LOGGER.info("Email already in use: " + requestModel.getEmail());
                return new RegisterResponseModel(16, ValidateRegister.caseMessage(16));
            }
            else
            {
                ServiceLogger.LOGGER.info("Valid Email: " + requestModel.getEmail());
                String insertQuery =
                        "INSERT INTO users (email, status, plevel, salt, pword) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement ps2 = IDMService.getCon().prepareStatement(insertQuery);
                ps2.setString(1, requestModel.getEmail());

                byte[] salt = Crypto.genSalt();
                String encodedSalt = Hex.encodeHexString(salt);
                String hashedPass = Hex.encodeHexString(Crypto.hashPassword(requestModel.getPassword(), salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH));
                ps2.setInt(2, AccountCodes.USER_STATUS_DEFAULT);
                ps2.setInt(3, AccountCodes.PLEVEL_DEFAULT);
                ps2.setString(4, encodedSalt);
                ps2.setString(5, hashedPass);

                ServiceLogger.LOGGER.info("Trying query: " + ps2.toString());
                ps2.execute();
                ServiceLogger.LOGGER.info("Query succeeded.");
                RegisterResponseModel responseModel = new RegisterResponseModel(110, ValidateRegister.caseMessage(110));
                ServiceLogger.LOGGER.info("RESPONSE MODEL: " + responseModel.toString());
                return responseModel;
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert account " + requestModel.getEmail());
            e.printStackTrace();
        }
        return new RegisterResponseModel(-1, ValidateRegister.caseMessage(-1));
    }

    public static LoginResponseModel loginToDb(LoginRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Logging in user...");
        try
        {
            String searchEmailQuery =
                    "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(searchEmailQuery);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next())
            {
                byte[] salt = convert(rs.getString("salt"));
                String encodedPass = rs.getString("pword");
                String hashedPass = Hex.encodeHexString(Crypto.hashPassword(requestModel.getPassword(), salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH));
                if (encodedPass.equals(hashedPass))
                {
                    ServiceLogger.LOGGER.info("Email and password valid");
                    Session s = Session.createSession(requestModel.getEmail());
                    String searchSessions =
                            "SELECT * FROM sessions WHERE email = ?";
                    PreparedStatement sessionPS = IDMService.getCon().prepareStatement(searchSessions);
                    sessionPS.setString(1, requestModel.getEmail());
                    ServiceLogger.LOGGER.info("Trying query: " + sessionPS.toString());
                    ResultSet sessionRS = sessionPS.executeQuery();
                    ServiceLogger.LOGGER.info("Query succeeded.");
                    if (sessionRS.next())
                    {
                        ServiceLogger.LOGGER.info("Existing session found, revoking it.");
                        String revokeSession =
                                "UPDATE sessions SET status = " + REVOKED + " WHERE sessions.email = '" + requestModel.getEmail() + "'";
                        PreparedStatement revokePS = IDMService.getCon().prepareStatement(revokeSession);
                        ServiceLogger.LOGGER.info("Trying query: " + revokePS.toString());
                        revokePS.executeUpdate();
                        ServiceLogger.LOGGER.info("Query succeeded.");
                    }
                    ServiceLogger.LOGGER.info("Inserting session into database.");

                    String insertSession =
                            "INSERT INTO sessions (sessionID, email, status, exprTime) VALUES(?, ?, ?, ?)";
                    PreparedStatement insertSessionPS = IDMService.getCon().prepareStatement(insertSession);
                    insertSessionPS.setString(1, s.getSessionID().toString());
                    insertSessionPS.setString(2, requestModel.getEmail());
                    insertSessionPS.setInt(3, ACTIVE);
                    insertSessionPS.setTimestamp(4, s.getExprTime());
                    ServiceLogger.LOGGER.info("Trying query: " + insertSessionPS.toString());
                    insertSessionPS.execute();
                    ServiceLogger.LOGGER.info("Query succeeded.");
                    return new LoginResponseModel(120, ValidateLogin.caseMessage(120), s.getSessionID().toString());
                }
                else
                {
                    ServiceLogger.LOGGER.info("Passwords do not match.");
                    return new LoginResponseModel(11, ValidateLogin.caseMessage(11), null);
                }
            }
            else
            {
                ServiceLogger.LOGGER.info("Email not found: " + requestModel.getEmail());
                return new LoginResponseModel(14, ValidateLogin.caseMessage(14), null);
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to login: " + requestModel.getEmail());
            e.printStackTrace();
        }
        return new LoginResponseModel(-1, ValidateLogin.caseMessage(-1), null);
    }

    public static SessionResponseModel verifySessionWithDb(SessionRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Verifying session with database...");
        try
        {
            String searchEmailQuery =
                    "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(searchEmailQuery);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next()) {
                ServiceLogger.LOGGER.info("Found email in database: " + requestModel.getEmail());
                String searchSessionID =
                        "SELECT * FROM sessions WHERE email = ? AND sessionID = ?";
                PreparedStatement searchSessionIDPS = IDMService.getCon().prepareStatement(searchSessionID);
                searchSessionIDPS.setString(1, requestModel.getEmail());
                searchSessionIDPS.setString(2, requestModel.getSessionID());
                ServiceLogger.LOGGER.info("Trying query: " + searchSessionIDPS.toString());
                ResultSet searchSessionIDRS = searchSessionIDPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");
                if (searchSessionIDRS.next())
                {
                    ServiceLogger.LOGGER.info("Checking session status...");
                    if (searchSessionIDRS.getInt("status") == 2)
                    {
                        return new SessionResponseModel(132, ValidateSession.caseMessage(132),
                            null);
                    }
                    if (searchSessionIDRS.getInt("status") == 3)
                    {
                        return new SessionResponseModel(131, ValidateSession.caseMessage(131),
                            null);
                    }
                    if (searchSessionIDRS.getInt("status") == 4)
                    {
                        return new SessionResponseModel(133, ValidateSession.caseMessage(133),
                            null);
                    }
                    Session currSession = Session.rebuildSession(searchSessionIDRS.getString("email"),
                            Token.rebuildToken(searchSessionIDRS.getString("sessionID")),
                            searchSessionIDRS.getTimestamp("timeCreated"),
                            searchSessionIDRS.getTimestamp("lastUsed"),
                            searchSessionIDRS.getTimestamp("exprTime"),
                            searchSessionIDRS.getInt("status"));
                    int sessionCase = currSession.update();

                    String sessionQuery =
                            "UPDATE sessions SET status = ? WHERE email = ? AND sessionID = ?";

                    if (sessionCase == Session.ACTIVE)
                    {
                        ServiceLogger.LOGGER.info("Session status is ACTIVE.");
                        String sessionActiveQuery =
                                "UPDATE sessions SET lastUsed = ? WHERE email = ? AND sessionID = ?";
                        PreparedStatement sessionActivePS = IDMService.getCon().prepareStatement(sessionActiveQuery);
                        sessionActivePS.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                        sessionActivePS.setString(2, currSession.getEmail());
                        sessionActivePS.setString(3, requestModel.getSessionID());

                        ServiceLogger.LOGGER.info("Trying query: " + sessionActivePS.toString());
                        sessionActivePS.executeUpdate();
                        ServiceLogger.LOGGER.info("Query succeeded.");
                        return new SessionResponseModel(130, ValidateSession.caseMessage(130),
                                currSession.getSessionID().toString());
                    }
                    else if (sessionCase == Session.REVOKED)
                    {
                        ServiceLogger.LOGGER.info("Setting session status to REVOKED.");
                        PreparedStatement updateSessionPS = IDMService.getCon().prepareStatement(sessionQuery);
                        updateSessionPS.setInt(1, currSession.getStatus());
                        updateSessionPS.setString(2, currSession.getEmail());
                        updateSessionPS.setString(3, requestModel.getSessionID());

                        ServiceLogger.LOGGER.info("Trying query: " + updateSessionPS.toString());
                        updateSessionPS.executeUpdate();
                        ServiceLogger.LOGGER.info("Query succeeded.");
                        return new SessionResponseModel(133, ValidateSession.caseMessage(133),
                                null);
                    }
                    else if (sessionCase == Session.EXPIRED)
                    {
                        ServiceLogger.LOGGER.info("Setting session status to EXPIRED.");
                        PreparedStatement updateSessionPS = IDMService.getCon().prepareStatement(sessionQuery);
                        updateSessionPS.setInt(1, currSession.getStatus());
                        updateSessionPS.setString(2, requestModel.getEmail());
                        updateSessionPS.setString(3, requestModel.getSessionID());

                        ServiceLogger.LOGGER.info("Trying query: " + updateSessionPS.toString());
                        updateSessionPS.executeUpdate();
                        ServiceLogger.LOGGER.info("Query succeeded.");
                        return new SessionResponseModel(131, ValidateSession.caseMessage(131),
                                null);
                    }
                    else if (currSession.getStatus() == Session.EXPIRED)
                {
                    ServiceLogger.LOGGER.info("Session is CLOSED.");
                    return new SessionResponseModel(131, ValidateSession.caseMessage(131),
                            null);
                }
                    else if (currSession.getStatus() == Session.CLOSED)
                    {
                        ServiceLogger.LOGGER.info("Session is CLOSED.");
                        return new SessionResponseModel(132, ValidateSession.caseMessage(132),
                                null);
                    }
                    else // ACTIVE_REVOKED, generate new session
                    {
                        ServiceLogger.LOGGER.info("Setting session status to REVOKED.");
                        PreparedStatement updateSessionPS = IDMService.getCon().prepareStatement(sessionQuery);
                        updateSessionPS.setInt(1, currSession.getStatus());
                        ServiceLogger.LOGGER.info("Trying query: " + updateSessionPS.toString());
                        updateSessionPS.executeUpdate();
                        ServiceLogger.LOGGER.info("Query succeeded.");

                        // Generate new session
                        String newSessionQ =
                                "INSERT INTO sessions (sessionID, email, status, exprTime) VALUES (?, ?, ?, ?)";
                        Session insertSession = Session.createSession(currSession.getEmail());

                        PreparedStatement newSessionPS = IDMService.getCon().prepareStatement(newSessionQ);
                        newSessionPS.setString(1, insertSession.getSessionID().toString());
                        newSessionPS.setString(2, insertSession.getEmail());
                        newSessionPS.setInt(3, insertSession.getStatus());
                        newSessionPS.setTimestamp(4, insertSession.getExprTime());
                        ServiceLogger.LOGGER.info("Trying query: " + newSessionPS.toString());
                        newSessionPS.execute();
                        ServiceLogger.LOGGER.info("Query succeeded.");
                        return new SessionResponseModel(130, ValidateSession.caseMessage(130),
                                insertSession.getSessionID().toString());
                    }
                }
                else
                {
                    return new SessionResponseModel(134, ValidateSession.caseMessage(134), null);

                }
            }
            else
            {
                ServiceLogger.LOGGER.info("Email not found in database: " + requestModel.getEmail());
                return new SessionResponseModel(14, ValidateSession.caseMessage(14), null);

            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to verify session: " + requestModel.getEmail() + requestModel.getSessionID());
            e.printStackTrace();
        }
        return new SessionResponseModel(-1, ValidateSession.caseMessage(-1), null);
    }

    public static PrivilegeResponseModel verifyPrivilegeWithDb(PrivilegeRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Verifying privilege...");
        try
        {
            String searchEmailQuery =
                    "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(searchEmailQuery);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next()) {
                int plevel = rs.getInt("plevel");
                if (requestModel.getPlevel() >= plevel)
                {
                    ServiceLogger.LOGGER.info("User has sufficient privilege level");
                    return new PrivilegeResponseModel(140, ValidatePrivilege.caseMessage(140));
                }
                else
                {
                    ServiceLogger.LOGGER.info("User has insufficient privilege level");
                    return new PrivilegeResponseModel(141, ValidatePrivilege.caseMessage(141));
                }
            }
            else
            {
                ServiceLogger.LOGGER.info("Unable to find user in database: " + requestModel.getEmail());
                return new PrivilegeResponseModel(14, ValidatePrivilege.caseMessage(14));
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert account " + requestModel.getEmail());
            e.printStackTrace();
        }
        return new PrivilegeResponseModel(-1, ValidatePrivilege.caseMessage(-1));
    }


    private static byte[] convert(String tok) {
        int len = tok.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(tok.charAt(i), 16) << 4) + Character.digit(tok.charAt(i + 1), 16));
        }
        return data;
    }
}
