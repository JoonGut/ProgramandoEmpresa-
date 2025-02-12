package gui;

import javax.swing.*;
import java.awt.*;
import DAO.FichajeDAO;
import DTO.FichajeDTO;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import main.MenuPrincipal;

public class FichajeGUI extends JFrame {
    private JTextField inputEmpleado;
    private JButton btnEntrada, btnSalida, btnVolver;
    private JTextArea areaInformes;
    private FichajeDAO fichajeDAO;

    public FichajeGUI() {
        fichajeDAO = new FichajeDAO();
        setTitle("Gestión de Fichajes");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Fondo amarillo pastel
        getContentPane().setBackground(new Color(255, 239, 184));

        // 🔹 Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        JLabel labelTitulo = new JLabel("GESTIÓN DE FICHAJES", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setForeground(new Color(255, 204, 51));
        panelTitulo.add(labelTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // 🔹 Panel central con formulario
        JPanel panelCentro = new JPanel(new GridLayout(3, 1, 10, 10));
        panelCentro.setBackground(new Color(255, 239, 184));

        JLabel labelEmpleado = new JLabel("ID Empleado:", SwingConstants.CENTER);
        inputEmpleado = new JTextField();
        inputEmpleado.setFont(new Font("Arial", Font.PLAIN, 16));
        inputEmpleado.setHorizontalAlignment(JTextField.CENTER);

        btnEntrada = new JButton("Registrar Entrada");
        btnSalida = new JButton("Registrar Salida");

        estilizarBoton(btnEntrada);
        estilizarBoton(btnSalida);

        panelCentro.add(labelEmpleado);
        panelCentro.add(inputEmpleado);
        panelCentro.add(btnEntrada);
        panelCentro.add(btnSalida);

        add(panelCentro, BorderLayout.CENTER);

        // 🔹 Panel inferior con área de informes
        areaInformes = new JTextArea(5, 30);
        areaInformes.setEditable(false);
        areaInformes.setFont(new Font("Arial", Font.BOLD, 14));
        areaInformes.setForeground(Color.BLACK);
        areaInformes.setBackground(new Color(255, 239, 184));
        JScrollPane scrollPane = new JScrollPane(areaInformes);
        add(scrollPane, BorderLayout.SOUTH);

        // 🔹 Botón para volver al menú
        btnVolver = new JButton("⬅ Volver al Menú");
        estilizarBoton(btnVolver);
        add(btnVolver, BorderLayout.SOUTH);

        // 🔹 Eventos de botones
        btnEntrada.addActionListener(e -> registrarFichaje(true));
        btnSalida.addActionListener(e -> registrarFichaje(false));
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    private void registrarFichaje(boolean esEntrada) {
        String idEmpleado = inputEmpleado.getText().trim();
        if (idEmpleado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Por favor, ingrese su ID de empleado.");
            return;
        }

        fichajeDAO.registrarFichaje(new FichajeDTO(0, idEmpleado, new Date(), esEntrada));

        if (!esEntrada) { // Si es salida, mostrar las horas trabajadas
            double horasTrabajadas = fichajeDAO.obtenerHorasTrabajadasHoy(idEmpleado);
            mostrarHorasTrabajadas(horasTrabajadas);
        } else {
            areaInformes.setText("✅ Fichaje registrado: Entrada");
        }

        inputEmpleado.setText("");
    }

    private void mostrarHorasTrabajadas(double horasTrabajadas) {
        if (horasTrabajadas < 0) horasTrabajadas = 0; // Evita negativos

        DecimalFormat df = new DecimalFormat("00");
        int horas = (int) horasTrabajadas;
        int minutos = (int) ((horasTrabajadas - horas) * 60);
        int segundos = (int) (((horasTrabajadas - horas) * 60 - minutos) * 60);

        String tiempoFormateado = horas + "h " + df.format(Math.max(minutos, 0)) + "m " + df.format(Math.max(segundos, 0)) + "s";
        areaInformes.setText("✅ Fichaje registrado: Salida\nHas trabajado: " + tiempoFormateado);
    }

    private void volverAlMenu() {
        dispose();
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(Color.BLACK);
        boton.setForeground(new Color(255, 204, 51));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(220, 50));
    }
}
