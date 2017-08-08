package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Pruebas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPruebas;
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
public class PersistenciaPruebas implements IPersistenciaPruebas {

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(EntityManager em, String usuario, BigInteger secEmplConvo) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT EI.SECUENCIA, EI.EVALRESULTADOCONV, EI.PUNTOOBTENIDO,\n"
                    + "EI.OBSEVALUADOR, EI.OBSEVALUADO, EI.EMPLEADOEVALUADOR, \n"
                    + "(SELECT PL.CODIGO||'-'||PL.DESCRIPCION\n"
                    + "FROM EVALPRUEBAS PR, EVALPLANILLAS PL\n"
                    + "WHERE PR.PLANILLA = PL.SECUENCIA\n"
                    + "AND PR.SECUENCIA = EI.EVALPRUEBA) PRUEBA, -- Usa evalpruebas para llegar a los campos de evalplanillas\n"
                    + "(SELECT PR.DESCRIPCION\n"
                    + "FROM EVALPRUEBAS PR\n"
                    + "WHERE PR.SECUENCIA = EI.EVALPRUEBA) FACTOR, -- Usa la descripción de evalpruebas para indicar el factor\n"
                    + "(SELECT PR.SECUENCIA\n"
                    + "FROM EVALPRUEBAS PR\n"
                    + "WHERE PR.SECUENCIA = EI.EVALPRUEBA) SECPRUEBA\n"
                    + "FROM EVALINDAGACIONES EI\n"
                    + "WHERE EI.EMPLEADOEVALUADOR=(\n"
                    + "           SELECT P.SECUENCIA \n"
                    + "           FROM USUARIOS U, PERSONAS P\n"
                    + "           WHERE U.persona=P.secuencia\n"
                    + "           AND U.ALIAS=?)\n"
                    + "AND EI.EVALRESULTADOCONV = ? ", Pruebas.class);
            q.setParameter(1, usuario);
            q.setParameter(2, secEmplConvo);
            List<Pruebas> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaPruebas.obtenerPruebasEvalaudo: " + ex);
            return null;
        }
    }
}
