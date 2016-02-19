/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedidos.modelo;

import java.util.List;
import administracion.modelo.Producto;
import java.util.ArrayList;

/**
 *
 * @author juang
 */
public class Hamburguesa extends Producto {
    
    private List<Extra> listaProductosExtra;

    public Hamburguesa() {
        listaProductosExtra = new ArrayList<>();
    }
    
    public List<Extra> getListaProductosExtra() {
        return listaProductosExtra;
    }

    public void setListaProductosExtra(List<Extra> listaProductosExtra) {
        this.listaProductosExtra = listaProductosExtra;
    }    
}