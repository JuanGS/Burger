/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptores;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author juang
 */
public class InterceptorAcceso extends AbstractInterceptor {

    private String actionsPublicos; //Este atributo recibira una lista separada por comas con los nombres de los Actions que no serán filtrados
    /*
        Como este parámetro es una cadena, la transformaremos en una lista para usar los métodos de la interface "List"
        para saber si el Action que se este ejecutando es protegido o no. Esta transformación la haremos en el método "init"
    */
    private List<String> actionsSinFiltrar = new ArrayList<>();

    @Override
    public void init() {
        actionsSinFiltrar = Arrays.asList(actionsPublicos.split(","));
    }
    
    @Override
    public String intercept(ActionInvocation ai) throws Exception {

        String result = Action.LOGIN; //Inicializamos a un valor por defecto
        String actionActual = (String)ActionContext.getContext().get(ActionContext.ACTION_NAME); //Obtenemos el nombre del action
        
        /*
            Modificamos la condición del método "execute", para verificar si el nombre del Action 
            que se esta ejecutando actualmente está en la lista de los Actions que no deben ser filtrados:
        */
        if(ai.getInvocationContext().getSession().containsKey("usuario") || actionsSinFiltrar.contains(actionActual)) {
            result = ai.invoke();
        }
        
        return result;
    }

    public String getActionsPublicos() {
        return actionsPublicos;
    }

    public void setActionsPublicos(String actionsPublicos) {
        this.actionsPublicos = actionsPublicos;
    }

    public List<String> getActionsSinFiltrar() {
        return actionsSinFiltrar;
    }

    public void setActionsSinFiltrar(List<String> actionsSinFiltrar) {
        this.actionsSinFiltrar = actionsSinFiltrar;
    }    
}