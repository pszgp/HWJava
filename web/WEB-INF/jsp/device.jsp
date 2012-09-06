<%-- 
    Document   : device
    Created on : 10-May-2012, 11:41:14
    Author     : pszgp
--%>
<%@include file="include.jsp" %>

<script type="text/javascript" src="js/device_bars.js"> </script>

<!--Selected device-->
<h3><p id="deviceDataUsage"><a href="device.htm?ip=${deviceIp}">Device: ${deviceIp}</p></a></h3>  
            
            <h3>Years considered: ${years}</h3>          

            <div id="deviceUsageMonth"/>
            
            <c:if test="${month!=null}">    
                <h3>Month: ${month}</h3>
                <h3>Device usage for the selected month:</h3>
                <br/>
                <div class="div_RootBody" id="bar_chart_1">
                    <div class="chart"></div>
                </div>
                <script type="text/javascript">
                    //alert("${deviceIp}");
                    //alert("${deviceDataMonths}");
                    drawHorizontalBarChart("Bars1", "#bar_chart_1 .chart", "${deviceIp}", "colorScale10", "${deviceDataDays}", false, "${month}");//dataSet
                </script>    
            </c:if>
            <!--table>
            <!--c:forEach var="usage" items="${deviceDataDays}">
                <tr><td>${usage.date} ${month}</td>
                    <td><!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                        value="${usage.nbytes/(1024*1024)}"/>Mb</td></tr>
            <!--/c:forEach>
            </table-->    
            <br/>
            
            <!--${deviceDataDays}
            <table>
            <!--c:forEach var="usage" items="${deviceDataMonths}">
                <!--c:set var="months" value="{JAN, FEB, MAR, APR}"/-->
                <!--%Sring[] months={"jan","feb","mar","apr"};%-->
                <!--c:set var = "monthId" value="${usage.date}"><!--/c:set>
                <!--%Object monthId = "${usage.date}";out.print(monthId);%-->
                <!--tr><td>${usage.date}</td>
                    <td><!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                        value="${usage.nbytes/(1024*1024)}"/>Mb</td>
                </tr>
            <!--/c:forEach>
            </table-->   
                      
                     
            <!-- 12june2012: sample selective barchart (only certain attributes/values) -->
            <!--%@include file="barchart/barchart_devices.jsp" %-->  
            <!-- 27 june 2012: frequencies of urls for the device -->
            <!--%@include file="barchart/barchart_urls_devices_frequencies.jsp" %> 
            
            <br/>
            <!--script type="text/javascript" src="js/d3.js_examples/sunburstd3.js"></script!-->
            <!--div id="chart">Chart d3.js</div>
            <br/>
            <br/>
            
           <!--%@include file="flowssums.jsp" %-->
            
            <!--%@include file="bandwidthusage.jsp" %-->
        
            <c:if test="${month==null}">                
                <h3>Device usage per months:</h3>
                <br/>
                <div class="div_RootBody" id="bar_chart_2">
                    <div class="chart"></div>
                </div>

                <script type="text/javascript">
                    //alert("${deviceIp}");
                    //alert("${deviceDataMonths}");
                    //chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName
                    drawHorizontalBarChart("Bars1", "#bar_chart_2 .chart", "${deviceIp}", "colorScale10", "${deviceIntMonths}", true, false, "${month}");//dataSet
                </script>            
                <br/>
            </c:if>
        
        </div>
    </body>
</html>        