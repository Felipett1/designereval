package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarIngreso;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.clasesAyuda.SessionEntityManager;
import co.com.designer.eval.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.designer.eval.conexionFuente.interfaz.ISesionEntityManagerFactory;
import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Perfiles;
import co.com.designer.eval.entidades.Personas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexionInicial;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexiones;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso {

    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    private IPersistenciaConexiones persistenciaConexiones;
    @EJB
    private IAdministrarSesiones administrarSessiones;

    private String unidadPersistencia;
    private final ISesionEntityManagerFactory sessionEMF;

    private EntityManagerFactory emf;
    private BigInteger secPerfil;
    private Perfiles perfilUsuario;
    private Personas persona;

    public AdministrarIngreso() {
        sessionEMF = new SesionEntityManagerFactory();
    }

    @Override
    public boolean conexionIngreso(String unidadPersistencia) {
        System.out.println(this.getClass().getName() + ".conexionIngreso()");
        System.out.println("Unidad de persistencia: " + unidadPersistencia);
        boolean resul;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                resul = true;
            } else {
                System.out.println("Error la unidad de persistencia no existe, revisar el archivo XML de persistencia.");
                resul = false;
            }
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
            emf = null;
        }
        if (resul) {
            this.unidadPersistencia = unidadPersistencia;
            System.out.println("Unid. Pesistencia asignada.");
        }
        return resul;
    }

    @Override
    public boolean validarUsuario(String usuario) {
        System.out.println(this.getClass().getName() + ".validarUsuario()");
        boolean resul = false;
        try {
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                resul = persistenciaConexionInicial.validarUsuario(em, usuario);
                em.close();
            }
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
        }
        return resul;
    }

    @Override
    public Personas conexionUsuario(String baseDatos, String usuario, String clave) {
        try {
            EntityManager em = emf.createEntityManager();
            secPerfil = persistenciaConexionInicial.usuarioLogin(em, usuario);
            perfilUsuario = persistenciaConexionInicial.perfilUsuario(em, secPerfil);
            em.close();
            emf.close();
            emf = sessionEMF.crearFactoryUsuario(usuario, clave, baseDatos);
            setearRol();
            em = emf.createEntityManager();
            persona = persistenciaConexionInicial.obtenerPersona(em, usuario);
            return persona;
        } catch (Exception e) {
            System.out.println("Error creando EMF AdministrarIngreso.conexionUsuario: " + e);
            return null;
        }
    }

    public boolean setearRol() {
        try {
            EntityManager em = emf.createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    persistenciaConexionInicial.setearUsuario(em, perfilUsuario.getDescripcion(), perfilUsuario.getPwd());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarConexionUsuario: " + e);
            return false;
        }
    }

    @Override
    public Conexiones ultimaConexionUsuario(String usuario) {
        try {
            EntityManager em = emf.createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    return persistenciaConexionInicial.conexionUsuario(em, usuario);
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.ultimaConexionUsuario: " + e);
            return null;
        }
    }

    @Override
    public boolean insertarUltimaConexion(Conexiones conexion) {
        try {
            EntityManager em = emf.createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    return persistenciaConexiones.insertarUltimaConexion(em, conexion);
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.insertarUltimaConexion: " + e);
            return false;
        }
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion) {
        System.out.println(this.getClass().getName() + ".adicionarConexionUsuario()");
        boolean resul;
        try {
            SessionEntityManager sem = new SessionEntityManager(idSesion, unidadPersistencia, emf);
            administrarSessiones.adicionarSesion(sem);
            resul = true;
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
        }
        return resul;
    }

    @Override
    public void cerrarSession(String idSesion) {
        System.out.println(this.getClass().getName() + ".modificarUltimaConexion()");
        try {
            administrarSessiones.borrarSesion(idSesion);
            emf.close();
        } catch (Exception e) {
            System.out.println("Error general " + "cerrarSession" + ": " + e);
        }
    }

    public Personas getPersona() {
        return persona;
    }

}//Fin de la clase.
