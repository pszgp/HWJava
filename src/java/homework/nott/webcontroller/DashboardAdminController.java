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

//pszgp, 26/03-11/04 2012 (old dashboardcontroller java file), 11 may 2012 
//  (created on 11 may 2012 from DashboardController, moved functions to HWDevicesEngine.java)

@Controller
public class DashboardAdminController {// implements Runnable {
 
    final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mvHWDB = new ModelAndView();
    private int MAX_ROWS_RESULT_SET = 1000;
    
    private HwdbEngine hw = HwdbEngine.getInstance();
    private HWMySQLEngine hwMysql = HWMySQLEngine.getInstance();
            
    private Connection conn;
    
    @RequestMapping("/dashboardadmin")///update/devices")      
    public ModelAndView dashboardadminUpdateDevices(HttpServletRequest request, HttpServletResponse response) { 
       
        mvHWDB.setViewName("dashboardadmin");
                
        //subscribe to hwdb when the session starts
        HttpSession session = request.getSession(true);
        
        if (session.getValueNames().length==0)
        {            
            try {                         
                ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
                ArrayList<DeviceHistory> devicesHistory = HWDevicesEngine.getInstance().getDevicesHistory();

                System.out.println("Devices details: " + devices);

                mvHWDB.addObject("users", hw.retrieveUsersNamesFromHWDB());
                mvHWDB.addObject("devicesPerMonths", devicesHistory);            
                mvHWDB.addObject("devices", devices);    
                
                mvHWDB.addObject("deviceTypes", hw.retrieveDeviceTypesFromHWDB());
            
                /*mvHWDB.addObject("flowsLinksUrls", 
                    //hw.retrieveFlowsDetails());
                    hw.retrieveFlowsFromHWDB());*/
            
            //devices = this.getDevicesAllDetails(devices);           
            
            System.out.println("DEVICES...");
            
            //mvHWDB.addObject("devicesPerMonths", this.getDevicesPerMonths(devices));
            
            //mvHWDB.addObject("devices", devices);            
            //add to the persistent mysql tables the current data contained in the hwdb 
            
            System.out.println("Populating the urls, links and flows tables...");
            
            hwMysql.populateUrlsMySQLFromHwdb();
            
            System.out.println("Populated urls...");
            
            hwMysql.populateLinksMySQLFromHwdb();
            
            System.out.println("Populated links...");
            
            hwMysql.populateFlowsMySQLFromHwdb();
            
            System.out.println("Populated flows...");
            
            System.out.println("Populated the mysql tables with hwdb data.");
                
                        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
        
        return mvHWDB;
    }
    
    @RequestMapping("/dashboardadmin/update/flowscsv")      
    public ModelAndView dashboardadminUpdateFlowsCSV(HttpServletRequest request, HttpServletResponse response) { 
       
        mvHWDB.setViewName("dashboardadmin");
                
        //subscribe to hwdb when the session starts
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        //System.out.println(sessionId);
        //System.out.println(session.getValueNames().length);
        if (session.getValueNames().length==0)
        {
            session.setAttribute("hwdb_session_id", sessionId);
            
            try {
            
                MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("Flows");                           
                        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
                        
        return mvHWDB;
    }
    
}

