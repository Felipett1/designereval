package co.com.kiosko.conexionFuente.implementacion;

import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
@Stateless
public class SesionEntityManagerFactory implements ISesionEntityManagerFactory, Serializable {

    @Override
    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia) {
        try {
            return Persistence.createEntityManagerFactory(unidadPersistencia);
        } catch (Exception e) {
            System.out.println("Error SesionEntityManagerFactory.crearConexionUsuario: " + e);
            return null;
        }
    }

    @Override
    public EntityManagerFactory crearFactoryUsuario(String usuario, String clave, String baseDatos) {
        try {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", usuario);
            properties.put("javax.persistence.jdbc.password", clave);
            /*System.out.println("Usuario: " + usuario);
            System.out.println("Contraseña: " + contraseña);
            System.out.println("Base de Datos: " + baseDatos);*/
            return Persistence.createEntityManagerFactory(baseDatos, properties);
        } catch (Exception e) {
            System.out.println("Error crearFactoryUsuario: " + e.getMessage());
            return null;
        }
    }
}
