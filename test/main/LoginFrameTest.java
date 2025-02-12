package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import javax.swing.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginFrameTest {
    private static LoginFrame loginFrame;

    @BeforeAll
    static void setUp() {
        loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }

    @Test
    @Order(1)
    void testCamposNoVacios() {
        JTextField usuarioField = (JTextField) loginFrame.getContentPane().getComponent(1);
        JPasswordField contraseniaField = (JPasswordField) loginFrame.getContentPane().getComponent(3);
        usuarioField.setText("admin");
        contraseniaField.setText("admin123");

        assertFalse(usuarioField.getText().isEmpty(), "El campo usuario está vacío");
        assertFalse(new String(contraseniaField.getPassword()).isEmpty(), "El campo contraseña está vacío");
    }

    @Test
    @Order(2)
    void testBotonLogin() {
        JButton loginButton = (JButton) loginFrame.getContentPane().getComponent(5);
        assertNotNull(loginButton, "El botón de login no existe");
    }

    @AfterAll
    static void tearDown() {
        loginFrame.dispose();
    }
}
