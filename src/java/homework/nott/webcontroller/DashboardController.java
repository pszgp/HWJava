/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.util.Device;
import homework.nott.csv.DevicesDetailsCSV;
import homework.nott.mysql.MySQLAccessData;
import homework.nott.mysql.sums.HWMySQLEngine;
import homework.nott.mysql.sums.HWMySQLSums;
import homework.nott.util.JSON;
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
        
    @RequestMapping("/dashboard")      
    public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) {        
        ModelAndView mv = new MW().getModelView(request, response, "dashboard");
        
        //17 sept 2012: moved from MW.java, and replaced CSVParser calls with MySQLAccessData
        
        Set<String> devicesIps = (Set<String>) new HWMySQLSums().getIpsFromLeases();
        
        //Create the filesums file
        new HWMySQLSums().saveFLowsDevicesSums(devicesIps, MySQLAccessData.fileSums);//"flows_sums_hw.csv");      
               
        //read from the filesums file
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> 
                devicesUsage = mysql.getDevicesUsageHours(devicesIps);
        
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                mysql.getDevicesUsageMonths(devicesUsage);
        TreeMap<String, Long> devicesTotal = mysql.getTotalUsageDevices(devicesUsage);
        
        ArrayList<Device> devices = new DevicesDetailsCSV().getDevices(devicesIps);
        
        Set<Integer> years = getYearsOfData(data);                
        
        mv.addObject("devicesIps", devicesIps);
        mv.addObject("devicesTotal", devicesTotal);
        mv.addObject("dataMonths", data);
        mv.addObject("devices", devices);
        mv.addObject("years", years);//27 aug 2012, add the years of data
        
        TreeMap<String, TreeMap<String, Long>> dataMonthsDevices = DeviceController.getDataMonthsDevices(data);//for each month the devices data, not for each device
        mv.addObject("dataMonthsDevices", dataMonthsDevices);//dashboard
                //31 july 2012: save the json file for the devices usage     
        
        String fileDevicesUsage = "webapps/hwdashboard/js/devicesUsage_hwdashboard.json"; 
                            //"/webapps/devicesUsage_hwdashboard.json";
        File f = new File(fileDevicesUsage);
        System.out.println("fileDevicesUsage = " + f.getAbsolutePath()+" "+f.exists()+
                                new File(".").getAbsolutePath());
        JSON.convertArraytoJSON(devicesUsage, fileDevicesUsage);
                //"devicesUsage_hwdashboard.json");//"devicesUsageFlow.json");//to copy then in js/sunburstchart/        
        
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
    
}