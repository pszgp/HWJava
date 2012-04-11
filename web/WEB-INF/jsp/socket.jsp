<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
        <h3>Show data from HWDB... under development!</h3>
        <br/>
        
         <!-- calendar -->
        <div id="menuLeft" style="width: 190px; height: 500px; position: absolute; left: 10px; top: 40px; background-color: blue; color: black">
           
            <script type="text/javascript">                
                var currentMonth = 1;
                var monthNames = ["null", "January", "February", "March", "April"];
                function nav_cal(index){
                    var realMonth = new Date().getMonth();
                    if ((currentMonth <= realMonth) && (index == 1))
                        currentMonth++;
                    else if ((currentMonth > 1) && (index == -1))
                        currentMonth--;
                    var month = monthNames[currentMonth];
                    //alert(month);
                    //alert(document.getElementById("currentMonth").innerHTML);
                    document.getElementById("currentMonth").innerHTML = month;
                    
                }
            </script>
            <p style="font-size: 18pt; width: 190px;">
                <b><table><tr><td>
                        <nav_cal onclick="nav_cal(-1);" style="font-size: 30px;"><b>&lt;</b></nav_cal>
                        </td>
                    <td width="170px" align="center" style="font-size: 18px;"><month id="currentMonth">January</month></td>
                    <td>
                        <nav_cal onclick="nav_cal(1);" style="font-size: 30px;"><b>&gt;</b></nav_cal></td>
            </tr></table></b>
            </p>
        </div>
        <div id="center" style="width: 500px; height: 500px; position:absolute; left:230px; top: 40px;">
            Fields types and values:<br/>
            <table style="border:2px; border-color: blue;">
                <tr><c:forEach var="type" items="${types}">
                        <th>${type}</th>
                    </c:forEach>
                </tr>
                <c:forEach var="item" items="${items}">
                    <tr>    
                        <c:forEach var="itemRow" items="${item}">
                            <td>${itemRow}</td>
                        </c:forEach>                        
                    </tr>
                </c:forEach> 
                
            </table>            
            <br/>
            sessionId = ${sessionId};<br/>${sessionAttrsNr}<br/>
            nrFields = ${countFields}<br/>
            nrRows = ${countRows}<br/>
            <input type="button" value="Reload" onclick="window.location.reload()"/>
        </div>
    </body>
</html>
