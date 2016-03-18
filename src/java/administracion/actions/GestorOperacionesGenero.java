/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Categoria;
import administracion.modelo.Producto;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author juang
 */
public class GestorOperacionesGenero {
    
    private static final int OPERACION_SUCCESS = 1;
    private static final int OPERACION_ERROR = 0;    
    private static final int CLAVE_DUPLICADA = 2;    
    
    public List<Categoria> obtenerListaCategorias() {
        Session sesion = null;
        List<Categoria> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_categoria,nombre_categoria,activo FROM categoria ORDER BY nombre_categoria";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Categoria.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener lista categorias: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;         
    }
    
    public List<Categoria> obtenerListaCategoriasActivas() {
        Session sesion = null;
        List<Categoria> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_categoria,nombre_categoria,activo FROM categoria WHERE activo = true ORDER BY nombre_categoria";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Categoria.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener lista categorias: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;         
    }    
    
    public int actualizarEstadoCategoria(int id, boolean activo) {
        Session sesion = null;
        int resultadoOperacion = 0;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "UPDATE categoria SET activo = "+activo+" WHERE id_categoria = " + id;
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();
            
            //Si deshabilitamos la categoria Hamburguesa deshabilitamos la categoria Extra
            Categoria categoria = obtenerCategoria(id); //Obtenemos la informacion de la categoria que acabamos de deshabilitar
            if (categoria.getNombre().equals("Hamburguesa")) { //Si el nombre de la categoria es Hamburguesa
                //Obtenemos el id de la categoria Extra
                Categoria categoriaExtra = obtenerCategoria("Extra");
                String sql1 = "UPDATE categoria SET activo = " + activo + " WHERE id_categoria = " + categoriaExtra.getId();
                SQLQuery query1 = sesion.createSQLQuery(sql1);
                query1.executeUpdate();
            }        
            
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error baja usuario: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
        return resultadoOperacion;        
    }
    
    public int insertarCategoria(String nombre, boolean check) {
        Session sesion = null;
        int resultadoOperacion;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "INSERT INTO categoria (nombre_categoria, activo) VALUES ('"+nombre+"', "+check+")";
            SQLQuery query =  sesion.createSQLQuery(sql);
            query.executeUpdate();
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;       
        } catch(ConstraintViolationException e) {
            resultadoOperacion = CLAVE_DUPLICADA;
            System.out.println("Error alta categoria por calve duplicada: " + e);
        } catch (HibernateException e) {             
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error alta categoria: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }     
        return resultadoOperacion;
    }
    
    public Categoria obtenerCategoria(int id) {
        Session sesion = null;
        Categoria categoria = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_categoria,nombre_categoria,activo FROM categoria WHERE id_categoria = " + id;            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Categoria.class);             
            categoria = (Categoria) query.list().get(0);          
            sesion.getTransaction().commit();     
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener categoria: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return categoria;         
    }   
    
    public Categoria obtenerCategoria(String nombreCategoria) {
        Session sesion = null;
        Categoria categoria = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_categoria,nombre_categoria,activo FROM categoria WHERE nombre_categoria = '" + nombreCategoria + "'";            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Categoria.class);             
            categoria = (Categoria) query.list().get(0);          
            sesion.getTransaction().commit();     
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener categoria: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return categoria;         
    }      
    
    public List<Producto> obtenerListaProductos() {
        Session sesion = null;
        List<Producto> lista = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_producto,nombre_producto,precio,descripcion,activo,nombre_categoria FROM producto ORDER BY nombre_producto";            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Producto.class);             
            lista = query.list();          
            sesion.getTransaction().commit();     
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener productos: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;         
    }  
    
    public List<Producto> obtenerListaProductosActivos() {
        Session sesion = null;
        List<Producto> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_producto,nombre_producto,precio,descripcion,activo,nombre_categoria FROM producto WHERE activo = true ORDER BY nombre_producto";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Producto.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener lista categorias: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;         
    }       
    
    public int actualizarEstadoProducto(int id, boolean activo) {
        Session sesion = null;
        int resultadoOperacion = 0;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "UPDATE producto SET activo = "+activo+" WHERE id_producto = " + id;
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error baja usuario: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
        return resultadoOperacion;        
    }      
    
    public int insertarProducto(Producto producto) {
        Session sesion = null;
        int resultadoOperacion;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('"+producto.getNombre()+"', "+producto.getPrecio()+", '"+producto.getDescripcion()+"', "+producto.isActivo()+", '"+producto.getCategoria()+"')";
            SQLQuery query =  sesion.createSQLQuery(sql);
            query.executeUpdate();
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;        
        } catch(ConstraintViolationException e) {
            resultadoOperacion = CLAVE_DUPLICADA;
            System.out.println("Error alta producto por calve duplicada: " + e);
        } catch (HibernateException e) {             
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error alta producto: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }     
        return resultadoOperacion;
    } 
    
    public Producto obtenerProducto(int id) {
        Session sesion = null;
        Producto producto = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_producto,nombre_producto,precio,descripcion,activo,nombre_categoria FROM producto WHERE id_producto = "+id+" ORDER BY nombre_producto";            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Producto.class);             
            producto = (Producto) query.list().get(0);          
            sesion.getTransaction().commit();     
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener productos: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return producto;         
    }
    
    public int modificarProducto(Producto producto) {
        Session sesion = null;
        int resultadoOperacion = 0;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "UPDATE producto SET precio = "+producto.getPrecio()+", descripcion = '"+producto.getDescripcion()+"', activo = "+producto.isActivo()+", nombre_categoria = '"+producto.getCategoria()+"' WHERE nombre_producto = '" + producto.getNombre() +"'";
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error baja usuario: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
        return resultadoOperacion;        
    }    
}
