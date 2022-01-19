package service.billing.core;

import service.billing.BillingService;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.CartItemModel;
import service.billing.models.cart.CartRetrieveResponseModel;
import service.billing.models.creditcard.*;

import javax.xml.ws.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreditCardRecords {
    public static ResponseModel insertCreditCardToDb(CCInsertRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Inserting credit card to database...");
        try
        {
            String query = "INSERT into creditcards (id, firstName, lastName, expiration) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setDate(4, new java.sql.Date(requestModel.getExpiration().getTime()));
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return new ResponseModel(3200, validate.caseMessage(3200));
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
            {
                return new ResponseModel(325, validate.caseMessage(325));
            }
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));

    }

    public static ResponseModel updateCreditCardToDb(CCUpdateRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Updating credit card to database...");
        try
        {
            String query = "UPDATE creditcards SET firstName = ? , lastName = ? , expiration = ? WHERE id = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            ps.setDate(3, new java.sql.Date(requestModel.getExpiration().getTime()));
            ps.setString(4, requestModel.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs <= 0)
            {
                return new ResponseModel(324, validate.caseMessage(324));
            }
            return new ResponseModel(3210, validate.caseMessage(3210));
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, "not implemented");
    }

    public static ResponseModel deleteCreditCardFromDb(CCDeleteRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Deleting credit card from database...");
        try
        {
            String query = "DELETE FROM creditcards WHERE id = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs <= 0 )
            {
                return new ResponseModel(324, validate.caseMessage(324));
            }
            return new ResponseModel(3220, validate.caseMessage(3220));

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));

    }

    public static CCRetrieveResponseModel retrieveCreditCardFromDb(CCRetrieveRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Retrieving credit card from database...");
        try
        {
            int exists = 0;
            String query = "SELECT * FROM creditcards WHERE id = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            CCCreditCardModel result = null;
            if (rs.next())
            {
                exists = 1;
                result = new CCCreditCardModel(rs.getString("id"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getDate("expiration"));
            }
            if (exists == 0)
            {
                return new CCRetrieveResponseModel(324, validate.caseMessage(324), null);

            }
            return new CCRetrieveResponseModel(3230, validate.caseMessage(3230), result);
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new CCRetrieveResponseModel(-1, "not implemented", null);

    }


}
