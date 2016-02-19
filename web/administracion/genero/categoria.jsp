<%-- 
    Document   : categoria
    Created on : 06-feb-2016, 10:05:24
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../../head.jsp"/>
        
        <title>Categoria</title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1>Categoría</h1>
        <hr/>        
        <div id="divListaCategorias" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption>Categorías</caption>
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Habilitada</th>                            
                        </tr>
                    </thead>                        
                    <tbody id="cuerpoTablaCategoria">
                        <s:iterator value="listaCategoria">
                            <tr>
                                <td style="display:none;"><s:property value="id"/></td>
                                <td><s:property value="nombre"/></td>
                                <%
                                    int idCategoria = Integer.parseInt(request.getAttribute("id").toString());
                                    boolean activoCategoria = Boolean.parseBoolean(request.getAttribute("activo").toString());
                                    if(activoCategoria) {
                                        out.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+idCategoria+", false)' checked></label></div></td>");
                                    } else {
                                        out.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+idCategoria+", true)'></label></div></td>");
                                    }
                                %>                                 
                            </tr>
                        </s:iterator>
                    </tbody>                        
                </table>
            </div>
        </div>
        
        <div id="divFormAlta">
            <form>
                <fieldset>
                    <legend>Alta categoría</legend>
                    <div id="divNombre" class="form-group">
                        <label class="sr-only" for="nombre">Nombre</label>
                        <input type="text" id="nombre" class="form-control" placeholder="Nombre" required/>
                    </div>
                    <div class='checkbox'>
                        <label><input type='checkbox' id="checkAlta" checked>Habilitada</label>
                    </div>
                    <div class="form-group">
                        <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="Alta"/>
                    </div>                    
                </fieldset>
            </form>
        </div>
        
        <div id="divRespuesta"></div>
        
        <hr/>
        <p>Para dar de baja una categoría póngase en contacto con el administrador del sistema.</p>            

        <div style="clear: left">
            <hr/>            
            <s:form action="administracion">
                <button type="submit" class="btn btn-default btn-sm">Volver</button>
            </s:form>            
        </div>        
        
        <jsp:include page="../../footer.jsp"/>       
        <script src="../Burger/administracion/genero/categoria.js"></script>        
    </body>
</html>
