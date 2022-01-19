package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemModel {
    private String email;
    private String movieId;
    private Integer quantity;
    private Date saleDate;

    public OrderItemModel(String email, String movieId, Integer quantity, Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.saleDate = saleDate;
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

    public Date getSaleDate() {
        return saleDate;
    }

    @Override
    public String toString() {
        return "OrderItemModel{" +
                "email='" + email + '\'' +
                ", movieId='" + movieId + '\'' +
                ", quantity=" + quantity +
                ", saleDate=" + saleDate +
                '}';
    }
}
