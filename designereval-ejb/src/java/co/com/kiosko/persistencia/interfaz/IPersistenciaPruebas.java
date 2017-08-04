package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Pruebas;
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
