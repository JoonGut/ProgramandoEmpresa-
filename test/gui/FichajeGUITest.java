package gui;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import javax.swing.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FichajeGUITest {
    private static FichajeGUI fichajeGUI;

    @BeforeAll
    static void setUp() {
        fichajeGUI = new FichajeGUI();
        fichajeGUI.setVisible(true);
    }

    @Test
    @Order(1)
    void testVentanaCarga() {
        assertTrue(fichajeGUI.isVisible(), "La ventana de fichajes no está visible");
    }

    @Test
    @Order(2)
    void testBotonRegistrarExiste() {
        JButton botonRegistrar = (JButton) fichajeGUI.getContentPane().getComponent(1);
        assertNotNull(botonRegistrar, "El botón de registrar fichaje no existe");
    }

    @AfterAll
    static void tearDown() {
        fichajeGUI.dispose();
    }
}