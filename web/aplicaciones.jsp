<%-- 
    Document   : index
    Created on : 02-ene-2016, 14:02:17
    Author     : Juan
--%>

<%@page import="java.net.URL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp"/>  
        
        <style type="text/css">
            .welcome ul{ 
                padding: 0;
                margin: 0;                
            }               
            .welcome li{ 
                list-style: none;                
            }          
        </style>        
        
        <title><s:text name="global.aplicaciones.title"/></title>
    </head>
    <body>
      
       <s:if test="#session.login != 'true'">
           <jsp:forward page="login.jsp" /> 
        </s:if>
  
        <h1><s:text name="global.aplicaciones.nombreRestaurante"/></h1>

        <s:if test="hasActionMessages()">
            <div class="welcome">
                <s:actionmessage/>
            </div>
        </s:if>
        <s:else>
            <%   
                String usuario = session.getAttribute("usuario").toString();
            %>
            <div class="welcome">
                <s:text name="global.aplicaciones.usuario"/>: <%= usuario %>
            </div>
        </s:else>
        
        <br/>
        <s:form action="pedidos/OperacionesPedidos">
            <%--Indicamos la operacion que queremos ejecutar --%>
            <input type="hidden" name="operacion" value="cargarPedidos" />
            <button type="submit" class="btn btn-default"><s:text name="global.aplicaciones.pedidos"/></button>
        </s:form>
            <br/>
        <s:form action="comedor/OperacionesComedor">
            <%--Indicamos la operacion que queremos ejecutar --%>
            <input type="hidden" name="operacion" value="cargarMesas" />
            <button type="submit" class="btn btn-default"><s:text name="global.aplicaciones.comedor"/></button>            
        </s:form>
        <br/>
        <s:form action="Logout">
            <button type="submit" class="btn btn-default"><s:text name="global.aplicaciones.cerrarSesion"/></button>
        </s:form>            
            
        <jsp:include page="footer.jsp"/>  
    </body>
</html>
