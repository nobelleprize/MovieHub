package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStarRequestModel {
    public String email;

    private String name;
    private Integer birthYear;

    @JsonCreator
    public AddStarRequestModel(@JsonProperty(value = "email") String email,
                               @JsonProperty(value = "name", required = true) String name,
                               @JsonProperty(value = "birthYear") Integer birthYear) {
        this.email = email;
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }


}
