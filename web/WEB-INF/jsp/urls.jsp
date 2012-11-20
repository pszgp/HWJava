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
        * {
        margin:  0;
        padding: 0;
        }
        div#tagcloud {
        width: 450px;
        }
        ul.tagcloud-list {
        border: 1px solid #000;
        font-size: 100%;
        font-weight: bold;
        font-family: "Arial", "sans-self";
        padding: 2px;
        margin: 10px;
        }
        li.tagcloud-base {
        font-size: 24px;
        display: inline;
        }
        a.tagcloud-anchor {
        text-decoration: none;
        }
        a.tagcloud-ealiest {
        color: #ccc;
        }
        a.tagcloud-earlier {
        color: #99c;
        }
        a.tagcloud-later {
        color: #99f;
        }
        a.tagcloud-latest {
        color: #00f;
        }

     </style>
     
     
     
     <c:choose>
        <c:when test="${url eq null}">
            Zoom tags <img src="images/zoom_in.png" alt="Zoom-in" width="50px" height="40px" onclick="zoom(1);"/>
            <img src="images/zoom_out.png" alt="Zoom-out" width="50px" height="40px" onclick="zoom(-1);"/>
            <input type="hidden" id="zoomed" name="zoomed" value="1"></input>
            <br/>
     
            <script type="text/javascript" src="js/lyokato-javascript-tagcloud-05ffc75/lib/tagcloud.js"></script>
            <script type="text/javascript" src="js/urlstagcloud.js"></script>

            <script type="text/javascript">
                function zoom(index){
                    var zoomed = document.getElementById("zoomed").value;
                    zoomed = +zoomed;
                    zoomed = zoomed+index;
                    if ((zoomed>0)&&(zoomed<=3))
                        document.getElementById("zoomed").value = zoomed;

                    var urlsOccur = "${urlsOccurZoomOne}";
                    if (zoomed == 2)
                        urlsOccur = "${urlsOccurZoomTwo}";
                    if ((zoomed!=1)&&(zoomed!=2))
                        urlsOccur = "${urlsOccur}";

                    setuptc(urlsOccur, 'tagcloud');
                }
            </script> 
            <br/>
            
            <div class ="tabber">
                <div class="tabbertab" title="Tagcloud">                    
                    
                    <%@include file="tagcloudurls.jsp"%>
                    <script type="text/javascript">
                        var urlsOccur = "${urlsOccurView}";
                        setuptc(urlsOccur, 'tagcloud');
                    </script>
                    <div id="tagcloud"></div> 
                    
                    <table style="width: 450px">
                        <tr style="background-color: #64A0DE;""><th>Visited website/domain</th><th style="width: 250px;">Device</th><th>Frequency</th></tr>
                        <c:set var="rowCount" value="0"/>
                        <c:forEach var="device" items="${urlsOccurView}">
                            <c:set var="rowCount" value="${rowCount+1}"/>
                            <c:choose>
                                <c:when test='${(rowCount)%2 eq 0}'><tr style="background-color: #64A0DE;"></c:when>
                                <c:otherwise><tr style=""></c:otherwise>  
                            </c:choose>           
                            <c:forEach var="uri" items="${device.value}">
                                <tr><td><a href="urls.htm?url=${device.key}">${device.key}</a></td>
                                    <td style="width: 250px"><a href="device.htm?ip=${uri.key}">${uri.key}</a></td>
                                    <td>${uri.value}</td></tr>
                            </c:forEach>
                        </c:forEach>    
                    </table>
                </div>
                <div class="tabbertab" title="Table view all urls">
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
                                    <tr><td><a href="device.htm?ip=${device.key}">${device.key}</a></td>
                                        <td style="width: 250px"><a href="urls.htm?url=${uri.key}">${uri.key}</a></td>
                                        <td>${uri.value}</td></tr>
                                </c:forEach>
                            </c:forEach>    
                        </table>
                        <br/>         
                </div>                   
            </div>   
       
        </c:when>
        <c:otherwise> <div class="tabbertab" title="Graph view">      
                    <script type="text/javascript" src="js/device_bars.js"> </script>
                    <h3>Occurrences for the selected url per device:</h3>
                        URL = ${url}
                        <br/>
                        <!--${urlsView}-->
                        <table style="width: 450px">
                            <tr style="background-color: #64A0DE;""><th>Device</th><th>Frequency</th></tr>
                            <c:set var="rowCount" value="0"/>
                            <c:forEach var="device" items="${urlsView}">
                                <c:set var="rowCount" value="${rowCount+1}"/>
                                <c:choose>
                                    <c:when test='${(rowCount)%2 eq 0}'><tr style="background-color: #64A0DE;"></c:when>
                                    <c:otherwise><tr style=""></c:otherwise>  
                                </c:choose>           
                                <tr><td>${device.key}</td></td><td>${device.value}</td></tr>                              
                            </c:forEach>    
                        </table>
                        <br/>        
                        <br/>
                        <table><tr><td height="40px;"></td></tr>
                        <tr><td>
                        <div class="div_RootBody" id="bar_chart_2">
                            <div class="chart"></div>
                        </div>
                        <script type="text/javascript">
                            //chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName
                            //drawHorizontalBarChart("Bars1", "#bar_chart_2 .chart", "null", "colorScale10", "${urlsView}", false, false, "null");//dataSet
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
