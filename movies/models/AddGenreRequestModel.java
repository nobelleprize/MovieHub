package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddGenreRequestModel {
    public String email;

    private String name;

    @JsonCreator
    public AddGenreRequestModel(@JsonProperty(value = "email") String email,
                                @JsonProperty(value = "name", required = true) String name) {
        this.email = email;
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
