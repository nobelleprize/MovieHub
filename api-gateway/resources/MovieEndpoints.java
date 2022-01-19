package service.api_gateway.resources;

import service.api_gateway.GatewayService;
import service.api_gateway.logger.ServiceLogger;
import service.api_gateway.models.*;
import service.api_gateway.threadpool.ClientRequest;
import service.api_gateway.utilities.ModelValidator;
import service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) {

        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("email: "+ email);
        String sessionID = headers.getHeaderString("sessionID");
        ServiceLogger.LOGGER.info("sessionID: "+ sessionID);


        String query = uriInfo.getRequestUri().getQuery();

        Map<String, String> result = new HashMap<>();

        String[] queries = query.split("&");
        for (String queryParam : queries) {
            String[] params = queryParam.split("=");
            result.put(params[0], params[1]);
        }


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
        FullRequestModel queryRequestModel = new FullRequestModel(email, sessionID, result);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch());
        clientRequest.setRequest(queryRequestModel);
        clientRequest.setType(GatewayService.GET);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
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
        FullRequestModel idRequestModel = new FullRequestModel(email, sessionID, movieid);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPMovieGet());
        clientRequest.setRequest(idRequestModel);
        clientRequest.setType(GatewayService.GET);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, String jsonText) {

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
        FullRequestModel jsonRequestModel = new FullRequestModel(email, sessionID, jsonText);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPMovieAdd());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
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
        FullRequestModel idRequestModel = new FullRequestModel(email, sessionID, movieid);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPMovieDelete());
        clientRequest.setRequest(idRequestModel);
        clientRequest.setType(GatewayService.DELETE);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {

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
        FullRequestModel userRequestModel = new FullRequestModel(email, sessionID);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());
        clientRequest.setRequest(userRequestModel);
        clientRequest.setType(GatewayService.GET);

        GatewayService.getThreadPool().add(clientRequest);


        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, String jsonText) {

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPGenreAdd());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
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
        FullRequestModel idRequestModel = new FullRequestModel(email, sessionID, movieid);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPGenreMovie());
        clientRequest.setRequest(idRequestModel);
        clientRequest.setType(GatewayService.GET);
        clientRequest.setTransactionID(newID);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) {

        String query = uriInfo.getRequestUri().getQuery();

        Map<String, String> result = new HashMap<>();

        String[] queries = query.split("&");
        for (String queryParam : queries) {
            String[] params = queryParam.split("=");
            result.put(params[0], params[1]);
        }

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
        FullRequestModel queryRequestModel = new FullRequestModel(email, sessionID, result);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPStarSearch());
        clientRequest.setRequest(queryRequestModel);
        clientRequest.setType(GatewayService.GET);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers, @PathParam("id") String id) {
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
        FullRequestModel idRequestModel = new FullRequestModel(email, sessionID, id);

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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPStarGet());
        clientRequest.setRequest(idRequestModel);
        clientRequest.setType(GatewayService.GET);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, String jsonText) {
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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers, String jsonText) {
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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPStarIn());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, String jsonText) {
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

        clientRequest.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        clientRequest.setEndpoint(GatewayService.getMovieConfigs().getEPRating());
        clientRequest.setRequest(jsonRequestModel);
        clientRequest.setType(GatewayService.POST);

        GatewayService.getThreadPool().add(clientRequest);

        return Response.status(Status.NO_CONTENT).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("transactionID", newID).build();
    }

}
