/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql.sums;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 *
 * @author pszgp
 */
public class HWMySQLFlows {
    //for each device, each year, month, day, hour, get the data usage
    public static TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> getDetailsFLowsMySQL()
    {
        TreeMap<String, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>>> result = new TreeMap();
        ArrayList<TreeMap<String, Object>> resultSet = HWMySQLEngine.getInstance().queryMySQL("SELECT * FROM Flows", null);
        for (TreeMap<String, Object> record: resultSet)
        {
            String deviceIp = (String) record.get("saddr");
            Object bytesRecord = (Object)record.get("nbytes");
            long bytes = (Integer)bytesRecord;
            
            String date = (String)record.get("last");
            Timestamp t = HWTimestamp.getTimestampFromUnixString(date);
            Date d = new Date(t.getTime());
            
            int month = d.getMonth();
            int day = d.getDate();
            int year = d.getYear()+1900;
            int hour = d.getHours();
            
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>>> deviceUsage = new TreeMap();
            if (result.containsKey(deviceIp))
                deviceUsage = result.get(deviceIp);            
            
            TreeMap<Integer, TreeMap<Integer, TreeMap<Integer, Long>>> deviceUsageYear = new TreeMap();
            if (deviceUsage.containsKey(year))
                deviceUsageYear = deviceUsage.get(year);
            
            TreeMap<Integer, TreeMap<Integer, Long>> deviceUsageYearMonth = new TreeMap();;            
            if (deviceUsageYear.containsKey(month))
                deviceUsageYearMonth = deviceUsageYear.get(month);
            
            TreeMap<Integer, Long> deviceUsageYearMonthDay = new TreeMap();            
            if (deviceUsageYearMonth.containsKey(day))
                deviceUsageYearMonthDay = deviceUsageYearMonth.get(day);
            
            Long usage=0L;
            if (deviceUsageYearMonthDay.containsKey(hour))
                usage = deviceUsageYearMonthDay.get(hour);
            usage+=bytes;
            
            deviceUsageYearMonthDay.put(hour, usage);
            deviceUsageYearMonth.put(day, deviceUsageYearMonthDay);
            deviceUsageYear.put(month, deviceUsageYearMonth);
            deviceUsage.put(year, deviceUsageYear);
            
            result.put(deviceIp, deviceUsage);
        }
        System.out.println("Flows sums per hours: " + result);
        return result;
    }
}
