/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author pszgp, 23 april 2012
 */
public class Device {
    private String timestamp;//as date string, not unix timestamp
    private String ip;
    private String name;
    private String type;
    private String user;
    private long allowance;
    private long nbytes;
    private double nbytesMb;

    public double getNbytesMb() {
        return (double) (nbytes / (1024 * 1024));
    }
    
    private TreeMap<Integer,TreeMap<Integer,TreeMap<Integer,Long>>> usage;
    private TreeMap<Integer,TreeMap<Integer,TreeMap<Integer,Long>>> usageFlows;//from flows...
    //total usage, split per years, months, days using the usage map
    private ArrayList<TreeMap> totalUsageSplitDetails = new ArrayList();
    private ArrayList<TreeMap> totalUsageSplitDetailsFlows = new ArrayList();//from flows (30 april 2012)
    private long totalUsage;
    private long totalUsageFlows;
    private String macaddr;
    private HashMap<String, Long> nbytesMonths;

    public ArrayList<TreeMap> getTotalUsageSplitDetails() {
        return totalUsageSplitDetails;
    }
    
    public ArrayList<TreeMap> getTotalUsageSplitDetailsFlows() {
        return totalUsageSplitDetailsFlows;
    }
    //not for beans, but for use in the code
    public void setTotalUsageDetailsFlows(ArrayList totalUsageDetailsFlows)
    {
        this.totalUsageSplitDetailsFlows = totalUsageDetailsFlows;
    }

    private String MONTHS[] = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
                                            "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    
    public static String[] getMONTHs(){
        return new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
                                            "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    }
    public static String getMONTH(int month){
        return getMONTHs()[month];
    }
    public static int getMONTHId(String MONTH){
        for (int i=0; i< getMONTHs().length; i++)
        {
            String month = getMONTHs()[i];
            if (month.equals(MONTH))
                return i;
        }
        return -1;
    }
    
    public void setTotalUsageSplitDetails() {
        try{
            if (usage==null) return;
            
            this.totalUsageSplitDetails = new ArrayList();
            
            for (Integer year: usage.keySet()){
                TreeMap<Integer,TreeMap<Integer, Long>> yearUsage = usage.get(year);
                for (Integer month: yearUsage.keySet())
                {
                    TreeMap<Integer,Long> monthUsage = yearUsage.get(month);
                    for (Integer day: monthUsage.keySet()){

                        long dayUsage = monthUsage.get(day);

                        TreeMap<String,Long> mapDay = new TreeMap();                    
                        String monthName = MONTHS[month];
                        String dateName = day + " "+ monthName+" "+year;

                        mapDay.put(dateName, dayUsage);

                        this.totalUsageSplitDetails.add(mapDay);
                    }
                }
            }
        }catch(Exception e){e.printStackTrace();};
    }
    
    public void setTotalUsageSplitDetailsFlows() {
        try{
            if (usageFlows==null) return;
            
            this.totalUsageSplitDetailsFlows = new ArrayList();
            
            for (Integer year: usageFlows.keySet()){
                TreeMap<Integer,TreeMap<Integer, Long>> yearUsage = usageFlows.get(year);
                for (Integer month: yearUsage.keySet())
                {
                    TreeMap<Integer,Long> monthUsage = yearUsage.get(month);
                    for (Integer day: monthUsage.keySet()){

                        long dayUsage = monthUsage.get(day);

                        TreeMap<String,Long> mapDay = new TreeMap();                    
                        String monthName = MONTHS[month];
                        String dateName = day + " "+ monthName+" "+year;

                        mapDay.put(dateName, dayUsage);

                        this.totalUsageSplitDetailsFlows.add(mapDay);
                    }
                }
            }
        }catch(Exception e){e.printStackTrace();};
    }

    public long getAllowance() {
        return allowance;
    }

    public void setAllowance(long allowance) {
        this.allowance = allowance;
    }

    public long getNbytes() {
        return nbytes;
    }

    public void setNbytes(long nbytes) {
        this.nbytes = nbytes;
    }
    
    public Device(String timestamp, String ip, String name, String type, String user, long allowance, long nbytes)
    {
        this.timestamp = timestamp;
        this.ip = ip;
        this.name = name;
        this.type = type;
        this.user = user;
        this.allowance = allowance;
        this.nbytes = nbytes;
    }
    public String getTimestamp()
    {
        return this.timestamp;
    }
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getIp()
    {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public String toString(){
        return "Device=[timestamp:"+timestamp+";ip:"+ip+";name:"+name+";type:"+type+";user:"+user+";allowance="+allowance+";usage="+nbytes+"]";
    }

    public void setUsage(TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> usage) {
        this.usage = new TreeMap(usage);
        this.setTotalUsageSplitDetails();
    }
    
    public void setUsageFlows(TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> usage) {
        this.usageFlows = new TreeMap(usage);
        this.setTotalUsageSplitDetailsFlows();
    }
    
    public TreeMap getUsage()
    {
        return this.usage;
    }

    public TreeMap getUsageFlows()
    {
        return this.usage;
    }
    public void setTotalUsage(long totalUsage) {
        this.totalUsage = totalUsage;
    }
    public long getTotalUsage()
    {
        return this.totalUsage;
    }
    
    public void setTotalUsageFlows(long totalUsage) {
        this.totalUsageFlows = totalUsageFlows;
    }
    public long getTotalUsageFlows()
    {
        return this.totalUsageFlows;
    }

    public String getMacaddr() {
        return this.macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public void setNbytesMonths(HashMap<String, Long> nbytesMonths) {
        this.nbytesMonths = new HashMap(nbytesMonths);
    }
    public HashMap<String, Long> getNbytesMonths() {
        return this.nbytesMonths;
    }
    
}
