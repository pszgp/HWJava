/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql.sums;

/*import homework.nott.mysql.HWMySQL;
import homework.nott.mysql.HWTimestamp;
import homework.nott.webcontroller.DashboardController;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;*/

/**
 *
 * @author pszgp, 16-17 apr 2012
 */
public class HwdbEngine {
   
  /*  private static HwdbEngine instance;
    public static HwdbEngine getInstance(){
        if (instance != null)
            return instance;
        return new HwdbEngine();
    }
    Connection conn;
    java.sql.Connection connMySQL;
    private HwdbEngine(){//}
    
    //private void HwdbEngineConnect(){
       try {
            //initialize the connection to the hwdb database
            String serviceName = "Handler";
            SRPC srpc = new SRPC();
            Service service = srpc.offer(serviceName);
            
            System.out.println("service: " + service.getName());
            
            conn = srpc.connect("localhost", 987,"HWDB");
            int port = srpc.details().getPort();
            
            System.out.println("port: " + port);                  
            
        } catch (IOException ex) {
            Logger.getLogger(HwdbEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    //16-19 april 2012
    public String retrieveUrlsFromHWDB(){
        return callHWDB("SQL:select * from Urls");
    }
    //24 april 2012
    public String retrieveUsage(){
        return callHWDB("SQL:select * from BWUsage");
    }
    public String retrieveAllowance(){
        return callHWDB("SQL:select * from Allowances");
    }
    
    //23 april 2012
    public ArrayList<Device> retrieveDevicesDetailsFromHWDB()
    {        
        //Note: Hwdb does not allow to use a multiple table selection...
        String deviceNamesString = this.retrieveDeviceNamesFromHWDB();
        String deviceTypesString = this.retrieveDeviceTypesFromHWDB();
        String usersNamesString = this.retrieveUsersFromHWDB();
        
        String bwusagesString = this.retrieveUsage();//24 april 2012, bwusage + allowance
        String allowancesString = this.retrieveAllowance();//these can be null
        
        if ((deviceNamesString == null) || (deviceTypesString == null) || (usersNamesString == null))
            return null;
        
        ArrayList<TreeMap<String,String>> deviceNames = this.parseHWDBResult(deviceNamesString);
        //System.out.println("deviceNames="+deviceNames);
        ArrayList<TreeMap<String,String>> deviceTypes = this.parseHWDBResult(deviceTypesString);
        //System.out.println("deviceTypes="+deviceTypes);
        ArrayList<TreeMap<String,String>> usersNames = this.parseHWDBResult(usersNamesString);
        //System.out.println("usersNames="+usersNames);
        
        ArrayList<TreeMap<String,String>> bwusages = this.parseHWDBResult(bwusagesString);
        ArrayList<TreeMap<String,String>> allowances = this.parseHWDBResult(allowancesString);
        
        if ((deviceNames == null) || (deviceTypes == null) || (usersNames == null))
            return null;
        if ((deviceNames.size()==0) || (deviceTypes.size() == 0) || (usersNames.size() == 0)) 
            return null;
        Set<String> deviceNamesFields = deviceNames.get(0).keySet();//(String[]) deviceNames.get(0).keySet().toArray();
        Set<String> deviceTypesFields = deviceTypes.get(0).keySet();
        Set<String> usersNamesFields = usersNames.get(0).keySet();
                
        if (!usersNamesFields.contains("varchar:ip")) return null;
        if (!deviceNamesFields.contains("varchar:ip")) return null;
        if (!deviceTypesFields.contains("varchar:ip")) return null;
        
        ArrayList<Device> devicesDetails = new ArrayList();
        
        for (TreeMap<String,String> device: deviceNames)
        {
            if (!device.containsKey("varchar:ip"))
                continue;
            String ip = device.get("varchar:ip");
            
            TreeMap<String,String> deviceDetails = new TreeMap(device);
                    
            TreeMap<String, String> userIp = this.findRecord(usersNames, "varchar:ip", ip);
            TreeMap<String, String> typeIp = this.findRecord(deviceTypes, "varchar:ip", ip);
            TreeMap<String, String> usageIp = this.findRecord(bwusages, "varchar:ip", ip);
            TreeMap<String, String> allowanceIp = this.findRecord(allowances, "varchar:ip", ip);
            
            System.out.println("usage for device is: " + usageIp);
            System.out.println("allowance for device is: " + allowanceIp);
            
            try{
                System.out.println("Saving the device details...");
                //save device details //3 may 2012: add the try/catch block
                if (userIp!=null){
                    deviceDetails.put("User", userIp.get("varchar:name"));
                }
                else
                    deviceDetails.put("User", null);

                if (typeIp!=null)
                    deviceDetails.put("Type", typeIp.get("varchar:type"));
                else
                    deviceDetails.put("Type", null);

                if (usageIp!=null)
                    deviceDetails.put("Usage", usageIp.get("integer:nbytes"));
                else 
                    deviceDetails.put("Usage", null);

                if (allowanceIp!=null)
                    deviceDetails.put("Allowance", allowanceIp.get("integer:allowance"));
                else
                    deviceDetails.put("Allowance", null);

                deviceDetails.put("Ip", ip);
                deviceDetails.remove("varchar:ip");

                deviceDetails.put("Name", device.get("varchar:name"));
                deviceDetails.remove("varchar:name");

                deviceDetails.put("Timestamp", ""+HWTimestamp.getTimestampFromUnixString(device.get("timestamp:timestamp")));
                deviceDetails.remove("timestamp:timestamp");   

                int allowance = 0;
                if (deviceDetails.get("Allowance")!=null)
                    allowance = Integer.parseInt(deviceDetails.get("Allowance"));

                int nbytes = 0;
                if (deviceDetails.get("Usage")!=null)
                    nbytes = Integer.parseInt(deviceDetails.get("Usage"));

                Device d = new Device(deviceDetails.get("Timestamp"), deviceDetails.get("Ip"), deviceDetails.get("Name"), deviceDetails.get("Type"), deviceDetails.get("User"), allowance, nbytes);

                String macaddr = HWMySQLEngine.getInstance().retrieveMacaddr(d.getIp());
                d.setMacaddr(macaddr);

                devicesDetails.add(d);//deviceDetails);
            }catch(Exception e){
                System.out.println("ERROR: Cannot save the device details into the map variable.");
                e.printStackTrace();
            }
        }
        return devicesDetails;
    }
    
    // find the record of an unique field (such as IP) and value 
    public TreeMap<String,String> findRecord(ArrayList<TreeMap<String,String>> records, String field, String value)
    {
        if (records == null) return null;
        if (records.size() == 0) return null;
        
        for (TreeMap<String,String> record: records)
        {
            if (record.containsKey(field))
                if (record.containsValue(value))
                    return record;
        }        
        return null;
    }
    
    // parse the hwdb query result
    ArrayList<TreeMap<String,String>> parseHWDBResult(String query)
    {
        ArrayList<TreeMap<String,String>> listRecords = new ArrayList();
        if (query == null) 
            return null;
        try{
            String[] lines = query.split("\n");        
            int len = lines.length;
            
            //24 april 2012: to avoid memory problems, parse fragments of the result and then merge them
            final int nrMaxRows = 10;
            if (len > nrMaxRows)
            {
                for (int i=0; i<len; i=i+nrMaxRows)
                {
                    String[] fragment = Arrays.copyOf(lines, nrMaxRows);
                    
                    System.out.println("fragment...");
                    
                    StringBuffer sb = new StringBuffer();
                    
                    for (int j=0;j<nrMaxRows;j++)
                        sb = new StringBuffer(sb).append(fragment[j]).append("\n");
                    
                    String queryFragment = sb.toString();                    
                    
                    listRecords.addAll(parseHWDBResult(queryFragment));
                    
                }
                return listRecords;
            }*/
      /*     
            if (len <=1) 
                return null;
            if (!lines[0].contains("Success")) 
                return null;
            //get the number of rows (nr records) and columns (nr fields)
            String[] itemsResultFirstLine = lines[0].split("<\\|>");
            if (itemsResultFirstLine.length != 4)
                return null;
            
            int nrRecords = Integer.parseInt(itemsResultFirstLine[3]);
            int nrFields = Integer.parseInt(itemsResultFirstLine[2]);
            
            System.out.println("nrRecords = " + nrRecords + " nrFields = " + nrFields);
            if (nrRecords > 0)
            {
                String[] fields = lines[1].split("<\\|>");
                //if (fields.length != nrFields) 
                //    return null;
                for (int i=2; i<lines.length; i++)
                {
                    TreeMap<String, String> record = new TreeMap();
                    String[] values = lines[i].split("<\\|>");//records start at line 2
                    for (int j=0; j<nrFields; j++)
                    {
                        record.put(fields[j], values[j]);
                    }
                    
                    listRecords.add(record);
                }
            }
            //System.out.println(listRecords);
            return listRecords;
            
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR in parsing the HWDB query's result for query:\n"+query.length()+".");
            //e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> retrieveUsersNamesFromHWDB()
    {        
        ArrayList<String> result = new ArrayList();
        String usersString = callHWDB("SQL:select name from Users");
        ArrayList<TreeMap<String, String>> users = this.parseHWDBResult(usersString);
        for (TreeMap<String,String> user: users)
        {
            result.add(user.get("varchar:name"));
        }
        System.out.println("The users are: " + users + " " + result);
        return result;
    }
    //23 april 2012
    public String retrieveUsersFromHWDB()
    {
        //test without hwdb:
        //return "0<|>Success<|>3<|>2<|>"+
         //          "\ntimestamp:timestamp<|>varchar:ip<|>varchar:name<|>"+
          //         "\n@1287852a9e952300@<|>10.2.0.1<|>User_1<|>"+
          //         "\n@1287852b515f7df0@<|>10.2.0.17<|>Users_2<|>";
        
        return callHWDB("SQL:select * from Users");
    }
    public String retrieveDeviceNamesFromHWDB()
    {
         //test without hwdb:
         //return "0<|>Success<|>3<|>2<|>"+
                //   "\ntimestamp:timestamp<|>varchar:ip<|>varchar:name<|>"+
                //   "\n@1287852a9e952300@<|>10.2.0.1<|>Device_1<|>"+
                 //  "\n@1287852b515f7df0@<|>10.2.0.17<|>Device_2<|>"; 
        
         return callHWDB("SQL:select * from DeviceNames");
    }
    public String retrieveDeviceTypesFromHWDB()
    {
         //test without hwdb:
        //return "0<|>Success<|>3<|>2<|>"+
             //      "\ntimestamp:timestamp<|>varchar:ip<|>varchar:type<|>"+
              //     "\n@1287852a9e952300@<|>10.2.0.1<|>Laptop<|>"+
              //     "\n@1287852b515f7df0@<|>10.2.0.17<|>Mac<|>";
        
        return callHWDB("SQL:select * from DeviceTypes");
    }
    
    //24 april 2012: Flows, Links, Urls
    public String retrieveFlowsFromHWDB()
    {
        return callHWDB("SQL:select * from Flows");
    }
    public String retrieveLinksFromHWDB()
    {
        return callHWDB("SQL:select * from Links");
    }
    public String retrieveLeasesFromHWDB()
    {
        return callHWDB("SQL:select * from Leases");
    }
    public ArrayList<FlowLinkUrl> retrieveFlowsDetails(){
        String flowsString = this.retrieveFlowsFromHWDB();
        String linksString = this.retrieveLinksFromHWDB();
        String urlsString = this.retrieveUrlsFromHWDB();
        String leasesString = this.retrieveLeasesFromHWDB();
        
        ArrayList<TreeMap<String, String>> flows = this.parseHWDBResult(flowsString);
        ArrayList<TreeMap<String, String>> urls = this.parseHWDBResult(urlsString);
        ArrayList<TreeMap<String, String>> leases = this.parseHWDBResult(leasesString);
        ArrayList<TreeMap<String, String>> links = this.parseHWDBResult(linksString);
        
        ArrayList<FlowLinkUrl> flowsDetails = new ArrayList();
        
        for (TreeMap<String,String> flow: flows){
            String ip = flow.get("varchar:saddr");
            int sport = 0;
            int dport = 0;
            String daddr = flow.get("varchar.daddr");
            String timestamp = flow.get("timestamp:timestamp");
            int proto = 0;
            
            try{
                sport = Integer.parseInt(flow.get("integer:sport"));
                dport = Integer.parseInt(flow.get("integer:dport"));
                proto = Integer.parseInt(flow.get("integer:proto"));
                
            }catch(Exception e){e.printStackTrace();}
            
            ArrayList<TreeMap<String, String>> leasesIp = this.findAllRecords(leases, "varchar:ipaddr", ip);
            ArrayList<TreeMap<String, String>> urlsIp = this.findAllRecords(urls, "varchar:saddr", ip);
            for (TreeMap<String,String> url: urlsIp)
            {
                String hst = url.get("hst");
                String uri = url.get("url");
                String cnt = url.get("cnt");
                
                for (TreeMap<String,String> lease: leasesIp)
                {
                    String hostname = lease.get("varchar:hostname");
                    String action = lease.get("varchar:action");
                    String macaddr = lease.get("vachar:macaddr");
                    
                    ArrayList<TreeMap<String,String>> linksIp = this.findAllRecords(leasesIp, "varchar:macaddr", macaddr);
                    for (TreeMap<String,String> linkIp: linksIp)
                    {
                        int nretries = 0;                             
                        int nbytes = 0;
                        int npkts = 0;
                        double rssi = 0.0;
                        
                        try{
                            nretries = Integer.parseInt(lease.get("integer:nretries"));
                            nbytes = Integer.parseInt(lease.get("integer:nbytes"));
                            npkts = Integer.parseInt(lease.get("integer:npkts"));
                            
                            rssi = Double.parseDouble(lease.get("real:rssi"));
                            
                        }catch(Exception e){e.printStackTrace();}
                        
                        //String timestamp, String proto, String saddr, int sport, 
                        //String daddr, int dport, int npkts, int nbytes, 
                        //String macaddr, double rssi, int nretries, 
                        //String hst, String uri, String cnt, String action, String hostname
                        FlowLinkUrl flowDetails = new FlowLinkUrl(timestamp, proto, ip, sport, daddr, dport, 
                                                                    npkts, nbytes, macaddr, rssi, nretries, 
                                                                    hst, uri, cnt, action, hostname);
                        flowsDetails.add(flowDetails);
                    }
                }
            }
        }
        return flowsDetails;
    }
    
    //23 april 2012
    String callHWDB(String query){
        try {
            
            return conn.call(String.format(query));
            
            } catch (Exception e) {
                System.exit(1);
        }
        return null;
    }          
    
    public static void main(String[] args)
    {
        String result = "0<|>Success<|>9<|>4<|>"+
                   "\ntimestamp:timestamp<|>integer:proto<|>varchar:saddr<|>integer:sport<|>varchar:daddr<|>integer:dport<|>varchar:hst<|>varchar:uri<|>varchar:cnt<|>"+
                   "\n@1287852a9e952300@<|>6<|>10.2.0.1<|>1391<|>10.2.0.2<|>8080<|>10.2.0.2:8080<|>/favicon.ico<|>NULL<|>"+
                   "\n@1287852b515f7df0@<|>6<|>10.2.0.1<|>1392<|>10.2.0.2<|>8080<|>10.2.0.2:8080<|>/HWJavaSocket/socket.htm<|>NULL<|>"+
                   "\n@1287852d69ce5da0@<|>6<|>10.2.0.1<|>1392<|>10.2.0.2<|>8080<|>10.2.0.2:8080<|>/HWJavaSocket/socket.htm<|>NULL<|>"+
                   "\n@12878567635bd5d0@<|>6<|>10.2.0.1<|>1438<|>10.2.0.2<|>8080<|>10.2.0.2:8080<|>/HWJavaSocket/socket.htm<|>NULL<|>";
        //System.out.println(HwdbEngine.getInstance().parseHWDBResult(result));
        System.out.println(HwdbEngine.getInstance().retrieveDevicesDetailsFromHWDB());
    }

    private ArrayList<TreeMap<String,String>> findAllRecords(ArrayList<TreeMap<String, String>> records, String field, String value) {
        
        if (records == null) return null;
        if (records.size() == 0) return null;
        
        ArrayList<TreeMap<String,String>> commonRecords = new ArrayList();
        for (TreeMap<String,String> record: records)
        {
            if (record.containsKey(field))
                if (record.containsValue(value))
                    commonRecords.add(record);
        }        
        return commonRecords;
    }
    
    //25 april 2012, 4 may 2012: change to TreeMap instead of HashMap
    public TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>> retrieveUsagePerMonthsForDevice(String deviceIp)
    {
        String queryMacaddr = "SQL:select * from Leases "//;//hwdb does not like the where clause.
            +" where ipaddr=\""+deviceIp+"\" ";
        try{
            String macaddrResult = this.callHWDB(queryMacaddr);
            ArrayList<TreeMap<String, String>> macaddrMap = this.parseHWDBResult(macaddrResult);            
            String macaddr = macaddrMap.get(0).get("varchar:macaddr");            
            System.out.println("macaddr="+macaddr+" for deviceIp="+deviceIp);            
            
            //only current day details, why???
            //String linksString = this.callHWDB("SQL:select * from Links where macaddr=\""+macaddr+"\"");
            
            //test: get data from flows instead of links
            String linksString = this.callHWDB("SQL:select * from Flows where saddr=\""+deviceIp+"\"");
            //System.out.println(linksString);
            //if (true)return null;
            
            ArrayList<TreeMap<String, String>> links = this.parseHWDBResult(linksString);
            //System.out.println(links.size());
            
            //if (true)return null;
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>> totalUsagePerYearsMonthsDays = new TreeMap();
            //for each record, print the time in the timestamp
            for (TreeMap<String,String> record: links)
            {
                String timestamp = record.get("timestamp:timestamp");
                Timestamp tstmt = HWTimestamp.getTimestampFromUnixString(timestamp);
                long time = tstmt.getTime();
                Date date = new Date(time);                
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy");//roseIndia site: for the correct year
                int year = Integer.parseInt(simpleDateFormat.format(date));                
                int month = date.getMonth();
                int day = date.getDate();
                
                int nbytes = Integer.parseInt(record.get("integer:nbytes"));                
                
                //System.out.println("year="+year+" month="+month+" day="+day+" nbytes="+nbytes);
                
                //if (true) return null;//exit to avoid stopping tomcat on unix
                
                //add the usage (in bytes) to the year-month-day total usage
                TreeMap<Integer,TreeMap<Integer, Integer>> totalUsageMonthOfYear = new TreeMap();                
                if (totalUsagePerYearsMonthsDays.containsKey(year))
                {
                    totalUsageMonthOfYear = totalUsagePerYearsMonthsDays.get(year);
                }                
                int usageDay = nbytes;
                
                TreeMap<Integer,Integer> usageMonth = new TreeMap();
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
    //25 april 2012
    public int retrieveTotalUsageForDevice(String deviceIp)
    {
        try{
            String macaddr = this.parseHWDBResult(
                    this.callHWDB("select macaddr from Leases where ipaddr=\""+deviceIp+"\"")).
                        get(0).get("varchar:macaddr");
            return Integer.parseInt(this.parseHWDBResult(
                    this.callHWDB("select sum(nbytes) from Links where macaddr=\""+macaddr+"\"")).
                        get(0).get("integer:sum(nbytes)"));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot get macaddr from Leases for deviceIp="+deviceIp);
        }
        return 0;
    }
    */
}
