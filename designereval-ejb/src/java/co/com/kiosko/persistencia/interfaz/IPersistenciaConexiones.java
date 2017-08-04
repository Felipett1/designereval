package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Conexiones;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaConexiones {

    public boolean insertarUltimaConexion(EntityManager em, Conexiones conexion);
    
}
