/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comedor.actions;

import administracion.actions.GestorOperacionesDatosRestaurante;
import administracion.actions.GestorOperacionesGenero;
import administracion.actions.GestorOperacionesUsuario;
import administracion.modelo.Impuesto;
import administracion.modelo.Mesa;
import administracion.modelo.Producto;
import administracion.modelo.Restaurante;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comedor.modelo.Cuenta;
import comedor.modelo.ProductoLineaPedido;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
public class OperacionesComedorAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    //Para interactuar con la JSP
    private List<Mesa> listaMesas;
    private Pedido pedido;
    private Cuenta cuenta;
    private List<Impuesto> listaImpuestos;    
    
    //Uso interno
    private List<Producto> listaProductos;
    
    //Variables para gestionar la peticion
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletOutputStream output;        
    private InputStream respuesta;
    private String nombreDocumento;

    private final String RUTA_CUENTAS = System.getProperty("catalina.base") + File.separator + "webapps" + File.separator + "cuentas" + File.separator; //Crear la carpeta "cuentas" en el directorio catalina.base/webapps
        
    //Para gestionar la sesion
    private Map session = ActionContext.getContext().getSession();    
    
    //Para gestionar las peticiones a BD
    private final GestorOperacionesDatosRestaurante godr = new GestorOperacionesDatosRestaurante(); 
    private final GestorOperacionesGenero gog = new GestorOperacionesGenero();
    private final GestorOperacionesUsuario gou = new GestorOperacionesUsuario();    
    private final GestorOperacionesComedor goc = new GestorOperacionesComedor();
    
    //Para gestionar la nevegacion del xml
    private String navegacion; 

    @Override
    public String execute() {    
        //Configuramos el objeto para response
        response.setContentType("text/html; charset=iso-8859-1");
        
        int numeroMesa;
        
        //Obtenemos los datos del request
        String operacion = request.getParameter("operacion");

        switch(operacion) {
            case "cargarMesas":
                //Obtenemos el listado de mesas en BD
                listaMesas = godr.obtenerListadoMesas();
                
                listaProductos = gog.obtenerListaProductosActivos(); //Obtenemos los productos activos
                
                session.put("listaProductosComedor", listaProductos);                  
                
                navegacion = "CARGAR_MESAS";
               
                break;
            
            case "actualizarMesas":
                try {
                    actualizarVistaMesas();
                } catch (IOException ex) {
                    System.out.println("Error al actualizar la vista mesas");
                }
                
                navegacion = null;

                break;
                
            case "generarCuenta":
                //Recuperamos los parametros de la peticion
                numeroMesa = Integer.parseInt(request.getParameter("numeroMesa"));
                //Obtenemos pedido asociado a la mesa (numeroMesa)
                Pedido pedidoAux = goc.obtenerPedidoMesa(numeroMesa);           
                //Con el id del pedido obtenemos los registros del pedido
                List<ProductoLineaPedido> listaLineasPedido = goc.obtenerLineasPedido(pedidoAux.getId());                
                //Con los registros del pedido vamos "construyendo" los productos completos, creamos el pedido y los vamos añadiendo
                construirPedido(pedidoAux, listaLineasPedido);
                //Guardamos el pedido en sesion
                session.put("pedidoComedor", pedido);         
                //Obtenemos los impuestos
                listaImpuestos = godr.obtenerImpuestos();
                //Guardamos impuestos en sesion
                session.put("listaImpuestosComedor", listaImpuestos);
                //Añadimos el registro del pedido a la BD (cuenta) y Actualizamos el estado de la mesa.
                cuenta = goc.generarCuenta(pedido, listaImpuestos);
                //Guardamos la cuenta en sesion
                session.put("cuentaComedor", cuenta); 

                navegacion = "MOSTRAR_CUENTA";
                
                break;
                
            case "descargarCuenta":
                //Recuperamos el pedido de la sesion
                pedido = (Pedido) session.get("pedidoComedor"); 
                //Recuperamos la cuenta de la sesion
                cuenta = (Cuenta) session.get("cuentaComedor");  
                //Recuperamos impuestos de la sesion
                listaImpuestos = (List<Impuesto>) session.get("listaImpuestosComedor");                 
               
                //Asignamos el nombreDocumento
                SimpleDateFormat formatoNombreCuenta = new SimpleDateFormat("_yyyyMMdd_HHmmss_");
                nombreDocumento = "cuenta"+formatoNombreCuenta.format(cuenta.getFecha())+cuenta.getId()+".pdf";
                
                try {
                    //Generamos el pdf con los datos del pedido
                    crearPDF();
                    //Asignamos el documento al flujo de respuesta      
                    respuesta = new DataInputStream(new FileInputStream(RUTA_CUENTAS+nombreDocumento));
                    
                } catch (FileNotFoundException ex) {
                    System.err.println("Error al crear el PDF. FileNotFoundException: " + ex);
                } catch (DocumentException ex) {
                    System.err.println("Error al crear el PDF. DocumentException: " + ex);
                } catch (IOException ex) {
                    System.err.println("Error al crear el PDF. IOException: " + ex);
                }
                
                //Limpiamos la sesion
                session.remove("pedidoComedor");
                session.remove("cuentaComedor");
                session.remove("listaImpuestosComedor");
                session.remove("listaProductosComedor");
                
                navegacion = "CUENTA";
                
                break;                                                                                                                                
                
            case "cuentaPagada":
                //Recuperamos los parametros de la peticion
                numeroMesa = Integer.parseInt(request.getParameter("numeroMesa"));
                //Actualizamos el estado de la mesa
                goc.actualizarEstadoMesaPendienteALibre(numeroMesa);

                try {
                    actualizarVistaMesas();
                } catch (IOException ex) {
                    System.out.println("Error al actualizar la vista mesas");
                }                
                
                navegacion = null;

                break;
        }
        
        return navegacion;
    }
    
    private void actualizarVistaMesas() throws IOException {
        output = response.getOutputStream(); //Obtenemos una referencia al objeto que nos permite escribir en la respuesta del servlet  
        
        //Obtenemos el listado de mesas en BD
        listaMesas = godr.obtenerListadoMesas();        
        
        for (Mesa mesa : listaMesas) {
            if (mesa.getEstado().equals("libre")) {
                output.print("<div style='float: left; margin-right: 20px;'>");
                output.print("<img alt=" + mesa.getNumero() + " class='img-circle' width='140' height='140' style='background-color: green'>");
                output.print("</div>");
            } else if (mesa.getEstado().equals("ocupada")) {
                output.print("<div style='float: left; margin-right: 20px;'>");
                output.print("<img alt=" + mesa.getNumero() + " class='img-circle' width='140' height='140' style='background-color: red'><br/><br/>");
                output.print("<form action='OperacionesComedor' method='POST'>");
                output.print("<button type='submit' class='btn btn-default btn-default' name='operacion' value='generarCuenta'>"+getText("comedor.generarCuenta")+"</button>");
                output.print("<input type='hidden' id='numeroMesa' name='numeroMesa' value='"+mesa.getNumero()+"'/>");                 
                output.print("</form>");              
                output.print("</div>");
            } else if (mesa.getEstado().equals("pendiente")) {
                output.print("<div style='float: left; margin-right: 20px;'>");
                output.print("<img alt=" + mesa.getNumero() + " class='img-circle' width='140' height='140' style='background-color: yellow'><br/><br/>");
                output.print("<input type='button' class='btn btn-default btn-default' value='"+getText("comedor.cuentaPagada")+"' onclick='cuentaPagada("+mesa.getNumero()+")'/>");  
                output.print("</div>");
            } else {
                output.print("<div style='float: left; margin-right: 20px;'>");
                output.print("<p><strong style='color: red;'>"+getText("comedor.error.cargarMesa")+"</strong></p>");
                output.print("</div>");
            }
        }
    }
    
    private void construirPedido(Pedido pedidoAux, List<ProductoLineaPedido> listaLineasPedido) {
        //Obtenemos la lista de productos de la sesion para poder obtener la info completa del producto a apartir de su id
        listaProductos = (List<Producto>) session.get("listaProductosComedor");
        //Contruimos el pedido
        pedido = new Pedido();
        //Le asignamos los parametros
        pedido.setId(pedidoAux.getId()); //Id
        pedido.setNumeroMesa(pedidoAux.getNumeroMesa()); //Numero de mesa
        pedido.setIdUsuario(pedidoAux.getIdUsuario()); //camarero
        //Lista de productos
        Hamburguesa h = null;
        double importe = 0;
        for (ProductoLineaPedido linea : listaLineasPedido) { //Recorremos las lineas del pedido
            Producto producto = obtenerProducto(linea.getIdProducto()); //Obtenemos la informacion completa del producto                 
            producto.setUnidades(linea.getUnidades()); //Asignamos el numero de unidades que quiere el cliente
            importe += (producto.getPrecio() * producto.getUnidades()); //Vamos obteniendo el importe del pedido
            if (producto instanceof Hamburguesa) { //Si el producto es de tipo hamburguesa
                h = (Hamburguesa) producto; //Hacemos que nuestra referecia hamburguesa apunte al objeto (sera de tipo Hamburguesa)
                pedido.getListaProductos().add(h); //Añadimos la hamburguesa al pedido
            } else if (producto instanceof Extra) { //Si el producto es de tipo extra
                h.getListaProductosExtra().add((Extra) producto); //h apuntara a la ultima hamburguesa por lo que los extras que vengan le pertenecen
            } else { //Si es de cualquier otro tipo
                pedido.getListaProductos().add(producto); //Añadimos el producto al pedido
            }
        }        
        //Importe
        pedido.setImporte(importe);
    }
    
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
                    hamburguesa.setCategoria(p.getCategoria());                         
                    
                    producto = hamburguesa;
                } else if("Extra".equals(p.getCategoria())) { //Si el producto es de la categoria extra
                    //Creamos un objeto Extra y le asignamos los datos del producto que estamos analizando
                    Producto extra = new Extra();
                    extra.setId(p.getId());
                    extra.setNombre(p.getNombre());
                    extra.setPrecio(p.getPrecio());
                    extra.setDescripcion(p.getDescripcion());
                    extra.setActivo(p.isActivo());
                    extra.setCategoria(p.getCategoria());                      

                    producto = extra;
                } else { //Si es de cualquier otra categoria
                    producto = p;
                }
                break; //Dejamos de iterar
            }
        }
        return producto;
    }    
    
    private void crearPDF() throws IOException, DocumentException {
        Restaurante restaurante = godr.obtenerDatosRestaurante();

        //Creamos el directorio donde almacenar los pdf sino existe
        File file = new File(RUTA_CUENTAS); //Especificamos la ruta
        if (!file.exists()) { //Si el directorio no existe
            if (file.mkdir()) { //Creamos el directorio
                //Le asignamos los permisos 777
                file.setExecutable(true);
                file.setReadable(true);
                file.setExecutable(true);
            } else {
                System.err.println("Error al crear el directorio especificado");
                throw new IOException(); //Lanzamos una excepcion
            }
        }

        if (file.exists()) { //Si el directorio existe
            //Creamos el documento
            Document documento = new Document();
            //Creamos el OutputStream para el fichero pdf   
            FileOutputStream destino = new FileOutputStream(RUTA_CUENTAS + nombreDocumento);

            //Asociamos el FileOutputStream al Document
            PdfWriter.getInstance(documento, destino);
            //Abrimos el documento
            documento.open();

            //Añadimos el nombre del restaurante
            Font titulo = FontFactory.getFont(FontFactory.TIMES, 16, Font.BOLDITALIC);
            Chunk chunk = new Chunk(restaurante.getNombre(), titulo);
            Paragraph parrafo = new Paragraph(chunk);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            documento.add(parrafo);
            //Añadimos la imagen
            URL url = new URL("http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath() + "/img/elvis.png");
            Image foto = Image.getInstance(url);
            foto.scaleToFit(100, 100);
            foto.setAlignment(Chunk.ALIGN_MIDDLE);
            documento.add(foto);
            //Añadimos los datos del restaurante
            Font datos = FontFactory.getFont(FontFactory.TIMES, 12, Font.NORMAL);
            chunk = new Chunk(getText("cuenta.cif") + ": " + restaurante.getCif(), datos);
            documento.add(new Paragraph(chunk));
            chunk = new Chunk(getText("cuenta.direccion") + ": " + restaurante.getDireccion(), datos);
            documento.add(new Paragraph(chunk));
            chunk = new Chunk(getText("cuenta.telefono") + ": " + restaurante.getTelefono(), datos);
            documento.add(new Paragraph(chunk));
            //Añadimos los datos de la cuenta
            chunk = new Chunk(getText("cuenta.cuentaId") + ": " + cuenta.getId(), datos);
            documento.add(new Paragraph(chunk));
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            chunk = new Chunk(getText("cuenta.fecha") + ": " + formatoFecha.format(cuenta.getFecha()), datos);
            documento.add(new Paragraph(chunk));
            SimpleDateFormat formtoHora = new SimpleDateFormat("HH:mm:ss");
            chunk = new Chunk(getText("cuenta.hora") + ": " + formtoHora.format(cuenta.getFecha()), datos);
            documento.add(new Paragraph(chunk));
            //Añadimos los datos del pedido
            //Obtenemos el usuario, es decir, del camarero con el nombre que tenemos registrado en la session 
            chunk = new Chunk(getText("cuenta.camarero") + ": " + session.get("usuario").toString(), datos);
            documento.add(new Paragraph(chunk));
            documento.add(new Chunk(Chunk.NEWLINE)); //Salto de linea        
            //Añadimos la tabla con los datos del pedido
            //Creamos una tabla
            PdfPTable tabla = new PdfPTable(4); //Especificamos el numero de columnas
            //Añadimos la cabecera de la tabla
            tabla.addCell(getText("cuenta.producto"));
            tabla.addCell(getText("cuenta.unidades"));
            tabla.addCell(getText("cuenta.pvp"));
            tabla.addCell(getText("cuenta.total"));
            for (Producto producto : pedido.getListaProductos()) {
                tabla.addCell(producto.getNombre());
                tabla.addCell(String.valueOf(producto.getUnidades()));
                tabla.addCell(String.valueOf(producto.getPrecio()));
                tabla.addCell(String.valueOf(producto.getPrecio() * producto.getUnidades()));

                if (producto instanceof Hamburguesa) {
                    Hamburguesa h = (Hamburguesa) producto;
                    for (Producto extra : h.getListaProductosExtra()) {
                        tabla.addCell(extra.getNombre());
                        tabla.addCell(String.valueOf(extra.getUnidades()));
                        tabla.addCell(String.valueOf(extra.getPrecio()));
                        tabla.addCell(String.valueOf(extra.getPrecio() * extra.getUnidades()));
                    }
                }
            }
            //Añadimos la tabla al documento
            documento.add(tabla);
            documento.add(new Chunk(Chunk.NEWLINE)); //Salto de linea
            //Añadimos una tabla con los impuestos y el total a pagar
            tabla = new PdfPTable(3); //Especificamos el numero de columnas    
            tabla.addCell(getText("cuenta.baseImponible") + ": " + pedido.getImporte() + "€");
            tabla.addCell("");
            tabla.addCell("");
            DecimalFormat formato = new DecimalFormat("#.##€");
            for (Impuesto dato : listaImpuestos) {
                tabla.addCell("");
                tabla.addCell(dato.getNombre() + ": " + dato.getValor());
                double impuesto = (pedido.getImporte() * dato.getValor()) / 100;
                tabla.addCell(getText("cuenta.impuesto") + " " + dato.getNombre() + ": " + formato.format(impuesto));
            }
            tabla.addCell(getText("cuenta.total") + ": " + cuenta.getCantidad() + "€");
            tabla.addCell("");
            tabla.addCell("");
            //Añadimos la tabla al documento
            documento.add(tabla);

            //Cerramos el documento
            documento.close();
            
        } else { //Si el directoiro no existe
            System.err.println("Error. No existe el directorio especificado");
            throw new IOException(); //Lanzamos una excepcion
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

    public List<Mesa> getListaMesas() {
        return listaMesas;
    }

    public void setListaMesas(List<Mesa> listaMesas) {
        this.listaMesas = listaMesas;
    }    

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    
    public List<Impuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<Impuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public InputStream getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(InputStream respuesta) {
        this.respuesta = respuesta;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
}