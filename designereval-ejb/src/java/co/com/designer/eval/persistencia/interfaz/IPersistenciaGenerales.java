package co.com.designer.eval.persistencia.interfaz;

import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaGenerales {

    public co.com.designer.eval.entidades.Generales consultarRutasGenerales(javax.persistence.EntityManager eManager);

    public String logoEmpresa(EntityManager eManager, String nit);
    
}
