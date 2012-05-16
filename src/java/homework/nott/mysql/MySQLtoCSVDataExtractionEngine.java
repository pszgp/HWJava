/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql;

import homework.nott.hwdb.HWMySQLEngine;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

/**
 *
 * @author pszgp, 8 May 2012
 */
public class MySQLtoCSVDataExtractionEngine {
    private static MySQLtoCSVDataExtractionEngine instance;
    public static MySQLtoCSVDataExtractionEngine getInstance(){
        if (instance != null)
            return instance;
        return new MySQLtoCSVDataExtractionEngine();
    }

    private static void writeCSVToFile(String text, String out, boolean append) {
        BufferedWriter bw;
        try {
            FileWriter fw = new FileWriter(out, append);
            bw = new BufferedWriter(fw);//true = append to existing file
            System.out.println("save csv text to file: " + new File(out).getAbsolutePath()+
                    " location: " + new File(out).getCanonicalPath());
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(MySQLtoCSVDataExtractionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public MySQLtoCSVDataExtractionEngine(){}
    public static String extractCSVFromMySQLTable(String table){
        try{
            StringBuffer csvText = new StringBuffer();
            
            String query = "SELECT * FROM " + table;
            
            ArrayList<TreeMap<String, Object>> resultQuery = HWMySQLEngine.getInstance().queryMySQL(query, null);

            System.out.println(resultQuery.get(0));
            //System.exit(0);

            if (resultQuery == null) return null;
            if (resultQuery.size()==0) return null;

            //retrieve the fields
            System.out.println("fieds="+resultQuery.get(0).keySet());
            
            ArrayList fields = new ArrayList(resultQuery.get(0).keySet());
            csvText = new StringBuffer("");
            for (int i=0;i<fields.size()-1;i++){
                csvText = new StringBuffer(csvText).append(fields.get(i)+",");
            }
            csvText = new StringBuffer(csvText).append(fields.get(fields.size()-1)+"\n");
            
            System.out.println("fields are: " + csvText+" ...");

            //System.out.println("csv text: " + csvText);
            
            //filepath: /var/lib/tomcat6/HWOUT/Flows_.csv
            
            String filePath = "webapps/HWJavaSocket/js/"+table+"_sql_.csv";
                        //"HWOUT/"+table+"_sql_.csv";
            File file = new File(filePath);
            System.out.println(file.getAbsolutePath()+" "+file.canRead()+" "+file.canWrite()+" "+file.canExecute());
            
            writeCSVToFile(csvText.toString(), filePath, false);//empty the file
            
            csvText = new StringBuffer("");
            
            int countRows = 0;
            //retrieve the rows
            for (TreeMap<String,Object> row: resultQuery){
                countRows++;
                for (int i=0;i<fields.size();i++){
                    String endText = ",";
                    if (i==fields.size()-1)
                        endText = "\n";
                    if (fields.get(i)!=null)
                    {
                        if (fields.get(i).toString().contains("last")) {
                            Object rowValue = row.get(fields.get(i));
                            Timestamp t = null;
                            if (rowValue!=null)
                            {
                                t = HWTimestamp.getTimestampFromUnixString((String)rowValue);
                            }
                            csvText = new StringBuffer(csvText).append(t+endText);
                        }
                        else
                            csvText = new StringBuffer(csvText).append(row.get(fields.get(i))+endText);
                    }
                    else
                        csvText = new StringBuffer(csvText).append(row.get(fields.get(i))+endText);
                        //System.out.println("csv text: " + csvText);
                }
                //break;
                if (countRows%100==0)
                {
                    System.out.println("csv text: " + csvText);
                    writeCSVToFile(csvText.toString(), filePath, true);
                    csvText = new StringBuffer("");//reinitiate the buffer
                }
            }
            
            //default file: /var/lib/tomcat6/Flows_.csv (permission denied)
            //create a folder with writing rights: HWOUT
            
            System.out.println("csv text: " + csvText);              
            writeCSVToFile(csvText.toString(), filePath, true);

            
            //System.out.println("Permissions: " + new FilePermission(filePath, "read,write").getActions());
                        
            //writeCSVToFile(csvText.toString(), filePath, false);

            return csvText.toString();            
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR: Cannot read data from database!");
        }
        return null;
    }
}
