/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cuerpoTablaCategoria;
var divRespuesta;

//Metodo que se ejecuta al cargar la pagina
function iniciar() {
    cuerpoTablaCategoria = document.getElementById('cuerpoTablaCategoria');
    divRespuesta = document.getElementById('divRespuesta');
    var botonAlta = document.getElementById('botonAlta');
    botonAlta.addEventListener('click', altaCategoria);    
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {
    var campo1 = false;

    if (!document.getElementById('nombre').validity.valid) {
        $('[data-toggle="divNombre"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divNombre').className = 'form-group has-error has-feedback';    
    } else {
        document.getElementById('divNombre').className = 'form-group';
        campo1 = true;
    }

    if (campo1) {
        return true;
    }    
}

function altaCategoria() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios                
        $(this).toggleClass('active'); //Activamos el spinner
        
        //Obtenemos los valores de los campos que queremos enviar
        var nombre = document.getElementById('nombre').value;
        var checkAlta = document.getElementById('checkAlta').checked;

        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('operacion', 'altaCategoria');
        operacion.append("nombre", nombre);
        operacion.append("checkAlta", checkAlta);        

        //Creamos la solicitud AJAX
        //Especificamos la action a ejecutar
        var url = "OperacionesGenero";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('load', mostrar);
        solicitud.open("POST", url, true);
        solicitud.send(operacion);
    }    
}

function cambiarEstado(id, activo) {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cambiarEstadoCategoria');
    operacion.append("idCategoria", id);
    operacion.append("activo", activo);    

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesGenero";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);    
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrar(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        $('.has-spinner').removeClass('active'); //Desactivamos el spinner        
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var tabla = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';
        cuerpoTablaCategoria.innerHTML = tabla;        
        
        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuesta); //Activamos el temporizador (sera un mensaje corto)
        }   
    }
}

addEventListener('load', iniciar);