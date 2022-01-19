package service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullRequestModel extends RequestModel {
    private Map<String, String> queryParams;
    private String urlParam;
    private String jsonText;

    public FullRequestModel(@JsonProperty(value = "email") String email,
                            @JsonProperty(value = "sessionID") String sessionID)
    {
        super(email, sessionID);
    }

    public FullRequestModel(@JsonProperty(value = "email") String email,
                            @JsonProperty(value = "sessionID") String sessionID,
                            @JsonProperty(value = "queryParams") Map<String, String> queryParams)
    {
        super(email, sessionID);
        this.queryParams = queryParams;
    }

    public FullRequestModel(@JsonProperty(value = "email") String email,
                            @JsonProperty(value = "sessionID") String sessionID,
                            @JsonProperty(value = "urlParam") String urlParam)
    {
        super(email, sessionID);
        this.urlParam = urlParam;
    }

    public FullRequestModel(@JsonProperty(value = "email") String email,
                            @JsonProperty(value = "sessionID") String sessionID,
                            @JsonProperty(value = "jsonText") String jsonText,
                            boolean dummy)
    {
        super(email, sessionID);
        this.jsonText = jsonText;
    }

    @JsonProperty("queryParams")
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @JsonProperty("urlParam")
    public String getUrlParam() {
        return urlParam;
    }

    @JsonProperty("jsonText")
    public String getJsonText() {
        return jsonText;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public void setJsonText(String jsonText) {
        this.jsonText = jsonText;
    }


}
