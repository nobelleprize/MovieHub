package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarModel {
    private String id;
    private String name;
    private Integer birthYear;

    @JsonCreator
    public StarModel(@JsonProperty(value = "id", required = true) String id,
                     @JsonProperty(value = "name", required = true) String name,
                     @JsonProperty(value = "birthYear") Integer birthYear)
    {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
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
