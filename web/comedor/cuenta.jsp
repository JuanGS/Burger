<%-- 
    Document   : cuenta
    Created on : 11-feb-2016, 22:15:29
    Author     : juang
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../head.jsp"/>
        
        <title><s:text name="cuenta.title"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <h1><s:text name="cuenta.title"/></h1>
        
        <hr/>
        
        <span class="label label-default"><s:text name="cuenta.cuenta"/></span><s:property value="cuenta.id" />
        
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <caption><s:text name="cuenta.cuenta"/></caption>
                <thead>
                    <tr>
                        <th><s:text name="cuenta.producto"/></th>
                        <th><s:text name="cuenta.unidades"/></th>     
                        <th><s:text name="cuenta.pvp"/></th>
                        <th><s:text name="cuenta.total"/></th>
                    </tr>
                <tbody>
                    <%
                        int unidades;
                        double precio;
                        double total;
                    %>
                    <s:iterator value="pedido.listaProductos">
                        <tr>
                            <td><s:property value="nombre"/></td>
                            <td><s:property value="unidades"/></td>
                            <td><s:property value="precio"/></td>
                            <%
                                unidades = Integer.parseInt(request.getAttribute("unidades").toString());
                                precio = Double.parseDouble(request.getAttribute("precio").toString());
                                total = unidades * precio;
                                out.println("<td>"+total+"</td>");    
                            %>
                        </tr>
                        <s:if test='categoria.equals("Hamburguesa")'>
                             <s:iterator value="listaProductosExtra">
                                <tr>
                                    <td><strong style="color: red">(E)</strong> <s:property value="nombre"/></td>
                                    <td><s:property value="unidades"/></td>
                                    <td><s:property value="precio"/></td>  
                                    <%
                                        unidades = Integer.parseInt(request.getAttribute("unidades").toString());
                                        precio = Double.parseDouble(request.getAttribute("precio").toString());
                                        total = unidades * precio;
                                        out.println("<td>" + total + "</td>");
                                    %>                                    
                                </tr>
                            </s:iterator>
                        </s:if>
                    </s:iterator>
                </tbody>                
            </table>
        </div>        

        <div>
            <table>
                <tr>
                    <td><span class="label label-default"><s:text name="cuenta.baseImponible"/></span><s:property value="pedido.importe" />€</td>
                    <td></td>
                    <td></td>
                </tr>
                <%
                    double importePedido = Double.parseDouble(request.getAttribute("pedido.importe").toString());
                    double impuesto;
                %>                
                <s:iterator value="listaImpuestos">
                    <%
                        DecimalFormat formato = new DecimalFormat("#.##€");
                        impuesto = (Double.parseDouble(request.getAttribute("valor").toString()) * importePedido) / 100;
                    %>
                    <tr>
                        <td></td>
                        <td><span class="label label-default"><s:property value="nombre" /></span><s:property value="valor" /></td>
                        <td><span class="label label-default"><s:text name="cuenta.impuesto"/> <s:property value="nombre" /></span><%= formato.format(impuesto) %></td>
                    </tr>
                </s:iterator>
                <tr>
                    <td><span class="label label-default"><s:text name="cuenta.total"/></span><s:property value="cuenta.cantidad" />€</td>                   
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
                    
        <hr/>
        
        <div>
            <div style="float: left">
                <form action="OperacionesComedor" method="POST">
                    <input type="hidden" name="operacion" value="cargarMesas"/>
                    <button type="submit" class="btn btn-default"><s:text name="cuenta.volver"/></button>
                </form>  
            </div>
            <div style="float: left; margin-left: 5px;">
                <s:url action="OperacionesComedor" var="enlace">
                    <s:param name="operacion">descargarCuenta</s:param> 
                </s:url>
                <a href="<s:property value="#enlace" />" >
                    <img src="../img/pdf.png" width="32" height="32" alt="<s:text name="cuenta.descargarCuenta"/>" title="Descargar cuenta"/>
                </a>      
            </div>
            <div style="float: left; margin-left: 10px;">
                <div id="divRespuesta"></div> 
            </div>            
        </div>             
            
        <jsp:include page="../footer.jsp"/>    
        
    </body>
</html>
