package service.api_gateway.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.api_gateway.GatewayService;
import service.api_gateway.models.FullRequestModel;
import service.api_gateway.models.PrivilegeRequestModel;
import service.api_gateway.models.RegisterRequestModel;
import service.api_gateway.models.SessionRequestModel;
import service.api_gateway.threadpool.ClientRequest;
import service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {

        String newID = TransactionIDGenerator.generateTransactionID();
        RegisterRequestModel requestModel;

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            requestModel = new RegisterRequestModel(null, null);
        }

        FullRequestModel jsonRequestModel = new FullRequestModel(requestModel.getEmail(), null, jsonText, false);

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(requestModel.getEmail());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getIdmConfigs().getIdmUri());
        clientRequest.setEndpoint(GatewayService.getIdmConfigs().getEPUserRegister());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {

        String newID = TransactionIDGenerator.generateTransactionID();
        RegisterRequestModel requestModel;

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            requestModel = new RegisterRequestModel(null, null);
        }

        FullRequestModel jsonRequestModel = new FullRequestModel(requestModel.getEmail(), null, jsonText,
                false);

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(requestModel.getEmail());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getIdmConfigs().getIdmUri());
        clientRequest.setEndpoint(GatewayService.getIdmConfigs().getEPUserLogin());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(String jsonText) {

        String newID = TransactionIDGenerator.generateTransactionID();
        SessionRequestModel requestModel;

        try {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            requestModel = new SessionRequestModel(null, null);
        }

        FullRequestModel jsonRequestModel = new FullRequestModel(requestModel.getEmail(), requestModel.getSessionID(),
                jsonText, false);

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(requestModel.getEmail());
        clientRequest.setSessionID(requestModel.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getIdmConfigs().getIdmUri());
        clientRequest.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(@Context HttpHeaders headers, String jsonText) {

        String newID = TransactionIDGenerator.generateTransactionID();
        PrivilegeRequestModel requestModel;

        try {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            requestModel = new PrivilegeRequestModel(null, 0);
        }


        FullRequestModel jsonRequestModel = new FullRequestModel(requestModel.getEmail(), null, jsonText,
                false);

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(requestModel.getEmail());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getIdmConfigs().getIdmUri());
        clientRequest.setEndpoint(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }
}

