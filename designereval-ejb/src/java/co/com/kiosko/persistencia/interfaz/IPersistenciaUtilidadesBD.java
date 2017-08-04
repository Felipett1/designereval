package co.com.kiosko.persistencia.interfaz;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaUtilidadesBD {

    public byte[] encriptar(javax.persistence.EntityManager eManager, java.lang.String valor);

    public String desencriptar(javax.persistence.EntityManager eManager, byte[] valor);

    public BigDecimal totalEmpleadosEvaluador(EntityManager em, BigInteger secuenciaEvaluador);

    public BigDecimal cantidadEvaluadosConvocatoria(EntityManager em, BigInteger secConvocatoria);

    public BigDecimal totalEmpleadosEvaluadorConvocatoria(EntityManager em, BigInteger secuenciaEvaluador, BigInteger secConvocatoria);

    public BigDecimal cantidadEvaluados(EntityManager em, BigInteger secuenciaEvaluador, BigInteger secConvocatoria);
    
}
