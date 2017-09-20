package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
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
public class PersistenciaEvaluados implements IPersistenciaEvaluados {

    @Override
    public List<Evaluados> obtenerEvaluados(EntityManager em, String usuario, BigInteger secConvocatoria) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT R.SECUENCIA, R.EMPLEADO, CONCAT(CONCAT(CONCAT(CONCAT(PE.NOMBRE,' '),PE.PRIMERAPELLIDO),' '),PE.SEGUNDOAPELLIDO) NOMBREPERSONA, R.PUNTAJEOBTENIDO, \n"
                    + "R.FECHAPERIODODESDE, R.FECHAPERIODOHASTA, R.EVALCONVOCATORIA, \n"
                    + "R.NOMBREPRUEBA, R.ESTADOEVAL \n"
                    + "FROM EVALRESULTADOSCONV R, EMPLEADOS E, PERSONAS PE\n"
                    + "WHERE (EXISTS(SELECT 'X' \n"
                    + "              FROM EVALINDAGACIONES I \n"
                    + "			  WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA \n"
                    + "                     FROM USUARIOS U, PERSONAS P\n"
                    + "                     WHERE U.persona=P.secuencia\n"
                    + "                     AND U.ALIAS=?)\n"
                    + "              AND R.SECUENCIA=I.evalresultadoconv)\n"
                    + "      )\n"
                    + "AND R.EVALCONVOCATORIA = ?\n"
                    + "AND R.EMPLEADO = E.SECUENCIA\n"
                    + "AND E.PERSONA = PE.SECUENCIA ", Evaluados.class);
            q.setParameter(1, usuario);
            q.setParameter(2, secConvocatoria);
            List<Evaluados> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean actualizarPorcentaje(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT COUNT(*) FROM EVALPRUEBAS WHERE CONVOCATORIA = ?");
            q.setParameter(1, secConvocatoria);
            Integer total = ((BigDecimal) q.getSingleResult()).intValue();
            if (total != null && total != 0) {
                q = em.createNativeQuery("SELECT sum(nvl(a.puntoobtenido,0)*b.puntos)/100/?\n"
                        + "FROM evalindagaciones a, evalpruebas b\n"
                        + "WHERE a.evalprueba = b.secuencia\n"
                        + "AND a.evalresultadoconv = ?");
                q.setParameter(1, total);
                q.setParameter(2, secEvaluado);
                BigDecimal porcentaje = (BigDecimal) q.getSingleResult();
                if (porcentaje != null) {
                    q = em.createNativeQuery("UPDATE EVALRESULTADOSCONV A SET A.PUNTAJEOBTENIDO = ? WHERE A.SECUENCIA = ?");
                    q.setParameter(1, porcentaje);
                    q.setParameter(2, secEvaluado);
                    q.executeUpdate();
                }
                em.getTransaction().commit();
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaEvaluados.actualizarPorcentaje: " + ex);
            em.getTransaction().rollback();
            return false;
        }
    }
}
