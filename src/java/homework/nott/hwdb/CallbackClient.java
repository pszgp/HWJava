package homework.nott.hwdb;

import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.Message;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;

/**
 * Hello world! author: txl
 *
 */
public class CallbackClient implements Runnable
{

    public static void main(String[] args)
    {
        try {
            SRPC srpc = new SRPC();
            service = srpc.offer("Handler");
            (new Thread(new CallbackClient())).start();
            Connection conn = srpc.connect("localhost", 20001,"Callback");
            int port = srpc.details().getPort();
            System.out.println(conn.call(String.format("connect localhost %d Handler", port)));

            } catch (Exception e) {
                System.exit(1);
            }

    }

    public static Service echo(SRPC srpc) {
            return srpc.offer("Echo");
    }


    static Service service;
    public void run() {
        try {
            Message query;
            while ((query = service.query()) != null) {
                System.out.println(query.getContent());
                query.getConnection().response("OK");
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }


}
