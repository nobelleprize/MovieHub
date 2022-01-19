package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPlaceResponseModel {
    private int resultCode;
    private String message;
    private String redirectURL;
    private String token;

    public OrderPlaceResponseModel(int resultCode, String message, String redirectURL, String token) {
        this.resultCode = resultCode;
        this.message = message;
        this.redirectURL = redirectURL;
        this.token = token;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "OrderPlaceResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", redirectURL='" + redirectURL + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
