<%-- 
    Document   : urls
    Created on : 10-May-2012, 11:41:23
    Author     : pszgp

--%>
<%@include file="header.jsp" %>        
<%@include file="leftmenu.jsp" %>

     <div id="users_content">  

        <h3>Urls:<br/></h3>
        <table>
            <tr><th>Device ip</th><th>Url</th><th>Accessed</th></tr>
            <c:forEach var="uri" items="${urls}">
                <tr><td>${uri.saddr}</td><td>${uri.hst}${uri.uri}</td><td>${uri.last}<td></td>
            </c:forEach>
        </table>

        </div>
    </body>
</html>
