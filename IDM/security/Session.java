package service.idm.security;

import service.idm.IDMService;

import java.sql.Timestamp;

public class Session {
    public static final long SESSION_TIMEOUT = IDMService.getConfigs().getTimeout(); // 600000; // 10-minute timeout
    public static final long TOKEN_EXPR = IDMService.getConfigs().getExpiration(); // 1800000; // 30-minute expiration
    public static final int ACTIVE = 1;
    public static final int CLOSED = 2;
    public static final int EXPIRED = 3;
    public static final int REVOKED = 4;
    public static final int ACTIVE_REVOKED = 11;


    private final String email;
    private final Token sessionID;
    private final Timestamp timeCreated;
    private final Timestamp exprTime;
    private Timestamp lastUsed;
    private int status;

    /*
        This constructor is used to generate a new session. The only required information is the user's email address.
        This session will be forever associated with the user corresponding to the email address passed.
     */
    private Session(String email) {
        sessionID = Token.generateToken();
        this.email = email;
        this.timeCreated = new Timestamp(System.currentTimeMillis());
        this.lastUsed = timeCreated;
        this.exprTime = new Timestamp(System.currentTimeMillis() + TOKEN_EXPR);
        this.status = ACTIVE;
    }

    /*
        This constructor is used when trying to rebuild an existing session for verification purposes. The email,
        sessionID (token) will come from the client in a request, the lastUsed time will be known at the time the
        request happens (current time), timeCreated and exprTime come from the sessions table in the database.
     */
    private Session(String email, Token sessionID, Timestamp timeCreated, Timestamp lastUsed, Timestamp exprTime, int status) {
        this.email = email;
        this.sessionID = sessionID;
        this.timeCreated = timeCreated;
        this.lastUsed = timeCreated;
        this.exprTime = exprTime;
        this.status = status;
    }

    /*
        Use factory method to create a new session for increased security.
     */
    public static Session createSession(String name) {
        return new Session(name);
    }

    /*
        Rebuild an existing session object using the username and sessionID.
     */
    public static Session rebuildSession(String userName, Token sessionID, Timestamp timeCreated, Timestamp lastUsed, Timestamp exprTime, int status) {
        return new Session(userName, sessionID, timeCreated, lastUsed, exprTime, status);
    }

    /*
        This updates the lastUsed data member to the current time.
     */
    public int update() {
        // Generate new session
        if (isDataValid() == ACTIVE)
        {
            lastUsed = new Timestamp(System.currentTimeMillis());
            return ACTIVE;
        }
        else if (isDataValid() == REVOKED)
        {
            return REVOKED;
        }
        else if (isDataValid() == EXPIRED)
        {
            return EXPIRED;
        }
        return ACTIVE_REVOKED;
    }

    /*
        This method checks if the session has timed out. A timeout occurs if the user attempts to use a session more
        than SESSION_TIMEOUT time after the session was initially created.
     */
    public int isDataValid() {
        // Get the current time
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp timeoutTime = new Timestamp(lastUsed.getTime() + SESSION_TIMEOUT);

        // Verify that the time difference of current time and expiration time is less than session timeout
        if ((!currentTime.after(timeoutTime)) && currentTime.after(exprTime))
        {
            status = REVOKED;
            return ACTIVE_REVOKED; // Generate new session
        }
        // Verify that the current time is not greater than the hard expiration time of the session
        if (currentTime.after(exprTime))
        {
            status = EXPIRED;
            return EXPIRED;
        }
        // Verify that the current time is not AFTER the time of last use, causing a timeout.
        if (currentTime.after(timeoutTime))
        {
            status = REVOKED;
            return REVOKED;
        }
        return ACTIVE;
    }

    @Override
    /*
        This method checks that two session objects are equal.
     */
    public boolean equals(Object o) {
        // Compare with self
        if (this == o) {
            return true;
        }

        // Compare with class type
        if (!(o instanceof Session)) {
            return false;
        }

        // Compare data members
        Session session = (Session) o;
        return getTimeCreated() == session.getTimeCreated() &&
                getLastUsed() == session.getLastUsed() &&
                getSessionID().equals(session.getSessionID()) &&
                getEmail().equals(session.getEmail()) &&
                getStatus() == session.getStatus();
    }

    public Token getSessionID()
    {
        return sessionID;
    }

    public String getEmail()
    {
        return email;
    }

    public Timestamp getTimeCreated()
    {
        return timeCreated;
    }

    public Timestamp getLastUsed()
    {
        return lastUsed;
    }

    public Timestamp getExprTime()
    {
        return exprTime;
    }

    public int getStatus() {
        return status;
    }
}



