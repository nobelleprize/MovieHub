package service.billing.models.cart;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CartItemModel[] items;

    public CartRetrieveResponseModel(int resultCode, String message, CartItemModel[] items) {
        this.resultCode = resultCode;
        this.message = message;
        this.items = items;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public CartItemModel[] getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "CartRetrieveResponseModel{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
