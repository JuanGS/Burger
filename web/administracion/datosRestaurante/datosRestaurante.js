/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Variables para obtener el valor inicial de los campos y comparar si se han realizado cambios antes de realizar la operacion
var cifInicial;
var nombreInicial;
var direccionInicial;
var telefonoInicial;
var numeroMesasInicial;
var ivaInicial;
var servicioMesaInicial;

//Div donde daremos el resultado de realizar las operaciones
var divRespuestaDatosLocal;
var divRespuestaNumeroMesas;
var divRespuestaImpuestos;

//Metodo que se ejecuta al cargar la pagina
function iniciar() {
    registrarValores();
    asignarListenerInput();
    divRespuestaDatosLocal = document.getElementById('divRespuestaDatosLocal');
    divRespuestaNumeroMesas = document.getElementById('divRespuestaNumeroMesas'); 
    divRespuestaImpuestos = document.getElementById('divRespuestaImpuestos'); 
    var botonModificarDatosLocal = document.getElementById('botonModificarDatosLocal');
    var botonModificarNumeroMesas = document.getElementById('botonModificarNumeroMesas');
    var botonModificarImpuestos = document.getElementById('botonModificarImpuestos');
    botonModificarDatosLocal.addEventListener('click', leerDatosLocal);
    botonModificarNumeroMesas.addEventListener('click', leerNumeroMesas);  
    botonModificarImpuestos.addEventListener('click', leerImpuestos);
}

//Metodo para obtener los valores inciales y comprobar si han cambiado antes de realizar una operacion
function registrarValores() {
    cifInicial = document.getElementById('cif').value;
    nombreInicial = document.getElementById('nombre').value;
    direccionInicial = document.getElementById('direccion').value;
    telefonoInicial = document.getElementById('telefono').value;
    numeroMesasInicial = document.getElementById('numeroMesas').value;
    ivaInicial = document.getElementById('iva').value;
    servicioMesaInicial = document.getElementById('servicio mesa').value;  
}

//Metodo para asignar escuchas a los input de los formularios
function asignarListenerInput() {
    //Escuchas para habilitar el boton de aplicar cambios
    document.getElementById('cif').addEventListener('input', habilitarBotonModificarDatosLocal);
    document.getElementById('nombre').addEventListener('input', habilitarBotonModificarDatosLocal);
    document.getElementById('direccion').addEventListener('input', habilitarBotonModificarDatosLocal);
    document.getElementById('telefono').addEventListener('input', habilitarBotonModificarDatosLocal); 

    document.getElementById('numeroMesas').addEventListener('input', habilitarBotonModificarNumeroMesas);

    document.getElementById('iva').addEventListener('input', habilitarBotonModificarImpuestos); 
    document.getElementById('servicio mesa').addEventListener('input', habilitarBotonModificarImpuestos); 
}

//Metodos para habilitar los botones del formulario
function habilitarBotonModificarDatosLocal() {
    document.getElementById('botonModificarDatosLocal').disabled = false;
}
function habilitarBotonModificarNumeroMesas() {
    document.getElementById('botonModificarNumeroMesas').disabled = false;
}
function habilitarBotonModificarImpuestos() {
    document.getElementById('botonModificarImpuestos').disabled = false;
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCamposDatosLocal() {
    var campo1 = false;
    var campo2 = false;
    var campo3 = false;
    var campo4 = false;

    if (!document.getElementById('cif').validity.valid) {
        $('[data-toggle="divCif"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divCif').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divCif').className = 'form-group';
        campo1 = true;
    }

    if (!document.getElementById('nombre').validity.valid) {
        $('[data-toggle="divNombre"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divNombre').className = 'form-group has-error has-feedback'; 
    } else {
        document.getElementById('divNombre').className = 'form-group';
        campo2 = true;
    }

    if (!document.getElementById('direccion').validity.valid) {
        $('[data-toggle="divDireccion"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divDireccion').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divDireccion').className = 'form-group';
        campo3 = true;
    }

    if (!(/^\d{9}$/.test(document.getElementById('telefono').value))) {
        $('[data-toggle="divTelefono"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divTelefono').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divTelefono').className = 'form-group';
        campo4 = true;
    }

    if (campo1 && campo2 && campo3 && campo4) {
        return true;
    }
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCamposNumeroMesas() {
    var numeroMesas = document.getElementById('numeroMesas');  
    var campo1 = false;

    if (!numeroMesas.validity.valid || numeroMesas.value < 1) {
        $('[data-toggle="divNumeroMesas"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divNumeroMesas').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divNumeroMesas').className = 'form-group';
        campo1 = true;
    }   
    
    if (campo1) {
        return true;
    } 
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCamposImpuestos() {
    var campo1 = false;
    var campo2 = false;    
    
    if (!document.getElementById('iva').validity.valid) {
        $('[data-toggle="divImpuestoIva"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divImpuestoIva').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divImpuestoIva').className = 'form-group';
        campo1 = true;
    }   
 
    if (!document.getElementById('servicio mesa').validity.valid) {
        $('[data-toggle="divImpuestoServicioMesa"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divImpuestoServicioMesa').className = 'form-group has-error has-feedback';  
    } else {
        document.getElementById('divImpuestoServicioMesa').className = 'form-group';
        campo2 = true;
    }       
    
    if (campo1 && campo2) {
        return true;
    }
}

//Funcion para comprobar si ha habido algun cambio respecto a los valores iniciales
function comprobarCambiosDatosLocal(cif, nombre, direccion, telefono) {
    if (cif === cifInicial && nombre === nombreInicial && direccion === direccionInicial && telefono === telefonoInicial) {
        return false;
    } else {
        return true;
    }
}

function comprobarCambiosNumeroMesas(numeroMesas) {
    if (numeroMesas === numeroMesasInicial) {
        return false;
    } else {
        return true;
    }
}
 
function comprobarCambiosImpuestos(iva, servicioMesa) {
    if (iva === ivaInicial && servicioMesa === servicioMesaInicial) {
        return false;
    } else {
        return true;
    }    
} 
 
function leerDatosLocal() {
    if (validarCamposDatosLocal()) { //Primero validamos los campos
        //Obtenemos los valores de los campos que queremos enviar
        var cif = document.getElementById('cif').value;
        var nombre = document.getElementById('nombre').value;
        var direccion = document.getElementById('direccion').value;
        var telefono = document.getElementById('telefono').value;

        if (comprobarCambiosDatosLocal(cif, nombre, direccion, telefono)) { //Comporbamos que ha cambiado algun valor

            $(this).toggleClass('active'); //Activamos el spinner  

            //Creamos una variable con formato JSON
            var datosLocalJSON = {"cif": cif, "nombre": nombre, "direccion": direccion, "telefono": telefono};
            //Creamos un objeto para almacenar los valores
            var operacion = new FormData();
            operacion.append('operacion', 'modificarDatosLocal');
            operacion.append('datosLocal', JSON.stringify(datosLocalJSON));

            //Creamos la solicitud AJAX
            //Especificamos la action a ejecutar
            var url = "OperacionesDatosRestaurante";
            var solicitud = new XMLHttpRequest();
            solicitud.addEventListener('load', mostrarDatosLocal);
            solicitud.open("POST", url, true);
            solicitud.send(operacion);

        } else {          
            if (sessionStorage.getItem("idioma") === 'es') {
                divRespuestaDatosLocal.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_es + '</strong>';
            } else if (sessionStorage.getItem("idioma") === 'en') {
                divRespuestaDatosLocal.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_en + '</strong>';
            }

            temporizador(divRespuestaDatosLocal);
        }
    }
}

function leerNumeroMesas() {
    if (validarCamposNumeroMesas()) { //Primero validamos los campos
        var numeroMesas = document.getElementById('numeroMesas').value;

        if (comprobarCambiosNumeroMesas(numeroMesas)) { //Comporbamos que ha cambiado algun valor
            
            $(this).toggleClass('active'); //Activamos el spinner             
             
            //Creamos un objeto para almacenar los valores
            var operacion = new FormData();
            operacion.append('operacion', 'modificarNumeroMesas');
            operacion.append('numeroMesas', numeroMesas);
            operacion.append('numeroMesasInicial', numeroMesasInicial);
            //Creamos la solicitud AJAX
            //Especificamos la action a ejecutar
            var url = "OperacionesDatosRestaurante";
            var solicitud = new XMLHttpRequest();
            solicitud.addEventListener('load', mostrarNumeroMesas);
            solicitud.open("POST", url, true);
            solicitud.send(operacion);
        } else {
            if (sessionStorage.getItem("idioma") === 'es') {
                divRespuestaNumeroMesas.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_es + '</strong>';
            } else if (sessionStorage.getItem("idioma") === 'en') {
                divRespuestaNumeroMesas.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_en + '</strong>';
            }
            temporizador(divRespuestaNumeroMesas);
        }
    }
}

function leerImpuestos() {
    if (validarCamposImpuestos()) { //Primero validamos los campos
        var iva = document.getElementById('iva').value;
        var servicioMesa = document.getElementById('servicio mesa').value;

        if (comprobarCambiosImpuestos(iva, servicioMesa)) { //Comporbamos que ha cambiado algun valor
            
            $(this).toggleClass('active'); //Activamos el spinner
            
            var idIva = document.getElementById('idIva').value;
            var idServicioMesa = document.getElementById('idServicioMesa').value;

            //Creamos una variable con formato JSON
            //Pasamos las dos listas para modificar solo los campos que han cambiado
            var impuestosJSON = [
                {"idImpuesto": idIva, "nombre": "iva", "valor": iva},
                {"idImpuesto": idServicioMesa, "nombre": "servicio mesa", "valor": servicioMesa}
            ];
            var impuestosInicialesJSON = [
                {"idImpuesto": idIva, "nombre": "iva", "valor": ivaInicial},
                {"idImpuesto": idServicioMesa, "nombre": "servicio mesa", "valor": servicioMesaInicial}
            ];
            //Creamos un objeto para almacenar los valores
            var operacion = new FormData();
            operacion.append('operacion', 'modificarImpuestos');
            operacion.append('listaImpuestos', JSON.stringify(impuestosJSON));
            operacion.append('listaImpuestosIniciales', JSON.stringify(impuestosInicialesJSON));
            //Creamos la solicitud AJAX
            //Especificamos la action a ejecutar
            var url = "OperacionesDatosRestaurante";
            var solicitud = new XMLHttpRequest();
            solicitud.addEventListener('load', mostrarImpuestos);
            solicitud.open("POST", url, true);
            solicitud.send(operacion);
        } else {
            if (sessionStorage.getItem("idioma") === 'es') {
                divRespuestaImpuestos.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_es + '</strong>';
            } else if (sessionStorage.getItem("idioma") === 'en') {
                divRespuestaImpuestos.innerHTML = '<strong style="color: red;">' + NO_MODIFICADO_VALOR_en + '</strong>';
            }
            temporizador(divRespuestaImpuestos);
        }
    }
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrarDatosLocal(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        $('.has-spinner').removeClass('active'); //Desactivamos el spinner        
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var formulario = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP       
        
        divRespuestaDatosLocal.innerHTML = "<strong style='color: blue;'>"+resultado+"</strong>";
        document.getElementById('formDatosLocal').innerHTML = formulario;
 
        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuestaDatosLocal); //Activamos el temporizador (sera un mensaje corto)
        }        

        iniciar();
    }
}

function mostrarNumeroMesas(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        $('.has-spinner').removeClass('active'); //Desactivamos el spinner         
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var formulario = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP          
        
        divRespuestaNumeroMesas.innerHTML = "<strong style='color: blue;'>"+resultado+"</strong>";  
        document.getElementById('formNumeroMesas').innerHTML = formulario;

        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuestaNumeroMesas); //Activamos el temporizador (sera un mensaje corto)
        } 

        iniciar();
    }
}

function mostrarImpuestos(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        $('.has-spinner').removeClass('active'); //Desactivamos el spinner         
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var formulario = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP  
        
        divRespuestaImpuestos.innerHTML = "<strong style='color: blue;'>"+resultado+"</strong>";          
        document.getElementById('formImpuestos').innerHTML = formulario;

        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuestaImpuestos); //Activamos el temporizador (sera un mensaje corto)
        }  
        
        iniciar();
    }    
}

addEventListener('load', iniciar);