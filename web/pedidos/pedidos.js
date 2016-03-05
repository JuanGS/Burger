/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var selectMesas;
var numeroMesa;

var arrayPedido = [];
var arrayExtrasPedido = [];
var ultimaCategoria;

var divRespuestaPedido;
var divRespuestaMesas;
var divListaMesas;
var divRespuesta;

function iniciar() {
    
    //Recuperamos los datos de la sesion
    recuperarDatosSesion();

    divRespuestaPedido = document.getElementById('divRespuestaPedido'); 
    divRespuestaMesas = document.getElementById('divRespuestaMesas');  
    divListaMesas = document.getElementById('divListaMesas');      
    divRespuesta = document.getElementById('divRespuesta');      
    selectMesas = document.getElementById('selectMesas');
    
    selectMesas.addEventListener('change', obtenerNumeroMesaPedido);
        
    document.forms['formProductos'].addEventListener('submit', previoPeticion);    
}

function obtenerNumeroMesaPedido() {
    numeroMesa = selectMesas.value;
}

function obtenerMesasDisponibles() {
    //Creamos un objeto para almacenar los valores
    var operacion = new FormData();
    operacion.append('operacion', 'actualizarMesasLibres');
    
    //Creamos la solicitud AJAX
    //Especificamos la action a ejecutar
    var url = "OperacionesPedidos";
    var solicitud = new XMLHttpRequest();
    solicitud.addEventListener('load', actualizarSelectMesas);
    solicitud.open("POST", url, true);
    solicitud.send(operacion);    
}

function actualizarSelectMesas(e) {
    var datos = e.target;
    if (datos.status == 200) {      
        //Obtenemos la cadena completa de respuesta
        var completa = datos.responseText;
        //Sacamos la primera parte de la cadena (sabemos que es el resultado de la operacion)
        var resultado = completa.split("*",1); //resultado sera un array
        //Sacamos la segunda parte de la cadena (sabemos que es el select)
        var selectMesasNuevo = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divListaMesas.innerHTML = selectMesasNuevo;        

        iniciar();        
    }    
}

function incluirProductoPedido(id, categoria) {

    producto = new Object(); //Producto  que añadimos al arrayPedido
    producto.id = id; //Asignamos el id
    producto.unidades = 1; //Tendremos 1 unidad del producto de inicio

    //Incluir productos al pedido 
    if (arrayPedido.length === 0) { //Primer producto que añadimos al pedido
        arrayPedido.push(producto); //Añadimo el producto al arrayPedido  
    } else { //Resto de productos que añadimos
        if (categoria !== 'Extra') { //Si el producto a añadir no es un extra
            if (ultimaCategoria === 'Extra') { //Pero el ultimo producto que añadimos fue un extra                
                //Quiere decir que hemos completado una hamburguesa con sus extras
                //Añadimos los valores del  array parcial de extras al array del pedido
                for (i = 0; i < arrayExtrasPedido.length; i++) { //Añadimos los valores del  arrayExtrasPedido al arrayPedido                
                    arrayPedido.push(arrayExtrasPedido[i]);
                }
                //Inicializamos el arrayExtrasPedido
                arrayExtrasPedido = [];
            }
        }
        if (categoria === 'Hamburguesa') { //Si queremos añadir una hamburguesa              
            arrayPedido.push(producto); //Añadimos el producto al arrayPedido                
        } else if (categoria === 'Extra') { //Si queremos añadir un extra                
            if (arrayExtrasPedido.length === 0) { //Primer producto extra que añadimos a la hamburguesa
                arrayExtrasPedido.push(producto); //Añadimos el extra al arrayExtrasPedido  
            } else { //Siguientes productos extra que añadimos a la hamburguesa
                indice = -1;
                for (i = 0; i < arrayExtrasPedido.length; i++) { //Recorremos el arrayExtrasPedido
                    if (arrayExtrasPedido[i].id === id) { //Comprobamos si el extra ya esta en la hamburguesa
                        indice = i; //Obtenemos el indice
                        break;
                    }
                }
                if (indice === -1) { //Si el extra no esta en la hamburguesa
                    arrayExtrasPedido.push(producto); //Añadimos el extra al arrayExtrasPedido  
                } else { //Si el extra esta en la hamburguesa
                    arrayExtrasPedido[i].unidades += 1;
                }
            }
        } else { //Si añadimos cualquier otro producto           
            indice = -1;
            for (i = 0; i < arrayPedido.length; i++) { //Recorremos el arrayPedido
                if (arrayPedido[i].id === id) { //Comprobamos si el producto ya esta en el pedido
                    indice = i; //Obtenemos el indice
                    break;
                }
            }
            if (indice === -1) { //Si el producto no esta en el pedido
                arrayPedido.push(producto); //Añadimos el producto al arrayPedido
            } else { //Si el producto esta en el pedido
                arrayPedido[i].unidades += 1;
            }
        }

        //Almacenamos la categoria del producto añadido
        ultimaCategoria = categoria;
    }

    //Habilitar/deshabilitar extas

    if (categoria === 'Hamburguesa') {
        habilitarExtras(false);
    } else if (categoria !== 'Hamburguesa') {
        if (categoria !== 'Extra') {
            habilitarExtras(true);
        }
    }
}

function habilitarExtras(valor) {
    var extras = document.getElementsByClassName('extra');
    
    for(i=0;i<extras.length;i++) {
        extras[i].disabled = valor;
    }
}

//Metodo para validar los campos del formulario (necesario porque utilizamos AJAX)
function validarCampos() {   
    var campo1 = false;
    var campo2 = false;   
 
    if (selectMesas.value === "") {
        document.getElementById('divListaMesas').className = 'form-group has-error has-feedback';
        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuestaMesas.innerHTML = '<strong style="color: red">' + NUMERO_MESA_OBLIGATORIO_es + '</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuestaMesas.innerHTML = '<strong style="color: red">' + NUMERO_MESA_OBLIGATORIO_en + '</strong>';
        }        
    } else {
        document.getElementById('divListaMesas').className = 'form-group';
        divRespuestaMesas.innerHTML = '';
        campo1 = true;
    }     

    if (arrayPedido.length < 1) {
        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuestaPedido.innerHTML = '<strong style="color: red">' + PEDIDO_VACIO_es + '</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuestaPedido.innerHTML = '<strong style="color: red">' + PEDIDO_VACIO_en + '</strong>';
        }           
    } else {
        divRespuestaPedido.innerHTML = '';
        campo2 = true;
    }   

    if (campo1 && campo2) {
        return true;
    } else {
        temporizador();
        return false;
    }
}

function realizarPedido() {
    if(validarCampos()) {
        //Si realizamos el pedido y el ultimo producto añadido fue un extra (tal y como se ha implementado el metodo incluirProductoPedido) necesitamos añadir los producto extra "directamente"
        if (arrayExtrasPedido.length > 0) {
            for (i = 0; i < arrayExtrasPedido.length; i++) {
                arrayPedido.push(arrayExtrasPedido[i]);
            }
        }

        //Creamos un objeto para almacenar los valores
        var operacion = new FormData();
        operacion.append('arrayPedido', JSON.stringify(arrayPedido));
        operacion.append('numeroMesa', numeroMesa);
        operacion.append('operacion', 'realizarPedido');

        //Inicializamos los array
        arrayPedido = [];
        arrayExtrasPedido = [];
        //Inicializamos la ultimaCategoria
        ultimaCategoria = '';
        //Inicializamos el numero de mesa
        numeroMesa = 0;

        //Eliminamos los datos de la sesion
        eliminarDatosSesion();

        //Creamos la solicitud AJAX
        //Especificamos la action a ejecutar
        var url = "OperacionesPedidos";
        var solicitud = new XMLHttpRequest();
        solicitud.addEventListener('loadstart', inicio);
        solicitud.addEventListener('load', mostrar);
        solicitud.open("POST", url, true);
        solicitud.send(operacion);
    }
}

function limpiarPedido() {
    if (sessionStorage.getItem("idioma") === 'es') {
        var respuesta = confirm(LIMPIAR_PEDIDO_es);
    } else if (sessionStorage.getItem("idioma") === 'en') {
        var respuesta = confirm(LIMPIAR_PEDIDO_en);
    }
    if (respuesta === true) {
        //Inicializamos los array
        arrayPedido = [];
        arrayExtrasPedido = [];
        //Inicializamos la ultimaCategoria
        ultimaCategoria = '';

        //Deshabilitamos los productos extras (por si estan activados)
        habilitarExtras(true);

        //Eliminamos los datos de la sesion
        eliminarDatosSesion();

        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuesta.innerHTML = '<strong style="color: blue;">' + PEDIDO_REINICIADO_es + '</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuesta.innerHTML = '<strong style="color: blue;">' + PEDIDO_REINICIADO_en + '</strong>';
        }
    } else {
        if (sessionStorage.getItem("idioma") === 'es') {
            divRespuesta.innerHTML = '<strong style="color: blue;">' + OPERACION_CANCELADA_es + '</strong>';
        } else if (sessionStorage.getItem("idioma") === 'en') {
            divRespuesta.innerHTML = '<strong style="color: blue;">' + OPERACION_CANCELADA_en + '</strong>';
        }
    }

    temporizador();
}

function previoPeticion(evObject) {
    evObject.preventDefault(); //Anulamos la acción de defecto   
    if (validarCampos()) {
        //Si realizamos el pedido y el ultimo producto añadido fue un extra (tal y como se ha implementado el metodo incluirProductoPedido) necesitamos añadir los producto extra "directamente"
        if (arrayExtrasPedido.length > 0) {
            for (i = 0; i < arrayExtrasPedido.length; i++) {
                arrayPedido.push(arrayExtrasPedido[i]);
            }
        }

        //Guardamos los datos en sesion
        guardarDatosSesion();

        //Añadimos los valores a los campos ocultos para pasarlos en la peticion
        document.getElementById('numeroMesa').value = numeroMesa;
        document.getElementById('arrayPedido').value = JSON.stringify(arrayPedido);

        document.forms['formProductos'].submit();//Se envía el formulario
    }
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
        //Sacamos la segunda parte de la cadena (sabemos que es el select)
        var selectMesasNuevo = completa.substring(resultado[0].length+1); //Le sumamos 1 para no contar el sepador * que hemos puesto en la JSP

        divRespuesta.innerHTML = '<strong style="color: blue;">'+resultado+'</strong>';
        divListaMesas.innerHTML = selectMesasNuevo;        

        temporizador();
        
        iniciar();        
    }
}

//Metodo para limpiar la respuesta despues de X segundos
function temporizador() {
    setTimeout(function () {
        divRespuestaPedido.innerHTML = '';
        divRespuestaMesas.innerHTML = '';
        divRespuesta.innerHTML = '';
    }, 3000);
}

function recuperarDatosSesion() {
    //Recuperamos el valor de arrayPedido y el numero de mesa de la sesion
    if (sessionStorage.getItem('arrayPedido') !== null) {
        arrayPedido = JSON.parse(sessionStorage.getItem('arrayPedido'));
    }
    if (sessionStorage.getItem('numeroMesa') !== null) {
        numeroMesa = sessionStorage.getItem('numeroMesa');
    }
}

function guardarDatosSesion() {
    //Guardamos el valor de arrayPedido en sesion
    sessionStorage.setItem('arrayPedido', JSON.stringify(arrayPedido));
    //Guardamos el numero de mesa
    sessionStorage.setItem('numeroMesa', numeroMesa);
}

function eliminarDatosSesion() {
    //Borramos el valor de arrayPedido en sesion
    sessionStorage.removeItem('arrayPedido');
    //Borramos el numero de mesa
    sessionStorage.removeItem('numeroMesa');
}

addEventListener('load', iniciar);