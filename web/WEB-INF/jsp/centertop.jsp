<%-- 
    Document   : centertop
    Created on : 10-May-2012, 11:48:25
    Author     : pszgp
--%>
   <div id="users_content">
            <h2>Current year: <y id="currentYear"></y></h2>
            <script language="javascript">
                document.getElementById("currentYear").innerHTML = new Date().getFullYear();                
            </script>
            <!--${deviceIp}${month}-->
            <!--h2>Month: <select id="menu_calendar" size="1">
                    <option value="JANUARY" selected="selected">JANUARY</option>
                    <option value="FEBRUARY">FEBRUARY</option>
                    <option value="MARCH">MARCH</option>
                    <option value="APRIL">APRIL</option>
                    <option value="MAY">MAY</option>
                    <option value="JUNE">JUNE</option>
                    <option value="JULY">JULY</option>
                    <option value="AUGUST">AUGUST</option>
                    <option value="SEPTEMBER">SEPTEMBER</option>
                    <option value="OCTOBER">OCTOBER</option>
                    <option value="NOVEMBER">NOVEMBER</option>
                    <option value="DECEMBER">DECEMBER</option>
                </select> </h2>
            
            <script type="text/javascript">
            //alert("hello from the device page!");
            var MONTHS = ["JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
            var dateName = "${month}";
            deviceIp = "${deviceIp}";            
            var dateIndex = -1;
            for (i=0;i<MONTHS.length;i++)
            {
                if(MONTHS[i]==dateName)
                {
                    dateIndex=i;
                    break;
                }
            }
            var selectmenu=document.getElementById("menu_calendar");            
            selectmenu.options[dateIndex].selected="selected";
            selectmenu.onchange=function(){ 
                var chosenoption=this.options[this.selectedIndex];
                //alert(chosenoption.value);
                if (chosenoption.value!="nothing"){
                    //alert("redirect");
                    window.location.href="device.htm?ip="+deviceIp+"&month="+chosenoption.value;
                }
            }
            
            </script-->      
            
 
