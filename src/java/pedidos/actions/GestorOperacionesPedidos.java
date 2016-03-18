/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedidos.actions;

import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import pedidos.modelo.Pedido;
import administracion.modelo.Producto;
import org.hibernate.HibernateException;
import pedidos.modelo.Hamburguesa;

/**
 *
 * @author juang
 */
public class GestorOperacionesPedidos {

    private static final int OPERACION_SUCCESS = 1;
    private static final int OPERACION_ERROR = 0;  
    
    public int insertarPedido(Pedido pedido) {
        Session sesion = null;
        int resultadoOperacion;

        try {
            HibernateUtil hb = new HibernateUtil();
            sesion = hb.getSessionFactory().openSession();
            sesion.beginTransaction();
                    
            //Insertamos el pedido en la tabla pedido
            String sql = "INSERT INTO pedido (numero_mesa,id_usuario) VALUES ("+pedido.getNumeroMesa()+", "+pedido.getIdUsuario()+")";
            SQLQuery query =  sesion.createSQLQuery(sql);
            query.executeUpdate();
           
            //Obtenemos el id del pedido que se acaba de generar de forma automatica
            String sql2 = "SELECT MAX(id_pedido) AS id_pedido,numero_mesa,id_usuario FROM pedido";
            SQLQuery query2 = sesion.createSQLQuery(sql2).addEntity(Pedido.class);
            List<Pedido> lista = query2.list(); 
            int idPedido = lista.get(0).getId();
            pedido.setId(idPedido); //Le asignamos el id que se genera de forma automatica en BD al pedido

            //Actualizamos el estado de la mesa de libre a ocupada
            String sql3 = "UPDATE mesa SET estado_mesa = 'ocupada' WHERE numero_mesa = " + pedido.getNumeroMesa();     
            SQLQuery query3 =  sesion.createSQLQuery(sql3);
            query3.executeUpdate();
      
            //Insertamos las lineas de pedido
            Producto producto;
            for(int i=1; i<=pedido.getListaProductos().size(); i++) { //La i representa las lineas de pedido
                producto = pedido.getListaProductos().get(i-1); //Obtenemos el producto de la iteracion (simplemente para tener un codigo mas limpio)
                String sql4 = "INSERT INTO producto_linea_pedido (numero_linea, id_pedido, id_producto, unidades) VALUES ("+i+", "+pedido.getId()+", "+producto.getId()+", "+producto.getUnidades()+")";     
                SQLQuery query4 =  sesion.createSQLQuery(sql4);
                query4.executeUpdate();
                
                if(producto instanceof Hamburguesa) { //Si el producto es una hamburguesa miramos si tiene productos extra
                    Producto extra;
                    for(int j=0; j<((Hamburguesa) producto).getListaProductosExtra().size(); j++) {
                        extra = ((Hamburguesa) producto).getListaProductosExtra().get(j); //Obtenemos el producto extra
                        //La linea pedido sigue siendo la i del bucle for de fuera
                        String sql5 = "INSERT INTO producto_linea_pedido (numero_linea, id_pedido, id_producto, unidades) VALUES ("+i+", "+pedido.getId()+", "+extra.getId()+", "+extra.getUnidades()+")";     
                        SQLQuery query5 =  sesion.createSQLQuery(sql5);
                        query5.executeUpdate();                        
                    }
                }
            }            
         
            //Si todo a ido bien realizamos la transaccion
            sesion.getTransaction().commit();
            resultadoOperacion = OPERACION_SUCCESS;
        } catch (HibernateException e) { 
            System.out.println("Error en la conexion con la base de datos: " + e);
            throw e;
        } catch(Exception e) {
            resultadoOperacion = OPERACION_ERROR;
            System.out.println("Error al insertar pedido: " + e);
        } finally {
            if(sesion != null) {
                sesion.close();
            }
        }

        return resultadoOperacion;         
    }    
}