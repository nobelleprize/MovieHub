package service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.billing.core.CartRecords;
import service.billing.core.OrderRecords;
import service.billing.core.validate;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.CartInsertRequestModel;
import service.billing.models.order.OrderPlaceRequestModel;
import service.billing.models.order.OrderPlaceResponseModel;
import service.billing.models.order.OrderRetrieveRequestModel;
import service.billing.models.order.OrderRetrieveResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Path("order")
public class OrderPage {
    @Path("place")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to place order.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        OrderPlaceRequestModel requestModel = null;
        OrderPlaceResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, OrderPlaceRequestModel.class);
            responseModel = OrderRecords.placeOrderToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeOrder(@QueryParam("paymentId") String paymentId,
                                  @QueryParam("token") String token,
                                  @QueryParam("PayerID") String PayerID)
    {
        ServiceLogger.LOGGER.info("Received request to complete order.");
        ResponseModel responseModel = null;
        responseModel = OrderRecords.completeOrderToDb(paymentId, token, PayerID);
        return Response.status(Response.Status.OK).entity(responseModel).build();

    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrder(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to retrieve order.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        OrderRetrieveRequestModel requestModel = null;
        OrderRetrieveResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, OrderRetrieveRequestModel.class);
            responseModel = OrderRecords.retrieveOrderFromDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
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
