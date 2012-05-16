<%@include file="header.jsp" %>

<script>
    function open_menu(menu){
        var element = document.getElementById( menu + "_details");
        var text = "<table>";
        if (menu=='users_menu')
            data = "${users}";
        if (data!=null)
            if (data.length>0)
                {
                    data = data.substring(1, data.length-1);
                    data = data.split(",");
                }
        for (var i=0; i < data.length; i++)
        {            
            text += "<tr><td class=<table><tr><td class=\"user\" "+
                    "onclick=\"showUserData('" + data[i] +"');\">"+data[i]+"</td></tr>";
        }
        text += "</table>";
        
        if(menu=='users_menu'){            
            element.innerHTML = text;  
        }     
        if(menu=='devices_menu'){
            element.innerHTML = text;             
        }            
        document.getElementById("open_"+menu).innerHTML = "-";
        document.getElementById("open_"+menu).setAttribute("onclick", "close_menu('"+menu+"')");
    }
    function close_menu(menu){
        document.getElementById(menu+"_details").innerHTML = "";
        document.getElementById("open_"+menu).innerHTML = "+";
        document.getElementById("open_"+menu).setAttribute("onclick", "open_menu('"+menu+"')");
    }
    
</script>
        <div id="users_left_menu">
            <p class="menu_title" id="users_menu">
                <img src="images/users.png" alt=""/>
                <open_menu id="open_users_menu" onclick="open_menu('users_menu');"> + </open_menu>
                Users
            </p>
            <p id="users_menu_details"/>  
             <p class="menu_title" id="devices_menu">
                <img src="images/device.png" alt=""/>
                <open_menu id="open_devices_menu" onclick="open_menu('devices_menu');"> + </open_menu>
                Devices
            </p>
            <p id="devices_menu_details"/>
            </p>      
        </div>
        
        <!--script language="javascrip">
            d3.csv("http://mbostock.github.com/d3/ex/population.csv", function(data) {
                // Convert strings to numbers.
                data.forEach(function(d) {
                    d.people = +d.people;
                    d.year = +d.year;
                    d.age = +d.age;
            });
            // Nest by year then birthyear.
            data = d3.nest()
                .key(function(d) { return d.year; })
                .key(function(d) { return d.year - d.age; })
                .rollup(function(v) { return v.map(function(d) { return d.people; }); })
                .map(data);

        </script-->       
        
        <br/>
        
         <!-- calendar -->
        <!--div id="menuLeft" style="width: 190px; height: 50px; position: absolute; left: 10px; top: 150px; background-color:#64A0DE; color: black">
           <h2>Users:</h2-->
           <script type="text/javascript" language="javascript">
                function showUserData(userName){
                    //var text = document.createElement("p");                        
                    var textUserData = "<table>";
                    textUserData+="test...";
                    textUserData+="</table>";                        
                    var userData = document.getElementById("userDataUsage");
                    //userData.appendChild(text);
                    userData.innerHTML = textUserData; 
                }

            </script>

            <!--c:forEach var="user" items="${users}">
                <p onclick="showUserDataInfo(${user}, ${devices});" onmousemove="style: text-decoration='underline'">
                <h3 background-color="#64A0DE" onmouseover="style='text-decoration: underline';">
                    ${user}
                </h3>
                </p>                
            <!--/c:forEach-->
               
            <!--User data usage:-->
            <div id="userDataUsage"/>                 
        <!--/div-->
            
        <div id="users_content">
            <h2>Current year: <y id="currentYear"></y></h2>
            <script language="javascript">
                document.getElementById("currentYear").innerHTML = new Date().getFullYear();                
            </script>
            
            <!--Total bandwidth usage for users:<br/>
            <!--table style="border:2px; border-color: blue;">
                <tr><!--c:forEach var="type" items="${types}">
                        <th>${type}</th>
                    <!--/c:forEach>
                </tr>
                <!--c:forEach var="item" items="${items}">
                    <tr>    
                        <!--c:forEach var="itemRow" items="${item}">
                            <td>${itemRow}</td>
                        <!--/c:forEach>                        
                    </tr>
                <!--/c:forEach>          
            </table>            
            <br/-->
            
            <h3>Total bandwidth usage per devices and users:</h3><br/>    
            <table border="1">
                <tr><th>User</th><th>Device Ip</th><th>Device Name</th><th>Device Type</th><th>Usage(Mb)</th><th>Allowance (Mb)</th></tr>
                <c:forEach var="device" items="${devices}">
                    <!--${device}<br/-->
                    <tr><td>${device.user}</td><td>${device.ip}</td><td>${device.name}</td><td>${device.type}</td>
                          <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${device.totalUsage/(1024*1024)}"/><!--${device.nbytes}--></td>
                              <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${device.allowance/(1024*1024)}"/></td>
                    </tr>
                </c:forEach>    
            </table>
            <br/>
            
            ${flows}<!-- test only -->
            
            <h2>Current month:</h2> 
            <!--script type="text/javascript">                
                var currentMonth = 1;
                var monthNames = ["null", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", 
                                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
                function nav_cal(index){
                    var currentMonthName = document.getElementById("currentMonth").innerHTML;
                    for (i=0;i<monthNames.length;i++)
                        if (monthNames == currentMonthName)
                            currentMonth = i;
                    var realMonth = new Date().getMonth();
                    if ((currentMonth <= realMonth) && (index == 1))
                        currentMonth++;
                    else if ((currentMonth > 1) && (index == -1))
                        currentMonth--;
                    var month = monthNames[currentMonth];
                    document.getElementById("currentMonth").innerHTML = month;                    
                }
            </script-->
            
            <form id="aform">
                <select id="menu_calendar" size="1">
                    <option value="JANUARY" selected="selected">JANUARY</option>
                    <option value="FEBRUARY">FEBRUARY</option>
                    <option value="MARCH">MARCH</option>
                    <option value="APRIL">APRIL</option>
                    <option value="MAY">MAY</option>
                    <option value="JUNE">JUNE</option>
                    <option value="JULY">JULY</option>
                    <option value="AUGUST">AUGUST</option>
                    <option value="SEPTEMBER">SEPTEMBER</option>
                    <option value="OCTOBER">OCTOBER</option>
                    <option value="NOVEMBER">NOVEMBER</option>
                    <option value="DECEMBER">DECEMBER</option>
                </select>
            </form>

            <script type="text/javascript">

            var selectmenu=document.getElementById("menu_calendar")
            selectmenu.onchange=function(){ 
                var chosenoption=this.options[this.selectedIndex] 
                if (chosenoption.value!="nothing"){
                    alert("show details of the month " + chosenoption.value);
                    showDetailsMonth(chosenoption.value);
                    }
            }
            function showDetailsMonth(month)
            {
                //...                
            }

            </script>
            <!--p style="font-size: 18pt; width: 190px;">
                <b><table><tr><td>
                        <nav_cal onclick="nav_cal(-1);" style="font-size: 20px;"><b>&lt;</b></nav_cal>
                        </td>
                    <td width="170px" align="center" style="font-size: 16px;">
                    <month id="currentMonth">
                        <script>document.getElementById("currentMonth").innerHTML = monthNames[new Date().getMonth()];</script></month></td>
                    <td>
                        <nav_cal onclick="nav_cal(1);" style="font-size: 20px;"><b>&gt;</b></nav_cal></td>
            </tr></table></b>
            </p-->
                                             
            
            <script src="http://mbostock.github.com/d3/d3.v2.js"></script>
            <!--script>
                     // This example draws horizontal bar charts...
            // Created by Frank Guerino : "http://www.guerino.net"
            // Data Used for this example...
            var dataSet1 = [{legendLabel: "Data Usage: 2012 APRIL", magnitude: 54.45, link: ""},
            {legendLabel: "Data Usage: 2012 MAY", magnitude: 21.1, link: ""}];

            function drawHorizontalBarChart(chartID, dataSet, selectString, colors) {
                
                var canvasWidth = 700;
                var barsWidthTotal = 300
                var barHeight = 20;
                var barsHeightTotal = barHeight * dataSet.length;
                //var canvasHeight = 200;
                var canvasHeight = dataSet.length * barHeight + 10; // +10 puts a little space at bottom.
                var legendOffset = barHeight/2;
                var legendBulletOffset = 30;
                var legendTextOffset = 20;

                var x = d3.scale.linear().domain([0, d3.max(dataSet, function(d) { return d.magnitude; })]).rangeRound([0, barsWidthTotal]);
                var y = d3.scale.linear().domain([0, dataSet.length]).range([0, barsHeightTotal]);

                // Color Scale Handling...
                var colorScale = d3.scale.category10();
                switch (colors)
                {
                default:
                    colorScale = d3.scale.category10();
                };

                var synchronizedMouseOver = function() {
                var bar = d3.select(this);
                var indexValue = bar.attr("index_value");

                var barSelector = "." + "bars-" + chartID + "-bar-" + indexValue;
                var selectedBar = d3.selectAll(barSelector);
                selectedBar.style("fill", "Blue");

                var bulletSelector = "." + "bars-" + chartID + "-legendBullet-" + indexValue;
                var selectedLegendBullet = d3.selectAll(bulletSelector);
                selectedLegendBullet.style("fill", "Blue");

                var textSelector = "." + "bars-" + chartID + "-legendText-" + indexValue;
                var selectedLegendText = d3.selectAll(textSelector);
                selectedLegendText.style("fill", "Blue");
                };

                var synchronizedMouseOut = function() {
                var bar = d3.select(this);
                var indexValue = bar.attr("index_value");

                var barSelector = "." + "bars-" + chartID + "-bar-" + indexValue;
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);

                var bulletSelector = "." + "bars-" + chartID + "-legendBullet-" + indexValue;
                var selectedLegendBullet = d3.selectAll(bulletSelector);
                var colorValue = selectedLegendBullet.attr("color_value");
                selectedLegendBullet.style("fill", colorValue);

                var textSelector = "." + "bars-" + chartID + "-legendText-" + indexValue;
                var selectedLegendText = d3.selectAll(textSelector);
                selectedLegendText.style("fill", "Maroon");
                };

            // Create the svg drawing canvas...
            var canvas = d3.select(selectString)
                .append("svg:svg")
                .attr("width", canvasWidth)
                .attr("height", canvasHeight);

            // Draw individual hyper text enabled bars...
            canvas.selectAll("rect")
                .data(dataSet)
                .enter()
                .append("svg:rect")
                    .attr("x", 0) // Right to left
                    .attr("y", function(d, i) { return y(i); })
                    .attr("height", barHeight)
                    .on('mouseover', synchronizedMouseOver)
                    .on("mouseout", synchronizedMouseOut)
                    .style("fill", "White" )
                    .style("stroke", "White" )
                    .attr("width", function(d) { return x(d.magnitude); })
                    .style("fill", function(d, i) { colorVal = colorScale(i); return colorVal; } )
                    .attr("index_value", function(d, i) { return "index-" + i; })
                    .attr("class", function(d, i) { return "bars-" + chartID + "-bar-index-" + i; })
                    .attr("color_value", function(d, i) { return colorScale(i); }) 
                    .style("stroke", "white"); 

            // Create text values that go at end of each bar...
            canvas.selectAll("text")
                .data(dataSet) // Bind dataSet to text elements
                .enter().append("svg:text") // Append text elements
                .attr("x", x)
                .attr("y", function(d, i) { return y(i); })
                .attr("dx", function(d) { return x(d.magnitude) - 5; })
                .attr("dy", barHeight-5) 
                .attr("text-anchor", "end") 
                .text(function(d) { return d.magnitude;})
                .attr("fill", "White");

            // Create hyper linked text at right that acts as label key...
            canvas.selectAll("a.legend_link")
                .data(dataSet) // Instruct to bind dataSet to text elements
                .enter()
                    .append("text")
                    .attr("text-anchor", "center")
                    .attr("x", barsWidthTotal + legendBulletOffset + legendTextOffset)
                    .attr("y", function(d, i) { return legendOffset + i*barHeight; } )
                    .attr("dx", 0)
                    .attr("dy", "5px") 
                    .text(function(d) { return d.legendLabel;})
                    .style("fill", "Black")
                        .on('mouseover', synchronizedMouseOver)
                    .on("mouseout", synchronizedMouseOut)
                    .attr("index_value", function(d, i) { return "index-" + i; })
                    .attr("class", function(d, i) { return "bars-" + chartID + "-legendText-index-" + i; });

            };
            </script>
            <script type="text/javascript">
                drawHorizontalBarChart("Bars1", dataSet1, "#bar_chart_1 .chart", "colorScale10");
            </script>
            <div class="div_RootBody" id="bar_chart_1">
                <h3 class="h3_Body">Bar Chart Number 1</h3>
                <div class="chart"></div>
            </div>
            <br/-->
            
            Bandwidth usage per device: <br/>
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
            
            <!--table border="1">
                <tr><th>Device</th><th>Date</th><th>Date Usage (bytes)</th><th>Data Usage (Mb)</th></tr>
                <!--c:forEach var="monthDeviceMap" items="${devicesPerMonths}">
                    Month: ${monthDeviceMap.key}
                    <!--c:forEach var="device" items="${monthDeviceMap.value}">
                        <!--c:forEach var="usage" items="${device.totalUsageSplitDetailsFlows}">
                            <!--c:set var="map" value="${usage}"><!--/c:set>
                                <!--c:forEach var="mapItem" items="${map}">
                                    <tr>
                                        <td>${device.ip}</td>
                                        <td>${mapItem.key}</td><!-- date -->
                                        <!--td>${mapItem.value}</td> <!-- usage -->
                                        <!--td style="text-align: right">
                                            <!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${mapItem.value/(1024*1024)}"/>
                                        </td>
                                    </tr>
                                <!--/c:forEach>
                        <!--/c:forEach>
                    <!--/c:forEach>  
                <!--/c:forEach>                      
            </table>
            <br/-->
            
            <!--table border="1">
                <tr><th>Device</th><th>Date</th><th>Date Usage (bytes)</th><th>Data Usage (Mb)</th></tr>
                <!--c:forEach var="device" items="${devices}">
                    <!--c:forEach var="usage" items="${device.totalUsageSplitDetailsFlows}">
                        <!--c:set var="map" value="${usage}"><!--/c:set>
                            <!--c:forEach var="mapItem" items="${map}">
                                <tr>
                                    <td>${device.ip}</td>
                                    <td>${mapItem.key}</td><!-- date -->
                                    <!--td>${mapItem.value}</td> <!-- usage -->
                                    <!--td style="text-align: right">
                                        <!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                          value="${mapItem.value/(1024*1024)}"/>
                                    </td>
                                </tr>
                            <!--/c:forEach>
                    <!--/c:forEach>
                <!--/c:forEach>    
            </table>
            <br/-->
            
            <input type="button" value="Reload" onclick="window.location.reload()"/>
        </div>
    </body>
</html>
