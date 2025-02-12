package gui;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import javax.swing.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HorasTrabajadasGUITest {
    private static HorasTrabajadasGUI horasTrabajadasGUI;

    @BeforeAll
    static void setUp() {
        horasTrabajadasGUI = new HorasTrabajadasGUI();
        horasTrabajadasGUI.setVisible(true);
    }

    @Test
    @Order(1)
    void testVentanaCarga() {
        assertTrue(horasTrabajadasGUI.isVisible(), "La ventana de horas trabajadas no está visible");
    }

    @Test
    @Order(2)
    void testTablaExiste() {
        JTable tablaHoras = (JTable) horasTrabajadasGUI.getContentPane().getComponent(2);
        assertNotNull(tablaHoras, "La tabla de horas trabajadas no existe");
    }

    @AfterAll
    static void tearDown() {
        horasTrabajadasGUI.dispose();
    }
}
