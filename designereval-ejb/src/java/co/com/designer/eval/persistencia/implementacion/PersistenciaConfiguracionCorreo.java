package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.ConfiguracionCorreo;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConfiguracionCorreo implements IPersistenciaConfiguracionCorreo {

    @Override
    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(EntityManager eManager, String nitEmpresa) {
        System.out.println(this.getClass().getName() + "." + "consultarConfiguracionServidorCorreo" + "()");
        ConfiguracionCorreo cc = null;
        try {
            if (eManager != null && eManager.isOpen()) {
                eManager.getTransaction().begin();
                String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.nit = :nitEmpresa";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("nitEmpresa", Long.valueOf(nitEmpresa));
                cc = (ConfiguracionCorreo) query.getSingleResult();
                eManager.getTransaction().commit();
            } else {
                cc = null;
                System.out.println("entityManager nulo.");
            }
        } catch (IllegalStateException ise) {
            System.out.println("ERROR: " + ise.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
        }
        return cc;
    }
}
