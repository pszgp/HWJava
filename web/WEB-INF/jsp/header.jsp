<%-- 
    Document   : header
    Created on : 06-May-2012, 17:02:14
    Author     : pszgp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homework Dashboard</title>
        <link rel="stylesheet" href="js/styles.css"/>
    
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>      
        
        <!--script type="text/javascript" 
		src="http://mbostock.github.com/d3/d3.v2.js?2.9.1"></script-->
        <script type="text/javascript" src="js/d3/d3.v2.min.js"></script>

        <link rel="stylesheet" href="js/styles.css"/>
        
    </head>
    
    <body>
        <div id="header">
            <table style="width: 100%">                
                <tr>
                    <td>
                        <table><tr>                                
                                <td>
                                    <a class="a_header" href="http://www.homenetworks.ac.uk" data-tooltip="Homework Project">
                                        <img src="images/homework_logo.png" alt="Homework"/>
                                    </a>
                                </td>                                
                                <td style="color: white;">
                                    <a class="a_header" href="dashboard.htm" data-tooltip="Dashboard">
                                        <h4>Dashboard</h4>                    
                                    </a>
                                </td>
                            </tr>
                        </table>    
                    </td>
                    <td align="right">
                        <img src="images/uon_logo_black_transparent_bg_small.png" alt="Nottingham University"/>
                    </td>
                </tr>
            </table>
        </div>    
