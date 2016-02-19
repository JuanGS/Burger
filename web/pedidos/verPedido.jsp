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
        
        <title>Ver pedido</title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <h1>Pedido</h1>
        
        <hr/>
        <s:label>Número de mesa: </s:label><s:property value="pedido.numeroMesa"/>
        <hr/>
        
        <div id="divListaPedido">
            <div class="table-responsive">
                <table id="tablaPedido" class="table table-striped table-bordered">
                    <caption>Pedido</caption>
                        <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Unidades</th>     
                            <th>Eliminar</th>                                                     
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
            <s:form id="formVolver" action="pedidos">
                <input type="hidden" name="operacion" value="vuelta"/>
                <button type="submit" class="btn btn-default btn-sm">Volver</button>
            </s:form>            
        </div>            
        
        <jsp:include page="../footer.jsp"/>       
        <script src="../Burger/pedidos/verPedido.js"></script>
        
    </body>
</html>
