package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaConexiones {

    public BigInteger consultarSIDActual(EntityManager em) throws Exception;
    public boolean insertarUltimaConexion(EntityManager em, Conexiones conexion);
    
}
