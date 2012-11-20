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
            <br/>
           
            <c:if test="${month==null}">                
                <h3>Device usage per months:</h3>
                <br/>
                <div class="div_RootBody" id="bar_chart_2">
                    <div class="chart"></div>
                </div>

                <script type="text/javascript">
                    //chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName
                    drawHorizontalBarChart("Bars1", "#bar_chart_2 .chart", "${deviceIp}", "colorScale10", "${deviceIntMonths}", true, false, "${month}");//dataSet
                </script>            
                <br/>
            </c:if>
        
        </div>
    </body>
</html>        