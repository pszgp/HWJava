/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


    // d3 barchart sample from : "http://www.guerino.net"
    //var dataSet = [{legendLabel: "Data Usage: 2012 APRIL", magnitude: 54.45},
        //{legendLabel: "Data Usage: 2012 MAY", magnitude: 21.1}];
  
var hwColour = "#64A0DE";

function drawHorizontalBarChart(chartID, selectString, deviceIp, colors, values, months, devicesAll, monthName) {
//
//alert("HERE");
//boolean values: months, devicesAll (device.js, dashboard.js)
//
    //alert(devicesAll);
    //alert(values);
    //alert(values.indexOf("{"));

    //var deviceIp = "${deviceIp}";
    //var monthId = "${monthId}";
    //var monthName = "${month}";
    /*alert("*"+monthName+"*");
    if (monthName==null) alert("is null");
    if (monthName=="") alert ("is zero");
    alert (""+((monthName==null)||(monthName=="")));
    if (monthName=="") months = false;
    //if ((monthName!=null) && (monthName != "")) months = false;*/

    var MONTHS = [null, "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
                    "OCTOBER", "NOVEMBER", "DECEMBER"];
    
    //alert(monthName in MONTHS);
    if (monthName in MONTHS)
        months = false;
    
    if (values.indexOf("{")==0)
    {
        values = values.substring(1, values.length-1);
        //alert(values);
        values = values.split(",");
        //alert(values);
    }

    if (values==null)
        return;
    /*d3.csv("data/HW/HWStatsUsageFlows_sql_.csv", function (data) {
            values = data;
    });*/
    /*if (deviceIp!=null)
        values = data.filter(function(d) {             
            var date = d.ipdate;
            date = date.substring(date.indexOf(" ")+1);
            var deviceMonth = date.split("-")[1];
            if (deviceMonth.indexOf("0")==0)
                deviceMonth=deviceMonth.substring(1);
            deviceMonth--;
            //alert(deviceMonth+" "+monthId+" "+(deviceMonth==monthId));
            //exit;
            //if (d.ipaddr == deviceIp)
            //    if (deviceMonth == monthId)
            //        return true;
            //return false;
            return ((d.ipaddr == deviceIp) && (deviceMonth == monthId));
        }); 
        //alert(values);
        /*values.forEach(function(d) {
            var date = d.ipdate;
            date = date.substring(date.indexOf(" ")+1);
            var day = date.split("-")[2];
            //to avoid null values set the non used values to default:
            d.date = date; d.ipaddr = deviceIp; d.last = date;
            //set the values used for the graph
            d.ipdate = day + monthName;//parse(date);
            d.bytes =+ d.bytes;
            d.bytes = d.bytes/(1024*1024);
            d.bytes = d.bytes.toFixed(2);
            //set the link to details of the day... 
            d.link = "deviceday.htm?ip="+deviceIp+"&month="+monthName+"&monthId="+monthId+"&day="+day;
        });*/
        var data = [];
        for (var i=0;i<values.length;i++)
        {
            var d = values[i];
            var date = d;
            date = date.substring(0, date.indexOf("="));
            if (months)
            {
                var dateNr = +date;
                //date = +date;
                if (dateNr == null)
                {    
                    months = false; 
                }
                else
                    date = MONTHS[dateNr];                  
                /*if (dateNr != null)
                    date = dateNr;
                else {
                    date = date;//25 aug. 2012: use the name of the device for the dashboard page
                    months = false; devicesAll = false;
                }*/
               
            }
            else 
                date = date;//+" "+monthName;

            var nbytes = d.substring(d.indexOf("=")+1, d.length);
            nbytes = +nbytes;
            nbytes = nbytes / (1024*1024);    
            nbytes = nbytes.toFixed(2);//decimals, Math.floor(): no decimals

            var object = {};
            object.date = date;
            object.bytes = nbytes;

            data[i] = object;

            //alert(object);
        }

        for (var i=0;i<data.length;i++)
        {    
            var d = data[i];
            //alert(d.date+" "+d.bytes);
        }
        //alert(data);

        /*var data=[1,2,3,4];
        data.forEach(function(d){
            data.date = 1;
            data.bytes = 2;
            alert(data.date+" "+data.bytes);
            return data;
        });*/
        /*values.forEach(function(d){
            //alert(d+" "+d.indexOf("="));
            var date = d;
            date = date.substring(0, date.indexOf("="));
            var nbytes = d.substring(d.indexOf("=")+1, d.length);
            nbytes = +nbytes;
            //alert(d+" "+date+" and "+nbytes);
            d.date = date;
            d.nbytes = nbytes;  
            var data={"date":date, "nbytes": nbytes};
            data.date = data["date"];
            data.nbytes = data["nbytes"];
            alert(data.date+" "+d.nbytes);  
            d.date = data.date;
            d.nbytes = data.nbytes;
            d = data;
            return d;
            //return data;
            //alert(d.nbytes+" "+d.date);
        });*/

        //alert(values);

        values = data;
        //alert(values);
        //values.forEach(function(d){alert(d.date+" "+d.bytes);});

        var canvasWidth = 700;//d3.max(values, function(d) { return d.bytes/10;});//700;
        var barsWidthTotal = 300;
        var barHeight = 15;//24;//20;//25 aug. 2012: increase the bars to align to the table of total usages
        var barsHeightTotal = barHeight * values.length;
        //var canvasHeight = 200;
        var canvasHeight = values.length * barHeight;// + 10; // +10 puts a little space at bottom.
        var legendOffset = barHeight/2;
        var legendBulletOffset = 30;
        var legendTextOffset = 40;

        var x = d3.scale.linear().domain([0, d3.max(values, function(d) { return d.bytes/10; })]).rangeRound([0, barsWidthTotal]);
        var y = d3.scale.linear().domain([0, values.length]).range([0, barsHeightTotal]);

        // Create the svg drawing canvas...
        var canvas = d3.select(selectString)
        .append("svg:svg")
            .attr("width", canvasWidth)
            .attr("height", canvasHeight).style("background-color", "white");

        // Draw individual hyper text enabled bars...
        canvas.selectAll("rect")
        .data(values)
        .enter()
            .append("svg:rect")
            .attr("x", 0) // Right to left
            .attr("y", function(d, i) { return y(i); })
            .attr("height", barHeight)
            .style("fill", "black" )
            .style("stroke", "black" ) 
            .attr("width", function(d) { if ((d.bytes/10)<1) return 1; else return x(d.bytes/10); })
            .style("fill", function(d, i) { colorVal = hwColour; return colorVal; } )
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d, i) { return "bars-" + chartID + "-bar-index-" + i; })
            .attr("color_value", function(d, i) { return hwColour; }) 
            .style("stroke", "black");            

        // Create text values that go at end of each bar...
        if (months)
        {
            canvas.selectAll("text")
            .data(values) // Bind dataSet to text elements
            .enter()
            .append("svg:a").attr("xlink:href", function(d) { return "device.htm?ip="+deviceIp+"&month="+d.date; })
            .append("svg:text") // Append text elements
                .attr("x", x)
                .attr("y", function(d, i) { return y(i); })
                .attr("dx", function(d) { return barsWidthTotal;})//return x(d.bytes/10) + 100; }) 
                .attr("dy", barHeight-5) 
                //.attr("text-anchor", "end") 
                .attr("font", "12px Verdana, sans-serif")
                .text(function(d) { return d.date+": "+d.bytes + " Mb";})
                .attr("font", "12px Verdana, sans-serif");
                //.attr("fill", "black");
            //.append("svg:a")
           // .attr("xlink:href", function(d) { return "device.htm?ip="+deviceIp+"&month="+d.date; });
        }
        else if (devicesAll)
        {
            canvas.selectAll("text")
            .data(values) // Bind dataSet to text elements
            .enter()
            .append("svg:a").attr("xlink:href", function(d) { return "device.htm?ip="+d.date; })
            .append("svg:text") // Append text elements
                .attr("x", x)
                .attr("y", function(d, i) { return y(i); })
                .attr("dx", function(d) { return barsWidthTotal;})
                .attr("dy", barHeight-5) 
                .text(function(d) { return d.date+": "+d.bytes + " Mb";})
                .attr("fill", "black");            
        }
        else
        {
            canvas.selectAll("text")
            .data(values) // Bind dataSet to text elements
            .enter()
            .append("svg:text") // Append text elements
                .attr("x", x)
                .attr("y", function(d, i) { return y(i); })
                .attr("dx", function(d) { return barsWidthTotal;})
                .attr("dy", barHeight-5) 
                .text(function(d) { return d.date+": "+d.bytes + " Mb";})
                .attr("fill", "black");
            
        }
        // Create hyper linked text at right that acts as label key...
        /*if (months)
        {
            canvas.selectAll("a.legend_link")
            .data(values) // Instruct to bind dataSet to text elements
            .enter().append("svg:a")
                .attr("xlink:href", function(d) { return "device.htm?ip="+deviceIp+"&month="+d.date; })
                    .append("text")                    
                    .attr("text-anchor", "center")
                    .attr("x", x)//barsWidthTotal + legendBulletOffset + legendTextOffset)
                    .attr("y", function(d, i) { return legendOffset + i*barHeight; } )
                    .attr("dx", 0)
                    .attr("dy", "5px") 
                    .text(function(d) { return d.date;})//legendLabel
                    .style("fill", "Black")                    
                    //.on('mouseover', synchronizedMouseOver)
                    //.on("mouseout", synchronizedMouseOut)
                    .attr("index_value", function(d, i) { return "index-" + i; })
                    .attr("class", function(d, i) { return "bars-" + chartID + "-legendText-index-" + i; });
        }*/

            // });//dataSet function end
    };//draw function end

    