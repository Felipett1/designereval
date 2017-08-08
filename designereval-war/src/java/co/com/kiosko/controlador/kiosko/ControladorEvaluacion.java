package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarEvaluacion;
import co.com.kiosko.entidades.Preguntas;
import co.com.kiosko.utilidadesUI.MensajesUI;
import java.io.*;
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
public class ControladorEvaluacion implements Serializable {

    @EJB
    private IAdministrarEvaluacion administrarEvaluacion;
    private List<Preguntas> preguntas;

    //Informacion general
    private String evaluado, evaluador, convocatoria, prueba, observacion;
    private BigInteger nroPreguntas, puntajeMaximo, secIndigacion;

    public ControladorEvaluacion() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorEvaluacion.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEvaluacion.obtenerConexion(ses.getId());
            System.out.println("Inicializado");
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void cargarPreguntas(BigInteger secPrueba) {
        FacesContext x = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        evaluado = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(0);
        evaluador = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(1);
        convocatoria = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(2);
        prueba = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(3);
        secIndigacion = new BigInteger(((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(4));
        preguntas = administrarEvaluacion.obtenerCuestinonario(secPrueba, secIndigacion);
        nroPreguntas = administrarEvaluacion.obtenerNroPreguntas(secPrueba);
    }

    public void enviarRespuestas() {
        boolean todas = true;
        for (Preguntas pregunta : preguntas) {
            if (pregunta.getRespuesta() == null) {
                todas = false;
                break;
            }
        }
        if (todas) {
            boolean error = false;
            for (Preguntas pregunta : preguntas) {
                if (administrarEvaluacion.registrarRespuesta(secIndigacion, pregunta.getSecuencia(), pregunta.getRespuesta())) {
                    continue;
                } else {
                    MensajesUI.error("No fue posible registrar las respuesta.");
                    error = true;
                    break;
                }
            }
            if (!error) {
                MensajesUI.info("Respuestas guardadas exitosamente.");
            }
        } else {
            MensajesUI.error("Antes de enviar la evaluación debe responder todas las preguntas.");
        }
    }

    //GETTER AND SETTER
    public List<Preguntas> getPreguntas() {
        return preguntas;
    }

    public String getEvaluado() {
        return evaluado;
    }

    public String getEvaluador() {
        return evaluador;
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public BigInteger getNroPreguntas() {
        return nroPreguntas;
    }

    public BigInteger getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public String getPrueba() {
        return prueba;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
