function drawPartitionSunburst(file)
{
    var width = 700,//960,
        height = 500,//700,
        radius = Math.min(width, height) / 2,
        color = d3.scale.category20c();

    var vis = d3.select("#chart").append("svg")
        .attr("width", width)
        .attr("height", height)
    .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

    var partition = d3.layout.partition()
        .sort(null)
        .size([2 * Math.PI, radius * radius])
        .value(function(d) { return 1; });

    var arc = d3.svg.arc()
        .startAngle(function(d) { return d.x; })
        .endAngle(function(d) { return d.x + d.dx; })
        .innerRadius(function(d) { return Math.sqrt(d.y); })
        .outerRadius(function(d) { return Math.sqrt(d.y + d.dy); });

    //d3.json("../data/flare.json", function(json) {
    d3.json(file, function(json){
    var path = vis.data([json]).selectAll("path")
        .data(partition.nodes)
        .enter() .append("a")
            .attr("xlink:href", function(d) {
                if (d.depth==1)
                    return "device.htm?ip=" + d.name; })           
        .append("path")
        .attr("display", function(d) { return d.depth ? null : "none"; }) // hide inner ring
        .attr("d", arc)
        .attr("fill-rule", "evenodd")
        .style("stroke", "#fff")
        .style("fill", function(d) { return color((d.children ? d : d.parent).name); })    
        //.append("svg:a").attr("xlink:href", function(d) { return "device.htm?ip="+d.name; })
       
      // .append("a")
    //.attr("xlink:href", function(d) {alert(d.depth+" "+d.parent+" "+d.name); return "device.htm?id=" + d.name; })
   
        .append("svg:title").text(function(d) { 
            var MONTHS=["", "JANUARY", "FEBRUARY", "MARH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
                            "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
                        //alert(d.depth+" "+d.name);
            if (d.depth==1)
                return "Device: "+d.name; 
            if (d.depth == 2)
                return "Device: " + d.parent.name + " Year: "+d.name;
            if (d.depth == 3)
                return "Device: " + d.parent.parent.name + " Year: " + d.parent.name + " Month: " + MONTHS[+d.name];
            if (d.depth ==4)
                return "Device: " + d.parent.parent.parent.name + " Year: " + d.parent.parent.name + 
                    " Month: " + MONTHS[+d.parent.name]+" Day: " + d.name;
            if (d.depth == 5)
                return "Device: " + d.parent.parent.parent.parent.name + " Year: " + d.parent.parent.parent.name + 
                    " Month: " + MONTHS[+d.parent.parent.name]+" Day: " + d.parent.name + " Hour: " + d.name+"h "+ (+d.size)+" Mb";
        })
        .each(stash);
        
    //append labels!!!!!!!!!!! ????
    // Place Labels 
    //path.append("g").append("svg:title").text("a");
    path//.append("g").append("rect").attr("height", 100).style("fill", "green")
           .append("a")
            .attr("xlink:href", function(d) {
                if (d.depth==1)
                    return "device.htm?id=" + d.name; });
    
        vis.append("g").append("svg:text") 
           // .attr("transform", function(d) { return "translate(" + 
           // pos.centroid(d) + ")"; }) 
            .attr("dy", 5) 
            .attr("text-anchor", "middle") 
            .attr("fill", function(d, i) { return "blue"; }) //Colorarray Labels: collorL(i)
            .attr("display", function(d) { return d.value >= 2 ? null : "none"; })  
            .text(function(d, i) { return "Sunburst View of Devices Usage Per Hours";});

    d3.select("#size").on("click", function() {
        path
            .data(partition.value(function(d) { return d.size; }))
        .transition()
            .duration(1500)
            .attrTween("d", arcTween);

        d3.select("#size").classed("active", true);
        d3.select("#count").classed("active", false);
    });

    d3.select("#count").on("click", function() {
        path
            .data(partition.value(function(d) { return 1; }))
        .transition()
            .duration(1500)
            .attrTween("d", arcTween);

        d3.select("#size").classed("active", false);
        d3.select("#count").classed("active", true);
    });
    });

    // Stash the old values for transition.
    function stash(d) {
    d.x0 = d.x;
    d.dx0 = d.dx;
    }

    // Interpolate the arcs in data space.
    function arcTween(a) {
    var i = d3.interpolate({x: a.x0, dx: a.dx0}, a);
    return function(t) {
        var b = i(t);
        a.x0 = b.x;
        a.dx0 = b.dx;
        return arc(b);
    };
    }
}