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
        <jsp:include page="../head.jsp"/>
        
        <title>Administración</title>
    </head>
    <body>

        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        

        <h1>Panel de administración</h1>
        <hr/>
        <s:form action="OperacionesAdministracionAction">
            <h2>Gestión usuarios</h2>
            <button type="submit" class="btn btn-default" value="usuario" name="opcion">Usuario</button>            
            <h2>Gestión datos restaurante</h2>
            <button type="submit" class="btn btn-default" value="datosRestaurante" name="opcion">Datos restaurante</button>
            <h2>Gestión género</h2>
            <button type="submit" class="btn btn-default" value="categoria" name="opcion">Categoría</button>   
            <button type="submit" class="btn btn-default" value="producto" name="opcion">Producto</button>              
        </s:form>
        <hr/>
        
        <s:form action="cerrarSesion">
            <button type="submit" class="btn btn-default">Cerrar sesión</button>
        </s:form>           
        
        <jsp:include page="../footer.jsp"/>          
    </body>
</html>
