package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Evaluados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaEvaluados {

    public List<Evaluados> obtenerEvaluados(EntityManager em, String usuario, BigInteger secConvocatoria);

    public boolean actualizarPorcentaje(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado);
    
}
