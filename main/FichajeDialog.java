package main;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import DAO.FichajeDAO;
import DTO.FichajeDTO;
import java.util.Date;

public class FichajeDialog extends JDialog {

    private JTextField txtEmpleado;
    private JButton btnEntrada, btnSalida, btnVolver;
    private JLabel lblMensaje;
    private FichajeDAO fichajeDAO;

    public FichajeDialog(JFrame parent) {
        super(parent, "Registrar Fichaje", true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        fichajeDAO = new FichajeDAO();

        // 🔹 Fondo amarillo pastel
        getContentPane().setBackground(new Color(255, 239, 184));

        // 🔹 Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        JLabel labelTitulo = new JLabel("REGISTRO DE FICHAJES", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(255, 204, 51));
        panelTitulo.add(labelTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con ID Empleado y Mensaje centrado
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(new Color(255, 239, 184));
        panelCentro.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔹 Campo de ID Empleado
        JPanel panelEmpleado = new JPanel();
        panelEmpleado.setBackground(new Color(255, 239, 184));
        JLabel lblEmpleado = new JLabel("ID Empleado: ");
        lblEmpleado.setFont(new Font("Arial", Font.BOLD, 16));
        txtEmpleado = new JTextField(15);
        panelEmpleado.add(lblEmpleado);
        panelEmpleado.add(txtEmpleado);
        panelCentro.add(panelEmpleado);

        // 🔹 Mensaje centrado
        lblMensaje = new JLabel("📌 Introduzca su ID y seleccione una opción.", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setPreferredSize(new Dimension(400, 30));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelMensaje = new JPanel();
        panelMensaje.setBackground(new Color(255, 239, 184));
        panelMensaje.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelMensaje.add(lblMensaje);

        panelCentro.add(panelMensaje);
        add(panelCentro, BorderLayout.CENTER);

        // 🔹 Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBotones.setBackground(new Color(255, 239, 184));

        btnEntrada = new JButton("Registrar Entrada");
        btnSalida = new JButton("Registrar Salida");
        btnVolver = new JButton("⬅ Volver");

        estilizarBoton(btnEntrada);
        estilizarBoton(btnSalida);
        estilizarBoton(btnVolver);

        panelBotones.add(btnEntrada);
        panelBotones.add(btnSalida);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        // 🔹 Acciones de los botones
        btnEntrada.addActionListener(e -> registrarFichaje(true));
        btnSalida.addActionListener(e -> registrarFichaje(false));
        btnVolver.addActionListener(e -> dispose());
    }

    private void registrarFichaje(boolean esEntrada) {
        String idEmpleado = txtEmpleado.getText().trim();
        if (idEmpleado.isEmpty()) {
            lblMensaje.setText("⚠ Ingrese el ID del empleado.");
            return;
        }

        fichajeDAO.registrarFichaje(new FichajeDTO(0, idEmpleado, new Date(), esEntrada));

        if (esEntrada) {
            lblMensaje.setText("✅ Entrada registrada con éxito.");
        } else {
            double horasTrabajadas = fichajeDAO.obtenerHorasTrabajadasHoy(idEmpleado);
            mostrarHorasTrabajadas(horasTrabajadas);
        }

        txtEmpleado.setText(""); // Limpia el campo después de fichar
    }

    private void mostrarHorasTrabajadas(double horasTrabajadas) {
        int totalSegundos = (int) (horasTrabajadas * 3600);
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;

        DecimalFormat df = new DecimalFormat("00");
        String tiempoFormateado = horas + "h " + df.format(minutos) + "m " + df.format(segundos) + "s";
        lblMensaje.setText("✅ Salida registrada. Has trabajado: " + tiempoFormateado);
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(Color.BLACK);
        boton.setForeground(new Color(255, 204, 51));
        boton.setFocusPainted(false);
    }
}
