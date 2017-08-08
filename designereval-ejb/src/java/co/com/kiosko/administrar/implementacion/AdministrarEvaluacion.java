package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarEvaluacion;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Preguntas;
import co.com.kiosko.entidades.Respuestas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPreguntas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaRespuestas;
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

}
