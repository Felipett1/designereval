package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarInicio;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Convocatorias;
import co.com.kiosko.entidades.Evaluados;
import co.com.kiosko.entidades.Pruebas;
import java.io.*;
import java.math.BigDecimal;
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

    public String obtenerInformacion(int opcion) {
        /*
        0 - Evaluado
        1 - Evaluador
        2 - Convocatoria
        3 - Prueba
        4 - Indagacion
         */
        if (opcion == 0) {
            return evaluado.getNombrePersona();
        } else if (opcion == 1) {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            return ((ControladorInformacionBasica) x.getApplication().evaluateExpressionGet(x, "#{controladorInformacionBasica}", ControladorInformacionBasica.class)).getPersona().getNombreCompleto();
        } else if (opcion == 2) {
            return convocatoria.getCodigo() + " - " + convocatoria.getEnfoque();
        } else if (opcion == 3) {
            return prueba.getPrueba();
        } else {
            return prueba.getSecuencia().toString();
        }
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

}
