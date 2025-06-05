/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Date;

/**
 *
 * @author franc
 */
public class ControlConsumo {
    private int id;
    private Date fecha;
    private int granja;
    private int numeroGalpon;
    private String tipoAlimento;
    private double cantidad;
    private String observaciones;

    public ControlConsumo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getGranja() {
        return granja;
    }

    public void setGranja(int granja) {
        this.granja = granja;
    }

    public int getNumeroGalpon() {
        return numeroGalpon;
    }

    public void setNumeroGalpon(int numeroGalpon) {
        this.numeroGalpon = numeroGalpon;
    }

    public String getTipoAlimento() {
        return tipoAlimento;
    }

    public void setTipoAlimento(String tipoAlimento) {
        this.tipoAlimento = tipoAlimento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "ControlConsumo{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", granja=" + granja +
                ", numeroGalpon=" + numeroGalpon +
                ", tipoAlimento='" + tipoAlimento + '\'' +
                ", cantidad=" + cantidad +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }

}
