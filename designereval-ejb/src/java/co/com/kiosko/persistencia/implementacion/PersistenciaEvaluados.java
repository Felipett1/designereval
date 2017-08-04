package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Evaluados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEvaluados;
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
            return null;
        }
    }
}
