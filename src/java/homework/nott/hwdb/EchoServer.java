package homework.nott.hwdb;

import org.hwdb.srpc.Message;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;

/**
 * Hello world! author: txl
 *
 */
public class EchoServer
{

    public static void main(String[] args)
    {
        try {
            SRPC srpc = new SRPC(20000);
            Service service = srpc.offer("Echo");
            Message query;
            while ((query = service.query()) != null) {
                query.getConnection().response("1" + query.getContent());
            }

            } catch (Exception e) {
                System.exit(1);
            }

    }

    public static Service echo(SRPC srpc) {
            return srpc.offer("Echo");
    }





}
