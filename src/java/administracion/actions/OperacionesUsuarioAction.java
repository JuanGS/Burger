/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
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
public class OperacionesUsuarioAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    
    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletOutputStream output;

    GestorOperacionesUsuario ga = new GestorOperacionesUsuario();
    
    private String navegacion; //Para gestionar la nevegacion del xml    
    
    @Override
    public String execute() {
        
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");        
        String usuarioRequest = request.getParameter("usuario");
   
        //Configuramos el objeto para response
        response.setContentType( "text/html; charset=iso-8859-1" );
        
        int resultadoOperacion = 0;             
        
        switch(operacion) {
            case "altaUsuario":
                //Reconstruimos el objeto JSON usuario del request
                Gson gson = new GsonBuilder().create();
                Usuario usuario = gson.fromJson(usuarioRequest, Usuario.class);                
              
                //Realizamos la operacion
                resultadoOperacion = ga.insertarUsuario(usuario);
 
                try {
                    montarVistaUsuarios(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }  

                navegacion = null;
                
                break;
                
            case "bajaUsuario":     
                //Realizamos la operacion
                resultadoOperacion = ga.eliminarUsuario(usuarioRequest);
                
                try {
                    montarVistaUsuarios(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }                 
          
                navegacion = null;
                
                break;
        }
        
        return navegacion;
    }
    
    private void montarVistaUsuarios(int resultadoOperacion) throws IOException {
        output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet
        
        //Obtenemos la lista de usuarios
        List<Usuario> listaUsuarios = ga.obtenerListaUsuarios();        
        
        switch (resultadoOperacion) {
            case 1:
                output.print("<p>"+getText("global.success.realizarOperacion")+"</p>*");
                break;
            case 2:
                output.print("<p>"+getText("global.error.usuarioExiste")+"</p>*");           
                break;
            default:
                output.print("<p>"+getText("global.error.realizarOperacion")+"</p>*");
                break;
        }

        for (Usuario user : listaUsuarios) {
            output.print("<tr>");
            output.print("<td>" + user.getUsuario() + "</td>");
            output.print("<td>" + user.getPassword() + "</td>");
            if (user.getRol().equals("admin")) {
                output.print("<td></td>");
            } else {
                output.print("<td><button type='button' class='btn btn-default' aria-label='Left Align' onclick=" + "bajaUsuarioSeleccionado(" + user.getId() + ")" + "><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>");
            }
            output.print("</tr>");
        }    
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