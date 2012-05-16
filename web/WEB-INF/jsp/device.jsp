<%-- 
    Document   : device
    Created on : 10-May-2012, 11:41:14
    Author     : pszgp
--%>
<%@include file="include.jsp" %>

<!--Selected device-->
<h3><p id="deviceDataUsage">Device: ${deviceIp}</p></h3>  
            
            <div id="deviceUsageMonth"/>
            
            <!--input type="hidden" name="currentMonth" value=""/-->
                                    
            <%@include file="bandwidthusage.jsp" %>
            
            <br/>
            <h3><a class="a_header" href="devicegraph.htm?ip=${deviceIp}&month=${month}">Switch to graph view</a></h3>
            <br/> 
            
            
        </div>
    </body>
</html>        