package service.api_gateway.threadpool;

import service.api_gateway.models.FullRequestModel;
import service.api_gateway.models.RequestModel;

import javax.print.attribute.standard.RequestingUserName;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private FullRequestModel request;
    private String URI;
    private String endpoint;
    private String type;

    public ClientRequest() {

    }

    public ClientRequest(String email, String sessionID, String transactionID, FullRequestModel request, String URI, String endpoint, String type) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public FullRequestModel getRequest() {
        return request;
    }

    public void setRequest(FullRequestModel request) {
        this.request = request;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "email='" + email + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", transactionID='" + transactionID + '\'' +
                ", request=" + request.toString() +
                ", URI='" + URI + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", type=" + type +
                '}';
    }
}
