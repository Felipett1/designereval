package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Conexiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexiones;
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
            return false;
        }
    }
}
