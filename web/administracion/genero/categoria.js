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
    var nombre = document.getElementById('nombre');
    var campo1 = false;

    if (!nombre.validity.valid) {
        document.getElementById('divNombre').className = 'form-group has-error has-feedback';
        divRespuesta.innerHTML += '<strong style="color: red">Nombre obligatorio</strong>';
    } else {
        document.getElementById('divNombre').className = 'form-group';
        campo1 = true;
    }

    if (campo1) {
        return true;
    } else {
        temporizador();
        return false;
    }
}

function altaCategoria() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
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
        var url = "OperacionesGeneroAction.action";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('loadstart', inicio);
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
    var url = "OperacionesGeneroAction.action";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('loadstart', inicio);
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);    
}

//Metodo que se inicia cuando comienza la solicitud
function inicio() {
    divRespuesta.innerHTML = "<div class='sk-fading-circle'>" +
            "<div class='sk-circle1 sk-circle'></div>" +
            "<div class='sk-circle2 sk-circle'></div>" +
            "<div class='sk-circle3 sk-circle'></div>" +
            "<div class='sk-circle4 sk-circle'></div>" +
            "<div class='sk-circle5 sk-circle'></div>" +
            "<div class='sk-circle6 sk-circle'></div>" +
            "<div class='sk-circle7 sk-circle'></div>" +
            "<div class='sk-circle8 sk-circle'></div>" +
            "<div class='sk-circle9 sk-circle'></div>" +
            "<div class='sk-circle10 sk-circle'></div>" +
            "<div class='sk-circle11 sk-circle'></div>" +
            "<div class='sk-circle12 sk-circle'></div>" +
            "</div>";
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrar(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var tabla = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';
        cuerpoTablaCategoria.innerHTML = tabla;        
        
        temporizador(); 
    }
}

//Metodo para limpiar la respuesta despues de X segundos
function temporizador() {
    setTimeout(function () {
        divRespuesta.innerHTML = '';
    }, 3000);
}

addEventListener('load', iniciar);