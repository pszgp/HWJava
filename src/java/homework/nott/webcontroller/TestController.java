/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import com.google.common.base.Service;
import com.google.gson.Gson;
import homework.nott.hwdb.HwdbEngine;
import homework.nott.mysql.MySQLtoCSVDataExtractionEngine;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hwdb.srpc.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.stereotype.Controller;

/**
 *
 * @author pszgp
 */

@Controller
public class TestController {
    
    @RequestMapping("/test")      
    public ModelAndView test(HttpServletRequest request, HttpServletResponse response) { 
       
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        
        /*HwdbEngine hw = HwdbEngine.getInstance();
        String urls = hw.retrieveUrlsFromHWDB();
        mv.addObject("urls", urls);
        
        System.out.println(urls.length());*/
        /* 
        service: Handler
        port: 54048
        18928
        populate mysql table...
         */
        
        //hw.populateUrls(urls);   
        
        //System.out.println("populate mysql table...");
        
        //String text = MySQLtoCSVDataExtractionEngine.extractCSVFromMySQLTable("Flows");
        //System.out.println("flows text length: " + text.length());
        
        return mv;
    }
}
