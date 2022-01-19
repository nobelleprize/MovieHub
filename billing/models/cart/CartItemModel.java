package service.billing.models.cart;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemModel {
    private String email;
    private String movieId;
    private Integer quantity;

    public CartItemModel(String email, String movieId, Integer quantity)
    {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "CartItemModel{" +
                "email='" + email + '\'' +
                ", movieId='" + movieId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
