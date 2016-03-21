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
    var botonActualizar = document.getElementById('botonActualizar');    
    botonActualizar.addEventListener('click', actualizarProducto);     
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {
    var campo1 = false;
    var campo2 = false;
    var campo3 = false;
    var campo4 = false;    

    if (!document.getElementById('nombre').validity.valid) {
        $('[data-toggle="divNombre"]').tooltip('show');  
        temporizadorTooltip();           
        document.getElementById('divNombre').className = 'form-group has-error has-feedback';        
    } else {
        document.getElementById('divNombre').className = 'form-group';
        campo1 = true;
    }
    
    if (!document.getElementById('precio').validity.valid) {
        $('[data-toggle="divPrecio"]').tooltip('show');  
        temporizadorTooltip();         
        document.getElementById('divPrecio').className = 'form-group has-error has-feedback';
    } else {
        document.getElementById('divPrecio').className = 'form-group';
        campo2 = true;
    }
    
    if (!document.getElementById('descripcion').validity.valid) {
        $('[data-toggle="divDescripcion"]').tooltip('show');  
        temporizadorTooltip();                
        document.getElementById('divDescripcion').className = 'form-group has-error has-feedback';
    } else {
        document.getElementById('divDescripcion').className = 'form-group';
        campo3 = true;
    }

    if (document.getElementById('selectCategoria').value === "") {
        $('[data-toggle="divCategoria"]').tooltip('show');  
        temporizadorTooltip();                
        document.getElementById('divCategoria').className = 'form-group has-error has-feedback';        
    } else {
        document.getElementById('divCategoria').className = 'form-group';
        campo4 = true;
    }    

    if (campo1 && campo2 && campo3 && campo4) {
        return true;
    }
}

function altaProducto() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios     
        $(this).toggleClass('active'); //Activamos el spinner    
        operacion('altaProducto');    
    }    
}

function actualizarProducto() {
    if (validarCampos()) { //Primero comprobamos que los campos no estan vacios
        $(this).toggleClass('active'); //Activamos el spinner         
        operacion('modificarProducto');
    } 
}

function operacion(operacionSobreProducto) {    
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
        operacion.append('operacion', operacionSobreProducto);
        operacion.append("producto", JSON.stringify(productoJSON));

        //Creamos la solicitud AJAX
        //Especificamos la action a ejecutar
        var url = "OperacionesGenero";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('load', mostrar);
        solicitud.open("POST", url, true);
        solicitud.send(operacion);             
}

function cambiarEstado(id, activo) {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cambiarEstadoProducto');
    operacion.append("idProducto", id);
    operacion.append("activo", activo);    

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesGenero";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);    
}

function cargarDatosProducto(id, nFila) {
    
    //Activamos el spinner. Obtenemos de la tabla -> la fila -> la columna 2 y accedemos al boton
    $('#tablaProductos tr:nth-child('+nFila+') td:eq(5) button').toggleClass('active');    
    
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cargarDatosProducto');
    operacion.append("idProducto", id);  

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesGenero";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', mostrarModificar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);   
}

function reiniciarFormulario() {
    $('#formAltaProducto div input').each(function (){
        $(this).val(''); //Limpiamos el valor de los campos
    });
    $('#formAltaProducto div select').each(function (){
        $(this).val(''); //Limpiamos el valor de los campos
    });    
    
    $('#formAltaProducto #divNombre input').attr('disabled', false); //Habilitamos el campo nombre
    $('#botonAlta').attr('disabled', false); //Mostramos el botona Alta
    $('#botonActualizar').attr('disabled', true); //Ocultamos el boton Actualizar
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

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';;
        cuerpoTablaProducto.innerHTML = tabla;
        
        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuesta); //Activamos el temporizador (sera un mensaje corto)
        }   
        
        iniciar();        
    }
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrarModificar(e) {
    var datos = e.target;
    if (datos.status == 200) {
        
        $('.has-spinner').removeClass('active'); //Desactivamos el spinner        
        
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es la tabla)
        var formulario = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';
        document.getElementById('divFormAlta').innerHTML = formulario;
                
        //Si 'completa' contiene el tag <html>  quiere decir que estamos devolviendo una pagina completa
        if(!completa.includes("<!DOCTYPE html>")) { //Si no lo contiene
            temporizador(divRespuesta); //Activamos el temporizador (sera un mensaje corto)
        }   
        
        iniciar();
    }
}

addEventListener('load', iniciar);