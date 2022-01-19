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

public class StarRecords {
    public static SearchStarResponseModel searchStarInDb(SearchStarRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Searching MovieDatabase for Stars");
        try {
            String query = "SELECT DISTINCT starId, name, birthYear FROM stars, stars_in_movies, movies WHERE ";
            boolean first = true;
            String nameQuery = "";
            String yearQuery = "";
            String titleQuery = "";

            if (requestModel.getName() != null && requestModel.getName().length() > 0) {
                if (!first) {
                    nameQuery =  " AND stars.name LIKE '%" + requestModel.getName() + "%'";
                }
                else {
                    nameQuery =  " stars.name LIKE '%" + requestModel.getName() + "%'";

                    first = false;
                }
            }
            if (requestModel.getBirthYear() != null && requestModel.getBirthYear() > 0) {
                if (!first) {
                    yearQuery = " AND stars.birthYear = " + requestModel.getBirthYear();
                }
                else {
                    yearQuery = " stars.birthYear = " + requestModel.getBirthYear();

                    first = false;
                }
            }
            if (requestModel.getMovieTitle() != null && requestModel.getMovieTitle().length() > 0) {
                if (!first) {
                    titleQuery = " AND movies.title LIKE '%" + requestModel.getMovieTitle() + "%'";
                }
                else {
                    titleQuery = " movies.title LIKE '%" + requestModel.getMovieTitle() + "%'";
                    first = false;
                }
            }
            query += nameQuery + yearQuery + titleQuery;
            query += " AND stars.id = stars_in_movies.starId AND movies.id = stars_in_movies.movieId";
            String orderQuery;
            if (requestModel.getOrderby().equals("name")) {
                orderQuery = " ORDER BY " + requestModel.getOrderby() + " " + requestModel.getDirection() +
                        ", birthYear asc LIMIT " + requestModel.getLimit() +
                        " OFFSET " + requestModel.getOffset();
            }
            else {
                orderQuery = " ORDER BY " + requestModel.getOrderby() + " " +
                        requestModel.getDirection()  + " LIMIT " + requestModel.getLimit() +
                        " OFFSET " + requestModel.getOffset();
            }
            query += orderQuery;
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Executing query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ArrayList<StarModel> stars = new ArrayList<>();

            boolean found = false;
            while (rs.next()) {
                found = true;

                StarModel star = new StarModel(
                        rs.getString("starId"),
                        rs.getString("name"),
                        rs.getInt("birthYear"));
                stars.add(star);
            }
            if (!found) {
                ServiceLogger.LOGGER.info("No stars found.");
                return new SearchStarResponseModel(213, validate.caseMessage(213), null);
            }
            StarModel[] result = new StarModel[stars.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = stars.get(i);
            }

            return new SearchStarResponseModel(212, validate.caseMessage(212), result);
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("SQL Exception.");
            e.printStackTrace();
            return new SearchStarResponseModel(-1, validate.caseMessage(-1), null);
        }
    }

    public static GetStarResponseModel getStarByIdInDb(String starId, String email)
    {
        ServiceLogger.LOGGER.info("Getting Star from Movie Database.");
        try {
            String query = "SELECT * FROM stars s WHERE s.id = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starId);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                int birthYear = rs.getInt("birthYear");
                StarModel starModel = new StarModel(starId, name, birthYear);
                return new GetStarResponseModel(212, validate.caseMessage(212), starModel);
            }
            else {
                return new GetStarResponseModel(213, validate.caseMessage(213), null);
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("SQL Exception.");
            e.printStackTrace();
            return new GetStarResponseModel(-1, "Internal server error.", null);
        }
    }
    public static ResponseModel addStarToDb(AddStarRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Adding star to database....");
        if (!isUserAllowedToMakeRequest(requestModel.getEmail(), 3)) {
            return new ResponseModel(141, validate.caseMessage(141));
        }
        try {

            String query = "SELECT * FROM stars WHERE stars.name = ? AND stars.birthYear = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getName());
            ps.setInt(2, requestModel.getBirthYear());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            if (!found) {
                ServiceLogger.LOGGER.info("Star not found");

                String newStarID = "ss";

                String findNextStarQuery = "SELECT * FROM stars WHERE id LIKE 'ss%' ORDER BY id DESC LIMIT 1";
                PreparedStatement findNextStarPS = MovieService.getCon().prepareStatement(findNextStarQuery);
                ServiceLogger.LOGGER.info("Trying query: " + findNextStarPS.toString());
                ResultSet nextStar = findNextStarPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");

                if (nextStar.next())
                {
                    String starString = nextStar.getString("id").substring(7);
                    int starNum = Integer.parseInt(starString);
                    starNum++;
                    int offset = 7 - String.valueOf(starNum).length();
                    for (int i = 0; i < offset; i++)
                    {
                        newStarID += "0";
                    }
                    newStarID += String.valueOf(starNum);
                }
                else
                {
                    newStarID = "ss0000001";
                }


                String insertQuery = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";
                PreparedStatement is = MovieService.getCon().prepareStatement(insertQuery);
                Integer birthYear = requestModel.getBirthYear();
                if (requestModel.getBirthYear() > 2019) {
                    birthYear = 0;
                }
                is.setString(1, newStarID);
                is.setString(2, requestModel.getName());
                is.setInt(3, birthYear);
                ServiceLogger.LOGGER.info("Trying query: " + is.toString());
                is.execute();
                ServiceLogger.LOGGER.info("Query succeeded.");

                ServiceLogger.LOGGER.info("Successfully added star.");
                return new ResponseModel(220, validate.caseMessage(220));
            } else {
                ServiceLogger.LOGGER.info("Star exists.");
                return new ResponseModel(222, validate.caseMessage(222));
            }
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("SQL Exception.");
            e.printStackTrace();
            return new ResponseModel(221, validate.caseMessage(221));
        }
    }

    public static ResponseModel AddStarToMovieInDb(AddStarToMovieRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Adding star to movie...");
        if (!isUserAllowedToMakeRequest(requestModel.getEmail(), 3)) {
            return new ResponseModel(141, validate.caseMessage(141));
        }
        try {
            String checkMovieExists = "SELECT * FROM movies WHERE movies.id = ?";
            PreparedStatement searchMoviePS = MovieService.getCon().prepareStatement(checkMovieExists);
            searchMoviePS.setString(1, requestModel.getMovieid());
            ServiceLogger.LOGGER.info("Trying query: " + searchMoviePS.toString());
            ResultSet searchMovieRS = searchMoviePS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (!searchMovieRS.next()) {
                ServiceLogger.LOGGER.info("No movies found.");
                return new ResponseModel(211, validate.caseMessage(211));
            }

            String checkStarExists = "SELECT * FROM movies WHERE movies.id = ?";
            PreparedStatement searchStarPS = MovieService.getCon().prepareStatement(checkStarExists);
            searchStarPS.setString(1, requestModel.getMovieid());
            ServiceLogger.LOGGER.info("Trying query: " + searchStarPS.toString());
            ResultSet searchStarRS = searchStarPS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (!searchStarRS.next()) {
                ServiceLogger.LOGGER.info("No stars found.");
                return new ResponseModel(231, validate.caseMessage(231));
            }

            String checkStarInMovie = "SELECT * FROM stars_in_movies  WHERE stars_in_movies.starId = ? " +
                    "AND stars_in_movies.movieId = ?";
            PreparedStatement starInMoviePS = MovieService.getCon().prepareStatement(checkStarInMovie);
            starInMoviePS.setString(1, requestModel.getStarid());
            starInMoviePS.setString(2, requestModel.getMovieid());
            ServiceLogger.LOGGER.info("Trying query: " + starInMoviePS.toString());
            ResultSet starInMovieRS = starInMoviePS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (starInMovieRS.next()) {
                ServiceLogger.LOGGER.info("Star already in movie.");
                return new ResponseModel(232, validate.caseMessage(232));
            }

            String insertStarInMovie = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)";
            PreparedStatement insertPS = MovieService.getCon().prepareStatement(insertStarInMovie);
            insertPS.setString(1, requestModel.getStarid());
            insertPS.setString(2, requestModel.getMovieid());
            ServiceLogger.LOGGER.info("Trying query: " + insertPS.toString());
            insertPS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return new ResponseModel(230, validate.caseMessage(230));
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("SQL Exception.");
            e.printStackTrace();
            return new ResponseModel(231, validate.caseMessage(231));
        }
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
