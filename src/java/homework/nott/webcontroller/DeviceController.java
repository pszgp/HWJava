/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp, 10 may 2012
 */
import homework.nott.csv.CSVParserStaticData;
import homework.nott.device.DateDeviceUsage;
import homework.nott.mysql.MySQLAccessData;
import homework.nott.mysql.sums.HWMySQLSums;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceController {
 
    @RequestMapping("/livedevice")      
    public ModelAndView device(HttpServletRequest request, HttpServletResponse response) { 
               
        ModelAndView mv = new MW().getModelView(request, response, "livedevice");     
        
        MySQLAccessData mysql = new MySQLAccessData();
        
        String deviceIp = (String)request.getParameter("ip").trim();
        TreeMap<Integer, Long> deviceDataMonths=new TreeMap();
        TreeMap<Integer, Long> deviceDataDays = new TreeMap(); 
        Set<String> devicesIps = new HWMySQLSums().getIpsFromLeases();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> devicesUsage = 
                mysql.getDevicesUsageHours(devicesIps);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> dataDays = 
                mysql.getDevicesUsageDays(devicesUsage);
        
        if (deviceIp!=null)
        {
            System.out.println("device ip: " + deviceIp);
            deviceIp = deviceIp.trim();
            
            Object year = request.getParameter("year");
            if (year==null)
                year = new Date(System.currentTimeMillis()).getYear()+1900;
            int yearId = (Integer)year;
            
            TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                mysql.getDevicesUsageMonths(devicesUsage);
            
            //year = 2011;//London data!!!!!!!!!! (25 aug. 2012)
            
            if (data.containsKey(deviceIp))
            {
                deviceDataMonths = data.get(deviceIp).get(year);
            }
            
            System.out.println("HERE1: " + data);
            System.out.println("HERE2: " + deviceDataMonths);
            
            ArrayList<DateDeviceUsage> deviceUsage = getDateDeviceUsage(deviceDataMonths, true);
            System.out.println("deviceDataMonths: " + deviceUsage);
            mv.addObject("deviceDataMonths", deviceUsage);
            mv.addObject("deviceIntMonths", deviceDataMonths);
            String month = (String)request.getParameter("month");
            Object monthId = null;
            if (month!=null)
            {
                System.out.println("month: " + month);
                month = month.trim();
                MW.MONTHS m = MW.MONTHS.valueOf(month);
                monthId = MW.MONTHS.getIndexOf(m);              
            }
            else
            {                
                monthId = new Date(System.currentTimeMillis()).getMonth()+1;
            }
            int monthIdInt = (Integer)monthId;
            System.out.println("month id: " + monthId+" year id: "+year); 
            System.out.println(year+" "+yearId);
            //deviceDataDays = dataDays.get(deviceIp).get(yearId).get(monthIdInt);
            try{
                deviceDataDays = dataDays.get(deviceIp).get(year).get(monthIdInt);
            }catch(NullPointerException e){}
            
            mv.addObject("deviceDataDays", deviceDataDays);//this.getDateDeviceUsage(deviceDataDays, false));
                               
            request.setAttribute("month", month);
            request.setAttribute("deviceIp", deviceIp);
        }
                
        return mv;
    }
    
    //read from local data
    @RequestMapping("/device")      
    public ModelAndView deviceLocal(HttpServletRequest request, HttpServletResponse response) { 
               
        ModelAndView mv = new MW().getModelView(request, response, "device");     
        
        //MySQLAccessData mysql = new MySQLAccessData();
        
        String deviceIp = (String)request.getParameter("ip").trim();
        TreeMap<Integer, Long> deviceDataMonths=new TreeMap();
        TreeMap<Integer, Long> deviceDataDays = new TreeMap(); 
         
        CSVParserStaticData csv = new CSVParserStaticData();
         
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> 
                devicesUsage = csv.getDevicesUsageHours(devicesIps);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> dataDays = 
                csv.getDevicesUsageDays(devicesUsage);
        
        if (deviceIp!=null)
        {
            System.out.println("device ip: " + deviceIp);
            deviceIp = deviceIp.trim();
            
            Object year = request.getParameter("year");
            if (year==null)
                year = new Date(System.currentTimeMillis()).getYear()+1900;
            int yearId = (Integer)year;
            
            TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                csv.getDevicesUsageMonths(devicesUsage);
            
            //year = 2011;//London data!!!!!!!!!! (25 aug. 2012)
            
            if (data.containsKey(deviceIp))
            {
                deviceDataMonths = data.get(deviceIp).get(year);
            }
            
            System.out.println("HERE1: " + data);
            System.out.println("HERE2: " + deviceDataMonths);
            
            ArrayList<DateDeviceUsage> deviceUsage = getDateDeviceUsage(deviceDataMonths, true);
            System.out.println("deviceDataMonths: " + deviceUsage);
            mv.addObject("deviceDataMonths", deviceUsage);
            mv.addObject("deviceIntMonths", deviceDataMonths);
            String month = (String)request.getParameter("month");
            Object monthId = null;
            if (month!=null)
            {
                System.out.println("month: " + month);
                month = month.trim();
                MW.MONTHS m = MW.MONTHS.valueOf(month);
                monthId = MW.MONTHS.getIndexOf(m);              
            }
            else
            {                
                monthId = new Date(System.currentTimeMillis()).getMonth()+1;
            }
            int monthIdInt = (Integer)monthId;
            System.out.println("month id: " + monthId+" year id: "+year); 
            System.out.println(year+" "+yearId);
            //deviceDataDays = dataDays.get(deviceIp).get(yearId).get(monthIdInt);
            try{
                deviceDataDays = dataDays.get(deviceIp).get(year).get(monthIdInt);
            }catch(NullPointerException e){}
            
            mv.addObject("deviceDataDays", deviceDataDays);//this.getDateDeviceUsage(deviceDataDays, false));
                               
            request.setAttribute("month", month);
            request.setAttribute("deviceIp", deviceIp);
        }
                
        return mv;
    }
    
    /*//26 july 2012
    @RequestMapping("/deviceday")      
    public ModelAndView deviceday(HttpServletRequest request, HttpServletResponse response) { 
        ModelAndView mv = new MW().getModelView(request, response, "deviceday");
        return mv;
    }
    */
    //moved from MW.java, 17 sept 2012, added static
    public static ArrayList<DateDeviceUsage> getDateDeviceUsage(TreeMap<Integer, Long> usage, boolean months)
    {
        ArrayList<DateDeviceUsage> usageDate = new ArrayList();
        System.out.println("usage for device is: " + usage);
        for (int date: usage.keySet())
        {
            DateDeviceUsage ddu = new DateDeviceUsage(date+"", usage.get(date));
            if (months)
            {
                MW.MONTHS m = MW.MONTHS.get(date);
                String dateText = m.name();
                ddu.setDate(dateText);
                usageDate.add(ddu);
            }
        }
        System.out.println("date usage: " + usageDate);
        return usageDate;
    }
    
    //20 july 2012; 17 sept 2012: static
    public static TreeMap<String, TreeMap<String, Long>> getDataMonthsDevices(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data) {
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
        
        /*Collections.sort(dataMonths, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                if ((o1 instanceof MONTHS) && (o2 instanceof MONTHS))
                {
                    String m1 = ((MONTHS)o1).name();
                    String m2 = ((MONTHS)o2).name();
                    int indexM1 = MONTHS.getIndexOfString(m1);
                    int indexM2 = MONTHS.getIndexOfString(m2);                    
                    return m1.compareTo(m2);                    
                }
            }
        });*/
        
        System.out.println("DATA MONTHS: "+dataMonths);
        return dataMonths;
    }
}

