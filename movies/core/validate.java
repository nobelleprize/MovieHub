package service.movies.core;

public class validate {
    public static String caseMessage(int resultCode)
    {
        if (resultCode == -3)
        {
            return "JSON parse exception.";
        }
        else if (resultCode == -2)
        {
            return "JSON mapping exception.";
        }
        else if (resultCode == -1)
        {
            return "Internal server error.";
        }
        else if (resultCode == 141)
        {
            return "User has insufficient privilege";
        }
        else if (resultCode == 210)
        {
            return "Found movies with search parameters.";
        }
        else if (resultCode == 211)
        {
            return "No movies found with search parameters.";
        }
        else if (resultCode == 212)
        {
            return "Found stars with search parameters";
        }
        else if (resultCode == 213)
        {
            return "No stars found with search parameters.";
        }
        else if (resultCode == 214)
        {
            return "Movie successfully added.";
        }
        else if (resultCode == 215)
        {
            return "Could not add movie.";
        }
        else if (resultCode == 216)
        {
            return "Movie already exists.";
        }
        else if (resultCode == 217)
        {
            return "Genre successfully added.";
        }
        else if (resultCode == 218)
        {
            return "Genre could not be added.";
        }
        else if (resultCode == 219)
        {
            return "Genres successfully retrieved";
        }
        else if (resultCode == 220)
        {
            return "Star successfully added.";
        }
        else if (resultCode == 221)
        {
            return "Could not add star.";
        }
        else if (resultCode == 222)
        {
            return "Star already exists.";
        }
        else if (resultCode == 230)
        {
            return "Star successfully added to movie.";
        }
        else if (resultCode == 231)
        {
            return "Could not add star to movie.";
        }
        else if (resultCode == 232)
        {
            return "Star already exists in movie.";
        }
        else if (resultCode == 240)
        {
            return "Movie successfully removed.";
        }
        else if (resultCode == 241)
        {
            return "Could not remove movie.";
        }
        else if (resultCode == 242)
        {
            return "Movie has been already removed.";
        }
        else if (resultCode == 250)
        {
            return "Rating successfully updated.";
        }
        else if (resultCode == 251)
        {
            return "Could not update rating.";
        }

        // resultCode == -1
        return "NOT IMPLEMENTED";
    }
}
