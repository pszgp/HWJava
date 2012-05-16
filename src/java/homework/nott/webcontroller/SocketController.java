/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.nott.webcontroller;

import homework.nott.hwdb.HWDBClient;
import com.sun.grizzly.websockets.WebSocketEngine;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.Message;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//pszgp, 26/03-11/04 2012

@Controller
public class SocketController implements Runnable{
 
    final int MAX_SESSION_INTERVAL = 1000000;
    static ModelAndView mvHWDB = new ModelAndView();
    private int MAX_ROWS_RESULT_SET = 1000;
    
    @RequestMapping("/socket")      
    public ModelAndView socket(HttpServletRequest request, HttpServletResponse response) { 
       
        //subscribe to hwdb when the session starts
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        System.out.println(sessionId);
        System.out.println(session.getValueNames().length);
        if (session.getValueNames().length==0)
        {
            session.setAttribute("hwdb_session_id", sessionId);
            
            try {
            String serviceName = "Handler";
            SRPC srpc = new SRPC();
            //HWDBClient.setService(srpc.offer(serviceName));
            service = srpc.offer(serviceName);
            //(new Thread(new HWDBClient())).start();
            new Thread(new SocketController()).start();
            Connection conn = srpc.connect("localhost", 987,"HWDB");
            int port = srpc.details().getPort();
            System.out.println(conn.call(String.format("SQL:subscribe LinksLast 127.0.0.1 %d %s", port, serviceName)));

            } catch (Exception e) {
                System.exit(1);
            }
        }
        else{
            Object o = session.getAttribute("sessionAttrsNr");
            if (o != null)
            {
                int sessionAttrsNr = Integer.parseInt(o.toString());
                sessionAttrsNr++;
                session.setAttribute("sessionAttrsNr", sessionAttrsNr);
            }
            
        }
        
        session.setMaxInactiveInterval(MAX_SESSION_INTERVAL);
        
        mvHWDB.addObject("sessionId", sessionId);
        mvHWDB.addObject("sessionAttrsNr", session.getValueNames().length);
        
        //if session started, receive mesages from hwdb and send to client
        
        return mvHWDB;
    }
    
    static Service service;
    public void run() {
        try {
            Message query;
            while ((query = service.query()) != null) {
                System.out.println(query.getContent());                
                String content = query.getContent();
                String[] rows = content.split("\n");
                int countRows = 0;
                int countFields = 0;
                if (rows.length>=3)
                {
                    //get the field types
                    String typesRow = rows[1];
                    String[] types = typesRow.split("<\\|>");
                    
                    ArrayList<String[]> valuesRows = new ArrayList();
                    
                    countFields = types.length;
                    
                    if (countFields > 1)//at least a timestamp field
                    //get all the rows of the result set
                    for (int j = 2; j < rows.length; j=j+3)
                    {
                        //get the field values of the current row
                        String valuesRow = rows[j];//2];
                        String[] values = valuesRow.split("<\\|>");
                        if (values.length != types.length)
                            continue;
                        //System.out.println("number of columns: " + values.length);
                        //System.exit(0);

                        String[] valuesWithTime = new String[countFields];
                        for (int i=0;i<values.length;i++)
                        {               
                            valuesWithTime[i] = values[i];                          
                            if (i == 0)
                            {
                                String valueTimestamp = values[i];
                                Timestamp t = getTimestampFromUnixString(valueTimestamp);
                                if (t!=null)
                                    valueTimestamp = t.toString();
                                valuesWithTime[i] = valueTimestamp;  
                            }               
                        } 
                        valuesRows.add(valuesWithTime);
                        countRows++;
                    }
                    mvHWDB.addObject("types", types);
                    mvHWDB.addObject("items", valuesRows);
                    mvHWDB.addObject("countFields", countFields);
                    mvHWDB.addObject("countRows", countRows);
                }
                
                query.getConnection().response("OK");
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }
    
    Timestamp getTimestampFromUnixString(String hexa){
        try{
            //remove the @ characters from the first and last position of the string
            if (hexa.length() < 3)
                return null;
            if (hexa.contains("@"))
                hexa = hexa.replaceAll("@", "");
            long l=Long.parseLong(hexa, 16)/1000000;
            return new Timestamp(l);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

