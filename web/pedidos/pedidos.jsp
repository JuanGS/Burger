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
        <title><s:text name="pedidos.title"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../login.jsp" />
        </s:if>        
        
        <div id="divFijo" class="fijo"></div>
        
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
                            <input class="btn btn-default" type="button" value='<s:property value="nombre"/>' onclick="incluirProductoPedido(<s:property value="id"/>, '<s:property value="categoria"/>', '<s:property value="nombre"/>')" /> 
                            <%
                                    } else if ("Extra".equals(categoriaProducto)) { //Esto es porque los extra de inicio estan disabled
                            %>
                            <input class="extra btn btn-primary" type="button" value='<s:property value="nombre"/>' onclick="incluirProductoPedido(<s:property value="id"/>, '<s:property value="categoria"/>', '<s:property value="nombre"/>')" disabled/> 
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
                <button type="button" id="botonLimpiar" class="btn btn-default"><s:text name="pedidos.limpiarPedido"/></button>
                <button type="button" id="botonRealizarPedido" class="btn btn-success has-spinner">
                    <span class="spinner"><i class="glyphicon glyphicon-refresh spin"></i></span>
                        <s:text name="pedidos.realizarPedido"/>
                </button>  
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
        
        <script>
            var titulo;
            var botonLimpiar;
            var botonCancelar;
            if (sessionStorage.getItem("idioma") === 'es') {
                titulo = LIMPIAR_PEDIDO_es;
                botonLimpiar = BOTON_LIMPIAR_es;
                botonCancelar = BOTON_CANCELAR_es;
            } else if (sessionStorage.getItem("idioma") === 'en') {
                titulo = LIMPIAR_PEDIDO_en;
                botonLimpiar = BOTON_LIMPIAR_en;
                botonCancelar = BOTON_CANCELAR_en;
            }
            
            $('#botonLimpiar').css('margin-right', '2px');
            
            $('#botonLimpiar').confirmation({
                title: titulo,
                placement: "bottom",
                popout: "true",
                btnOkLabel: botonLimpiar,
                btnOkIcon: "glyphicon glyphicon-ok-circle",
                btnOkClass: "btn-success",
                btnCancelLabel: botonCancelar,
                btnCancelIcon: "glyphicon glyphicon-ban-circle",
                btnCancelClass: "btn-danger",                
                onConfirm: function(event) { limpiarPedido(true); $('#botonLimpiar').confirmation('hide');  },
                onCancel: function(event) { limpiarPedido(false); }
            }); 
        </script>
        
    </body>
</html>
