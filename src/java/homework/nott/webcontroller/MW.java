/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.csv.CSVParser;
import homework.nott.device.DateDeviceUsage;
import homework.nott.util.Device;
import homework.nott.csv.DevicesDetailsCSV;
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

    /*private Set<Integer> getYearsOfData(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> data) {
        Set<Integer> years = new HashSet();
        for (String device: data.keySet())
        {
            for (Integer year: data.get(device).keySet())
            {
                years.add(year);
            }
        }
        return years;
    }*/
    
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
                
        return mv;
    }
    
    
}
