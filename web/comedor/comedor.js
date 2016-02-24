/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var divRespuesta;

function iniciar() {
    divRespuesta = document.getElementById('divRespuesta');     
}

function actualizarMesas() {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'actualizarMesas');

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesComedor";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('loadstart', inicio);
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);
}

function cuentaPagada(numeroMesa) {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cuentaPagada');
    operacion.append('numeroMesa', numeroMesa);    

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesComedor";
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
    if (datos.status === 200) {

        document.getElementById('divMesas').innerHTML = datos.responseText; 
        divRespuesta.innerHTML = '';
    }
}

addEventListener('load', iniciar);