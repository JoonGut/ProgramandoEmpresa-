package DTO;

public class EmpleadoDTO {
    private String usuario;
    private String contrasenia;
    private String nombre;
    private int id_rol;

    // Constructor
    public EmpleadoDTO(String usuario, String contrasenia, String nombre, int id_rol) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.id_rol = id_rol;
    }

    // Getters y Setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }
}
