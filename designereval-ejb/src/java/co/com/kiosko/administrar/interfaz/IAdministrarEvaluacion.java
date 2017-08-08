package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Preguntas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Familia Triviño Amad
 */
public interface IAdministrarEvaluacion {

    public void obtenerConexion(String idSesion);

    public List<Preguntas> obtenerCuestinonario(BigInteger secPrueba, BigInteger secIndagacion);

    public BigInteger obtenerNroPreguntas(BigInteger secPrueba);

    public boolean registrarRespuesta(BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);
    
}
