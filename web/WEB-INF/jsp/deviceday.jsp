<%-- 
    Document   : device
    Created on : 16 may 2012
    Author     : pszgp
--%>
<%@include file="include.jsp" %>

<!--Selected device-->
<h3><p id="deviceDataUsage">Device: ${deviceIp}</p></h3>  
            
            <!--div id="deviceUsageMonth"/-->
                                    
            <!--Flows sums: ${flowsSums}-->
            <!--Day: ${day} ${month}-->
            
            <!--%@include file="bandwidthusage.jsp" %-->
            
            <!--script type="text/javascript" src="js/sunburstchart/sunburstd3.js"></script>
            
            <div id="chart" style="background-color: green; width: 800px; height: 800px;">Chart
                <button id="size">Size</button>
                <button id="count">Count</button>            
            </div-->            
            
            <br/>

            Partition sunburst (d3) for devices usage. Mouse over to see the labels. <br/>
            First circle (inner): device(s)<br/>
            Second circle: years <br/>
            Third circle: months<br/>
            Fourth circle: days <br/>
            Fifth circle (external): hours <br/>
                      
            
            <br/>
            <script type="text/javascript" src="js/d3/d3.v2.js"></script>
            <div id="chart">
            <!--button id="size" class="first">
                Size
            </button><button id="count" class="active last">
                Count
            </button><p-->
            </div>
            <script type="text/javascript" src="js/sunburstchart/sunburstd3.js"></script>
            <script type="text/javascript">
                var file = "js/sunburstchart/flare.json";
                file = "js/sunburstchart/devices.json";//mock - ok
                file = "js/sunburstchart/deviceshours.json";//real 
                file = "js/sunburstchart/devicesUsage.json";//complete: all devices, per years, months, days, hours
                drawPartitionSunburst(file);
            </script>
            
            <!--c:forEach var="device" items="{flowsSums}">
                <!--${device.key}<br/>
                <!--c:forEach var="usageYear" items="${device.value}">
                    <!--${usageYear.key}<br/>
                    <!--c:forEach var="usageMonth" items="${usageYear.value}">
                        <!--${usageMonth.key}<br/>
                        <!--c:forEach var="usageDay" items="${usageMonth.value}">
                            <!--${usageDay.key}<br/>
                            <!--c:forEach var="usageHour" items="${usageDay.value}">
                                <!--${usageHour.key} = ${usageHour.value}<br/>
                            <!--/c:forEach>    
                        <!--/c:forEach>
                    <!--/c:forEach>
                <!--/c:forEach>  
            <!--/c:forEach-->
            
        <h3><a class="a_header" href="device.htm?ip=${deviceIp}"><!--&month=${month}"-->
                Back to the total device usage <!-- of the device days view -->
            </a>
        </h3>
        <br/> 
            
            
        </div>
    </body>
</html>        