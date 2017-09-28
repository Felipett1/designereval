package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarEvaluacion;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Respuestas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPreguntas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaRespuestas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarEvaluacion implements IAdministrarEvaluacion {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaPreguntas persistenciaPreguntas;
    @EJB
    private IPersistenciaRespuestas persistenciaRespuestas;
    @EJB
    private IPersistenciaPruebas persistenciaPruebas;
    @EJB
    private IPersistenciaEvaluados persistenciaEvaluados;
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Preguntas> obtenerCuestinonario(BigInteger secPrueba, BigInteger secIndagacion) {
        List<Preguntas> lst = obtenerPreguntas(secPrueba);
        EntityManager em = emf.createEntityManager();
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); i++) {
                lst.get(i).setRespuestas(obtenerRespuestas(lst.get(i).getSecuencia()));
                lst.get(i).setRespuesta(persistenciaRespuestas.consultarRespuesta(em, secIndagacion, lst.get(i).getSecuencia()));
                if (lst.get(i).getRespuesta() == null) {
                    lst.get(i).setNuevo(true);
                } else {
                    lst.get(i).setNuevo(false);
                }
            }
        }
        return lst;
    }

    public List<Preguntas> obtenerPreguntas(BigInteger secPrueba) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPreguntas.obtenerPreguntas(em, secPrueba);
    }

    public List<Respuestas> obtenerRespuestas(BigInteger secPregunta) {
        EntityManager em = emf.createEntityManager();
        return persistenciaRespuestas.obtenerRespuestas(em, secPregunta);
    }

    @Override
    public BigInteger obtenerNroPreguntas(BigInteger secPrueba) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPreguntas.obtenerNroPreguntas(em, secPrueba);
    }

    @Override
    public boolean registrarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        EntityManager em = emf.createEntityManager();
        return persistenciaRespuestas.registrarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
    }

    @Override
    public boolean actualizarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        EntityManager em = emf.createEntityManager();
        return persistenciaRespuestas.actualizarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
    }

    @Override
    public boolean eliminarRespuestas(BigInteger secIndagacion) {
        EntityManager em = emf.createEntityManager();
        return persistenciaRespuestas.eliminarRespuestas(em, secIndagacion);
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secPrueba, String observacion, double porcentaje) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPruebas.actualizarPorcentaje(em, secPrueba, observacion, porcentaje);
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        EntityManager em = emf.createEntityManager();
        return persistenciaEvaluados.actualizarPorcentaje(em, secConvocatoria, secEvaluado, agrupado);
    }

}
