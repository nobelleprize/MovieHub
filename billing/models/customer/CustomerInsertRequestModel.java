package service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerInsertRequestModel {
    private String email;
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;

    @JsonCreator
    public CustomerInsertRequestModel(@JsonProperty(value = "email", required = true) String email,
                                      @JsonProperty(value = "firstName", required = true) String firstName,
                                      @JsonProperty(value = "lastName", required = true) String lastName,
                                      @JsonProperty(value = "ccId", required = true) String ccId,
                                      @JsonProperty(value = "address", required = true) String address)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    @JsonProperty("email")
    public String getEmail()
    {
        return email;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("ccId")
    public String getCcId() {
        return ccId;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CustomerInsertRequestModel{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ccId='" + ccId + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}


