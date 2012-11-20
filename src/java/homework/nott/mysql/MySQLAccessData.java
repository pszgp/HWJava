/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql;

import homework.nott.device.DeviceData;
import homework.nott.mysql.sums.HWMySQLEngine;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pszgp, 17 sept 2012: moved the functionalities from CSVParser here
 */
public class MySQLAccessData {
   
    private String pathFile = "data/HW/";
    //private String extensionFile = "_sql_.csv";
    
    public MySQLAccessData(){}
    public MySQLAccessData(String path)//, String extension)
    {
        this.pathFile = path;
        //this.extensionFile = extension;
    }
    //12 july 2012
    public ArrayList<TreeMap<String, Object>> getRecords(String table)
    {
        ArrayList<TreeMap<String, Object>> records = new ArrayList();
        try {            
            String query = "SELECT * FROM "+table;
            ArrayList<TreeMap<String, Object>> result = HWMySQLEngine.getInstance().queryMySQL(query, null);            
            return result;                        
        } catch (Exception ex) {
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return records;
    }    
    
    //11 july 2012
    //String fileSums = "data/London/flows_sums.csv";
                    //"data/HW/flows_sums_.csv";//on server (copy the file on server first)
    //String fileSums = "web/data/flows_sums_.csv";//local: 

    /*public Set<String> getDevicesIps(){
        
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
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return ips;
    }*/
    
    //Read the deviceUsage from the CSV file
    public static final String fileSums = "flows_sums_hours_hw_.csv";
            //"/webapps/hwdashboard/js/flows_sums_hours.csv";
        //"/webapps/homedashboard/js/flows_sums_hw.csv";
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> getDevicesUsageHours(Set<String> devicesIps)
    {
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> devicesUsage = new TreeMap();
        
        BufferedReader br;
        try {
            System.out.println("Read devices usage from csv file: " + new File(fileSums).getAbsolutePath());
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
                
                //System.out.println(line);
                    
                String[] items = line.split(",");
                //The fields are: deviceIp,year,month,day,hour,nbytes
                if (items.length == 6)
                {
                    String deviceIp = items[0];
                    if (deviceIp == null) continue;
                    //if (deviceIp.startsWith("\""))
                    //    deviceIp = deviceIp.substring(1);
                    
                    //System.out.println(deviceIp);
                    //System.out.println(devicesIps + " " + (devicesIps.contains(deviceIp)));
                    //System.exit(0);
                    if (!devicesIps.contains(deviceIp))
                        continue;//ignore ips from devices outside the network
                    
                    int year = Integer.parseInt(items[1]);
                    int month = Integer.parseInt(items[2]);
                    int day = Integer.parseInt(items[3]);
                    int hour = Integer.parseInt(items[4]);
                    long nbytes = Long.parseLong(items[5]);
                    
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> device = new TreeMap();
                    
                    if (devicesUsage.containsKey(deviceIp))
                        device = devicesUsage.get(deviceIp);
                    
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> deviceYear = new TreeMap();
                    
                    if (device.containsKey(year))
                        deviceYear = device.get(year);
                    
                    TreeMap<Integer, TreeMap<Integer, Long>> deviceMonth = new TreeMap();
                    
                    if (deviceYear.containsKey(month))
                        deviceMonth = deviceYear.get(month);
                    
                    TreeMap<Integer, Long> deviceDay = new TreeMap();
                    
                    if (deviceMonth.containsKey(day))
                        deviceDay = deviceMonth.get(day);
                    
                    deviceDay.put(hour, nbytes);
                    
                    deviceMonth.put(day, deviceDay);
                    
                    deviceYear.put(month, deviceMonth);
                    
                    device.put(year, deviceYear);
                    
                    devicesUsage.put(deviceIp, device);
                    
                    //System.out.println("fileSums - line: " + line);
                
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        System.out.println("Devices usage per hours: "+devicesUsage);
        return devicesUsage;
    }
    
    //get the sum per days per devices
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> getDevicesUsageDays(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> devicesUsage)
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
                            TreeMap<Integer, Long> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            Long nbytesDay = 0L;

                            for (Integer hour: hours)
                            {
                                Long nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
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
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //System.out.println(devicesUsageDays);
        return devicesUsageDays;
    }
    
    //get the sum per months per devices
    public TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> getDevicesUsageMonths(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> devicesUsage)
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
                            TreeMap<Integer, Long> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            for (Integer hour: hours)
                            {
                                Long nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
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
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        System.out.println("devicesUsageMonths=" + devicesUsageMonths);
        return devicesUsageMonths;
    }
    //get the sum per years per devices... not used yet in the UI!
    public TreeMap<String, TreeMap<Integer, Long>> getDevicesUsageYears(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> devicesUsage)
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
                    TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> deviceYear = new TreeMap();
                    
                    if (device.containsKey(year))
                        deviceYear = devicesUsage.get(deviceIp).get(year);
                    
                    Set<Integer> months = devicesUsage.get(deviceIp).get(year).keySet();
                    
                    for (Integer month: months)
                    {                        
                        TreeMap<Integer, TreeMap<Integer, Long>> deviceMonth = new TreeMap();
                        
                        if (deviceYear.containsKey(month))
                            deviceMonth = devicesUsage.get(deviceIp).get(year).get(month);
                        
                        Set<Integer> days = devicesUsage.get(deviceIp).get(year).get(month).keySet();
                        
                        for (Integer day: days)
                        {
                            TreeMap<Integer, Long> deviceDay = new TreeMap();

                            if (deviceMonth.containsKey(day))
                                deviceDay = devicesUsage.get(deviceIp).get(year).get(month).get(day);
                            
                            Set<Integer> hours = devicesUsage.get(deviceIp).get(year).get(month).get(day).keySet();
                            
                            for (Integer hour: hours)
                            {
                                Long nbytes = devicesUsage.get(deviceIp).get(year).get(month).get(day).get(hour);
                                nbytesYear += nbytes;
                            }
                        }
                    }
                    device.put(year, nbytesYear);
                }
                devicesUsageYears.put(deviceIp, device);              
            }
        } catch (Exception ex) {
            Logger.getLogger(homework.nott.csv.CSVParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //System.out.println(devicesUsageYears);
        return devicesUsageYears;
    }
           
    public TreeMap<String, Long> getTotalUsageDevices(TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> usage)
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

    //12 july 2012: filter records and let only those with a specific field
    //e.g. save only ips that are in the source address
    public ArrayList<TreeMap<String, Object>> filterRecordsForField(Set<String> ipList, ArrayList<TreeMap<String, Object>> records, String field) {
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

    public ArrayList<DeviceData> getDevicesDownload(Set<String> ipList, ArrayList<TreeMap<String, Object>> flows) {
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
    //17 sept 2012: read from mysql instead of csv files
    public TreeMap<String, TreeMap<String, Integer>> getUrlsForIps(Set<String> ips) {
        //fields of the Urls table: cnt,daddr,dport,hst,last,proto,saddr,sport,uri
        TreeMap<String, TreeMap<String, Integer>> urls = new TreeMap();
        
        ArrayList<TreeMap<String, Object>> records = getRecords("Urls");
        
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

