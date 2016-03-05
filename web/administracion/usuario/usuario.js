/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var divRespuesta;

//Metodo que se ejecuta al cargar la pagina
function iniciar() {
    asignarListenerInput();
    cuerpoTablaUsuario = document.getElementById('cuerpoTablaUsuario');    
    divRespuesta = document.getElementById('divRespuesta');
    var botonAlta = document.getElementById('botonAlta');
    botonAlta.addEventListener('click', altaUsuario);
}

//Metodo para asignar escuchas a los input de los formularios
function asignarListenerInput() {
    //Escuchas para limpiar el texto de respuesta cuando un input coja el foco
    document.getElementById('usuario').addEventListener('focus', focoObtenido);   
    document.getElementById('password').addEventListener('focus', focoObtenido);    
}

//Metodo que se ejecuta cuando un input coja el foco para limpiar los div de respuesta
function focoObtenido() {
    document.getElementById('divRespuesta').innerHTML = '';
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {
    var usuario = document.getElementById('usuario');
    var password = document.getElementById('password');
    var campo1 = false;
    var campo2 = false;

    if (!usuario.validity.valid) {
        document.getElementById('divUsuario').className = 'form-group has-error has-feedback';
        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuesta.innerHTML = '<strong style="color: red;">'+USUARIO_OBLIGATORIO_es+'</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuesta.innerHTML = '<strong style="color: red;">'+USUARIO_OBLIGATORIO_en+'</strong>';
        } 
    } else {
        document.getElementById('divUsuario').className = 'form-group';
        campo1 = true;
    }

    if (!password.validity.valid || password.value.length > 8) {
        document.getElementById('divPassword').className = 'form-group has-error';
        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuesta.innerHTML = '<strong style="color: red;">'+PASSWORD_OBLIGATORIO_es+'</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuesta.innerHTML = '<strong style="color: red;">'+PASSWORD_OBLIGATORIO_en+'</strong>';
        } 
    } else {
        document.getElementById('divPassword').className = 'form-group';
        campo2 = true;
    }

    if (campo1 && campo2) {
        return true;
    } else {
        temporizador();
        return false;
    }
}

//Metodo para procesar la operacion
function altaUsuario() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
        //Obtenemos los valores de los campos que queremos enviar
        var usuario = document.getElementById('usuario').value;
        var password = document.getElementById('password').value;

        //Creamos una variable con formato JSON
        var usuarioJSON = {"usuario": usuario, "password": password};
        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('operacion', 'altaUsuario');
        operacion.append("usuario", JSON.stringify(usuarioJSON));

        //Creamos la solicitud AJAX
        //Especificamos la action a ejecutar
        var url = "OperacionesUsuario";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('loadstart', inicio);
        solicitud.addEventListener('load', mostrar);
        solicitud.open("POST", url, true);
        solicitud.send(operacion);
    }
}

//Metodo para procesar la operacion
function bajaUsuarioSeleccionado(usuario) {
     //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'bajaUsuario');
    operacion.append("usuario", usuario);

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesUsuario";
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
        cuerpoTablaUsuario.innerHTML = tabla;        
        
        temporizador();        
    }
}

//Metodo para limpiar los campos del formulario
function limpiarCampos() {
    divRespuesta.innerHTML = "";
}

//Metodo para limpiar la respuesta despues de X segundos
function temporizador() {
    setTimeout(function () {
        divRespuesta.innerHTML = '';
    }, 3000);
}

addEventListener('load', iniciar);