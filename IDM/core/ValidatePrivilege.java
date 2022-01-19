package service.idm.core;

public class ValidatePrivilege {
    private String email;
    private int plevel;

    public ValidatePrivilege(String email, int session) {
        this.email = email;
        this.plevel = plevel;
    }

    public static int checkCase(String email, int plevel) {
        if ((plevel > 5) || (plevel < 1))
        {
            return -14;
        }
        else if ((email == null) || (email.length() == 0) || (email.length() > 50))
        {
            return -10;
        }
        else if ((!email.matches("^([A-Za-z0-9\\-_]+)@([A-Za-z0-9\\-_]+)\\.([A-Za-z0-9\\-_.]+)$")))
        {
            return -11;
        }
        return 140;
    }

    public static String caseMessage(int resultCode) {
        if (resultCode == -14) {
            return "Privilege level out of valid range.";
        } else if (resultCode == -11) {
            return "Email address has invalid format.";
        } else if (resultCode == -10) {
            return "Email address has invalid length.";
        } else if (resultCode == -3) {
            return "JSON Parse Exception";
        } else if (resultCode == -2) {
            return "JSON Mapping Exception.";
        } else if (resultCode == -1) {
            return "Internal Server Error.";
        } else if (resultCode == 14) {
            return "User not found.";
        } else if (resultCode == 141) {
            return "User has insufficient privilege level.";
        }
        // case 140
        return "User has sufficient privilege level.";
    }
}
