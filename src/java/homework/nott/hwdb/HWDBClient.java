package homework.nott.hwdb;

import java.io.IOException;
import java.lang.Runnable;
import java.lang.String;
import java.lang.Thread;
import java.util.logging.Level;
import org.hwdb.srpc.*;
import org.hwdb.srpc.Service;

public class HWDBClient implements Runnable {

    public static void main(String[] args)
    {
        try {
            String serviceName = "Handler";
            SRPC srpc = new SRPC();
            service = srpc.offer(serviceName);
            (new Thread(new HWDBClient())).start();
            Connection conn = srpc.connect("localhost", 987,"HWDB");
            int port = srpc.details().getPort();
            System.out.println(conn.call(String.format("SQL:subscribe LinksLast 127.0.0.1 %d %s", port, serviceName)));

            } catch (Exception e) {
                System.exit(1);
            }

    }
    static Service service;
    public static Service getService(){
        return service;
    }
    public static void setService(Service s)
    {
        service = s;
    }
    public void run() {
        try {
            Message query;
            while ((query = service.query()) != null) {
                System.out.println(query.getContent());
                
                String content = query.getContent();
                String[] rows = content.split("\n");
                if (rows.length>=3)
                {
                    //print the field types
                    String typesRow = rows[1];
                    String[] types = typesRow.split("<\\|>");
                    for (int i=0;i<types.length-1;i++)//omit the item after the last delimitor
                        System.out.println("type="+types[i]);
                    
                    //print the field values
                    String valuesRow = rows[2];
                    String[] values = valuesRow.split("<\\|>");
                    for (int i=0;i<values.length-1;i++)//omit the item after the last delimitor
                        System.out.println("value="+values[i]);
                }
                
                query.getConnection().response("OK");
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }


}
