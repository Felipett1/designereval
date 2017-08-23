package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.clasesAyuda.SessionEntityManager;
//import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarSesiones {

    public void adicionarSesion(SessionEntityManager session);

    public EntityManagerFactory obtenerConexionSesion(String idSesion);

    public void borrarSesion(String idSesion);
}
