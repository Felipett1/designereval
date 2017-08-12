package co.com.kiosko.controlador.ingreso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Datos implements Serializable {

    private List<Evaluados> listaEvaluados;
    private List<Convocatorias> listaConvocatorias;

    @PostConstruct
    public void init() {
        listaEvaluados = new ArrayList<>();
        listaEvaluados.add(new Evaluados(1, "NEMOCON LEAÑO MARTHA ISABEL", 33));
        listaEvaluados.add(new Evaluados(2, "BERNAL GONZALES NELSY ADRIANA", 100));
        listaEvaluados.add(new Evaluados(3, "CHAMACHO LUCILA", 14));
        listaEvaluados.add(new Evaluados(4, "QUINTERO JAIMES MARCELA", 0));
        listaEvaluados.add(new Evaluados(5, "YAZO BENAVIDEZ AURORA", 8));
        listaEvaluados.add(new Evaluados(6, "RODRIGUEZ ARIAS XIMENA", 35));
        listaEvaluados.add(new Evaluados(7, "MENESES MARTINEZ ROSA ISABLE", 0));
        listaEvaluados.add(new Evaluados(8, "CARDENAS SOTAQUIRA YUDY CONSTANZA", 56));
        listaEvaluados.add(new Evaluados(9, "CASTRO REY FLOR DEL CARMEN", 0));
        listaEvaluados.add(new Evaluados(10, "CAUSIL RIVAS ROSA ELENA", 20));
        listaEvaluados.add(new Evaluados(11, "TRIVIÑO PAREDES LUIS FELIPE", 35));
        listaEvaluados.add(new Evaluados(12, "HASTAMORIR EDWIN", 0));
        listaEvaluados.add(new Evaluados(13, "SUSATAMA ANGELICA", 56));
        listaEvaluados.add(new Evaluados(14, "AMADO LAURA KATERINE", 0));
        listaEvaluados.add(new Evaluados(15, "CAMACHO VICTOR ALFONSO", 20));

        listaConvocatorias = new ArrayList<>();
        listaConvocatorias.add(new Convocatorias(1, "OPERACIO(A) DE PLANTA - JEFATURA DE PRODUCCION", "COMPETENCIA"));
        listaConvocatorias.add(new Convocatorias(2, "AUXILIAR DE PRODUCCION - JEFATURA DE PRODUCCION", "COMPETENCIA"));

    }

    public List<Evaluados> getListaEvaluados() {
        return listaEvaluados;
    }

    public List<Convocatorias> getListaConvocatorias() {
        return listaConvocatorias;
    }

}
