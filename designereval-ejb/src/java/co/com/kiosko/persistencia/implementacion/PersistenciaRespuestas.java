package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Respuestas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaRespuestas;
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
public class PersistenciaRespuestas implements IPersistenciaRespuestas {

    @Override
    public List<Respuestas> obtenerRespuestas(EntityManager em, BigInteger secPregunta) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CUALITATIVO, CUANTITATIVO, DESCRIPCION\n"
                    + "FROM EVALRESPUESTAS RES\n"
                    + "WHERE RES.EVALPREGUNTAS = ?\n"
                    + "ORDER BY CUANTITATIVO ASC", Respuestas.class);
            q.setParameter(1, secPregunta);
            List<Respuestas> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaRespuestas.obtenerRespuestas: " + ex);
            return null;
        }
    }

    @Override
    public boolean registrarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("INSERT INTO EVALRESPUESTASINDAGACIONES (EVALINDAGACION, EVALPREGUNTA, EVALRESPUESTA, CUALITATIVOASIGNADO, CUANTITATIVOASIGNADO )\n"
                    + "VALUES ( ?, ?, ?, \n"
                    + "(SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?),\n"
                    + "(SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?)\n"
                    + ")");
            q.setParameter(1, secIndagacion);
            q.setParameter(2, secPregunta);
            q.setParameter(3, secRespuesta);
            q.setParameter(4, secRespuesta);
            q.setParameter(5, secRespuesta);
            q.executeUpdate();
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaRespuestas.registrarRespuesta: " + ex);
            return false;
        }
    }

    public boolean actualizarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("UPDATE EVALRESPUESTASINDAGACIONES \n"
                    + "SET CUALITATIVOASIGNADO = (SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = :?) ,\n"
                    + "CUANTITATIVOASIGNADO = (SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) ,\n"
                    + "EVALRESPUESTA = :?\n"
                    + "WHERE EVALINDAGACION = ?\n"
                    + "EVALPREGUNTA = ?");
            q.setParameter(1, secRespuesta);
            q.setParameter(2, secRespuesta);
            q.setParameter(3, secRespuesta);
            q.setParameter(4, secIndagacion);
            q.setParameter(5, secPregunta);
            q.executeUpdate();
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaRespuestas.registrarRespuesta: " + ex);
            return false;
        }
    }

    @Override
    public BigInteger consultarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT EVALRESPUESTA\n"
                    + "FROM EVALRESPUESTASINDAGACIONES \n"
                    + "WHERE EVALINDAGACION = ?\n"
                    + "AND EVALPREGUNTA = ?");
            q.setParameter(1, secIndagacion);
            q.setParameter(2, secPregunta);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
            em.getTransaction().commit();
            return resultado.toBigInteger();
        } catch (Exception ex) {
            System.out.println("Error PersistenciaRespuestas.consultarRespuesta: " + ex);
            return null;
        }
    }
}
