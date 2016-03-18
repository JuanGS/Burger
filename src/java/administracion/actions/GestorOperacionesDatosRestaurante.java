/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracion.actions;

import administracion.modelo.Impuesto;
import administracion.modelo.Mesa;
import administracion.modelo.Restaurante;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 *
 * @author juang
 */
public class GestorOperacionesDatosRestaurante { 
    
    private static final int OPERACION_SUCCESS = 1;
    private static final int OPERACION_NO_POSIBLE = -1;    
    private static final int OPERACION_ERROR = 0; 
    private static final String ESTADO_MESA = "libre";    
    
    public Restaurante obtenerDatosRestaurante() {
        Session sesion = null;
        Restaurante restaurante = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
            String sql = "SELECT cif,nombre_local,direccion,telefono FROM datos_local";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Restaurante.class);
            List<Restaurante> lista = query.list();
            sesion.getTransaction().commit();
            restaurante = lista.get(0);
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            System.out.println("Error obtener datos restaurante: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }

        return restaurante;
    }
    
    public List<Impuesto> obtenerImpuestos() {     
        Session sesion = null;
        List<Impuesto> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();  
            String sql = "SELECT nombre_impuesto,id_impuesto,valor FROM impuesto WHERE id_impuesto IN (SELECT MAX(id_impuesto) FROM impuesto GROUP BY nombre_impuesto) and valor > 0 ORDER BY nombre_impuesto";
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Impuesto.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener impuestos: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;        
    }    

    public int obtenerNumeroMesas() {     
        Session sesion = null;
        int numeroMesa = 0;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT MAX(numero_mesa) AS numero_mesa,estado_mesa,activo FROM mesa WHERE activo = true"; //Nos interesa el numero de mesas activas
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Mesa.class);
            List<Mesa> lista = query.list(); 
            numeroMesa = lista.get(0).getNumero();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener numero de mesas: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }
   
        return numeroMesa;        
    }
    
    public int actualizarDatosRestaurante(Restaurante restaurante) {
        Session sesion = null;
        int resultadoOperacion = 0;
        
        try {       
            HibernateUtil hb = new HibernateUtil();        
            sesion = hb.getSessionFactory().openSession();        
            sesion.beginTransaction();         
            String sql = "UPDATE datos_local SET nombre_local = '"+restaurante.getNombre()+"', direccion = '"+restaurante.getDireccion()+"', telefono = "+restaurante.getTelefono()+"  WHERE cif = '"+restaurante.getCif()+"'";
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();                                
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;                      
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {           
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error modificar datos restaurante: " + e);       
        } finally {           
             if(sesion != null) {
                sesion.close();
            }           
        }
        
        return resultadoOperacion;
    }
    
    public int actualizarNumeroMesas(int numeroMesasInicial, int numeroMesasNuevo) {

        int resultadoOperacion = 0;
        int diferencia = 0;
        Session sesion = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction(); 

            if(numeroMesasNuevo > numeroMesasInicial) { //En este caso queremos ampliar el numero de mesas
                diferencia = numeroMesasNuevo - numeroMesasInicial; //Obtenemos la diferencia
                
                //Obtenemos el numero de mesas total. Teniendo en cuenta las activas + las no activas (si las hay)
                String sql = "SELECT numero_mesa,estado_mesa,activo FROM mesa";
                SQLQuery query = sesion.createSQLQuery(sql).addEntity(Mesa.class);
                int numeroMesasTotal = (int) query.list().size();             

                for(int i=numeroMesasInicial+1; i<=numeroMesasNuevo; i++) { //Itreamos desde el mayor numero de mesa activo actualmente hasta el numero de mesas final que queremos                   
                    if(numeroMesasTotal > i) { //Quiere decir que la mesa ya existia pero estaba deshabilitada                       
                        String sql1 = "UPDATE mesa SET activo = true WHERE numero_mesa = " + i; //Actualizamos su estado
                        SQLQuery query1 = sesion.createSQLQuery(sql1);
                        query1.executeUpdate();                     
                    } else { //La mesa no exite y la creamos                 
                        Mesa mesa = new Mesa(); //Creamos la mesa
                        mesa.setNumero(i);
                        mesa.setEstado(ESTADO_MESA);
                        mesa.setActivo(true);
                        sesion.save(mesa); //Hacemos el INSERT
                    }
                }
                sesion.getTransaction().commit();
                resultadoOperacion = OPERACION_SUCCESS;

            } else { //En este caso queremos reducir el numero de mesas
                List<Mesa> listaMesas = obtenerListadoMesas(); //Obtenemos el listado de mesas (nos interesa para ver el estado)
                boolean reducir = true; //Indica si podemos realizar la operacion para reducir el numero de mesas          
                diferencia = numeroMesasInicial - numeroMesasNuevo; //Numero de mesas que queremos reducir
                for(int i=numeroMesasInicial-1; i>=numeroMesasNuevo; i--) { //Iteremos por las mesas que queremos desactivar
                    if(!listaMesas.get(i).getEstado().equals(ESTADO_MESA)) { //Si la mesa no esta libre
                        reducir = false; //NO PERMITIMOS LA OPERACION
                        resultadoOperacion = OPERACION_NO_POSIBLE;
                        break;
                    }
                }
                if(reducir) { //Si todas las mesas que queremos desactivar estan libres
                    for(int i=numeroMesasInicial-1; i>=numeroMesasNuevo; i--) { //Las recorremos y las desactivamos
                        String sql = "UPDATE mesa SET activo = false WHERE numero_mesa = " + (i+1);
                        SQLQuery query = sesion.createSQLQuery(sql);
                        query.executeUpdate();                 
                    }
                    sesion.getTransaction().commit(); 
                    resultadoOperacion = OPERACION_SUCCESS;
                }
            }   
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch (Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error actualizar numero de mesas: " + e);    
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }

        return resultadoOperacion;
    }
    
    public List<Mesa> obtenerListadoMesas() {
        Session sesion = null;
        List<Mesa> lista = null;
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT numero_mesa,estado_mesa,activo FROM mesa WHERE activo = true"; //En este caso siempre nos van a interesar las mesas que esten en estado activo=true
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Mesa.class);
            lista = query.list();
            sesion.getTransaction().commit();
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            System.out.println("Error obtener lista de mesas: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return lista;        
    }
    
    public int insertarImpuesto(Impuesto dato) {
        Session sesion = null;
        int resultadoOperacion = 0;
        
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction(); 
            String sql = "INSERT INTO impuesto (nombre_impuesto, valor) VALUES ('"+dato.getNombre()+"', "+dato.getValor()+")";
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();                                
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;                      
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error insertar impuesto: " + e);           
        } finally {
             if(sesion != null) {
                sesion.close();
            }           
        }        
        return resultadoOperacion;
    }    
}