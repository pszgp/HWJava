/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function drawStackedDevicesUsage(devicesUsage, devicesList, isMonths, isDays, isHours)
{
    var width = 960,
    height = 500;

    var x = d3.scale.linear()
        .range([0, width]);

    var y = d3.scale.linear()
        .range([0, height - 40]);

    // An SVG element with a bottom-right origin.
    var svg = d3.select("#chart").append("svg")
        .attr("width", width)
        .attr("height", height)
        .style("padding-right", "30px")
    .append("g")
        .attr("transform", "translate(" + x(1) + "," + (height - 20) + ")scale(-1,-1)");

    // A sliding container to hold the bars.
    var body = svg.append("g")
        .attr("transform", "translate(0,0)");

    // A container to hold the y-axis rules.
    var rules = svg.append("g");

    // A label for the current year.
    var title = svg.append("text")
        .attr("class", "title")
        .attr("dy", ".12em")//.71em
        .attr("transform", "translate(" + x(1) + "," + y(1) + ")scale(-1,-1)")
        .text(2012);

    //d3.csv("js/stacked_bar_chart/flows_sums_.csv", function(data) {
        //population/devices_mock.csv", function(data) {
    
    //convert the devices data into array
    /* {10.2.0.1={2012={4=528516004, 5=2904380438, 6=106080327, 7=175350}}, 10.2.0.2={2012={4=18539541, 5=158112950, 6=4370045, 7=91445}}, 128.243.19.10={2012={5=2318}}, 128.243.19.110={2012={4=66, 5=1312}}, 128.243.19.12={2012={5=518}}, 128.243.19.19={2012={5=968}}, 128.243.19.20={2012={5=64040}}, 128.243.19.35={2012={5=11204}}, 128.243.20.248={2012={5=6675922}}, 128.243.20.6={2012={5=162}}, 128.243.21.108={2012={5=999}}, 128.243.21.16={2012={5=54}}, 128.243.21.248={2012={5=1332}}, 128.243.22.140={2012={5=7830}}, 128.243.22.141={2012={5=24238}}, 128.243.22.244={2012={5=540}}, 128.243.22.9={2012={5=4462}}, 128.243.46.118={2012={5=360}}, 192.168.1.64={2012={5=15222}}, 192.168.1.67={2012={6=524}}, 192.168.1.68={2012={5=9948, 6=800}}, 192.168.1.70={2012={7=248}}, 192.168.1.94={2012={4=214}}, 192.168.1.96={2012={5=1758}}, 192.168.217.1={2012={4=180, 5=40320, 6=828}}, 192.168.36.1={2012={4=180, 5=40132, 6=828}}}
     *
     **/
    
    
    // Convert strings to numbers.
    var data = devicesUsage.forEach(function(d) {
        //d.people = +d.people;
        //d.year = +d.year;
        //d.age = +d.age;

        d.nbytes = +d.nbytes;
        d.year = +d.year;
        d.month = +d.month;
        d.day = +d.month;
    });

    // Compute the extent of the data set in age and years.
    var day0 = 0;//age0 = 0,
        day1 = d3.max(data, function(d) {return d.day;});//age1 = d3.max(data, function(d) { return d.age; }),
        year0 = d3.min(data, function(d) { return d.year; }),
        year1 = d3.max(data, function(d) { return d.year; }),
        year = year1;

    // Update the scale domains.
    x.domain([0, day1 + 5]);//age1
    y.domain([0, d3.max(data, function(d) { return d.nbytes;})]);//people; })]);

    // Add rules to show the population values.
    rules = rules.selectAll(".rule")
        .data(y.ticks(10))
        .enter().append("g")
        .attr("class", "rule")
        .attr("transform", function(d) { return "translate(0," + y(d) + ")"; });

    rules.append("line")
        .attr("x2", width);

    rules.append("text")
        .attr("x", 6)
        .attr("dy", ".35em")
        .attr("transform", "rotate(180)")
        .text(function(d) { return Math.round(d / 1e6) + "Mb"; });

    // Add labeled rects for each birthyear.
    var years = body.selectAll("g")
        .data(d3.range(year0 - day1, year1 + 5, 5))//age1
        .enter().append("g")
        .attr("transform", function(d) { return "translate(" + x(year1 - d) + ",0)"; });

    years.selectAll("rect")
        .data(d3.range(2))
        .enter().append("rect")
        .attr("x", 1)
        .attr("width", x(5) - 2)
        .attr("height", 1e-6);

    years.append("text")
        .attr("y", -6)
        .attr("x", -x(5) / 2)
        .attr("transform", "rotate(180)")
        .attr("text-anchor", "middle")
        .style("fill", "#fff")
        .text(String);

    // Add labels to show the age.
    svg.append("g").selectAll("text")
        .data(d3.range(0, day1 + 5, 5))//age1
        .enter().append("text")
        .attr("text-anchor", "middle")
        .attr("transform", function(d) { return "translate(" + (x(d) + x(5) / 2) + ",-4)scale(-1,-1)"; })
        .attr("dy", ".71em")
        .text(String);

    // Nest by year then birthyear.
    data = d3.nest()
        .key(function(d) { return d.year; })
        .key(function(d) { return d.year - d.day; })//d.age
        .rollup(function(v) { return v.map(function(d) { return d.nbytes; }); })//d.people
        .map(data);

    // Allow the arrow keys to change the displayed year.
    d3.select(window).on("keydown", function() {
        switch (d3.event.keyCode) {
        case 37: year = Math.max(year0, year - 10); break;
        case 39: year = Math.min(year1, year + 10); break;
        }
        redraw();
    });

    redraw();

    function redraw() {
        if (!(year in data)) return;
        title.text(year);

        body.transition()
            .duration(750)
            .attr("transform", function(d) { return "translate(" + x(year - year1) + ",0)"; });

        years.selectAll("rect")
            .data(function(d) { return data[year][d] || [0, 0]; })
        .transition()
            .duration(750)
            .attr("height", y);
    }
    //});

    
    /*/alert(devicesList);
    var w = 960,
        h = 500,
        p = [20, 50, 30, 20],
        //x = d3.scale.ordinal().rangeRoundBands([0, w - p[1] - p[3]]),
        //y = d3.scale.linear().range([0, h - p[0] - p[2]])
        x = d3.scale.linear().range([0, w]),
        y = d3.scale.linear().range([0, h - 40]),
        z = d3.scale.category20();

    var svg = d3.select("#stacked").append("svg:svg")
        .attr("width", w)
        .attr("height", h)
        .append("svg:g")
        .attr("transform", "translate(" + p[3] + "," + (h - p[2]) + ")");
        
    //adapted from: http://mbostock.github.com/d3/ex/population.html 
    // A sliding container to hold the bars.
    var body = svg.append("g").attr("transform", "translate(0,0)");  
    // A container to hold the y-axis rules.
    var rules = svg.append("g");    

    //read the devices and draw the bars for devices!!!
    d3.csv("js/stacked_bar_chart/flows_sums_.csv", function(devices){
 
        devices.forEach(function(d) {
            //alert(d.deviceIp); //or: alert(d["deviceIp"]);
            return {x: +d.month, y: (+d.nbytes/(1024*1024))};
        });

        // Compute the x-domain (by date) and y-domain (by top).
        var xmin = devices[0].month;
        var xmax = d3.max(devices, function(d) { return d.month; });
        var ymin = 0;
        var ymax = d3.max(devices, function(d) { return d.nbytes; });
        x.domain([xmin, xmax]);
        y.domain([0, ymax]);
        
        // Add rules to show the devices usage.
        rules = rules.selectAll(".rule")
            .data(y.domain())
            .enter().append("g")
            .attr("class", "rule")
            .attr("transform", function(d) { return y(d); });
  
        // Add labeled rects for each date.
        var dates = body.selectAll("g")
            .data(x.domain())
            .enter().append("g")
            .attr("transform", function(d) { return x(d); });
  
        dates.selectAll("rect")
            .data(x.domain())
            .enter().append("rect")
            .attr("y", -20)
            .attr("x", function(d){return x(d) * 20;})//x(d)*20;})
            .attr("width", 20)
            .attr("height", function(d){return y(d) * 10;})//y(d)*10;})            
            .style("fill", function(d, i) { return z(i); });
            //.style("stroke", function(d, i) { return d3.rgb(z(i)).darker(); });
  
        //add labels for the dates (e.g.: months)
        dates.append("text")
            .attr("y", 10)
            .attr("x", function(d){return d.month*20;})//x(d);})//)-y(5) / 2)
            .attr("text-anchor", "middle")
            .text(function(d){return d.deviceIp;});//String);
            
        // Add labels to show the devices.
        /*svg.append("g").selectAll("text")
            .data(devices)
            .enter().append("text")
            .attr("text-anchor", "middle")
            //.attr("transform", function(d) { return "translate(" + (x(d) + x(5) / 2) + ",-4)scale(-1,-1)"; })
            //.attr("dy", ".71em")
            .text(function(d) { return d.deviceIp;});//String);
           */ 
            
        /*    // Add y-axis rules.
        var rule = svg.selectAll("g.rule")
            .data(y.domain())
            .enter().append("svg:g")
            .attr("class", "rule")
            .attr("transform", function(d) { return "translate(0," + -y(d) + ")"; });

        rule.append("svg:line")
            .attr("x2", w - p[1] - p[3])
            .style("stroke", function(d) { return d ? "#fff" : "#000"; })
            .style("stroke-opacity", function(d) { return d ? .7 : null; });

        rule.append("svg:text")
            .attr("x", w - p[1] - p[3] + 6)
            .attr("dy", ".35em")
            .text(String);*/
            
            //...
       /* var device = svg.selectAll("g.device")
            .data(devices)
            .enter().append("svg:g")
            .attr("class", "device")
            .style("fill", function(d, i) { return z(i); })
            .style("stroke", function(d, i) { return d3.rgb(z(i)).darker(); });

        var rect = device.selectAll("rect")
            .data(Object)
            .enter().append("svg:rect")
            .attr("x", function(d) { return x(d) * 20; })
            .attr("y", function(d) { return y(d); })
            .attr("height", function(d) { return y(d); })
            .attr("width", 20);

       /* // Add a label per date.
        var label = svg.selectAll("g:text")
            .data(x.domain())//this could be also: data(d3.range(samples))
            .enter().append("svg:text")
            .attr("x", function(d) { return x(d) * 20 ; })
            .attr("y", 6)
            .attr("text-anchor", "middle")
            .attr("dy", ".71em");      
            label.append("svg:text")
                .attr("transform", function(d) {return "rotate(90)";})
                .attr("dy", ".5em")
                .text(String);//function(d) { return x(d);});*/

        // Add y-axis rules.
       /* var rule = svg.selectAll("g.rule")
            .data(y.ticks(5))
            .enter().append("svg:g")
            .attr("class", "rule")
            .attr("transform", function(d) { return "translate(0," + -y(d) + ")"; });

        rule.append("svg:line")
            .attr("x2", w - p[1] - p[3])
            .style("stroke", function(d) { return d ? "#fff" : "#000"; })
            .style("stroke-opacity", function(d) { return d ? .7 : null; });

        rule.append("svg:text")
            .attr("x", w - p[1] - p[3] + 16)
            .attr("dy", ".35em")//.35em
            .text(function(d){return Math.floor(y(d))+"Mb";});//d3.format(",d")+"Mb");
           

    });*/
}