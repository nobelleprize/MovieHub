package service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.movies.core.StarRecords;
import service.movies.core.validate;
import service.movies.logger.ServiceLogger;
import service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("star")
public class StarPage {

    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response stars(@Context HttpHeaders headers,
                          @QueryParam("name") String name,
                          @QueryParam("birthYear") int birthYear,
                          @QueryParam("movieTitle") String movieTitle,
                          @QueryParam("limit") int limit,
                          @QueryParam("offset") int offset,
                          @QueryParam("orderby") String orderBy,
                          @QueryParam("direction") String direction)
        {
        SearchStarRequestModel requestModel;
        SearchStarResponseModel responseModel;
        ServiceLogger.LOGGER.info("Received request to search for stars.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        requestModel = new SearchStarRequestModel(
                email, name, birthYear, movieTitle, limit, offset, orderBy, direction);

        ServiceLogger.LOGGER.info("Passed Precheck!");

        responseModel = StarRecords.searchStarInDb(requestModel);

        if (responseModel.getResultCode() == -1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID)
                    .build();
        }
        return Response.status(Response.Status.OK).
                header("email", email).
                header("sessionID", sessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarById(@Context HttpHeaders headers,
                                @PathParam("id") String id) {
        ServiceLogger.LOGGER.info("Received request to get star with id: " + id);
        GetStarResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        responseModel = StarRecords.getStarByIdInDb(id, email);

        if (responseModel.getResultCode() == -1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID)
                    .build();
        }
        return Response.status(Response.Status.OK).
                header("email", email).
                header("sessionID", sessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStar(@Context HttpHeaders headers, String jsonText) {
        AddStarRequestModel requestModel;
        ResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("Received request to add star.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, AddStarRequestModel.class);
            requestModel.email = email;

            responseModel = StarRecords.addStarToDb(requestModel);
            if (responseModel.getResultCode() == -1) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                        header("email", email).
                        header("sessionID", sessionID).
                        header("transactionID", transactionID)
                        .build();
            }
            return Response.status(Response.Status.OK).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovie(@Context HttpHeaders headers, String jsonText) {
        AddStarToMovieRequestModel requestModel;
        ResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("Received request to add star to movie.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, AddStarToMovieRequestModel.class);
            requestModel.email = email;

            responseModel = StarRecords.AddStarToMovieInDb(requestModel);
            if (responseModel.getResultCode() == -1) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                        header("email", email).
                        header("sessionID", sessionID).
                        header("transactionID", transactionID)
                        .build();
            }
            return Response.status(Response.Status.OK).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    private Response catchIOException(IOException e)
    {
        if (e instanceof JsonMappingException) {
            ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
        } else if (e instanceof JsonParseException) {
            ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-3, validate.caseMessage(-3))).build();
        }
        ServiceLogger.LOGGER.info("IOException");
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
    }
}
