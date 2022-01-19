package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMoviesRequestModel {
    private String email;

    private String title;
    private String genre;
    private Integer year;
    private String director;
    private Boolean hidden;
    private Integer limit;
    private Integer offset;
    private String orderby;
    private String direction;

    @JsonCreator
    public SearchMoviesRequestModel(@JsonProperty(value = "email", required = true) String email,
                                    @JsonProperty(value = "title") String title,
                                    @JsonProperty(value = "genre") String genre,
                                    @JsonProperty(value = "year") Integer year,
                                    @JsonProperty(value = "director") String director,
                                    @JsonProperty(value = "hidden") Boolean hidden,
                                    @JsonProperty(value = "limit") Integer limit,
                                    @JsonProperty(value = "offset") Integer offset,
                                    @JsonProperty(value = "orderby") String orderby,
                                    @JsonProperty(value = "direction") String direction) {
        this.email = email;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.hidden = hidden;
        this.limit = (limit == null || limit <= 0) ? 50 : limit;
        this.offset = (offset == null || offset < 0) ? 0 : offset;
        this.orderby = (orderby == null) ? "rating" : orderby;
        this.direction = (direction == null || (!direction.equals("desc") && !direction.equals("asc"))) ? "desc" : direction;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("genre")
    public String getGenre() {
        return genre;
    }

    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty("director")
    public String getDirector() {
        return director;
    }

    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("orderBy")
    public String getOrderby() {
        return orderby;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "SearchMoviesRequestModel{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", hidden=" + hidden +
                ", limit=" + limit +
                ", offset=" + offset +
                ", orderby='" + orderby + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
