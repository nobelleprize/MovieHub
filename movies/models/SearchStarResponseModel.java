package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarResponseModel {
    private Integer resultCode;
    private String message;
    private StarModel[] stars;

    @JsonCreator
    public SearchStarResponseModel(@JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                   @JsonProperty(value = "message", required = true) String message,
                                   @JsonProperty(value = "stars", required = true) StarModel[] stars) {
        this.resultCode = resultCode;
        this.message = message;
        this.stars = stars;
    }

    @JsonProperty("resultCode")
    public Integer getResultCode() {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("stars")
    public StarModel[] getStars() {
        return stars;
    }
}
