package service.api_gateway.resources;

public class validate {

    public static String caseMessage(int resultCode)
    {
        if (resultCode == -17)
        {
            return "SessionID not provided in request header.";
        }
        else if (resultCode == -16)
        {
            return "Email not provided in request header.";
        }
        else
            return "not implemented.";
    }
}
