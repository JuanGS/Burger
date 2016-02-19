/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cuerpoTablaProducto;
var divRespuesta;

function iniciar() {
    cuerpoTablaProducto = document.getElementById('cuerpoTablaProducto');
    divRespuesta = document.getElementById('divRespuesta');
    var botonAlta = document.getElementById('botonAlta');    
    botonAlta.addEventListener('click', altaProducto);    
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {
    var nombre = document.getElementById('nombre');
    var precio = document.getElementById('precio'); 
    var descripcion = document.getElementById('descripcion');
    var selectCategoria = document.getElementById('selectCategoria');
    var campo1 = false;
    var campo2 = false;
    var campo3 = false;
    var campo4 = false;    

    if (!nombre.validity.valid) {
        document.getElementById('divNombre').className = 'form-group has-error has-feedback';
        divRespuesta.innerHTML += '<strong style="color: red">Nombre obligatorio</strong><br/>';        
    } else {
        document.getElementById('divNombre').className = 'form-group';
        campo1 = true;
    }
    
    if (!precio.validity.valid) {
        document.getElementById('divPrecio').className = 'form-group has-error has-feedback';
        divRespuesta.innerHTML += '<strong style="color: red">Precio obligatorio</strong><br/>';
    } else {
        document.getElementById('divPrecio').className = 'form-group';
        campo2 = true;
    }
    
    if (!descripcion.validity.valid) {
        document.getElementById('divDescripcion').className = 'form-group has-error has-feedback';
        divRespuesta.innerHTML += '<strong style="color: red">Descripción obligatorio</strong><br/>';
    } else {
        document.getElementById('divDescripcion').className = 'form-group';
        campo3 = true;
    }

    if (selectCategoria.value === "") {
        document.getElementById('divCategoria').className = 'form-group has-error has-feedback';
        divRespuesta.innerHTML += '<strong style="color: red">Categoría obligatorio</strong><br/>';
    } else {
        document.getElementById('divCategoria').className = 'form-group';
        campo4 = true;
    }    

    if (campo1 && campo2 && campo3 && campo4) {
        return true;
    } else {
        temporizador();
        return false;
    }
}

function altaProducto() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
        //Obtenemos los valores de los campos que queremos enviar
        var nombre = document.getElementById('nombre').value;
        var precio = document.getElementById('precio').value; 
        var descripcion = document.getElementById('descripcion').value;
        var categoria = document.getElementById('selectCategoria').value;
        var checkAlta = document.getElementById('checkAlta').checked;

        //Creamos una variable con formato JSON
        var productoJSON = {"nombre": nombre, "precio": precio, "descripcion": descripcion, "categoria": categoria, "activo": checkAlta};

        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('operacion', 'altaProducto');
        operacion.append("producto", JSON.stringify(productoJSON));        

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
    operacion.append('operacion', 'cambiarEstadoProducto');
    operacion.append("idProducto", id);
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

function cargarDatosProducto(id) {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cargarDatosProducto');
    operacion.append("idProducto", id);  

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesGeneroAction.action";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('loadstart', inicio);
    solicitud.addEventListener('load', mostrarModificar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);   
}

function modificarProducto() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
        //Obtenemos los valores de los campos que queremos enviar
        var nombre = document.getElementById('nombre').value;
        var precio = document.getElementById('precio').value; 
        var descripcion = document.getElementById('descripcion').value;
        var categoria = document.getElementById('selectCategoria').value;
        var checkAlta = document.getElementById('checkAlta').checked;

        //Creamos una variable con formato JSON
        var productoJSON = {"nombre": nombre, "precio": precio, "descripcion": descripcion, "categoria": categoria, "activo": checkAlta};

        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('operacion', 'modificarProducto');
        operacion.append("producto", JSON.stringify(productoJSON));        

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

function reiniciar() {
        var nombre = document.getElementById('nombre');
        var precio = document.getElementById('precio'); 
        var descripcion = document.getElementById('descripcion');
        var categoria = document.getElementById('selectCategoria');
        var checkAlta = document.getElementById('checkAlta');   
        
        nombre.disabled = false;
        
        nombre.value = "";
        precio.value = "";
        descripcion.value = "";
        categoria.value = "";
        checkAlta.checked = true;
        
        document.getElementById('botonAlta').disabled = false;
        document.getElementById('botonActualizar').disabled = true;        
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

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';;
        cuerpoTablaProducto.innerHTML = tabla;
        
        temporizador();
        
        iniciar();        
    }
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrarModificar(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var formulario = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';
        document.getElementById('divFormAlta').innerHTML = formulario;
                
        temporizador(); 
        
        iniciar();
    }
}

//Metodo para limpiar la respuesta despues de X segundos
function temporizador() {
    setTimeout(function () {
        divRespuesta.innerHTML = '';
    }, 3000);
}

addEventListener('load', iniciar);