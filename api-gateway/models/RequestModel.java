package service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestModel extends Model {

    private String email;
    private String sessionID;

    public RequestModel() {
    }

    public RequestModel(@JsonProperty(value = "email") String email,
                        @JsonProperty(value = "sessionID") String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "email='" + email + '\'' +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }
}
