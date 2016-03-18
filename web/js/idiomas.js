/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

if(sessionStorage.getItem("idioma") === null) { //Sino hay definido ningun idoma
    sessionStorage.setItem("idioma", "es"); //Añadimos un valor de idioma por defecto
}

function cambiarIdioma(siglas) {
    sessionStorage.setItem("idioma", siglas); 
}

/*
 * pedidos.js
 */

//es
var NUMERO_MESA_OBLIGATORIO_es = "Número de mesa obligatorio";
var PEDIDO_VACIO_es = "El pedido no tiene ningún producto";
var LIMPIAR_PEDIDO_es = "¿Limpiar el pedido actual?";
var BOTON_LIMPIAR_es = "Limpiar";
var BOTON_CANCELAR_es = "Cancelar";
var PEDIDO_REINICIADO_es = "Pedido reiniciado";
var OPERACION_CANCELADA_es = "Operación cancelada. Continua con el pedido";

//en
var NUMERO_MESA_OBLIGATORIO_en = "Table number required";
var PEDIDO_VACIO_en = "The order does not have any product";
var LIMPIAR_PEDIDO_en = "Does cleaning the current order?";
var BOTON_LIMPIAR_en = "Clean";
var BOTON_CANCELAR_en = "Cancel";
var PEDIDO_REINICIADO_en = "Order restarts";
var OPERACION_CANCELADA_en = "Operation Cancelled. Continuing with the order";

/*
 * datosRestaurante.js
 */

//es
var NO_MODIFICADO_VALOR_es = "No se ha modificado ningun valor";

//en
var NO_MODIFICADO_VALOR_en = "It has not changed any value";