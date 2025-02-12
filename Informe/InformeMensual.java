package Informe;

import DAO.HorasTrabajadasDAO;
import conexion.Conexion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class InformeMensual {
    private HorasTrabajadasDAO horasTrabajadasDAO;

    public InformeMensual() {
        this.horasTrabajadasDAO = new HorasTrabajadasDAO();
    }

    public void generarInformeMensual(String idEmpleado, int mes, int anio) {
        double horasTrabajadas = horasTrabajadasDAO.obtenerHorasTrabajadasPorMes(idEmpleado, mes, anio);
        double horasExtras = horasTrabajadasDAO.obtenerHorasExtras(idEmpleado, mes, anio);

        try {
            Connection conexion = Conexion.getInstance().getConnection();

            String reportPath = "src/Informe/InformeHoras.jasper";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("horasTrabajadas", horasTrabajadas);
            parameters.put("horasExtras", horasExtras);
            parameters.put("idEmpleado", idEmpleado);
            parameters.put("mes", mes);
            parameters.put("anio", anio);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, conexion);
            JasperViewer.viewReport(jasperPrint, false);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "src/Informe/informe_mensual.pdf");

            System.out.println("✅ Informe generado correctamente.");
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("❌ Error al generar el informe: " + e.getMessage());
        }
    }
}
