/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.controlador.ingreso;

/**
 *
 * @author luistriviño
 */
public class Convocatorias {

    private int id;
    private String nombre;
    private String enfoque;

    public Convocatorias(int id, String nombre, String enfoque) {
        this.id = id;
        this.nombre = nombre;
        this.enfoque = enfoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnfoque() {
        return enfoque;
    }

    public void setEnfoque(String enfoque) {
        this.enfoque = enfoque;
    }

}
