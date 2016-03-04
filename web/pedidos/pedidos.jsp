<%-- 
    Document   : pedidos
    Created on : 07-feb-2016, 12:56:38
    Author     : juang
--%>

<%@page import="pedidos.modelo.Pedido"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../head.jsp"/>
        <link rel="stylesheet" href="../css/estiloGeneral.css"/>
        <title><s:text name="pedidos.title"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <h1><s:text name="pedidos.title"/></h1>
        <hr/>          

        <!-- Miramos si hay un pedido en sesion -->
        <%
            Pedido pedido = (Pedido) session.getAttribute("pedido");
        %>
        
        <div id="divListaMesas" class="form-group">
            <span class="label label-primary"><s:text name="pedidos.numeroMesa"/></span>
            <select id="selectMesas" class="form-control" required>      
                <%
                    //Sino no lo hay o si lo hay pero no tiene mesa asignada
                    if (pedido == null || pedido.getNumeroMesa() == 0) {
                %>
                <option value="" disabled selected><s:text name="pedidos.elijaMesa"/></option>
                <%
                    //Si lo hay
                    //Sacamos el numero de mesa del pedido y se lo asignamos al select como opcion seleccionada
                } else {
                    int numeroMesa = pedido.getNumeroMesa();
                %>
                <option value="<%= numeroMesa%>" selected><%= numeroMesa%></option>
                <%
                    }
                %>                

                <s:iterator value="listaMesas"> 
                    <%
                        //Obtenemos el estado de la mesa y su numero
                        //Si la mesa esta ocupadda no la mostramos
                        //Si el numero de mesa es la del pedido en curso no lo volvemos a mostrar (estaria dos veces)
                        String estadoMesa = request.getAttribute("estado").toString();
                        int numeroMesa = Integer.parseInt(request.getAttribute("numero").toString());
                        if (estadoMesa.equals("libre") && numeroMesa != (pedido != null ? pedido.getNumeroMesa() : 0)) {
                    %>
                    <option value="<s:property value="numero"/>"><s:property value="numero"/></option>
                    <%
                        }
                    %>                 
                </s:iterator>
            </select>
            <button type='button' class='btn btn-default' aria-label='Left Align' onclick='obtenerMesasDisponibles()'><span class='glyphicon glyphicon-refresh' aria-hidden='true'></span></button>
        </div>           

        <div id="divListaProductos" class="form-group">
            <form id="formProductos" action="OperacionesPedidos" method="POST">
                <s:iterator value="listaCategorias">
                    <%
                        String categoriaActual = request.getAttribute("nombre").toString();
                    %>                     
                    <fieldset>
                        <legend><s:property value="nombre"/></legend>
                    </fieldset>      
                    <div class="form-group">
                        <s:iterator value="listaProductos">
                            <%
                                String categoriaProducto = request.getAttribute("categoria").toString();
                                if (categoriaActual.equals(categoriaProducto)) {
                                    if (!"Extra".equals(categoriaProducto)) {
                            %>  
                            <input class="btn btn-default" type="button" value='<s:property value="nombre"/>' onclick="incluirProductoPedido(<s:property value="id"/>, '<s:property value="categoria"/>')" /> 
                            <%
                                    } else if ("Extra".equals(categoriaProducto)) { //Esto es porque los extra de inicio estan disabled
                            %>
                            <input class="extra btn btn-primary" type="button" value='<s:property value="nombre"/>' onclick="incluirProductoPedido(<s:property value="id"/>, '<s:property value="categoria"/>')" disabled/> 
                            <%
                                    }
                                }
                            %>
                        </s:iterator>
                    </div>
                </s:iterator>
                <hr/>
                <button type="submit" class="btn btn-default btn-info" name="operacion" value="verPedido"><s:text name="pedidos.verPedido"/></button>
                <input type="hidden" id="numeroMesa" name="numeroMesa"/>
                <input type="hidden" id="arrayPedido" name="arrayPedido"/>  
                <input type="button" class="btn btn-default btn-warning" value="<s:text name="pedidos.limpiarPedido"/>" onclick="limpiarPedido();"/>
                <input type="button" class="btn btn-default btn-success" value="<s:text name="pedidos.realizarPedido"/>" onclick="realizarPedido();"/>
            </form>
        </div>
  
        <div>
            <div id="divRespuestaPedido"></div> 
            <div id="divRespuestaMesas"></div> 
            <div id="divRespuesta"></div> 
        </div>
        
        <div style="clear: left">
            <hr/>
            <s:form action="VolverAplicaciones">
                <button type="submit" class="btn btn-default btn-sm"><s:text name="pedidos.volver"/></button>
            </s:form>            
        </div>                  
                
        <jsp:include page="../footer.jsp"/>       
        <script src="../pedidos/pedidos.js"></script>
        
    </body>
</html>
