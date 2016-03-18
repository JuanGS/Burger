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
        <title><s:text name="datosRestaurante.datosRestaurante"/></title>    
    </head>
    <body>
        
        <s:if test="#session.login != 'true'">
            <jsp:forward page="../../login.jsp" />
        </s:if>         
        
        <h1><s:text name="datosRestaurante.datosRestaurante"/></h1>
        <hr/>
        
        <h2><s:text name="datosRestaurante.datosLocal"/></h2>
        <div id="divFormularioDatosLocal">
            <form id="formDatosLocal">
                <div id="divCif" class="form-group" data-toggle="divCif" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                    <label class="control-label" for="cif"><s:text name="datosRestaurante.cif"/></label>
                    <input type="text" id="cif" class="form-control" name="cif" value="<s:property value="restaurante.cif"/>"  placeholder="<s:text name="datosRestaurante.cif"/>" required autocomplete="off" disabled/>                    
                </div>
                <div id="divNombre" class="form-group" data-toggle="divNombre" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                    <label class="control-label" for="nombre"><s:text name="datosRestaurante.nombre"/></label>
                    <input type="text" id="nombre" class="form-control" name="nombre" value="<s:property value="restaurante.nombre"/>" placeholder="<s:text name="datosRestaurante.nombre"/>" required autocomplete="off"/> 
                </div>
                <div id="divDireccion" class="form-group" data-toggle="divDireccion" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                    <label class="control-label" for="direccion"><s:text name="datosRestaurante.direccion"/></label>
                    <input type="text" id="direccion" class="form-control" name="direccion" value="<s:property value="restaurante.direccion"/>" placeholder="<s:text name="datosRestaurante.direccion"/>" required autocomplete="off"/> 
                </div>
                <div id="divTelefono" class="form-group" data-toggle="divTelefono" data-placement="bottom" title="<s:text name="global.error.telefonoFormato"/>">
                    <label class="control-label" for="telefono"><s:text name="datosRestaurante.telefono"/></label>
                    <input type="tel" id="telefono" class="form-control" name="telefono" value="<s:property value="restaurante.telefono"/>" placeholder="<s:text name="datosRestaurante.telefono"/>" required autocomplete="off"/> 
                </div>
                <div style="float: left">
                    <button type="button" id="botonModificarDatosLocal" class="btn btn-default btn-sm has-spinner" disabled>
                        <span class="spinner"><i class="glyphicon glyphicon-refresh spin"></i></span>
                        <s:text name="datosRestaurante.aplicarCambios"/>
                    </button>                       
                </div>
            </form>
            <div id="divRespuestaDatosLocal" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>                
        </div>
            
        <h2><s:text name="datosRestaurante.numeroMesas"/></h2>
        <div id="divFormularioNumeroMesas">
            <form id="formNumeroMesas">
                <div id="divNumeroMesas" class="form-group" data-toggle="divNumeroMesas" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                    <label class="control-label" for="numeroMesas"><s:text name="datosRestaurante.numeroMesas"/></label>
                    <input type="number" id="numeroMesas" class="form-control" name="numeroMesas" value="<s:property value="numeroMesas"/>" placeholder="<s:text name="datosRestaurante.numeroMesas"/>" required autocomplete="off" step="1"/>                     
                </div>
                <div style="float: left">
                    <button type="button" id="botonModificarNumeroMesas" class="btn btn-default btn-sm has-spinner" disabled>
                        <span class="spinner"><i class="glyphicon glyphicon-refresh spin"></i></span>
                        <s:text name="datosRestaurante.aplicarCambios"/>
                    </button>                       
                </div>
            </form>
            <div id="divRespuestaNumeroMesas" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>                
        </div>
             
        <h2><s:text name="datosRestaurante.impuestos"/></h2>
        <div id="divFormularioImpuestos">
            <form id="formImpuestos">                   
                <s:iterator value="listaImpuestos">
                    <%
                        String nombre = request.getAttribute("nombre").toString();                     
                        if(nombre.equals("iva")) {
                    %>               
                    <div id="divImpuestoIva" class="form-group" data-toggle="divImpuestoIva" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                        <label class="control-label"><s:property value="nombre"/></label>
                        <input type="number" id="iva" class="form-control" name="iva" value="<s:property value="valor"/>" placeholder="<s:text name="datosRestaurante.impuestos"/>" required autocomplete="off" step="0.1"/>                        
                        <s:hidden id="idIva" value="%{idImpuesto}" />
                    </div>                           
                    <%
                        } else if(nombre.equals("servicio mesa")) {
                    %>     
                    <div id="divImpuestoServicioMesa" class="form-group" data-toggle="divImpuestoServicioMesa" data-placement="bottom" title="<s:text name="global.error.campoObligatorio"/>">
                        <label class="control-label"><s:property value="nombre"/></label>
                        <input type="number" id="servicio mesa" class="form-control" name="servicio mesa" value="<s:property value="valor"/>" placeholder="<s:text name="datosRestaurante.impuestos"/>" required autocomplete="off" step="0.1"/>                        
                        <s:hidden id="idServicioMesa" value="%{idImpuesto}" />
                    </div>  
                    <%
                        }
                    %>   
                </s:iterator>  
                <div style="float: left">
                    <button type="button" id="botonModificarImpuestos" class="btn btn-default btn-sm has-spinner" disabled>
                        <span class="spinner"><i class="glyphicon glyphicon-refresh spin"></i></span>
                        <s:text name="datosRestaurante.aplicarCambios"/>
                    </button>                     
                </div>               
            </form>
            <div id="divRespuestaImpuestos" style="float: left; margin-left: 20px;"></div>
            <div style="clear: both"></div>             
        </div>          
        
        <hr/>
        <s:form action="VolverAdministracion">
            <button type="submit" class="btn btn-default btn-sm"><s:text name="datosRestaurante.volver"/></button>
        </s:form>      
        
        <jsp:include page="../../footer.jsp"/>
        <script src="../administracion/datosRestaurante/datosRestaurante.js"></script>
    </body>
</html>
