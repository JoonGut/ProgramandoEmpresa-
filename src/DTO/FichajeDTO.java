package DTO;

import java.util.Date;

public class FichajeDTO {
    private int id;
    private String idEmpleado;
    private Date fechaHora;
    private boolean esEntrada; // true = entrada, false = salida

    public FichajeDTO(int id, String idEmpleado, Date fechaHora, boolean esEntrada) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fechaHora = fechaHora;
        this.esEntrada = esEntrada;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEsEntrada() {
        return esEntrada;
    }

    public void setEsEntrada(boolean esEntrada) {
        this.esEntrada = esEntrada;
    }
}

