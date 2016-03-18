/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Usuario;
import hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author juang
 */
public class GestorOperacionesUsuario {
    
    private static final int OPERACION_SUCCESS = 1;
    private static final int OPERACION_ERROR = 0;    
    private static final int CLAVE_DUPLICADA = 2;    
    
    public Usuario obtenerUsuario(String usuario) {
        Session sesion = null;
        Usuario usuarioEncontrado = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "SELECT id_usuario,usuario,password,rol FROM usuario WHERE usuario = '" + usuario + "'";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Usuario.class);
            usuarioEncontrado = (Usuario) query.list().get(0);
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            System.out.println("Error obtener usuario: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return usuarioEncontrado;
    }    
    
    public int insertarUsuario(Usuario usuario) {
        Session sesion = null;
        int resultadoOperacion;
       
        usuario.setRol("usuario"); //Por defecto creamos roles de usuario
        
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "INSERT INTO usuario (usuario, password, rol) VALUES ('"+usuario.getUsuario()+"', '"+usuario.getPassword()+"', '"+usuario.getRol()+"')";
            SQLQuery query =  sesion.createSQLQuery(sql);
            query.executeUpdate();
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch(ConstraintViolationException e) {
            resultadoOperacion = CLAVE_DUPLICADA;
            System.out.println("Error alta usuario por calve duplicada: " + e);
        } catch(HibernateException e) {
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error alta usuario: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }     
        return resultadoOperacion;
    }
    
    public List<Usuario> obtenerListaUsuarios() {
        Session sesion = null;
        List<Usuario> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "SELECT id_usuario,usuario,password,rol FROM usuario ORDER BY rol";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Usuario.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch(HibernateException e) {
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            System.out.println("Error obtener lista de usarios: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;
    }
    
    public int eliminarUsuario(String usuario) {
        Session sesion = null;
        int resultadoOperacion;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "DELETE FROM usuario WHERE id_usuario = '" + usuario + "'";
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
