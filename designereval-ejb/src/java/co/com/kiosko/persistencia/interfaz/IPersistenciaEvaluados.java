package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Evaluados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaEvaluados {

    public List<Evaluados> obtenerEvaluados(EntityManager em, String usuario, BigInteger secConvocatoria);
    
}
