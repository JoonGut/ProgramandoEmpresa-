package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.Conexion;
import main.MenuPrincipal;

public class Main extends JFrame {
    private JTextField usuarioField;
    private JPasswordField contraseniaField;
    private JButton btnIniciar;

    public Main() {
        setTitle("BIENVENIDO");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fondo amarillo pastel
        getContentPane().setBackground(new Color(255, 239, 184));

        // Panel del título "BIENVENIDO"
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        JLabel labelTitulo = new JLabel("BIENVENIDO", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setForeground(new Color(255, 204, 51)); // Color dorado
        panelTitulo.add(labelTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel del formulario de login centrado
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);
        panelCentro.add(Box.createVerticalStrut(60)); // Espaciado para centrar

        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(4, 1, 10, 10));
        panelLogin.setOpaque(false);
        panelLogin.setPreferredSize(new Dimension(300, 150));
        panelLogin.setMaximumSize(new Dimension(300, 150));
        panelLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usuarioLabel = new JLabel("Usuario:", SwingConstants.CENTER);
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        usuarioField = new JTextField();
        usuarioField.setFont(new Font("Arial", Font.PLAIN, 16));
        usuarioField.setPreferredSize(new Dimension(250, 40));
        usuarioField.setHorizontalAlignment(JTextField.CENTER);

        JLabel contraseniaLabel = new JLabel("Contraseña:", SwingConstants.CENTER);
        contraseniaLabel.setFont(new Font("Arial", Font.BOLD, 16));

        contraseniaField = new JPasswordField();
        contraseniaField.setFont(new Font("Arial", Font.PLAIN, 16));
        contraseniaField.setPreferredSize(new Dimension(250, 40));
        contraseniaField.setHorizontalAlignment(JTextField.CENTER);

        panelLogin.add(usuarioLabel);
        panelLogin.add(usuarioField);
        panelLogin.add(contraseniaLabel);
        panelLogin.add(contraseniaField);
        panelCentro.add(panelLogin);
        panelCentro.add(Box.createVerticalStrut(40)); // Más espaciado para centrar

        add(panelCentro, BorderLayout.CENTER);

        // Botón de iniciar sesión estilizado y centrado
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBoton.add(Box.createVerticalStrut(30)); // Espaciado

        btnIniciar = new JButton("INICIAR SESIÓN");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciar.setBackground(new Color(0, 0, 0));
        btnIniciar.setForeground(new Color(255, 204, 51));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String contrasenia = new String(contraseniaField.getPassword());
                if (validarCredenciales(usuario, contrasenia)) {
                    JOptionPane.showMessageDialog(Main.this, "Ongi Etorri", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    new MenuPrincipal().setVisible(true);  // ✅ AHORA ABRE EL MENÚ PRINCIPAL
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBoton.add(btnIniciar);
        panelCentro.add(panelBoton);
    }

    private boolean validarCredenciales(String usuario, String contrasenia) {
        String sql = "SELECT * FROM empleados WHERE usuario = ? AND contrasenia = ?";
        try (Connection connection = Conexion.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario);
            statement.setString(2, contrasenia);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
