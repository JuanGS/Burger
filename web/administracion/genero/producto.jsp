<%-- 
    Document   : producto
    Created on : 06-feb-2016, 21:20:12
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../../head.jsp"/>
        
        <title>Producto</title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1>Producto</h1>
        <hr/>  
       
        <div id="divFormAlta">
            <form>
                <fieldset>
                    <legend>Alta producto</legend>
                    <div id="divNombre" class="form-group">
                        <label class="sr-only" for="nombre">Nombre</label>
                        <input type="text" id="nombre" class="form-control" placeholder="Nombre" required/>
                    </div>
                    <div id="divPrecio" class="form-group">
                        <label class="sr-only" for="precio">Precio</label>
                        <input type="text" id="precio" class="form-control" placeholder="Precio" required/>
                    </div>  
                    <div id="divDescripcion" class="form-group">
                        <label class="sr-only" for="descripcion">Descripción</label>
                        <input type="text" id="descripcion" class="form-control" placeholder="Descripción" required/>
                    </div>
                    <div id="divCategoria" class="form-group">
                        <select id="selectCategoria" class="form-control" required>
                            <option value="" disabled selected>Elija una categoría...</option>
                            <s:iterator value="listaCategoria">   
                                <option value="<s:property value="nombre"/>"><s:property value="nombre"/></option>
                            </s:iterator>
                        </select>
                    </div>                   
                    <div class='checkbox'>
                        <label><input type='checkbox' id="checkAlta" checked>Habilitada</label>
                    </div>
                    <div class="form-group">
                        <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="Alta" />
                        <input type="button" id="botonReiniciar" class="btn btn-default btn-sm" value="Reiniciar" onclick="reiniciar()" />
                    </div>                    
                </fieldset>
            </form>
        </div>        
        
        <div id="divRespuesta"></div>         
        
        <hr/>
        
        <div id="divListaProducto" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption>Productos</caption>
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Precio</th>     
                            <th>Categoria</th>                            
                            <th>Habilitado</th>
                            <th>Modificar</th>                             
                            <th>Descripción</th>                            
                        </tr>
                    </thead>                        
                    <tbody id="cuerpoTablaProducto">
                        <s:iterator value="listaProducto">
                            <tr>
                                <td style="display:none;"><s:property value="id"/></td>
                                <td><s:property value="nombre"/></td>
                                <td><s:property value="precio"/></td>     
                                <td><s:property value="categoria"/></td>                                 
                                <%
                                    int idProducto = Integer.parseInt(request.getAttribute("id").toString());
                                    boolean activoProducto = Boolean.parseBoolean(request.getAttribute("activo").toString());
                                    if(activoProducto) {
                                        out.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+idProducto+", false)' checked></label></div></td>");
                                    } else {
                                        out.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+idProducto+", true)'></label></div></td>");
                                    }
                                    out.print("<td><button type='button' id='botonModificar' class='btn btn-default' aria-label='Left Align' onclick=" + "cargarDatosProducto("+ idProducto+ ")" + "><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></button></td>");
                                %>  
                                <td><s:property value="descripcion"/></td>                                
                            </tr>
                        </s:iterator>
                    </tbody>                        
                </table>
            </div>
        </div>        
         
        <div style="clear: left">
            <hr/>
            <p>Para dar de baja un producto póngase en contacto con el administrador del sistema.</p>   
        </div>
        
        <div style="clear: left">
            <hr/>
            <s:form action="administracion">
                <button type="submit" class="btn btn-default btn-sm">Volver</button>
            </s:form>            
        </div>            
        
        <jsp:include page="../../footer.jsp"/>       
        <script src="../Burger/administracion/genero/producto.js"></script>         
    </body>
</html>
