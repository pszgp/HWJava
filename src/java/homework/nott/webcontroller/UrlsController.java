/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

/**
 *
 * @author pszgp, 10 may 2012
 */
import homework.nott.mysql.MySQLAccessData;
import homework.nott.mysql.sums.HWMySQLSums;
import homework.nott.tags.ZoomableTagcloud;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
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
                
        TreeMap<String, TreeMap<String, Integer>> urls = new MySQLAccessData().getUrlsForIps(new HWMySQLSums().getIpsFromLeases());
        TreeMap<String, Integer> urlsOccur = this.urlsOccur(urls);
        
        TreeMap<String, TreeMap<String, Integer>> urlsViewTotal = urlsView(urls);
        TreeMap<String, TreeMap<String, Integer>> urlsOccurZoomOne = new ZoomableTagcloud().zoomTags(urlsViewTotal, 1);
                //this.urlsOccur(new ZoomableTagcloud().zoomTags(urlsViewTotal, 1));
        TreeMap<String, TreeMap<String, Integer>> urlsOccurZoomTwo = new ZoomableTagcloud().zoomTags(urlsViewTotal, 2);
                //this.urlsOccur(new ZoomableTagcloud().zoomTags(urlsViewTotal, 2));
        
        mv.addObject("urls", urls);
        mv.addObject("urlsOccurZoomOne", urlsOccurZoomOne);
        mv.addObject("urlsOccurZoomTwo", urlsOccurZoomTwo);
        mv.addObject("urlsOccur", urlsOccur);
        
        mv.addObject("urlsOccurView", urlsOccurZoomOne);//19 sept 2012: use first the most general urls
        
        request.setAttribute("urls", urls);            
        
        //4 sept 2012: add the url and the stacked bar chart of the current url
        String url = request.getParameter("url");
        request.setAttribute("url", url);
        mv.addObject("url", url);
        
        //TreeMap<String, TreeMap<String, Integer>> urlsViewTotal = urlsView(urls);
        if (url != null)
        {
            if (url.startsWith("*"))
            {
                //19 sept 2012: show table for complete urls only, 
                //  show another tagcloud for partial urls
                
                mv.addObject("urlsOccurView", new ZoomableTagcloud().zoomUrlIntoSubtags(urlsViewTotal, url));
                
                url = null;
                request.setAttribute("url", url);
                mv.addObject("url", url);
            }
            else {
                TreeMap<String, Integer> urlsView = urlsViewTotal.get(url);
                request.setAttribute("urlsView", urlsView);
                mv.addObject("urlsView", urlsView);
            }
        }
        
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
    
    private TreeMap<String, TreeMap<String, Integer>> urlsView(TreeMap<String, TreeMap<String, Integer>> urls)
    {
        TreeMap<String, TreeMap<String, Integer>> urlsView = new TreeMap();
        for (String device: urls.keySet())
        {
            TreeMap<String, Integer> deviceUrls = urls.get(device);
            for (String uri: deviceUrls.keySet())
            {
                TreeMap<String, Integer> devicesForUrl = new TreeMap();
                if (urlsView.containsKey(uri))
                    devicesForUrl = urlsView.get(uri);
                devicesForUrl.put(device, deviceUrls.get(uri));
                urlsView.put(uri, devicesForUrl);                
            }
        }       
        System.out.println("Urls: "+urls);
        System.out.println("Urls view: "+urlsView);
        System.out.println(urlsView.size());
        
        return urlsView;
    }    
}

