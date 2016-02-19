/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Categoria;
import administracion.modelo.Impuesto;
import administracion.modelo.Producto;
import administracion.modelo.Restaurante;
import administracion.modelo.Usuario;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;

/**
 * Clase para actuar como Controlador en las accciones referentes a la administracion
 * @author Juan
 */
public class OperacionesAdministracionAction extends ActionSupport{

    private String opcion; //Operacion que quiere realizar el usuario 
    
    //Objetos para montar las JSP
    private List<Usuario> listaUsuarios;
    private Restaurante restaurante;
    private int numeroMesas;    
    private List<Impuesto> listaImpuestos;    
    private List<Categoria> listaCategoria;
    private List<Producto> listaProducto;    
    
    private String navegacion; //Para gestionar la nevegacion del xml
    
    /**
     * Metodo para gestionar las acciones del controlador.
     * @return el resultado de procesar la accion.
     * @throws java.lang.Exception
     */    
    @Override
    public String execute() throws Exception {
  
        //Analizamos la opcion seleccionada por el usuario
        switch(opcion) {
            case "usuario":
                GestorOperacionesUsuario ga = new GestorOperacionesUsuario();
                listaUsuarios = ga.obtenerListaUsuarios();
                navegacion = "USUARIO";
                break;                
            case "datosRestaurante":
                GestorOperacionesDatosRestaurante gdt = new GestorOperacionesDatosRestaurante();
                restaurante = gdt.obtenerDatosRestaurante();
                listaImpuestos = gdt.obtenerImpuestos();
                numeroMesas = gdt.obtenerNumeroMesas();
                navegacion = "DATOS_RESTAURANTE";
                break;
            case "categoria":
                GestorOperacionesGenero gogc = new GestorOperacionesGenero();
                listaCategoria = gogc.obtenerListaCategorias();
                navegacion = "CATEGORIA";
                break;
            case "producto":
                GestorOperacionesGenero gogp = new GestorOperacionesGenero();
                listaCategoria = gogp.obtenerListaCategoriasActivas();                
                listaProducto = gogp.obtenerListaProductos();
                navegacion = "PRODUCTO";
                break;                
        } 
        
        return navegacion;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
    
    public int getNumeroMesas() {
        return numeroMesas;
    }

    public void setNumeroMesas(int numeroMesas) {
        this.numeroMesas = numeroMesas;
    }

    public List<Impuesto> getListaImpuestos() {        
        return listaImpuestos;
    }

    public void setListaImpuestos(List<Impuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public List<Categoria> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<Categoria> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }
}