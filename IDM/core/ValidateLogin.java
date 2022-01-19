package service.idm.core;

import service.idm.logger.ServiceLogger;

public class ValidateLogin {
    private String email;
    private char[] password;

    private static String digits = "0123456789";
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lower = "abcdefghijklmnopqrstuvwxyz";
    private static String specialChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public ValidateLogin(String email, char[] password)
    {
        this.email = email;
        this.password = password;
    }

    public static int checkCase(String email, char[] password)
    {
        if ((password == null) || (password.length == 0))
        {
            return -12;
        }
        else if ((email == null) || (email.length() == 0) || (email.length() > 50))
        {
            return -10;
        }
        else if ((!email.matches("^([A-Za-z0-9\\-_]+)@([A-Za-z0-9\\-_]+)\\.([A-Za-z0-9\\-_.]+)$")))
        {
            return -11;
        }
        return 120;
    }

    public static String caseMessage(int resultCode) {
        if (resultCode == -12)
        {
            return "Password has invalid length.";
        }
        else if (resultCode == -11)
        {
            return "Email address has invalid format.";
        }
        else if (resultCode == -10)
        {
            return "Email address has invalid length.";
        }
        else if (resultCode == -3)
        {
            return "JSON Parse Exception.";
        }
        else if (resultCode == -2)
        {
            return "JSON Mapping Exception.";
        }
        else if (resultCode == -1)
        {
            return "Internal Server Error.";
        }
        else if (resultCode == 11)
        {
            return "Passwords do not match.";
        }
        else if (resultCode == 14)
        {
            return "User not found.";
        }
        // case 120
        return "User logged in successfully.";
    }


}
