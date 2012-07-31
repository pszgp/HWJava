/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp, 10 may 2012
 */
import homework.nott.util.CSVParser;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceController {
 
    @RequestMapping("/device")      
    public ModelAndView device(HttpServletRequest request, HttpServletResponse response) { 
               
        ModelAndView mv = new MW().getModelView(request, response, "device");
        
        CSVParser csv = new CSVParser();
        
        String deviceIp = (String)request.getParameter("ip");
        TreeMap<Integer, Long> deviceDataMonths=new TreeMap();
        TreeMap<Integer, Long> deviceDataDays = new TreeMap(); 
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> devicesUsage = 
                csv.getDevicesUsageHours(devicesIps);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> dataDays = 
                csv.getDevicesUsageDays(devicesUsage);
        
        if (deviceIp!=null)
        {
            System.out.println("device ip: " + deviceIp);
            
            Object year = request.getParameter("year");
            if (year==null)
                year = new Date(System.currentTimeMillis()).getYear()+1900;
            int yearId = (Integer)year;
            
            TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                csv.getDevicesUsageMonths(devicesUsage);
            
            if (data.containsKey(deviceIp))
            {
                deviceDataMonths = data.get(deviceIp).get(year);
            }
            System.out.println("deviceDataMonths: " + new MW().getDateDeviceUsage(deviceDataMonths, true));
            mv.addObject("deviceDataMonths", new MW().getDateDeviceUsage(deviceDataMonths, true));
            mv.addObject("deviceIntMonths", deviceDataMonths);
            String month = (String)request.getParameter("month");
            Object monthId = null;
            if (month!=null)
            {
                System.out.println("month: " + month);
                MW.MONTHS m = MW.MONTHS.valueOf(month);
                monthId = MW.MONTHS.getIndexOf(m);              
            }
            else
            {                
                monthId = new Date(System.currentTimeMillis()).getMonth()+1;
            }
            int monthIdInt = (Integer)monthId;
            System.out.println("month id: " + monthId+" year id: "+year); 
            deviceDataDays = dataDays.get(deviceIp).get(yearId).get(monthIdInt);
            
            mv.addObject("deviceDataDays", deviceDataDays);//this.getDateDeviceUsage(deviceDataDays, false));
                               
            request.setAttribute("month", month);
            request.setAttribute("deviceIp", deviceIp);
        }
                
        return mv;
    }
    
    //26 july 2012
    @RequestMapping("/deviceday")      
    public ModelAndView deviceday(HttpServletRequest request, HttpServletResponse response) { 
        ModelAndView mv = new MW().getModelView(request, response, "deviceday");
        return mv;
    }
    
}

