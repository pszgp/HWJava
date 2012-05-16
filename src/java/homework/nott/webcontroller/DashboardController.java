/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import com.google.gson.Gson;
import homework.nott.hwdb.HWDBClient;
import com.sun.grizzly.websockets.WebSocketEngine;
import homework.nott.hwdb.*;
import homework.nott.mysql.HWMySQL;
import homework.nott.mysql.HWTimestamp;
import homework.nott.mysql.MySQLtoCSVDataExtractionEngine;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.Message;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//pszgp, 26/03-11/04 2012

@Controller
public class DashboardController {// implements Runnable {
 
    final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mvHWDB = new ModelAndView();
    private int MAX_ROWS_RESULT_SET = 1000;
    
    HwdbEngine hw = HwdbEngine.getInstance();
    HWMySQLEngine hwMysql = HWMySQLEngine.getInstance();
            
    private Connection conn;
    @RequestMapping("/dashboard")      
    public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) { 
       
        mvHWDB.setViewName("dashboard");
        
        //mvHWDB.addObject("object", "object");
        
        //subscribe to hwdb when the session starts
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        //System.out.println(sessionId);
        //System.out.println(session.getValueNames().length);
        //if (session.getValueNames().length==0)
       // {
        //    session.setAttribute("hwdb_session_id", sessionId);
            
            try {
            
                //mvHWDB.addObject("flows", MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("Flows"));
                
            /*String serviceName = "Handler";
            SRPC srpc = new SRPC();
            service = srpc.offer(serviceName);
            
            //new Thread(new DashboardController.HWDBQueryEngine()).start();

            conn = srpc.connect("localhost", 987,"HWDB");
            int port = srpc.details().getPort();
            conn.call(String.format("SQL:subscribe LinksLast 127.0.0.1 %d %s", port, serviceName));
            */
                
            //locally test without the hwdb, but with mock values and db fields         
                
            //System.out.println(conn.call(String.format("SQL:subscribe UrlLast 127.0.0.1 %d %s", port, serviceName)));
            //String resultUrls = conn.call(String.format("SQL:select * from Urls"));            
            
            /*String urls = hw.retrieveUrlsFromHWDB();
            System.out.println(urls);
            mvHWDB.addObject("urls", urls);
            */
            
            ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
            
            
            //...
            //update the mysql tables from hwdb moves in the update file
            //...
            
            System.out.println("Devices details: " + devices);
            
            ArrayList<DeviceHistory> devicesHistory = HWDevicesEngine.getInstance().getDevicesHistory();
            HashMap<String, Long> devicesTotals = HWDevicesEngine.getInstance().getDevicesHistoryTotal(devicesHistory);
            System.out.println("devices history: " + devicesHistory);
            System.out.println("devices totals: " + devicesTotals);
            for (Device d: devices)
            {
                try{
                    String deviceIp = d.getIp();
                    if (devicesTotals.containsKey(deviceIp))
                         d.setNbytes(devicesTotals.get(d.getIp()));
                }catch(Exception e){e.printStackTrace();}
            }
            
            mvHWDB.addObject("users", hw.retrieveUsersNamesFromHWDB());
            mvHWDB.addObject("deviceNames", hw.retrieveDeviceNamesFromHWDB());                       
            mvHWDB.addObject("devicesHistory", devicesHistory);            
            mvHWDB.addObject("devices", devices); 
            
            System.out.println("users="+mvHWDB.getModel().get("users"));
            
            } catch (Exception e) {
                System.exit(1);
            }
       /* }
        else{
            Object o = session.getAttribute("sessionAttrsNr");
            if (o != null)
            {
                int sessionAttrsNr = Integer.parseInt(o.toString());
                sessionAttrsNr++;
                session.setAttribute("sessionAttrsNr", sessionAttrsNr);
            }            
        }*/
        
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
        
        mvHWDB.addObject("sessionId", sessionId);
        mvHWDB.addObject("sessionAttrsNr", session.getValueNames().length);
        
        //if session started, receive mesages from hwdb and send to client
        
        return mvHWDB;
    }
    
    /*static Service service;
    
    //class HWDBQueryEngine implements Runnable{
        public void run() {
            try {
                int i=0;                
                //System.out.println(conn.call(String.format("SQL:select * from Urls")));                               
                Message query;
                while ((query = service.query()) != null) {  
                    System.out.println("query content...");
                    System.out.println(query.getContent().length());

                    //17 april 2012
                    /*String fileLog = "hwdb_data.log";
                    FileWriter file = new FileWriter(fileLog);
                    BufferedWriter bwHWDB = new BufferedWriter(file);
                    bwHWDB.write(query.getContent());
                    bwHWDB.flush();
                    bwHWDB.close();
                    
                    File f = new File(fileLog);
                    System.out.println(f.getAbsolutePath()+" " + f.getCanonicalPath());
                    //log the content to use it for historical data
                    try{
                        String outData = "historical_hwdb.data";
                        FileWriter writer = new FileWriter(outData, true);//append data
                        System.out.println("historical data saved in: " + new File(outData).getAbsolutePath());
                        BufferedWriter bw = new BufferedWriter(writer);
                        bw.append(query.getContent());
                        bw.flush();
                        bw.close();
                    }catch(IOException e){e.printStackTrace();};
                    */
         /*           
                    Gson gson = new Gson();
                    ArrayList jsonData = new ArrayList();

                    String content = query.getContent();
                    String[] rows = content.split("\n");
                    int countRows = 0;
                    int countFields = 0;
                    if (rows.length>=3)
                    {
                        //get the field types
                        String typesRow = rows[1];
                        String[] types = typesRow.split("<\\|>");

                        ArrayList<String[]> valuesRows = new ArrayList();

                        countFields = types.length;

                        if (countFields > 1)//at least a timestamp field
                        //get all the rows of the result set
                        for (int j = 2; j < rows.length; j=j+3)
                        {
                            //get the field values of the current row
                            String valuesRow = rows[j];//2];
                            String[] values = valuesRow.split("<\\|>");
                            if (values.length != types.length)
                                continue;
                            //System.out.println("number of columns: " + values.length);
                            //System.exit(0);

                            String[] valuesWithTime = new String[countFields];
                            for (i=0;i<values.length;i++)
                            {               
                                valuesWithTime[i] = values[i];                          
                                if (i == 0)
                                {
                                    String valueTimestamp = values[i];
                                    Timestamp t = HWTimestamp.getTimestampFromUnixString(valueTimestamp);
                                    if (t!=null)
                                        valueTimestamp = t.toString();
                                    valuesWithTime[i] = valueTimestamp;  
                                }               
                            } 
                            valuesRows.add(valuesWithTime);

                            HashMap<String, Object> mapRow = new HashMap();
                            for (int k=0; k<valuesWithTime.length; k++)
                            {
                                mapRow.put(types[k], valuesWithTime[k]);
                            }
                            jsonData.add(mapRow);

                            countRows++;
                        }
                        //update the client information use the hwdb result
                        mvHWDB.addObject("types", types);
                        mvHWDB.addObject("items", valuesRows);
                        mvHWDB.addObject("countFields", countFields);
                        mvHWDB.addObject("countRows", countRows);
                        mvHWDB.addObject("json", jsonData);
                    }

                    query.getConnection().response("OK");
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }        
  //  }

    //private HashMap<String, ArrayList<Device>> getDevicesPerMonths(ArrayList<Device> devices) {
    TreeMap<String,TreeMap<Integer, TreeMap<String,TreeMap<Integer, Integer>>>> getDevicesPerMonths(ArrayList<Device> devices) {    
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
        
        /*HashMap<String, ArrayList<Device>> devicesPerMonths = new HashMap();
        System.out.println("devices... try to get details per months..." + devices);
        String[] months = Device.getMONTHs();
        for ( int i = 0; i < months.length; i++ )
        {
            String month = months[i];  
            System.out.println("month: " + month);
            ArrayList<Device> devicesMonth = new ArrayList();
            for (Device device: devices) {
                ArrayList<HashMap> totalUsage = device.getTotalUsageSplitDetailsFlows();
                ArrayList<HashMap> totalUsageMonth = new ArrayList();
                System.out.println("totalUsage: " + totalUsage);
                try{
                    for (HashMap<String,Integer> usage: totalUsage) {
                        String date = (String)new ArrayList(usage.keySet()).get(0);
                        String monthDevice = date.split(" ")[1];
                        if (month.equals(monthDevice)) {
                            totalUsageMonth.add(usage);
                        }
                    }
                    device.setTotalUsageDetailsFlows(totalUsageMonth);
                    if (devicesPerMonths.containsKey(month))
                        devicesMonth = devicesPerMonths.get(month);
                    devicesMonth.add(device);
                    devicesPerMonths.put(month, devicesMonth);
                }catch(Exception e){e.printStackTrace();System.out.println("Cannot find details for device and month.");};
            }
            System.out.println("devicesMonth: " + devicesMonth);
        }
        System.out.println("devicesPerMonths: " + devicesPerMonths);
        return devicesPerMonths;*/
   // }

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
    }//*/
}

