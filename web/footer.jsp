<%-- 
    Document   : footer
    Created on : 02-feb-2016, 13:29:15
    Author     : juang
--%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<!-- Para gestionar los confirm con Bootstrap -->
<script type="text/javascript" src="../js/bootstrap-tooltip.js"></script>
<script type="text/javascript" src="../js/bootstrap-confirmation.js"></script>

<!-- Para gestionar los idiomas en lado cliente con JavaScript -->
<script src="../js/idiomas.js"></script>

<!-- Para deshabilitar los tooltip por defecto  -->
<script>
  var form = document.querySelector('form');
  form.addEventListener('invalid',function(event){
        event.preventDefault();
  },true);
</script>

<!-- Temporizador que utilizaremos para ocultar los elementos tooltip -->
<script>
    function temporizadorTooltip() {
        setTimeout(function () {
             $('[data-toggle]').tooltip('destroy');
        }, 3000);
    }    
</script>

<!-- Para gestionar los confirm con Bootstrap -->
<script>
    $('[data-toggle=confirmation]').confirmation('hide');      
</script>

<!-- Metodo para limpiar la respuesta despues de X segundos -->
<script>
    function temporizador(divRespuesta) {
        setTimeout(function () {
            divRespuesta.innerHTML = '';
        }, 3000);
    }
</script>

<!-- Metodo para reiniciar los campos vacios de un formulario -->
<script>
    function reiniciarCampos() {
        $(".form-group").removeClass("has-error has-feedback");
    }
</script>