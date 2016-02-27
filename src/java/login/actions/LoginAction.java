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

/**
 *
 * @author juang
 */
public class LoginAction extends ActionSupport {
    
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
                addActionMessage(getText("global.etiqueta.usuario") + ": " + usuario);
            } else {
               addActionError(getText("global.error.passwordIncorrecto")); 
            }
        } else {
            addActionError(getText("global.error.usuarioNoEncontrado"));
        }
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