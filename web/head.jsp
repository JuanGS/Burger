<%-- 
    Document   : header
    Created on : 02-feb-2016, 13:28:40
    Author     : juang
--%>

<%@page import="java.net.URL"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">        
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">  

<!-- CSS -->
<%
    URL urlCSS = new URL("http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath() + "/css/estiloGeneral.css");
%>
<link rel="stylesheet" href="<%= urlCSS %>"/>

<!-- favicon -->
<%
    URL urlImagen = new URL("http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath() + "/img/elvis.png");
%>
<link rel="shortcut icon" href="<%= urlImagen %>" type="image/x-icon">