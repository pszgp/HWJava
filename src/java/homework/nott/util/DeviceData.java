/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

import java.sql.Timestamp;

/**
 *
 * @author pszgp, 12 july 2012, replace the Device class, add the downloaded/uploaded nbytes
 */
class DeviceData {
    private final String ip;
    private final int day;
    private final int month;
    private final int year;
    private final Timestamp tstamp;
    private final int nbytesDownload;
    private final int nbytesMb;
    private final int nbytesDownloadMb;
    private final int nbytsUpload;
    private final int nbytsUploadMb;
    private final int nbytes;
    private final int hour;

    DeviceData(String ip, int hour, int day, int month, int year, Timestamp t, int nbytesDownload, int nbytesUpload, int nbytes) {
        this.ip = ip;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.tstamp = t;
        this.nbytesDownload = nbytesDownload;
        this.nbytesDownloadMb = nbytesDownload/(1024*1024);
        this.nbytsUpload = nbytesUpload;
        this.nbytsUploadMb = nbytesUpload/(1024*1024);
        this.nbytes = nbytes;
        this.nbytesMb = nbytes/(1024*1024);
    }
    
}
