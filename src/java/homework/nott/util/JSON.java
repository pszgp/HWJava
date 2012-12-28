/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import homework.nott.csv.CSVParser;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author pszgp, 16 april 2012
 */

public class JSON {

    //31 july 2012: convert the csv format into json
    public static String convertArraytoJSON(TreeMap data, String jsonFile)
    {
        String jsonText = convertArraytoJSON(data, 0);
        String path = "";//webapps/homedashboard/js/sunburstchart/";
            //"webapps/Homeworkdashboard/js/sunburstchart/";
        System.out.println("json text: "+jsonText);
        try{
            if (jsonFile!=null)
            {
                //System.out.println("current path: "+new File(".").getAbsolutePath());
                BufferedWriter bw = new BufferedWriter(new FileWriter(path+jsonFile));
                bw.write(jsonText);
                bw.flush();
                bw.close();
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return jsonText;
    }
    //31 july 2012
    public static String convertArraytoJSON(TreeMap data, int level)
    {
        StringBuffer jsonText = new StringBuffer("");
        if (level==0)
            //jsonText = new StringBuffer
            return "{\n\"name\":\"devices\", \n\"children\": [\n"+convertArraytoJSON(data, level+1)+"\n]\n}";
        
        //{"":"","":[{"name": "", "children": ["name":"", "size":0]},{...}}]} 
        /*
         {
            "name": "devices",
            "children": [
            {
            "name": "10.2.0.1",
            "children": [
                {
                    "name": "APRIL",
                        "children": [
                        {"name": "25", 
                        "children": [
                            {"name": "18", "size": "78530591"},
                            {"name": "19", "size": "28882765"}
                        ]},
                        * ....
         */
        int countChildNode = 0;
        for (Object item: data.keySet())
        {            
            if (item==null) continue;
            
            countChildNode++;
            
            String key = item.toString();
            Object value = data.get(item);
            
            //System.out.println(spaceLevel(level));
            jsonText.append("\n"+spaceLevel(level));
            
            if (countChildNode>1)
                jsonText.append(",");
            
            if (value instanceof TreeMap)
            {            
                jsonText.append("{\"name\": \""+key+"\", \"children\":["+convertArraytoJSON((TreeMap)value, level+1)+"\n"+spaceLevel(level)+"]}");
            }
            else
            {
                if (value instanceof Number)
                {
                    jsonText.append("{\"name\": \""+key+"\", \"size\":"+value+"}");
                }
                else continue;
            }
        }
        //System.out.println("json text: "+jsonText.toString());
        return jsonText.toString();
    }
    //31 july 2012
    public static String spaceLevel(int level)
    {
        StringBuffer space = new StringBuffer("");
        for (int i=0;i<level;i++)
            space.append("\t");
        return space.toString();
    }
    
    //mysql to json
    public static void convertMySQLResultToJSON(String[] types, Object[] values, String jsonOutFile){
        Gson gson = new Gson();
        ArrayList data = new ArrayList();
        for (int i=0; i < types.length; i++)
        {
            HashMap<String, Object> row = new HashMap();
        }
	String json = gson.toJson(data);//new String[]{"a", "b"}));         
 
	System.out.println(json);
    }
    
    
}
