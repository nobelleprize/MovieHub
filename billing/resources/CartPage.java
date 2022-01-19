package service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.billing.core.CartRecords;

import service.billing.core.validate;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("cart")
public class CartPage {
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to insert to cart.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CartInsertRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CartInsertRequestModel.class);
            int emailCase = validate.validateEmail(requestModel.getEmail());
            int quantityCase = validate.validateQuantity(requestModel.getQuantity());
            if (emailCase != 1)
            {
                responseModel = new ResponseModel(emailCase, validate.caseMessage(emailCase));
            }
            else
            {
                responseModel = new ResponseModel(quantityCase, validate.caseMessage(quantityCase));

            }
            if (emailCase == -11 || emailCase == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (quantityCase == 33)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CartRecords.insertCartToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to update cart.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CartUpdateRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CartUpdateRequestModel.class);
            int emailCase = validate.validateEmail(requestModel.getEmail());
            int quantityCase = validate.validateQuantity(requestModel.getQuantity());
            if (emailCase != 1)
            {
                responseModel = new ResponseModel(emailCase, validate.caseMessage(emailCase));
            }
            else
            {
                responseModel = new ResponseModel(quantityCase, validate.caseMessage(quantityCase));

            }
            if (emailCase == -11 || emailCase == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (quantityCase == 33)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CartRecords.updateCartToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to delete cart.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CartDeleteRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CartDeleteRequestModel.class);
            int emailCase = validate.validateEmail(requestModel.getEmail());
            if (emailCase != 1)
            {
                responseModel = new ResponseModel(emailCase, validate.caseMessage(emailCase));
            }
            if (emailCase == -11 || emailCase == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = CartRecords.deleteCartFromDb(requestModel);
        } catch (IOException e) {
            return catchIOException(e);
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to retrieve cart.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CartRetrieveRequestModel requestModel = null;
        CartRetrieveResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CartRetrieveRequestModel.class);
            int emailCase = validate.validateEmail(requestModel.getEmail());
            if (emailCase != 1)
            {
                responseModel = new CartRetrieveResponseModel(emailCase, validate.caseMessage(emailCase), null);
            }
            if (emailCase == -11 || emailCase == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = CartRecords.retrieveCartFromDb(requestModel);
        } catch (IOException e) {
            return catchIOException(e);
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("clear")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to clear cart.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CartClearRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CartClearRequestModel.class);
            int emailCase = validate.validateEmail(requestModel.getEmail());
            if (emailCase != 1)
            {
                responseModel = new ResponseModel(emailCase, validate.caseMessage(emailCase));
            }
            if (emailCase == -11 || emailCase == -10)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = CartRecords.clearCartFromDb(requestModel);
        } catch (IOException e) {
            return catchIOException(e);
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    private Response catchIOException(IOException e)
    {
        if (e instanceof JsonMappingException) {
            ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
        } else if (e instanceof JsonParseException) {
            ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-3, validate.caseMessage(-3))).build();
        }
        ServiceLogger.LOGGER.info("IOException");
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseModel(-2, validate.caseMessage(-2))).build();
    }
}
