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

public class MovieRecords {
    public static SearchMoviesResponseModel searchMovieInDb(SearchMoviesRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Searching for movie in database...");
        try {
            boolean first = true;
            String titleQuery = "";
            String genreQuery = "";
            String yearQuery = "";
            String directorQuery = "";
            boolean allowed = isUserAllowedToMakeRequest(requestModel.getEmail(), 4);

            String query = "SELECT DISTINCT movies.title, movies.director, movies.year, movies.backdrop_path, " +
                    "movies.budget, movies.overview, " +
                    "movies.poster_path, movies.revenue, ratings.rating, ratings.numVotes, movies.hidden, " +
                    "genres_in_movies.movieId " +
                    "FROM movies, genres, genres_in_movies, ratings WHERE ";
            if (requestModel.getTitle() != null && requestModel.getTitle().length() > 0) {
                if (!first) {
                    titleQuery = "AND movies.title LIKE '%" + requestModel.getTitle() + "%'";
                }
                else {
                    titleQuery = " movies.title LIKE '%" + requestModel.getTitle() + "%'";
                    first = false;

                }
            }
            if (requestModel.getGenre() != null && requestModel.getGenre().length() > 0) {
                if (!first) {
                    genreQuery = " AND genres.name = '" + requestModel.getGenre() + "'";
                }
                else {
                    genreQuery = " genres.name = '" + requestModel.getGenre() + "'";
                    first = false;
                }
            }
            if (requestModel.getYear() > 0) {
                if (!first) {
                    yearQuery = " AND movies.year = " + requestModel.getYear();
                }
                else {
                    yearQuery = " movies.year = " + requestModel.getYear();
                    first = false;
                }
            }
            if (requestModel.getDirector() != null && requestModel.getDirector().length() > 0) {
                if (!first) {
                    directorQuery = " AND movies.director = '" + requestModel.getDirector() + "'";
                }
                else {
                    directorQuery = " movies.director = '" + requestModel.getDirector() + "'";
                    first = false;
                }
            }
            query += titleQuery + genreQuery + yearQuery + directorQuery;

            if (allowed) {
                if (!requestModel.getHidden()) {
                    if (!first)
                    {
                        query += " AND hidden = 0";
                    }
                    else
                    {
                        query += " hidden = 0";
                    }
                }
            }
            else {
                if (!first)
                {
                    query += " AND hidden = 0";
                }
                else
                {
                    query += " hidden = 0";
                }
            }

            query += " AND genres.id = genres_in_movies.genreId AND movies.id = genres_in_movies.movieId AND movies.id = ratings.movieId";
            if (requestModel.getOrderby().equals("rating")) {
                query += " ORDER BY " + requestModel.getOrderby() + " " + requestModel.getDirection() +
                        ", title asc LIMIT " + requestModel.getLimit() +
                        " OFFSET " + requestModel.getOffset();
            }
            else {
                query += " ORDER BY " + requestModel.getOrderby() + " " +
                        requestModel.getDirection()  + " LIMIT " + requestModel.getLimit() +
                        " OFFSET " + requestModel.getOffset();
            }

            PreparedStatement statement = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Executing query: " + statement.toString());
            ResultSet rs = statement.executeQuery();
            ServiceLogger.LOGGER.info("Query successful.");
            ArrayList<MovieModel> movies = new ArrayList<>();

            boolean found = false;
            while (rs.next()) {
                Boolean hidden = null;
                found = true;

                String movieId = rs.getString("movieId");
                String title = rs.getString("title");
                String director = rs.getString("director");
                Integer year = rs.getInt("year");
                Float rating;
                if (rs.getFloat("rating") == 0)
                {
                    rating = null;
                }
                else
                {
                    rating = rs.getFloat("rating");
                }
                Integer numVotes;
                if (rs.getInt("numVotes") == 0)
                {
                    numVotes = null;
                }
                else
                {
                    numVotes = rs.getInt("numVotes");
                }
                int hiddenVal = rs.getInt("hidden");

                if (allowed) {
                    if (hiddenVal == 0) {
                        hidden = false;
                    }
                    else {
                        hidden = true;
                    }
                }
                MovieModel movie = new MovieModel(movieId, title, director, year, rating, numVotes, hidden);
                movies.add(movie);


            }
            if (!found) {
                ServiceLogger.LOGGER.info("No movies found.");
                return new SearchMoviesResponseModel(211, validate.caseMessage(211), null);
            }

            MovieModel[] result = new MovieModel[movies.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = movies.get(i);
            }
            return new SearchMoviesResponseModel(210, validate.caseMessage(210), result);
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("SQL Exception.");
            e.printStackTrace();
            return new SearchMoviesResponseModel(-1, validate.caseMessage(-1), null);
        }
    }

    public static GetMovieResponseModel getMovieByIdInDb(String movieId, String email) {
        ServiceLogger.LOGGER.info("Searching for movie by ID in database...");
        try {
            String query = "SELECT * FROM movies, ratings WHERE ratings.movieId = movies.id AND movies.id = '" + movieId + "'";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
//            ps.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + query);
            ResultSet rs = ps.executeQuery();

            ServiceLogger.LOGGER.info("Query succeded.");

            ArrayList<GenreModel> genreList = new ArrayList<>();
            ArrayList<StarModel> starList = new ArrayList<>();
            if (rs.next()) {
                if (rs.getString("hidden").equals("true'") && !isUserAllowedToMakeRequest(email, 4)) {
                    return new GetMovieResponseModel(141, validate.caseMessage(141), null);
                }

                Boolean allowed = null;
                if (isUserAllowedToMakeRequest(email, 4)) {
                    allowed = rs.getBoolean("hidden");

                }
                ServiceLogger.LOGGER.info("ALLOWED: " + allowed);

                MovieModel m = new MovieModel(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getString("backdrop_path"),
                        rs.getInt("budget"),
                        rs.getString("overview"),
                        rs.getString("poster_path"),
                        rs.getInt("revenue"),
                        rs.getFloat("rating"),
                        rs.getInt("numVotes"),
                        allowed, null, null);

                String buildGenre;
                PreparedStatement buildGenrePS;


                buildGenre = "SELECT movieId, genreId, name FROM genres, genres_in_movies " +
                        "WHERE genres.id = genres_in_movies.genreId AND " +
                        "genres_in_movies.movieId = '" + rs.getString("movieId") + "'";

                buildGenrePS = MovieService.getCon().prepareStatement(buildGenre);
                ServiceLogger.LOGGER.info("Trying query: " + buildGenrePS.toString());
                ResultSet buildGenreRS = buildGenrePS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");

                while (buildGenreRS.next()) {
                    String id = buildGenreRS.getString("movieId");
                    Integer genreId = buildGenreRS.getInt("genreId");
                    String genreName = buildGenreRS.getString("name");

                    GenreModel gm = new GenreModel(genreId, genreName);
                    genreList.add(gm);
                }

                GenreModel[] gms = new GenreModel[genreList.size()];
                for (int j = 0; j < gms.length; j++) {
//                    ArrayList<GenreModel> genremodel = result.get(movies.get(i).getId());
                    GenreModel gm = genreList.get(j);
                    gms[j] = gm;
                }

                String buildStar = "SELECT movieId, starId, name FROM stars, stars_in_movies " +
                        "WHERE stars.id = stars_in_movies.starId AND stars_in_movies.movieId = '" +
                        rs.getString("movieId") + "'";
                PreparedStatement buildStarPS = MovieService.getCon().prepareStatement(buildStar);
                ServiceLogger.LOGGER.info("Trying query: " + buildStarPS.toString());
                ResultSet buildStarRS = buildStarPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");
                while (buildStarRS.next()) {
                    String id = buildStarRS.getString("movieId");
                    String starId = buildStarRS.getString("starId");
                    String name = buildStarRS.getString("name");

                    StarModel sm = new StarModel(starId, name, null);
                    starList.add(sm);
                }

                StarModel[] sms = new StarModel[starList.size()];
                for (int j = 0; j < sms.length; j++) {
//                    ArrayList<StarModel> starmodel = starMap.get(movies.get(i).getId());
                    StarModel sm = starList.get(j);
                    sms[j] = sm;
                }
                m.setGenres(gms);
                m.setStars(sms);

                return new GetMovieResponseModel(210, validate.caseMessage(210), m);

            } else {
                return new GetMovieResponseModel(211, validate.caseMessage(211), null);
            }


        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to search for movie");
            e.printStackTrace();
        }

        return new GetMovieResponseModel(-1, validate.caseMessage(-1), null);
    }

    public static AddMovieResponseModel addMovieToDb(AddMovieRequestModel requestModel)
    {
        if (!isUserAllowedToMakeRequest(requestModel.getEmail(), 3))
        {
            ServiceLogger.LOGGER.info("User has insufficient privilege");
            return new AddMovieResponseModel(141, validate.caseMessage(141), null, null);
        }
        try
        {
            ServiceLogger.LOGGER.info("Adding movie to database...");
            String query = "SELECT * FROM movies WHERE movies.title = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getTitle());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs.next())
            {
                if (!rs.getBoolean("hidden"))
                {
                    return new AddMovieResponseModel(216, validate.caseMessage(216), null, null);
                }
            }

            String csSearch = "SELECT * FROM movies WHERE movies.id LIKE 'cs%' ORDER BY movies.id DESC";
            PreparedStatement csSearchPS = MovieService.getCon().prepareStatement(csSearch);
            ServiceLogger.LOGGER.info("Trying query: " + csSearchPS.toString());
            ResultSet csSearchRS = csSearchPS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            String newMovieID = "cs";

            if (csSearchRS.next())
            {
                String csString = csSearchRS.getString("id").substring(7);
                ServiceLogger.LOGGER.info("csString: " + csString);
                int csNum = Integer.parseInt(csString);
                ServiceLogger.LOGGER.info("csNum: " + csNum);

                csNum++;
                int offset = 7 - String.valueOf(csNum).length();
                for (int i = 0; i < offset; i++)
                {
                    newMovieID += "0";
                }
                newMovieID += String.valueOf(csNum);
            }
            else
            {
                newMovieID = "cs0000001";
            }

            String insert = "INSERT INTO movies (id, title, year, director, backdrop_path, budget, overview, " +
                    "poster_path, revenue, hidden) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPS = MovieService.getCon().prepareStatement(insert);

            insertPS.setString(1, newMovieID);
            insertPS.setString(2, requestModel.getTitle());
            insertPS.setInt(3, requestModel.getYear());
            insertPS.setString(4, requestModel.getDirector());
            insertPS.setString(5, requestModel.getBackdrop_path());
            insertPS.setInt(6, requestModel.getBudget());
            insertPS.setString(7, requestModel.getOverview());
            insertPS.setString(8, requestModel.getPoster_path());
            insertPS.setInt(9, requestModel.getRevenue());
            insertPS.setBoolean(10, false);

            ServiceLogger.LOGGER.info("Trying query: " + insertPS.toString());
            insertPS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            String insertRating = "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?, ?, ?)";
            PreparedStatement insertRatingPS = MovieService.getCon().prepareStatement(insertRating);

            insertRatingPS.setString(1, newMovieID);
            insertRatingPS.setFloat(2, (float) 0);
            insertRatingPS.setInt(3, 0);
            ServiceLogger.LOGGER.info("Trying query: " + insertRatingPS.toString());
            insertRatingPS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");


            Integer[] genreIDs = new Integer[requestModel.getGenres().length];

            for (int i = 0; i < requestModel.getGenres().length; i++)
            {
                String genreSearch = "SELECT * FROM genres WHERE genres.name = ?";
                PreparedStatement genreSearchPS = MovieService.getCon().prepareStatement(genreSearch);
                genreSearchPS.setString(1, requestModel.getGenres()[i].getName());
                ServiceLogger.LOGGER.info("Trying query: " + genreSearchPS.toString());
                ResultSet genreSearchRS  = genreSearchPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");

                if (!genreSearchRS.next())
                {
                    ServiceLogger.LOGGER.info("Query succeeded.");
                    String insertID = "INSERT INTO genres (name) VALUES (?)";
                    PreparedStatement insertIDPS = MovieService.getCon().prepareStatement(insertID);
                    insertIDPS.setString(1, requestModel.getGenres()[i].getName());
                    ServiceLogger.LOGGER.info("Trying query: " + insertIDPS.toString());
                    insertIDPS.execute();
                    ServiceLogger.LOGGER.info("Query succeeded.");
                }

                String getNewID = "SELECT * FROM genres WHERE genres.name = ?";
                PreparedStatement getNewIDPS = MovieService.getCon().prepareStatement(getNewID);
                getNewIDPS.setString(1, requestModel.getGenres()[i].getName());
                ServiceLogger.LOGGER.info("Trying query: " + getNewIDPS.toString());
                ResultSet getNewIDRS = getNewIDPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");

                int genreID = 0;
                if (getNewIDRS.next())
                {
                    genreID = getNewIDRS.getInt("id");
                    ServiceLogger.LOGGER.info("genreID: " + genreID);
                }

                String addToGiM = "INSERT INTO genres_in_movies (genreId, movieID) VALUES (?, ?)";
                PreparedStatement addToGiMPS = MovieService.getCon().prepareStatement(addToGiM);
                addToGiMPS.setInt(1, genreID);
                addToGiMPS.setString(2, newMovieID);
                ServiceLogger.LOGGER.info("Trying query: " + addToGiMPS.toString());
                addToGiMPS.execute();
                ServiceLogger.LOGGER.info("Query succeeded.");


                String genreIdQ = "SELECT * FROM genres WHERE genres.name = ?";
                PreparedStatement getGenreIDsPS = MovieService.getCon().prepareStatement(genreIdQ);
                getGenreIDsPS.setString(1, requestModel.getGenres()[i].getName());
                ServiceLogger.LOGGER.info("Trying query: " + getGenreIDsPS.toString());
                ResultSet getGenresRS = getGenreIDsPS.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");
                if (getGenresRS.next())
                {
                    genreIDs[i] = getGenresRS.getInt("id");
                }
            }
            return new AddMovieResponseModel(214, validate.caseMessage(214), newMovieID, genreIDs);
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to retrieve movie " + requestModel.getTitle());
            e.printStackTrace();
        }
        return new AddMovieResponseModel(215, validate.caseMessage(215), null, null);
    }

    public static ResponseModel deleteMovieFromDb(String movieId, String email)
    {
        if (!isUserAllowedToMakeRequest(email, 3))
        {
            ServiceLogger.LOGGER.info("User has insufficient privilege");
            return new ResponseModel(141, validate.caseMessage(141));
        }
        ServiceLogger.LOGGER.info("Searching for movie in database...");
        try
        {
            String check = "SELECT * FROM movies WHERE movies.id = ?";
            PreparedStatement checkPS = MovieService.getCon().prepareStatement(check);
            checkPS.setString(1, movieId);
            ServiceLogger.LOGGER.info("Trying query: " + checkPS.toString());
            ResultSet checkRS = checkPS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (checkRS.next())
            {
                ServiceLogger.LOGGER.info("BOOLEAN: " + checkRS.getInt("hidden"));
                if (!checkRS.getBoolean("hidden"))
                {
                    ServiceLogger.LOGGER.info("Removing movie...");
                    String remove = "UPDATE movies SET hidden = " + "TRUE WHERE id = ?";
                    PreparedStatement ps = MovieService.getCon().prepareStatement(remove);
                    ps.setString(1, movieId);
                    ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                    int rs = ps.executeUpdate();
                    ServiceLogger.LOGGER.info("Query succeeded.");
                    ServiceLogger.LOGGER.info("Removing movie from database.");
                    return new ResponseModel(240, validate.caseMessage(240));
                }
                else
                {
                    ServiceLogger.LOGGER.info("Could not remove movie from database.");
                    return new ResponseModel(242, validate.caseMessage(242));
                }
            }
            else
            {
                return new ResponseModel(241, validate.caseMessage(241));
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to remove movie " + movieId);
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));

    }

    public static ResponseModel updateMovieRatingToDb(UpdateMovieRatingRequestModel requestModel)
    {
        try
        {
            String select = "SELECT * FROM ratings WHERE ratings.movieId = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(select);
            ps.setString(1, requestModel.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet selectRS = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");


            if (selectRS.next())
            {
                float prevRating = selectRS.getFloat("rating");
                int numVotes = selectRS.getInt("numVotes");
                float newRating =  (float) (((prevRating * numVotes) + requestModel.getRating()) / (numVotes + 1));
                String update = "UPDATE ratings SET rating = ? , numVotes = ? WHERE ratings.movieId = ?";
                PreparedStatement updatePS = MovieService.getCon().prepareStatement(update);
                updatePS.setFloat(1, newRating);
                updatePS.setInt(2, numVotes + 1);
                updatePS.setString(3, requestModel.getId());
                updatePS.executeUpdate();
                return new ResponseModel(250, validate.caseMessage(250));
            }
            else
            {
                return new ResponseModel(211, validate.caseMessage(211));
            }

        }
        catch (SQLException e)
        {
        ServiceLogger.LOGGER.warning("Unable to update movie rating " + requestModel.getId());
        e.printStackTrace();
        }
        return new ResponseModel(251, validate.caseMessage(251));
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
