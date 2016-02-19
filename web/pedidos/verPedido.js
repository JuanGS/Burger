/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var arrayPedido = [];

function iniciar() {
    
    //Recuperamos los datos de la sesion
    recuperarDatosSesion();  

    document.forms['formVolver'].addEventListener('submit', previoPeticion);  
}

function previoPeticion(evObject) {
    evObject.preventDefault(); //Anulamos la acción de defecto   

    //Guardamos los datos en sesion
    guardarDatosSesion();
    
    document.forms['formVolver'].submit();//Se envía el formulario
}

function recuperarDatosSesion() {
    //Recuperamos el valor de arrayPedido de la sesion
    if (sessionStorage.getItem('arrayPedido') !== null) {
        arrayPedido = JSON.parse(sessionStorage.getItem('arrayPedido'));
    }
}

function guardarDatosSesion() {
    //Guardamos el valor de arrayPedido en sesion
    sessionStorage.setItem('arrayPedido', JSON.stringify(arrayPedido));
}

function eliminarProductoPedido(idFila, elementosABorrar) {
    //Obtener el numero de fila (posicion en la tabla) del idFila.
    //En el estado inicial la fila sera igual al idFila, es decir:
    //idFila = 0 estara en la row 0, idFila = 1 estara en la row 1, etc etc
    //pero cuando comencemos a borrar, por ejemplo, la row 0 ahora idFila = 1 pasara a estar en la row 0 de la tabla
    //Por esta razon necesitamos identificar la row del idFila que ha seleccionado el usuario.
    var cuerpoTabla = document.getElementById('tablaPedido').tBodies[0]; //Obtenemos el cuerpo de la tabla
    var nFilas = cuerpoTabla.rows.length; //Obtenemos el numero de filas
    var posicion = 0; //Varaible para almacenar la posicion del idFila
    for (i = 0; i < nFilas; i++) { //Recorremos las filass de la tabla
        if (cuerpoTabla.rows[i].id == idFila) { //Si el id de una row es igual al idFila
            posicion = i; //Esa es la posicion a eliminar
            break; //Dejamos de iterar
        }
    }

    arrayPedido.splice(posicion,elementosABorrar); //Eliminamos del array   

    cuerpoTabla.deleteRow(posicion); //Eliminamos de la tabla
    //Si es una hamburguesa y tiene elementos extra
    if(elementosABorrar > 1) {
        for(i=0; i<elementosABorrar-1; i++) { //Le quitamos uno porque uno de los elementos era la hamburguesa    
            cuerpoTabla.deleteRow(posicion); //La posicion no la incrementamos porque al borrar un elemento la misma posicion tendra un nuevo valor
        }
    }   
}

function cambiarUnidades(posicion, unidades) {
    arrayPedido[posicion].unidades = unidades;
}

addEventListener('load', iniciar);