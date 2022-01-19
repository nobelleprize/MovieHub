package service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieModel {
    private String movieId;
    private String title;
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private Float rating;
    private Integer numVotes;
    private Boolean hidden;
    private GenreModel[] genres;
    private StarModel[] stars;


    @JsonCreator
    public MovieModel(@JsonProperty(value = "movieId", required = true) String movieId,
                      @JsonProperty(value = "title", required = true) String title,
                      @JsonProperty(value = "director", required = true) String director,
                      @JsonProperty(value = "year", required = true) Integer year,
                      @JsonProperty(value = "backdrop_path") String backdrop_path,
                      @JsonProperty(value = "budget") Integer budget,
                      @JsonProperty(value = "overview") String overview,
                      @JsonProperty(value = "poster_path") String poster_path,
                      @JsonProperty(value = "revenue") Integer revenue,
                      @JsonProperty(value = "rating", required = true) Float rating,
                      @JsonProperty(value = "numVotes") Integer numVotes,
                      @JsonProperty(value = "hidden") Boolean hidden,
                      @JsonProperty(value = "genres") GenreModel[] genres,
                      @JsonProperty(value = "stars") StarModel[] stars) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        if (this.budget == 0)
        {
            this.budget = null;
        }
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        if (this.revenue == 0)
        {
            this.revenue = null;
        }
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
        this.genres = genres;
        this.stars = stars;
    }


    @JsonCreator
    public MovieModel(@JsonProperty(value = "movieId", required = true) String movieId,
                      @JsonProperty(value = "title", required = true) String title,
                      @JsonProperty(value = "director", required = true) String director,
                      @JsonProperty(value = "year", required = true) Integer year,
                      @JsonProperty(value = "rating", required = true) Float rating,
                      @JsonProperty(value = "numVotes") Integer numVotes,
                      @JsonProperty(value = "hidden") Boolean hidden) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
    }

    @JsonProperty("movieId")
    public String getId() {
        return movieId;
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

    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }

    @JsonProperty("numVotes")
    public Integer getNumVotes() {
        return numVotes;
    }

    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty("genres")
    public GenreModel[] getGenres() {
        return genres;
    }

    @JsonProperty("stars")
    public StarModel[] getStars() {
        return stars;
    }

    public void setGenres(GenreModel[] genres) {
        this.genres = genres;
    }

    public void setStars(StarModel[] stars) {
        this.stars = stars;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", budget=" + budget +
                ", overview[0-20]='" + overview.substring(0, 20) + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", revenue=" + revenue +
                ", rating=" + rating +
                ", numVotes=" + numVotes +
                ", hidden=" + hidden +
                ", genres=" + Arrays.toString(genres) +
                ", stars=" + Arrays.toString(stars) +
                '}';
    }
}
