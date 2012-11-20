/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.device;

/**
 *
 * @author pszgp
 */
public class DeviceIP {
    public final static String DEVICE_IP_PREFIX = "10.2.0.";
    public final static String ROUTER_IP = "10.2.0.2";
    
    public static boolean isValidDeviceIp(String ip)
    {
        if (ip == null) return false;
        if (ip.startsWith(DEVICE_IP_PREFIX))
            return true;
        return false;
    }
    public static boolean isRooterIp(String ip)
    {
        if (ip == null) return false;
        if (ip.equals(ROUTER_IP))
            return true;
        return false;
    }
}
