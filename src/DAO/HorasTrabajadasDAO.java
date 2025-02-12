package DAO;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorasTrabajadasDAO {

    // 🔹 Obtener registros de horas trabajadas por usuario
    public List<String[]> obtenerRegistrosPorUsuario(String usuario) {
        List<String[]> registros = new ArrayList<>();
        String sql = "SELECT id_hora, usuario, fecha, horas_trabajadas, horas_extras, tipo_hora FROM horastrabajadas WHERE usuario = ?";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    registros.add(new String[]{
                        String.valueOf(resultSet.getInt("id_hora")),
                        resultSet.getString("usuario"),
                        resultSet.getString("fecha"),
                        String.valueOf(resultSet.getDouble("horas_trabajadas")),
                        String.valueOf(resultSet.getDouble("horas_extras")),
                        resultSet.getString("tipo_hora")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener registros de horas trabajadas: " + e.getMessage());
        }
        return registros;
    }

    // 🔹 Obtener la lista de empleados (usuarios únicos)
    public List<String> obtenerUsuarios() {
        List<String> usuarios = new ArrayList<>();
        String sql = "SELECT DISTINCT usuario FROM horastrabajadas";

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usuarios.add(resultSet.getString("usuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener lista de empleados: " + e.getMessage());
        }
        return usuarios;
    }

    // 🔹 Obtener total de horas trabajadas por un empleado en un mes y año específicos
    public double obtenerHorasTrabajadasPorMes(String usuario, int mes, int anio) {
        String sql = "SELECT SUM(horas_trabajadas) FROM horastrabajadas WHERE usuario = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ?";
        double totalHoras = 0;

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setInt(2, mes);
            statement.setInt(3, anio);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getObject(1) != null) {
                    totalHoras = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener horas trabajadas por mes: " + e.getMessage());
        }
        return totalHoras;
    }

    // 🔹 Obtener total de horas extras trabajadas por un empleado en un mes y año específicos
    public double obtenerHorasExtras(String usuario, int mes, int anio) {
        String sql = "SELECT SUM(horas_extras) FROM horastrabajadas WHERE usuario = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ?";
        double totalExtras = 0;

        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setInt(2, mes);
            statement.setInt(3, anio);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getObject(1) != null) {
                    totalExtras = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al obtener horas extras por mes: " + e.getMessage());
        }
        return totalExtras;
    }
}
