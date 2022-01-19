package service.movies.core;

import service.movies.MovieService;
import service.movies.logger.ServiceLogger;
import service.movies.models.*;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GenreRecords {
    public static GetGenresResponseModel getGenresFromDb(String email)
    {
        try
        {
            ServiceLogger.LOGGER.info("Getting genres from database...");
            String query = "SELECT * FROM genres";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            ArrayList<GenreModel> genres = new ArrayList<>();
            while (rs.next())
            {
                GenreModel gm = new GenreModel(rs.getInt("id"), rs.getString("name"));
                if (!genres.contains(gm))
                {
                    genres.add(gm);
                }
            }
            GenreModel[] result = new GenreModel[genres.size()];
            for (int i = 0; i < genres.size(); i++)
            {
                result[i] = genres.get(i);
            }
            return new GetGenresResponseModel(219, validate.caseMessage(219), result);
        }
        catch (SQLException e) {
        ServiceLogger.LOGGER.warning("Unable to get genres.");
        e.printStackTrace();
    }
        return new GetGenresResponseModel(-1, validate.caseMessage(-1), null);

}
    public static ResponseModel addGenreToDb(AddGenreRequestModel requestModel)
    {
        if (!isUserAllowedToMakeRequest(requestModel.getEmail(), 3))
        {
            ServiceLogger.LOGGER.info("User has insufficient privilege");
            return new ResponseModel(141, validate.caseMessage(141));
        }
        try
        {
            String query = "INSERT INTO genres (name) VALUES (?)";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getName());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded");
            return new ResponseModel(217, validate.caseMessage(217));

        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to get genres.");
            e.printStackTrace();
        }
        return new ResponseModel(218, validate.caseMessage(218));
    }

    public static GetGenresByMovieResponseModel getGenresByMovieFromDb(String movieId, String email)
    {
        try
        {
            String query = "SELECT * FROM movies WHERE movies.id = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");
            if (rs.next())
            {
                ArrayList<Integer> genreIDs = new ArrayList<>();
                ArrayList<GenreModel> genres = new ArrayList<>();
                GenreModel[] result = new GenreModel[genres.size()];
                String genreSearch = "SELECT * FROM genres_in_movies WHERE movieId = ?";
                PreparedStatement genreSearchPS = MovieService.getCon().prepareStatement(genreSearch);
                genreSearchPS.setString(1, movieId);
                ServiceLogger.LOGGER.info("Trying query: " + genreSearchPS.toString());
                ResultSet genreSearchRS = genreSearchPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded");

                while (genreSearchRS.next())
                {
                    if (!genreIDs.contains(genreSearchRS.getInt("genreId")))
                    {
                        genreIDs.add(genreSearchRS.getInt("genreId"));
                    }
                }
                for (int i = 0; i < genreIDs.size(); i++)
                {
                    String genresTable = "SELECT * FROM genres WHERE genres.id = ?";
                    PreparedStatement genresTablePS = MovieService.getCon().prepareStatement(genresTable);
                    genresTablePS.setInt(1, genreIDs.get(i));
                    ServiceLogger.LOGGER.info("Trying query: " + genresTablePS.toString());
                    ResultSet genresTableRS = genresTablePS.executeQuery();
                    ServiceLogger.LOGGER.info("Query succeeded");
                    if (genresTableRS.next())
                    {
                        GenreModel gm = new GenreModel(genreIDs.get(i), genresTableRS.getString("name"));
                        if (!genres.contains(gm))
                        {
                            genres.add(gm);
                        }
                        result = new GenreModel[genres.size()];
                        for (int j = 0; j < genres.size(); j++)
                        {
                            result[j] = genres.get(j);
                        }
                    }
                }
                return new GetGenresByMovieResponseModel(219, validate.caseMessage(219), result);
            }
            else
            {
                return new GetGenresByMovieResponseModel(211, validate.caseMessage(211), null);
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to get genres.");
            e.printStackTrace();
        }
        return new GetGenresByMovieResponseModel(-1, validate.caseMessage(-1), null);
    }


    public static boolean isUserAllowedToMakeRequest(String email, int plevel)
    {
        ServiceLogger.LOGGER.info("Verifying privilege level with IDM...");

        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = MovieService.getIdmConfigs().getIdmUri();
        ServiceLogger.LOGGER.info("*******URI: " + IDM_URI);

        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = MovieService.getIdmConfigs().getPrivilegePath();

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");

        // Check that status code of the request
        if (response.getStatus() == 200) {
            ServiceLogger.LOGGER.info("Received Status 200");
            // Success! Map the response to a ResponseModel
//            VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);
            String jsonText = response.readEntity(String.class);
            ServiceLogger.LOGGER.info("JsonText: " + jsonText);
            // TODO
            if (jsonText.contains("140")) {
                return true;
            }
            return false;
        } else {
            ServiceLogger.LOGGER.info("Received Status " + response.getStatus() + " -- you lose.");
        }
        return false;
    }

}
