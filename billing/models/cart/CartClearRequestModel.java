package service.billing.models.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartClearRequestModel {
    private String email;

    @JsonCreator
    public CartClearRequestModel(@JsonProperty(value = "email", required = true) String email)
    {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "CartClearRequestModel{" +
                "email='" + email + '\'' +
                '}';
    }
}
