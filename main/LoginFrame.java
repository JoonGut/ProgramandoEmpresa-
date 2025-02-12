package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DAO.EmpleadoDAO;

public class LoginFrame extends JFrame {
    private JTextField usuarioField;
    private JPasswordField contraseniaField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setBounds(10, 20, 80, 25);
        panel.add(usuarioLabel);

        usuarioField = new JTextField();
        usuarioField.setBounds(100, 20, 160, 25);
        panel.add(usuarioField);

        JLabel contraseniaLabel = new JLabel("Contraseña:");
        contraseniaLabel.setBounds(10, 60, 80, 25);
        panel.add(contraseniaLabel);

        contraseniaField = new JPasswordField();
        contraseniaField.setBounds(100, 60, 160, 25);
        panel.add(contraseniaField);

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(100, 100, 160, 30);
        panel.add(loginButton);

        
        // 🔹 Valida el usuario y la contraseña. Y nos da la bienvenida o nos indica si es incorrecto.
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String contrasenia = new String(contraseniaField.getPassword());

                EmpleadoDAO empleadoDAO = new EmpleadoDAO();
                boolean valido = empleadoDAO.validarEmpleado(usuario, contrasenia);

                if (valido) {
                    JOptionPane.showMessageDialog(null, "Bienvenido " + usuario);
                    new Main().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
