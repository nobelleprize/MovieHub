package service.billing.core;

import service.billing.BillingService;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartRecords {
    public static ResponseModel insertCartToDb(CartInsertRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Inserting cart to database...");
        try
        {
            String query = "INSERT INTO carts (email, movieId, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ps.setString(2, requestModel.getMovieId());
            ps.setInt(3, requestModel.getQuantity());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return new ResponseModel(3100, validate.caseMessage(3100));
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
            {
                return new ResponseModel(311, validate.caseMessage(311));
            }
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));
    }

    public static ResponseModel updateCartToDb(CartUpdateRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Updating cart to database...");
        try
        {
            String query = "UPDATE carts SET quantity = ? WHERE email = ? AND movieId = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setInt(1, requestModel.getQuantity());
            ps.setString(2, requestModel.getEmail());
            ps.setString(3, requestModel.getMovieId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs <= 0)
            {
                return new ResponseModel(312, validate.caseMessage(312));
            }
            return new ResponseModel(3110, validate.caseMessage(3110));
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));
    }

    public static ResponseModel deleteCartFromDb(CartDeleteRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Deleting cart from database...");
        try
        {
            String query = "DELETE FROM carts WHERE email = ? AND movieId = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ps.setString(2, requestModel.getMovieId());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int rs = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            if (rs <= 0 )
            {
                return new ResponseModel(312, validate.caseMessage(312));
            }
            return new ResponseModel(3120, validate.caseMessage(3120));

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));
    }

    public static CartRetrieveResponseModel retrieveCartFromDb(CartRetrieveRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Retrieving cart from database...");
        try
        {
            int exists = 0;
            String query = "SELECT * FROM carts WHERE email = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ArrayList<CartItemModel> items = new ArrayList<>();
            while (rs.next())
            {
                exists = 1;
                items.add(new CartItemModel(rs.getString("email"), rs.getString("movieId"), rs.getInt("quantity")));
            }
            if (exists == 0)
            {
                return new CartRetrieveResponseModel(312, validate.caseMessage(312), null);

            }
            CartItemModel[] result = new CartItemModel[items.size()];

            for (int i = 0; i < items.size(); i++)
            {
                result[i] = items.get(i);
            }
            return new CartRetrieveResponseModel(3130, validate.caseMessage(3130), result);
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        return new CartRetrieveResponseModel(-1, validate.caseMessage(-1), null);
    }

    public static ResponseModel clearCartFromDb(CartClearRequestModel requestModel)
    {
        ServiceLogger.LOGGER.info("Clearing cart from database...");
        try
        {
            String query = "DELETE FROM carts WHERE email = ?";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getEmail());
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            return new ResponseModel(3140, validate.caseMessage(3140));

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to insert cart " + requestModel.toString());
            e.printStackTrace();
        }
        return new ResponseModel(-1, validate.caseMessage(-1));
    }

}
