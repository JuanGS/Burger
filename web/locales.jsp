<%-- 
    Document   : locales
    Created on : 26-feb-2016, 10:19:51
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp"/>  
    </head>
    <body>
  
        <s:url action="Locale" var="enlaceES">
            <s:param name="request_locale">es</s:param> 
        </s:url>
        <a href="<s:property value="#enlaceES" />" >
            <img src="<s:url value="/img/banderas/bandera_spain.png"/>" width="16" height="16" alt="<s:text name="global.locale.spain"/>" title="<s:text name="global.locale.spain"/>"/>
        </a>         
        
        <s:url action="Locale" var="enlaceEN">
            <s:param name="request_locale">en</s:param> 
        </s:url>
        <a href="<s:property value="#enlaceEN" />" >
            <img src="<s:url value="/img/banderas/bandera_uk.png"/>" width="16" height="16" alt="<s:text name="global.locale.english"/>" title="<s:text name="global.locale.english"/>"/>
        </a>   
                   
    </body>
</html>