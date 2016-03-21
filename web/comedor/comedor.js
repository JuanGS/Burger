/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var divRespuesta;

function iniciar() {
    divRespuesta = document.getElementById('divRespuesta'); 
    var botonActualizarMesas = document.getElementById('botonActualizarMesas');
    botonActualizarMesas.addEventListener('click', actualizarMesas);  
    
    //Cuando se hace un submit sobre "Generar Cuenta".
    //1) Habilitamos el spinner
    //2) Añadimos el value y el name del boton como campo del formulario para la peticion
    //3) Hacemos el submit
    $('.formGenerarCuenta').submit(function (eventObj) {
        $(this).find('button').toggleClass('active');
        $(this).append('<input type="hidden" name="' + $('.formGenerarCuenta button').attr('name') + '" value="' + $('.formGenerarCuenta button').attr('value') + '" /> ');
        return true;
    });         
}

function actualizarMesas() {
    $(this).toggleClass('active'); //Activamos el spinner 
    
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'actualizarMesas');

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesComedor";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);
}

function cuentaPagada(obj, numeroMesa) {
    $(obj).toggleClass('active'); //Activamos el spinner 
    
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'cuentaPagada');
    operacion.append('numeroMesa', numeroMesa);    

    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesComedor";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', mostrar);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);    
}

//Metodo que se ejecuta cuando se ha completado la solicitud
function mostrar(e) {
    var datos = e.target;
    if (datos.status === 200) {

        $('.has-spinner').removeClass('active'); //Desactivamos el spinner

        document.getElementById('divMesas').innerHTML = datos.responseText; 
        divRespuesta.innerHTML = '';
        
        iniciar();
    }
}

addEventListener('load', iniciar);