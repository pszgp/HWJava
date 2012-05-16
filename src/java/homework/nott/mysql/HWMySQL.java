/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql;

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
    public HWMySQL(){}

    public static Connection connectMySQL() {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            String driverName = "org.gjt.mm.mysql.Driver";//MySQL MM JDBC driver
            Class.forName(driverName);
            
            Properties properties = new Properties();
            try {
                //System.out.println(new File("src/properties.properties").getAbsolutePath());
                properties.load(new FileInputStream("src/properties.properties")); //!!! ok                    
            } catch (FileNotFoundException ex) {
                Logger.getLogger(HWMySQL.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    properties.load(new FileInputStream("/properties.properties"));
                } catch (IOException ex1) {
                    Logger.getLogger(HWMySQL.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (IOException ex) {
                Logger.getLogger(HWMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //System.out.println(properties.getProperty("DATABASE"));//ok!!!!
          
            String DATABASE = properties.getProperty("DATABASE");
            String USERNAME = properties.getProperty("DBUSER");
            String PASSWORD = properties.getProperty("DBPASSW");
            String SERVER_MYSQL = properties.getProperty("SERVER");
                        
            //System.out.println("SQL connection details: "+DATABASE+" "+USERNAME+" "+PASSWORD+" "+SERVER_MYSQL);
            //System.exit(0);
            
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