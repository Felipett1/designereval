package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Convocatorias;
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

    public boolean cerrarEvaluaciones(EntityManager em, BigDecimal secConvocatoria);

    public List<Convocatorias> obtenerConvocatoriasAlcance(EntityManager em, String usuario);

    public String cerrarConvocatoria(EntityManager em, BigDecimal secConvocatoria);
}
