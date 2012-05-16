/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.hwdb;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pszgp, 16 april 2012
 */
public class JSON {
    public static void convertMySQLResultToJSON(String[] types, Object[] values, String jsonOutFile){
        Gson gson = new Gson();
        ArrayList data = new ArrayList();
        for (int i=0; i < types.length; i++)
        {
            HashMap<String, Object> row = new HashMap();
            //row.
            
            /*row.put("timestamp", "timestamp");
            row.put("allowance", 10000);
            data.add(row);*/
        }
	String json = gson.toJson(data);//new String[]{"a", "b"}));
         
	/*try {
		//write converted json data to a file named "file.json"
		FileWriter writer = new FileWriter("devices.json");
		writer.write(json);
		writer.close();
 
	} catch (IOException e) {
		e.printStackTrace();
	}*/
 
	System.out.println(json);
    }
    
    public static void main(String[] args) {
       JSON.convertMySQLResultToJSON(new String[]{"timestamp"}, new String[]{"@...@"}, "file");
    }
}
