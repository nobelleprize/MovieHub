package service.api_gateway.connectionpool;

import service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import static service.api_gateway.GatewayService.ANSI_RED;
import static service.api_gateway.GatewayService.ANSI_RESET;

public class ConnectionPool {
    LinkedList<Connection> connections;
    String driver;
    String url;
    String username;
    String password;

    public ConnectionPool(int numCons, String driver, String url, String username, String password) {
        this.connections = new LinkedList<>();
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
            for (int i = 0; i < numCons; ++i) {
                Connection con = createConnection();
                connections.add(con);
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection requestCon() {
        return connections.isEmpty() ? createConnection() : connections.removeFirst();
    }

    public void releaseCon(Connection con) {
        connections.add(con);
    }

    private Connection createConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password); ///

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
}
