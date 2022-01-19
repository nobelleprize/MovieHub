package service.api_gateway.resources;

import service.api_gateway.GatewayService;
import service.api_gateway.logger.ServiceLogger;
import service.api_gateway.models.ResponseModel;
import service.api_gateway.models.VerifyPrivilegeResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("")
public class ReportEndpoint {
    @Path("report")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@Context HttpHeaders headers) {
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");

        if (email == null) {
            ResponseModel responseModel = new ResponseModel(
                    -16, validate.caseMessage(-16));
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }

        return reportRecords.report(transactionID, email);
    }
}

