package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import javax.swing.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuPrincipalTest {
    private static MenuPrincipal menuPrincipal;

    @BeforeAll
    static void setUp() {
        menuPrincipal = new MenuPrincipal();
        menuPrincipal.setVisible(true);
    }

    @Test
    @Order(1)
    void testVentanaCarga() {
        assertTrue(menuPrincipal.isVisible(), "El menú principal no está visible");
    }

    @Test
    @Order(2)
    void testBotonFichajesExiste() {
        JButton botonFichajes = (JButton) menuPrincipal.getContentPane().getComponent(0);
        assertNotNull(botonFichajes, "El botón de fichajes no existe");
    }

    @AfterAll
    static void tearDown() {
        menuPrincipal.dispose();
    }
}