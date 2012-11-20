/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.csv;

import homework.nott.csv.CSVParser;
import homework.nott.mysql.MySQLAccessData;
import homework.nott.util.Device;
import java.util.*;
/**
 *
 * @author pszgp, 12 july 2012
 */
public class DevicesDetailsCSV {
    
    //private CSVParser csv = new CSVParser();
    MySQLAccessData mysql = new MySQLAccessData();//17 sept 2012
    
    public ArrayList<Device> getDevices(Set<String> ips){
        ArrayList<Device> devices = new ArrayList();
        
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> usage 
                = mysql.getDevicesUsageHours(ips);
        System.out.println("ips: "+ips);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> usageDays 
                = mysql.getDevicesUsageDays(usage);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> usageMonths 
                = mysql.getDevicesUsageMonths(usage);
        TreeMap<String, TreeMap<Integer, Long>> usageYears = mysql.getDevicesUsageYears(usage);
        
        TreeMap<String, Long> totalUsage = mysql.getTotalUsageDevices(usage);
        
        ArrayList<TreeMap<String, Object>> deviceNames = mysql.getRecords("DeviceNames");
        ArrayList<TreeMap<String, Object>> deviceTypes = mysql.getRecords("DeviceTypes");
        ArrayList<TreeMap<String, Object>> allowances = mysql.getRecords("Allowances");
        
        ArrayList<TreeMap<String, Object>> leases = mysql.getRecords("Leases");
        
        ArrayList<String> ipsUsed = new ArrayList();
        for (String ip: ips)
        {
            //String name = this.getRecordFieldValue(deviceNames, "name", "ip", ip);
            String type = this.getRecordFieldValue(deviceTypes, "type", "ip", ip);
            
            //read the name from Leases! 17/10/2012
            String name = this.getRecordFieldValue(leases, "hostname", "ipaddr", ip);
            
            long allowance = 0L;
            String allowanceText = this.getRecordFieldValue(allowances, "allowance", "ip", ip);
            //System.out.println(allowanceText);
            if (allowanceText!=null)
                if (!allowanceText.equals("null"))
                    allowance = Long.parseLong(allowanceText);
            long nbytes = 0L;
            if (totalUsage.containsKey(ip))
                nbytes = totalUsage.get(ip);
            String mac = this.getRecordFieldValue(leases, "mac", "ipaddr", ip);
            
            //String timestamp, String ip, String name, String type, String user, long allowance, long nbytes
            Device device = new Device(null, ip, name, type, null, allowance, nbytes);
            device.setMacaddr(mac);
            device.setTotalUsage(nbytes);
            
            devices.add(device);
            
            ipsUsed.add(ip);
        }
        
        Collections.sort(devices, new Comparator(){

            @Override
            public int compare(Object o1, Object o2) {
                if ((o1 instanceof Device) && (o2 instanceof Device))
                {
                    Device d1 = (Device)o1;
                    Device d2 = (Device)o2;
                    return d1.getIp().compareTo(d2.getIp());
                }
                return 1;
            }
            
        });
        System.out.println("devices: "+devices);
        return devices; 
    }      
    
    //get a field value knowing the value of another field: such as get the name of the device, knowing the ip
    private String getRecordFieldValue(ArrayList<TreeMap<String, Object>> records, String field, String fieldRef, String valueRef) {
        String value = null;
        if ((field==null)||(fieldRef==null)||(valueRef==null))
            return null;
        for (TreeMap<String, Object> record: records){
            if ((record.containsKey(field) && (record.containsKey(fieldRef))))
            {
                if (record.get(fieldRef).equals(valueRef))
                {
                    return (String)record.get(field);
                }
            }
        }
        return value;
    }    
   
}
