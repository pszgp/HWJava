/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.util;

/**
 *
 * @author pszgp, 17 july 2012
 */
public class DateDeviceUsage {

    String date = null;//day, month, or year...
    long nbytes=0L;//data usage for the specified date
    
    public DateDeviceUsage(String date, long nbytes)
    {
        this.date = date;
        this.nbytes = nbytes;
    }    
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getNbytes() {
        return nbytes;
    }

    public void setNbytes(long nbytes) {
        this.nbytes = nbytes;
    }
    
}
