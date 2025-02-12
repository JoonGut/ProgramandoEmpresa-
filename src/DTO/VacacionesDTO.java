package DTO;

public class VacacionesDTO {
    private String usuario;
    private int anio;
    private int horasVacaciones;

    // 🔹 Constructor con parámetros
    public VacacionesDTO(String usuario, int anio, int horasVacaciones) {
        this.usuario = usuario;
        this.anio = anio;
        this.horasVacaciones = horasVacaciones;
    }

    // 🔹 Constructor vacío (necesario para ciertas operaciones en Java)
    public VacacionesDTO() {}

    // 🔹 Getters y Setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getHorasVacaciones() {
        return horasVacaciones;
    }

    public void setHorasVacaciones(int horasVacaciones) {
        this.horasVacaciones = horasVacaciones;
    }

    // 🔹 Método para mostrar información (opcional, pero útil para depuración)
    @Override
    public String toString() {
        return "VacacionesDTO [usuario=" + usuario + ", anio=" + anio + ", horasVacaciones=" + horasVacaciones + "]";
    }
}
