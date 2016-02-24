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
        <title>Usuarios</title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>        
        
        <h1>Usuarios</h1>
        
        <hr/> 

        <div id="divListaUsuario" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <caption>Usuarios</caption>
                    <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Password</th>
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
                    <legend>Alta usuario</legend>
                        <div id="divUsuario" class="form-group">
                            <label class="control-label" for="usuario">Usuario</label>
                            <input type="text" id="usuario" class="form-control" placeholder="Usuario" required/>
                        </div>
                        <div id="divPassword" class="form-group">
                            <label class="control-label" for="password">Contraseña</label>
                            <input type="text" id="password" class="form-control" placeholder="Contraseña" required/>  
                        </div>    
                        <div class="form-group" style="float: left;">
                            <input type="button" id="botonAlta" class="btn btn-default btn-sm" value="Alta"/>
                            <input type="reset" class="btn btn-default btn-sm" value="Reiniciar" onclick="limpiarCampos()" />
                        </div>
                        <div id="divRespuesta" style="float: left; margin-left: 20px;"></div>
                        <div style="clear: both;"></div>                  
                </fieldset>
            </form>
        </div>        
        
        <div id="divRespuesta"></div>
        
        <div style="clear: left">    
            <hr/>
            <s:form action="Administracion">
                <button type="submit" class="btn btn-default btn-sm">Volver</button>
            </s:form>            
        </div>           
        
        <jsp:include page="../../footer.jsp"/>       
        <script src="../administracion/usuario/usuario.js"></script>        
        
    </body>
</html>
