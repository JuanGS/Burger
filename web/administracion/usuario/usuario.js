/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var divRespuesta;

//Metodo que se ejecuta al cargar la pagina
function iniciar() {
    cuerpoTablaUsuario = document.getElementById('cuerpoTablaUsuario');    
    divRespuesta = document.getElementById('divRespuesta');
    var botonAlta = document.getElementById('botonAlta');
    botonAlta.addEventListener('click', altaUsuario);  
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {
    var campo1 = false;
    var campo2 = false;

    if (!document.getElementById('usuario').validity.valid) {
        document.getElementById('divUsuario').className = 'form-group has-error has-feedback';
        $('[data-toggle="divUsuario"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divUsuario').className = 'form-group has-error has-feedback';    
    } else {
        document.getElementById('divUsuario').className = 'form-group';
        campo1 = true;
    }

    if (!document.getElementById('password').validity.valid) {
        document.getElementById('divPassword').className = 'form-group has-error';
        $('[data-toggle="divPassword"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divPassword').className = 'form-group has-error has-feedback';
    } else {
        document.getElementById('divPassword').className = 'form-group';
        campo2 = true;
    }

    if (campo1 && campo2) {
        return true;
    }
}

//Metodo para procesar la operacion
function altaUsuario() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
        $(this).toggleClass('active'); //Activamos el spinner  
        
        //Obtenemos los valores de los campos que queremos enviar
        var usuario = document.getElementById('usuario').value;
        var password = document.getElementById('password').value;

        //Creamos una variable con formato JSON
        var usuarioJSON = {"usuario": usuario, "password": password};
        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('operacion', 'altaUsuario');
        operacion.append('usuario', JSON.stringify(usuarioJSON));

        //Creamos la solicitud AJAX
        //Especificamos la action a ejecutar
        var url = "OperacionesUsuario";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('load', mostrar);
        solicitud.open('POST', url, true);
        solicitud.send(operacion);      
    }
}

//Metodo para procesar la operacion
function bajaUsuarioSeleccionado(usuario, nFila) {    
    
    //Activamos el spinner. Obtenemos de la tabla -> la fila -> la columna 2 y accedemos al boton
    $('#tablaUsuarios tr:nth-child('+nFila+') td:eq(2) button').toggleClass('active');

     //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'bajaUsuario');
    operacion.append("usuario", usuario);

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesUsuario";
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
        cuerpoTablaUsuario.innerHTML = tabla;        
        
        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuesta); //Activamos el temporizador (sera un mensaje corto)
        }       
    }
}

addEventListener('load', iniciar);