<%-- 
    Document   : verPedido
    Created on : 09-feb-2016, 10:51:48
    Author     : juang
--%>

<%@page import="pedidos.modelo.Hamburguesa"%>
<%@page import="administracion.modelo.Producto"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../head.jsp"/>
        <link rel="stylesheet" href="../css/estiloGeneral.css"/>        
        <title><s:text name="verPedido.title"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <h1><s:text name="verPedido.pedido"/></h1>
        
        <hr/>
        <label><s:text name="verPedido.numeroMesa"/>: </label><s:property value="pedido.numeroMesa"/>
        <hr/>
        
        <div id="divListaPedido">
            <div class="table-responsive">
                <table id="tablaPedido" class="table table-striped table-bordered">
                    <caption><s:text name="verPedido.pedido"/></caption>
                        <thead>
                        <tr>
                            <th><s:text name="verPedido.nombre"/></th>
                            <th><s:text name="verPedido.unidades"/></th>     
                            <th><s:text name="verPedido.eliminar"/></th>                                                     
                        </tr>
                        <tbody id="cuerpoTablaPedido">
                            <%
                                int idFila = 0;
                                List<Producto> listaProductos = (List<Producto>) request.getAttribute("pedido.listaProductos");
                                for (Producto producto : listaProductos) {
                            %>
                            <tr id="<%= idFila%>">
                                <td><%= producto.getNombre()%></td>
                                <td><input type="number" id="<%= idFila%>" value="<%= producto.getUnidades()%>" min="1" onchange="cambiarUnidades(this.id, this.value)"/></td>
                                    <%
                                        if (producto instanceof Hamburguesa) {
                                            int elementosABorrar = 1; //Como minimo eliminamos la hamburgesa
                                            int nElementosExtra = ((Hamburguesa) producto).getListaProductosExtra().size();
                                            elementosABorrar += nElementosExtra;
                                            
                                            out.print("<td><button type='button' class='btn btn-default' aria-label='Left Align' onclick='eliminarProductoPedido(" + idFila + ", " + elementosABorrar + ")'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>");
                                            out.print("</tr>");
                                            
                                            if(nElementosExtra > 0) { //Quiere decir que la hamburguesa tiene productos extra
                                                for(Producto extra : ((Hamburguesa) producto).getListaProductosExtra()) {
                                                    idFila++;
                                                    out.print("<tr id="+idFila+">");
                                                    out.print("<td><strong style='color: red'>(E) </strong>"+extra.getNombre()+"</td>");
                                                    out.print("<td><input type='number' id="+idFila+" value="+extra.getUnidades()+" min='1' onchange='cambiarUnidades(this.id, this.value)'/></td>");
                                                    out.print("<td><button type='button' class='btn btn-default' aria-label='Left Align' onclick='eliminarProductoPedido(" + idFila + ", 1)'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>");                                                    
                                                    out.print("</tr>");
                                                }
                                            }
                                        } else {
                                            out.print("<td><button type='button' class='btn btn-default' aria-label='Left Align' onclick='eliminarProductoPedido(" + idFila + ", 1)'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>");
                                            out.print("</tr>");
                                        }
                                    %>                                     
                            <%
                                idFila++;
                                }
                            %>    
                        </tbody>
                    </thead> 
                </table>
            </div>
        </div>
        
        <hr/>
        
        <div style="clear: left">
            <s:form id="formVolver" action="OperacionesPedidos">
                <input type="hidden" name="operacion" value="vuelta"/>
                <button type="submit" class="btn btn-default btn-sm"><s:text name="verPedido.volver"/></button>
            </s:form>            
        </div>            
        
        <jsp:include page="../footer.jsp"/>       
        <script src="../pedidos/verPedido.js"></script>
        
    </body>
</html>
