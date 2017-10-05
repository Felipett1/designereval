package co.com.designer.eval.entidades;

import java.io.Serializable;
//import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "CONEXIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conexiones.findAll", query = "SELECT p FROM Conexiones p")})
public class Conexiones implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "SID")
    private BigInteger sid;
    @Column(name = "DIRECCIONIP")
    private String direccionIP;
    @Column(name = "USUARIOSO")
    private String usuarioso;
    @Column(name = "ESTACION")
    private String estacion;
    @Column(name = "ULTIMAENTRADA")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimaEntrada;
    @Column(name = "USUARIOBD")
    private String usuarioBD;

    public Conexiones() {
    }

    public Conexiones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Conexiones(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public void setDireccionIP(String direccionIP) {
        this.direccionIP = direccionIP;
    }

    public String getUsuarioso() {
        return usuarioso;
    }

    public void setUsuarioso(String usuarioso) {
        this.usuarioso = usuarioso;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public Date getUltimaEntrada() {
        return ultimaEntrada;
    }

    public void setUltimaEntrada(Date ultimaEntrada) {
        this.ultimaEntrada = ultimaEntrada;
    }

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public BigInteger getSid() {
        return sid;
    }

    public void setSid(BigInteger sid) {
        this.sid = sid;
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
        if (!(object instanceof Conexiones)) {
            return false;
        }
        Conexiones other = (Conexiones) object;
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
