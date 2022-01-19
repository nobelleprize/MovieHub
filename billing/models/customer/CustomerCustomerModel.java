package service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCustomerModel {
    private String email;
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;

    public CustomerCustomerModel(String email, String firstName, String lastName, String ccId, String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCcId() {
        return ccId;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CustomerCustomerModel{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ccId='" + ccId + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}


