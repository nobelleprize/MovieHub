package service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.movies.core.MovieRecords;
import service.movies.core.validate;
import service.movies.logger.ServiceLogger;
import service.movies.models.*;
import service.movies.models.SearchMoviesRequestModel;
import service.movies.models.SearchMoviesResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("")
public class MoviePage {

    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovies(@Context HttpHeaders headers,
                                 @QueryParam("title") String title,
                                 @QueryParam("genre") String genre,
                                 @QueryParam("year") int year,
                                 @QueryParam("director") String director,
                                 @QueryParam("hidden") boolean hidden,
                                 @QueryParam("limit") int limit,
                                 @QueryParam("offset") int offset,
                                 @QueryParam("orderby") String orderby,
                                 @QueryParam("direction") String direction)
    {
        ServiceLogger.LOGGER.info("Received request to search for movies.");

        SearchMoviesRequestModel requestModel;
        SearchMoviesResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        requestModel = new SearchMoviesRequestModel(
                email, title, genre, year, director, hidden, limit, offset, orderby, direction);

        responseModel = MovieRecords.searchMovieInDb(requestModel);

        if (responseModel.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    build();
        }
        return Response.status(Status.OK).
                header("email", email).
                header("sessionID", sessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(@Context HttpHeaders headers,
                                 @PathParam("movieid") String movieid)
    {
        ServiceLogger.LOGGER.info("Received request to get movie with id: " + movieid);

        GetMovieResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        responseModel = MovieRecords.getMovieByIdInDb(movieid, email);

        if (responseModel.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID)
                    .build();
        }
        return Response.status(Status.OK).
                header("email", email).
                header("sessionID", sessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovie(@Context HttpHeaders headers, String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to add movie.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        AddMovieRequestModel requestModel;
        AddMovieResponseModel responseModel;
        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("email: " + email);
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("1");


        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, AddMovieRequestModel.class);
            requestModel.setEmail(email);

            responseModel = MovieRecords.addMovieToDb(requestModel);
            if (responseModel.getResultCode() == -1) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).
                        header("email", email).
                        header("sessionID", sessionID).
                        header("transactionID", transactionID)
                        .build();
            }
            return Response.status(Status.OK).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieById(@Context HttpHeaders headers,
                                    @PathParam("movieid") String movieid)
    {
        ServiceLogger.LOGGER.info("Received request to delete movie with id: " + movieid);

        ResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        responseModel = MovieRecords.deleteMovieFromDb(movieid, email);

        if (responseModel.getResultCode() == -1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID)
                    .build();
        }
        return Response.status(Status.OK).
                header("email", email).
                header("sessionID", sessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();

    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRating(@Context HttpHeaders headers, String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to update rating.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        UpdateMovieRatingRequestModel requestModel;
        ResponseModel responseModel;
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, UpdateMovieRatingRequestModel.class);
            requestModel.email = email;

            responseModel = MovieRecords.updateMovieRatingToDb(requestModel);
            if (responseModel.getResultCode() == -1) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).
                        header("email", email).
                        header("sessionID", sessionID).
                        header("transactionID", transactionID)
                        .build();
            }
            return Response.status(Status.OK).
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
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
        } else if (e instanceof JsonParseException) {
            ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-3, validate.caseMessage(-3))).build();
        }
        ServiceLogger.LOGGER.info("IOException");
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
    }
}
