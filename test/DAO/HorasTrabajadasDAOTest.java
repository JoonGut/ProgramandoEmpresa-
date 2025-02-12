package DAO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import DTO.HorasTrabajadasDTO;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HorasTrabajadasDAOTest {
    private static Connection connection;
    private static HorasTrabajadasDAO horasTrabajadasDAO;
    
    @BeforeAll
    static void setUp() {
        connection = Conexion.getInstance().getConnection();
        horasTrabajadasDAO = new HorasTrabajadasDAO();
    }

    @Test
    @Order(1)
    void testObtenerHorasTrabajadasPorMes() {
        double horas = horasTrabajadasDAO.obtenerHorasTrabajadasPorMes("testuser", 2, 2024);
        assertTrue(horas >= 0, "No se encontraron horas trabajadas para el usuario en el mes especificado");
    }
    
    @Test
    @Order(2)
    void testObtenerHorasExtras() {
        double horasExtras = horasTrabajadasDAO.obtenerHorasExtras("testuser", 2, 2024);
        assertTrue(horasExtras >= 0, "No se encontraron horas extras para el usuario");
    }
    
    @Test
    @Order(3)
    void testObtenerUsuarios() {
        List<String> usuarios = horasTrabajadasDAO.obtenerUsuarios();
        assertNotNull(usuarios, "No se pudo obtener la lista de usuarios");
    }
    
    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }
}