var w = 960,
    h = 500,
    p = [20, 50, 30, 20],
    x = d3.scale.ordinal().rangeRoundBands([0, w - p[1] - p[3]]),
    y = d3.scale.linear().range([0, h - p[0] - p[2]]),
    //devices need more colours: 24 colors if possible...
    //  http://www.w3schools.com/html/html_colors.asp
    /*z = d3.scale.ordinal().range(["009900","009933","009966","009999","0099CC","0099FF",
                                    "330000","330033","330066","330099","3300CC","3300FF",
                                    "663300","663333","663366","663399","6633CC","6633FF",
                                    "CC0000","CC0033","CC0066","CC0099","CC00CC","CC00FF"]);*/
        z = d3.scale.category20();
    //z = d3.scale.ordinal().range(["lightpink", "darkgray", "lightblue"]),
    parse = d3.time.format("%Y-%m-%d:%H").parse,
    format = d3.time.format("%b");

//alert(document.getElementById("stacked").innerHTML);

var svg = d3.select("#stacked").append("svg:svg")
    .attr("width", w)
    .attr("height", h)
    .append("svg:g")
    .attr("transform", "translate(" + p[3] + "," + (h - p[2]) + ")");

//d3.csv("js/stacked_bar_chart/devices_mock_stacked.csv", function(devicesFlows) {
//d3.csv("js/stacked_bar_chart/devices_flows_stacked.csv", function(devicesFlows) {
//7 june 2012 - change the file:
d3.csv("js/stacked_bar_chart/devices_7june2012_file_ready.csv", function(devicesFlows) {
   //alert(devicesFlows);
  // Transpose the data into layers by cause.
  //var devices = d3.layout.stack()(["device1", "device2"].map(function(cause) {
  /*var devicesList = ["10.2.0.1","10.2.0.2","128.243.19.10","128.243.19.110","128.243.19.12",
      "128.243.19.19","128.243.19.20","128.243.19.35","128.243.20.248","128.243.20.6","128.243.21.108",
      "128.243.21.16","128.243.21.248","128.243.22.140","128.243.22.141","128.243.22.244","128.243.22.9",
      "128.243.46.118","192.168.1.64","192.168.1.68","192.168.1.94","192.168.1.96","192.168.217.1",
      "192.168.36.1"];  */
    
    var devicesList = ["10.2.0.1","10.2.0.2","192.168.36.1","128.243.19.10",
        "128.243.19.110","128.243.19.12","128.243.19.19","128.243.19.20","128.243.20.6",
        "128.243.21.108","192.168.217.1","128.243.21.16","128.243.21.248","128.243.22.140",
        "128.243.19.35","128.243.20.248","128.243.22.141","128.243.22.244","192.168.1.94",
        "192.168.1.96","128.243.22.244", "128.243.22.9","128.243.46.118","192.168.1.64","192.168.1.68"];
  
  //alert(devicesList);
  var devices = d3.layout.stack()(devicesList.map(function(device) {    
            //alert(device);
            return devicesFlows.map(function(d) {
                //alert(d);
                return {x: parse(d.date), y: (+d[device]/(1024*1024))};
                //return {x: parse(d.date), y: +d[device]};
        });
   }));

  // Compute the x-domain (by date) and y-domain (by top).
  x.domain(devices[0].map(function(d) { return d.x; }));
  y.domain([0, d3.max(devices[devices.length - 1], function(d) { return d.y0 + d.y; })]);

  // Add a group for each cause.
  var device = svg.selectAll("g.device")
      .data(devices)
    .enter().append("svg:g")
      .attr("class", "device")
      .style("fill", function(d, i) { return z(i); })
      .style("stroke", function(d, i) { return d3.rgb(z(i)).darker(); });
      //add legend???? if possible???...
      //.append("svg:text").attr("x", w/2).attr("y", h/2).
        //  text(function(d,i){return d(i);});

  // Add a rect for each date.
  var rect = device.selectAll("rect")
      .data(Object)
    .enter().append("svg:rect")
      .attr("x", function(d) { return x(d.x); })
      .attr("y", function(d) { return -y(d.y0) - y(d.y); })
      .attr("height", function(d) { return y(d.y); })
      .attr("width", x.rangeBand());

  // Add a label per date.
  var label = svg.selectAll("text")
      .data(x.domain())//this could be also: data(d3.range(samples))
    .enter().append("svg:g")//"svg:text")
      .attr("x", function(d) { return x(d) + x.rangeBand() / 2; })
      .attr("y", 6)
      .attr("text-anchor", "middle")
      .attr("dy", ".71em");      
        //try to rotate the text vertically...
        //.attr("transform", function(d){return "rotate(90)"})
       //     .style("-webkit-transform", "rotateZ(90deg)") //rotate the label???
      // .attr("transform", function(d, i) {return "rotate(90)";})
      //.text(d3.time.format("%d:%H"));//-%m-%d:%H"));//format);
      label.append("svg:text")
          //attr("transform", function(d) {return "rotate(90)";}).
          .attr("dy", ".5em")
          .text(d3.time.format("%d:%H"));

  // Add y-axis rules.
  var rule = svg.selectAll("g.rule")
      .data(y.ticks(5))
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
      .text(d3.format(",d"));
      
      
});