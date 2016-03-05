/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Impuesto;
import administracion.modelo.Restaurante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author juang
 */
public class OperacionesDatosRestauranteAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletOutputStream output;   

    @Override
    public String execute() {
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");

        //Configuramos los datos del response
        response.setContentType("text/html; charset=iso-8859-1");
        try {
            output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet      
        } catch (IOException e) {
            System.out.println("Error al obtener el OutputStream: " + e);
        }

        GestorOperacionesDatosRestaurante gdt = new GestorOperacionesDatosRestaurante();
        int resultadoOperacion = 0;

        Gson gson = new GsonBuilder().create();

        switch (operacion) {
            case "modificarDatosLocal":
                String datosLocalRequest = request.getParameter("datosLocal");
                //Reconstruimos el objeto JSON usuario del request
                Restaurante restaurante = gson.fromJson(datosLocalRequest, Restaurante.class);

                //Realizamos la operacion
                resultadoOperacion = gdt.actualizarDatosRestaurante(restaurante);

                try {
                    montarVistaDatosRestaurante(resultadoOperacion, restaurante);
                } catch (IOException e) {
                    System.out.println("Error al montar la vista datos restaurante: " + e);
                }

                break;

            case "modificarNumeroMesas":
                int numeroMesas = Integer.parseInt(request.getParameter("numeroMesas"));
                int numeroMesasInicial = Integer.parseInt(request.getParameter("numeroMesasInicial"));

                //Realizamos la operacion
                resultadoOperacion = gdt.actualizarNumeroMesas(numeroMesasInicial, numeroMesas);

                try {
                    montarVistaNumeroMesas(resultadoOperacion, numeroMesas, numeroMesasInicial);
                } catch (IOException e) {
                    System.out.println("Error al montar la vista numero de mesas: " + e);
                }

                break;

            case "modificarImpuestos":
                String listaImpuestosRequest = request.getParameter("listaImpuestos");
                String listaImpuestosInicialesRequest = request.getParameter("listaImpuestosIniciales");

                //Reconstruimos el objeto JSON usuario del request y obtenemos una lista
                Type listTypeImpuesto = new TypeToken<ArrayList<Impuesto>>() {
                }.getType();
                List<Impuesto> listaImpuestos = gson.fromJson(listaImpuestosRequest, listTypeImpuesto);
                List<Impuesto> listaImpuestosIniciales = gson.fromJson(listaImpuestosInicialesRequest, listTypeImpuesto);

                //Realizamos la operacion
                //Por este motivo tenemos los valores iniciales porque si recibimos 4 impuestos en la lista pero solo se ha modificado 1, solo modificamos ese
                for (int i = 0; i < listaImpuestos.size(); i++) {
                    if (listaImpuestos.get(i).getValor() != listaImpuestosIniciales.get(i).getValor()) {
                        resultadoOperacion = gdt.insertarImpuesto(listaImpuestos.get(i));
                        if (resultadoOperacion != 1) {
                            break;
                        }
                    }
                }

                try {
                    montarVistaImpuestos(resultadoOperacion, listaImpuestos);
                } catch (IOException e) {
                    System.out.println("Error al montar la vista impuestos: " + e);
                }

                break;
        }

        return null; //Devilvemos null para no tener na vista
    }

    private void montarVistaDatosRestaurante(int resultadoOperacion, Restaurante restaurante) throws IOException {
        switch (resultadoOperacion) {
            case 1:
                output.print("<p>" + getText("datosRestaurante.success.realizarOperacion") + "</p>*");
                break;
            default:
                output.print("<p>" + getText("datosRestaurante.error.realizarOperacion") + "</p>*");
                break;
        }

        output.print("<form id='formDatosLocal'>");
        output.print("<div id='divCif' class='form-group'>");
        output.print("<label class='control-label' for='cif'>" + getText("datosRestaurante.cif") + "</label>");
        output.print("<input type='text' id='cif' class='form-control' name='cif' value='" + restaurante.getCif() + "'  disabled='true' />");
        output.print("</div>");
        output.print("<div id='divNombre' class='form-group'>");
        output.print("<label class='control-label' for='nombre'>" + getText("datosRestaurante.nombre") + "</label>");
        output.print("<input type='text' id='nombre' class='form-control' name='nombre' value='" + restaurante.getNombre() + "' />");
        output.print("</div>");
        output.print("<div id='divDireccion' class='form-group'>");
        output.print("<label class='control-label' for='direccion'>" + getText("datosRestaurante.direccion") + "</label>");
        output.print("<input type='text' id='direccion' class='form-control' name='direccion' value='" + restaurante.getDireccion() + "' />");
        output.print("</div>");
        output.print("<div id='divTelefono' class='form-group'>");
        output.print("<label class='control-label' for='telefono'>" + getText("datosRestaurante.telefono") + "</label>");
        output.print("<input type='text' id='telefono' class='form-control' name='telefono' value='" + restaurante.getTelefono() + "' />");
        output.print("</div>");
        output.print("<div style='float: left'>");
        output.print("<input type='button' id='botonModificarDatosLocal' class='btn btn-default btn-sm' value='" + getText("datosRestaurante.aplicarCambios") + "' disabled='disabled'/>");
        output.print("</div>");
        output.print("</form>");
    }

    private void montarVistaNumeroMesas(int resultadoOperacion, int numeroMesas, int numeroMesasInicial) throws IOException {
        switch (resultadoOperacion) {
            case 1:
                output.print("<p>" + getText("datosRestaurante.success.realizarOperacion") + "</p>*");
                break;
            case -1:
                output.print("<p>" + getText("datosRestaurante.error.mesaOcupada") + "</p>*");
                break;
            default:
                output.print("<p>" + getText("datosRestaurante.error.realizarOperacion") + "</p>*");
                break;
        }

        output.print("<form id='formNumeroMesas'>");
        output.print("<div id='divNumeroMesas' class='form-group'>");
        output.print("<label for='numeroMesas'>" + getText("datosRestaurante.numeroMesas") + "</label>");
        if (resultadoOperacion == 1) {
            output.print("<input type='text' id='numeroMesas' class='form-control' name='numeroMesas' value='" + numeroMesas + "' />");
        } else {
            output.print("<input type='text' id='numeroMesas' class='form-control' name='numeroMesas' value='" + numeroMesasInicial + "' />");
        }
        output.print("</div>");
        output.print("<div style='float: left'>");
        output.print("<input type='button' id='botonModificarNumeroMesas' class='btn btn-default btn-sm' value='" + getText("datosRestaurante.aplicarCambios") + "' disabled='disabled'/>");
        output.print("</div>");
        output.print("</form>");
    }

    private void montarVistaImpuestos(int resultadoOperacion, List<Impuesto> listaImpuestos) throws IOException {
        switch (resultadoOperacion) {
            case 1:
                output.print("<p>" + getText("datosRestaurante.success.realizarOperacion") + "</p>*");
                break;
            default:
                output.print("<p>" + getText("datosRestaurante.error.realizarOperacion") + "</p>*");
                break;
        }

        output.print("<form id='formImpuestos'>");
        for (int i = 0; i < listaImpuestos.size(); i++) {
            if (listaImpuestos.get(i).getNombre().equals("iva")) {
                output.print("<div id='divImpuestoIva' class='form-group'>");
                output.print("<label class='control-label'>" + listaImpuestos.get(i).getNombre() + "</label>");
                if (resultadoOperacion == 1) {
                    output.print("<input type='text' id='iva' class='form-control' name='iva' value=" + listaImpuestos.get(i).getValor() + " />");
                } else {
                    output.print("<input type='text' id='iva' class='form-control' name='iva' value=" + listaImpuestos.get(i).getValor() + " />");
                }
                output.print("</div>");
            } else if (listaImpuestos.get(i).getNombre().equals("servicio mesa")) {
                output.print("<div id='divImpuestoServicioMesa' class='form-group'>");
                output.print("<label class='control-label'>" + listaImpuestos.get(i).getNombre() + "</label>");
                if (resultadoOperacion == 1) {
                    output.print("<input type='text' id='servicio mesa' class='form-control' name='servicio mesa' value=" + listaImpuestos.get(i).getValor() + " />");
                } else {
                    output.print("<input type='text' id='servicio mesa' class='form-control' name='servicio mesa' value=" + listaImpuestos.get(i).getValor() + " />");
                }

                output.print("</div>");
            }
        }
        output.print("<div style='float: left'>");
        output.print("<input type='button' id='botonModificarImpuestos' class='btn btn-default btn-sm' value='" + getText("datosRestaurante.aplicarCambios") + "' disabled='disabled'/>");
        output.print("</div>");
        output.print("</form>");
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        this.request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        this.response = hsr;
    }

}
