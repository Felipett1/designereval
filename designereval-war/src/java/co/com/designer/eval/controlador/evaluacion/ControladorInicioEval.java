package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.clasesAyuda.ExtraeCausaExcepcion;
import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorInicioEval implements Serializable {

    @EJB
    private IAdministrarInicio administrarInicio;
    private String usuario, nitEmpresa, email, pathReporteGenerado;
    private List<Convocatorias> convocatorias;
    private List<Evaluados> evaluados;
    private List<Pruebas> pruebas;
    private BigDecimal secuenciaEvaluador, totalEmpleadosAsignados, empleadosConvocados, empleadosAsignados, empleadosEvaluados;
    private BigDecimal secConvocatoria;
    private int estadoConvocatoria = 2;

    //SELECCION
    private Convocatorias convocatoria;
    private Evaluados evaluado;
    private Pruebas prueba;

    //private Convocatorias convocatoria;
    public ControladorInicioEval() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorInicioEval.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicio.obtenerConexion(ses.getId());
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            convocatorias = administrarInicio.obtenerConvocatorias(usuario);
            secuenciaEvaluador = administrarInicio.obtenerSecuenciaEvaluador(usuario);
            if (secuenciaEvaluador != null) {
                totalEmpleadosAsignados = administrarInicio.totalEmpleadosEvaluador(secuenciaEvaluador.toBigInteger());
            }
            nitEmpresa = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
            email = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getPersona().getEmail();
            System.out.println("INICIALIZADO!!!!  " + ses.getId());
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e);
        }
    }

    //EVENTOS
    public void seleccionConvocatoria(int tipo) {
        //1 Si - 0 No
        switch (tipo) {
            case 1:
                evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
                empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
                empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                evaluado = null;
                break;
            case 2:
                evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
                empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
                empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                break;
            default:
                evaluados = null;
                evaluado = null;
                empleadosConvocados = null;
                empleadosAsignados = null;
                empleadosEvaluados = null;
                break;
        }
        pruebas = null;
    }

    public void seleccionEvaluado(int tipo) {
        //1 Si - 0 No
        if (tipo == 1 && evaluado != null) {
            pruebas = administrarInicio.obtenerPruebasEvaluado(usuario, evaluado.getSecuencia());
        } else {
            pruebas = null;
        }
    }

    public Object obtenerInformacion(int opcion) {
        /*
        0 - Evaluado
        1 - Evaluador
        2 - Convocatoria
        3 - Prueba
         */
        switch (opcion) {
            case 0:
                return evaluado;
            case 1:
                FacesContext x = FacesContext.getCurrentInstance();
                HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
                return ((ControladorInformacionBasica) x.getApplication().evaluateExpressionGet(x, "#{controladorInformacionBasica}", ControladorInformacionBasica.class
                )).getPersona().getNombreCompleto();
            case 2:
                return convocatoria;
            default:
                return prueba;
        }
    }

    public void seleccion() {
        int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
        String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        if (i.equals("0")) {
            convocatoria = convocatorias.get(index);
            PrimefacesContextUI.ejecutar("seleccionConvocatoria();");
        } else if (i.equals("1")) {
            evaluado = evaluados.get(index);
            PrimefacesContextUI.ejecutar("seleccionEvaluado();");
        } else if (i.equals("2")) {
            prueba = pruebas.get(index);
            PrimefacesContextUI.ejecutar("seleccionPrueba();");
        } else if (i.equals("3")) {
            secConvocatoria = new BigDecimal(convocatorias.get(index).getSecuencia());
            PrimefacesContextUI.ejecutar("PF('alertaCC').show();");
        } else if (i.equals("4")) {
            secConvocatoria = new BigDecimal(convocatorias.get(index).getSecuencia());
            PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show();");
        } else {
            prueba = pruebas.get(index);
            cambiarEstado(prueba.getSecuencia(), prueba.getEstado());
        }
    }

//    public void obtenerSecuenciaConvocatoria(BigInteger sec) {
    public void obtenerSecuenciaConvocatoria(BigDecimal sec) {
        secConvocatoria = sec;
    }

    private void reiniciarDatos() {
        convocatoria = null;
        convocatorias = administrarInicio.obtenerConvocatorias(usuario);
        evaluados = null;
        evaluado = null;
        empleadosConvocados = null;
        empleadosAsignados = null;
        empleadosEvaluados = null;
        pruebas = null;
    }

    public void cerrarEvaluaciones() {
        try {
            if (administrarInicio.cerrarEvaluaciones(secConvocatoria)) {
                //MensajesUI.info("Convocatoria cerrada exitosamente.");
                reiniciarDatos();
                PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show()");
            } else {
                MensajesUI.error("Error al cerrar la convocatoria.");
            }
        } catch (Exception e) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(e));
        }
    }

    public void cerrarConvocatoria() {
        String resultado = "";
        try {
            resultado = administrarInicio.cerrarConvocatoria(secConvocatoria);
            reiniciarDatos();
            MensajesUI.info(resultado);
            //PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show()");
        } catch (Exception ex) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(ex));
        }
    }

    public void generarReporteHistoEval() {
        try {
            envioCorreoCierreConvocatoria();
            MensajesUI.info("Convocatoria cerrada exitosamente.");
        } catch (Exception e) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(e));
        }
    }

    public void cargarConvocatorias() {
        if (estadoConvocatoria == 1) {
            convocatorias = administrarInicio.obtenerConvocatoriasAlcance(usuario);
        } else {
            convocatorias = administrarInicio.obtenerConvocatorias(usuario);
        }
        convocatoria = null;
        evaluados = null;
        evaluado = null;
        empleadosConvocados = null;
        empleadosAsignados = null;
        empleadosEvaluados = null;
        pruebas = null;
    }

    public void refrescarListas() {
        seleccionConvocatoria(2);
        seleccionEvaluado(1);
    }

    public void envioCorreoCierreConvocatoria() {
        if (secConvocatoria != null) {
            Convocatorias c = null;
            for (Convocatorias cvc : convocatorias) {
                if (new BigDecimal(cvc.getSecuencia()).compareTo(secConvocatoria) == 0) {
                    c = cvc;
                }
            }
            if (email != null && !email.isEmpty()) {
                if (c != null) {
                    generarReporte(c);
                    if (pathReporteGenerado != null && administrarInicio.enviarCorreo(nitEmpresa, email,
                            "Reporte Convocatoria - " + c.getCodigo(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
                            pathReporteGenerado)) {
                        MensajesUI.info("Se ha enviado un reporte con los resultados de la convocatoria a su dirección de correo.");
                    } else {
                        MensajesUI.error("No fue posible enviar el reporte consolidado de la convocatoria que acaba de cerrar, por favor comuníquese con soporte.");
                    }
                } else {
                    MensajesUI.error("Error al intentar obtener la convocatoria, por favor comuníquese con soporte.");
                }
            } else {
                MensajesUI.error("No es posible enviar el correo de confirmacion, ya que no esta configurado el correo de destino.");
            }
        }
    }

    public void descargarReporte() throws IOException {
        if (secConvocatoria != null) {
            Convocatorias c = null;
            for (Convocatorias cvc : convocatorias) {
                if (new BigDecimal(cvc.getSecuencia()).compareTo(secConvocatoria) == 0) {
                    c = cvc;
                }
            }
            if (c != null) {
                generarReporte(c);
                if (pathReporteGenerado != null) {
                    descarga();
                };
            } else {
                MensajesUI.error("Error al intentar obtener la convocatoria, por favor comuníquese con soporte.");
            }
        }
    }

    public void generarReporte(Convocatorias c) {
        Map parametros = new HashMap();
        parametros.put("secuenciaconvocatoria", secConvocatoria);
        pathReporteGenerado = administrarInicio.generarReporte("evalcompetenciacerrada", "PDF", parametros, c.getCodigo());
        if (pathReporteGenerado == null) {
            MensajesUI.error("El reporte consolidado de la convocatoria no se pudo generar.");
        }
    }

    public void cambiarEstado(BigInteger secPrueba, String estado) {
        administrarInicio.actualizarEstado(secPrueba, estado);
    }

    public void cambiarEstadoMobil(int index) {
        prueba = pruebas.get(index);
        administrarInicio.actualizarEstado(prueba.getSecuencia(), prueba.getEstado());
    }

    public void descarga() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        File f = new File(pathReporteGenerado);
        ec.responseReset();
        ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
        OutputStream output = ec.getResponseOutputStream();

        byte[] buf = new byte[2048];
        InputStream is = new FileInputStream(f);
        int c = 0;

        while ((c = is.read(buf, 0, buf.length)) > 0) {
            output.write(buf, 0, c);
            output.flush();
        }

        output.close();
        is.close();

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

    //GETTER AND SETTER
    public List<Convocatorias> getConvocatorias() {
        return convocatorias;
    }

    public List<Evaluados> getEvaluados() {
        return evaluados;
    }

    public BigDecimal getTotalEmpleadosAsignados() {
        return totalEmpleadosAsignados;
    }

    public BigDecimal getEmpleadosConvocados() {
        return empleadosConvocados;
    }

    public BigDecimal getEmpleadosAsignados() {
        return empleadosAsignados;
    }

    public BigDecimal getEmpleadosEvaluados() {
        return empleadosEvaluados;
    }

    public Convocatorias getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatorias convocatoria) {
        this.convocatoria = convocatoria;
    }

    public Evaluados getEvaluado() {
        return evaluado;
    }

    public void setEvaluado(Evaluados evaluado) {
        this.evaluado = evaluado;
    }

    public List<Pruebas> getPruebas() {
        return pruebas;
    }

    public Pruebas getPrueba() {
        return prueba;
    }

    public void setPrueba(Pruebas prueba) {
        this.prueba = prueba;
    }

    public int getEstadoConvocatoria() {
        return estadoConvocatoria;
    }

    public void setEstadoConvocatoria(int estadoConvocatoria) {
        this.estadoConvocatoria = estadoConvocatoria;
    }

}
