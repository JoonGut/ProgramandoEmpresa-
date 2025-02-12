package DAO;

import DTO.EmpleadoDTO;
import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // 🔹 Registrar un nuevo empleado
    public boolean registrarEmpleado(EmpleadoDTO empleado) {
        String sql = "INSERT INTO empleados (usuario, contrasenia, nombre, id_rol) VALUES (?, ?, ?, ?)";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empleado.getUsuario());
            statement.setString(2, empleado.getContrasenia());
            statement.setString(3, empleado.getNombre());
            statement.setInt(4, empleado.getId_rol());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Obtener un empleado por usuario
    public EmpleadoDTO obtenerEmpleado(String usuario) {
        String sql = "SELECT * FROM empleados WHERE usuario = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new EmpleadoDTO(
                    resultSet.getString("usuario"),
                    resultSet.getString("contrasenia"),
                    resultSet.getString("nombre"),
                    resultSet.getInt("id_rol")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Validar si el usuario y contraseña son correctos (para login)
    public boolean validarEmpleado(String usuario, String contrasenia) {
        String sql = "SELECT * FROM empleados WHERE usuario = ? AND contrasenia = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setString(2, contrasenia);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Retorna true si el usuario existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Modificar un empleado
    public boolean modificarEmpleado(EmpleadoDTO empleado) {
        String sql = "UPDATE empleados SET contrasenia = ?, nombre = ?, id_rol = ? WHERE usuario = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empleado.getContrasenia());
            statement.setString(2, empleado.getNombre());
            statement.setInt(3, empleado.getId_rol());
            statement.setString(4, empleado.getUsuario());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Eliminar un empleado
    public boolean eliminarEmpleado(String usuario) {
        String sql = "DELETE FROM empleados WHERE usuario = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Obtener todos los empleados
    public List<EmpleadoDTO> obtenerTodosLosEmpleados() {
        List<EmpleadoDTO> empleados = new ArrayList<>();
        String sql = "SELECT usuario, contrasenia, nombre, id_rol FROM empleados";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                empleados.add(new EmpleadoDTO(
                    resultSet.getString("usuario"),
                    resultSet.getString("contrasenia"),
                    resultSet.getString("nombre"),
                    resultSet.getInt("id_rol")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }
}
