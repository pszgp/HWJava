<!-- pszgp, 11 may 2012, the dashboard for admin only 
(to use in order to update the table HWDevicesUsage 
 - login required
-->
<%@include file="include.jsp" %>

            <h3>Total bandwidth usage per devices and users:</h3>    
            <table border="1">
                <tr><th>User</th><th>Device Name</th><th>Device Type</th><th>Device Ip</th><th>Usage(Mb)</th><th>Allowance (Mb)</th></tr>
                <c:forEach var="device" items="${devices}">
                    <!--${device}<br/-->
                    <tr><td>${device.user}</td><td>${device.name}</td><td>${device.type}</td><td>${device.ip}</td>
                          <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${device.totalUsageFlows/(1024*1024)}"/><!--${device.nbytes}--></td>
                              <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${device.allowance/(1024*1024)}"/></td>
                    </tr>
                </c:forEach>    
            </table>
            <br/>
        
        
            ${flows}
            
            <h2> Bandwidth usage per device: </h2><br/>
            
            <h3><a class="a_header" href="devicegraph.htm?ip=${deviceDataUsage}">Switch to graph view</a></h3>
            
            <table border="1">
            <tr><th>Device Ip</th><th>Year</th><th>Month</th><th>Day</th><th>Date Usage (bytes)</th><th>Data Usage (Mb)</th></tr>
            <c:forEach var="device" items="${devicesPerMonths}">                            
                <h2>DeviceIp: ${device.key}</h2>
                <c:forEach var="deviceYear" items="${device.value}">
                    <h3>Year: ${deviceYear.key}</h3>
                    <c:forEach var="deviceYearMonth" items="${deviceYear.value}">  
                        <!--h4>Month: ${deviceYearMonth.key}</h4-->
                        <c:set var="usageMonth" value="0"></c:set>
                        <c:forEach var="deviceYearMonthDay" items="${deviceYearMonth.value}">
                           <tr><td style="text-align: left">${device.key}</td>
                               <td style="text-align: left">${deviceYear.key}</td>
                               <td style="text-align: left">${deviceYearMonth.key}</td>
                               <td style="text-align: left">${deviceYearMonthDay.key}</td>
                               <td style="text-align: right">${deviceYearMonthDay.value}</td>
                               <td style="text-align: right">
                                   <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                value="${deviceYearMonthDay.value/(1024*1024)}"/>
                                   <c:set var="usageMonth" value="${usageMonth + deviceYearMonthDay.value/(1024*1024)}"></c:set>
                               </td>
                           </tr>
                        </c:forEach>
                        <h4>${deviceYearMonth.key} usage: <fmt:formatNumber value="${usageMonth}"/></h4>
                    </c:forEach>
                </c:forEach>
            </c:forEach> 
            </table>
            
        </div>
    </body>
</html>
