package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Personas;
import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuario(String usuario);

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public Personas conexionUsuario(String baseDatos, String usuario, String contraseña);

}
