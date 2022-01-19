package service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponseModel {
    private int resultCode;
    private String message;

    @JsonCreator
    public RegisterResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message)
    {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonProperty("resultCode")
    public int getResultCode()
    {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString() {
        return "RegisterResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                '}';
    }
}
