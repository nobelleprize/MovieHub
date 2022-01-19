package service.billing.models.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCInsertRequestModel {
    private String id;
    private String firstName;
    private String lastName;
    private Date expiration;

    @JsonCreator
    public CCInsertRequestModel(@JsonProperty(value = "id", required = true) String id,
                                @JsonProperty(value = "firstName", required = true) String firstName,
                                @JsonProperty(value = "lastName", required = true) String lastName,
                                @JsonProperty(value = "expiration", required = true) Date expiration)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("expiration")
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "CCInsertRequestModel{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
