/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;

/**
 *
 * @author juang
 */
public class LogoutAction extends ActionSupport {

    @Override
    public String execute() throws Exception {
        Map session = ActionContext.getContext().getSession();
        session.remove("login");   
        session.remove("usuario");
        
        return SUCCESS;
    }
    
}
