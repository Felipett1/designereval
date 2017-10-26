package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Personas;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuario(String usuario);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public Personas conexionUsuario(String baseDatos, String usuario, String clave);

    public Conexiones ultimaConexionUsuario(String usuario);

    public boolean insertarUltimaConexion(Conexiones conexion);
    
    public String cambiarPassword(String usuario, String password);

}
