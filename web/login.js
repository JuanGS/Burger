/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function reiniciarCampos() {
    var form = document.getElementsByTagName('form');
    var inputs = form[0].getElementsByTagName('input');
    for(i=0; i<inputs.length; i++) {
       inputs[i].value = "";
    }
}