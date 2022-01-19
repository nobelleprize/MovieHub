package service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class RegisterRequestModel {
    private String email;
    private char[] password;

    @JsonCreator
    public RegisterRequestModel(@JsonProperty(value = "email", required = true) String email,
                                @JsonProperty(value = "password", required = true) char[] password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail()
    {
        return email;
    }

    @JsonProperty("password")
    public char[] getPassword()
    {
        return password;
    }

    @Override
    public String toString() {
        return "RegisterRequestModel{" +
                "email='" + email + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }
}
