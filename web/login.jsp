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
               
        <title><s:text name="login.title"/></title>
        <s:head/>
    </head>
    <body>        
        
        <div style="float: right">
            <s:include value="locales.jsp" />
        </div>

        <div id="divFormAlta" style="clear: both">
            <s:form action="Login" namespace="/" method="POST" validate="true">
                <fieldset>
                    <legend><s:text name="login.form"/></legend>
                    <div id="divUsuario" class="form-group">
                        <label class="control-label"><s:text name="login.usuario"/></label>
                        <s:textfield cssClass="form-control" name="usuario"/>
                    </div>
                    <div id="divPassword" class="form-group">
                        <label class="control-label"><s:text name="login.password"/></label>
                        <s:textfield cssClass="form-control" name="password" type="password"/>
                    </div>    
                    <div class="form-group" style="float: left;">
                        <button class="btn btn-default btn-sm"><s:text name="login.validar"/></button>
                        <button type="button" class="btn btn-default btn-sm" onclick="reiniciarCampos()"><s:text name="login.reiniciar"/></button>
                    </div>  
                </fieldset>
            </s:form>
        </div> 

        <s:if test="hasActionErrors()">
            <div class="errors alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <s:actionerror/>
            </div>
        </s:if>                    
                    
        <jsp:include page="footer.jsp"/>
        <script src="login.js"></script>
        
    </body>
</html>