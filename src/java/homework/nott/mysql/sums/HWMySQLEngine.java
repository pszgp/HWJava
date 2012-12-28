/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql.sums;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import homework.nott.util.Device;
import homework.nott.device.DeviceHistory;

/**
 *
 * @author pszgp, 25 april 2012
 *  mysql access: populate with data from hwdb, access data
 */
public class HWMySQLEngine {
    private static HWMySQLEngine instance;
    public static HWMySQLEngine getInstance(){
        if (instance != null)
            return instance;
        return new HWMySQLEngine();
    }
    java.sql.Connection connMySQL;
    private HWMySQLEngine(){
       try {                       
            //initialize the connection to the mysql database
            connMySQL = HWMySQL.getInstance().connectMySQL();     
            
            //22 nov 2012: as per tom lodge suggested - use views instead of files
            
            //create a function to get the sql timestamp from the field "t"
            //first, check if it exists already
            ArrayList fctExists = queryMySQL("select specific_name from information_schema.ROUTINES where specific_name=\"summary_tstmp\"");
            if (fctExists.size()==0)
                queryMySQL("create function summary_tstmp(t varchar(128)) return timestamp "
                    +" return from_unixtimestamp(conv(substring(t, 2, length(t)), 16, 10) div 1000000000)");
            
            /*
             * queryMySQL("drop view if exists view_devices and view_summary_hours");
            
            //view 1: the ip addresses of devices
            queryMySQL("create view view_devices as (select ipaddr from Leases where action=\"add\")");
            
            //view 2: summary of nbytes per hours per devices
            queryMySQL("create view view_summary_hours as(select ipaddr, year(summary_tstmp(t)) as y, "
                    + "month(summary_tstmp(t)) as m, day(summary_tstmp(t)) as d, hour(summary_tstmp(t)) as h "
                    + "from KFlows, view_devices where (saddr=ipaddr or daddr=ipaddr) "
                    + "group by ipaddr, y, m, d, h");
                    * */
            
       } catch (Exception ex) {
            Logger.getLogger(HwdbEngine.class.getName()).log(Level.SEVERE, null, ex);
       }          
    }
    
    //25 april 2012: get the result of the mysql query
    public ArrayList<TreeMap<String,Object>> queryMySQL(String query, Object... pars)
    {
        if (connMySQL == null) return new ArrayList();//empty list
        
        try {
            ArrayList<TreeMap<String,Object>> rows = new ArrayList();
            
            System.out.println(connMySQL);
            System.out.println(query);
            
            PreparedStatement ps = connMySQL.prepareStatement(query); 
            
            if (pars!=null)
            for (int i=1;i<=pars.length;i++)
            {
                Object parameter = pars[i-1];
                if (parameter instanceof String){
                    System.out.println(parameter+" "+pars.length);
                    //if (true) return null;
                    ps.setString(i, (String)parameter);
                }
                if (parameter instanceof Integer){
                    ps.setInt(i, Integer.parseInt((String)parameter));
                }
                if (parameter instanceof Double){
                    ps.setDouble(i, Double.parseDouble((String)parameter));
                }
                if (parameter instanceof Timestamp){
                    ps.setTimestamp(i, (Timestamp)parameter);
                }
            }
            
            System.out.println("execute mysql query for the query=" + query);
            try{
                ResultSet rs = ps.executeQuery();
                        
                //get the number of columns (from http://www.devdaily.com)
                ResultSetMetaData rsmd = rs.getMetaData();
                int nrColumns = rsmd.getColumnCount();

                System.out.println("get the result from mysql...");
            
                //parse the result set and save into the array, where each hash is the row (field=value...)
                while (rs.next()){
                    TreeMap<String, Object> row = new TreeMap();
                                
                    //System.out.println("result row...");
                    //System.out.println("nrColumns resultset="+nrColumns);

                    for (int i=1;i<=nrColumns;i++){
                        String typeColumn = rsmd.getColumnTypeName(i);
                        String nameColumn = rsmd.getColumnName(i);
                        //System.out.println(typeColumn+" "+nameColumn);
                        Object value = null;
                        if (typeColumn.equals("VARCHAR"))
                            value = rs.getString(i);
                        if (typeColumn.equals("INT")){
                            value = rs.getInt(i);
                        }
                        if (typeColumn.equals("DECIMAL")){
                            value = rs.getLong(i);//15 may 2012
                        }
                        if (typeColumn.equals("DOUBLE"))
                            value = rs.getDouble(i);

                        //System.out.println("row of the query: " + query+" is: " + row);

                        row.put(nameColumn, value);
                    }
                    rows.add(row);
                }                
                //System.out.println("mysql result: " + rows);
                return rows;
                
            }
            catch(Exception e){
                    e.printStackTrace();
                    System.out.println("ERROR: Cannot process mysql result!");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(HWMySQLEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //25 april 2012
    public TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> retrieveMontlyUsagePerDeviceMySQL(String deviceIp)
    {        
        try{
            String queryLeases = "select * from view_devices";//5 dec 2012: read from the view
                //Leases where ipaddr=?";//\""+deviceIp+"\" ";
            Object[] pars = new String[]{deviceIp};
            ArrayList<TreeMap<String, Object>> macaddrMap = queryMySQL(deviceIp, pars);                  
            System.out.println(macaddrMap);            
            String macaddr = (String)macaddrMap.get(0).get("macaddr");            
                        
            System.out.println("macaddr="+macaddr+" for deviceIp="+deviceIp);            
            
            String queryLinks = "select * from Links where macaddr = \""+macaddr+"\"";
            //Object[] pars = new String[]{macaddr};
            ArrayList<TreeMap<String, Object>> linksMap = this.queryMySQL(queryLinks, null);//pars);
            //System.out.println("linksMap="+linksMap);
            
            //if (true)return null;
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> totalUsagePerYearsMonthsDays = new TreeMap();
            //for each record, print the time in the timestamp
            for (TreeMap<String,Object> record: linksMap)
            {
                Timestamp tstmt = HWTimestamp.getTimestampFromUnixString((String) record.get("last"));
                Date date = new Date(tstmt.getTime());                
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy");//roseIndia site: for the correct year
                int year = Integer.parseInt(simpleDateFormat.format(date));                
                int month = date.getMonth();
                int day = date.getDate();
                
                int nbytes = (Integer)record.get("nbytes");        
                
                //add the usage (in bytes) to the year-month-day total usage
                TreeMap<Integer,TreeMap<Integer, Long>> totalUsageMonthOfYear = new TreeMap();                
                if (totalUsagePerYearsMonthsDays.containsKey(year))
                {
                    totalUsageMonthOfYear = totalUsagePerYearsMonthsDays.get(year);
                }                
                long usageDay = nbytes;
                TreeMap<Integer,Long> usageMonth = new TreeMap();
                if (totalUsageMonthOfYear.containsKey(month))
                    usageMonth = totalUsageMonthOfYear.get(month);
                if (usageMonth.containsKey(day))
                    usageDay += usageMonth.get(day);
                usageMonth.put(day, usageDay);
                totalUsageMonthOfYear.put(month, usageMonth);
                totalUsagePerYearsMonthsDays.put(year, totalUsageMonthOfYear);
            }
            System.out.println("Total bandwidth usage per month and years for device ip="+
                                deviceIp+"is: "+totalUsagePerYearsMonthsDays);
            return totalUsagePerYearsMonthsDays;
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot get macaddr from Leases for deviceIp="+deviceIp);
        }
        return null;
    }
    
    //30 april 2012
    public TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> retrieveMontlyUsageFlowsPerDeviceMySQL(String deviceIp)
    {        
        try{
            String queryFlows = "select * from KFlows where saddr = \""+deviceIp+"\"";
            ArrayList<TreeMap<String, Object>> flowsMap = this.queryMySQL(queryFlows, null);
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> totalUsageFlowsPerYearsMonthsDays = new TreeMap();
            //for each record, print the time in the timestamp
            for (TreeMap<String,Object> record: flowsMap)
            {
                Timestamp tstmt = HWTimestamp.getTimestampFromUnixString((String) record.get("last"));
                Date date = new Date(tstmt.getTime());                
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy");//roseIndia site: for the correct year
                int year = Integer.parseInt(simpleDateFormat.format(date));                
                int month = date.getMonth();
                int day = date.getDate();
                
                int nbytes = (Integer)record.get("nbytes");        
                
                //add the usage (in bytes) to the year-month-day total usage
                TreeMap<Integer,TreeMap<Integer, Long>> totalUsageMonthOfYear = new TreeMap();                
                if (totalUsageFlowsPerYearsMonthsDays.containsKey(year))
                {
                    totalUsageMonthOfYear = totalUsageFlowsPerYearsMonthsDays.get(year);
                }                
                long usageDay = nbytes;
                
                TreeMap<Integer,Long> usageMonth = new TreeMap();
                if (totalUsageMonthOfYear.containsKey(month))
                    usageMonth = totalUsageMonthOfYear.get(month);
                if (usageMonth.containsKey(day))
                    usageDay += usageMonth.get(day);
                
                usageMonth.put(day, usageDay);
                
                totalUsageMonthOfYear.put(month, usageMonth);
                totalUsageFlowsPerYearsMonthsDays.put(year, totalUsageMonthOfYear);
            }
            System.out.println("Total bandwidth usage per month and years for device ip="+
                                deviceIp+"is: "+totalUsageFlowsPerYearsMonthsDays);
            return totalUsageFlowsPerYearsMonthsDays;
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot get macaddr from Leases for deviceIp="+deviceIp);
        }
        return null;
    }
    
    //25 april 2012
    public int retrieveTotalUsageForDevice(String deviceIp, String macaddr)
    {
        try{            
            ArrayList<TreeMap<String, Object>> result = this.queryMySQL("select sum(bytes) from HWStatsUsage where ipaddr=\""+deviceIp+"\" ");
            System.out.println("result for ipaddr="+deviceIp+" is: "+result);
            
            int totalUsage = Integer.parseInt(new String( result.get(0).get("sum(bytes)") +""));
            
            System.out.println("total usage for deviceIp="+deviceIp+" is: "+totalUsage);
            
            return totalUsage;
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot get macaddr from Leases for deviceIp="+deviceIp);
        }
        return 0;
    }

    //30 april 2012
    public long retrieveTotalUsageFlowsForDevice(String deviceIp, String macaddr)
    {
        try{
            
            ArrayList<TreeMap<String, Object>> result = this.queryMySQL("select sum(bytes) from HWStatsUsageFlows where ipaddr=\""+deviceIp+"\" ");
            System.out.println("result for ipaddr="+deviceIp+" is: "+result);
            
            long totalUsage = Long.parseLong(new String( result.get(0).get("sum(bytes)") +""));
            
            System.out.println("total usage for deviceIp="+deviceIp+" is: "+totalUsage);
            
            return totalUsage;
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot get macaddr from Leases for deviceIp="+deviceIp);
        }
        return 0;
    }
    
    //26 april 2012
    public void populateStatsUsageDay(String ip, ArrayList<TreeMap<String, Long>> usageIp) {
        try{
            //save the usage of the day in the new table HWStatsUsage
            PreparedStatement ps;
            for (TreeMap<String, Long> dayUsage: usageIp){
                
                if (dayUsage == null) continue;
                if (dayUsage.size() <= 0) continue;
                
                String dateString = (String)(new ArrayList(dayUsage.keySet())).get(0);
               
                Date date = new Date(Date.parse(dateString));
                long nbytes = dayUsage.get(dateString);
                
                                
                ps = connMySQL.prepareStatement("INSERT INTO HWStatsUsage (ipaddr, bytes, date, ipdate) "+
                                                                    "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE bytes = ?");  
                                
                String ipdate = ip + " " + date;
                ps.setString(1, ip);
                ps.setLong(2, nbytes); 
                ps.setDate(3, date);
                ps.setString(4, ipdate);
                ps.setLong(5, nbytes);
                
                System.out.println("populate HWStatsUsage table with details of the device usage per each day.");
                
                ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR saving the statistics usage in the mysql table.");
        }
    }
    
    //30 april 2012: save from flows (the usage of bandwidth) instead of links
    public void populateStatsUsageDayFlows(String ip, ArrayList<TreeMap<String, Long>> usageIp) {
        try{
            //save the usage of the day in the new table HWStatsUsage
            PreparedStatement ps;
            for (TreeMap<String, Long> dayUsage: usageIp){
                
                if (dayUsage == null) continue;
                if (dayUsage.size() <= 0) continue;
                
                String dateString = (String)(new ArrayList(dayUsage.keySet())).get(0);
               
                Date date = new Date(Date.parse(dateString));
                long nbytes = dayUsage.get(dateString);
                
                                
                //30 april 2012: save from flows instead of links
                ps = connMySQL.prepareStatement("INSERT INTO HWStatsUsageFlows (ipaddr, bytes, date, ipdate) "+
                                                                    "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE bytes = ?");  
             
                String ipdate = ip + " " + date;
                ps.setString(1, ip);
                ps.setLong(2, nbytes); 
                ps.setDate(3, date);
                ps.setString(4, ipdate);
                ps.setLong(5, nbytes);
                
                System.out.println("populate HWStatsUsage table with details of the device usage per each day.");
                
                ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR saving the statistics usage in the mysql table.");
        }
    }
    //26 april 2012: get the macaddress for the deviceIp
    String retrieveMacaddr(String ip) {
        
        String macaddr = null;
        ArrayList<TreeMap<String, Object>> mac = this.queryMySQL("SELECT * FROM Leases WHERE ipaddr = " + ip, null);
        if (mac != null) macaddr = (String) mac.get(0).get("macaddr");
        return macaddr;
    }

    //3 july 2012
    public ArrayList<Device> retrieveDevicesDetails()
    {        
            ArrayList<TreeMap<String, Object>> devices = this.queryMySQL("SELECT dn.ip, dn.name, dt.type, a.allowance " + 
                " FROM DeviceNames as dn, DeviceTypes as dt, Allowances as a "+ 
                " WHERE dn.ip=dt.ip AND dn.ip=a.ip", null);
        
        ArrayList<Device> devicesDetails = new ArrayList();
        
        for (TreeMap<String,Object> device: devices)
        {
            if (!device.containsKey("ip"))
                continue;
            String deviceIp = (String) device.get("ip");
            
            TreeMap<String,String> deviceDetails = new TreeMap(device);
            
            String deviceName = (String) device.get("name");
            String deviceType = (String) device.get("type");
            long deviceMonthlyAllowance = (Long) device.get("allowance");    
            ArrayList<TreeMap<String, Object>> usage = this.queryMySQL("SELECT saddr, sum(nbytes) FROM KFlows WHERE saddr = " + deviceIp, null);
            long deviceTotalUsageToPresent = (Long) usage.get(0).get("sum(nbytes)");
            
            deviceDetails.put("Ip", deviceName);
            deviceDetails.put("Name", deviceName);
            deviceDetails.put("Type", deviceName);                
            deviceDetails.put("Usage", ""+deviceTotalUsageToPresent);
            deviceDetails.put("Allowance", ""+deviceMonthlyAllowance);
             
            String tstamp = null; //no timestamp 
            Device d = new Device(tstamp, deviceDetails.get("Ip"), deviceDetails.get("Name"), deviceDetails.get("Type"), deviceDetails.get("User"), deviceMonthlyAllowance, deviceTotalUsageToPresent);

            String macaddr = HWMySQLEngine.getInstance().retrieveMacaddr(d.getIp());
            d.setMacaddr(macaddr);

            devicesDetails.add(d);//deviceDetails);
          
        }
        return devicesDetails;
    }

    //moved from HWEngine, 4 july 2012
    public ArrayList<DeviceHistory> getDevicesHistory() {
        
        ArrayList<DeviceHistory> devices = new ArrayList();
        try
        {
            ArrayList<TreeMap<String, Object>> queryDevices = queryMySQL("SELECT * from HWStatsUsageFlows");
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

    public ArrayList<TreeMap<String, Object>> retrieveUrls() {
        return this.queryMySQL("SELECT * FROM Urls", null);
    }

}
