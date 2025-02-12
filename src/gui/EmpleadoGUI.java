package gui;

import DAO.EmpleadoDAO;
import DTO.EmpleadoDTO;
import main.MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EmpleadoGUI extends JFrame {
    private JTextField txtUsuario, txtContrasenia, txtNombre, txtRol;
    private JButton btnAgregar, btnModificar, btnEliminar, btnVolver;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private EmpleadoDAO empleadoDAO;

    public EmpleadoGUI() {
        empleadoDAO = new EmpleadoDAO();
        setTitle("Gestión de Empleados");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(255, 239, 184));

        // 🔹 Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        JLabel labelTitulo = new JLabel("GESTIÓN DE EMPLEADOS", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setForeground(new Color(255, 204, 51));
        panelTitulo.add(labelTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // 🔹 Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBackground(new Color(255, 239, 184));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelFormulario.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panelFormulario.add(txtUsuario);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtContrasenia = new JTextField();
        panelFormulario.add(txtContrasenia);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("ID Rol:"));
        txtRol = new JTextField();
        panelFormulario.add(txtRol);

        add(panelFormulario, BorderLayout.CENTER);

        // 🔹 Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(255, 239, 184));

        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver al Menú");

        JButton[] botones = {btnAgregar, btnModificar, btnEliminar, btnVolver};
        for (JButton boton : botones) {
            boton.setBackground(Color.BLACK);
            boton.setForeground(new Color(255, 204, 51));
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.SOUTH);

        // 🔹 Tabla de empleados
        String[] columnas = {"Usuario", "Nombre", "ID Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEmpleados = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        add(scrollPane, BorderLayout.EAST);

        cargarEmpleados();

        // 🔹 Acciones de los botones
        btnAgregar.addActionListener((ActionEvent e) -> agregarEmpleado());
        btnModificar.addActionListener((ActionEvent e) -> modificarEmpleado());
        btnEliminar.addActionListener((ActionEvent e) -> eliminarEmpleado());
        btnVolver.addActionListener((ActionEvent e) -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });
    }

    private void cargarEmpleados() {
        modeloTabla.setRowCount(0);
        List<EmpleadoDTO> empleados = empleadoDAO.obtenerTodosLosEmpleados();

        for (EmpleadoDTO emp : empleados) {
            modeloTabla.addRow(new Object[]{emp.getUsuario(), emp.getNombre(), emp.getId_rol()});
        }
    }

    private void agregarEmpleado() {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtContrasenia.getText().trim();
        String nombre = txtNombre.getText().trim();
        int idRol = Integer.parseInt(txtRol.getText().trim());

        EmpleadoDTO nuevoEmpleado = new EmpleadoDTO(usuario, contrasenia, nombre, idRol);

        if (empleadoDAO.registrarEmpleado(nuevoEmpleado)) {
            JOptionPane.showMessageDialog(this, "✅ Empleado agregado con éxito.");
            cargarEmpleados();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al agregar el empleado.");
        }
    }

    private void modificarEmpleado() {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtContrasenia.getText().trim();
        String nombre = txtNombre.getText().trim();
        int idRol = Integer.parseInt(txtRol.getText().trim());

        EmpleadoDTO empleadoModificado = new EmpleadoDTO(usuario, contrasenia, nombre, idRol);

        if (empleadoDAO.modificarEmpleado(empleadoModificado)) {
            JOptionPane.showMessageDialog(this, "✅ Empleado modificado con éxito.");
            cargarEmpleados();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al modificar el empleado.");
        }
    }

    private void eliminarEmpleado() {
        String usuario = txtUsuario.getText().trim();

        if (empleadoDAO.eliminarEmpleado(usuario)) {
            JOptionPane.showMessageDialog(this, "✅ Empleado eliminado.");
            cargarEmpleados();
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar el empleado.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmpleadoGUI().setVisible(true));
    }
}
