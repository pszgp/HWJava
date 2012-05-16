<!-- author: pszgp, 25 april 2012 (renamed from socket.js to dashboard.js in may 2012) 
the index of the dashboard application
-->
<%@include file="header.jsp" %>        
<%@include file="leftmenu.jsp" %>

     <div id="users_content">       
            <h3>Welcome to the dashboard page of the Homework database.</h3>

            <h3>Total bandwidth usage per devices and users:</h3>    
            <table border="1">
                <tr><th>User</th><th>Device Name</th><th>Device Type</th><th>Device Ip</th><th>Usage(Mb)</th><th>Allowance (Mb)</th></tr>
                <c:forEach var="device" items="${devices}">
                    <!--${device}<br/-->
                    <script>
                        //alert("${device.ip}");                        
                    </script>
                    <tr><td><a href="user.htm?name=${device.user}">${device.user}</td>
                        <td>${device.name}</td><td>${device.type}</td>
                        <td><a href="device.htm?ip=${device.ip}">${device.ip}</a></td>
                          <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                             value="${device.nbytesMb}"/></td>
                                                          <!--value="${device.nbytes/(1024*1024)} = ${device.nbytesMonths}"/></td-->
                              <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${device.allowance/(1024*1024)}"/></td>
                    </tr>
                </c:forEach>    
            </table>
            <br/>                    
          
            <!--table border="1">
            <tr><th>Device Ip</th><th>Year</th><th>Month</th><th>Day</th><th>Date Usage (bytes)</th><th>Data Usage (Mb)</th></tr>
            <!--c:forEach var="device" items="${devicesPerMonths}">                            
                <h2>DeviceIp: ${device.key}</h2>
                <!--c:forEach var="deviceYear" items="${device.value}">
                    <h3>Year: ${deviceYear.key}</h3>
                    <!--c:forEach var="deviceYearMonth" items="${deviceYear.value}">  
                        <!--h4>Month: ${deviceYearMonth.key}</h4-->
                        <!--c:set var="usageMonth" value="0"><!--/c:set>
                        <!--c:forEach var="deviceYearMonthDay" items="${deviceYearMonth.value}">
                           <tr><td style="text-align: left">${device.key}</td>
                               <td style="text-align: left">${deviceYear.key}</td>
                               <td style="text-align: left">${deviceYearMonth.key}</td>
                               <td style="text-align: left">${deviceYearMonthDay.key}</td>
                               <td style="text-align: right">${deviceYearMonthDay.value}</td>
                               <td style="text-align: right">
                                   <!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                value="${deviceYearMonthDay.value/(1024*1024)}"/>
                                   <!--c:set var="usageMonth" value="${usageMonth + deviceYearMonthDay.value/(1024*1024)}"><!--/c:set>
                               </td>
                           </tr>
                        <!--/c:forEach>
                        <h4>${deviceYearMonth.key} usage: <!--fmt:formatNumber value="${usageMonth}"/></h4>
                    <!--/c:forEach>
                <!--/c:forEach>
            <!--/c:forEach> 
            </table-->
            
        </div>
    </body>
</html>
