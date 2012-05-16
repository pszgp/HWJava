/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.hwdb.Device;
import homework.nott.hwdb.DeviceHistory;
import homework.nott.hwdb.HWDevicesEngine;
import homework.nott.hwdb.HwdbEngine;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 * @author pszgp, 6 may 2012, 10 may 2012
 */
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
 
    ModelAndView mvHWDB = new ModelAndView();
    @RequestMapping("/user")      
    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) { 
       
       /* //ModelAndView mvHWDB = new ModelAndView();
        mvHWDB.setViewName("user");
        mvHWDB.addObject("object", "object1");
        request.setAttribute("object", "object2");
        String user = null;
        try{ 
            user = request.getParameter("name");  
            System.out.println("selected user: " + user);
        }catch(Exception e){e.printStackTrace();};
        mvHWDB.addObject("user", user);     
        request.setAttribute("user", user);
        
        HwdbEngine hw = HwdbEngine.getInstance();
        
        Object users = hw.retrieveUsersNamesFromHWDB();
        Object devicesNames = hw.retrieveDeviceNamesFromHWDB();
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
            
        ArrayList<DeviceHistory> devicesHistory = HWDevicesEngine.getInstance().getDevicesHistory();
        HashMap<String, Integer> devicesTotals = HWDevicesEngine.getInstance().getDevicesHistoryTotal(devicesHistory);
        for (Device d: devices)
        {
            try{
                String deviceIp = d.getIp();
                if (devicesTotals.containsKey(deviceIp))
                    d.setNbytes(devicesTotals.get(deviceIp));
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
                
        return mvHWDB;*/
        
        String user = null;
        try{ 
            user = request.getParameter("name");  
            System.out.println("selected user: " + user);
        }catch(Exception e){e.printStackTrace();};
        mvHWDB.addObject("user", user);     
        request.setAttribute("user", user);
        
        return ModelViewForController.getMVForController(request);
    }
    
}
      

