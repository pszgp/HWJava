/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql.sums;

/**
 *
 * @author pszgp, 2 april 2012, 17apr
 */
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HWMySQL {

    private static HWMySQL instance;
    public static HWMySQL getInstance(){
        if (instance != null)
            return instance;
        return new HWMySQL();
    }
    private HWMySQL(){
        connectMySQL();
    }

    public static Connection connectMySQL() {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            String driverName = "org.gjt.mm.mysql.Driver";//MySQL MM JDBC driver
            Class.forName(driverName);
            String DATABASE = "Homework";//properties.getProperty("DATABASE");
            String USERNAME = "homework";//properties.getProperty("DBUSER");
            String PASSWORD = "whatever";//properties.getProperty("DBPASSW");
            String SERVER_MYSQL = "localhost:3306";//properties.getProperty("SERVER");
                                    
            String url = "jdbc:mysql://"+SERVER_MYSQL+"/"+DATABASE;
            conn = DriverManager.getConnection(url, USERNAME, PASSWORD);//"jdbc:default:connection");           
        }catch(SQLException e) {
            System.out.println("SQLException: cannot connect to mysql database.");
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe){System.out.println("Cannot find mysql jdbc driver.");}
        return conn;
    }
    
    public static void main(String[] args)
    {
        HWMySQL.connectMySQL();
    }
}