/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import com.google.gson.Gson;
import homework.nott.util.CSVParser;
import homework.nott.util.Device;
import homework.nott.util.DevicesDetailsCSV;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
 
    /*final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mv = new ModelAndView();
    //private int MAX_ROWS_RESULT_SET = 1000;  
    
    CSVParser csv = new CSVParser();*/
            
    //private Connection conn;
    
    @RequestMapping("/dashboard_stacked")      
    public ModelAndView dashboard_stacked(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new MW().getModelView(request, response, "dashboard_stacked");
        return mv;
    }
    
    @RequestMapping("/dashboard")      
    public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) { 
       
        /*HttpSession session = request.getSession();
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
        
        mv.setViewName("dashboard");
        
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage = 
                csv.getDevicesUsageHours();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                csv.getDevicesUsageMonths(devicesUsage);
        TreeMap<String, Long> devicesTotal = csv.getTotalUsageDevices(devicesUsage);
        ArrayList<Device> devices = new DevicesDetailsCSV().getDevices();
                
        System.out.println("devicesIps" + devicesIps);
        System.out.println("data"+data);
        System.out.println("devices: "+ devices);
                
        mv.addObject("devicesIps", devicesIps);
        mv.addObject("devicesTotal", devicesTotal);
        mv.addObject("data", data);
        mv.addObject("devices", devices);
        */
        ModelAndView mv = new MW().getModelView(request, response, "dashboard");
        return mv;
    }
    
}