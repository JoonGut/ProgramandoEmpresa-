package models;

import java.time.LocalDateTime;

public class RegistroFichaje {
    private String idEmpleado;
    private LocalDateTime fechaHora;
    private boolean esEntrada;

    // Constructor
    public RegistroFichaje(String idEmpleado, LocalDateTime fechaHora, boolean esEntrada) {
        this.idEmpleado = idEmpleado;
        this.fechaHora = fechaHora;
        this.esEntrada = esEntrada;
    }

    // Getters y setters
    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEsEntrada() {
        return esEntrada;
    }

    public void setEsEntrada(boolean esEntrada) {
        this.esEntrada = esEntrada;
    }
}


