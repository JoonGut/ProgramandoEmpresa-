package DAO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import DTO.VacacionesDTO;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VacacionesDAOTest {
    private static Connection connection;
    private static VacacionesDAO vacacionesDAO;

    @BeforeAll
    static void setUp() {
        connection = Conexion.getInstance().getConnection();
        vacacionesDAO = new VacacionesDAO();
    }

    @Test
    @Order(1)
    void testRegistrarVacaciones() {
        VacacionesDTO vacaciones = new VacacionesDTO("testuser", 2024, 8);
        boolean registrado = vacacionesDAO.registrarVacaciones(vacaciones);
        assertTrue(registrado, "Las vacaciones no se registraron correctamente");
    }

    @Test
    @Order(2)
    void testObtenerHorasVacaciones() {
        int horasVacaciones = vacacionesDAO.obtenerHorasVacaciones("testuser", 2024);
        assertTrue(horasVacaciones >= 0, "No se encontraron vacaciones para el usuario");
    }

    @Test
    @Order(3)
    void testObtenerVacacionesPorUsuario() {
        assertNotNull(vacacionesDAO.obtenerVacacionesPorUsuario("testuser"), "No se encontraron vacaciones para el usuario");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }
}
