package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.ConfiguracionCorreo;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaConfiguracionCorreo {

    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(javax.persistence.EntityManager eManager, String nitEmpresa);
    
}
