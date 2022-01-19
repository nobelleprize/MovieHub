package service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.idm.core.*;
import service.idm.logger.ServiceLogger;
import service.idm.models.*;
import service.idm.security.Crypto;
import service.idm.security.Session;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

@Path("")
public class IDMPage {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(String jsonText){
        ServiceLogger.LOGGER.info("Received request to register.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        RegisterRequestModel requestModel = null;
        RegisterResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);
            int caseNumber = ValidateRegister.checkCase(requestModel.getEmail(), requestModel.getPassword());
            responseModel = new RegisterResponseModel(
                    ValidateRegister.checkCase(requestModel.getEmail(), requestModel.getPassword()),
                    ValidateRegister.caseMessage(caseNumber));
            if (caseNumber == -12 || caseNumber == -11 || caseNumber == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            if (caseNumber == 12 || caseNumber == 13)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = IDMRecords.insertAccountToDb(requestModel);
            if (responseModel.getResultCode() == -1)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            if (e instanceof JsonMappingException)
            {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new RegisterResponseModel(-2, ValidateRegister.caseMessage(-2))).build();
            }
            else if (e instanceof JsonParseException)
            {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new RegisterResponseModel(-3, ValidateRegister.caseMessage(-3))).build();
            }
            ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(String jsonText){
        ServiceLogger.LOGGER.info("Received request to login.");
        ServiceLogger.LOGGER.info("Login Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestModel requestModel = null;
        LoginResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, LoginRequestModel.class);
            int caseNumber = ValidateRegister.checkCase(requestModel.getEmail(), requestModel.getPassword());
            responseModel = new LoginResponseModel(
                    ValidateLogin.checkCase(requestModel.getEmail(), requestModel.getPassword()),
                    ValidateRegister.caseMessage(caseNumber), null);
            if (caseNumber == -12 || caseNumber == -11 || caseNumber == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = IDMRecords.loginToDb(requestModel);
            if (responseModel.getResultCode() == -1)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            if (e instanceof JsonMappingException)
            {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new LoginResponseModel(-2, ValidateLogin.caseMessage(-2), null)).build();
            }
            else if (e instanceof JsonParseException)
            {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new LoginResponseModel(-3, ValidateLogin.caseMessage(-3), null)).build();
            }
            ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Session(String jsonText){
        ServiceLogger.LOGGER.info("Received request to session.");
        ServiceLogger.LOGGER.info("Session Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        SessionRequestModel requestModel = null;
        SessionResponseModel responseModel = null;
        try {
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
            int caseNumber = ValidateSession.checkCase(requestModel.getEmail(), requestModel.getSessionID());
            responseModel = new SessionResponseModel(
                    ValidateSession.checkCase(requestModel.getEmail(), requestModel.getSessionID()),
                    ValidateSession.caseMessage(caseNumber), null);
            if (caseNumber == - -13|| caseNumber == -11 || caseNumber == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = IDMRecords.verifySessionWithDb(requestModel);
            if (responseModel.getResultCode() == -1)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        catch (IOException e)
        {
            if (e instanceof JsonMappingException)
            {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new SessionResponseModel(-2, ValidateSession.caseMessage(-2), null)).build();
            }
            else if (e instanceof JsonParseException)
            {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new SessionResponseModel(-3, ValidateSession.caseMessage(-3), null)).build();
            }
            ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Privilege(String jsonText){
        ServiceLogger.LOGGER.info("Received request to privilege.");
        ServiceLogger.LOGGER.info("Privilege Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        PrivilegeRequestModel requestModel = null;
        PrivilegeResponseModel responseModel = null;
        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
            int caseNumber = ValidatePrivilege.checkCase(requestModel.getEmail(), requestModel.getPlevel());
            responseModel = new PrivilegeResponseModel(
                    caseNumber, ValidatePrivilege.caseMessage(caseNumber));
            if (caseNumber == -14|| caseNumber == -11 || caseNumber == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = IDMRecords.verifyPrivilegeWithDb(requestModel);
            if (responseModel.getResultCode() == -1)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e)
        {
            if (e instanceof JsonMappingException)
            {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new PrivilegeResponseModel(-2, ValidatePrivilege.caseMessage(-2))).build();
            }
            else if (e instanceof JsonParseException)
            {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                return Response.status(Response.Status.BAD_REQUEST).entity(new PrivilegeResponseModel(-3, ValidatePrivilege.caseMessage(-3))).build();
            }
            ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("sessionTest")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(String jsonText) {
        ServiceLogger.LOGGER.info("Received request for session.");
        ServiceLogger.LOGGER.info("Session Request :\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        SessionRequestModel rsm = null;
        SessionModel sessionModel = null;

        try {
            rsm = mapper.readValue(jsonText, SessionRequestModel.class);
            ServiceLogger.LOGGER.info("Email: " + rsm.getEmail());
            Session session = Session.createSession(rsm.getEmail());
            sessionModel = new SessionModel(session.getEmail(), session.getSessionID().toString());
        } catch (IOException e) {
            ServiceLogger.LOGGER.info("IOException.");
        }
        ServiceLogger.LOGGER.info("Returning session: " + sessionModel);
        return Response.status(Response.Status.OK).entity(sessionModel).build();
    }

    @Path("hashedPass")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response hashPassword(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to hash password.");
        ServiceLogger.LOGGER.info("HashedPass Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        HashPassRequestModel requestModel = null;
        HashedPassResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, HashPassRequestModel.class);
            char[] pword = requestModel.getPassword().toCharArray();
            byte[] salt = Crypto.genSalt();
            byte[] hashedPassword = Crypto.hashPassword(pword, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
            String password = getHashedPass(hashedPassword);

            responseModel = new HashedPassResponseModel(requestModel.getPassword(), password);
        } catch (IOException e) {
            ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    private String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    private String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }
}
