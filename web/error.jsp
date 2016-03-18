<%-- 
    Document   : error
    Created on : 10-mar-2016, 8:52:29
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="global.error.title"/></title>
    </head>
    <body>
                        
        <h1><s:text name="global.error.mensaje"/></h1>

        <p><s:text name="global.error.informacion"/></p> 

        <strong><ins><s:text name="global.error.nombreExcepcion"/></ins></strong>
        <h4 style="color: red">
            <s:property value="exception" />
        </h4>
        
        <strong><ins><s:text name="global.error.detalleExcepcion"/></ins></strong><br/>
        <h6>
            <s:property value="exceptionStack" />
        </h6>  

    </body>
</html>
