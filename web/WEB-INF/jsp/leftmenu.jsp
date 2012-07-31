<%-- 
    Document   : leftmenu
    Created on : 10-May-2012, 11:43:54
    Author     : pszgp
--%>

<script>
    function open_menu(menu){
        var element = document.getElementById( menu + "_details");
        var text = "<table>";
        if (menu=='devices_menu')
            data = "${devicesIps}";
        //alert(data);
        if (data!=null)
            if (data.length>0)
                {
                    data = data.substring(1, data.length-1);
                    data = data.split(",");
                }
        //alert(data);        
        for (var i=0; i < data.length; i++)
        {            
            if (menu=='devices_menu')
            {    
                device_ip = data[i];//11july2011
                //alert(device_ip);
                //device_data = data[i];//.split(";");
                //for (var j=0; j<device_data.length; j++)
                //{
                    //index_device_ip = device_data[j].indexOf("ip:");
                    //if (index_device_ip != -1)
                    //{
                        //device_ip = device_data[j].substring(3, device_data[j].length);
                        //text += "<tr><td class=\"user\" "+
                        //    "onclick=\"drawAxis('" + device_ip +"');\">"+device_ip+"</td></tr>";
                        text += "<tr><td class=\"user\" >"+
                            "<a class=\"a_header\" href=\"device.htm?ip="+device_ip +"\">"+device_ip+"</a></td></tr>";
                        
                        //document.getElementById("deviceDataUsage").innerHTML = device_ip;
                        //break;
                    //}
                //}
            }
        }
        text += "</table>";
        
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
        <div id="users_left_menu">
             <p class="menu_title" id="devices_menu">
                <img src="images/device.png" alt=""/>
                <open_menu id="open_devices_menu" onclick="open_menu('devices_menu');"> + </open_menu>
                Devices
            </p>
            <p id="devices_menu_details"></p>    
            <br/><br/><br/><br/><br/><br/><br/><br/><br/>
            <h3><p style="padding-left: 30pt;" onclick="window.location.reload()"><u>Reload</u></p></h3>
            
            
        </div>    
        
<br/>
        
            

