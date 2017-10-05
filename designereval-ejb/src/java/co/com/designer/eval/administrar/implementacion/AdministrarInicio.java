package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.correo.EnvioCorreo;
import co.com.designer.eval.entidades.ConfiguracionCorreo;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Generales;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaGenerales;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaUtilidadesBD;
import co.com.designer.eval.reportes.IniciarReporteInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarInicio implements IAdministrarInicio {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConvocatorias persistenciaConvocatorias;
    @EJB
    private IPersistenciaPruebas persistenciaPruebas;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaEvaluados persistenciaEvaluados;
    @EJB
    private IPersistenciaConfiguracionCorreo persistenciaConfiguracionCorreo;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    @EJB
    private IniciarReporteInterface reporte;
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerConvocatorias(em, usuario);
    }

    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerConvocatoriasAlcance(em, usuario);
    }

    @Override
    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaEvaluados.obtenerEvaluados(em, usuario, secConvocatoria);
    }

    @Override
    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.totalEmpleadosEvaluador(em, secuenciaEvaluador);

    }

    @Override
    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.cantidadEvaluadosConvocatoria(em, secConvocatoria);
    }

    @Override
    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.totalEmpleadosEvaluadorConvocatoria(em, secuenciaEvaluador, secConvocatoria);
    }

    @Override
    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaUtilidadesBD.cantidadEvaluados(em, secuenciaEvaluador, secConvocatoria);
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(String usuario) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.obtenerSecuenciaEvaluador(em, usuario);
    }

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPruebas.obtenerPruebasEvaluado(em, usuario, secEmplConvo);
    }

    @Override
    public boolean cerrarConvocatoria(BigDecimal secConvocatoria) {
        EntityManager em = emf.createEntityManager();
        return persistenciaConvocatorias.cerrarConvocatoria(em, secConvocatoria);
    }

    @Override
    public boolean enviarCorreo(String nitEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        System.out.println(this.getClass().getName() + ".enviarCorreo()");
        boolean resul = false;
        try {
            EntityManager em = emf.createEntityManager();
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, nitEmpresa);
            em.close();
            resul = EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
        } catch (Exception e) {
            System.out.println("enviarCorreo: " + e);
        }
        return resul;
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros, String nombreConvocatoria) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        try {
            EntityManager em = emf.createEntityManager();
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            em.close();
            Calendar fecha = Calendar.getInstance();
            String pathReporteGenerado = null;
            String nombreArchivo;
            if (general != null) {
//                nombreArchivo = "EVAL - Reporte resumen convocatoria " + nombreConvocatoria ;
                nombreArchivo = "EVAL-resumen_convocatoria_" + nombreConvocatoria + fecha.get(Calendar.YEAR)+"_"+(fecha.get(Calendar.MONTH)+1)+"_"+fecha.get(Calendar.DAY_OF_MONTH)+"_"+fecha.get(Calendar.HOUR)+"_"+fecha.get(Calendar.MINUTE)+"_"+fecha.get(Calendar.SECOND)+"_"+fecha.get(Calendar.MILLISECOND);
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                } else if (tipoReporte.equals("XLSX")) {
                    nombreArchivo = nombreArchivo + ".xlsx";
                } else if (tipoReporte.equals("XLS")) {
                    nombreArchivo = nombreArchivo + ".xls";
                } else if (tipoReporte.equals("CSV")) {
                    nombreArchivo = nombreArchivo + ".csv";
                } else if (tipoReporte.equals("HTML")) {
                    nombreArchivo = nombreArchivo + ".html";
                } else if (tipoReporte.equals("DOCX")) {
                    nombreArchivo = nombreArchivo + ".rtf";
                }

                parametros.put("pathImagenes", rutaReporte);
                EntityManager em2 = emf.createEntityManager();
                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte,
                        rutaReporte, rutaGenerado, nombreArchivo, tipoReporte,
                        parametros, em2);
                em2.close();
                return pathReporteGenerado;
            }
            return pathReporteGenerado;
        } catch (Exception ex) {
            System.out.println("Error AdministrarInicio.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public boolean actualizarEstado(BigInteger secPrueba, String estado) {
        EntityManager em = emf.createEntityManager();
        return persistenciaPruebas.actualizarEstado(em, secPrueba, estado);
    }
}
