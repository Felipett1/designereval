package co.com.designer.eval.utilidadesUI;

import co.com.designer.eval.clasesAyuda.NavegationPageURL;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ControlNavegacion implements Serializable {

    private String urlMenuNavegation;

    @PostConstruct
    public void init() {
        System.out.println("ControlNavegacion.init");
        urlMenuNavegation = NavegationPageURL.INICIO_EVALUADOR.getUrl();
    }

    public String getUrlNavegation() {
        return urlMenuNavegation;
    }

    public String getUrlMenuNavegation() {
        return urlMenuNavegation;
    }

    public void setUrlMenuNavegation(String urlMenuNavegation) {
        this.urlMenuNavegation = urlMenuNavegation;
    }

    public void setUrlNavegation(String urlNavegation) {
        this.urlMenuNavegation = urlNavegation;
    }

    public void configuracionAction_Inicio() throws Exception {
        try {
            this.urlMenuNavegation = NavegationPageURL.INICIO_EVALUADOR.getUrl();
            System.out.println("ControlNavegacion.configuracionAction_Inicio");
        } catch (Exception e) {
            System.out.println("Error configuracionAction_Inicio: " + e.getMessage());
        }
    }

    public void pantallaDinamica(String url) throws Exception {
        System.out.println("ControlNavegacion.pantallaDinamica");
        try {
            System.out.println("url: " + url);
            this.urlMenuNavegation = url;
        } catch (Exception e) {
            System.out.println("Error pantallaDinamica: " + e.getMessage());
        }
    }
}
