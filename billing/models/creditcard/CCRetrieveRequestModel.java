package service.billing.models.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCRetrieveRequestModel {
    private String id;

    @JsonCreator
    public CCRetrieveRequestModel(@JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CCRetrieveRequestModel{" +
                "id='" + id + '\'' +
                '}';
    }
}
