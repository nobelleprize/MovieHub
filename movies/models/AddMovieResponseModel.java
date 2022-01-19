package service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieResponseModel {
    private Integer resultCode;
    private String message;
    private String movieid;
    private Integer[] genreid;

    public AddMovieResponseModel(Integer resultCode, String message, String movieid, Integer[] genreid) {
        this.resultCode = resultCode;
        this.message = message;
        this.movieid = movieid;
        this.genreid = genreid;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public String getMovieid() {
        return movieid;
    }

    public Integer[] getGenreid() {
        return genreid;
    }
}
