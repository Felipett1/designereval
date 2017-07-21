package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import co.com.kiosko.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import co.com.kiosko.entidades.Perfiles;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
//@Stateless
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso {

    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    private IAdministrarSesiones administrarSessiones;

    private String unidadPersistencia;
    private final ISesionEntityManagerFactory sessionEMF;

    private EntityManagerFactory emf;
    private BigInteger secPerfil;
    private Perfiles perfilUsuario;

    public AdministrarIngreso() {
        sessionEMF = new SesionEntityManagerFactory();
    }

    @Override
    public boolean conexionIngreso(String unidadPersistencia) {
        System.out.println(this.getClass().getName() + ".conexionIngreso()");
        System.out.println("Unidad de persistencia: " + unidadPersistencia);
        boolean resul = false;
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
    public boolean conexionUsuario(String baseDatos, String usuario, String contraseña) {
        try {
            EntityManager em = emf.createEntityManager();
            secPerfil = persistenciaConexionInicial.usuarioLogin(em, usuario);
            perfilUsuario = persistenciaConexionInicial.perfilUsuario(em, secPerfil);
            em.close();
            emf.close();
            emf = sessionEMF.crearFactoryUsuario(usuario, contraseña, baseDatos);
            setearRol();
            return true;
        } catch (Exception e) {
            System.out.println("Error creando EMF AdministrarIngreso.conexionUsuario: " + e);
            return false;
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
    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa) {
        System.out.println(this.getClass().getName() + ".validarIngresoUsuarioRegistrado()");
//        EntityManagerFactory emf;
        boolean resul = false;
        try {
//            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                resul = persistenciaConexionInicial.validarIngresoUsuarioRegistrado(em, usuario, clave, nitEmpresa);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
        }
        return resul;
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion) {
        System.out.println(this.getClass().getName() + ".adicionarConexionUsuario()");
        boolean resul = false;
        try {
            SessionEntityManager sem = new SessionEntityManager(idSesion, unidadPersistencia);
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
}//Fin de la clase.
