package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.clasesAyuda.SessionEntityManager;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements IAdministrarSesiones {

    private final List<SessionEntityManager> sessionesActivas;

    public AdministrarSesiones() {
        sessionesActivas = new ArrayList<>();
    }

    @Override
    public void adicionarSesion(SessionEntityManager session) {
        System.out.println(this.getClass().getName() + "." + "adicionarSesion" + "()");
        sessionesActivas.add(session);
    }

    @Override
    public EntityManagerFactory obtenerConexionSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "obtenerConexionSesion" + "()");
        SessionEntityManager sesionActual = null;
        try {
            if (!sessionesActivas.isEmpty()) {
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                        sesionActual = sessionesActivas.get(i);
                        i = sessionesActivas.size();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error en " + "obtenerConexionSesion");
            System.out.println("Causa: " + e);
            sesionActual = null;
        }
        EntityManagerFactory emf = null;
        if (sesionActual != null) {
            emf = sesionActual.getEmf();
            System.out.println("Se creó entityManagerFactory.");
            System.out.println("eManager" + emf.toString());
        }
        return emf;
    }

    @Override
    public void borrarSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "borrarSesion()");
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                    sessionesActivas.get(i).setIdSession("");
                    sessionesActivas.remove(sessionesActivas.get(i));
                    i = sessionesActivas.size();
                }
            }
        }
    }
}
