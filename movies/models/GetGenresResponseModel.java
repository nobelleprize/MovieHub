package service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGenresResponseModel {
    private Integer resultCode;
    private String message;
    private GenreModel[] genres;

    public GetGenresResponseModel(Integer resultCode, String message, GenreModel[] genres) {
        this.resultCode = resultCode;
        this.message = message;
        this.genres = genres;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public GenreModel[] getGenres() {
        return genres;
    }
}
