/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Metodo que se ejecuta al cargar la pagina
function iniciar() {
    var botonValidar = document.getElementById('botonValidar');
    botonValidar.addEventListener('click', validar);    
}

function validar() {
    var campo1 = false;
    var campo2 = false;    

    if (!document.getElementById('usuario').validity.valid) {
        $('[data-toggle="divUsuario"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divUsuario').className = 'form-group has-error has-feedback';    
    } else {
        document.getElementById('divUsuario').className = 'form-group';
        campo1 = true;
    }
    
    if (!document.getElementById('password').validity.valid) {
        $('[data-toggle="divPassword"]').tooltip('show');  
        temporizadorTooltip();
        document.getElementById('divPassword').className = 'form-group has-error has-feedback';    
    } else {
        document.getElementById('divPassword').className = 'form-group';
        campo2 = true;
    }    

    if (campo1 && campo2) {
        $(this).toggleClass('active'); //Activamos el spinner
        return true;
    }    
}

addEventListener('load', iniciar);
