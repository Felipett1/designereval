package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Preguntas;
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

    public boolean actualizarRespuesta(BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);

    public boolean eliminarRespuestas(BigInteger secIndagacion);

    public boolean actualizarPorcentaje(BigInteger secPrueba, String observacion, double porcentaje);

    public boolean actualizarPorcentaje(BigInteger secConvocatoria, BigInteger secEvaluado);
    
}
