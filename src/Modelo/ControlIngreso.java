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
public class ControlIngreso {
    private int id;
    private Date fecha;
    private int granja;
    private int numeroGalpon;
    private int cantidad;
    private String sexo;
    private String lote;
    private String observaciones;

    public ControlIngreso() {
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
