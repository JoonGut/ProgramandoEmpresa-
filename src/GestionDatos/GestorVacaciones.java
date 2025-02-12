package GestionDatos;

import java.util.HashMap;
import java.util.Map;

public class GestorVacaciones {

    private Map<String, Long> horasTrabajadas;

    public GestorVacaciones() {
        horasTrabajadas = new HashMap<>();
    }

    // Registrar horas trabajadas
    public void registrarHorasTrabajadas(String idEmpleado, long horas) {
        horasTrabajadas.put(idEmpleado, horasTrabajadas.getOrDefault(idEmpleado, 0L) + horas);
    }

    // Calcular las vacaciones
    public long calcularVacaciones(String idEmpleado) {
        long horasTotales = horasTrabajadas.getOrDefault(idEmpleado, 0L);
        return horasTotales / 160; // 1 mes de vacaciones por cada 160 horas trabajadas
    }
}
