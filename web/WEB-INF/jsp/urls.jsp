<%-- 
    Document   : urls
    Created on : 10-May-2012, 11:41:23
    Author     : pszgp

--%>
<!--%@include file="header.jsp" %-->        
<!--%@include file="leftmenu.jsp" %-->

<%@include file="include.jsp"%>
             
        <h3>Urls:</h3>             
        
        <%@include file="tagcloudurls.jsp"%>
        
        <!--script>
            
        function setup(dataUrls) {

            //pszgp: add the urls to the tag, then call the existing tagcloud function 
            var tc = TagCloud.create();

            if (dataUrls!=null)
            {
                var data = [];
                if (dataUrls.indexOf("{")==0)
                {
                    dataUrls = dataUrls.substring(1, dataUrls.length-1); 
                    data = dataUrls.split(", ");
                }
                for (var i=0;i<data.length;i++)
                {
                    var url = {};
                    var name = data[i];
                    var occur = data[i];

                    name = name.substring(0, name.indexOf("="));

                    occur = occur.substring(occur.indexOf("=")+1, occur.length);
                    occur = +occur;

                    url.name = name;
                    url.occur = occur;
                    
                    //tc.add(url.name, url.occur*10, url.name, Date.parse('0000/00/00 00:00:00'));

                }
            }
            //alert(data);
            
     
            
            //tc.loadEffector('CountSize').base(10).range(5);
            //tc.loadEffector('DateTimeColor');

            tc.setup('urlstagcloud');
            alert(tc.toHTML());
            var element = document.getElementById('urlstagcloudphph');
            tc.setup('urlstagcloud');
        }
        //alert("${urlsOccur}");
        setup("${urlsOccur}");
        </script>
        <!--div id="urlstagcloud"></div-->
        
        
        <!--/script>
<h1 onclick="setup();">TagCloud</h1>
<div id="mytagcloud"></div!--> 
        <!--%@include file="tagcloudurls.jsp" %--> 
                
        <h3>Urls occurences: </h3><br/>
        <table>
            <tr style="background-color: #64A0DE;""><th>Device</th><th>Visited website</th><th>Frequency</th></tr>
            <c:set var="rowCount" value="0"/>
            <c:forEach var="device" items="${urls}">
                <c:set var="rowCount" value="${rowCount+1}"/>
                <c:choose>
                    <c:when test='${(rowCount)%2 eq 0}'><tr style="background-color: #64A0DE;"></c:when>
                    <c:otherwise><tr style=""></c:otherwise>  
                </c:choose>           
                <c:forEach var="uri" items="${device.value}">
                    <tr><td>${device.key}</td><td>${uri.key}</td><td>${uri.value}</td></tr>
                </c:forEach>
            </c:forEach>    
        </table>
        <br/>     
        
        
        </div>
               
    </body>
</html>
