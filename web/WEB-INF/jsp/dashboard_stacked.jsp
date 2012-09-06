<%-- 
    Document   : dashboard_stacked
    Created on : 12-Jul-2012, 14:06:19
    Author     : ... taken from http://bl.ocks.org/2940908
--%>

<!--%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Simple Stack</title!-->
        <script src="http://d3js.org/d3.v2.js"></script>     
        <style>
            svg {
                border: solid 1px #ccc;
                font: 10px sans-serif;
                shape-rendering: crispEdges;
            }
        </style>             
    </head>
    <body>

        <div id="viz"></div>
            
        <script type="text/javascript">
        function drawBarCharts(data){
            //alert(data);
            //var dataMonths = "${dataMonthsDevices}";
            var devicesIps = "${devicesIps}";
            //alert(devicesIps);
            devicesIps = devicesIps.substring(1, devicesIps.length-1);
            devicesIps = devicesIps.split(", ");
            //alert(dataMonthsDevices);
            
            var w = 960,
            h = 400;//500;
            
            /*var matrix = [
                ["1", 1500, 2868],
                ["2", 2000, 6171],
                ["3", 1000, 8045],
                ["4", 5000, 6907],
                ["5", 3000, 5000]
            ];*/
            
            var matrix = [[]];
            
            data = data.split("}, ");
            for (var i=0;i<data.length;i++)
            {
                var monthData = data[i];
                if (monthData.indexOf("{")==0)
                    monthData = monthData.substring(1, monthData.length);
                
                var month = monthData.substring(0, monthData.indexOf("={"));
                //alert("month" + month);
                
                var devicesData = monthData.substring(monthData.indexOf("={")+2, monthData.length);
                if (devicesData.indexOf("}}")>0)
                    devicesData = devicesData.substring(0, devicesData.indexOf("}}"));
                
                devicesDataArray = devicesData.split(", ");
                //alert(devicesDataArray);
                var devicesUsage = {};
                for (var j=0;j<devicesDataArray.length;j++)
                {
                    var arrayData = devicesDataArray[j];
                    var device = arrayData.substring(0, arrayData.indexOf("="));        
                    var usage = arrayData.substring(arrayData.indexOf("=")+1, arrayData.length);
                    //alert(usage);
                    usage = +usage;
                    //usage/=(1024*1024);//(1024*1024);
                    usage = Math.floor(usage);
                    devicesUsage[device] = usage;
                    //exit;
                    //alert(device+" "+month+" "+usage);
                }
                
                var usage = [];
                usage[0] = month;//"\""+month+"\"";
                for (var j=0;j<devicesIps.length;j++)
                {
                    usage[j+1] = devicesUsage[devicesIps[j]];
                }
                
                //error for less than 2 data columns 
                //  => copy the first devices data in another column
                //if (devicesIps.length == 1)
                //    usage[2] = devicesUsage[devicesIps[0]];
              
                matrix[i] = usage;
                
                var months = ["JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
                //alert(months.indexOf("JULY"));
             
            }
           
            var nr = matrix.length;
            w = (nr+2) * 50;
            
            var columns = devicesIps;//["c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8"];//devicesIps;
            var remapped = columns.map(function(dat, i){//devicesIps.map(function(dat,i){
                //["c1","c2"].map(function(dat,i){
                return matrix.map(function(d,ii){
                    return {x: ii, y: d[i+1], deviceIp: devicesIps[i] };
                })
            });
            
            //alert(remapped);
                        
            var stacked = d3.layout.stack()(remapped);
            //alert(stacked);
            
            //var height = d3.max(stacked[stacked.length - 1], function(d) { return (d.y/2);});//(d.y/5); });
            
            // create the canvas
            var svg = d3.select("#viz").append("svg:svg")
            .attr("class", "chart")
            .attr("width", w)
            .attr("height", h)//height )
            .append("svg:g")
            .attr("transform", "translate(10,470)");

            x = d3.scale.ordinal().rangeRoundBands([0, w-50]);
            y = d3.scale.linear().range([0, h-50]);
            //z = d3.scale.ordinal().range(["darkblue", "blue", "lightblue", "gray", "darkgray"])
            z = d3.scale.category20c();

            console.log("RAW MATRIX---------------------------");
	    // columns: month,device1,device2,device3 etc.
                    
            x.domain(stacked[0].map(function(d) {return d.x; }));
            y.domain([0, d3.max(stacked[stacked.length - 1], function(d) { return (d.y0+d.y); })]);//*3//nbytes
            
            // Add a group for each column.
            var valgroup = svg.selectAll("g.valgroup")
            .data(stacked)
            .enter().append("svg:g")
            .attr("class", "valgroup")
            .style("fill", function(d, i) { return z(i); })
            .style("stroke", function(d, i) { return d3.rgb(z(i)).darker(); });

            // Add a rect for each date.
            var rect = valgroup.selectAll("rect")
            .data(function(d){return d;})
            .enter().append("svg:rect")
            .attr("x", function(d) { return x(d.x); })
            .attr("y", function(d) { return -y(d.y0) - y(d.y); })
            .attr("height", function(d) { return y(d.y); })
            .attr("width", x.rangeBand())
            .append("svg:title")
                .text(function(d) { return devicesIps[d.y]; })//add the device name????
            
            rect.append("svg:text")
            .attr("x", function(d) { return x(d.x); })
            .attr("y", function(d) { return -y(d.y0) - y(d.y); })
            .text(function (d) {
                var value = d.y/(1024*1024*1024);
                value.toFixed(2);
                return d.deviceIp + ": " + value.toFixed(2) + "Gb";
            });
            
            //add text
           /* rect.append("text")
            .attr("y", -6)
            .attr("x", -x(5) / 2)
            .attr("transform", "rotate(180)")
            .attr("text-anchor", "middle")
            .style("fill", "green")
            .text("String");//.append("svg:text")
            //.text(function(d) { return "text"; });;
            
            var text = rect.append("svg:g")
            //.attr("stroke", "white")
            //.attr("stroke-width", 3)
            .attr("fill", "orange");
            
            text.append("svg:text")
            .attr("text-anchor", "middle")
            .attr("dy", "1em")
            .attr("y", h * 7 / 6)
            .text(String);           
            
            text.append("svg:path")
            .style("fill", function(d) { return "green"; })
            .append("svg:title")
            .text(function(d) { return d.x + ": " + d.y; });

            // Add a label to the larger arcs, translated to the arc centroid and rotated.
            text.filter(function(d) { return d.y >= 1000; }).append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .attr("transform", function(d) { return "translate(" + 90 + ")rotate(" + 90 + ")"; })
            .text(function(d) { return d.x; });
            */
            //... try to add text and legend:
            // Add a caption to the graph.
            svg.append("svg:text")
            .attr("dy", ".35em")
            .attr("x", 0)//x.rangeBand()/2)
            .attr("y", 20)
            .attr("text-anchor", "bottom")
            .text(function(d) { return "Monthly usage";})//"Graph: Devices usage per months"; })
            .style("font-size", "14px");
            
         }

         drawBarCharts("${dataMonthsDevices}");
         //${deviceDataDays}
        </script>
    <!--/body>
</html-->
