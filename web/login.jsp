<%-- 
    Document   : login
    Created on : 18-feb-2016, 11:55:36
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp"/>  

        <style type="text/css">
            .errors li{ 
                list-style: none; 
            }
        </style>
        
        <title>Login</title>
    </head>
    <body>

        <div id="divFormAlta">
            <form action="Login" method="POST">
                <fieldset>
                    <legend>Validar usuario</legend>
                    <div id="divUsuario" class="form-group">
                        <label class="control-label">Usuario</label>
                        <input type="text" class="form-control" name="usuario" value="<s:property value="usuario" />"  placeholder="Usuario" required/>
                    </div>
                    <div id="divPassword" class="form-group">
                        <label class="control-label">Contraseña</label>
                        <input type="password" name="password" class="form-control" placeholder="Contraseña" required/>  
                    </div>    
                    <div class="form-group" style="float: left;">
                        <s:submit class="btn btn-default btn-sm" value="Validar"/>
                        <s:reset class="btn btn-default btn-sm" value="Reiniciar"/>
                    </div>                
                </fieldset>
            </form>
        </div>  

        <s:if test="hasActionErrors()">
            <div class="errors alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <s:actionerror/>
            </div>
        </s:if>                    
                    
        <jsp:include page="footer.jsp"/>      

    </body>
</html>
