/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp
 */

import homework.nott.hwdb.Device;
import homework.nott.hwdb.DeviceHistory;
import homework.nott.hwdb.HWDevicesEngine;
import homework.nott.hwdb.HwdbEngine;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.portlet.ModelAndView;

public class ModelViewForController {
    public static ModelAndView getMVForController(HttpServletRequest request)
    {
        ModelAndView mvHWDB = new ModelAndView();
        String device = null, month=null; int monthId = 0;
        try{ 
            device = request.getParameter("ip");      
            System.out.println("selected device: " + device);
        }catch(Exception e){e.printStackTrace();}; 
        try{ 
            month = request.getParameter("month");      
            System.out.println("selected month: " + month);
        }catch(Exception e){e.printStackTrace();}; 
        if (month==null)
            month = Device.getMONTH(new Date(System.currentTimeMillis()).getMonth());
        monthId = Device.getMONTHId(month);
        request.setAttribute("deviceIp", device);  
        request.setAttribute("month", month); 
        request.setAttribute("monthId", monthId);
        
        HwdbEngine hw = HwdbEngine.getInstance();    
        HWDevicesEngine hwde = HWDevicesEngine.getInstance();
        
        Object users = hw.retrieveUsersNamesFromHWDB();
        Object devicesNames = hw.retrieveDeviceNamesFromHWDB();
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
            
        ArrayList<DeviceHistory> devicesHistory = hwde.getDevicesHistory();
        HashMap<String, Long> devicesTotals = hwde.getDevicesHistoryTotal(devicesHistory);
        HashMap<String, HashMap<String, Long>> devicesTotalsMonths = hwde.getDevicesHistoryTotalMonths(devicesHistory);
           
        System.out.println("devicesTotalsMonths="+devicesTotalsMonths);
                
        for (Device d: devices)
        {
            try{
                String deviceIp = d.getIp();
                if (devicesTotals.containsKey(deviceIp))
                    d.setNbytes(devicesTotals.get(deviceIp));
                if (devicesTotalsMonths.containsKey(deviceIp))
                    d.setNbytesMonths(devicesTotalsMonths.get(deviceIp)); 
            }catch(Exception e){e.printStackTrace();}
        }
            
        mvHWDB.addObject("users", users);
        mvHWDB.addObject("deviceNames", devicesNames);                       
        mvHWDB.addObject("devicesHistory", devicesHistory);            
        mvHWDB.addObject("devices", devices); 
        
        //send variables through the request parameter instead of mv, this works
        request.setAttribute("users", users);
        request.setAttribute("devicesNames", devicesNames);
        request.setAttribute("devices", devices);
        request.setAttribute("devicesHistory", devicesHistory);
        
        return mvHWDB;
    }
}
