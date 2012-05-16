<%-- 
    Document   : bandwidthusage
    Created on : 12-May-2012, 20:28:30, 15 may 2012
    Author     : pszgp
--%>
            
            <h3>Bandwidth usage per device </h2>
            <!--i>${deviceIp}</i> for month <i>${month}${monthId}</i> </h2><br/-->
            <table> <!-- border="1"><!-- descending order -->
            <c:set var="countRows" value="0"/>     
            <tr style="background-color: #64A0DE; font-size: 14pt;"><th>Device Ip</th>
                <th>Date(y/m/d)</th><th>Date Usage (bytes)</th><th>Data Usage (Mb)</th></tr>
                <c:forEach var="device" items="${devicesHistory}"> 
                    <script>
                        var MONTHS = ["JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
                        var deviceMonth = "${device.month}";
                        var deviceMonthName = MONTHS[deviceMonth-1];
                        var month = "${month}";
                        //if (deviceMonthName == month){
                    </script>   
                    <c:if test="${(monthId == (device.month - 1)) && (deviceIp == device.ip)}"> 
                    <c:set var="countRows" value="${countRows + 1}"/> 
                    <c:choose>
                        <c:when test='${(countRows)%2 eq 0}'>
                        <c:set var="rowColor" value="even" scope="page"/>
                            <tr style="background-color:#64A0DE;">                                                                    
                                <td style="text-align: left">${device.ip}</td>
                                <td style="text-align: left">${device.date}</td>    
                                <td style="text-align: right">${device.nbytes}</td>
                                <td style="text-align: right"><!--${device.nbytesMb}-->
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                        value="${device.nbytes/(1024*1024)}"/>
                                </td>
                            </tr>  
                        </c:when>
                        <c:otherwise>
                        <c:set var="rowColor" value="odd" scope="page"/>
                            <tr style="background-color:white">                                                                    
                                <td style="text-align: left">${device.ip}</td>
                                <td style="text-align: left">${device.date}</td>    
                                <td style="text-align: right">${device.nbytes}</td>
                                <td style="text-align: right"><!--${device.nbytesMb}-->
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                        value="${device.nbytes/(1024*1024)}"/>
                                </td>
                            </tr>  
                        </c:otherwise>
                    </c:choose>
                    <!--/c:when-->  
                    </c:if>
                </c:forEach>
            </table> 
