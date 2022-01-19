package service.billing.core;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import service.billing.BillingService;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.creditcard.CCInsertRequestModel;
import service.billing.models.order.*;

import javax.ws.rs.core.UriBuilder;
import javax.xml.ws.Service;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OrderRecords {
    public static OrderPlaceResponseModel placeOrderToDb(OrderPlaceRequestModel requestModel)
    {
        String clientId = "";
        String clientSecret = "";
        ServiceLogger.LOGGER.info("Inserting order to database...");
        try
        {
            int found = 0;
            String cartSearch = "SELECT * FROM carts, movie_prices " +
                    "WHERE email = ? AND carts.movieId = movie_prices.movieId";
            PreparedStatement cartSearchPS = BillingService.getCon().prepareStatement(cartSearch);
            cartSearchPS.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + cartSearchPS.toString());
            ResultSet cartRS = cartSearchPS.executeQuery();
            float sum = 0;
            ServiceLogger.LOGGER.info("Query succeeded.");
            HashMap<String, ArrayList<String>> customerCart = new HashMap<>();
            ArrayList<String> cartInfo  = new ArrayList<>();
            String customerEmail = null;
            while (cartRS.next())
            {
                found = 1;
//                ArrayList<String> cartInfo  = new ArrayList<>();
                cartInfo.add(cartRS.getString("movieId"));
                cartInfo.add(Integer.toString(cartRS.getInt("quantity")));
                cartInfo.add(Float.toString(cartRS.getFloat("unit_price")));
                cartInfo.add(Float.toString(cartRS.getFloat("discount")));
                customerEmail = cartRS.getString("email");
//                customerCart.put(cartRS.getString("email"), cartInfo);
                sum += (cartRS.getInt("quantity") * cartRS.getFloat("unit_price") *
                        cartRS.getFloat("discount"));
            }
            customerCart.put(customerEmail, cartInfo);
            cartInfo.add("placeholder");
            ServiceLogger.LOGGER.info("CART: " + Arrays.toString(cartInfo.toArray()));
            if (found == 0)
            {
                return new OrderPlaceResponseModel(341, validate.caseMessage(341), null, null);
            }
            sum = (float) Math.round(sum * 100) / 100;
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(Float.toString(sum));

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            List<Transaction> transactions = new ArrayList<Transaction>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("https://example.com/cancel");
            URI uri = UriBuilder.fromUri(BillingService.getConfigs().getScheme() +
                    BillingService.getConfigs().getHostName() +
                    BillingService.getConfigs().getPath()).port(BillingService.getConfigs().getPort()).build();
            redirectUrls.setReturnUrl(uri.toString() + "/order/complete");
            payment.setRedirectUrls(redirectUrls);
            String token;
            OrderPlaceResponseModel responseModel = null;
            try
            {
                APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
                Payment createdPayment = payment.create(apiContext);
                System.out.println(createdPayment.toString());
                String redirectUrl = createdPayment.getLinks().get(1).getHref();
                token = redirectUrl.substring(redirectUrl.lastIndexOf("token=") + 6);
                System.out.println("Redirect URL: " + redirectUrl);
                System.out.println("Token: " + token);
                responseModel =  new OrderPlaceResponseModel(3400, "Order placed successfully.", redirectUrl, token);
            } catch (Exception e)
            {
                return new OrderPlaceResponseModel(342, "Create payment failed.", null, null);
            }

            for (String i : customerCart.keySet())
            {
                ServiceLogger.LOGGER.info("KEY: " + i);
                int c = 2;
                CallableStatement cStmt = BillingService.getCon().prepareCall("{call insert_sales_transactions(?, ?, ?, ?, ?)}");
                if (i.equals("placeholder"))
                {
                    ;
                }
                ServiceLogger.LOGGER.info("email: " + i);
                ServiceLogger.LOGGER.info("c1: " + i);
                cStmt.setString(1, i);
                cStmt.setDate(4, new Date(System.currentTimeMillis()));
                for (String j : customerCart.get(i))
                {
                    if (c == 6)
                    {
                        ServiceLogger.LOGGER.info("EXECUTE");
                        cStmt.execute();
                        c = 2;
                    }
                    ServiceLogger.LOGGER.info("VALUES: " + customerCart.get(i).toString());
                    if (c == 2)
                    {
                        ServiceLogger.LOGGER.info("c2: " + j);
                        cStmt.setString(c, j);
                    }
                    if (c == 3)
                    {
                        ServiceLogger.LOGGER.info("c3: " + j);
                        cStmt.setInt(c, Integer.parseInt(j));
                    }
                    if (c == 4)
                    {
                        ;
                    }
                    if (c == 5)
                    {
                        ServiceLogger.LOGGER.info("c5: " + responseModel.getToken());
                        cStmt.setString(c, responseModel.getToken());
                    }
                    c++;
                }
            }
            try
            {
                String clearcart = "DELETE FROM carts WHERE email = ?";
                PreparedStatement clearcartPS = BillingService.getCon().prepareStatement(clearcart);
                clearcartPS.setString(1, requestModel.getEmail());
                ServiceLogger.LOGGER.info("Trying query: " + clearcartPS.toString());
                clearcartPS.executeUpdate();
                ServiceLogger.LOGGER.info("Query succeeded.");

            }
            catch (SQLException e)
            {
                ServiceLogger.LOGGER.info("Unable to clear cart " + requestModel.toString());
                e.printStackTrace();

            }
            return responseModel;
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 1452)
            {
                return new OrderPlaceResponseModel(332, validate.caseMessage(332), null, null);

            }
            ServiceLogger.LOGGER.warning("Unable to place order: " + requestModel.toString());
            e.printStackTrace();
        }
        return new OrderPlaceResponseModel(-1, "Internal Server Error", null, null);
    }

    public static ResponseModel completeOrderToDb(String paymentId, String token, String PayerID) {
        String clientId = "";
        String clientSecret = "";
        ServiceLogger.LOGGER.info("Completing order to database...");
        try
        {

            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(PayerID);
            payment = payment.execute(apiContext, paymentExecution);
            String transactionId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            String updateTransactionID = "UPDATE transactions SET transactionId = ? WHERE token = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(updateTransactionID);
            ps.setString(1, transactionId);
            ps.setString(2, token);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs > 0)
            {
                return new ResponseModel(3420, validate.caseMessage(3420));
            }
            else
            {
                return new ResponseModel(3421, validate.caseMessage(3421));
            }
        }
        catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("Unable to complete order.");
            e.printStackTrace();
        }
        return new ResponseModel(3422, validate.caseMessage(3422));

    }

    public static OrderRetrieveResponseModel retrieveOrderFromDb(OrderRetrieveRequestModel requestModel)
    {
        String clientId = "";
        String clientSecret = "";
        ServiceLogger.LOGGER.info("Retrieving order to database...");
        try
        {
            int exists = 0;
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            String transactionsQ = "SELECT * FROM movie_prices, sales, transactions WHERE sales.id = " +
                    "transactions.sId AND email = ? AND movie_prices.movieId = sales.movieId";
            PreparedStatement transactionsPS = BillingService.getCon().prepareStatement(transactionsQ);
            transactionsPS.setString(1, requestModel.getEmail());
            ResultSet transId = transactionsPS.executeQuery();
            ArrayList<TransactionModel> result = new ArrayList<>();
            while (transId.next())
            {
                try
                {
                    int index = -1;
                    String transactionId = transId.getString("transactionId");
                    ServiceLogger.LOGGER.info("transactionID: " + transactionId);
                    for (int i = 0; i < result.size(); i++)
                    {
                        if (result.get(i).getTransactionId().equals(transactionId))
                        {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1)
                    {
                        result.get(index).addItem(new ItemModel(transId.getString("email"),
                                                    transId.getString("movieId"),
                                transId.getInt("quantity"), (float) Math.round(transId.getFloat("unit_price") * 100) / 100 ,
                                (float) Math.round(transId.getFloat("discount") * 100) / 100, transId.getString("saleDate")));
                    }
                    else
                    {
                        Sale sale = Sale.get(apiContext, transactionId);

                        result.add(new TransactionModel(sale.getId(), sale.getState(), new AmountModel(sale.getAmount().getTotal(), sale.getAmount().getCurrency()),
                                new TransactionFeeModel(sale.getTransactionFee().getValue(), sale.getTransactionFee().getCurrency()),
                                sale.getCreateTime(), sale.getUpdateTime(), new ItemModel(transId.getString("email"),
                                transId.getString("movieId"),
                                transId.getInt("quantity"), (float) Math.round(transId.getFloat("unit_price") * 100) / 100,
                                (float) Math.round(transId.getFloat("discount") * 100) / 100, transId.getString("saleDate"))));
                    }
                }
                catch (Exception e)
                {
                    ServiceLogger.LOGGER.warning("Unable to retrieve order" + requestModel.toString());
                    e.printStackTrace();
                    return new OrderRetrieveResponseModel(322, validate.caseMessage(322), null);
                }

            }
            return new OrderRetrieveResponseModel(3410, validate.caseMessage(3410), result);
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            ServiceLogger.LOGGER.warning("1" + requestModel.toString());
        }
        return new OrderRetrieveResponseModel(-1, validate.caseMessage(-1), null);
    }
}

