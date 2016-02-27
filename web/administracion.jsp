<%-- 
    Document   : administracion
    Created on : 28-ene-2016, 21:26:48
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp"/>
        
        <title><s:text name="global.administracion.title"/></title>
    </head>
    <body>

        <s:if test="#session.login != 'true'">
            <jsp:forward page="login.jsp" />
        </s:if>        

        <h1><s:text name="global.administracion.panelAdministracion"/></h1>
        <hr/>
        <s:form action="administracion/OperacionesAdministracion">
            <h2><s:text name="global.administracion.gestionUsuarios"/></h2>
            <button type="submit" class="btn btn-default" value="usuario" name="opcion"><s:text name="global.etiqueta.usuario"/></button>            
            <h2><s:text name="global.administracion.gestionDatosRestaurante"/></h2>
            <button type="submit" class="btn btn-default" value="datosRestaurante" name="opcion"><s:text name="global.etiqueta.datosRestaurante"/></button>
            <h2><s:text name="global.administracion.getionGenero"/></h2>
            <button type="submit" class="btn btn-default" value="categoria" name="opcion"><s:text name="global.etiqueta.categoria"/></button>   
            <button type="submit" class="btn btn-default" value="producto" name="opcion"><s:text name="global.etiqueta.producto"/></button>              
        </s:form>
        <hr/>
        
        <s:form action="Logout">
            <button type="submit" class="btn btn-default"><s:text name="global.etiqueta.cerrarSesion"/></button>
        </s:form>           
        
        <jsp:include page="footer.jsp"/>          
    </body>
</html>
