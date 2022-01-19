package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStarResponseModel {
    private Integer resultCode;
    private String message;
    private StarModel star;

    @JsonCreator
    public GetStarResponseModel(@JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                 @JsonProperty(value = "message", required = true) String message,
                                 @JsonProperty(value = "star", required = true) StarModel star) {
        this.resultCode = resultCode;
        this.message = message;
        this.star = star;
    }

    @JsonProperty("resultCode")
    public Integer getResultCode() {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("star")
    public StarModel getStar() {
        return star;
    }
}
