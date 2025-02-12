package DAO;

import DTO.VacacionesDTO;
import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacacionesDAO {

    // 🔹 Registrar vacaciones
    public boolean registrarVacaciones(VacacionesDTO vacaciones) {
        String sql = "INSERT INTO vacaciones (usuario, anio, horas_vacaciones) VALUES (?, ?, ?)";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, vacaciones.getUsuario());
            statement.setInt(2, vacaciones.getAnio());
            statement.setInt(3, vacaciones.getHorasVacaciones());

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Obtener horas de vacaciones por usuario y año
    public int obtenerHorasVacaciones(String usuario, int anio) {
        String sql = "SELECT horas_vacaciones FROM vacaciones WHERE usuario = ? AND anio = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setInt(2, anio);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("horas_vacaciones");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Si no encuentra datos, devuelve 0
    }

    // 🔹 Obtener todas las vacaciones de un usuario
    public List<VacacionesDTO> obtenerVacacionesPorUsuario(String usuario) {
        List<VacacionesDTO> listaVacaciones = new ArrayList<>();
        String sql = "SELECT * FROM vacaciones WHERE usuario = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listaVacaciones.add(new VacacionesDTO(
                        resultSet.getString("usuario"),
                        resultSet.getInt("anio"),
                        resultSet.getInt("horas_vacaciones")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaVacaciones;
    }
}
