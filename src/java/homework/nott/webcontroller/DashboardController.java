/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.csv.CSVParser;
import homework.nott.csv.CSVParserStaticData;
import homework.nott.util.Device;
import homework.nott.csv.DevicesDetailsCSV;
import homework.nott.device.DateDeviceUsage;
import homework.nott.mysql.HWDataToMySQL;
import homework.nott.mysql.MySQLAccessData;
import homework.nott.mysql.sums.HWMySQLEngine;
import homework.nott.mysql.sums.HWMySQLSums;
import homework.nott.util.JSON;
import homework.nott.webcontroller.MW.MONTHS;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//pszgp, 26/03-11/04 2012

@Controller
public class DashboardController {// implements Runnable {
 
    /*final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mv = new ModelAndView();
    //private int MAX_ROWS_RESULT_SET = 1000;  
   */
    MySQLAccessData mysql = new MySQLAccessData();
        
    /*======================= live data ===========================*/
    @RequestMapping("/live")      
    public ModelAndView live(HttpServletRequest request, HttpServletResponse response) {        
        ModelAndView mv = new MW().getModelView(request, response, "live");
        
        //17 sept 2012: moved from MW.java, and replaced CSVParser calls with MySQLAccessData
        
        Set<String> devicesIps = (Set<String>) new HWMySQLSums().getIpsFromLeases();
        
        /*5 dec 2012: modify the getDevicesUsageHours to read from the view instead of the file
          don't create the filesums file*/
        
        /*//Create the filesums file
        new HWMySQLSums().saveFLowsDevicesSums(devicesIps, MySQLAccessData.fileSums);//"flows_sums_hw.csv");      
        //read from the filesums file
        */
        
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> 
                devicesUsage = mysql.getDevicesUsageHours(devicesIps);
        
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                mysql.getDevicesUsageMonths(devicesUsage);
        TreeMap<String, Long> devicesTotal = mysql.getTotalUsageDevices(devicesUsage);
        
        ArrayList<Device> devices = new DevicesDetailsCSV().getDevicesLive(devicesIps);
        
        Set<Integer> years = getYearsOfData(data);                
        
        mv.addObject("devicesIps", devicesIps);
        mv.addObject("devicesTotal", devicesTotal);
        mv.addObject("dataMonths", data);
        mv.addObject("devices", devices);
        mv.addObject("years", years);//27 aug 2012, add the years of data
        
        TreeMap<String, TreeMap<String, Long>> dataMonthsDevices = DeviceController.getDataMonthsDevices(data);//for each month the devices data, not for each device
        mv.addObject("dataMonthsDevices", dataMonthsDevices);//dashboard      
        mv.addObject("dataJson", JSON.convertArraytoJSON(devicesUsage, null));//fileDevicesUsage);
        
        return mv;
    }
    
    private Set<Integer> getYearsOfData(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data) {
        Set<Integer> years = new HashSet();
        for (String device: data.keySet())
        {
            for (Integer year: data.get(device).keySet())
            {
                if (year!=null)
                    years.add(year);
            }
        }
        return years;
    }
 
    /*===================== local data ================================*/
    
    CSVParserStaticData csv = new CSVParserStaticData();
    @RequestMapping("/dashboard")      
    public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) { 
       
        ModelAndView mv = new MW().getModelView(request, response, "dashboard");
        HttpSession session = request.getSession();
        //session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
         mv.setViewName("dashboard");
        
         //here mention the location of the data on the server or on local computer, such as in the sample below
        new HWDataToMySQL().importDataCollection("D:/hwJavaSocket/London_data_txl/hwdatalondonv2 (1).tar/hwdata");
         
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> 
                devicesUsage = csv.getDevicesUsageHours(devicesIps);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                csv.getDevicesUsageMonths(devicesUsage);
        System.out.println("MW: data="+data);
        System.out.println("MW: devicesIps = " + devicesIps);
        System.out.println("MW: devicesUsage = " + devicesUsage);
        TreeMap<String, Long> devicesTotal = csv.getTotalUsageDevices(devicesUsage);
        /*ArrayList<Device> devices = new DevicesDetailsCSV().getDevices();
        
        Set<Integer> years = getYearsOfData(data);
                
        System.out.println("devicesIps" + devicesIps);
        System.out.println("data"+data);
        System.out.println("devices: "+ devices);             
        
        System.out.println("MW: devicesTotal=" + devicesTotal);
        
        mv.addObject("devicesIps", devicesIps);
        mv.addObject("devicesTotal", devicesTotal);
        mv.addObject("dataMonths", data);
        mv.addObject("devices", devices);
        mv.addObject("years", years);//27 aug 2012, add the years of data
        
        TreeMap<String, TreeMap<String, Long>> dataMonthsDevices = this.getDataMonthsDevices(data);//for each month the devices data, not for each device
        mv.addObject("dataMonthsDevices", dataMonthsDevices);//dashboard
        
        */
        return mv;
    }
    
    public ArrayList<DateDeviceUsage> getDateDeviceUsage(TreeMap<Integer, Long> usage, boolean months)
    {
        ArrayList<DateDeviceUsage> usageDate = new ArrayList();
        System.out.println("usage for device is: " + usage);
        for (int date: usage.keySet())
        {
            DateDeviceUsage ddu = new DateDeviceUsage(date+"", usage.get(date));
            if (months)
            {
                MONTHS m = MONTHS.get(date);
                String dateText = m.name();
                ddu.setDate(dateText);
                usageDate.add(ddu);
            }
        }
        System.out.println("date usage: " + usageDate);
        return usageDate;
    }
    
    //20 july 2012
    private TreeMap<String, TreeMap<String, Long>> getDataMonthsDevices(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data) {
        TreeMap<String, TreeMap<String, Long>> dataMonths = new TreeMap();
        for (String device: data.keySet()){
            TreeMap<Integer, TreeMap<Integer, Long>> deviceData = data.get(device);
            for (int year: deviceData.keySet())
            {
                TreeMap<Integer, Long> deviceDataMonth = deviceData.get(year);
                for (int month: deviceDataMonth.keySet())
                {
                    long usage = deviceDataMonth.get(month);
                    TreeMap<String, Long> dataMonth = new TreeMap<String, Long>();
                    
                    String monthName = ""+month;//MONTHS.get(month).name();
                    
                    if (dataMonths.containsKey(monthName))
                        dataMonth = dataMonths.get(monthName);
                    
                    dataMonth.put(device, usage);
                    
                    dataMonths.put(monthName, dataMonth);
                    
                }
            }
        }
        
        System.out.println("DATA MONTHS: "+dataMonths);
        return dataMonths;
    }
}