/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comedor.actions;

import administracion.modelo.Impuesto;
import comedor.modelo.Cuenta;
import comedor.modelo.ProductoLineaPedido;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import pedidos.modelo.Pedido;

/**
 *
 * @author juang
 */
public class GestorOperacionesComedor {
    
    private static final int OPERACION_SUCCESS = 1;
    private static final int OPERACION_ERROR = 0; 
    
    public Pedido obtenerPedidoMesa(int numeroMesa) {
        Session sesion = null;
        Pedido pedido = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT MAX(id_pedido) AS id_pedido,numero_mesa,id_usuario FROM pedido WHERE numero_mesa = " + numeroMesa;            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(Pedido.class);             
            pedido = (Pedido) query.list().get(0);          
            sesion.getTransaction().commit();     
        } catch(Exception e) {
            System.out.println("Error obtener id pedido: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return pedido;         
    }
    
    public List<ProductoLineaPedido> obtenerLineasPedido(int idPedido) {
        Session sesion = null;
        List<ProductoLineaPedido> listaLineasPedido = null;
        try {           
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();        
            String sql = "SELECT id_pedido,numero_linea,id_producto,unidades FROM producto_linea_pedido WHERE id_pedido = " + idPedido;            
            SQLQuery query = sesion.createSQLQuery(sql).addEntity(ProductoLineaPedido.class);             
            listaLineasPedido = query.list();          
            sesion.getTransaction().commit();     
        } catch(Exception e) {
            System.out.println("Error obtener lineas pedido: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return listaLineasPedido;  
    }

    public Cuenta generarCuenta(Pedido pedido, List<Impuesto> listaImpuestos) {
        Session sesion = null;
        Cuenta cuenta = null;
        
        //Calculamos los impuestos del pedido
        double impuestosTotal = 0;
        for (Impuesto impuesto : listaImpuestos) {
            impuestosTotal += ((impuesto.getValor() * pedido.getImporte()) / 100);
        }     
        
        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();     
            
            //Insertamos el pedido en la tabla cuenta
            String sql = "INSERT INTO cuenta (cantidad, id_pedido) VALUES (" + (pedido.getImporte()+impuestosTotal) + ", " + pedido.getId() + ")";
            SQLQuery query = sesion.createSQLQuery(sql);
            query.executeUpdate();            
            
            //Obtenemos la informacion de la cuenta que se acaba de generar
            String sql2 = "SELECT id_cuenta,cantidad,fecha_cuenta,id_pedido FROM cuenta WHERE id_pedido = " + pedido.getId();            
            SQLQuery query2 = sesion.createSQLQuery(sql2).addEntity(Cuenta.class);             
            cuenta = (Cuenta) query2.list().get(0);             
            
            //Insertamos los registros para saber los impuestos asociados a una cuenta
            for(Impuesto impuesto : listaImpuestos) {
                String sql3 = "INSERT INTO cuenta_impuesto (id_cuenta, id_impuesto) VALUES (" + cuenta.getId() + ", " + impuesto.getIdImpuesto() + ")";
                SQLQuery query3 = sesion.createSQLQuery(sql3);
                query3.executeUpdate();             
            }              
            
            //Actualizamos el estado de la mesa de ocupada a pendiente
            String sql4 = "UPDATE mesa SET estado_mesa = 'pendiente' WHERE numero_mesa = " + pedido.getNumeroMesa();     
            SQLQuery query4 =  sesion.createSQLQuery(sql4);
            query4.executeUpdate(); 
            
            //Si todo a ido bien realizamos la transaccion
            sesion.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al generar la cuenta pedido: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }  
        
        return cuenta;          
    }
    
    public int actualizarEstadoMesaPendienteALibre(int numeroMesa) {
        Session sesion = null;
        int resultadoOperacion;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();   
            
            //Actualizamos el estado de la mesa de pendiente a libre
            String sql = "UPDATE mesa SET estado_mesa = 'libre' WHERE numero_mesa = " + numeroMesa;     
            SQLQuery query =  sesion.createSQLQuery(sql);
            query.executeUpdate();              
            
            //Si todo a ido bien realizamos la transaccion
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch (Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error al generar la cuenta pedido: " + e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }  
        
        return resultadoOperacion;             
    }
}