package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStarToMovieRequestModel {
    public String email;

    private String starid;
    private String movieid;

    @JsonCreator
    public AddStarToMovieRequestModel(@JsonProperty(value = "email") String email,
                                      @JsonProperty(value = "starid", required = true) String starid,
                                      @JsonProperty(value = "movieid", required = true) String movieid) {
        this.email = email;
        this.starid = starid;
        this.movieid = movieid;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("starid")
    public String getStarid() {
        return starid;
    }

    @JsonProperty("movieid")
    public String getMovieid() {
        return movieid;
    }
}
