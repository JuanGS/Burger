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
var PEDIDO_REINICIADO_es = "Pedido reiniciado";
var OPERACION_CANCELADA_es = "Operación cancelada. Continua con el pedido";

//en
var NUMERO_MESA_OBLIGATORIO_en = "Table number required";
var PEDIDO_VACIO_en = "The order does not have any product";
var LIMPIAR_PEDIDO_en = "Does cleaning the current order?";
var PEDIDO_REINICIADO_en = "Order restarts";
var OPERACION_CANCELADA_en = "Operation Cancelled. Continuing with the order";

/*
 * usuario.js
 */

//es
var USUARIO_OBLIGATORIO_es = "Usuario obligatorio";
var PASSWORD_OBLIGATORIO_es = "Contraseña obligaoria, longitud [1-8]";

//en
var USUARIO_OBLIGATORIO_en = "User required";
var PASSWORD_OBLIGATORIO_en = "Password required, size [1-8]";

/*
 * datosRestaurante.js
 */

//es
var CIF_OBLIGATORIO_es = "CIF obligatorio";
var NOMBRE_OBLIGATORIO_es = "Nombre obligatorio";
var DIRECCION_OBLIGATORIA_es = "Direccion obligatoria";
var TELEFONO_OBLIGATORIO_es = "Teléfono obligatorio";
var IVA_OBLIGATORIO_es = "IVA obligatorio";
var SERVICIO_MESA_OBLIGATORIO_es = "Servicio de mesa obligatorio";
var NO_MODIFICADO_VALOR_es = "No se ha modificado ningun valor";

//en
var CIF_OBLIGATORIO_en = "CIF required";
var NOMBRE_OBLIGATORIO_en = "Name required";
var DIRECCION_OBLIGATORIA_en = "Address required";
var TELEFONO_OBLIGATORIO_en = "Phone required";
var IVA_OBLIGATORIO_en = "IVA required";
var SERVICIO_MESA_OBLIGATORIO_en = "Dinner set obligatorio";
var NO_MODIFICADO_VALOR_en = "It has not changed any value";

/*
 * producto.js
 */

//es
var PRECIO_OBLIGATORIO_es = "Precio obligatorio";
var DESCRIPCION_OBLIGATORIA_es = "Descripción obligatoria";
var CATEGORIA_OBLIGATORIA_es = "Categoria obligatoria";

//en
var PRECIO_OBLIGATORIO_en = "Price required";
var DESCRIPCION_OBLIGATORIA_en = "Description required";
var CATEGORIA_OBLIGATORIA_en = "Category required";