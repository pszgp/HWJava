<%-- 
    Document   : test
    Created on : 17-Apr-2012, 14:24:06
    Author     : pszgp
--%>

Example from: https://gist.github.com/1166403#file_index.html
<!-- axis component d3 sample from: https://gist.github.com/1166403#file_index.html -->
<style type="text/css">
    path.line {
        fill: none;
        stroke: #666;
        stroke-width: 1.5px;
    }
    path.area {
        fill: #e7e7e7;
    }
    .axis {
        shape-rendering: crispEdges;
    }
    .x.axis line {
        stroke: #fff;
    }
    .x.axis .minor {
        stroke-opacity: .5;
    }
    .x.axis path {
        display: none;
    }
    .y.axis line, .y.axis path {
        fill: none;
        stroke: #000;
    }
</style>


<!-- axis component d3 sample from: https://gist.github.com/1166403#file_index.html -->
            <script type="text/javascript">
            function drawAxis(deviceIp){
                alert("draw axis for device: " + deviceIp);
                var m = [80, 80, 80, 80],
                    w = 960 - m[1] - m[3],
                    h = 500 - m[0] - m[2],
                    //parse = d3.time.format("%d %B %Y %H:%M:%S").parse;
                    parse = d3.time.format("%Y-%m-%d %H:%M:%S").parse;
                        ////d3.time.format("%Y-%m-%d").parse;//("%b %Y").parse;
                    //format time: %a %b %e %H:%M:%S %Y"

                // Scales and axes. Note the inverted domain for the y-scale: bigger is up!
                var x = d3.time.scale().range([0, w]),
                    y = d3.scale.linear().range([h, 0]),
                    xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true),
                    yAxis = d3.svg.axis().scale(y).ticks(4).orient("right");

                // An area generator, for the light fill.
                var area = d3.svg.area()
                    .interpolate("monotone")
                    .x(function(d) { return x(d.date); })
                    .y0(h)
                    .y1(function(d) { return y(d.nbytes); });

                // A line generator, for the dark stroke.
                var line = d3.svg.line()
                    .interpolate("monotone")
                    .x(function(d) { return x(d.date); })
                    .y(function(d) { return y(d.nbytes); });

                //d3.csv("js/readme.csv", function(data) {
                //d3.csv("js/Flows_.csv", function(data) {
                d3.csv("js/hwdatatestmock.csv", function(data) {

                // Filter to one device: 10.2.0.1
                var values = data;
                if (deviceIp!=null)
                    values = data.filter(function(d) {
                        return d.device == deviceIp;//"10.2.0.1";
                    });

                // Parse dates and numbers. We assume values are sorted by date.
                values.forEach(function(d) {
                    //alert(d.date);
                    d.date = parse(d.date);
                    //alert(d.date);
                    //exit;
                    d.nbytes = +d.nbytes;
                    //d.price = +d.price;
                });

                // Compute the minimum and maximum date, and the maximum data usage.
                x.domain([values[0].date, values[values.length - 1].date]);
                y.domain([0, d3.max(values, function(d) { return d.nbytes; })]).nice();

                // Add an SVG element with the desired dimensions and margin.
                document.getElementById("chart").innerHTML = "";
                var svg = d3.select("#chart").append("svg:svg")//d3.select("body").append("svg:svg")
                    .attr("width", w + m[1] + m[3])
                    .attr("height", h + m[0] + m[2])
                    .append("svg:g")
                    .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

                // Add the clip path.
                svg.append("svg:clipPath")
                    .attr("id", "clip")
                    .append("svg:rect")
                    .attr("width", w)
                    .attr("height", h);

                // Add the area path.
                svg.append("svg:path")
                    .attr("class", "area")
                    .attr("clip-path", "url(#clip)")
                    .attr("d", area(values));

                // Add the x-axis.
                svg.append("svg:g")
                    .attr("class", "x axis")
                    .attr("transform", "translate(0," + h + ")")
                    .call(xAxis);

                // Add the y-axis.
                svg.append("svg:g")
                    .attr("class", "y axis")
                    .attr("transform", "translate(" + w + ",0)")
                    .call(yAxis);

                // Add the line path.
                svg.append("svg:path")
                    .attr("class", "line")
                    .attr("clip-path", "url(#clip)")
                    .attr("d", line(values));

                // Add a small label for the symbol name.
                svg.append("svg:text")
                    .attr("x", w - 6)
                    .attr("y", h - 6)
                    .attr("text-anchor", "end")
                    .text(values[0].device);

                // On click, update the x-axis.
                svg.on("click", function() {
                    var n = values.length - 1,
                        i = Math.floor(Math.random() * n / 2),
                        j = i + Math.floor(Math.random() * n / 2) + 1;
                    x.domain([values[i].date, values[j].date]);
                    var t = svg.transition().duration(750);
                    t.select(".x.axis").call(xAxis);
                    t.select(".area").attr("d", area(values));
                    t.select(".line").attr("d", line(values));
                });
                });
            }
            </script>  
            
                 
            <div id="chart">
                    <div id="footer">
                        <div class="title">Device data usage</div>
                        <div class="legend"></div>
                        <div class="times"></div>
                        <div class="hint">Click to toggle zoom</div>
                    </div>
                <h3><a class="a_header" href="device.htm?ip=${deviceDataUsage}">Switch to table view</a></h3>
            </div>
                     
        </div>
    <script>drawAxis("${deviceDataUsage}");</script> 
    
    <script src="http://mbostock.github.com/d3/d3.v2.js"></script>
        <style>
			/* tell the SVG path to be a thin blue line without any area fill */
			path {
				stroke: steelblue;
				stroke-width: 1;
				fill: none;
			}
        </style>
        
       <div id="graph" class="aGraph" style="position:absolute;top:0px;left:0; float:left; width:400px; height:60px;"></div>


	<script>
            //source: http://benjchristensen.com/2011/08/08/simple-sparkline-using-svg-path-and-d3-js/
            //
		// create an SVG element inside the #graph div that fills 100% of the div
		var graph = d3.select("#graph").append("svg:svg").attr("width", "100%").attr("height", "100%");

		// create a simple data array that we'll plot with a line (this array represents only the Y values, X will just be the index location)
		var data = //[3, 6, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9, 3, 6, 3, 6, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9, 3, 6, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9];
                    [12, 3.4, 56, 7.8, 0.8, 0.5, 9, 12, 1, 24, 10, 123];
		// X scale will fit values from 0-10 within pixels 0-100
		var x = d3.scale.linear().domain([0, 10]).range([0, 150]);
		// Y scale will fit values from 0-10 within pixels 0-100
		var y = d3.scale.linear().domain([0, 10]).range([0, 30]);

		// create a line object that represents the SVN line we're creating
		var line = d3.svg.line()
			// assign the X function to plot our line as we wish
			.x(function(d,i) { 
				// verbose logging to show what's actually being done
				console.log('Plotting X value for data point: ' + d + ' using index: ' + i + ' to be at: ' + x(i) + ' using our xScale.');
				// return the X coordinate where we want to plot this datapoint
				return x(i); 
			})
			.y(function(d) { 
				// verbose logging to show what's actually being done
				console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d) + " using our yScale.");
				// return the Y coordinate where we want to plot this datapoint
				return y(d); 
			})

			// display the line by appending an svg:path element with the data line we created above
			graph.append("svg:path").attr("d", line(data));

	</script>

        
        <script>        
               
      // This example draws horizontal bar charts...
      // Created by Frank Guerino : "http://www.guerino.net"
    // Data Used for this example...
    var dataSet1 = [{legendLabel: "Data Usage: 2012 APRIL", magnitude: 54.45},
      {legendLabel: "Data Usage: 2012 MAY", magnitude: 21.1}];

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

    <style type="text/css">

    </style>


    <!--[if gt IE 7]>
      <style>body { overflow-y:scroll; } </style>
    <![endif]-->


    <!-- enable cross-resources: XMLHTTPRequest?? 
        http://www.leggetter.co.uk/2010/03/12/making-cross-domain-javascript-requests-using-xmlhttprequest-or-xdomainrequest.html-->
    <script type="text/javascript">
 
        var isIE8 = window.XDomainRequest ? true : false;
        var invocation = createCrossDomainRequest();
        var url = 'http://www.phobos7.co.uk/research/xss/simple.php';        
 
        function createCrossDomainRequest(url, handler)
        {
            var request;
            if (isIE8)
            {
                request = new window.XDomainRequest();
            }
            else
            {
                request = new XMLHttpRequest();
            }
            return request;
        }
 
        function callOtherDomain()
        {
            if (invocation)
            {
                if(isIE8)
                {
                    invocation.onload = outputResult;
                    invocation.open("GET", url, true);
                    invocation.send();
                }
                else
                {
                    invocation.open('GET', url, true);
                    invocation.onreadystatechange = handler;
                    invocation.send();
                }
            }
            else
            {
                var text = "No Invocation TookPlace At All";
                var textNode = document.createTextNode(text);
                var textDiv = document.getElementById("textDiv");
                textDiv.appendChild(textNode);
            }
        }
 
        function handler(evtXHR)
        {
            if (invocation.readyState == 4)
            {
                if (invocation.status == 200)
                {
                    outputResult();
                }
                else
                {
                    alert("Invocation Errors Occured");
                }
            }
        }
 
        function outputResult()
        {
            var response = invocation.responseText;
            var textDiv = document.getElementById("textDiv");
            textDiv.innerHTML += response;
        }
    </script>
    
    <script type="text/javascript" language="javascript">
     <!-- samples... -->
    var data = [4, 8, 15, 16, 23, 42];
     var chart = d3.select("body").append("div").attr("class", "chart");
     chart.selectAll("div").data(data).enter().append("div")
            .style("width", function(d) { return d * 10 + "px"; })
            .text(function(d) { return d; });
            
     var h = 1000
     var vis = d3.select("body") //http://www.janwillemtulp.com/2011/03/20/tutorial-introduction-to-d3/
        .append("svg:svg")
        .attr("width", screen.width)
        .attr("height", screen.innerHeight)       
    
     /*vis.append("rect")
     .attr("x", 0)
     .attr("y", 25)
     .attr("width", 80)
     .attr("height", 20);  */
     
      //http://mbostock.github.com/d3/tutorial/protovis.html
      var l = vis.append("pv.Label")
        .data([4, 8, 15, 16, 23, 42])
        .text(String);

        // Update?
        l = vis.selectAll("text")
            .data([4, 8, 15, 16, 23, 42])
            .text(String);
        // Enter?
        l.enter().append("text").text(String);
        // Exit?
        l.exit().remove();
     
     <!-- samples end -->           
    </script> 
    <body>
        <!--h1>Hello World! - Test page</h1-->
        <!--Urls from hwdb: <br/>--><!--${urls}-->
        
         <div class="div_RootBody" id="bar_chart_1">
              <!--h3 class="h3_Body">Bar Chart Number 1</h3-->
              <div class="chart"></div>
      </div>

      <script type="text/javascript">
        drawHorizontalBarChart("Bars1", dataSet1, "#bar_chart_1 .chart", "colorScale10");
      </script>

      <div id="graph" class="aGraph" style="position:absolute;top:0px;left:0; float:left; width:300px; height:60px;"></div>


    </div>
  </body>    
</html>
