package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@XmlRootElement
public class Convocatorias implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "EVALVIGCONVOCATORIA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date evalVigConvocatoria;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "ENFOQUE")
    private String enfoque;

    public Convocatorias() {
    }

    public Convocatorias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Convocatorias(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getEvalVigConvocatoria() {
        return evalVigConvocatoria;
    }

    public void setEvalVigConvocatoria(Date evalVigConvocatoria) {
        this.evalVigConvocatoria = evalVigConvocatoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEnfoque() {
        return enfoque;
    }

    public void setEnfoque(String enfoque) {
        this.enfoque = enfoque;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convocatorias)) {
            return false;
        }
        Convocatorias other = (Convocatorias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.ParametrizaClave[ secuencia=" + secuencia + " ]";
    }

}
