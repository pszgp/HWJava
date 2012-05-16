<%@include file="include.jsp" %>

<!--Selected device-->
<h3><p id="deviceDataUsage">Device: ${deviceIp}</p></h3>
            
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
            //function drawAxis(deviceIp){
                var m = [80, 80, 80, 80],
                    w = 960 - m[1] - m[3],
                    h = 500 - m[0] - m[2],
                    //parse = d3.time.format("%d %B %Y %H:%M:%S").parse;
                    
                    //14 may 2012
                    //parse = d3.time.format("%Y-%m-%d %H:%M:%S").parse;//of for Flows__.csv
                    parse = d3.time.format("%Y-%m-%d").parse;//ok for HWStatsUsageFlows_sql_.csv
                    
                    //parse = d3.time.format("%Y-%m-%dT%H:%M:%SZ").parse;
                    //parse = d3.time.format("%Y-%m-%d %H:%M:%S").parse;
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
                    .x(function(d) { return x(d.last); })//d.date
                    .y0(h)
                    .y1(function(d) { return y(d.nbytes); });
                                
                // A line generator, for the dark stroke.
                var line = d3.svg.line()
                    .interpolate("monotone")
                    .x(function(d) { return x(d.ipdate); })//d.date, //d.last
                    .y(function(d) { return y(d.bytes); });//d.nbytes                     

                //d3.csv("js/readme.csv", function(data) {
                //d3.csv("js/Flows__fragment.csv", function(data) {
                //14 may 2012: use the days consume instead of hours
                d3.csv("js/HWStatsUsageFlows_sql_.csv", function (data) { 
                //d3.csv("js/hwdatatestmock.csv", function(data) {
                // Filter to one device: 10.2.0.1
                var values = data;
                var deviceIp = "${deviceIp}";
                var monthId = "${monthId}";
                if (deviceIp!=null)
                    values = data.filter(function(d) {                
                        //return d.daddr == deviceIp;//"10.2.0.1";
                        var date = d.ipdate;//d.last
                        date = date.substring(date.indexOf(" ")+1);
                        var deviceMonth = date.split("-")[1];
                        deviceMonth=deviceMonth-1;
                        return ((d.ipaddr == deviceIp) && (deviceMonth == monthId));
                    });
                    
                 //15 may 2012: set the values for the graph (non null)
                // Parse dates and numbers. We assume values are sorted by date.
                values.forEach(function(d) {
                    var date = d.ipdate;//d.last
                    date = date.substring(date.indexOf(" ")+1);
                    
                    //to avoid null values set the non used values to default:
                    d.date = date;
                    d.ipaddr = deviceIp;
                    d.last = date;
                    
                    //set the values used for the graph
                    d.ipdate = parse(date);//set the date
                    d.bytes =+ d.bytes;
                    d.bytes = d.bytes / (1024*1024);//set the usage (in Mb)
                });

                //alert(values);
                for (i=0;i<values.length;i++)
                {
                    var value = values[i];
                    //alert(value);
                    //for (key in value)
                    //    alert(key+"="+value[key]);
                    //alert(value.date+" "+value.ipdate+" "+value.bytes+" "+value.ipaddr);  
                    //exit;
                }
                // Compute the minimum and maximum date, and the maximum data usage.
                x.domain([values[0].ipdate, values[values.length - 1].ipdate]);
                y.domain([0, d3.max(values, function(d) { return d.bytes; })]).nice();
                // Add an SVG element with the desired dimensions and margin.
                //document.getElementById("chart").innerHTML = "";
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
                    .text(values[0].ipaddr);//device                                                  
                   
                // On click, update the x-axis.
                svg.on("click", function() {
                    var n = values.length - 1,
                        i = Math.floor(Math.random() * n / 2),
                        j = i + Math.floor(Math.random() * n / 2) + 1;
                    x.domain([values[i].ipdate, values[j].ipdate]);//.last
                    var t = svg.transition().duration(750);
                    t.select(".x.axis").call(xAxis);
                    t.select(".area").attr("d", area(values));
                    t.select(".line").attr("d", line(values));
                });
                });
           //}
            </script>  
            
            <!--script>drawAxis("${deviceDataUsage}");</script-->     
            <div id="chart" style="border: 1pt; border-color:#64A0DE;">
                    <div id="footer">
                        <h3>
                            <div class="title">Device data usage</div>
                            <div class="legend">X axis: days<br/>Y axis: data usage</div>
                            <div class="times"></div>
                            <div class="hint">Click to zoom</div>
                        </h3>
                    </div>
                <h3><a class="a_header" href="device.htm?ip=${deviceIp}&month=${month}">Switch to table view</a></h3>
            </div>
                     
        </div>
    </body>
</html>             