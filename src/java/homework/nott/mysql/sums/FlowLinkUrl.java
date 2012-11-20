package homework.nott.mysql.sums;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pszgp, 24 april 2012
 */
public class FlowLinkUrl {
    
    public FlowLinkUrl(String timestamp, int proto, String saddr, int sport, 
                        String daddr, int dport, int npkts, int nbytes, 
                        String macaddr, double rssi, int nretries, 
                        String hst, String uri, String cnt, String action, String hostname)
    {
        this.timestamp = timestamp;
        this.proto = proto;
        this.saddr = saddr;
        this.sport = sport;
        this.daddr = daddr;
        this.dport = dport;
        this.npkts = npkts;
        this.nbytes = nbytes;
        this.macaddr = macaddr;
        this.rssi = rssi;
        this.uri = uri;
        this.cnt = cnt;
        this.action = action;
        this.hostname = hostname;
    }
    
    //table Flows:
    //  timestamp (timestamp), proto (integer), saddr (varchar), sport (integer), daddr (varchar), dport (integer), npkts (integer), nbytes (integer)
    private String timestamp;
    private int proto;
    private String saddr;
    private int sport;
    private String daddr;
    private int dport;
    private int npkts;
    private int nbytes;
    
    //table Links:
    //  timestamp (timestamp), macaddr (varchar), rssi (real), nretries (integer), npkts (integer), nbytes (integer)
    private String macaddr;
    private double rssi;
    private int nretries;    
    
    //table Urls:
    //  timestamp (timestamp), proto (integer), saddr (varchar), sport (integer), daddr (varchar), dport (integer), hst (varchar), uri (varchar), cnt (varchar)
    private String hst;//e.g.: 10.2.0.2:8080
    private String uri;//e.g.: HWJavaSocket/socket.htm
    private String cnt;//e.g.: NULL
    
    //table Leases:
    private String action;//user action?//ipaddr, maccaddr
    private String hostname;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getDaddr() {
        return daddr;
    }

    public void setDaddr(String daddr) {
        this.daddr = daddr;
    }

    public int getDport() {
        return dport;
    }

    public void setDport(int dport) {
        this.dport = dport;
    }

    public String getHst() {
        return hst;
    }

    public void setHst(String hst) {
        this.hst = hst;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public int getNbytes() {
        return nbytes;
    }

    public void setNbytes(int nbytes) {
        this.nbytes = nbytes;
    }

    public int getNpkts() {
        return npkts;
    }

    public void setNpkts(int npkts) {
        this.npkts = npkts;
    }

    public int getNretries() {
        return nretries;
    }

    public void setNretries(int nretries) {
        this.nretries = nretries;
    }

    public int getProto() {
        return proto;
    }

    public void setProto(int proto) {
        this.proto = proto;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public String getSaddr() {
        return saddr;
    }

    public void setSaddr(String saddr) {
        this.saddr = saddr;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
}
