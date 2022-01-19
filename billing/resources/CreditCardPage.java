package service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.billing.core.CartRecords;
import service.billing.core.CreditCardRecords;
import service.billing.core.validate;
import service.billing.logger.ServiceLogger;
import service.billing.models.ResponseModel;
import service.billing.models.cart.CartInsertRequestModel;
import service.billing.models.creditcard.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Path("creditcard")
public class CreditCardPage {
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCard(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to insert credit card.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCInsertRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CCInsertRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getId());
            int expirationCaseNumber = validate.validateExpiration(requestModel.getExpiration());
            responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            if (ccCaseNumber != 1)
            {
                responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            }
            if (expirationCaseNumber != 1)
            {
                responseModel = new ResponseModel(expirationCaseNumber, validate.caseMessage(expirationCaseNumber));
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (expirationCaseNumber == 323)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CreditCardRecords.insertCreditCardToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCard(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to update credit card.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCUpdateRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CCUpdateRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getId());
            int expirationCaseNumber = validate.validateExpiration(requestModel.getExpiration());
            responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            if (ccCaseNumber != 1)
            {
                responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            }
            if (expirationCaseNumber != 1)
            {
                responseModel = new ResponseModel(expirationCaseNumber, validate.caseMessage(expirationCaseNumber));
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (expirationCaseNumber == 323)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CreditCardRecords.updateCreditCardToDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCard(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to delete credit card.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCDeleteRequestModel requestModel = null;
        ResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CCDeleteRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getId());
            responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            if (ccCaseNumber != 1)
            {
                responseModel = new ResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber));
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CreditCardRecords.deleteCreditCardFromDb(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            return catchIOException(e);
        }
    }

    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCard(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to retrieve credit card.");
        ServiceLogger.LOGGER.info("Register Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCRetrieveRequestModel requestModel = null;
        CCRetrieveResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, CCRetrieveRequestModel.class);
            int ccCaseNumber = validate.validateCreditCard(requestModel.getId());
            responseModel = new CCRetrieveResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber), null);
            if (ccCaseNumber != 1)
            {
                responseModel = new CCRetrieveResponseModel(ccCaseNumber, validate.caseMessage(ccCaseNumber), null);
            }
            if (ccCaseNumber == 322 || ccCaseNumber == 321)
            {
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            responseModel = CreditCardRecords.retrieveCreditCardFromDb(requestModel);
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
