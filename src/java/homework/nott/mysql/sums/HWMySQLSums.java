/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql.sums;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pszgp
 */
public class HWMySQLSums {

    /**
     * @param args the command line arguments
     */
    private HWMySQLEngine hwmysql = HWMySQLEngine.getInstance();
    
    public Set<String> getIpsFromLeases(){
        Set<String> ips = new HashSet();
        
        //!!! tl advice: read from Leases (action=add) instead of Flows (Leases contains the devices)
        
        ArrayList<TreeMap<String, Object>> result = hwmysql.queryMySQL("SELECT ipaddr FROM Leases WHERE action='add'");

        for (TreeMap<String, Object> lease: result)
        {
            if (lease!=null)
            {
                String ip = null;
                if (lease.values()!=null)
                {
                    if (lease.values().size()!=0)
                        ip = (String)new ArrayList(lease.values()).get(0);
                }
                if (ip!=null) ips.add(ip);
            }
        }
        //System.out.println(ips);
        return ips;
    }
        
    public TreeMap<Integer,TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> getFlowsDeviceSumHours(String deviceIp) {
        
        ArrayList<TreeMap<String, Object>> ipFlows = 
                hwmysql.queryMySQL("SELECT nbytes, t FROM KFlows WHERE (saddr=\""+deviceIp+"\")" +//, null);
                         " OR (daddr=\""+deviceIp+"\")", null);
        
        TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> ipUsage = 
                new TreeMap();
        
        if (ipFlows!=null)
        {    for (TreeMap<String, Object> flow: ipFlows)
            {
                if (flow!=null)
                {
                    System.out.println("flow="+flow);
                    if ((flow.containsKey("nbytes")&&(flow.containsKey("t"))))
                    {
                        int value = (Integer)flow.get("nbytes");
                        long nbytes = value;
                        String last = (String)flow.get("t");
                        System.out.println("last="+last+" "+flow.get("t"));
                        
                        Timestamp tstamp = HWTimestamp.getTimestampFromUnixString(last);    
                        System.out.println("timestamp: "+ tstamp + "from t value: "+last);
                        
                        if (tstamp == null) continue;
                        
                        int hour = tstamp.getHours();
                        int day = tstamp.getDate();
                        int month = tstamp.getMonth()+1;//starts with 1
                        int year = 1900+tstamp.getYear();                       
                        
                        TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> ipUsageYear = new TreeMap();
                        if (ipUsage.containsKey(year))
                            ipUsageYear = ipUsage.get(year);     
                        
                        TreeMap<Integer, TreeMap<Integer, Long>> ipUsageMonth = new TreeMap();                        
                        if (ipUsageYear.containsKey(month))
                            ipUsageMonth = ipUsageYear.get(month);
                        
                        TreeMap<Integer, Long> ipUsageDay = new TreeMap();
                        
                        if (ipUsageMonth.containsKey(day))
                            ipUsageDay = ipUsageMonth.get(day);
                        
                        Long ipUsageHour = 0L;
                        if (ipUsageDay.containsKey(hour))
                            ipUsageHour = ipUsageDay.get(hour);
                        
                        ipUsageHour += nbytes;
                        
                        ipUsageDay.put(hour, ipUsageHour);
                        ipUsageMonth.put(day, ipUsageDay);
                        ipUsageYear.put(month, ipUsageMonth);
                        ipUsage.put(year, ipUsageYear);
                        
                    }
                }
            }
        }
        System.out.println("ipUsage = " + ipUsage.size());
        //System.exit(0);
        return ipUsage;
    }
    
    public void saveFLowsDevicesSums(Set<String> ips, String file){
        //ArrayList<String> ips = this.getIpsFromLeases();
        if (ips==null) return;
        try {
            System.out.println("save the flows information in the file: " + new File(file).getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));//"HW/flows_sums_.csv"));
                        
            //9 oct 2012: read and write in the file (is sudo working here???!!)
            new File(file).setReadable(true);
            new File(file).setWritable(true);
            
            bw.write("deviceIp,year,month,day,hour,nbytes");
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(HWMySQLSums.class.getName()).log(Level.SEVERE, null, ex);
            //ex.printStackTrace();
        }
        for (String ip: ips){
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> ipUsage = getFlowsDeviceSumHours(ip);
            
            saveToCSV(ip, ipUsage, file);
        }
    }
    
    public void saveToCSV(String ip, TreeMap<Integer,TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> ipUsage, String file)
    {
        try {
            
            System.out.println("Save in file: " + new File(file).getAbsolutePath()+" csv text for ipUsage: "+ ipUsage);
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            Set<Integer> years = ipUsage.keySet();
            for (int y: years){
                Set<Integer> months = ipUsage.get(y).keySet();
                for (int m: months){
                    Set<Integer> days = ipUsage.get(y).get(m).keySet();
                    for (int d: days){
                        Set<Integer> hours = ipUsage.get(y).get(m).get(d).keySet();
                        for (int h: hours)
                        {
                            long nbytes = ipUsage.get(y).get(m).get(d).get(h);
                            bw.write(ip+","+y+","+m+","+d+","+h+","+nbytes);
                            bw.newLine();
                        }
                    }
                    bw.flush();
                }
                bw.flush();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(HWMySQLSums.class.getName()).log(Level.SEVERE, null, ex);
            //ex.printStackTrace();
            System.out.println("ERROR: cannot save in file = " + new File(file).getAbsoluteFile());
        }
        
    }
   

}
