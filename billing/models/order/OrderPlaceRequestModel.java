package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPlaceRequestModel {
    private String email;

    @JsonCreator
    public OrderPlaceRequestModel(@JsonProperty(value = "email", required = true) String email)
    {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail()
    {
        return email;
    }

    @Override
    public String toString() {
        return "OrderPlaceRequestModel{" +
                "email='" + email + '\'' +
                '}';
    }
}
