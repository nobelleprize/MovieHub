package service.billing.models.creditcard;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CCCreditCardModel {
    private String id;
    private String firstName;
    private String lastName;
    private Date expiration;

    public CCCreditCardModel(String id, String firstName, String lastName, Date expiration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "CCCreditCardModel{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
