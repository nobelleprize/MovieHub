package service.billing.models.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartInsertRequestModel {
    private String email;
    private String movieId;
    private Integer quantity;

    @JsonCreator
    public CartInsertRequestModel(@JsonProperty(value = "email", required = true) String email,
                                  @JsonProperty(value = "movieId", required = true) String movieId,
                                  @JsonProperty(value = "quantity", required = true) Integer quantity)
    {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "CartInsertRequestModel{" +
                "email='" + email + '\'' +
                ", movieId='" + movieId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
