package service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMovieResponseModel {
    private Integer resultCode;
    private String message;
    private MovieModel movie;

    public GetMovieResponseModel(Integer resultCode, String message, MovieModel movie) {
        this.resultCode = resultCode;
        this.message = message;
        this.movie = movie;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public MovieModel getMovie() {
        return movie;
    }
}
