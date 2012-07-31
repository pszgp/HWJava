<!-- author: pszgp, 25 april 2012 (renamed from socket.js to dashboard.js in may 2012) 
the index of the dashboard application
-->
<%@include file="header.jsp" %>        
<%@include file="leftmenu.jsp" %>

     <div id="users_content">       
            <h3>Welcome to the dashboard page of the Homework database.</h3>

            <h3>Total bandwidth usage per devices:</h3>   
            <table>
                <tr style="background-color: #64A0DE;""><!--th>User</th--><th>Device Name</th><th>Device Type</th><th>Device Ip</th><th>Usage(Mb)</th><th>Allowance (Mb)</th></tr>
                <c:set var="rowCount" value="0"/>
                <c:forEach var="device" items="${devices}">
                    <c:set var="rowCount" value="${rowCount+1}"/>
                    <c:choose>
                        <c:when test='${(rowCount)%2 eq 0}'>
                        <tr style="background-color: #64A0DE;">
                        </c:when>
                        <c:otherwise>
                        <tr style="">
                        </c:otherwise>  
                    </c:choose>           
                            <!--td><a href="user.htm?name=${device.user}">${device.user}</td-->
                            <td>${device.name}</td><td>${device.type}</td>
                            <td><a href="device.htm?ip=${device.ip}">${device.ip}</a></td>
                            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                value="${device.nbytes/(1024*1024)}"/>Mb</td><!--${device.nbytesMb}-->
                                                            <!--value="${device.nbytes/(1024*1024)} = ${device.nbytesMonths}"/></td-->
                                <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${device.allowance/(1024*1024)}"/></td>
                        </tr>    
                </c:forEach>    
            </table>
            <br/>     
            
            
            <table>
                <tr style="vertical-align: top"><td>                        
                        <%@include file="dashboard_stacked.jsp"%>                        
                    </td>
                    <td style="widht: 30px;"></td>
                    <td>
                        <h3>Total bandwidth usage per months:</h3>   
                        <c:set var="monthsNames" scope="request">JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
                        </c:set>
                        <table>
                            <tr style="background-color: #64A0DE;""><th>Month</th><th>Device</th><th>Usage (Mb)</th></tr>
                            <c:set var="rowCount" value="0"/>
                            <c:forEach var="monthData" items="${dataMonthsDevices}">
                            <c:forEach var="devicesMonth" items="${monthData.value}">
                                <c:set var="containsDevice" value="false" />
                                    <c:forEach var="item" items="${devicesIps}">
                                    <c:if test="${item eq devicesMonth.key}">
                                        <c:set var="containsDevice" value="true" />
                                    </c:if>
                                    </c:forEach>    
                                    <c:if test="${containsDevice==true}">
                                        <c:set var="rowCount" value="${rowCount+1}"/>
                                        <c:choose>
                                        <c:when test='${(rowCount)%2 eq 0}'>
                                            <tr style="background-color: #64A0DE;">
                                        </c:when>
                                        <c:otherwise>
                                            <tr style="">
                                        </c:otherwise>  
                                        </c:choose>    
                                                <td>${monthData.key}.
                                                    <c:set var="mCount" value="0"></c:set>
                                                    <c:forEach var="m" items="${monthsNames}">
                                                        <c:set var="mCount" value="${mCount+1}"/>
                                                        <c:if test="${mCount==(monthData.key)}">                                               
                                                        <c:out value="${m}"/>
                                                        </c:if>    
                                                    </c:forEach>
                                                </td>
                                        <td>${devicesMonth.key}</td>
                                        <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                            value="${devicesMonth.value/(1024*1024)}"/>Mb
                                        </td> 
                                        </tr>
                                    </c:if>  
                            </c:forEach>
                            </c:forEach>    
                        </table>
                    </td>
                </tr>
                
            </table>            
            
            <br/>                
           
        </div>
    </body>
</html>
