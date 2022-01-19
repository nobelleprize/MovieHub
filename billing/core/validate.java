package service.billing.core;

import java.sql.Date;

public class validate {
    public static int validateEmail(String email)
    {
        if (email == null)
        {
            return -16;
        }
        else if ((email.length() == 0) || (email.length() > 50))
        {
            return -10;
        }
        else if ((!email.matches("^([A-Za-z0-9\\-_.]+)@([A-Za-z0-9\\-_]+)\\.([A-Za-z0-9\\-_.]+)$")))
        {
            return -11;
        }
        // valid email
        return 1;
    }

    public static int validateQuantity(Integer i)
    {
        if ((i == null) || (i % 1 != 0) || (i  <= 0))
        {
            return 33;
        }
        // valid quantity
        return 1;
    }

    public static int validateCreditCard(String id)
    {
        if ((id == null) || (!id.matches("[0-9]+")))
        {
            return 322;
        }
        else if ((id.length() < 16) || (id.length() > 20))
        {
            return 321;
        }
        // valid credit card
        return 1;
    }

    public static int validateExpiration(java.util.Date expiration)
    {
        if (expiration.before(new java.util.Date()))
        {
            return 323;
        }
        return 1;
    }

    public static String caseMessage(int resultCode)
    {
        if (resultCode == -11)
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
        else if (resultCode == 33)
        {
            return "Quantity has invalid value.";
        }
        else if (resultCode == 311)
        {
            return "Duplicate insertion.";
        }
        else if (resultCode == 312)
        {
            return "Shopping item does not exist.";
        }
        else if (resultCode == 321)
        {
            return "Credit card ID has invalid length.";
        }
        else if (resultCode == 322)
        {
            return "Credit card ID has invalid value.";
        }
        else if (resultCode == 323)
        {
            return "expiration has invalid value.";
        }
        else if (resultCode == 324)
        {
            return "Credit card does not exist.";
        }
        else if (resultCode == 325)
        {
            return "Duplicate insertion.";
        }
        else if (resultCode == 331)
        {
            return "Credit card ID not found.";
        }
        else if (resultCode == 332)
        {
            return "Customer does not exist.";
        }
        else if (resultCode == 333)
        {
            return "Duplicate insertion.";
        }
        else if (resultCode == 341)
        {
            return "Shopping cart for this customer not found.";
        }
        else if (resultCode == 342)
        {
            return "Create payment failed.";
        }
        else if (resultCode == 3100)
        {
            return "Shopping cart item inserted successfully.";
        }
        else if (resultCode == 3110)
        {
            return "Shopping cart item updated successfully.";
        }
        else if (resultCode == 3120)
        {
            return "Shopping cart item deleted successfully.";
        }
        else if (resultCode == 3130)
        {
            return "Shopping cart retrieved successfully.";
        }
        else if (resultCode == 3140)
        {
            return "Shopping cart cleared successfully.";
        }
        else if (resultCode == 3200)
        {
            return "Credit card inserted successfully.";
        }
        else if (resultCode == 3210)
        {
            return "Credit card updated successfully.";
        }
        else if (resultCode == 3220)
        {
            return "Credit card deleted successfully.";
        }
        else if (resultCode == 3230)
        {
            return "Credit card retrieved successfully.";
        }
        else if (resultCode == 3300)
        {
            return "Customer inserted successfully.";
        }
        else if (resultCode == 3310)
        {
            return "Customer updated successfully.";
        }
        else if (resultCode == 3320)
        {
            return "Customer retrieved successfully.";
        }
        else if (resultCode == 3400)
        {
            return "Order placed successfully";
        }
        else if (resultCode == 3410)
        {
            return "Orders retrieved successfully.";
        }
        else if (resultCode == 3420)
        {
            return "Payment is completed successfully.";
        }
        else if (resultCode == 3421)
        {
            return "Token not found.";
        }
        else if (resultCode == 3422)
        {
            return "Payment can not be completed.";
        }


        return "INCORRECT RESULT CODE";
    }
}
