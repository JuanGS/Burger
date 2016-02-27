<%-- 
    Document   : usuario
    Created on : 07-feb-2016, 10:45:22
    Author     : juang
--%>

<%@page import="java.util.List"%>
<%@page import="administracion.modelo.Usuario"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../../head.jsp"/>
        <link rel="stylesheet" href="../css/estiloGeneral.css"/>       
        <title><s:text name="global.etiqueta.usuarios"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>        
        
        <h1><s:text name="global.etiqueta.usuarios"/></h1>
        
        <hr/> 

        <div id="divListaUsuario" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption><s:text name="global.etiqueta.usuarios"/></caption>
                    <thead>
                        <tr>
                            <th><s:text name="global.etiqueta.usuario"/></th>
                            <th><s:text name="global.etiqueta.password"/></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody id="cuerpoTablaUsuario">
                        <s:iterator value="listaUsuarios">
                            <tr>
                                <td><s:property value="usuario"/></td>
                                <td><s:property value="password"/></td>
                                <%
                                    String rol = request.getAttribute("rol").toString();
                                    if(rol.equals("admin")){
                                       out.print("<td></td>"); 
                                    } else {
                                        int id = Integer.parseInt(request.getAttribute("id").toString()); //Obtenemos el usuario del request0
                                        out.print("<td><button type='button' class='btn btn-default' aria-label='Left Align' onclick=" + "bajaUsuarioSeleccionado("+ id+ ")" + "><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>");
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
                    <legend><s:text name="global.usuario.altaUsuario"/></legend>
                        <div id="divUsuario" class="form-group">
                            <label class="control-label" for="usuario"><s:text name="global.etiqueta.usuario"/></label>
                            <input type="text" id="usuario" class="form-control" placeholder="<s:text name="global.etiqueta.usuario"/>" required/>
                        </div>
                        <div id="divPassword" class="form-group">
                            <label class="control-label" for="password"><s:text name="global.etiqueta.password"/></label>
                            <input type="text" id="password" class="form-control" placeholder="<s:text name="global.etiqueta.password"/>" required/>  
                        </div>    
                        <div class="form-group" style="float: left;">
                            <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="<s:text name="global.etiqueta.alta"/>"/>
                            <input type="reset" class="btn btn-default btn-sm" value="<s:text name="global.etiqueta.reiniciar"/>" onclick="limpiarCampos()" />
                        </div>
                        <div id="divRespuesta" style="float: left; margin-left: 20px;"></div>
                        <div style="clear: both;"></div>                  
                </fieldset>
            </form>
        </div>        
        
        <div id="divRespuesta"></div>
        
        <div style="clear: left">    
            <hr/>
            <s:form action="VolverAdministracion">
                <button type="submit" class="btn btn-default btn-sm"><s:text name="global.etiqueta.volver"/></button>
            </s:form>            
        </div>           
        
        <jsp:include page="../../footer.jsp"/>       
        <script src="../administracion/usuario/usuario.js"></script>        
        
    </body>
</html>
