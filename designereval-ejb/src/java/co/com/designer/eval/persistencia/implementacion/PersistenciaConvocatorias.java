package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
//import javax.persistence.PersistenceException;
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
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, "
                    + "(SELECT FECHAVIGENCIA FROM EVALVIGCONVOCATORIAS WHERE SECUENCIA = C.EVALVIGCONVOCATORIA) EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C "
                    + "WHERE C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'PROCESAR' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + " FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + " WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    + "  FROM USUARIOS U, PERSONAS P "
                    + "  WHERE U.persona = P.secuencia "
                    + "  AND U.ALIAS = ? ) "
                    + " AND I.evalresultadoconv = R.SECUENCIA "
                    + " AND R.evalconvocatoria = C.secuencia "
                    + " )) "
                    + "ORDER BY EVALVIGCONVOCATORIA DESC", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerConvocatorias: " + ex);
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(EntityManager em, String usuario) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, "
                    + "(SELECT FECHAVIGENCIA FROM EVALVIGCONVOCATORIAS WHERE SECUENCIA = C.EVALVIGCONVOCATORIA) EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C "
                    + "WHERE C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'ALCANCE' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + "               FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + "               WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    + "                             FROM USUARIOS U, PERSONAS P "
                    + "                             WHERE U.persona = P.secuencia "
                    + "                             AND U.ALIAS = ?) "
                    + "               AND I.evalresultadoconv = R.SECUENCIA "
                    + "               AND R.evalconvocatoria = C.secuencia "
                    + "             )) "
                    + "order by EVALVIGCONVOCATORIA DESC ", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerConvocatoriasAlcance: " + ex);
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(EntityManager em, String usuario) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT PERSONA FROM USUARIOS WHERE ALIAS = ? ");
            q.setParameter(1, usuario);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerSecuenciaEvaluador: " + ex);
            return null;
        }
    }

    @Override
    public boolean cerrarConvocatoria(EntityManager em, BigDecimal secConvocatoria) {
        em.getTransaction().begin();
        Query q = em.createNativeQuery("CALL EVALCONVOCATORIAS_PKG.CERRAREVALUACION(?) ");
        q.setParameter(1, secConvocatoria);
        q.executeUpdate();
        em.getTransaction().commit();
        return true;
    }
}
