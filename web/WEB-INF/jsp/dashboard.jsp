<!-- author: pszgp, 25 april 2012 (renamed from socket.js to dashboard.js in may 2012) 
the index of the dashboard application
-->
<%@include file="header.jsp" %>        
<%@include file="leftmenu.jsp" %>

     <script type="text/javascript" src="js/device_bars.js"> </script>
     
     <script type="text/javascript" src="js/tabber.js"></script>
     <!-- tabs to view as table or graph; 28 aug. 2012
     tabber.js from: http://www.barelyfitz.com/projects/tabber/example.html -->
     <link rel="stylesheet" href="js/tabber/tabber.css" type="text/css" media="screen"/>
     <link rel="stylesheet" href="js/tabber/tabber_print.css" type="text/css" media="print"/>
     <style> 
         .tabber{display: none;}
         body{
             font: 12px Verdana, sans-serif;
         }
     </style>
     
     <div id="users_content">
         
         <h3>Welcome to the dashboard page of the Homework database.</h3>            
        <h4>Years considered: ${years}</h4>
        <h4>Total bandwidth usage per devices:</h4>   
        
        <div class ="tabber">
            <div class="tabbertab" title="Table view">
                        <h3>Table view:</h3>   
                        <table width="400px">
                            <tr style="background-color: #64A0DE; height: 10px;"><!--th>User</th--><th>Device Name</th><th>Device Type</th><th>Device Ip</th><th>Usage(Mb)</th><th>Allowance (Mb)</th></tr>
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
                                        <td height="10px">${device.name}</td><td>${device.type}</td>
                                        <td><a href="device.htm?ip=${device.ip}">${device.ip}</a></td>
                                        <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${device.nbytes/(1024*1024)}"/>Mb</td><!--${device.nbytesMb}-->
                                                                        <!--value="${device.nbytes/(1024*1024)} = ${device.nbytesMonths}"/></td-->
                                            <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                                        value="${device.allowance/(1024*1024)}"/></td>
                                    </tr>    
                            </c:forEach>    
                        </table>
               </div>             
                    <!--/td>
                    <td style="vertical-align: top;"-->
            <div class="tabbertab" title="Graph view">      
                    <h3>Graph view - horizontal stacked barchart:</h3>
                        <table><tr><td height="40px;"></td></tr>
                        <tr><td>
                        <div class="div_RootBody" id="bar_chart_1">
                            <div class="chart"></div>
                        </div>
                        <script type="text/javascript">
                            //chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName
                            drawHorizontalBarChart("Bars1", "#bar_chart_1 .chart", "null", "colorScale10", "${devicesTotal}", false, true, "null");//dataSet
                        </script> 
                        </td>
                    </tr>
                </table>            
                <br/>         
            </div>
                        
            <div class="tabbertab" title="Months graph view">            
                <h3>Graph view - vertical stacked barchart per months:</h3>
                <%@include file="dashboard_stacked.jsp" %>
            </div>
            
            <div class="tabbertab" title="Days & hours graph view">
                <h3>Partition sunburst (d3) for devices usage:</h3> 
                <br/>Move the mouse over to see the labels. <br/>
                Circles represent (from most inner to external): devices, years, months, days, hourly usage.<br/>
                
                <script type="text/javascript" src="js/d3/d3.v2.js"></script>
                <div id="chart"></div>
                <script type="text/javascript" src="js/sunburstchart/sunburstd3.js"></script>
                <script type="text/javascript">
                    var file = "js/sunburstchart/devicesUsage.json";
                        //complete data: all devices, per years, months, days, hours
                    drawPartitionSunburst(file);
                </script>
            
           
        </div><!-- end of the tabber --> 
        
     </div> <!-- end of the content -->
    </body>
</html>
