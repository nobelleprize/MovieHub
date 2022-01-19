package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieveResponseModel {
    private int resultCode;
    private String message;
    private ArrayList<TransactionModel> transactions;

    public OrderRetrieveResponseModel(int resultCode, String message, ArrayList<TransactionModel> transactions) {
        this.resultCode = resultCode;
        this.message = message;
        this.transactions = transactions;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public TransactionModel[] getTransactions() {
        TransactionModel[] array = new TransactionModel[transactions.size()];
        for (int i = 0; i < transactions.size(); i++)
        {
            array[i] = transactions.get(i);
        }
        return array;
    }
}

