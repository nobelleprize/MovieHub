package service.billing.core;

import service.billing.BillingService;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.customer.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRecords {
    public static ResponseModel insertCustomerToDb(CustomerInsertRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Inserting customer to database...");
        try
        {
            String query = "INSERT INTO customers (email, firstName, lastName, ccId, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setString(4, requestModel.getCcId());
            ps.setString(5, requestModel.getAddress());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return new ResponseModel(3300, validate.caseMessage(3300));
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 1452)
            {
                return new ResponseModel(331, validate.caseMessage(331));

            }
            else if (e.getErrorCode() == 1062)
            {
                return new ResponseModel(333, validate.caseMessage(333));
            }
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));
    }

    public static ResponseModel updateCustomerToDb(CustomerUpdateRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Updating credit card to database...");
        try
        {
            String query = "UPDATE customers SET firstName = ? , lastName = ? , ccId = ? , address = ? " +
                    "WHERE email = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            ps.setString(3, requestModel.getCcId());
            ps.setString(4, requestModel.getAddress());
            ps.setString(5, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs <= 0)
            {
                return new ResponseModel(332, validate.caseMessage(332));
            }
            return new ResponseModel(3310, validate.caseMessage(3310));
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 1452)
            {
                return new ResponseModel(331, validate.caseMessage(331));

            }
            ServiceLogger.LOGGER.warning("Unable to insert credit card " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, "not implemented");
    }

    public static CustomerRetrieveResponseModel retrieveCustomerFromDb(CustomerRetrieveRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Retrieving customer from database...");
        try
        {
            int exists = 0;
            String query = "SELECT * FROM customers WHERE email = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            CustomerCustomerModel result = null;
            if (rs.next())
            {
                exists = 1;
                result = new CustomerCustomerModel(rs.getString("email"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("ccId"), rs.getString("address"));
            }
            if (exists == 0)
            {
                return new CustomerRetrieveResponseModel(332, validate.caseMessage(332), null);

            }
            return new CustomerRetrieveResponseModel(3320, validate.caseMessage(3320), result);
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to retrieve customer " + requestModel.toString());
            e.printStackTrace();
        }
        return new CustomerRetrieveResponseModel(-1, "not implemented", null);

    }


}
