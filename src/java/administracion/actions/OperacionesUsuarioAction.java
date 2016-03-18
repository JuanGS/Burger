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
    public String execute() throws Exception {
        
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");        
        String usuarioRequest = request.getParameter("usuario");
           
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
                    System.out.println("OperacionesUsuarioAction. Error al montar la vista: " + e);
                }  

                navegacion = null;

                break;
                
            case "bajaUsuario":     
                //Realizamos la operacion
                resultadoOperacion = ga.eliminarUsuario(usuarioRequest);
                
                try {
                    montarVistaUsuarios(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("OperacionesUsuarioAction. Error al montar la vista: " + e);
                }                 
          
                navegacion = null;
                
                break;
        }
        
        return navegacion;
    }
    
    private void montarVistaUsuarios(int resultadoOperacion) throws IOException {    
        try {
            //Obtenemos la lista de usuarios
            List<Usuario> listaUsuarios = ga.obtenerListaUsuarios();
                      
            //Configuramos el objeto para response
            response.setContentType("text/html; charset=iso-8859-1");
            output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet                 
            
            if (listaUsuarios != null) { //Si hemos obtenido un listado de usuarios           
                switch (resultadoOperacion) {
                    case 1:
                        output.print("<p>" + getText("usuario.success.realizarOperacion") + "</p>*");
                        break;
                    case 2:
                        output.print("<p>" + getText("usuario.error.usuarioExiste") + "</p>*");
                        break;
                    default:
                        output.print("<p>" + getText("usuario.error.realizarOperacion") + "</p>*");
                        break;
                }                
                
                int nFila = 2; //La primera fila es la del admin
                for (Usuario user : listaUsuarios) {
                    output.print("<tr>");
                    output.print("<td>" + user.getUsuario() + "</td>");
                    output.print("<td>" + user.getPassword() + "</td>");
                    if (user.getRol().equals("admin")) {
                        output.print("<td></td>");
                    } else {
                        output.print("<td><button type='button' class='btn btn-danger has-spinner' onclick='bajaUsuarioSeleccionado("+user.getId()+","+ nFila++ +")'>"
                                + "<span class='spinner'><i class='glyphicon glyphicon-refresh spin'></i></span>"
                                + "<span class='glyphicon glyphicon-trash'></span>"
                                + "</button></td>");                        
                    }
                    output.print("</tr>");
                }
            } else { //Si ha habido algun error y listaUsuarios es null
                output.print("<p><strong style='color: red;'>" + getText("usuario.error.obtenerListaUsuarios") + "</strong></p>");
            }
        } catch (Exception e) {
            System.out.println("OperacionesUsuarioAction. Error al montar la vista resultado: " + e);
            throw e;
        } finally {
            //Cerramos el flujo de respuesta del servlet
            output.flush();
            output.close();
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