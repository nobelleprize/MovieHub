package service.billing.models.creditcard;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCRetrieveResponseModel {
    private int resultCode;
    private String message;
    private CCCreditCardModel creditcard;

    public CCRetrieveResponseModel(int resultCode, String message, CCCreditCardModel creditcard) {
        this.resultCode = resultCode;
        this.message = message;
        this.creditcard = creditcard;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public CCCreditCardModel getCreditcard() {
        return creditcard;
    }


    @Override
    public String toString() {
        return "CCRetrieveResponseModel{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", creditcard=" + creditcard +
                '}';
    }
}
