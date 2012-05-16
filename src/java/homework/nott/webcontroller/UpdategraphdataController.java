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
public class UpdategraphdataController {
    
    @RequestMapping("/updategraphdata")
    public ModelAndView update(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        mv.setView("updategraphdata");
        
        //MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("Flows"); 
        MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("HWStatsUsageFlows");

        return mv;
            
    }
}
