package service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMoviesResponseModel {
    private Integer resultCode;
    private String message;
    private MovieModel[] movies;

    public SearchMoviesResponseModel(Integer resultCode, String message, MovieModel[] movies) {
        this.resultCode = resultCode;
        this.message = message;
        this.movies = movies;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public MovieModel[] getMovies() {
        return movies;
    }
}
