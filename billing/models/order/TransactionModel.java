package service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionModel {
    private String transactionId;
    private String state;
    private AmountModel amount;
    private TransactionFeeModel transaction_fee;
    private String create_time;
    private String update_time;
    private ArrayList<ItemModel> items;

    public TransactionModel(String transactionId, String state, AmountModel amount, TransactionFeeModel transaction_fee, String create_time, String update_time, ItemModel item) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = new ArrayList<>();
        this.items.add(item);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getState() {
        return state;
    }

    public AmountModel getAmount() {
        return amount;
    }

    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public ArrayList<ItemModel> getItems() {
        return items;
    }

    public void addItem(ItemModel itemModel)
    {
        this.items.add(itemModel);
    }
}
