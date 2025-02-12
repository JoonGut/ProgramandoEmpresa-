package DAO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import DTO.EmpleadoDTO;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmpleadoDAOTest {
    private static Connection connection;
    private static EmpleadoDAO empleadoDAO;
    
    @BeforeAll
    static void setUp() {
        connection = Conexion.getInstance().getConnection();
        empleadoDAO = new EmpleadoDAO();
    }

    @Test
    @Order(1)
    void testRegistrarEmpleado() {
        EmpleadoDTO empleado = new EmpleadoDTO("testuser", "password", "Test User", 2);
        boolean registrado = empleadoDAO.registrarEmpleado(empleado);
        assertTrue(registrado, "El empleado no se registró correctamente");
    }

    @Test
    @Order(2)
    void testObtenerEmpleado() {
        EmpleadoDTO empleado = empleadoDAO.obtenerEmpleado("testuser");
        assertNotNull(empleado, "El empleado no fue encontrado");
        assertEquals("Test User", empleado.getNombre(), "El nombre del empleado no coincide");
    }

    @Test
    @Order(3)
    void testEliminarEmpleado() {
        boolean eliminado = empleadoDAO.eliminarEmpleado("testuser");
        assertTrue(eliminado, "El empleado no se eliminó correctamente");
        EmpleadoDTO empleado = empleadoDAO.obtenerEmpleado("testuser");
        assertNull(empleado, "El empleado aún existe después de la eliminación");
    }
    
    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }
}