package service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.billing.core.CartRecords;
import service.billing.core.CustomerRecords;
import service.billing.core.validate;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.*;
import service.billing.models.customer.CustomerInsertRequestModel;
import service.billing.models.customer.CustomerRetrieveRequestModel;
import service.billing.models.customer.CustomerRetrieveResponseModel;
import service.billing.models.customer.CustomerUpdateRequestModel;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("customer")
public class CustomerPage {
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomer(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to insert customer.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CustomerInsertRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CustomerInsertRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getCcId());
            responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            if (ccCaseNumber != 1)
            {
                responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CustomerRecords.insertCustomerToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to update customer.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CustomerUpdateRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CustomerUpdateRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getCcId());
            responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            if (ccCaseNumber != 1)
            {
                responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CustomerRecords.updateCustomerToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomer(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to retrieve customer.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        CustomerRetrieveRequestModel requestModel = null;
        CustomerRetrieveResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CustomerRetrieveRequestModel.class);
            responseModel = CustomerRecords.retrieveCustomerFromDb(requestModel);
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
