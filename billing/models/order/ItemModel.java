package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemModel {
    private String email;
    private String movieId;
    private int quantity;
    private float unit_price;
    private float discount;
    private String saleDate;

    public ItemModel(String email, String movieId, int quantity, float unit_price, float discount, String saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.saleDate = saleDate;
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public float getDiscount() {
        return discount;
    }

    public String getSaleDate() {
        return saleDate;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "email='" + email + '\'' +
                ", movieId='" + movieId + '\'' +
                ", quantity=" + quantity +
                ", unit_price=" + unit_price +
                ", discount=" + discount +
                ", saleDate='" + saleDate + '\'' +
                '}';
    }
}
