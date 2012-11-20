/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.tags;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pszgp, 15,19 sept 2012
 */
public class ZoomableTagcloud {
    
    private boolean isGatewayOrIgnoredIp(String url)
    {
        //change the pattern, 18 sept 2012
        Pattern p = Pattern.compile("^0|[1-9]\\d*$");//"[^a-bA-Z0-9]");
        Matcher m = p.matcher(url);
        if (m.find())
            return true;
        return false;
    }
    
    private String getFilteredUrl(String url, int filterLevel)
    {
        if (filterLevel == 3)
            return url;
        if (url.contains("."))
        {
            String[] items = url.split("\\.");
            if (items.length < filterLevel)
                return url;
            StringBuilder filteredUrl = new StringBuilder("*");
            for (int i=items.length-filterLevel; i<items.length; i++)
            {
                filteredUrl.append(".").append(items[i]);
            }
            return filteredUrl.toString();
        }
        return url;
    }
    
    public TreeMap<String, TreeMap<String, Integer>> zoomTags(TreeMap<String, TreeMap<String, Integer>> urls, int filterLevel)
    {
        System.out.println("Zoom for filter = "+filterLevel+" the tags: "+urls);
        TreeMap<String, TreeMap<String, Integer>> zoomedUrls = new TreeMap();
        for (String url: urls.keySet())
        {
            //System.out.println("url="+url);
            //System.out.println(this.isGatewayOrIgnoredIp(url));
            if (!this.isGatewayOrIgnoredIp(url))
            {
                String filteredUrl = this.getFilteredUrl(url, filterLevel);
                //System.out.println("ulr="+url+" filtered="+filteredUrl);
                TreeMap<String, Integer> data = new TreeMap();
                if (zoomedUrls.containsKey(filteredUrl))
                    data = zoomedUrls.get(filteredUrl);
                data.putAll(urls.get(url));
                zoomedUrls.put(filteredUrl, data);
            }
                    
        }        
        System.out.println("zoomed urls for filter=" + filterLevel + " are: " + zoomedUrls);
        return zoomedUrls;
    }
    
    //19 sept 2012: zoom a general tag into subtags
    public TreeMap<String, TreeMap<String, Integer>> zoomUrlIntoSubtags(TreeMap<String, TreeMap<String, Integer>> urls, String urlTag)
    {        
        System.out.println("Zoom the url "+urlTag+" using the list of tags: "+urls);
        TreeMap<String, TreeMap<String, Integer>> zoomedUrls = new TreeMap();
        
        //System.out.println(urlTag.startsWith("*"));
        
        if (!urlTag.startsWith("*")) 
            return new TreeMap();//empty list: cannot extend any further
        
        int filterLevel = 0;
        if (urlTag.contains("."))
        {
            //System.out.println("here");
            String tmp = urlTag.substring(urlTag.indexOf("."));
            filterLevel = 1;
            if (urlTag.contains("."))
                filterLevel = 2;            
        }
        else 
            return new TreeMap();
        
        //System.out.println(urlTag);
        //System.out.println(filterLevel);
        
        filterLevel++;//get the zoomed urls for the url only
        urlTag = urlTag.substring(1);//skip the '*' character
        
        for (String url: urls.keySet())
        {
            if (!url.endsWith(urlTag))
                continue;
            if (!this.isGatewayOrIgnoredIp(url))
            {
                String filteredUrl = this.getFilteredUrl(url, filterLevel);
                TreeMap<String, Integer> data = new TreeMap();
                if (zoomedUrls.containsKey(filteredUrl))
                    data = zoomedUrls.get(filteredUrl);
                data.putAll(urls.get(url));
                zoomedUrls.put(filteredUrl, data);
            }
                    
        }        
        return zoomedUrls;
    }
    
    
    public static void main(String[] args)
    {
        ZoomableTagcloud zoom = new ZoomableTagcloud();
        /*System.out.println(zoom.getFilteredUrl("www.nottingham.ac.uk", 1));
        System.out.println(zoom.getFilteredUrl("www.nottingham.ac.uk", 2));
        System.out.println(zoom.getFilteredUrl("www.nottingham.ac.uk", 3));
        
        System.out.println(zoom.getFilteredUrl("red.nott.ac.uk", 1));
        System.out.println(zoom.getFilteredUrl("red.nott.ac.uk", 2));
        System.out.println(zoom.getFilteredUrl("red.nott.ac.uk", 3));  
        
        System.out.println(zoom.isGatewayOrIgnoredIp("45.0.7"));*/
        
        TreeMap<String, TreeMap<String, Integer>> urls = new TreeMap();
        TreeMap<String, Integer> devices = new TreeMap();
        devices.put("device1", 123);
        urls.put("red.nott.ac.uk", devices);
        urls.put("google.com", devices);
        urls.put("www.nottingham.ac.uk", devices);
        
        System.out.println(zoom.zoomUrlIntoSubtags(urls, "*.uk"));
        System.out.println(zoom.zoomUrlIntoSubtags(urls, "*ac.uk"));
        System.out.println(zoom.zoomUrlIntoSubtags(urls, "*.com"));
        
    }
    
}
