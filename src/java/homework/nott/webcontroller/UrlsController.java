/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp, 10 may 2012
 */
import homework.nott.hwdb.*;
import homework.nott.mysql.HWTimestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class UrlsController {
 
    @RequestMapping("/urls")      
    public ModelAndView device(HttpServletRequest request, HttpServletResponse response) { 
       
        ModelAndView mvHWDB = new ModelAndView();
        mvHWDB.setViewName("urls");
        
        String urls = HwdbEngine.getInstance().retrieveUrlsFromHWDB();
        System.out.println(urls);
        ArrayList<TreeMap<String, Object>> urlsData = HWMySQLEngine.getInstance().queryMySQL("SELECT * FROM Urls", null);
        ArrayList<TreeMap<String, Object>> copyUrlsData = new ArrayList();
        for (TreeMap<String, Object> url: urlsData)
        {
            String date = url.get("last").toString();
            Timestamp t = HWTimestamp.getTimestampFromUnixString(date);
            url.put("last", t);
            copyUrlsData.add(url);
        }
        
        mvHWDB.addObject("urls", urls);
        request.setAttribute("urls", copyUrlsData);
        
        HwdbEngine hw = HwdbEngine.getInstance();
        
        Object users = hw.retrieveUsersNamesFromHWDB();
        Object devicesNames = hw.retrieveDeviceNamesFromHWDB();
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
            
        ArrayList<DeviceHistory> devicesHistory = HWDevicesEngine.getInstance().getDevicesHistory();
        HashMap<String, Long> devicesTotals = HWDevicesEngine.getInstance().getDevicesHistoryTotal(devicesHistory);
        for (Device d: devices)
        {
            String deviceIp = d.getIp();
            try{
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
                
        return mvHWDB;
    }
    
}

