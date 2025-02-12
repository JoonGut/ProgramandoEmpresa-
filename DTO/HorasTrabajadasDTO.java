package DTO;

public class HorasTrabajadasDTO {
    private int idHora;
    private String usuario;
    private String fecha;
    private double horasTrabajadas;
    private double horasExtras;
    private String tipoHora; // Normal, Extra, Vacaciones

    public HorasTrabajadasDTO(int idHora, String usuario, String fecha, double horasTrabajadas, double horasExtras, String tipoHora) {
        this.idHora = idHora;
        this.usuario = usuario;
        this.fecha = fecha;
        this.horasTrabajadas = horasTrabajadas;
        this.horasExtras = horasExtras;
        this.tipoHora = tipoHora;
    }

    // Getters y Setters
    public int getIdHora() { return idHora; }
    public void setIdHora(int idHora) { this.idHora = idHora; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    public double getHorasExtras() { return horasExtras; }
    public void setHorasExtras(double horasExtras) { this.horasExtras = horasExtras; }

    public String getTipoHora() { return tipoHora; }
    public void setTipoHora(String tipoHora) { this.tipoHora = tipoHora; }
}
