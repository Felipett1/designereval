package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Preguntas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Familia Triviño Amad
 */
public interface IPersistenciaPreguntas {

    public List<Preguntas> obtenerPreguntas(EntityManager em, BigInteger secPrueba);

    public BigInteger obtenerNroPreguntas(EntityManager em, BigInteger secPrueba);
    
}
