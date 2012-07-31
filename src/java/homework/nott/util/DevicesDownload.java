/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author pszgp, 12 july 2012
 */
public class DevicesDownload {
    
    private CSVParser csv = new CSVParser();
    
    public void getDevicesDownload()
    {
        ArrayList<TreeMap<String, Object>> flows = csv.getRecords("KFlows");
        Set<String> ipList = csv.getDevicesIps();
        flows = csv.filterRecordsForField(ipList, flows, "saddr");
        ArrayList<DeviceData> devices = csv.getDevicesDownload(ipList, flows);
    }
}
