/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp, 10 may 2012
 */
import homework.nott.hwdb.Device;
import homework.nott.hwdb.DeviceHistory;
import homework.nott.hwdb.HWDevicesEngine;
import homework.nott.hwdb.HwdbEngine;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceGraphController {
 
    @RequestMapping("/devicegraph")      
    public ModelAndView devicegraph(HttpServletRequest request, HttpServletResponse response) { 
       
        /*ModelAndView mvHWDB = new ModelAndView();
        mvHWDB.setViewName("devicegraph");        
        String device = null;
        try{ 
            device = request.getParameter("ip");   
            System.out.println("selected device: " + device);
        }catch(Exception e){e.printStackTrace();};
        mvHWDB.addObject("deviceIp", device);  
        request.setAttribute("deviceIp", device);
        
        HwdbEngine hw = HwdbEngine.getInstance();
        
        Object users = hw.retrieveUsersNamesFromHWDB();
        Object devicesNames = hw.retrieveDeviceNamesFromHWDB();
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
        //devices = new DashboardController().getDevicesAllDetails(devices);
        
        ArrayList<DeviceHistory> devicesHistory = HWDevicesEngine.getInstance().getDevicesHistory();
        HashMap<String, Integer> devicesTotals = HWDevicesEngine.getInstance().getDevicesHistoryTotal(devicesHistory);
        for (Device d: devices)
        {
            try{
                d.setNbytes(devicesTotals.get(d.getIp()));
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
        
        ModelAndView mv = ModelViewForController.getMVForController(request);
        mv.setView("devicegraph");
        return mv;
    }
    
}

