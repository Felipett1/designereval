package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Convocatorias;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConvocatorias;
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
public class PersistenciaConvocatorias implements IPersistenciaConvocatorias {

    @Override
    public List<Convocatorias> obtenerConvocatorias(EntityManager em, String usuario) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, \n"
                    + "(SELECT FECHAVIGENCIA FROM EVALVIGCONVOCATORIAS WHERE SECUENCIA = C.EVALVIGCONVOCATORIA) EVALVIGCONVOCATORIA,\n"
                    + "C.ESTADO, C.CODIGO, \n"
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE\n"
                    + "FROM EVALCONVOCATORIAS C\n"
                    + "WHERE C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) \n"
                    + "AND C.ESTADO = 'PROCESAR'\n"
                    + "AND ( EXISTS ( SELECT 1 \n"
                    + "               FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R \n"
                    + "               WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA\n"
                    + "                             FROM USUARIOS U, PERSONAS P\n"
                    + "                             WHERE U.persona = P.secuencia\n"
                    + "                             AND U.ALIAS = ?)\n"
                    + "               AND I.evalresultadoconv = R.SECUENCIA\n"
                    + "               AND R.evalconvocatoria = C.secuencia\n"
                    + "             ))\n"
                    + "ORDER BY EVALVIGCONVOCATORIA DESC", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerConvocatorias: " + ex);
            return null;
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(EntityManager em, String usuario) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT PERSONA FROM USUARIOS WHERE ALIAS = ?");
            q.setParameter(1, usuario);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerSecuenciaEvaluador: " + ex);
            return null;
        }
    }
}
