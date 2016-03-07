<%-- 
    Document   : comedor
    Created on : 11-feb-2016, 12:44:18
    Author     : juang
--%>

<%@page import="administracion.modelo.Mesa"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../head.jsp"/>     
        <title><s:text name="comedor.title"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <h1><s:text name="comedor.title"/></h1>
        <hr/>
        
        <div id="divMesas">
            <%
                List<Mesa> listaMesas = (List<Mesa>) request.getAttribute("listaMesas");
                for (Mesa mesa : listaMesas) {
                    if (mesa.getEstado().equals("libre")) {
            %>
            <div style="float: left; margin-right: 20px;">
                <img alt="<%= mesa.getNumero()%>" class="img-circle" width="140" height="140" style="background-color: green">
            </div>
            <%
            } else if (mesa.getEstado().equals("ocupada")) {
            %>
            <div style="float: left; margin-right: 20px;">
                <img alt="<%= mesa.getNumero()%>" class="img-circle" width="140" height="140" style="background-color: red"><br/><br/>
                <form action="OperacionesComedor" method="POST">
                    <input type="hidden" id="numeroMesa" name="numeroMesa" value="<%= mesa.getNumero()%>"/>    
                    <button type="submit" class="btn btn-default" name="operacion" value="generarCuenta"><s:text name="comedor.generarCuenta"/></button>           
                </form>
            </div>        
            <%
            } else if (mesa.getEstado().equals("pendiente")) {
            %>
            <div style="float: left; margin-right: 20px;">
                <img alt="<%= mesa.getNumero()%>" class="img-circle" width="140" height="140" style="background-color: yellow"><br/><br/>
                <input type="button" class="btn btn-default" value="<s:text name="comedor.cuentaPagada"/>" onclick="cuentaPagada(<%= mesa.getNumero()%>)"/>              
            </div>        
            <%
            } else {
            %>
            <div style="float: left; margin-right: 20px;">
                <p><strong style="color: red;"><s:text name="comedor.error.cargarMesa"/></strong></p>
            </div>        
            <%
                    }
                }
            %>
        </div>
        
        <div style="clear: both"></div>
        <hr/>
                  
        <div>
            <s:form action="VolverAplicaciones">
                <input type="button" class="btn btn-default" value="<s:text name="comedor.actualizar"/>" onclick="actualizarMesas()"/> 
                <button type="submit" class="btn btn-default"><s:text name="comedor.volver"/></button>
            </s:form>   
        </div>
        
        <div style="float: left; margin-left: 10px;">
            <div id="divRespuesta"></div> 
        </div>       
        
        <jsp:include page="../footer.jsp"/>       
        <script src="../comedor/comedor.js"></script> 
        
    </body>
</html>
