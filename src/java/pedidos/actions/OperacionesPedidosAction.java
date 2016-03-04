/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedidos.actions;

import administracion.actions.GestorOperacionesDatosRestaurante;
import administracion.actions.GestorOperacionesGenero;
import administracion.actions.GestorOperacionesUsuario;
import administracion.modelo.Categoria;
import administracion.modelo.Mesa;
import administracion.modelo.Producto;
import administracion.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import pedidos.modelo.Extra;
import pedidos.modelo.Hamburguesa;
import pedidos.modelo.Pedido;

/**
 *
 * @author juang
 */
public class OperacionesPedidosAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    //Para interactuar con la JSP
    private List<Mesa> listaMesas;
    private List<Producto> listaProductos;
    private List<Categoria> listaCategorias; 
    private Pedido pedido;
    
    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletOutputStream output; 

    //Para gestionar la sesion
    private Map session = ActionContext.getContext().getSession();
    
    //Para gestionar las peticiones a BD
    private final GestorOperacionesDatosRestaurante godr = new GestorOperacionesDatosRestaurante();
    private final GestorOperacionesGenero gog = new GestorOperacionesGenero();
    private final GestorOperacionesUsuario gou = new GestorOperacionesUsuario();
    private final GestorOperacionesPedidos gop = new GestorOperacionesPedidos();
    
    //Para gestionar la nevegacion del xml
    private String navegacion; 
    
    @Override
    public String execute() {
        //Configuramos el objeto para response
        response.setContentType("text/html; charset=iso-8859-1");
        
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");
        
        switch(operacion) {
            
            /*
                Este case solo se ejecutara cuando entremos en la aplicacion de pedidos por primera vez.
                Mientras nos mantengamos dentro de la aplicacion es "autosuficiente"
            */
            
            case "cargarPedidos":
                cargarEstadoInicial();              
                navegacion = "CARGAR_PEDIDO";
                
                break;
                
            case "actualizarMesasLibres":    
                try {
                    //Volvemos a cargar el select de las mesas. Le pasamos un 1 porque nos da igual la parte del resultado de la operacion
                    montarVistaResultado(1);
                } catch (IOException e) {
                    System.out.println("Error al montar la vista resultado: " + e);
                }
                
                navegacion = null;
                
                break;
                
            case "realizarPedido":            
                construirPedido();
                //Una vez contruido el pedido lo añadimos a la BD
                int resultadoOperacion = gop.insertarPedido(pedido);               

                try {
                    //Informamos del resultado
                    montarVistaResultado(resultadoOperacion);
                } catch (IOException e) {
                    System.out.println("Error al montar la vista resultado: " + e);
                }

                //Eliminamos la informacion de la sesion
                session.remove("listaMesas");
                session.remove("listaProductos");
                session.remove("listaCategorias");
                session.remove("pedido");

                cargarEstadoInicial();

                navegacion = null;
                
                break;
                
            case "verPedido":
                
                construirPedido();
 
                navegacion = "VER_PEDIDO";
                
                break;
                
            case "vuelta":       
                /*
                    Este case es resultado de una peticion por lo que las referencias al pedido y las listas se pierden.
                    Es necesario volver a "recargarlas" para que estes inicializadas cuando se recargue el action.
                 */
                pedido = (Pedido) session.get("pedido");
                listaMesas = (List<Mesa>) session.get("listaMesas");
                listaProductos = (List<Producto>) session.get("listaProductos");
                listaCategorias = (List<Categoria>) session.get("listaCategorias");             

                navegacion = "VUELTA_PEDIDO";   
        }    
     
        return navegacion;
    }    

    /*
        Metodo que ejecutamos al iniciar la aplicacion de pedidos y al finalizar un pedido para cargar el estado inicial de la aplicacion.
        En este metodo se cargan las listas con las que vamos a trabajar y se crea el pedido.
    */
    private void cargarEstadoInicial() {              
        listaMesas = godr.obtenerListadoMesas(); //Obtenemos la lista de mesas
        listaProductos = gog.obtenerListaProductosActivos(); //Obtenemos los productos activos
        listaCategorias = gog.obtenerListaCategoriasActivas(); //Obtenemos las categorias activadas

        //En este punto creamos el pedido con el quer vamos a trabajar hasta que el cliente realiace el pedido o lo elimine
        pedido = new Pedido();

        //Almacenamos la informacion en sesion para evitar tener que volver a la BD.
        session.put("listaMesas", listaMesas);        
        session.put("listaProductos", listaProductos);  
        session.put("listaCategorias", listaCategorias); 
        session.put("pedido", pedido);    
    }
    
    /*
        Del cliente recibimos el numero de mesa y un array JSON con los id de los productos y el numero de unidades de cada producto.
        Este metodo construye un Pedido con dicha informacion
    */
    private void construirPedido() {
        //Recuperamos los parametros de la peticion
        int numeroMesa = Integer.parseInt(request.getParameter("numeroMesa"));
        String arrayPedido = request.getParameter("arrayPedido");

        //Reconstruimos el array JSON para obtener los productos seleccionados
        Gson gson = new Gson();
        Type tipoRefProducto = new TypeToken<List<RefProducto>>() {}.getType();
        List<RefProducto> listaRefProductos = gson.fromJson(arrayPedido, tipoRefProducto);

        //Recuperamos la listaProductos de la sesion
        listaProductos = (List<Producto>) session.get("listaProductos");

        //Recuperamos el pedido de la sesion
        pedido = (Pedido) session.get("pedido");
        /*
            Puede darse el caso de que mientras se esta creando un pedido el usuario mire el pedido y luego continue añadiendo,
            es decir, que se producen saltos entre pedido.jsp y verPedido.jsp. En todo este proceso la referencia al "pedido"
            sigue siendo la misma por lo que cada vez que pasemos por este punto se añadiran los productos al "pedido" incluso
            los que ya estan añadidos. Por esta razon una vez que recuperamos la referencia al pedido de la sesion limpiamos
            la lista de productos. Asi evitamos que se sobrescriban los productos mas de una vez.
        */
        pedido.getListaProductos().clear();
        pedido.setNumeroMesa(numeroMesa); //Le asignamos el numero de mesa
       
        //Obtenemos el usuario, es decir, del camarero con el nombre que tenemos registrado en la session
       Usuario usuario = gou.obtenerUsuario(session.get("usuario").toString());  
       //Asignamos el id del camarero al pedido
       pedido.setIdUsuario(usuario.getId()); 

        Hamburguesa h = null;
        for (RefProducto rp : listaRefProductos) { //Recorremos la lista de refProductos que obtenemos del cliente
            Producto producto = obtenerProducto(rp.getId()); //Obtenemos la informacion completa del producto                 
            producto.setUnidades(rp.getUnidades()); //Asignamos el numero de unidades que quiere el cliente
            if (producto instanceof Hamburguesa) { //Si el producto es de tipo hamburguesa
                h = (Hamburguesa) producto; //Hacemos que nuestra referecia hamburguesa apunte al objeto (sera de tipo Hamburguesa)
                pedido.getListaProductos().add(h); //Añadimos la hamburguesa al pedido
            } else if (producto instanceof Extra) { //Si el producto es de tipo extra
                h.getListaProductosExtra().add((Extra) producto); //h apuntara a la ultima hamburguesa por lo que los extras que vengan le pertenecen
            } else { //Si es de cualquier otro tipo
                pedido.getListaProductos().add(producto); //Añadimos el producto al pedido
            }
        }
 
        //Almacenamos el pedido en sesion.
        session.put("pedido", pedido);  
    }
    
    /**
     * Creamos esta clase para poder deserializar el objeto JSON que recibimos del cliente al realizar un pedido.
     * Es una clase auxiliar. Del cliente recibimos una cadena en formato JSON que contiene los productos del
     * pedido (su id) y el numero de unidades de cada producto. Con esta clase reconstruimos dicha informacion
     * y a partir de aqui montamos el objeto Pedido.
     */  
    class RefProducto {

        private int id;
        private int unidades;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUnidades() {
            return unidades;
        }

        public void setUnidades(int unidades) {
            this.unidades = unidades;
        }
    }  
    
    /*
        Cuando cargamos el action por primera vez ya cargamos de la BD la listaProductos. En lugar de consultar uno
        a uno cada producto en BD vamos obtiendo la información de cada producto a partir de esta lista. Esto es asi
        porque en cliente solo se selecciona el id del producto y las unidades pero necesitamos el resto de la informacion
        para montar el pedido: nombre, precio, etc.
    */
    private Producto obtenerProducto(int id) {
        Producto producto = null;
        for(Producto p : listaProductos) { //Recorremos la listaProductos
            if(p.getId() == id) { //Si encontramos el producto
                if("Hamburguesa".equals(p.getCategoria())) { //Si el producto es de la categoria hamburguesa
                    //Creamos un objeto Hamburguesa y le asignamos los datos del producto que estamos analizando
                    Producto hamburguesa = new Hamburguesa();
                    hamburguesa.setId(p.getId());
                    hamburguesa.setNombre(p.getNombre());
                    hamburguesa.setPrecio(p.getPrecio());
                    hamburguesa.setDescripcion(p.getDescripcion());
                    hamburguesa.setActivo(p.isActivo());
                    
                    producto = hamburguesa;
                } else if("Extra".equals(p.getCategoria())) { //Si el producto es de la categoria extra
                    //Creamos un objeto Extra y le asignamos los datos del producto que estamos analizando
                    Producto extra = new Extra();
                    extra.setId(p.getId());
                    extra.setNombre(p.getNombre());
                    extra.setPrecio(p.getPrecio());
                    extra.setDescripcion(p.getDescripcion());
                    extra.setActivo(p.isActivo());

                    producto = extra;
                } else { //Si es de cualquier otra categoria
                    producto = p;
                }
                break; //Dejamos de iterar
            }
        }
        return producto;
    }    
    
    private void montarVistaResultado(int resultadoOperacion) throws IOException {
            output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet  

            switch (resultadoOperacion) {
                case 1:
                    output.print("<p>"+getText("pedidos.success.realizarOperacion")+"</p>*");
                    break;
                default:
                    output.print("<p>"+getText("pedidos.error.realizarOperacion")+"</p>*");
                    break;
            }  

            //Necesitamos obtener de nuevo el listado de mesas porque se han modificado los estados
            listaMesas = godr.obtenerListadoMesas();
            //La volvemos a guardar en sesion porque ha cambiado
            session.put("listaMesas", listaMesas); 

            output.print("<span class='label label-primary'>"+getText("pedidos.numeroMesa")+"</span>");
            output.print("<select id='selectMesas' class='form-control' required>");
            output.print("<option value='' disabled selected>"+getText("pedidos.elijaMesa")+"</option>");
            for (Mesa mesa : listaMesas) {
                if (mesa.getEstado().equals("libre")) {
                    output.print("<option value='"+mesa.getNumero()+"'>"+mesa.getNumero()+"</option>");
                }
            }
            output.print("</select>");
            output.print("<button type='button' class='btn btn-default' aria-label='Left Align' onclick='obtenerMesasDisponibles()'><span class='glyphicon glyphicon-refresh' aria-hidden='true'></span></button>");
    }
    
    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        this.request = hsr; 
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        this.response = hsr;
    }
    
    public List<Mesa> getListaMesas() {
        return listaMesas;
    }

    public void setListaMesas(List<Mesa> listaMesas) {
        this.listaMesas = listaMesas;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}