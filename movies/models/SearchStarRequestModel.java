package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarRequestModel {
    private String email;

    private String name;
    private Integer birthYear;
    private String movieTitle;
    private Integer limit;
    private Integer offset;
    private String orderby;
    private String direction;

    @JsonCreator
    public SearchStarRequestModel(@JsonProperty(value = "email", required = true) String email,
                                  @JsonProperty(value = "name") String name,
                                  @JsonProperty(value = "birthYear") Integer birthYear,
                                  @JsonProperty(value = "movieTitle") String movieTitle,
                                  @JsonProperty(value = "limit") Integer limit,
                                  @JsonProperty(value = "offset") Integer offset,
                                  @JsonProperty(value = "orderBy") String orderby,
                                  @JsonProperty(value = "direction") String direction) {
        this.email = email;
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = (limit == null || limit <= 0) ? 10 : limit;
        this.offset = (offset == null || offset < 0) ? 0 : offset;
        this.orderby = (orderby == null) ? "name" : orderby;
        this.direction = (direction == null) ? "asc" : direction;
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

    @JsonProperty("movieTitle")
    public String getMovieTitle() {
        return movieTitle;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("orderby")
    public String getOrderby() {
        return orderby;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }
}
