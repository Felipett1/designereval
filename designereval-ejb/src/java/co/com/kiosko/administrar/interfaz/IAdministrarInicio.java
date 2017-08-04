package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Convocatorias;
import co.com.kiosko.entidades.Evaluados;
import co.com.kiosko.entidades.Pruebas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author luistrivino
 */
public interface IAdministrarInicio {

    public void obtenerConexion(String idSesion);

    public List<Convocatorias> obtenerConvocatorias(String usuario);

    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria);

    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria);

    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria);

    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria);

    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador);

    public BigDecimal obtenerSecuenciaEvaluador(String usuario);

    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo);
    
}
