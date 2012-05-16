<%-- 
    Document   : user
    Created on : 06-May-2012, 17:01:54, modified 10 may 2012
    Author     : pszgp
--%>
<%@include file="header.jsp" %>        
<%@include file="leftmenu.jsp" %>

     <div id="users_content">  

            <!--Selected user-->
            <h3><p id="userDataUsage">User: ${user}</p></h3>   
            
            <h3>Total bandwidth usage for user: ${user}</h3>    
            <table><!-- border="1"-->
                <tr style="background-color: #64A0DE; font-size: 14pt;">
                    <th>Device Name</th>
                    <th>Device Type</th>
                    <th>Device Ip</th>
                    <th>Data Usage(Mb)</th>
                    <th>Allowance (Mb)</th>
                </tr>
                <c:forEach var="device" items="${devices}">
                    <c:if test="${user == device.user}">
                        <tr><td>${device.name}</td><td>${device.type}</td><td>${device.ip}</td>
                            <td><!--c:forEach var="deviceMonths" items = "${device.nbytesMonths}">
                                    ${deviceMonths.key}=<!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${deviceMonths.value/(1024*1024)}"/>
                            <!--/c:forEach-->
                            <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${device.nbytesMb}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                                            value="${device.allowance/(1024*1024)}"/></td>
                        </tr>
                    </c:if>
                </c:forEach>    
            </table>
            <br/>
            
            <h3>Monthly devices' usage for the user: ${user}</h3>    
            <h4><c:forEach var="device" items="${devices}">
                    <c:if test="${user == device.user}">
                        <a href="device.htm?ip=${device.ip}">Device ip: ${device.ip}</a><br/>
                        <c:set var="deviceMonthsUsage" value="${device.nbytesMonths}"/>
                        <table>
                            <c:forEach var="deviceMonths" items = "${device.nbytesMonths}">
                                <tr><!--td>${deviceMonths.key} </td>
                                    <td><!--fmt:formatNumber maxFractionDigits="2" minFractionDigits="2"
                                            value="${deviceMonths.value/(1024*1024)}"/>Mb</td-->
                                </tr>
                            </c:forEach>
                        </table>    
                        <br/>                       
                    </c:if>
            </c:forEach></h4>    
        
       <script>        
               var valuesUsage = "${deviceMonthsUsage}";
               valuesUsage = valuesUsage.substring(1, valuesUsage.length-1);
               valuesUsage = valuesUsage.split(", ");
               var dataSet = new Array (valuesUsage.length);;
               for (i=0;i<valuesUsage.length;i++)
               {
                   var data = valuesUsage[i];
                   var month=data.substring(0, data.indexOf("="));
                   var nbytes = data.substring(data.indexOf("=")+1);
                   nbytes = nbytes / (1024 * 1024);//Mb
                   nbytes = nbytes.toFixed(2);//2 decimals only
                   dataSet[i] = {legendLabel: month, magnitude: nbytes};
               }
      // This example draws horizontal bar charts...
      // sample from : "http://www.guerino.net"
    //var dataSet1 = [{legendLabel: "Data Usage: 2012 APRIL", magnitude: 54.45},
    //  {legendLabel: "Data Usage: 2012 MAY", magnitude: 21.1}];

        var hwColour = "#64A0DE";
        
      function drawHorizontalBarChart(chartID, dataSet, selectString, colors) {
        var canvasWidth = 700;
        var barsWidthTotal = 300;
        var barHeight = 20;
        var barsHeightTotal = barHeight * dataSet.length;
        //var canvasHeight = 200;
        var canvasHeight = dataSet.length * barHeight + 10; // +10 puts a little space at bottom.
        var legendOffset = barHeight/2;
        var legendBulletOffset = 30;
        var legendTextOffset = 40;//20

        var x = d3.scale.linear().domain([0, d3.max(dataSet, function(d) { return d.magnitude; })]).rangeRound([0, barsWidthTotal]);
        var y = d3.scale.linear().domain([0, dataSet.length]).range([0, barsHeightTotal]);

        /*/ Color Scale Handling...
        var colorScale = d3.scale.category10();
        switch (colors)
        {
          default:
            colorScale = d3.scale.category10();
        };*/

        /*var synchronizedMouseOver = function() {
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
        };*/

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
            //.on('mouseover', synchronizedMouseOver)
            //.on("mouseout", synchronizedMouseOut)
            .style("fill", "black" )
            .style("stroke", "black" ) //15 may 2012
            .attr("width", function(d) { return x(d.magnitude/4); })// data usage values are quite big!
            //.style("fill", function(d, i) { colorVal = colorScale(i); return colorVal; } )
            .style("fill", function(d, i) { colorVal = hwColour; return colorVal; } )
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d, i) { return "bars-" + chartID + "-bar-index-" + i; })
            //.attr("color_value", function(d, i) { return colorScale(i); }) 
            .attr("color_value", function(d, i) { return hwColour; }) 
            .style("stroke", "black"); 

      // Create text values that go at end of each bar...
      canvas.selectAll("text")
        .data(dataSet) // Bind dataSet to text elements
        .enter().append("svg:text") // Append text elements
          .attr("x", x)
          .attr("y", function(d, i) { return y(i); })
          .attr("dx", 80) //function(d) { return x(d.magnitude) - 5; }) //15 may 2012
          .attr("dy", barHeight-5) 
          .attr("text-anchor", "end") 
          .text(function(d) { return d.magnitude + " Mb";})
          .attr("fill", "black");

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
		//.on('mouseover', synchronizedMouseOver)
            //.on("mouseout", synchronizedMouseOut)
              .attr("index_value", function(d, i) { return "index-" + i; })
              .attr("class", function(d, i) { return "bars-" + chartID + "-legendText-index-" + i; });
              

      };

    </script>

    <style type="text/css">

    </style>
     <div class="div_RootBody" id="bar_chart_1">
              <!--h3 class="h3_Body">Bar Chart Number 1</h3-->
              <div class="chart"></div>
      </div>

      <script type="text/javascript">
        drawHorizontalBarChart("Bars1", dataSet, "#bar_chart_1 .chart", "colorScale10");
      </script>

      <div id="graph" class="aGraph" style="position:absolute;top:0px;left:0; float:left; width:300px; height:60px;"></div>

            
                   
        </div>
    </body>
</html>
