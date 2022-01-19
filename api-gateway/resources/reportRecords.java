package service.api_gateway.resources;

import service.api_gateway.GatewayService;
import service.api_gateway.logger.ServiceLogger;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class reportRecords {

    public static Response report(String transactionID, String email)
    {
        try{
            Connection con = GatewayService.getConPool().requestCon();
            String query = "SELECT * FROM responses WHERE transactionid = ? AND email = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, transactionID);
            ps.setString(2, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next()) {
                int status = rs.getInt("httpstatus");
                String response = rs.getString("response");
                ServiceLogger.LOGGER.info("Status: " + status + ",  Response: " + response);

                String deleteQuery = "DELETE FROM responses WHERE transactionid = ?";
                PreparedStatement deletePS = con.prepareStatement(deleteQuery);
                deletePS.setString(1, transactionID);
                ServiceLogger.LOGGER.info("Trying query: " + deletePS.toString());
                deletePS.executeUpdate();
                ServiceLogger.LOGGER.info("Query succeeded.");
                return Response.status(status).entity(response).build();
            }
            else {
                return Response.status(Response.Status.NO_CONTENT).
                        header("message", "Waiting on response.").
                        header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                        header("transactionID", transactionID).build();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
