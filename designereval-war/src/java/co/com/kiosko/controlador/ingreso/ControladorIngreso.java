package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.autenticacion.Util;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorIngreso implements Serializable {

    @EJB
    private IAdministrarIngreso administrarIngreso;
    private String usuario, clave, unidadPersistenciaIngreso, bckUsuario;
    private Date ultimaConexion;
    private boolean ingresoExitoso;
    private String nit;
    private final String logo;
    private String grupo;
    private List<SelectItem> listaGrupos;
    private String grupoSeleccionado;

    public ControladorIngreso() {
        logo = "logonominadesignertrans.png";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        validarGrupo();
    }

    public List<CadenasKioskos> obtenerCadenasKiosko() {
        //return (new LeerArchivoXML()).leerArchivoEmpresasKiosko();
        return obtenerCadenasKioskoGrupo();
    }

    public List<CadenasKioskos> obtenerCadenasKioskoGrupo() {
        List<CadenasKioskos> listaResultado;
        boolean resultadoValidacion = false;
        if ((grupo != null)) {
            if (!grupo.isEmpty()) {
                resultadoValidacion = true;
            }
        }
        if (resultadoValidacion) {
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasKioskoGrupo(this.grupo);
        } else {
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasKioskoGrupo("0");
        }
        return listaResultado;
    }

    public List<SelectItem> obtenerGruposCadenasKiosko() {
        List<String> listaOriginal = (new LeerArchivoXML()).obtenerGruposEmpresasKiosko();
        Collections.sort(listaOriginal);
        List<SelectItem> listaRetorno = new ArrayList<SelectItem>();
        for (int i = 0; i < listaOriginal.size(); i++) {
            listaRetorno.add(new SelectItem(listaOriginal.get(i), listaOriginal.get(i)));
        }
        return listaRetorno;
    }

    private CadenasKioskos validarUnidadPersistencia(String unidadP) {
        CadenasKioskos resultado = null;
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if (elemento.getId().equals(unidadP)) {
                resultado = elemento;
                break;
            }
        }
        return resultado;
    }

    public String ingresar() throws IOException {
        String retorno = null;
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        try {
            if (!ingresoExitoso) {
//            CadenasKioskos cadena = null;
                CadenasKioskos cadena;
                cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
                usuario = usuario.trim();
                if (usuario != null && !usuario.isEmpty()
                        && clave != null && !clave.isEmpty()
                        && cadena != null) {
                    nit = cadena.getNit();
                    if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                        if (administrarIngreso.validarUsuario(usuario)) {
                            if (administrarIngreso.conexionUsuario(cadena.getCadena(), usuario, clave)) {
                                administrarIngreso.adicionarConexionUsuario(ses.getId());
                                ingresoExitoso = true;
                                HttpSession session = Util.getSession();
                                session.setAttribute("idUsuario", usuario);
                                imprimir("Conectado a: " + session.getId());
                                //retorno = "plantilla";
                            } else {
                                //CONTRASEÑA INVALIDA.
                                MensajesUI.error("Contraseña invalida.");
                                ingresoExitoso = false;
                            }
                        } else {
                            //EL USUARIO NO EXISTE O ESTA INACTIVO.
                            MensajesUI.error("El usuario " + usuario + " no existe o esta inactivo, por favor contactar al área de soporte.");
                            ingresoExitoso = false;
                        }
                    } else {
                        //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                        MensajesUI.fatal("Unidad de persistencia inválida, por favor contactar al área de soporte.");
                        ingresoExitoso = false;
                    }
                } else {
                    MensajesUI.error("Todos los campos son de obligatorio ingreso.");
                    ingresoExitoso = false;
                }
            } else {
                usuario = "";
                HttpSession session = Util.getSession();
                System.out.println("la session con " + session.getAttribute("idUsuario") + " termino.");
                session.setAttribute("idUsuario", "");
                session.removeAttribute("idUsuario");
                ingresoExitoso = false;
                nit = null;
                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext ec = context.getExternalContext();

                try {
                    ec.invalidateSession();
                } catch (NullPointerException npe) {
                    System.out.println("ExternalContext vacio");
                }
                administrarIngreso.cerrarSession(ses.getId());
                ec.redirect(ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado);
            }
        } catch (EJBTransactionRolledbackException etre) {
            System.out.println(this.getClass().getName() + ".ingresar() exception");
            System.out.println("La transacción se deshizo.");
            System.out.println(etre);
        }
        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide()");
        //return "";
        return retorno;
    }

    public String entrar() throws IOException {
        String retorno;
        if (ingresoExitoso) {
            retorno = "plantilla";
        } else {
            try {
                retorno = ingresar();
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        return retorno;
    }

    private boolean validarGrupo() {
        boolean respuesta = false;
        if ((this.grupo == null) || (this.grupo.isEmpty())) {
            //System.out.println("El grupo esta nulo.");
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').show();");
            respuesta = false;
        } else {
            //System.out.println("El grupo es: " + this.grupo);
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').hide();");
            this.grupoSeleccionado = this.grupo;
            respuesta = true;
        }
        return respuesta;
    }

    public void obtenerParametroURL() {
        String ruta;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        imprimir("aplicacion: " + ec.getRequestContextPath());
        ruta = ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado;
        imprimir("ruta: " + ruta);
        try {
            ec.redirect(ruta);
        } catch (IOException ex) {
            imprimir("error al redireccionar");
            ex.printStackTrace();
        }
        //return ruta;
    }

    //GETTER AND SETTER
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUnidadPersistenciaIngreso() {
        List<CadenasKioskos> cadenas = obtenerCadenasKiosko();
//        if (cadenas.size() == 1) {
//            unidadPersistenciaIngreso = cadenas.get(0).getId();
//        } else {
//            unidadPersistenciaIngreso = null;
//        }
        unidadPersistenciaIngreso = (cadenas.size() == 1) ? cadenas.get(0).getId() : null;
        return unidadPersistenciaIngreso;
    }

    public void setUnidadPersistenciaIngreso(String unidadPersistenciaIngreso) {
        this.unidadPersistenciaIngreso = unidadPersistenciaIngreso;
    }

    public boolean isIngresoExitoso() {
        return ingresoExitoso;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public String getNit() {
        return nit;
    }

    public String getLogo() {
        return logo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
        validarGrupo();
    }

    public String getGrupoSeleccionado() {
        imprimir("getGrupoSeleccionado: " + this.grupoSeleccionado);
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(String grupoSeleccionado) {
        imprimir("setGrupoSeleccionado: " + grupoSeleccionado);
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public List<SelectItem> getListaGrupos() {
        imprimir("getListaGrupos");
        listaGrupos = obtenerGruposCadenasKiosko();
        return listaGrupos;
    }

    public void setListaGrupos(List<SelectItem> listaGrupos) {
        imprimir("setListaGrupos");
        this.listaGrupos = listaGrupos;
    }

    private void imprimir(String mensajeConsola) {
        if (true) {
            System.out.println(mensajeConsola);
        }
    }
}
