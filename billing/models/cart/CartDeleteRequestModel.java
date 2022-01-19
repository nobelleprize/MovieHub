package service.billing.models.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDeleteRequestModel {
    private String email;
    private String movieId;

    @JsonCreator
    public CartDeleteRequestModel(@JsonProperty(value = "email", required = true) String email,
                                  @JsonProperty(value = "movieId", required = true) String movieId)
    {
        this.email = email;
        this.movieId = movieId;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "CartDeleteRequestModel{" +
                "email='" + email + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }
}
