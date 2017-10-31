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
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                em = emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.obtenerConexion: " + e);
        }
    }

    @Override
    public List<Preguntas> obtenerCuestinonario(BigInteger secPrueba, BigInteger secIndagacion) {
        List<Preguntas> lst = null;
        try {
            lst = obtenerPreguntas(secPrueba);
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
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.obtenerCuestinonario: " + e);
        }
        return lst;
    }

    public List<Preguntas> obtenerPreguntas(BigInteger secPrueba) {
        try {
            return persistenciaPreguntas.obtenerPreguntas(em, secPrueba);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.obtenerPreguntas: " + e);
            return null;
        }
    }

    public List<Respuestas> obtenerRespuestas(BigInteger secPregunta) {
        try {
            return persistenciaRespuestas.obtenerRespuestas(em, secPregunta);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.obtenerRespuestas: " + e);
            return null;
        }
    }

    @Override
    public BigInteger obtenerNroPreguntas(BigInteger secPrueba) {
        try {
            return persistenciaPreguntas.obtenerNroPreguntas(em, secPrueba);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.obtenerNroPreguntas: " + e);
            return null;
        }
    }

    @Override
    public boolean registrarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            return persistenciaRespuestas.registrarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.registrarRespuesta: " + e);
            return false;
        }
    }

    @Override
    public boolean actualizarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            return persistenciaRespuestas.actualizarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.actualizarRespuesta: " + e);
            return false;
        }
    }

    @Override
    public boolean eliminarRespuestas(BigInteger secIndagacion) {
        try {
            return persistenciaRespuestas.eliminarRespuestas(em, secIndagacion);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.eliminarRespuestas: " + e);
            return false;
        }
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secPrueba, String observacion, double porcentaje) {
        try {
            return persistenciaPruebas.actualizarPorcentaje(em, secPrueba, observacion, porcentaje);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.actualizarPorcentaje: " + e);
            return false;
        }
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        try {
            return persistenciaEvaluados.actualizarPorcentaje(em, secConvocatoria, secEvaluado, agrupado);
        } catch (Exception e) {
            System.out.println("Error AdministrarEvaluacion.actualizarPorcentaje2: " + e);
            return false;
        }
    }

}
