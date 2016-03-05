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
        <link rel="stylesheet" href="../css/estiloGeneral.css"/>            
        <title><s:text name="categoria.categoria"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1><s:text name="categoria.categoria"/></h1>
        <hr/>        
        <div id="divListaCategorias" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption><s:text name="categoria.categorias"/></caption>
                    <thead>
                        <tr>
                            <th><s:text name="categoria.nombre"/></th>
                            <th><s:text name="categoria.habilitada"/></th>                            
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
                    <legend><s:text name="categoria.altaCategoria"/></legend>
                    <div id="divNombre" class="form-group">
                        <label class="sr-only" for="nombre"><s:text name="categoria.nombre"/></label>
                        <input type="text" id="nombre" class="form-control" placeholder="<s:text name="categoria.nombre"/>" required/>
                    </div>
                    <div class='checkbox'>
                        <label><input type='checkbox' id="checkAlta" checked><s:text name="categoria.habilitada"/></label>
                    </div>
                    <div class="form-group">
                        <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="<s:text name="categoria.alta"/>"/>
                    </div>                    
                </fieldset>
            </form>
        </div>
        
        <div id="divRespuesta"></div>
        
        <hr/>
        <p><s:text name="categoria.mensaje.darBaja"/></p>            

        <div style="clear: left">
            <hr/>            
            <s:form action="VolverAdministracion">
                <button type="submit" class="btn btn-default btn-sm"><s:text name="categoria.volver"/></button>
            </s:form>            
        </div>        
        
        <jsp:include page="../../footer.jsp"/>   
        <script src="../js/idiomas.js"></script> 
        <script src="../administracion/genero/categoria.js"></script>        
    </body>
</html>
