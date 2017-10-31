package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.entidades.Generales;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaGenerales;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import co.com.designer.eval.administrar.interfaz.IAdministrarInicioEval;
import javax.ejb.Stateful;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarInicioEval implements IAdministrarInicioEval {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
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
            System.out.println("Error AdministrarInicioEval.obtenerConexion: " + e);
        }
    }

    @Override
    public String obtenerRutaImagenes() {
        String rutaFoto = null;
        try {
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            if (general != null) {
                rutaFoto = general.getPathfoto();
            } else {
                rutaFoto = null;
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarInicioEval.obtenerRutaImagenes: " + e);
        }
        return rutaFoto;
    }

    @Override
    public String logoEmpresa(String nit) {
        try {
            return persistenciaGenerales.logoEmpresa(em, nit);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicioEval.obtenerRutaImagenes: " + e);
            return null;
        }
    }
}
