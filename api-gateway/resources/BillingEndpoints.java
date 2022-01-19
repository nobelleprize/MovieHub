package service.api_gateway.resources;

import service.api_gateway.GatewayService;
import service.api_gateway.models.FullRequestModel;
import service.api_gateway.models.RequestModel;
import service.api_gateway.models.ResponseModel;
import service.api_gateway.models.SessionResponseModel;
import service.api_gateway.threadpool.ClientRequest;
import service.api_gateway.utilities.ModelValidator;
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

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText)
    {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }


        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
    {
        return Response.status(Status.OK).entity(checkSession).build();
    }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCartInsert());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText)
    {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText)
    {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCartDelete());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }


        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCartRetrieve());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }


        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCartClear());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCcInsert());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);
        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCcUpdate());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCcDelete());
        clientRequest.setRequest(jsonModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCcRetrieve());
        clientRequest.setRequest(jsonModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setTransactionID(newID);

        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerUpdate());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }


        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerRetrieve());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPOrderPlace());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null) {
            ResponseModel responseModel = new ResponseModel(
                    -17, validate.caseMessage(-17));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        String newID = TransactionIDGenerator.generateTransactionID();

        RequestModel requestModel = new RequestModel(email, sessionID);
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText, false);

        SessionResponseModel checkSession = ModelValidator.verifySession(requestModel);
        if (checkSession.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (checkSession.getResultCode() == 131)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 132)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }
        if (checkSession.getResultCode() == 134)
        {
            return Response.status(Status.OK).entity(checkSession).build();
        }

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setEmail(email);
        clientRequest.setSessionID(checkSession.getSessionID());
        clientRequest.setTransactionID(newID);

        clientRequest.setURI(GatewayService.getBillingConfigs().getBillingUri());
        clientRequest.setEndpoint(GatewayService.getBillingConfigs().getEPOrderRetrieve());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).header("sessionID", sessionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }
}
