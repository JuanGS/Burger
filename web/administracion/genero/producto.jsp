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
        <title><s:text name="producto.producto"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1><s:text name="producto.producto"/></h1>
        <hr/>  
       
        <div id="divFormAlta">
            <form>
                <fieldset>
                    <legend><s:text name="producto.altaProducto"/></legend>
                    <div id="divNombre" class="form-group">
                        <label class="sr-only" for="nombre"><s:text name="producto.nombre"/></label>
                        <input type="text" id="nombre" class="form-control" placeholder="<s:text name="producto.nombre"/>" required/>
                    </div>
                    <div id="divPrecio" class="form-group">
                        <label class="sr-only" for="precio"><s:text name="producto.precio"/></label>
                        <input type="text" id="precio" class="form-control" placeholder="<s:text name="producto.precio"/>" required/>
                    </div>  
                    <div id="divDescripcion" class="form-group">
                        <label class="sr-only" for="descripcion"><s:text name="producto.descripcion"/></label>
                        <input type="text" id="descripcion" class="form-control" placeholder="<s:text name="producto.descripcion"/>" required/>
                    </div>
                    <div id="divCategoria" class="form-group">
                        <select id="selectCategoria" class="form-control" required>
                            <option value="" disabled selected><s:text name="producto.elijaProducto"/></option>
                            <s:iterator value="listaCategoria">   
                                <option value="<s:property value="nombre"/>"><s:property value="nombre"/></option>
                            </s:iterator>
                        </select>
                    </div>                   
                    <div class='checkbox'>
                        <label><input type='checkbox' id="checkAlta" checked><s:text name="producto.habilitada"/></label>
                    </div>
                    <div class="form-group">
                        <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="<s:text name="producto.alta"/>" />
                        <input type="button" id="botonReiniciar" class="btn btn-default btn-sm" value="<s:text name="producto.reiniciar"/>" onclick="reiniciar()" />
                    </div>                    
                </fieldset>
            </form>
        </div>        
        
        <div id="divRespuesta"></div>         
        
        <hr/>
        
        <div id="divListaProducto" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption><s:text name="producto.productos"/></caption>
                    <thead>
                        <tr>
                            <th><s:text name="producto.nombre"/></th>
                            <th><s:text name="producto.precio"/></th>     
                            <th><s:text name="producto.categoria"/></th>                            
                            <th><s:text name="producto.habilitada"/></th>
                            <th><s:text name="producto.editar"/></th>                             
                            <th><s:text name="producto.descripcion"/></th>                            
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
            <p><s:text name="producto.mensaje.darBaja"/></p>   
        </div>
        
        <div style="clear: left">
            <hr/>
            <s:form action="VolverAdministracion">
                <button type="submit" class="btn btn-default btn-sm"><s:text name="producto.volver"/></button>
            </s:form>            
        </div>            
        
        <jsp:include page="../../footer.jsp"/>  
        <script src="../administracion/genero/producto.js"></script>         
    </body>
</html>
