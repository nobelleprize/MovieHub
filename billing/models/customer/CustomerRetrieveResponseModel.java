package service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CustomerCustomerModel customer;

    public CustomerRetrieveResponseModel(int resultCode, String message, CustomerCustomerModel customer) {
        this.resultCode = resultCode;
        this.message = message;
        this.customer = customer;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public CustomerCustomerModel getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "CustomerRetrieveResponseModel{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", customer=" + customer +
                '}';
    }
}

