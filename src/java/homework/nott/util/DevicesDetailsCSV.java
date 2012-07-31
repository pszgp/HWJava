/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import java.util.*;
/**
 *
 * @author pszgp, 12 july 2012
 */
public class DevicesDetailsCSV {
    
    private CSVParser csv = new CSVParser();
    public ArrayList<Device> getDevices(){
        ArrayList<Device> devices = new ArrayList();
        
        Set<String> devicesIps = csv.getDevicesIps();
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Integer>>>>> usage 
                = csv.getDevicesUsageHours(devicesIps);
        Set<String> ips = csv.getDevicesIps();
        System.out.println("ips: "+ips);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> usageDays 
                = csv.getDevicesUsageDays(usage);
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, Long>>> usageMonths 
                = csv.getDevicesUsageMonths(usage);
        TreeMap<String, TreeMap<Integer, Long>> usageYears 
                = csv.getDevicesUsageYears(usage);
        
        TreeMap<String, Long> totalUsage = csv.getTotalUsageDevices(usage);
        
        ArrayList<TreeMap<String, Object>> deviceNames = csv.getRecords("DeviceNames");
        ArrayList<TreeMap<String, Object>> deviceTypes = csv.getRecords("DeviceTypes");
        ArrayList<TreeMap<String, Object>> allowances = csv.getRecords("Allowances");
        //System.out.println(allowances);
        //System.exit(0);
        ArrayList<TreeMap<String, Object>> leases = csv.getRecords("Leases");
        
        ArrayList<String> ipsUsed = new ArrayList();
        for (String ip: ips)
        {
            String name = this.getRecordFieldValue(deviceNames, "name", "ip", ip);
            String type = this.getRecordFieldValue(deviceTypes, "type", "ip", ip);
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
        /*for (TreeMap<String, Object> data: deviceNames){
            String ip = null;
            if (data.containsKey("ip"))
            {
                ip = (String)data.get("ip");            
                if (!ipsUsed.contains(ip)){
                    
                    String name = this.getRecordFieldValue(deviceNames, "name", "ip", ip);
                    String type = this.getRecordFieldValue(deviceTypes, "type", "ip", ip);
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
                    
                    Device d = new Device(null, ip, name, type, null, allowance, nbytes);
                    d.setMacaddr(mac);
                    
                    devices.add(d);
                    
                }
            }
        }*/
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
    
    /*public ArrayList<DeviceHistory> getDevicesHistoryMonths(TreeMap<String, TreeMap<Integer, TreeMap<Integer, Integer>>> usageMonths)
    {
        ArrayList<DeviceHistory> devicesHistory = new ArrayList();
        for (String ip: usageMonths.keySet())
        {
            TreeMap<Integer, TreeMap<Integer, Integer>>>>TreeMap<Integer, TreeMap<Integer, Integer>> years = usageMonths.get(ip);
            for (Integer year: years.keySet())
            {
                TreeMap<Integer, Integer> months = years.get(year);
                for (Integer month: months.keySet())
                {
                    int nbytes = months.get(month);
                    String date = year+"-"+month+"-"+"0";
                    DeviceHistory dh = new DeviceHistory(ip, date, nbytes);
                    dh.setMonth(month);
                    dh.setYear(year);
                    devicesHistory.add(dh);
                }
            }
            return devicesHistory;
        }
        return devicesHistory;
    }*/
    
    public static void main(String[] args)
    {
        DevicesDetailsCSV details = new DevicesDetailsCSV();
        details.getDevices();
    }
}
