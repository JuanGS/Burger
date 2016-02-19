<%-- 
    Document   : modificarDatosRestaurante
    Created on : 02-feb-2016, 17:05:15
    Author     : juang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../../head.jsp"/>
        
        <title>Datos restaurante</title>
       
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1>Datos restuarante</h1>
        <hr/>
        
        <h2>Datos local</h2>
        <div id="divFormularioDatosLocal">
            <form id="formDatosLocal">
                <div id="divCif" class="form-group">
                    <label class="control-label" for="cif">CIF</label>
                    <s:textfield id="cif" cssClass="form-control" name="cif" value="%{restaurante.cif}" disabled="true"/>
                </div>
                <div id="divNombre" class="form-group">
                    <label class="control-label" for="nombre">Nombre</label>
                    <s:textfield id="nombre" cssClass="form-control" name="nombre" value="%{restaurante.nombre}" />
                </div>
                <div id="divDireccion" class="form-group">
                    <label class="control-label" for="direccion">Direcccion</label>
                    <s:textfield id="direccion" cssClass="form-control" name="direccion" value="%{restaurante.direccion}" />
                </div>
                <div id="divTelefono" class="form-group">
                    <label class="control-label" for="telefono">Teléfono</label>
                    <s:textfield id="telefono" cssClass="form-control" name="telefono" value="%{restaurante.telefono}" />    
                </div>
                <div style="float: left">
                    <input type="button" id="botonModificarDatosLocal" class="btn btn-default btn-sm" value="Aplicar cambios" disabled="disabled"/>
                </div>
            </form>
            <div id="divRespuestaDatosLocal" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>                
        </div>
            
        <h2>Número de mesas</h2>
        <div id="divFormularioNumeroMesas">
            <form id="formNumeroMesas">
                <div id="divNumeroMesas" class="form-group">
                    <label class="control-label" for="numeroMesas">Número de mesas</label>
                    <s:textfield id="numeroMesas" cssClass="form-control" name="numeroMesas" value="%{numeroMesas}" />    
                </div>
                <div style="float: left">
                    <input type="button" id="botonModificarNumeroMesas" class="btn btn-default btn-sm" value="Aplicar cambios" disabled="disabled"/>
                </div>
            </form>
            <div id="divRespuestaNumeroMesas" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>                
        </div>
             
        <h2>Impuestos</h2>
        <div id="divFormularioImpuestos">
            <form id="formImpuestos">                   
                <s:iterator value="listaImpuestos">
                    <%
                        String nombre = request.getAttribute("nombre").toString();                     
                        if(nombre.equals("iva")) {
                    %>               
                    <div id="divIva" class="form-group">
                        <label class="control-label"><s:property value="nombre"/></label>
                        <s:textfield id="iva" cssClass="form-control" name="iva" value="%{valor}" /> 
                        <s:hidden id="idIva" value="%{idImpuesto}" />
                    </div>                           
                    <%
                        } else if(nombre.equals("servicio mesa")) {
                    %>     
                    <div id="divServicioMesa" class="form-group">
                        <label class="control-label"><s:property value="nombre"/></label>
                        <s:textfield id="servicio mesa" cssClass="form-control" name="servicio mesa" value="%{valor}" />
                        <s:hidden id="idServicioMesa" value="%{idImpuesto}" />
                    </div>  
                    <%
                        }
                    %>   
                </s:iterator>  
                <div style="float: left">
                     <input type="button" id="botonModificarImpuestos" class="btn btn-default btn-sm" value="Aplicar cambios" disabled="disabled"/>
                </div>               
            </form>
            <div id="divRespuestaImpuestos" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>             
        </div>          
        
        <hr/>
        <s:form action="administracion">
            <button type="submit" class="btn btn-default btn-sm">Volver</button>
        </s:form>      
        
        <jsp:include page="../../footer.jsp"/>
        <script src="../Burger/administracion/datosRestaurante/datosRestaurante.js"></script>
    </body>
</html>
