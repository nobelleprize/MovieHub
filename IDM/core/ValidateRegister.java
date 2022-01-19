package service.idm.core;

import service.idm.logger.ServiceLogger;

import java.util.Arrays;

public class ValidateRegister {
    private String email;
    private char[] password;

    private static String digits = "0123456789";
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lower = "abcdefghijklmnopqrstuvwxyz";
    private static String specialChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public ValidateRegister(String email, char[] password)
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
        else if ((password.length < 7) || (password.length > 16))
        {
            return 12;
        }
        else if (!isValidPass(password))
        {
            return 13;
        }
        return 110;
    }

    public static String caseMessage(int resultCode)
    {
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
        else if (resultCode == 12)
        {
            return "Password does not meet length requirements.";
        }
        else if (resultCode == 13)
        {
            return "Password does not meet character requirements.";
        }
        else if (resultCode == 16)
        {
            return "Email already in use.";
        }
        // case 110
        return "User registered successfully.";
    }

    public static boolean isValidPass(char[] password)
    {
        int digitCount = 0;
        int upperCount = 0;
        int lowerCount = 0;
        int specialCharsCount = 0;
        for (int i=0; i < password.length; i++)
        {
            if (digits.indexOf(password[i]) != -1)
            {
                digitCount++;
            }
            if (upper.indexOf(password[i]) != -1)
            {
                upperCount++;
            }
            if (lower.indexOf(password[i]) != -1)
            {
                lowerCount++;
            }
            if (specialChars.indexOf(password[i]) != -1)
            {
                specialCharsCount++;
            }
        }
        ServiceLogger.LOGGER.info("DIGITS: " + digitCount);
        ServiceLogger.LOGGER.info("UPPER: " + upperCount);
        ServiceLogger.LOGGER.info("LOWER: " + lowerCount);
        ServiceLogger.LOGGER.info("SPECIALCHARS: " + specialCharsCount);

        return (digitCount >= 1 && upperCount >= 1 && lowerCount >= 1 && specialCharsCount >= 1);
    }
}
