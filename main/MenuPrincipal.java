package main;

import javax.swing.*;
import gui.HorasTrabajadasGUI;
import gui.EmpleadoGUI;
import conexion.Conexion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal extends JFrame {
    private JButton btnRegistrarHoras, btnConsultarHoras, btnGestionEmpleados, btnVerInforme, btnExportarInforme, btnCerrarSesion;

    public MenuPrincipal() {
        setTitle("Menú Principal");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(255, 239, 184));

        // 🔹 Título centrado
        JLabel labelTitulo = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(255, 204, 51));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(Color.BLACK);
        labelTitulo.setPreferredSize(new Dimension(getWidth(), 50));
        add(labelTitulo, BorderLayout.NORTH);

        // 🔹 Panel de botones con mejor distribución
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(new Color(255, 239, 184));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        btnRegistrarHoras = new JButton("Registrar Horas");
        btnConsultarHoras = new JButton("Consultar Horas");
        btnGestionEmpleados = new JButton("Gestionar Empleados");
        btnVerInforme = new JButton("Ver Informe");
        btnExportarInforme = new JButton("Exportar PDF");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        JButton[] botones = {btnRegistrarHoras, btnConsultarHoras, btnGestionEmpleados, btnVerInforme, btnExportarInforme, btnCerrarSesion};

        int row = 0;
        for (JButton boton : botones) {
            boton.setFont(new Font("Arial", Font.BOLD, 16));
            boton.setBackground(Color.BLACK);
            boton.setForeground(new Color(255, 204, 51));
            boton.setFocusPainted(false);
            boton.setPreferredSize(new Dimension(220, 80));

            gbc.gridx = row % 2;
            gbc.gridy = row / 2;
            panelBotones.add(boton, gbc);
            row++;
        }

        add(panelBotones, BorderLayout.CENTER);

        // 🔹 Acciones de los botones
        btnRegistrarHoras.addActionListener(e -> new FichajeDialog(MenuPrincipal.this).setVisible(true));
        btnConsultarHoras.addActionListener(e -> new HorasTrabajadasGUI().setVisible(true));
        btnGestionEmpleados.addActionListener(e -> new EmpleadoGUI().setVisible(true));
        btnVerInforme.addActionListener(e -> visualizarInforme());
        btnExportarInforme.addActionListener(e -> exportarInformeAPDF());
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Main().setVisible(true);
        });
    }

    private void visualizarInforme() { 
        try {
            String reportPath = "src\\Informe\\InformeTrabajoHoras.jasper";
            Connection conexion = Conexion.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, conexion);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Error al generar el informe: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarInformeAPDF() {
        try {
            String reportPath = "src\\Informe\\InformeTrabajoHoras.jasper";
            Connection conexion = Conexion.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, conexion);

            String outputPath = "InformeGenerado.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            JOptionPane.showMessageDialog(null, "✅ Informe PDF generado correctamente en:\n" + outputPath);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Error al exportar el informe: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
