/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Categoria;
import administracion.modelo.Producto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionContext;
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
public class OperacionesGeneroAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletOutputStream output;    
    
    private GestorOperacionesGenero gog = new GestorOperacionesGenero();
    
    @Override
    public String execute() throws IOException {
        
        //Configuramos el objeto para response
        response.setContentType("text/html; charset=iso-8859-1");
        output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet   
        
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");        
               
        int resultadoOperacion;  

        switch(operacion) {
            case "cambiarEstadoCategoria":
                
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                boolean activoCategoria = Boolean.parseBoolean(request.getParameter("activo"));
                
                //Realizamos la operacion
                resultadoOperacion = gog.actualizarEstadoCategoria(idCategoria, activoCategoria);
                       
                try {
                    montarVistaTablaCategoria(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }
                
                break;
                
            case "altaCategoria":
                
                String nombreCategoria = request.getParameter("nombre");
                boolean ckeck = Boolean.parseBoolean(request.getParameter("checkAlta"));
                
                //Realizamos la operacion
                resultadoOperacion = gog.insertarCategoria(nombreCategoria, ckeck);
                
                try {
                    montarVistaTablaCategoria(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }
                
                break;

            case "cambiarEstadoProducto":
                
                int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                boolean activoProducto = Boolean.parseBoolean(request.getParameter("activo"));
                
                //Realizamos la operacion
                resultadoOperacion = gog.actualizarEstadoProducto(idProducto, activoProducto);
                       
                try {
                    montarVistaTablaProducto(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }
                
                break;
                
            case "altaProducto":
                
                String productoRequest = request.getParameter("producto");
                
                //Reconstruimos el objeto JSON usuario del request
                Gson gson = new GsonBuilder().create();
                Producto producto = gson.fromJson(productoRequest, Producto.class);     
                
                //Realizamos la operacion
                resultadoOperacion = gog.insertarProducto(producto);  
                
                try {
                    montarVistaTablaProducto(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }                
                
                break;
                
            case "cargarDatosProducto":
                
                int idProductoBuscar = Integer.parseInt(request.getParameter("idProducto"));            
                
                //Realizamos la consulta para obtener los datos del producto
                Producto productoEncontrado = gog.obtenerProducto(idProductoBuscar);
           
                try {
                    montarVistaFormularioProducto(productoEncontrado);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }  
                
                break;  
                
            case "modificarProducto":
                
                String productoModificarRequest = request.getParameter("producto");
                
                //Reconstruimos el objeto JSON usuario del request
                Gson gson2 = new GsonBuilder().create();
                Producto productoModificar = gson2.fromJson(productoModificarRequest, Producto.class);   
                
                //Realizamos la operacion
                resultadoOperacion = gog.modificarProducto(productoModificar);  
                
                try {
                    montarVistaTablaProducto(resultadoOperacion);
                } catch(IOException e) {
                    System.out.println("Error al montar la vista: " + e);
                }                   
                
                break;
         }
                
        return null;
    }    

    private void montarVistaTablaCategoria(int resultadoOperacion) throws IOException {
        //Obtenemos la lista categoria
        List<Categoria> listaCategoria = gog.obtenerListaCategorias();

        switch (resultadoOperacion) {
            case 1:
                output.print("<p>"+getText("global.success.realizarOperacion")+"</p>*");
                break;
            case 2:
                output.print("<p>"+getText("global.error.categoriaExiste")+"</p>*");           
                break;
            default:
                output.print("<p>"+getText("global.error.realizarOperacion")+"</p>*");
                break;
        }
        
        for (Categoria categoria : listaCategoria) {
            output.print("<tr>");
            output.print("<td style='display:none;'>" + categoria.getId() + "</td>");
            output.print("<td>" + categoria.getNombre() + "</td>");
            if (categoria.isActivo()) {
                output.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado(" + categoria.getId() + ", false)' checked></label></div></td>");
            } else {
                output.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado(" + categoria.getId() + ", true)'></label></div></td>");
            }
            output.println("</tr>");
        }
    }
    
    private void montarVistaTablaProducto(int resultadoOperacion) throws IOException {
        //Obtenemos la lista productos
        List<Producto> listaProducto = gog.obtenerListaProductos();

        switch (resultadoOperacion) {
            case 1:
                output.print("<p>"+getText("global.success.realizarOperacion")+"</p>*");
                break;
            case 2:
                output.print("<p>"+getText("global.error.productoExiste")+"</p>*");           
                break;
            default:
                output.print("<p>"+getText("global.error.realizarOperacion")+"</p>*");
                break;
        }
        
        for (Producto producto : listaProducto) {
            output.print("<tr>");
            output.print("<td style='display:none;'>" + producto.getId() + "</td>");
            output.print("<td>" + producto.getNombre() + "</td>");
            output.print("<td>" + producto.getPrecio() + "</td>");  
            output.print("<td>" + producto.getCategoria() + "</td>");             
            if (producto.isActivo()) {
                output.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+producto.getId()+", false)' checked></label></div></td>");
            } else {
                output.print("<td><div class='checkbox'><label><input type='checkbox' onclick='cambiarEstado("+producto.getId()+", true)'></label></div></td>");
            }
            output.print("<td><button type='button' id='botonModificar' class='btn btn-default' aria-label='Left Align' onclick=" + "cargarDatosProducto("+ producto.getId()+ ")" + "><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></button></td>");
            output.print("<td>" + producto.getDescripcion() + "</td>");
            output.println("</tr>");
        }          
    }    
    
    private void montarVistaFormularioProducto(Producto producto) throws IOException {
        //Obtenemos la lista categoria
        List<Categoria> listaCategorias = gog.obtenerListaCategorias();
        
        output.print("<p>"+getText("global.success.realizarOperacion")+"</p>*");
        
        output.print("<form>");
        output.print("<fieldset>");
        output.print("<legend>"+getText("global.producto.altaProducto")+"</legend>");         
        output.print("<div id='divNombre' class='form-group'>");
        output.print("<label class='sr-only' for='nombre'>"+getText("global.etiqueta.nombre")+"</label>");
        output.print("<input type='text' id='nombre' class='form-control' placeholder='"+getText("global.etiqueta.nombre")+"' required disabled value='"+producto.getNombre()+"'/>");
        output.print("</div>");  
        output.print("<div id='divPrecio' class='form-group'>");
        output.print("<label class='sr-only' for='precio'>"+getText("global.etiqueta.precio")+"</label>");
        output.print("<input type='text' id='precio' class='form-control' placeholder='"+getText("global.etiqueta.precio")+"' required value='"+producto.getPrecio()+"'/>");        
        output.print("</div>");   
        output.print("<div id='divDescripcion' class='form-group'>");
        output.print("<label class='sr-only' for='descripcion'>"+getText("global.etiqueta.descripcion")+"</label>");
        output.print("<input type='text' id='descripcion' class='form-control' placeholder='"+getText("global.etiqueta.descripcion")+"' required value='"+producto.getDescripcion()+"'/>");          
        output.print("</div>");  
        output.print("<div id='divCategoria' class='form-group'>");
        output.print("<select id='selectCategoria' class='form-control' required>");
        output.print("<option value='"+producto.getCategoria()+"' selected>"+producto.getCategoria()+"</option>");
        for(Categoria categoria : listaCategorias) {
            if(!producto.getCategoria().equals(categoria.getNombre())) { //Para evitar repetir la categoria que tiene el producto en ese momento
                output.print("<option value="+categoria.getNombre()+">"+categoria.getNombre()+"</option>");
            }
        }               
        output.print("</select>");                                      
        output.print("</div>");  
        output.print("<div class='checkbox'>");
        if(producto.isActivo()) {
            output.print("<label><input type='checkbox' id='checkAlta' checked>"+getText("global.etiqueta.habilitada")+"</label>");
        } else {
            output.print("<label><input type='checkbox' id='checkAlta'>"+getText("global.etiqueta.habilitada")+"</label>"); 
        }
        output.print("</div>");  
        output.print("<div class='form-group'>");
        output.print("<input type='button' id='botonAlta' class='btn btn-default btn-sm' value='"+getText("global.etiqueta.alta")+"' disabled style='margin-right: 3px' />");
        output.print("<input type='button' id='botonActualizar' class='btn btn-default btn-sm' value='"+getText("global.etiqueta.actualizar")+"' onclick='modificarProducto()' style='margin-right: 3px' />");        
        output.print("<input type='button' id='botonReiniciar' class='btn btn-default btn-sm' value='"+getText("global.etiqueta.reiniciar")+"' onclick='reiniciar()' />");
        output.print("</div>");                   
        output.print("</fieldset>");
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