/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.hwdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author pszgp, 11 may 2012, retrieve the device history from mysql instead of hw 
 *  (quicker access, let the hw data for the admin only)
 */
public class DeviceHistory {
    String ip;
    String date;
    long nbytes;
    double nbytesMb;
    int year;

    //parse the day in the format: yyyy-mm-dd
    public int getDay() {
        return Integer.parseInt(date.split("-")[2]);//day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return Integer.parseInt(date.split("-")[1]);//month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return Integer.parseInt(date.split("-")[0]);//year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    int month;
    int day;
    
    public DeviceHistory(String ip, String date, int nbytes)
    {
        this.ip=ip; this.date=date; this.nbytes=nbytes;
        this.nbytesMb = (double)this.nbytes/(1024 * 1024);//Megabytes
        
        if (date!=null)
        {
            //parse the day to get year, month, day
            try{
                year = Integer.parseInt(date.split("-")[0]);
                month = Integer.parseInt(date.split("-")[1]);
                day = Integer.parseInt(date.split("-")[2]);
            }catch(NumberFormatException e){
                e.printStackTrace();
                System.out.println("ERROR: The date is not in the required format (yyyy-mm-dd): " + date);
            }
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getNbytes() {
        return nbytes;
    }

    public void setNbytes(long nbytes) {
        this.nbytes = nbytes;
    }

    public double getNbytesMb() {
        return nbytesMb;
    }

    public void setNbytesMb(double nbytesMb) {
        this.nbytesMb = nbytesMb;
    }
    
    public static ArrayList<DeviceHistory> sortDevicesHistory(ArrayList<DeviceHistory> dh){
        Collections.sort(dh, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                if ((o1 instanceof DeviceHistory)&&(o2 instanceof DeviceHistory))
                {
                    DeviceHistory dh1 = (DeviceHistory)o1;
                    DeviceHistory dh2 = (DeviceHistory)o2;
                    if (dh1.getYear() < dh2.getYear())
                        return 1;
                    if (dh1.getYear() > dh2.getYear())
                        return -1;
                    if (dh1.getYear() == dh2.getYear())
                    {
                        if (dh1.getMonth() < dh2.getMonth()) return 1;
                        if (dh1.getMonth() > dh2.getMonth()) return -1;
                        if (dh1.getMonth() == dh2.getMonth())
                        {
                            if (dh1.getDay() < dh2.getDay()) return 1;
                            if (dh1.getDay() > dh2.getDay()) return -1;
                            if (dh1.getDay() == dh2.getDay()) return 0;
                        }
                        return 0;
                    }
                    return 0;
                }
                return 0;
            }
        });
        return dh;
    }
    
    public String toString(){
        return "DH=["+this.ip+" "+this.date+" "+this.nbytes+"]";
    }
}
