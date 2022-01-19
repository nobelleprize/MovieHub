package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieRequestModel {

    private String email;

    private String title;
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private GenreModel[] genres;

    @JsonCreator
    public AddMovieRequestModel(@JsonProperty(value = "email") String email,
                                @JsonProperty(value = "title", required = true) String title,
                                @JsonProperty(value = "director", required = true) String director,
                                @JsonProperty(value = "year", required = true) Integer year,
                                @JsonProperty(value = "backdrop_path") String backdrop_path,
                                @JsonProperty(value = "budget") Integer budget,
                                @JsonProperty(value = "overview") String overview,
                                @JsonProperty(value = "poster_path") String poster_path,
                                @JsonProperty(value = "revenue") Integer revenue,
                                @JsonProperty(value = "genres", required = true) GenreModel[] genres) {
        this.email = email;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = (backdrop_path == null) ? "" : backdrop_path;
        this.budget = (budget == null) ? 0 : budget;
        this.overview = (overview == null) ? "" : overview;
        this.poster_path = (poster_path == null) ? "" : poster_path;
        this.revenue = (revenue == null) ? 0 : revenue;
        this.genres = genres;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("director")
    public String getDirector() {
        return director;
    }

    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty("backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty("budget")
    public Integer getBudget() {
        return budget;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty("revenue")
    public Integer getRevenue() {
        return revenue;
    }

    @JsonProperty("genres")
    public GenreModel[] getGenres() {
        return genres;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}