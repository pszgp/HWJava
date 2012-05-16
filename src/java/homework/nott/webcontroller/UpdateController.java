/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp
 */

import homework.nott.hwdb.*;
import homework.nott.mysql.MySQLtoCSVDataExtractionEngine;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UpdateController {
    
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        mv.setView("update");
        
        HwdbEngine hw = HwdbEngine.getInstance();
        HWMySQLEngine hwMysql = HWMySQLEngine.getInstance();
        
        ArrayList<Device> devices = hw.retrieveDevicesDetailsFromHWDB();
        devices = new DashboardController().getDevicesAllDetails(devices);           

        System.out.println("DEVICES...");

        //add to the persistent mysql tables the current data contained in the hwdb 

        System.out.println("Populating the urls, links and flows tables...");

        hwMysql.populateUrlsMySQLFromHwdb();

        System.out.println("Populated urls...");

        hwMysql.populateLinksMySQLFromHwdb();

        System.out.println("Populated links...");

        hwMysql.populateFlowsMySQLFromHwdb();

        System.out.println("Populated flows...");

        System.out.println("Populated the mysql tables with hwdb data.");
        
        MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("Flows"); 
        MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("HWStatsUsageFlows");

        return mv;
            
    }
}
