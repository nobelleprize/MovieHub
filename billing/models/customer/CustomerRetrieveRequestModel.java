package service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerRetrieveRequestModel {
        private String email;

        @JsonCreator
        public CustomerRetrieveRequestModel(@JsonProperty(value = "email", required = true) String email)
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
                return "CustomerRetrieveRequestModel{" +
                        "email='" + email + '\'' +
                        '}';
        }
}
