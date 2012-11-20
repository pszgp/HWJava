/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.mysql;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author pszgp
 */
public class HWTimestamp {
    
    static public Timestamp getTimestampFromUnixString(String hexa){
            try{
                //remove the @ characters from the first and last position of the string
                if (hexa.length() < 3)
                    return null;
                if (hexa.contains("@"))
                    hexa = hexa.replaceAll("@", "");
                long l=Long.parseLong(hexa, 16)/1000000;
                //System.out.println(l);
                //System.out.println(new Timestamp(l));
                return new Timestamp(l);
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println(hexa);
            }
            return null;
        }
    
    //test
    public static void main(String[] args){
        
        Timestamp time = HWTimestamp.getTimestampFromUnixString("@129db8f340ceb098@");
        System.out.println(time.getDate()+" "+time.getMonth()+" "+time.getTime());//Urls: 4th july 2012
        System.exit(0);
        
        System.out.println(HWTimestamp.getTimestampFromUnixString("@12883089a08c8eb8@"));
        System.exit(0);
        System.out.println(new String("2012-04-25").split("-")[0]);
        System.out.println("timestamp...");
        System.out.println(HWTimestamp.getTimestampFromUnixString("@1288cb3a7ad51e28@"));
        System.out.println(HWTimestamp.getTimestampFromUnixString("@128832f98e7ea8d8@"));
        System.out.println(HWTimestamp.getTimestampFromUnixString("@128c81f1c6384e78@"));
        System.exit(0);
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse("2006-05-22 14:04:59");
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            
            Timestamp tstmt = Timestamp.valueOf(timestamp.toString());
            System.out.println(tstmt + "\ntime=" + tstmt.getTime()+ " "+new Date(tstmt.getTime()));
            
            //Timestamp tstmt = Timestamp.valueOf("@1281e02eab7b860@");
            String hexa = "@1282024a6342ecf0@";
            hexa = hexa.replaceAll("@", "");
            
                String[] values = new String[]{hexa, "lala", "number...12.2.3.4"};
                String[] valuesWithTime = new String[values.length];
                    for (int i=0;i<values.length-1;i++)//omit the item after the last delimitor
                    {               
                        valuesWithTime[i] = values[i];     
                        String valueTimestamp = null;
                        /*if (i == 0)
                        {
                            valueTimestamp = values[i];
                            //System.out.println("timestamp value: " + valueTimestamp);
                            Timestamp t = getTimestampFromUnixString(valueTimestamp);
                            //System.out.println("timestamp: " + t);
                            if (t!=null)
                                valueTimestamp += t.toString();
                            valuesWithTime[i] = valueTimestamp;  
                        }   */                
                    }                    
            System.out.println(hexa);
            System.exit(0);
            System.out.println(hexa.indexOf("@"));
            if ((hexa.indexOf("@")==0)&&(hexa.lastIndexOf("@") == hexa.length()-1))
                hexa = hexa.substring(1, hexa.length()-1);
            System.out.println(hexa);
            long l=Long.parseLong(hexa, 16)/1000000;
            System.out.println(hexa+" "+l);
            Date d = new Date(l);
            System.out.println(d);
            tstmt = new Timestamp(d.getTime());
            System.out.println(tstmt);
            
        }catch(Exception e){e.printStackTrace();}
        //catch(java.text.ParseException e){e.printStackTrace();}

    }
}
