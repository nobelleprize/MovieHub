package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateMovieRatingRequestModel {
    public String email;
    private String id;
    private Double rating;

    @JsonCreator
    public UpdateMovieRatingRequestModel(@JsonProperty(value = "email") String email,
                                         @JsonProperty(value = "id", required = true) String id,
                                         @JsonProperty(value = "rating", required = true) Double rating) {
        this.email = email;
        this.id = id;
        this.rating = rating;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("rating")
    public Double getRating() {
        return rating;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
