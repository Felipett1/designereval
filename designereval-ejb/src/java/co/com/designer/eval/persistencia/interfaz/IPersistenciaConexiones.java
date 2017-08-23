package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaConexiones {

    public boolean insertarUltimaConexion(EntityManager em, Conexiones conexion);
    
}
