/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.hwdb;

import homework.nott.mysql.HWMySQL;
import homework.nott.mysql.HWTimestamp;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
//import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;

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
            
       } catch (Exception ex) {
            Logger.getLogger(HwdbEngine.class.getName()).log(Level.SEVERE, null, ex);
       }
           
    }
    public void populateUrlsMySQLFromHwdb()
    {        
        if (connMySQL == null) 
            return;
        
        String resultUrls = HwdbEngine.getInstance().retrieveUrlsFromHWDB();
        
        if (resultUrls != null)
        {
            ArrayList<TreeMap<String, String>> urls = HwdbEngine.getInstance().parseHWDBResult(resultUrls);
            
            PreparedStatement ps;
            //String[] rows = resultUrls.split("\n");
            
            //for (int i=0;i<rows.length;i++)
            for (TreeMap<String,String> urlRecord: urls)
            {                 
                try {
                    //String[] row = rows[i].split("<\\|>");
                    
                    //skip rows with less elements (not containing the required data)
                    //if (rows.length != 9) continue;
                    
                    //skip the rows containing the fields names
                    //if (!row[0].contains("timestamp"))continue;
                    
                    //System.out.println("row to be inserted in mysql: " + urlRecord);
                    
                    //System.out.println("mysql connection: "+connMySQL);
                    ps = connMySQL.prepareStatement("INSERT INTO Urls (proto, saddr, sport, daddr, dport, hst, uri, cnt, last) " + 
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE saddr = ?");
                    //System.out.println("here...");
                    /*ps.setString(0, row[1]);
                    ps.setInt(1, Integer.parseInt(row[2]));
                    ps.setString(2, row[3]);
                    ps.setInt(3, Integer.parseInt(row[4]));
                    ps.setString(4, row[5]);
                    ps.setString(5, row[6]);
                    ps.setString(6, row[7]);
                    ps.setString(7, row[0]);//timestamp
                    ps.setInt(8, Integer.parseInt(row[0]));*/
                    
                    try{
                        ps.setInt(1, Integer.parseInt(urlRecord.get("integer:proto")));      
                        //System.out.println("parameter 1");
                        ps.setString(2, urlRecord.get("varchar:saddr"));
                        //System.out.println("parameter 2");
                        ps.setInt(3, Integer.parseInt(urlRecord.get("integer:sport")));
                        //System.out.println("parameter 3");
                        ps.setString(4, urlRecord.get("varchar:daddr"));
                        //System.out.println("parameter 4");
                        ps.setInt(5, Integer.parseInt(urlRecord.get("integer:dport")));
                        //System.out.println("parameter 5");
                        ps.setString(6, urlRecord.get("varchar:hst"));
                        //System.out.println("parameter 6");
                        ps.setString(7, urlRecord.get("varchar:uri"));
                        //System.out.println("parameter 7");
                        ps.setString(8, urlRecord.get("varchar:cnt"));
                        //System.out.println("parameter 8");
                        ps.setString(9, urlRecord.get("timestamp:timestamp"));
                        //System.out.println("parameter 9");
                        ps.setString(10, urlRecord.get("varchar:saddr"));
                        //ps.setString(10, urlRecord.get("timestamp:timestamp"));
                        //System.out.println("parameter 10");
                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("ERROR in configuring the ps for mysql query.");
                    };
                    
                    //System.out.println("ps="+ps);
                    ps.executeUpdate();
                    
                    if (ps!=null) 
                        ps.close();
                    
                    /*//test - see what was inserted
                    ps = connMySQL.prepareStatement("SELECT * from Urls");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next())
                    {
                        System.out.println("value" + rs.getString(0));
                    }*/
                    
                } catch (SQLException ex) {
                    //Logger.getLogger(HwdbMySQLEngine.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    System.out.println("ERROR MySQL: cannot insert in table Urls");
                    return;                    
                    
                }
            }
        }
        System.out.println("populated urls...!!!");
    }
    
    /* add links to the persistent (mysql) table */
    public void populateLinksMySQLFromHwdb()
    {        
        if (connMySQL == null) 
            return;
        
        String resultLinks = HwdbEngine.getInstance().retrieveLinksFromHWDB();
        
        if (resultLinks != null)
        {
            ArrayList<TreeMap<String, String>> links = HwdbEngine.getInstance().parseHWDBResult(resultLinks);
          
            PreparedStatement ps;            
            for (TreeMap<String,String> record: links)
            {                 
                try {                                       
                    //System.out.println("row to be inserted in mysql: " + record);    
                    ps = connMySQL.prepareStatement("INSERT INTO Links (macaddr, rssi, nretries, npkts, nbytes, last) " + 
                            " VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE macaddr = ?");                                        
                    try{
                        ps.setString(1, record.get("varchar:macaddr"));      
                        ps.setDouble(2, Double.parseDouble(record.get("real:rssi")));
                        ps.setInt(3, Integer.parseInt(record.get("integer:nretries")));
                        ps.setInt(4, Integer.parseInt(record.get("integer:npkts")));
                        ps.setInt(5, Integer.parseInt(record.get("integer:nbytes")));
                        ps.setString(6, record.get("timestamp:timestamp"));   
                        ps.setString(7, record.get("varchar:macaddr"));
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
        System.out.println("populate the Links table...");
    }
    
    /* add flows to the persistent (mysql) table */
    public void populateFlowsMySQLFromHwdb()
    {        
        if (connMySQL == null) 
            return;
        
        String resultFlows = HwdbEngine.getInstance().retrieveFlowsFromHWDB();
        
        if (resultFlows != null)
        {
            ArrayList<TreeMap<String, String>> links = HwdbEngine.getInstance().parseHWDBResult(resultFlows);
          
            PreparedStatement ps;            
            for (TreeMap<String,String> record: links)
            {                 
                try {                                       
                    //System.out.println("row to be inserted in mysql: " + record);    
                    ps = connMySQL.prepareStatement("INSERT INTO Flows (proto, saddr, sport, daddr, dport, npkts, nbytes, last) " + 
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE proto = ?");                                        
                    try{
                        ps.setInt(1, Integer.parseInt(record.get("integer:proto")));      
                        ps.setString(2, record.get("varchar:saddr"));
                        ps.setInt(3, Integer.parseInt(record.get("integer:sport")));
                        ps.setString(4, record.get("varchar:daddr"));
                        ps.setInt(5, Integer.parseInt(record.get("integer:dport")));
                        ps.setInt(6, Integer.parseInt(record.get("integer:npkts")));   
                        ps.setInt(7, Integer.parseInt(record.get("integer:nbytes")));
                        ps.setString(8, record.get("timestamp:timestamp"));
                        ps.setInt(9, Integer.parseInt(record.get("integer:proto")));
                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("ERROR in configuring the ps for mysql query.");
                    };
                    
                    ps.executeUpdate();
                    
                    if (ps!=null) 
                        ps.close();
                    
                    //System.out.println("populate the Flows table...");
                    
                } catch (SQLException ex) {
                    //Logger.getLogger(HwdbMySQLEngine.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    System.out.println("ERROR MySQL: cannot insert in table Links");
                    return;                    
                    
                }
            }
        }
        System.out.println("populate the Flows table...!!!!!!!!!!!!!!!");
    }
    //25 april 2012: get the result of the mysql query
    public ArrayList<TreeMap<String,Object>> queryMySQL(String query, Object... pars)
    {
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
            /*String queryLeases = "select * from Leases where ipaddr=?";//\""+deviceIp+"\" ";
            Object[] pars = new String[]{deviceIp};
            ArrayList<HashMap<String, Object>> macaddrMap = this.queryMySQL(deviceIp, pars);                  
            System.out.println(macaddrMap);            
            String macaddr = (String)macaddrMap.get(0).get("macaddr");            
            
            */
            HwdbEngine hw = HwdbEngine.getInstance();
            String queryLeases = "SQL:select * from Leases where ipaddr=\""+deviceIp+"\" ";
            String macaddrString = hw.callHWDB(queryLeases);
            ArrayList<TreeMap<String, String>> macaddrMap = HwdbEngine.getInstance().parseHWDBResult(macaddrString);
            System.out.println(macaddrMap);
            String macaddr = macaddrMap.get(0).get("varchar:macaddr");
            
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
                
                //System.out.println("record="+record);
                //System.out.println("timestamp: year="+year+" month="+month+" day="+day);
                
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
            String queryFlows = "select * from Flows where saddr = \""+deviceIp+"\"";
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
            /*ArrayList<HashMap<String, Object>> linksResult = this.queryMySQL("select sum(nbytes) from Links where macaddr='"+macaddr+"'");
            System.out.println("links result for macaddr="+macaddr+" is: "+linksResult);
            
            int totalUsage = Integer.parseInt((String) linksResult.get(0).get("sum(nbytes)"));*/
            
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
        
            HwdbEngine hw = HwdbEngine.getInstance();
            
            String queryLeases = "SQL:select * from Leases where ipaddr=\""+ip+"\" ";
            
            String macaddrString = hw.callHWDB(queryLeases);
            
            ArrayList<TreeMap<String, String>> macaddrMap = HwdbEngine.getInstance().parseHWDBResult(macaddrString);
            System.out.println(macaddrMap);
            
            String macaddr = macaddrMap.get(0).get("varchar:macaddr");
            
            return macaddr;
    }
}
