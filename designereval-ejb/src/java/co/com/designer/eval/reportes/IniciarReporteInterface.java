package co.com.designer.eval.reportes;

import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IniciarReporteInterface {

    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Map parametros, EntityManager em);
}
