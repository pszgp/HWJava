/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author pszgp, 27 june 2012, 09:53 am, 11-12july2012 add new functions, comment old functions!!!
 */
public class CSVParser {
    
    //12 july 2012
    public ArrayList<TreeMap<String, Object>> getRecords(String table)
    {
        ArrayList<TreeMap<String, Object>> records = new ArrayList();
        String file = "data/London/"+table+".csv";//"data/HW/"+table+"_sql_.csv";
        try {
            //System.out.println(file);
            //System.out.println(new File(file).getAbsolutePath());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line=null;
            int countLine=0;
            String[] fields = null;
            while((line=br.readLine())!=null)
            {
                countLine++;
                if (countLine==1){        
                    fields = line.split(",");
                    continue;
                }
                if (fields == null)
                    return records;
                String[] items = line.split(",");
                TreeMap<String, Object> record = new TreeMap();
                System.out.println("line="+line+"!!!");
                System.out.println(items.length+ " "+ fields.length);
                //for (int i=0;i<items.length; i++)
                for (int i=0;i<fields.length;i++)
                {
                    record.put(fields[i], items[i]);
                }
                records.add(record);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return records;
    }
    
    
    //11 july 2012
    String fileSums = "data/London/flows_sums.csv";
                    //"data/HW/flows_sums_.csv";//on server (copy the file on server first)
    //String fileSums = "web/data/flows_sums_.csv";//local: 

    public Set<String> getDevicesIps(){
        
        //System.out.println(new File("/HWUIDashboard").exists());
        System.out.println(new File(".").getAbsolutePath());
        
        TreeSet<String> ips = new TreeSet();
        //String file = "WEB-INF/data/flows_sums_.csv";
        try {
            String fileLeases = "data/London/"+"Leases"+"_sql_.csv";
            System.out.println(new File(fileLeases).getAbsoluteFile());
            BufferedReader br = new BufferedReader(new FileReader(fileLeases));//fileSums));
            String line = null;
            int countLine=0;
            while ((line=br.readLine())!=null)
            {
                countLine++;
                if (countLine ==1) continue;
                String[] items = line.split(",");
                if (items.length>1)
                {
                    String ip = items[2];//ipaddr
                    String action = items[0];//action
                    if (action!=null)
                        if (action.equals("add"))
                            ips.add(ip);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return ips;
    }
    
    //Read the deviceUsage from the CSV file
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> getDevicesUsageHours(Set<String> devicesIps)
    {
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage = new TreeMap();
        
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileSums));
            
            String line = null;
            int countLine = 0;
            while ((line=br.readLine())!=null)
            {
                countLine++;
                if (countLine==1) continue;
                
                if (line.contains("\""))
                        line = line.replaceAll("\"", "");//28 aug 2012
                line = line.trim();
                
                System.out.println(line);
                    
                String[] items = line.split(",");
                //The fields are: deviceIp,year,month,day,hour,nbytes
                if (items.length == 6)
                {
                    String deviceIp = items[0];
                    if (deviceIp == null) continue;
                    //if (deviceIp.startsWith("\""))
                    //    deviceIp = deviceIp.substring(1);
                    
                    System.out.println(deviceIp);
                    System.out.println(devicesIps + " " + (devicesIps.contains(deviceIp)));
                    //System.exit(0);
                    if (!devicesIps.contains(deviceIp))
                        continue;//ignore ips from devices outside the network
                    
                    int year = Integer.parseInt(items[1]);
                    int month = Integer.parseInt(items[2]);
                    int day = Integer.parseInt(items[3]);
                    int hour = Integer.parseInt(items[4]);
                    int nbytes = Integer.parseInt(items[5]);
                    
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>> device = new TreeMap();
                    
                    if (devicesUsage.containsKey(deviceIp))
                        device = devicesUsage.get(deviceIp);
                    
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>> deviceYear = new TreeMap();
                    
                    if (device.containsKey(year))
                        deviceYear = device.get(year);
                    
                    TreeMap<Integer, TreeMap<Integer, Integer>> deviceMonth = new TreeMap();
                    
                    if (deviceYear.containsKey(month))
                        deviceMonth = deviceYear.get(month);
                    
                    TreeMap<Integer, Integer> deviceDay = new TreeMap();
                    
                    if (deviceMonth.containsKey(day))
                        deviceDay = deviceMonth.get(day);
                    
                    deviceDay.put(hour, nbytes);
                    
                    deviceMonth.put(day, deviceDay);
                    
                    deviceYear.put(month, deviceMonth);
                    
                    device.put(year, deviceYear);
                    
                    devicesUsage.put(deviceIp, device);
                    
                    System.out.println("fileSums - line: " + line);
                
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        System.out.println("Devices usage per hours: "+devicesUsage);
        return devicesUsage;
    }
    
    //get the sum per days per devices
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> getDevicesUsageDays(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage)
    {
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> devicesUsageDays = new TreeMap();
        
        try {
            
            Set<String> devicesIps = devicesUsage.keySet();
            
            //System.out.println(devicesIps);
            
            for (String deviceIp: devicesIps)
            {
                TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> device = new TreeMap();

                if (devicesUsageDays.containsKey(deviceIp))
                    device = devicesUsageDays.get(deviceIp);
                
                Set<Integer> years = devicesUsage.get(deviceIp).keySet();
                
                //System.out.println(deviceIp);
                
                for (Integer year: years)
                {
                    TreeMap<Integer, TreeMap<Integer, Long>> deviceYear = new TreeMap();

                    //System.out.println(year);
                    
                    if (device.containsKey(year))
                        deviceYear = device.get(year);
                    
                    Set<Integer> months = devicesUsage.get(deviceIp).get(year).keySet();
                    
                    for (Integer month: months)
                    {
                        TreeMap<Integer, Long> deviceMonth = new TreeMap();

                        if (deviceYear.containsKey(month))
                            deviceMonth = deviceYear.get(month);
                        
                        Set<Integer> days = devicesUsage.get(deviceIp).get(year).get(month).keySet();
                        
                        for (Integer day: days)
                        {
                            TreeMap<Integer, Integer> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            Long nbytesDay = 0L;

                            for (Integer hour: hours)
                            {
                                Integer nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
                                nbytesDay += nbytes;
                            }
                            
                            //System.out.println(nbytesDay);
                            //System.exit(0);
                            deviceMonth.put(day, nbytesDay);
                        }
                        deviceYear.put(month, deviceMonth);
                    }
                    device.put(year, deviceYear);
                }
                devicesUsageDays.put(deviceIp, device);              
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //System.out.println(devicesUsageDays);
        return devicesUsageDays;
    }
    
    //get the sum per months per devices
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> getDevicesUsageMonths(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage)
    {
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> devicesUsageMonths = new TreeMap();
        
        try {
            
            Set<String> devicesIps = devicesUsage.keySet();
            
            for (String deviceIp: devicesIps)
            {
                TreeMap<Integer, TreeMap<Integer, Long>> device = new TreeMap();

                if (devicesUsageMonths.containsKey(deviceIp))
                    device = devicesUsageMonths.get(deviceIp);
                
                Set<Integer> years = devicesUsage.get(deviceIp).keySet();
                
                for (Integer year: years)
                {
                    TreeMap<Integer, Long> deviceYear = new TreeMap();

                    if (device.containsKey(year))
                        deviceYear = device.get(year);
                    
                    Set<Integer> months = devicesUsage.get(deviceIp).get(year).keySet();
                    
                    for (Integer month: months)
                    {
                        Long nbytesMonth = 0L;
                        
                        TreeMap<Integer, TreeMap<Integer, Integer>> deviceMonth = new TreeMap();
                        
                        if (deviceYear.containsKey(month))
                            nbytesMonth = deviceYear.get(month);
                        
                        Set<Integer> days = devicesUsage.get(deviceIp).get(year).get(month).keySet();
                        
                        for (Integer day: days)
                        {
                            TreeMap<Integer, Integer> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            for (Integer hour: hours)
                            {
                                Integer nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
                                nbytesMonth += nbytes;
                            }                            
                        }
                        deviceYear.put(month, nbytesMonth);
                    }
                    device.put(year, deviceYear);
                }
                devicesUsageMonths.put(deviceIp, device);              
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        System.out.println("devicesUsageMonths=" + devicesUsageMonths);
        return devicesUsageMonths;
    }
    //get the sum per years per devices... not used yet in the UI!
    public TreeMap<String, TreeMap<Integer, Long>> getDevicesUsageYears(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage)
    {
        TreeMap<String, TreeMap<Integer, Long>> devicesUsageYears = new TreeMap();
        
        try {
            
            Set<String> devicesIps = devicesUsage.keySet();
            
            for (String deviceIp: devicesIps)
            {
                TreeMap<Integer, Long> device = new TreeMap();
                
                Long nbytesYear = 0L;

                if (devicesUsageYears.containsKey(deviceIp))
                    device = devicesUsageYears.get(deviceIp);
                
                Set<Integer> years = devicesUsage.get(deviceIp).keySet();
                
                for (Integer year: years)
                {
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>> deviceYear = new TreeMap();
                    
                    if (device.containsKey(year))
                        deviceYear = devicesUsage.get(deviceIp).get(year);
                    
                    Set<Integer> months = devicesUsage.get(deviceIp).get(year).keySet();
                    
                    for (Integer month: months)
                    {                        
                        TreeMap<Integer, TreeMap<Integer, Integer>> deviceMonth = new TreeMap();
                        
                        if (deviceYear.containsKey(month))
                            deviceMonth = devicesUsage.get(deviceIp).get(year).get(month);
                        
                        Set<Integer> days = devicesUsage.get(deviceIp).get(year).get(month).keySet();
                        
                        for (Integer day: days)
                        {
                            TreeMap<Integer, Integer> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            for (Integer hour: hours)
                            {
                                Integer nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
                                nbytesYear += nbytes;
                            }
                        }
                    }
                    device.put(year, nbytesYear);
                }
                devicesUsageYears.put(deviceIp, device);              
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //System.out.println(devicesUsageYears);
        return devicesUsageYears;
    }
    
    /*public String CSVtoString(TreeMap map)
    {
        String text=null;
        if (map!=null)
        {
            for (Object o1: map.keySet())
            {
                Object o2 = map.get(o1);
                text+=o1+",";
                if (o1 instanceof TreeMap){
                    Object o3 = ((TreeMap)o1).get(o2);
                    text+=o2+",";
                    if (o2 instanceof TreeMap){
                        Object o4 = ((TreeMap)o2).get(o3);
                        text+=o3+",";
                        if (o4 instanceof TreeMap){
                            Object o5 = ((TreeMap)o3).get(o4);
                            text+=o4+",";
                            if (o2 instanceof TreeMap){
                                Object o6 = ((TreeMap)o4).get(o5);
                                text+=o5+",";
                                text+=o6;
                                text+="\n";
                            }
                            text+="\n";    
                        }
                        text+="\n";
                    }
                    text+="\n";
                }
                text+="\n";
            }
        }
        return text;
    }*/
    
    public TreeMap<String, Long> getTotalUsageDevices(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> usage)
    {
        TreeMap<String, Long> totalUsage = new TreeMap();
        if (usage!=null){
            TreeMap<String, TreeMap<Integer, Long>> usageYears = this.getDevicesUsageYears(usage);
            if (usageYears!=null)
            {
                for (String deviceIp: usageYears.keySet())
                {                    
                    long nbytes = 0;
                    TreeMap<Integer, Long> yearUsage = usageYears.get(deviceIp);
                    if (yearUsage!=null)
                    {
                        for (Integer year: yearUsage.keySet()){
                            Long yearNBytes = yearUsage.get(year);
                            nbytes+=yearNBytes;
                        }
                    }
                    totalUsage.put(deviceIp, nbytes);
                }
            }
        }
        System.out.println("totalUsage=" + totalUsage);
        return totalUsage;
    }
    
    //test: function main
    public static void main(String[] args)
    {     
        System.out.println();
        System.exit(0);
        CSVParser csv = new CSVParser();
        //System.out.println(csv.getRecords("DeviceNames"));
        System.out.println(csv.getUrlsForIps(csv.getDevicesIps()));
        System.exit(0);
        System.out.println(csv.getDevicesIps());
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> usage = 
                csv.getDevicesUsageHours(csv.getDevicesIps());
        System.out.println(usage);
        System.out.println(csv.getTotalUsageDevices(usage));
        //TreeMap o = new CSVParser().getDevicesUsageDays(usage);
        //System.out.println(new CSVParser().CSVtoString(o));
        System.out.println(csv.getDevicesUsageMonths(usage));
        System.out.println(csv.getDevicesUsageYears(usage));
        
        System.exit(0);
        
        /*String urlsPath = "web/WEB-INF/HWOUT/Urls_sql_.csv";
        try {
            System.out.println(new File(".").getAbsolutePath());
            //cnt,daddr,dport,hst,last,proto,saddr,sport,uri
            HashMap<String, HashMap<String, Integer>> devicesUrls = new HashMap();
            BufferedReader br = new BufferedReader(new FileReader(urlsPath));
            String line="";            
            while ((line = br.readLine()) != null)
            {
                if (line.contains("saddr"))
                    continue;//skip the fields names line (first line)
                String[] fields = line.split(",");
                String uri = fields[3];//hst field (website, not page)
                String device = fields[6];//saddr field (device ip)
                HashMap<String, Integer> urlsMap = new HashMap();
                if (devicesUrls.containsKey(device))
                    urlsMap = devicesUrls.get(device);
                int count = 0;
                if (urlsMap.containsKey(uri))
                    count = urlsMap.get(uri);
                count++;
                urlsMap.put(uri, count);
                devicesUrls.put(device, urlsMap);
                //if (!uri.contains("10.2.0.2:8080"))
                //    System.out.println(line);
            }
            System.out.println("devices urls frequencies: " + devicesUrls);
            
            //convert to json
            Gson gson = new Gson();
            //ArrayList jsonData = new ArrayList();
            HashMap<String, Object> jsonData = new HashMap();
            //HashMap<String, Object> mapRow = new HashMap();
            //jsonData.add(mapRow);
            System.out.println(gson.toJson(devicesUrls));//jsonData));
            
            HashMap<String, Object> type = new HashMap();
            type.put("_id", "/type/device");
            type.put("name", "Devices");
            class MyJSON{
                MyJSON(){};
            }
            try {
                System.out.println(gson.fromJson("{a:b}", MyJSON.class));//Class.forName("MyJSON")));
            
                //convert the json string back to object
		String obj = gson.fromJson("{a:b}", String.class);
                
                JsonElement element = gson.fromJson("{ \"a\": \"A\", \"b\": true }",  JsonElement.class ); 
                JsonObject object = element.getAsJsonObject(); 
                System.out.println(gson);
                
            }catch(Exception ex){ 
            //catch (ClassNotFoundException ex) {
                Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            //type.put("properties", properties);
            jsonData.put("type", type);
            /*
             {
        "type": {
            "properties": {
            "name": {"name": "Device Name", "type": "string" },
            "uri": {"name": "Visited websites", "type": "string" },
            "nbytes": { "name": "Number of bytes", "type": "number" },
            "nuris": { "name": "Number of visited websites", "type": "number" }
            },
            "indexes": {
            "by_name": ["name"]
            }
        },
        "objects": [
            {
            "_id": "d1",
            "name": "Device_1",
            "devices": ["google.co.uk", "mrl.nott.ac.uk"],
            "nbytes": 83,
            "nuris": 20
            },
            {
            "_id": "d2",
            "name": "Device_2",
            "languages": ["google.co.uk"],
            "nbytes": 82,
            "nuris": 10
            },
            {
            "_id": "d3",
            "name": "Device_3",
            "languages": ["google.co.uk", "microsoft.com", "guardian.co.uk", "linkedin.com", "bbc.co.uk"],
            "nbytes": 311,
            "nuris": 50
            },
            {
            "_id": "d4",
            "name": "Device_4",
            "languages": ["microsoft.com"],
            "nbytes": 62.3,
            "nuris": 10
            },
            {
            "_id": "d5",
            "name": "Device_5",
            "languages": ["guardian.co.uk"],
            "nbytes": 30.6,
            "nuris": 10
            },
            {
            "_id": "d6",
            "name": "Device_6",
            "languages": ["microsoft.com", "bbc.co.uk", "guardian.co.uk"],
            "nbytes": 40.1,
            "nuris": 30
            }
        ]
        };  
             
             */
                        
            
      /*  } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            * */            
    }

    //12 july 2012: filter records and let only those with a specific field
    //e.g. save only ips that are in the source address
    ArrayList<TreeMap<String, Object>> filterRecordsForField(Set<String> ipList, ArrayList<TreeMap<String, Object>> records, String field) {
        ArrayList<TreeMap<String, Object>> filteredRecords = new ArrayList();
        for (TreeMap<String, Object> record: records)
        {
            if (record==null) continue;
            if (!record.containsKey(field)) continue;
            String value = (String)record.get(field);
            if (value==null)continue;
            if (ipList.contains(value))
                filteredRecords.add(record);
        }
        return filteredRecords;
    }

    ArrayList<DeviceData> getDevicesDownload(Set<String> ipList, ArrayList<TreeMap<String, Object>> flows) {
        ArrayList<DeviceData> devices = new ArrayList();
        for (TreeMap<String, Object> record: flows)
        {
            if (record==null)continue;
            try{
                String ip = (String)record.get("saddr");            
                if (ipList.contains(ip))
                {
                    int nbytes = Integer.parseInt((String)record.get("nbytes"));
                    Timestamp t = new Timestamp(Long.parseLong((String)record.get("t")));
                    int day = t.getDate();
                    int month = t.getMonth();
                    int year = 1900+t.getYear();
                    DeviceData d = new DeviceData(ip, 0, day, month, year, t, nbytes, 0, nbytes);
                    devices.add(d);
                }
            }catch(Exception e){e.printStackTrace();}
        }
        return devices;
    }

    //17 july 2012
    public TreeMap<String, TreeMap<String, Integer>> getUrlsForIps(Set<String> ips) {
        //fields of the Urls table: 
        //  cnt,daddr,dport,hst,last,proto,saddr,sport,uri
        TreeMap<String, TreeMap<String, Integer>> urls = new TreeMap();
        ArrayList<TreeMap<String, Object>> records = this.getRecords("Urls");
        for (TreeMap<String, Object> record: records)
        {
            //sum uri occurences for uploads and downloads
            String uri = (String)record.get("hst");//first part of the url
            
            String device = (String)record.get("saddr");
            if (ips.contains(device))
            {
                TreeMap<String, Integer> deviceUrls = new TreeMap();
                if (urls.containsKey(device))
                {
                    deviceUrls = urls.get(device);
                }
                int occur = 0;
                if (deviceUrls.containsKey(uri))
                {
                    occur = deviceUrls.get(uri);
                }
                occur++;
                deviceUrls.put(uri, occur);
                urls.put(device, deviceUrls);
            }
            device = (String)record.get("daddr");
            if (ips.contains(device))
            {
                TreeMap<String, Integer> deviceUrls = new TreeMap();
                if (urls.containsKey(device))
                {
                    deviceUrls = urls.get(device);
                }
                int occur = 0;
                if (deviceUrls.containsKey(uri))
                {
                    occur = deviceUrls.get(uri);
                }
                occur++;
                deviceUrls.put(uri, occur);
                urls.put(device, deviceUrls);
            }
        }
        return urls;
    }
}
