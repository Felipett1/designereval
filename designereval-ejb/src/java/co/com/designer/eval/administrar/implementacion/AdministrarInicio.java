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
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                em = emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerConexion: " + e);
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(String usuario) {
        try {
            return persistenciaConvocatorias.obtenerConvocatorias(em, usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerConvocatorias: " + e);
            return null;
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(String usuario) {
        try {
            return persistenciaConvocatorias.obtenerConvocatoriasAlcance(em, usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerConvocatoriasAlcance: " + e);
            return null;
        }
    }

    @Override
    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria) {
        try {
            return persistenciaEvaluados.obtenerEvaluados(em, usuario, secConvocatoria);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerEvaluados: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador) {
        try {
            return persistenciaUtilidadesBD.totalEmpleadosEvaluador(em, secuenciaEvaluador);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.totalEmpleadosEvaluador: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria) {
        try {
            return persistenciaUtilidadesBD.cantidadEvaluadosConvocatoria(em, secConvocatoria);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.cantidadEvaluadosConvocatoria: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        try {
            return persistenciaUtilidadesBD.totalEmpleadosEvaluadorConvocatoria(em, secuenciaEvaluador, secConvocatoria);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.totalEmpleadosEvaluadorConvocatoria: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        try {
            return persistenciaUtilidadesBD.cantidadEvaluados(em, secuenciaEvaluador, secConvocatoria);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.cantidadEvaluados: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(String usuario) {
        try {
            return persistenciaConvocatorias.obtenerSecuenciaEvaluador(em, usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerSecuenciaEvaluador: " + e);
            return null;
        }
    }

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo) {
        try {
            return persistenciaPruebas.obtenerPruebasEvaluado(em, usuario, secEmplConvo);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.obtenerPruebasEvaluado: " + e);
            return null;
        }
    }

    @Override
    public boolean cerrarConvocatoria(BigDecimal secConvocatoria) {
        try {
            return persistenciaConvocatorias.cerrarConvocatoria(em, secConvocatoria);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.cerrarConvocatoria: " + e);
            return false;
        }
    }

    @Override
    public boolean enviarCorreo(String nitEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        System.out.println(this.getClass().getName() + ".enviarCorreo()");
        boolean resul = false;
        try {
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, nitEmpresa);
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
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            Calendar fecha = Calendar.getInstance();
            String pathReporteGenerado = null;
            String nombreArchivo;
            if (general != null) {
                nombreArchivo = "EVAL-resumen_convocatoria_" + nombreConvocatoria + fecha.get(Calendar.YEAR) + "_" + (fecha.get(Calendar.MONTH) + 1) + "_" + fecha.get(Calendar.DAY_OF_MONTH) + "_" + fecha.get(Calendar.HOUR) + "_" + fecha.get(Calendar.MINUTE) + "_" + fecha.get(Calendar.SECOND) + "_" + fecha.get(Calendar.MILLISECOND);
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
        try {
            return persistenciaPruebas.actualizarEstado(em, secPrueba, estado);
        } catch (Exception e) {
            System.out.println("Error AdministrarInicio.actualizarEstado: " + e);
            return false;
        }
    }
}
