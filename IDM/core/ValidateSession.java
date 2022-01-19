package service.idm.core;

public class ValidateSession {
    private String email;
    private String session;

    private static String digits = "0123456789";
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lower = "abcdefghijklmnopqrstuvwxyz";
    private static String specialChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public ValidateSession(String email, String session)
    {
        this.email = email;
        this.session = session;
    }

    public static int checkCase(String email, String session)
    {
        if ((session == null) || (session.length() == 0))
        {
            return -13;
        }
        else if ((email == null) || (email.length() == 0) || (email.length() > 50))
        {
            return -10;
        }
        else if ((!email.matches("^([A-Za-z0-9\\-_.]+)@([A-Za-z0-9\\-_]+)\\.([A-Za-z0-9\\-_.]+)$")))
        {
            return -11;
        }
        return 130;
    }

    public static String caseMessage(int resultCode)
    {
        if (resultCode == -13)
        {
            return "Token has invalid length.";
        }
        else if (resultCode == -1)
        {
            return "Internal Server Error.";
        }
        else if (resultCode == -11)
        {
            return "Email address has invalid format.";
        }
        else if (resultCode == -10)
        {
            return "Email address has invalid length.";
        }
        else if (resultCode == 11)
        {
            return "Passwords do not match.";
        }
        else if (resultCode == 14)
        {
            return "User not found.";
        }
        else if (resultCode == 131)
        {
            return "Session is expired.";
        }
        else if (resultCode == 132)
        {
            return "Session is closed.";
        }
        else if (resultCode == 133)
        {
            return "Session is revoked.";
        }
        else if (resultCode == 134)
        {
            return "Session not found.";
        }
        else if (resultCode == -2)
        {
            return "JSON Mapping Exception.";
        }
        else if (resultCode == -3)
        {
            return "JSON Parse Exception.";
        }
        // case 130
        return "Session is active.";
    }

}
