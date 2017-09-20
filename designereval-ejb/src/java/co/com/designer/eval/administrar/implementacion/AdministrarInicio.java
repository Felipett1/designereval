package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaUtilidadesBD;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Trivi�o
 */
@Stateful
public class AdministrarInicio implements IAdministrarInicio {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConvocatorias persistenciaConvocatorias;
    @EJB
    private IPersistenciaPruebas persistenciaPruebas;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaEvaluados persistenciaEvaluados;
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerConvocatorias(em, usuario);
    }
    
    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerConvocatoriasAlcance(em, usuario);
    }

    @Override
    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaEvaluados.obtenerEvaluados(em, usuario, secConvocatoria);
    }

    @Override
    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.totalEmpleadosEvaluador(em, secuenciaEvaluador);

    }

    @Override
    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.cantidadEvaluadosConvocatoria(em, secConvocatoria);
    }

    @Override
    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.totalEmpleadosEvaluadorConvocatoria(em, secuenciaEvaluador, secConvocatoria);
    }

    @Override
    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.cantidadEvaluados(em, secuenciaEvaluador, secConvocatoria);
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerSecuenciaEvaluador(em, usuario);
    }

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPruebas.obtenerPruebasEvaluado(em, usuario, secEmplConvo);
    }

    @Override
    public boolean cerrarConvocatoria(BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.cerrarConvocatoria(em, secConvocatoria);
    }
}
