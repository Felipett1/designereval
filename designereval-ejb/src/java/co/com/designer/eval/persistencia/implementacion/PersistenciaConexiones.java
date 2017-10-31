package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexiones;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaConexiones implements IPersistenciaConexiones {

    @Override
    public boolean insertarUltimaConexion(EntityManager em, Conexiones conexion) {
        try {
            em.getTransaction().begin();
            em.persist(conexion);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConexiones.insertarUltimaConexion: " + ex);
            terminarTransaccionException(em);
            return false;
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
