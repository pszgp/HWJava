/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.hwdb;

import homework.nott.mysql.HWMySQL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author pszgp, 11 may 2012: update or retrieve devices usage per days
 * table exists already, use the HWStatsUsageFlows instead to read data only
 */
public class HWDevicesEngine {
    private static HWDevicesEngine instance;
    public static HWDevicesEngine getInstance(){
        if (instance != null)
            return instance;
        return new HWDevicesEngine();
    }
    private HWDevicesEngine(){
       try {                       
            //initialize the connection to the mysql database
            connMySQL = HWMySQL.getInstance().connectMySQL();          
            
       } catch (Exception ex) {
            Logger.getLogger(HwdbEngine.class.getName()).log(Level.SEVERE, null, ex);
       }           
    }
    
    java.sql.Connection connMySQL;
    HwdbEngine hw = HwdbEngine.getInstance();
    HWMySQLEngine hwMysql = HWMySQLEngine.getInstance();
    
    //populate the persistent mysql table HWDevicesHistory
    public void updateMySQLHWDevicesHistoryFromHWDB(){        
        if (connMySQL == null) 
            return;
        
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
        
        try{
            
            System.out.println("Devices details: " + devices);
            
            devices = this.getDevicesAllDetails(devices);           
            
            System.out.println("DEVICES...");
            
            //add to the persistent mysql tables the current data contained in the hwdb             
            System.out.println("Populating the urls, links and flows tables...");            
            hwMysql.populateUrlsMySQLFromHwdb();            
            System.out.println("Populated urls...");            
            hwMysql.populateLinksMySQLFromHwdb();            
            System.out.println("Populated links...");            
            hwMysql.populateFlowsMySQLFromHwdb();
            
            System.out.println("Populated flows...");
            
            System.out.println("Populated the mysql tables with hwdb data.");
            
            
            //SAVE THE DEVICES HISTORY (USAGE PER DAYS IN MYSQL TABLE)
            //saveDevicesHistory(devices);
            
            } catch (Exception e) {
                System.exit(1);
            }        
        
    }

    //moved this function from DevicesController to separate the code
    ArrayList<Device> getDevicesAllDetails(ArrayList<Device> devices) {
        if (devices!=null)
        {
            for (Device device: devices)
            {
                System.out.println("... device: " + device);
                
                //mvHWDB.addObject("data", hwMysql.retrieveMontlyUsagePerDeviceMySQL("10.2.0.1"));
                    //hw.retrieveUsagePerMonthsForDevice("10.2.0.1"));          
                    
                TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> usage = 
                        hwMysql.retrieveMontlyUsagePerDeviceMySQL(device.getIp());

                TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> usageFlows = 
                        hwMysql.retrieveMontlyUsageFlowsPerDeviceMySQL(device.getIp());

                System.out.println("usage for device is "+usage); 

                String deviceIp = device.getIp();
                String deviceMacaddr = device.getMacaddr();

                //set the usage of the device
                device.setUsage(usage);
                device.setUsageFlows(usageFlows);
                device.setTotalUsage(hwMysql.retrieveTotalUsageForDevice(deviceIp, deviceMacaddr));
                device.setTotalUsageFlows(hwMysql.retrieveTotalUsageFlowsForDevice(deviceIp, deviceMacaddr));

                ArrayList totalUsageDetails = device.getTotalUsageSplitDetails();
                ArrayList totalUsageDetailsFlows = device.getTotalUsageSplitDetailsFlows();

                System.out.println("HERE...");

                //add date to the mysql table (statistical usage per days per ip of devices)
                hwMysql.populateStatsUsageDay(device.getIp(), totalUsageDetails);
                hwMysql.populateStatsUsageDayFlows(device.getIp(), totalUsageDetailsFlows);

                System.out.println("HERE 2...");
                System.out.println("device=" + device);
            } 
        }
        return devices;
    }
    
    //moved from DashboardController
    //private HashMap<String, ArrayList<Device>> getDevicesPerMonths(ArrayList<Device> devices) {
    public TreeMap<String,TreeMap<Integer, TreeMap<String,TreeMap<Integer, Integer>>>> getDevicesPerMonths(ArrayList<Device> devices) {    
        //4 may 2012: for each device, for each year, month, day, the usage:
        TreeMap<String,TreeMap<Integer, TreeMap<String,TreeMap<Integer, Integer>>>> monthlyUsage = new TreeMap();
        for (Device device: devices)
        {
            String deviceIp = device.getIp();
            
            TreeMap<Integer, TreeMap<String,TreeMap<Integer,Integer>>> deviceUsage = new TreeMap();
            
            if (monthlyUsage.containsKey(deviceIp))
                deviceUsage = monthlyUsage.get(deviceIp);
            
            ArrayList<TreeMap> totalUsage = device.getTotalUsageSplitDetailsFlows();
            
            for (TreeMap<String, Integer> usage: totalUsage)
            {
                String date = (String)(new ArrayList(usage.keySet())).get(0);
                int dataUsage = usage.get(date);
                String[] dateDetails = date.split(" ");
                int day = Integer.parseInt(dateDetails[0]);
                String month = dateDetails[1];
                int year = Integer.parseInt(dateDetails[2]);
                
                TreeMap<String, TreeMap<Integer, Integer>> deviceUsageYear = new TreeMap();
                if (deviceUsage.containsKey(year))
                    deviceUsageYear = deviceUsage.get(year);
                
                TreeMap<Integer, Integer> deviceUsageYearMonth = new TreeMap();
                
                if (deviceUsageYear.containsKey(month))
                    deviceUsageYearMonth = deviceUsageYear.get(month);
                
                int deviceUsageYearMonthDay = dataUsage;
                
                if (deviceUsageYearMonth.containsKey(day))
                    deviceUsageYearMonthDay = deviceUsageYearMonth.get(day);
                
                deviceUsageYearMonth.put(day, dataUsage);
                deviceUsageYear.put(month, deviceUsageYearMonth);
                deviceUsage.put(year, deviceUsageYear);
                
                monthlyUsage.put(deviceIp, deviceUsage);
            }
        }
        
        System.out.println("Devices usage: " + monthlyUsage);
        
        return monthlyUsage;
    }

    //accessed from the controller!!!
    public ArrayList<DeviceHistory> getDevicesHistory() {
        ArrayList<DeviceHistory> devices = new ArrayList();
        try
        {
            ArrayList<TreeMap<String, Object>> queryDevices = this.hwMysql.queryMySQL("SELECT * from HWStatsUsageFlows");
            for (TreeMap<String,Object> record: queryDevices){
                String ip = null;
                String date = null;
                int nbytes = 0;
                for (String key: record.keySet())
                {
                    Object value = record.get(key);
                    if (value!=null)
                    {
                        //System.out.println("KEY="+key);
                        if (key.contains("ipaddr"))
                            ip = (String)value;
                        if (key.contains("date"))
                        {
                            date = (String)value;
                            if (date.contains(" "))
                                date = date.substring(date.indexOf(" ")+1);
                        }
                        if (key.contains("bytes"))
                            nbytes = Integer.parseInt(value.toString());
                    }
                }
                if ((ip!=null) && (date!=null))
                {    
                    DeviceHistory dh = new DeviceHistory(ip, date, nbytes);
                    devices.add(dh);
                }
            }            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR: Cannot read the devices usage history from mysql!");
        }
        devices = DeviceHistory.sortDevicesHistory(devices);
        return devices;
    }

    //to use from the controller, instead of hw or mysql access, 13 may 2012
    public HashMap<String, HashMap<String, Long>> getDevicesHistoryTotalMonths(ArrayList<DeviceHistory> devicesHistory) {
        HashMap<String, HashMap<String, Long>> historyTotals = new HashMap();
        try
        {
            //device=(month=usage)
            for (DeviceHistory dh: devicesHistory)
            {
                String ip = dh.getIp();
                HashMap<String, Long> deviceTotal = new HashMap();
                if (historyTotals.containsKey(ip))
                    deviceTotal = historyTotals.get(ip);
                long sumBytes = dh.getNbytes();
                int month = dh.getMonth();
                String monthName = Device.getMONTHs()[month-1];
                if (deviceTotal.containsKey(monthName))
                    sumBytes += deviceTotal.get(monthName);
                deviceTotal.put(monthName, sumBytes);
                //update the total of bytes
                historyTotals.put(dh.getIp(), deviceTotal);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return historyTotals;
    }
    
    //to use from the controller, instead of hw or mysql access
    public HashMap<String, Long> getDevicesHistoryTotal(ArrayList<DeviceHistory> devicesHistory) {
        HashMap<String, Long> historyTotals = new HashMap();
        try
        {
            for (DeviceHistory dh: devicesHistory)
            {
                String ip = dh.getIp();
                long sumBytes = dh.getNbytes();
                if (historyTotals.containsKey(ip))
                    sumBytes += historyTotals.get(ip);
                //update the total of bytes
                historyTotals.put(dh.getIp(), sumBytes);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return historyTotals;
    }
    /*//save the devices history per days in the mysql HWDevicesHistory table
    private void saveDevicesHistory(ArrayList<Device> devices) {
        
        try{
        if (devices != null)
        {
            PreparedStatement ps;            
            for (Device device: devices)
            {                 
                String deviceIp = device.getIp();
                Timestamp timestamp = new Timestamp(new Date().getTime());
                ArrayList<TreeMap> deviceUsage = device.getTotalUsageSplitDetailsFlows();
                for (TreeMap usage: deviceUsage)
                {
                    String date = (new ArrayList(usage.keySet()).get(0)).toString();
                    int nbytes = Integer.parseInt(usage.get(date).toString());
                    
                    try {                                       
                        //System.out.println("row to be inserted in mysql: " + record);    
                        ps = connMySQL.prepareStatement("INSERT INTO HWDevicesHistory (deviceip, last, date, nbytes) " + 
                                " VALUES (?, ?, ?, ?)");                                        
                        try{
                            ps.setString(1, deviceIp);
                            ps.setString(2, timestamp.toString());
                            ps.setString(3, date);
                            ps.setInt(4, nbytes);
                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.println("ERROR in configuring the ps for mysql query.");
                        };

                        ps.executeUpdate();

                        if (ps!=null) 
                            ps.close();

                        //System.out.println("populate the Links table...");

                    } catch (SQLException ex) {
                        //Logger.getLogger(HwdbMySQLEngine.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                        System.out.println("ERROR MySQL: cannot insert in table Links");
                        return;                    

                    }
                }
            }
        }
        System.out.println("populate the HWDevicesHistory table...");
        }
        catch(Exception e){}
    }*/
}
