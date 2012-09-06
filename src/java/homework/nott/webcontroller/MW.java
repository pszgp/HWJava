/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.util.CSVParser;
import homework.nott.util.DateDeviceUsage;
import homework.nott.util.Device;
import homework.nott.util.DevicesDetailsCSV;
import homework.nott.util.JSON;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author pszgp, 12 july 2012
 */
public class MW {
    
    final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mv = new ModelAndView();
    //private int MAX_ROWS_RESULT_SET = 1000;  
    
    CSVParser csv = new CSVParser();

    private Set<Integer> getYearsOfData(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data) {
        Set<Integer> years = new HashSet();
        for (String device: data.keySet())
        {
            for (Integer year: data.get(device).keySet())
            {
                years.add(year);
            }
        }
        return years;
    }
    
    public enum MONTHS{NULL, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECECMBER;
   
        public static MONTHS get(int i){
            return values()[i];
            }
        public static int getIndexOf(MONTHS month){
            MONTHS[] values = values();
            for (int i=0;i<values.length;i++)
            {
                MONTHS m = values[i];
                if (m.equals(month))
                    return i;
            }
            return 0;
        }
        public static int getIndexOfString(String monthName)
        {
            MONTHS[] values = MONTHS.values();
            for (int i=0;i<values.length;i++)
            {
                String m = values[i].name();
                if (m.equals(monthName))
                    return i;
            }
            return 0;
        }
    }
    
    public ModelAndView getModelView(HttpServletRequest request, HttpServletResponse response, String viewName) { 
       
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
        
        mv.setViewName(viewName);
        
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> 
                devicesUsage = csv.getDevicesUsageHours(devicesIps);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data = 
                csv.getDevicesUsageMonths(devicesUsage);
        System.out.println("MW: data="+data);
        System.out.println("MW: devicesIps = " + devicesIps);
        System.out.println("MW: devicesUsage = " + devicesUsage);
        TreeMap<String, Long> devicesTotal = csv.getTotalUsageDevices(devicesUsage);
        ArrayList<Device> devices = new DevicesDetailsCSV().getDevices();
        
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
        
        
        //31 july 2012: save the json file for the devices usage
        
        System.out.println("devicesUsage = " + devicesUsage);
        JSON.convertArraytoJSON(devicesUsage, "devicesUsage.json");//to copy then in js/sunburstchart/
        
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
