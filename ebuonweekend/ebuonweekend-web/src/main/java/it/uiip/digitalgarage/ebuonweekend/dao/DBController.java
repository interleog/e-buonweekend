package it.uiip.digitalgarage.ebuonweekend.dao;


import java.sql.*;


public class DBController {

    protected static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    //  Database credentials
    /*protected static final String DB_URL = "jdbc:mysql://138.68.133.189:3306/buonweekend";
    protected static final String USER = "digitalgarage";
    protected static final String PASS = "digipassword";*/

    protected static final String DB_URL = "jdbc:mysql://localhost:3306/buonweekend";
    protected static final String USER = "root";
    protected static final String PASS = "admin";
    protected static Connection conn = null;
    protected static PreparedStatement stmt = null;
    protected static ResultSet rs = null;


    protected static boolean connectDB(String statement) {
        //connessione al server MySQL

        try {
            if(conn == null || conn.isClosed()) {
                Class.forName(JDBC_DRIVER).newInstance();
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.prepareStatement(statement);
                System.out.println("==Connected to MySQL Server==");
            }else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Connection failed!!\n" + e);
            disconnectDB();
            return false;
        }
        return true;
    }

    protected static boolean disconnectDB() {
        //chiusura connessione db
        try {
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            if(conn != null) conn.close();
            System.out.println("==DB Connection closed==");
            return true;
        } catch (Exception e) {
            System.out.println("Errore chiusura connessione DB!\n"+e);
            return false;
        }
    }


}