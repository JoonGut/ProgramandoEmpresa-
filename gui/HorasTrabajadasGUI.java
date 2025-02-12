package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DAO.HorasTrabajadasDAO;
import main.MenuPrincipal;

public class HorasTrabajadasGUI extends JFrame {
    private JComboBox<String> comboEmpleados;
    private JButton btnConsultar, btnVolver;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private HorasTrabajadasDAO horasTrabajadasDAO;

    public HorasTrabajadasGUI() {
        horasTrabajadasDAO = new HorasTrabajadasDAO();
        setTitle("Consulta de Horas Trabajadas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Fondo amarillo pastel
        getContentPane().setBackground(new Color(255, 239, 184));

        // 🔹 Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        JLabel labelTitulo = new JLabel("CONSULTA DE HORAS TRABAJADAS", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setForeground(new Color(255, 204, 51));
        panelTitulo.add(labelTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // 🔹 Panel de selección de usuario
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setBackground(new Color(255, 239, 184));
        panelSeleccion.setLayout(new FlowLayout());

        JLabel labelEmpleado = new JLabel("Seleccione un empleado:");
        labelEmpleado.setFont(new Font("Arial", Font.BOLD, 14));

        comboEmpleados = new JComboBox<>();
        cargarUsuariosEnComboBox();
        comboEmpleados.setFont(new Font("Arial", Font.PLAIN, 14));

        btnConsultar = new JButton("Consultar Horas");
        estilizarBoton(btnConsultar);

        panelSeleccion.add(labelEmpleado);
        panelSeleccion.add(comboEmpleados);
        panelSeleccion.add(btnConsultar);

        add(panelSeleccion, BorderLayout.NORTH);

        // 🔹 Crear tabla para mostrar resultados con fondo amarillo pastel
        String[] columnas = {"ID", "Usuario", "Fecha", "Horas Trabajadas", "Horas Extras", "Tipo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setFillsViewportHeight(true);
        tablaResultados.setBackground(new Color(255, 239, 184)); // Fondo de la tabla

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.getViewport().setBackground(new Color(255, 239, 184)); // Fondo de la tabla
        scrollPane.setBackground(new Color(255, 239, 184)); // Fondo del scrollPane
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Botón "Volver al Menú Principal" con tamaño corregido
        btnVolver = new JButton("Volver al Menú Principal");
        estilizarBoton(btnVolver);
        btnVolver.setPreferredSize(new Dimension(300, 45)); // 🔹 Aumento de tamaño para evitar recorte

        JPanel panelBotonVolver = new JPanel();
        panelBotonVolver.setBackground(new Color(255, 239, 184));
        panelBotonVolver.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 🔹 Margen para mejor visibilidad
        panelBotonVolver.add(btnVolver);

        add(panelBotonVolver, BorderLayout.SOUTH);

        // 🔹 Acción del botón "Consultar"
        btnConsultar.addActionListener(e -> consultarHorasTrabajadas());

        // 🔹 Acción del botón "Volver"
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });
    }

    private void cargarUsuariosEnComboBox() {
        List<String> usuarios = horasTrabajadasDAO.obtenerUsuarios();
        comboEmpleados.removeAllItems(); // Limpiar para evitar duplicados
        for (String usuario : usuarios) {
            comboEmpleados.addItem(usuario);
        }
    }

    private void consultarHorasTrabajadas() {
        String usuarioSeleccionado = (String) comboEmpleados.getSelectedItem();
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        // Obtener registros de la base de datos
        List<String[]> registros = horasTrabajadasDAO.obtenerRegistrosPorUsuario(usuarioSeleccionado);

        // Limpiar la tabla antes de agregar nuevos datos
        modeloTabla.setRowCount(0);

        if (registros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron registros para " + usuarioSeleccionado);
        } else {
            for (String[] registro : registros) {
                modeloTabla.addRow(registro);
            }
        }
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(Color.BLACK);
        boton.setForeground(new Color(255, 204, 51));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(200, 40));
    }
}
