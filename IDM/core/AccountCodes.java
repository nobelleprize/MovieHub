package service.idm.core;

public class AccountCodes {
    public static final int MIN_EMAIL_LENGTH = 6;
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int TOKEN_LENGTH = 128;

    public static final int MIN_PLEVEL = 2;
    public static final int MAX_PLEVEL = 5;
    public static final int PLEVEL_ADMIN = 2;
    public static final int PLEVEL_EMPLOYEE = 3;
    public static final int PLEVEL_SERVICE = 4;
    public static final int PLEVEL_USER = 5;
    public static final int PLEVEL_DEFAULT = PLEVEL_USER;

    public static final int MIN_USER_LEVEL = 1;
    public static final int MAX_USER_STATUS = 4;
    public static final int USER_STATUS_ACTIVE = 1;
    public static final int USER_STATUS_CLOSED = 2;
    public static final int USER_STATUS_LOCKED = 3;
    public static final int USER_STATUS_REVOKED = 4;
    public static final int USER_STATUS_DEFAULT = USER_STATUS_ACTIVE;

//    public static final int SESSION_STATUS_ACTIVE = 1;
//    public static final int SESSION_STATUS_CLOSED = 2;
//    public static final int SESSION_STATUS_EXPIRED = 3;
//    public static final int SESSION_STATUS_REVOKED = 4;
//    public static final int SESSION_STATUS_DEFAULT = SESSION_STATUS_ACTIVE;

}
