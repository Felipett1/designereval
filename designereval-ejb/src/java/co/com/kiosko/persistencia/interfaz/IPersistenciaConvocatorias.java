package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Convocatorias;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaConvocatorias {

    public List<Convocatorias> obtenerConvocatorias(EntityManager em, String usuario);

    public BigDecimal obtenerSecuenciaEvaluador(EntityManager em, String usuario);

}
