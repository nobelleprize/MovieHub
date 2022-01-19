package service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestModel {

    private String email;
    private char[] password;

    @JsonCreator
    public RegisterRequestModel(@JsonProperty(value = "email") String email,
                                @JsonProperty(value = "password") char[] password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("password")
    public char[] getPassword() {
        return password;
    }
}
