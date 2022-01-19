package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteMovieRequestModel {
    private String email;
    private String sessionID;

    private String movieId;

    @JsonCreator
    public DeleteMovieRequestModel(@JsonProperty(value = "email") String email,
                                   @JsonProperty(value = "sessionID") String sessionID,
                                   @JsonProperty(value = "movieId", required = true) String ID) {
        this.email = email;
        this.sessionID = sessionID;
        this.movieId = ID;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

    @JsonProperty("movieId")
    public String getID() {
        return movieId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
