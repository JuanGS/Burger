<%-- 
    Document   : login
    Created on : 18-feb-2016, 11:55:36
    Author     : juang
--%>

<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
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
        
        <title><s:text name="global.login.title"/></title>
    </head>
    <body>        
        
        <div style="float: right">
            <jsp:include page="locales.jsp" />
        </div>
        
        <div id="divFormAlta" style="clear: both">
            <form action="Login" method="POST">
                <fieldset>
                    <legend><s:text name="global.login.form"/></legend>
                    <div id="divUsuario" class="form-group">
                        <label class="control-label"><s:text name="global.etiqueta.usuario"/></label>
                        <input type="text" class="form-control" name="usuario" value="<s:property value="usuario" />"  placeholder="<s:text name="global.etiqueta.usuario"/>" required/>
                    </div>
                    <div id="divPassword" class="form-group">
                        <label class="control-label"><s:text name="global.etiqueta.password"/></label>
                        <input type="password" name="password" class="form-control" placeholder="<s:text name="global.etiqueta.password"/>" required/>  
                    </div>    
                    <div class="form-group" style="float: left;">
                        <button class="btn btn-default btn-sm"><s:text name="global.etiqueta.validar"/></button>
                        <button type="reset" class="btn btn-default btn-sm"><s:text name="global.etiqueta.reiniciar"/></button>
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
