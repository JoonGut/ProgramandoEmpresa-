package DAO;

import DTO.FichajeDTO;
import conexion.Conexion;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

public class FichajeDAO {

    // 🔹 Método para registrar un fichaje (entrada o salida)
    public void registrarFichaje(FichajeDTO fichaje) {
        String sql;

        if (fichaje.isEsEntrada()) {
            // 🔹 Verificar si hay una entrada sin salida antes de registrar una nueva
            if (tieneEntradaSinSalida(fichaje.getIdEmpleado())) {
                System.out.println("⚠ Ya existe una entrada sin salida registrada.");
                return;
            }
            sql = "INSERT INTO fichajes (id_empleado, fecha, hora_entrada, hora_salida) VALUES (?, ?, ?, NULL)";
        } else {
            sql = "UPDATE fichajes SET hora_salida = ? WHERE id_empleado = ? AND fecha = ? AND hora_salida IS NULL";
        }

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Timestamp timestamp = new Timestamp(fichaje.getFechaHora().getTime());

            if (fichaje.isEsEntrada()) {
                statement.setString(1, fichaje.getIdEmpleado());
                statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                statement.setTimestamp(3, timestamp);
            } else {
                statement.setTimestamp(1, timestamp);
                statement.setString(2, fichaje.getIdEmpleado());
                statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al registrar el fichaje: " + e.getMessage());
        }
    }

    // 🔹 Verificar si el empleado ya tiene una entrada sin salida
    private boolean tieneEntradaSinSalida(String idEmpleado) {
        String sql = "SELECT COUNT(*) FROM fichajes WHERE id_empleado = ? AND fecha = ? AND hora_salida IS NULL";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, idEmpleado);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Obtener las horas trabajadas en el día actual
    public double obtenerHorasTrabajadasHoy(String idEmpleado) {
        String sql = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, hora_entrada, hora_salida) / 3600), 0) " +
                     "FROM fichajes WHERE id_empleado = ? AND fecha = CURDATE()";
        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, idEmpleado);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Math.max(resultSet.getDouble(1), 0) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 🔹 Obtener total de horas trabajadas en un mes para un empleado
    public double obtenerHorasTrabajadasPorMes(String idEmpleado, int mes, int anio) {
        String sql = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, hora_entrada, hora_salida) / 3600), 0) " +
                     "FROM fichajes WHERE id_empleado = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ?";
        double horasTrabajadas = 0;

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, idEmpleado);
            statement.setInt(2, mes);
            statement.setInt(3, anio);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    horasTrabajadas = Math.max(resultSet.getDouble(1), 0); // Evita valores negativos
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener horas trabajadas por mes: " + e.getMessage());
        }
        return horasTrabajadas;
    }

    // 🔹 Obtener las horas extras trabajadas en un mes
    public double obtenerHorasExtras(String idEmpleado, int mes, int anio) {
        String sql = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, hora_entrada, hora_salida) / 3600) - 160, 0) " +
                     "FROM fichajes WHERE id_empleado = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ?";
        double horasExtras = 0;

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, idEmpleado);
            statement.setInt(2, mes);
            statement.setInt(3, anio);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    horasExtras = Math.max(resultSet.getDouble(1), 0); // Evita valores negativos
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener horas extras: " + e.getMessage());
        }
        return horasExtras;
    }
}
