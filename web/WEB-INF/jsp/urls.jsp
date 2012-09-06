<%-- 
    Document   : urls
    Created on : 10-May-2012, 11:41:23
    Author     : pszgp

--%>
<!--%@include file="header.jsp" %-->        
<!--%@include file="leftmenu.jsp" %-->

<%@include file="include.jsp"%>
             
        <h3>Urls:</h3>             
        
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
     
     <c:choose>
        <c:when test="${url eq null}">
            <div class ="tabber">
                <div class="tabbertab" title="Tagcloud">
                    <%@include file="tagcloudurls.jsp"%>
                </div>
                <div class="tabbertab" title="Table view">
                    <h3>Urls occurences: </h3><br/>
                        <table style="width: 450px">
                            <tr style="background-color: #64A0DE;""><th>Device</th><th style="width: 250px;">Visited website</th><th>Frequency</th></tr>
                            <c:set var="rowCount" value="0"/>
                            <c:forEach var="device" items="${urls}">
                                <c:set var="rowCount" value="${rowCount+1}"/>
                                <c:choose>
                                    <c:when test='${(rowCount)%2 eq 0}'><tr style="background-color: #64A0DE;"></c:when>
                                    <c:otherwise><tr style=""></c:otherwise>  
                                </c:choose>           
                                <c:forEach var="uri" items="${device.value}">
                                    <tr><td>${device.key}</td><td style="width: 250px">${uri.key}</td><td>${uri.value}</td></tr>
                                </c:forEach>
                            </c:forEach>    
                        </table>
                        <br/>         
                </div>                   
            </div>   
       
        </c:when>
        <c:otherwise> <div class="tabbertab" title="Graph view">      
                    <script type="text/javascript" src="js/device_bars.js"> </script>
                    <h3>Graph view of the selected url - horizontal stacked barchart:</h3>
                        URL = ${url}
                        <br/>
                        ${urlsView}
                        <br/>
                        <table><tr><td height="40px;"></td></tr>
                        <tr><td>
                        <div class="div_RootBody" id="bar_chart_2">
                            <div class="chart"></div>
                        </div>
                        <script type="text/javascript">
                            //chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName
                            drawHorizontalBarChart("Bars1", "#bar_chart_2 .chart", "null", "colorScale10", "${urlsView}", false, false, "null");//dataSet
                        </script> 
                        </td>
                    </tr>
                </table>            
                <br/>         
            </div>
            </c:otherwise>
     </c:choose>
     
     
        </div>
               
    </body>
</html>
