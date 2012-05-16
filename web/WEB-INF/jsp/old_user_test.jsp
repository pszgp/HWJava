<%-- 
    Document   : user
    Created on : 06-May-2012, 17:01:54
    Author     : pszgp
--%>

<%@include file="header.jsp" %>
<script>
    function open_menu(menu){
        alert(menu);
        alert("${users}");
        alert("${object}");
        var element = document.getElementById( menu + "_details");
        var text="<table>";
        if (menu=='users_menu')
            data = usersNames;
        alert(data);
        if (data!=null)
            if (data.length>0)
                data = data.substring(1, data.length-2).data.split(",");
        for (i=0;i<data.length;i++) {
            text+="<tr><td class=\"user\">"+data[i]+"</td>";
        };
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

        Users: ${users}<br/>
        Object: ${object}
        <fmt:message key="${users}" var="usersNames"/>

        <div id="users_left_menu">
            <p class="menu_title" id="users_menu">
                <img src="images/users.png" alt=""/>
                <open_menu id="open_users_menu" onclick="open_menu('users_menu');"> + </open_menu>
                Users
            </p>
            <p id="users_menu_details"/>
            <!--table>
                <tr><td class="user">User_1</td>
                </tr><td class="user">User_2</td>
            </table-->    
             <p class="menu_title" id="devices_menu">
                <img src="images/device.png" alt=""/>
                <open_menu id="open_devices_menu" onclick="open_menu('devices_menu', ${devicesNames});"> + </open_menu>
                Devices
            </p>
            <p id="devices_menu_details"/>
            </p>
            <!--table>
                <tr><td class="user">Device_1</td>
                </tr><td class="user">Device_2</td>
            </table-->        
        </div>
        <div id="users_content">
             <!--style="width: 190px; height: 50px; position: absolute; left: 10px; top: 150px; background-color:#64A0DE; color: black"-->
             Users page... 
       
       Example using axis... http://bl.ocks.org/1166403
       <style type="text/css">

            <!--body {
            font: 14px sans-serif;
            margin: 0;
            }-->

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
        <script type="text/javascript">

        var m = [80, 80, 80, 80],
            w = 960 - m[1] - m[3],
            h = 500 - m[0] - m[2],
            parse = d3.time.format("%d %B %Y %H:%M:%S").parse;
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
        d3.csv("js/hwdatatestmock.csv", function(data) {

        // Filter to one device: 10.2.0.1
        var values = data.filter(function(d) {
            return d.device == "10.2.0.1";
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

        </script>
  
             
       <div id="chart">
            <div id="footer">
                <div class="title"><!--Ireland Power [MW]--></div>
                <div class="legend"></div>
                <div class="times"></div>
                <div class="hint">click or option-click to toggle zoom</div>
            </div>
       </div>
      
       <!--script src="http://mbostock.github.com/d3/d3.v2.js"></script-->
       
       <!--script type="text/javascript">

        var m = [79, 80, 160, 79],
            w = 1000 - m[1] - m[3],
            h = 500 - m[0] - m[2],
            parse = d3.time.format("%Y-%m-%d %H:%M:%S").parse,
            format = d3.time.format("%Y-%m-%d %H:%M"),
            colors = d3.scale.ordinal().range(["lightgray", "lightpink", "lightblue"]);
            // color = d3.interpolateRgb("#aad", "#556");
        // Scales. Note the inverted domain for the y-scale: bigger is up!
        var x = d3.time.scale().range([0, w]),
            y = d3.scale.linear().range([h, 0]),
            x_dom = [],
            x_dom_zoom = [];

        // Axes.
        var xAxis = d3.svg.axis().scale(x).orient("bottom"),
            yAxis = d3.svg.axis().scale(y).orient("left");

        // The area generator.
        var area = d3.svg.area()
            .x(function(d) {return x(d.x) })
            .y0(function(d){return y(d.y0)   })
            .y1(function(d){return y(d.y0+d.y) });

        var svg = d3.select("#chart").append("svg:svg")
            .attr("width", w + m[1] + m[3])
            .attr("height", h + m[0] + m[2])
            .append("svg:g")
            .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

        svg.append("svg:rect")
            .attr("width", w)
            .attr("height", h);

        svg.append("svg:clipPath")
            .attr("id", "clip")
            .append("svg:rect")
            .attr("x", x(0))
            .attr("y", y(1))
            .attr("width", x(1) - x(0))
            .attr("height", y(0) - y(1));
            
        d3.csv("js/data.csv", function(data) {
                     
                // get header names
                var header_row=d3.keys(data[0]);
                var gen_names = header_row.filter(
                    function(s){
                        return s.substring(0,7)=='power: ';
                    }).map(
                    function(s){
                        return s.substring(7,s.length);
                    });
            d3.select("#footer.legend").html(gen_names.map(
                    function(name,i){
                        return '<span style="color:'+colors(i)+'">'+name+'</span>'
                    }
                ).join(", "));
        
            // Parse times and power for generators.
            var stack_gens = d3.layout.stack()(gen_names.map(function(gen_kind) {
                return data.map(function(d) {
                return {x: parse(d.times), y: +d['power: '+gen_kind]};
                });
            }));  
            // console.log(stack_gens)
            index_last_gen=stack_gens.length-1
            index_last_time=stack_gens[0].length-1
            // Compute the minimum and maximum date, and the maximum price.
            // d0 = stack_gens[0].map(function(d){return d.x}); //the whole domain
            x_dom = [stack_gens[0][0].x,stack_gens[0][index_last_time].x]
            d3.select("#footer .times").text(x_dom.map(format).join(" to "));

            x_dom_zoom = [new Date(2010,4-1,28,20), new Date(2010, 4-1, 29,0)]; //just a small part of domain
            y_dom = [0, d3.max(stack_gens[index_last_gen], function(d) { return d.y0+d.y; })]

            x.domain(x_dom);
            y.domain(y_dom);

            svg.append("svg:g")
                .attr("class", "x grid")
                .attr("transform", "translate(0," + h + ")")
                .call(xAxis.tickSubdivide(0).tickSize(-h));

            svg.append("svg:g")
                .attr("class", "y grid")
                .attr("transform", "translate(0,0)")
                .call(yAxis.tickSubdivide(1).tickSize(-w));

            svg.append("svg:g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + h + ")")
                .call(xAxis.tickSubdivide(0).tickSize(6));

            svg.append("svg:g")
                .attr("class", "y axis")
                .call(yAxis.tickSubdivide(0).tickSize(6));

            svg.selectAll("g.generator")
                .data(stack_gens)
                .enter().append("svg:path")
                    .attr("class", "generator")
                    .style("fill",   function(d,i){return colors(i)})
                    .style("stroke", function(d,i){return d3.rgb(colors(i)).darker()})
                    .attr("clip-path", "url(#clip)")
                    .attr("d", area)

            });

            // On click, update the x-axis.
            svg.on("click", function() {
                var new_dom = x.domain()[0] - x_dom[0] ? x_dom : x_dom_zoom;
                x.domain(new_dom);
                d3.select("#footer .times").text(new_dom.map(format).join(" to "));
                var t = svg.transition().duration(d3.event.altKey ? 7500 : 750);
                t.select("g.x.grid").call(xAxis.tickSubdivide(1).tickSize(-h));
                t.select("g.y.grid").call(yAxis.tickSubdivide(1).tickSize(-w));
                t.select("g.x.axis").call(xAxis.tickSubdivide(0).tickSize(6));
                t.select("g.y.axis").call(yAxis.tickSubdivide(0).tickSize(6));
                t.selectAll("path.generator").attr("d", area);
            });
    </script-->
        
        
        </div>
    </body>
</html>
