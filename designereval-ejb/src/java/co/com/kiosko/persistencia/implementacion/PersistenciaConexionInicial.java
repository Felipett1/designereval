package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Perfiles;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Stateless
public class PersistenciaConexionInicial implements IPersistenciaConexionInicial {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    private EntityManager em;

    @Override
    public boolean validarUsuario(EntityManager eManager, String usuario) {
        try {
            em = eManager;
            em.getTransaction().begin();
            String sqlQuery = "SET ROLE ROLENTRADA";
            Query query = em.createNativeQuery(sqlQuery);
            query.executeUpdate();
            sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S'";
            query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            em.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario es correcto y esta activo");
                return true;
            } else {
                System.out.println("El usuario es incorrecto y/o esta inactivo");
                em.getEntityManagerFactory().close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error validarUsuario: " + e);
            return false;
        }
    }

    @Override
    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT p.secuencia FROM Usuarios u, Perfiles p WHERE u.alias = ? AND u.perfil = p.secuencia");
            query.setParameter(1, usuarioBD);
            em.getTransaction().commit();
            BigDecimal secPerfil = (BigDecimal) query.getSingleResult();
            return secPerfil.toBigInteger();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.usuarioLogin() e: " + e);
            return null;
        }
    }

    @Override
    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
            query.setParameter("secPerfil", secPerfil);
            Perfiles perfil = (Perfiles) query.getSingleResult();
            em.getTransaction().commit();
            return perfil;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
            return null;
        }
    }

    @Override
    public void setearUsuario(EntityManager eManager, String rol, String pwd) {
        try {
            String texto = "SET ROLE " + rol + " IDENTIFIED BY " + pwd;
            em = eManager;
            em.getTransaction().begin();
            String sqlQuery = texto;
            Query query = em.createNativeQuery(sqlQuery);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.setearUsuario() e: " + e);
        }
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa) {
        boolean resultado = false;
        try {
//            eManager.getTransaction().begin();
//            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e "
//                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
//                    + "AND e.codigoempleado = ? "
//                    + "AND ck.PWD = GENERALES_PKG.ENCRYPT(?)";
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "AND e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND ck.PWD = GENERALES_PKG.ENCRYPT(?) "
                    + "AND em.nit = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, clave);
            query.setParameter(3, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
//            eManager.getTransaction().commit();
            /*if (instancia > 0) {
             //System.out.println("El usuario y clave son correctos.");
             return true;
             } else {
             //System.out.println("El usuario o clave son incorrectos");
             return false;
             }*/
            resultado = instancia > 0;
//            return resultado;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuarioRegistrado: " + e);
//            return false;
            resultado = false;
        }
        return resultado;
    }

    @Override
    public EntityManager validarConexionUsuario(EntityManagerFactory emf) {
        try {
            EntityManager eManager = emf.createEntityManager();
            if (eManager.isOpen()) {
                return eManager;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarConexionUsuario : " + e);
            try {
                emf.close();
            } catch (NullPointerException npe) {
                System.out.println("error de nulo en el entity manager.");
            }
        }
        return null;
    }
}
