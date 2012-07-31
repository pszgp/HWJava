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
            //var dataMonths = "${dataMonthsDevices}";
            var devicesIps = "${devicesIps}";
            
            devicesIps = devicesIps.substring(1, devicesIps.length-1);
            devicesIps = devicesIps.split(", ");
            //alert(dataMonthsDevices);
            
            var w = 960,
            h = 500;
            
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
                
                var devicesData = monthData.substring(monthData.indexOf("={")+2, monthData.length);
                if (devicesData.indexOf("}}")>0)
                    devicesData = devicesData.substring(0, devicesData.indexOf("}}"));
                
                devicesDataArray = devicesData.split(", ");
                var devicesUsage = {};
                for (var j=0;j<devicesDataArray.length;j++)
                {
                    var arrayData = devicesDataArray[j];
                    var device = arrayData.substring(0, arrayData.indexOf("="));        
                    var usage = arrayData.substring(arrayData.indexOf("=")+1, arrayData.length);
                    usage = +usage;
                    usage/=(1024*1024);
                    usage = Math.floor(usage);
                    devicesUsage[device] = usage;
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
                
                /*matrix.sort(function(a,b){
                        var indexA = months.indexOf(a);
                        var indexB = months.indexOf(b);
                        if ((indexA > -1) && (indexB > -1))
                            return indexA > indexB ? 1: (indexA < indexB ? -1 : 0);
                        //if(a.item1 === b.item1){
                        //    return a.item2 > b.item2 ? 1 : a.item2 < b.item2 : -1 : 0;
                    });
                */
            }
            
            for (var m=0;m<matrix.length;m++)
            {
                for (var k=0; k<matrix[m].length;k++)
                {
                    //alert(matrix[m][k]);                    
                }
                //exit;                
            }
           
            var nr = matrix.length;
            w = (nr+2) * 50;
            
            var columns = devicesIps;
            var remapped = devicesIps.map(function(dat,i){
                //["c1","c2"].map(function(dat,i){
                return matrix.map(function(d,ii){
                    return {x: ii, y: d[i+1] };
                })
            });
            //alert(remapped);

            var stacked = d3.layout.stack()(remapped);
            //alert(stacked);
            
            var height = d3.max(stacked[stacked.length - 1], function(d) { return (d.y/5); });
            
            // create canvas
            var svg = d3.select("#viz").append("svg:svg")
            .attr("class", "chart")
            .attr("width", w)
            .attr("height", height )
            .append("svg:g")
            .attr("transform", "translate(10,470)");

            x = d3.scale.ordinal().rangeRoundBands([0, w-50])
            y = d3.scale.linear().range([0, h-50])
            z = d3.scale.ordinal().range(["darkblue", "blue", "lightblue", "gray", "darkgray"])

            console.log("RAW MATRIX---------------------------");
	    // 4 columns: ID,c1,c2,c3
            //columns: ID, deviceIp,hour,day,month,year,nbytes
            /*var matrix = [
                [1, "10.2.0.1, 12, 25, 4, 2012", 0, 2868],
                [2, "10.2.0.17, 11, 12, 5, 2012", 0, 6171],
                [3, "10.2.0.21, 14, 10, 6, 2012", 0, 8045],
                [4, "10.2.0.3, 9, 4, 7, 2012", 0, 6907],
                [5, "10.2.0.15, 15, 11, 7, 2012", 0, 5000]
            ];*/
                    
            x.domain(stacked[0].map(function(d) {return d.x; }));
            //alert(stacked[0]);
            
            //alert(h);
            //y = d3.scale.linear().range([0, h-50]);
            
            y.domain([0, d3.max(stacked[stacked.length - 1], function(d) { return (d.y0+d.y)*3; })]);//nbytes
            
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
            .attr("width", x.rangeBand());
            
            rect.append("svg:text")
            .attr("x", function(d) { return x(d.x); })
            .attr("y", function(d) { return -y(d.y0) - y(d.y); })
            .text("text...");
            
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
            .attr("x", x.rangeBand()/2)
            .attr("y", 20)
            .attr("text-anchor", "bottom")
            .text(function(d) { return "Graph: Devices usage per days"; })
            .style("font-size", "14px");
            
         }

         drawBarCharts("${dataMonthsDevices}");
         ${deviceDataDays}
        </script>
    <!--/body>
</html-->
