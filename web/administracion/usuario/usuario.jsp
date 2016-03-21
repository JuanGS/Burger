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
        <title><s:text name="usuario.usuarios"/></title>
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>        
        
        <h1><s:text name="usuario.usuarios"/></h1>
        
        <hr/> 

        <div id="divListaUsuario" style="float: left; margin-right: 50px">
            <div class="table-responsive">
                <table id="tablaUsuarios" class="table table-striped table-bordered">
                    <caption><s:text name="usuario.usuarios"/></caption>
                    <thead>
                        <tr>
                            <th><s:text name="usuario.usuario"/></th>
                            <th><s:text name="usuario.password"/></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody id="cuerpoTablaUsuario">
                        <%
                            int nFila = 2; //La primera fila es la del admin
                        %>
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
                                        out.print("<td><button type='button' class='btn btn-danger has-spinner' onclick='bajaUsuarioSeleccionado("+id+","+ nFila++ +")'>"
                                                + "<span class='spinner'><i class='glyphicon glyphicon-refresh spin'></i></span>"
                                                + "<span class='glyphicon glyphicon-trash'></span>"
                                                + "</button></td>");
                                    }
                                %> 
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>                      
        </div>  
        
        <div id="divFormAlta">
            <form id="formAltaUsuario">
                <fieldset>
                    <legend><s:text name="usuario.altaUsuario"/></legend>
                        <div id="divUsuario" class="form-group" data-toggle="divUsuario" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                            <label class="control-label" for="usuario"><s:text name="usuario.usuario"/></label>
                            <input type="text" id="usuario" class="form-control" placeholder="<s:text name="usuario.usuario"/>" required autocomplete="off"/>
                        </div>
                        <div id="divPassword" class="form-group" data-toggle="divPassword" data-placement="bottom" title="<s:text name="global.error.passwordSize"/>">
                            <label class="control-label" for="password"><s:text name="usuario.password"/></label>
                            <input type="password" id="password" class="form-control" placeholder="<s:text name="usuario.password"/>" required autocomplete="off" maxlength="8"/>                            
                        </div>    
                        <div class="form-group" style="float: left;">
                            <button type="button" id="botonAlta" class="btn btn-default btn-sm has-spinner">
                                <span class="spinner"><i class="glyphicon glyphicon-refresh spin"></i></span>
                                <s:text name="usuario.alta"/>
                            </button>                               
                            <button type="reset" class="btn btn-default btn-sm" onclick="reiniciarCampos()"><s:text name="usuario.reiniciar"/></button>
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
                <button type="submit" class="btn btn-default btn-sm"><s:text name="usuario.volver"/></button>
            </s:form>            
        </div>           
        
        <jsp:include page="../../footer.jsp"/>           
        <script src="../administracion/usuario/usuario.js"></script>        

    </body>
</html>
