package service.api_gateway.threadpool;


import com.fasterxml.jackson.databind.ObjectMapper;
import service.api_gateway.GatewayService;
import service.api_gateway.logger.ServiceLogger;
import service.api_gateway.models.LoginResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Worker extends Thread {
    int workID;
    ThreadPool threadPool;

    private Worker(int workID, ThreadPool threadPool) {
        this.workID = workID;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process() {
        ClientRequest request = threadPool.remove();
        if (request == null) {
            return;
        }
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        String uri = request.getURI();
        String endpoint = request.getEndpoint();
        WebTarget webTarget = client.target(uri).path(endpoint);

        if (request.getRequest().getQueryParams() != null) {
            for (String i : request.getRequest().getQueryParams().keySet()) {
                webTarget = webTarget.queryParam(i, request.getRequest().getQueryParams().get(i));
            }
        }

        if (request.getRequest().getUrlParam() != null && !request.getRequest().getUrlParam().equals("")) {
            webTarget = client.target(uri).path(endpoint.split("\\{")[0] + request.getRequest().getUrlParam());
        }

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        invocationBuilder.header("email", request.getEmail()).
                header("sessionID", request.getSessionID()).
                header("transactionID", request.getTransactionID());
        Response response;

        if (request.getType().equals(GatewayService.POST)) {
            response = invocationBuilder.post(Entity.entity(request.getRequest().getJsonText(), MediaType.APPLICATION_JSON));
        }
        else if (request.getType().equals(GatewayService.GET)) {
            response = invocationBuilder.get();
        }
        else {
            response = invocationBuilder.delete();
        }

        String jsonText = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();


        Connection con = GatewayService.getConPool().requestCon();
        try {
            String query = "INSERT INTO responses (transactionid, email, sessionid, response, httpstatus) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, request.getTransactionID());
            ps.setString(2, request.getEmail());

            if (jsonText.contains("logged in successfully")) {
                try{
                    LoginResponseModel responseModel = mapper.readValue(jsonText, LoginResponseModel.class);
                    ps.setString(3, responseModel.getSessionID());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                ps.setString(3, request.getSessionID());
            }
            ps.setString(4, jsonText);
            ps.setInt(5, response.getStatus());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        GatewayService.getConPool().releaseCon(con);
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }
}
