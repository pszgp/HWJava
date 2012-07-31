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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class UrlsController {
 
    @RequestMapping("/urls")      
    public ModelAndView urls(HttpServletRequest request, HttpServletResponse response) { 
       
        ModelAndView mv = new MW().getModelView(request, response, "urls");
        mv.setViewName("urls");                                
                
        TreeMap<String, TreeMap<String, Integer>> urls = new CSVParser().getUrlsForIps(new CSVParser().getDevicesIps());
        TreeMap<String, Integer> urlsOccur = this.urlsOccur(urls);
        
        mv.addObject("urls", urls);
        mv.addObject("urlsOccur", urlsOccur);
        request.setAttribute("urls", urls);
        
        return mv;
    }
    
    private TreeMap<String, Integer> urlsOccur(TreeMap<String, TreeMap<String, Integer>> urls)
    {
        TreeMap<String, Integer> urlsOccurences = new TreeMap();
        for (String device: urls.keySet())
        {
            TreeMap<String, Integer> deviceUrls = urls.get(device);
            for (String uri: deviceUrls.keySet())
            {
                int occur = deviceUrls.get(uri);
                
                 if (urlsOccurences.containsKey(uri))
                    occur += urlsOccurences.get(uri);
                
                urlsOccurences.put(uri, occur);
            }
        }       
        System.out.println("Urls: "+urls);
        System.out.println("Urls occurencies: "+urlsOccurences);
        System.out.println(urlsOccurences.size());
        return urlsOccurences;
    }
}

