package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Pruebas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaPruebas {

    public List<Pruebas> obtenerPruebasEvaluado(EntityManager em, String usuario, BigInteger secEmplConvo);
    
}
