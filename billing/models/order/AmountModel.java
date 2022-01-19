package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountModel {
    private String total;
    private String currency;

    public AmountModel(String total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "AmountModel{" +
                "total='" + total + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
