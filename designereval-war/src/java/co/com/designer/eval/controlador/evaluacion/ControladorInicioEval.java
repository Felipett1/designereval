package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
    private String usuario;
    private List<Convocatorias> convocatorias;
    private List<Evaluados> evaluados;
    private List<Pruebas> pruebas;
    private BigDecimal secuenciaEvaluador, totalEmpleadosAsignados, empleadosConvocados, empleadosAsignados, empleadosEvaluados;
    private BigInteger secConvocatoria;
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
            totalEmpleadosAsignados = administrarInicio.totalEmpleadosEvaluador(secuenciaEvaluador.toBigInteger());
            System.out.println("Inicializado");
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EVENTOS
    public void seleccionConvocatoria(int tipo) {
        //1 Si - 0 No
        if (tipo == 1) {
            evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
            empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
            empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
            empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
            evaluado = null;
        } else if (tipo == 2) {
            evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
            empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
            empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
            empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
        } else {
            evaluados = null;
            evaluado = null;
            empleadosConvocados = null;
            empleadosAsignados = null;
            empleadosEvaluados = null;
        }
        pruebas = null;
    }

    public void seleccionEvaluado(int tipo) {
        //1 Si - 0 No
        if (tipo == 1) {
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
        if (opcion == 0) {
            return evaluado;
        } else if (opcion == 1) {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            return ((ControladorInformacionBasica) x.getApplication().evaluateExpressionGet(x, "#{controladorInformacionBasica}", ControladorInformacionBasica.class)).getPersona().getNombreCompleto();
        } else if (opcion == 2) {
            return convocatoria;
        } else {
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
        } else {
            secConvocatoria = convocatorias.get(index).getSecuencia();
            PrimefacesContextUI.ejecutar("PF('alertaCC').show();");
        }
    }

    public void obtenerSecuenciaConvocatoria(BigInteger sec) {
        secConvocatoria = sec;
    }

    public void cerrarConvocatoria() {
        if (administrarInicio.cerrarConvocatoria(secConvocatoria)) {
            MensajesUI.info("Convocatoria cerrada exitosamente.");
            convocatoria = null;
            convocatorias = administrarInicio.obtenerConvocatorias(usuario);
            evaluados = null;
            evaluado = null;
            empleadosConvocados = null;
            empleadosAsignados = null;
            empleadosEvaluados = null;
            pruebas = null;
        } else {
            MensajesUI.error("Error al cerrar la convocatoria.");
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
