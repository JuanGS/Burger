/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.actions;

import administracion.actions.GestorOperacionesUsuario;
import administracion.modelo.Usuario;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author juang
 */
public class LoginAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
    
    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;    
    
    private String usuario;
    private String password;
    private Usuario usuarioEncontrado;
    private String navegacion;
    
    GestorOperacionesUsuario gou = new GestorOperacionesUsuario();

    @Override
    public String execute() throws Exception {       
        
        Map session = ActionContext.getContext().getSession();
        session.put("login", "true");
        session.put("usuario", usuarioEncontrado.getUsuario());        
        
        if(usuarioEncontrado.getRol().equals("admin")) {
            navegacion = "admin";
        } else {
            navegacion = "usuario";
        }
        return navegacion;
    }

    @Override
    public void validate() {
        usuarioEncontrado = gou.obtenerUsuario(usuario);

        if(usuarioEncontrado != null) {
            if(usuarioEncontrado.getPassword().equals(password)) {
                addActionMessage("Usuario: " + usuario);
            } else {
               addActionError("Password incorrecto"); 
            }
        } else {
            addActionError("Usuario no encontrado");
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}