package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPreguntas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaPreguntas implements IPersistenciaPreguntas {

    @Override
    public List<Preguntas> obtenerPreguntas(EntityManager em, BigInteger secPrueba) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CONSECUTIVO, DESCRIPCION, OBSERVACIONES \n"
                    + "FROM EVALPREGUNTAS\n"
                    + "WHERE EVALPLANILLA=(SELECT PL.SECUENCIA \n"
                    + "       FROM EVALPRUEBAS PR, EVALPLANILLAS PL\n"
                    + "	   WHERE PR.PLANILLA = PL.SECUENCIA\n"
                    + "	   AND PR.SECUENCIA = ?\n"
                    + "       )\n"
                    + "ORDER BY CONSECUTIVO ASC", Preguntas.class);
            q.setParameter(1, secPrueba);
            List<Preguntas> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaPreguntas.obtenerPreguntas: " + ex);
            return null;
        }
    }

    @Override
    public BigInteger obtenerNroPreguntas(EntityManager em, BigInteger secPrueba) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT PL.NUMEROPREGUNTA\n"
                    + "FROM EVALPRUEBAS PR, EVALPLANILLAS PL\n"
                    + "WHERE PR.PLANILLA = PL.SECUENCIA\n"
                    + "AND PR.SECUENCIA = ? ");
            q.setParameter(1, secPrueba);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
            em.getTransaction().commit();
            return resultado.toBigInteger();
        } catch (Exception ex) {
            System.out.println("Error PersistenciaPreguntas.obtenerPreguntas: " + ex);
            return null;
        }
    }
}
