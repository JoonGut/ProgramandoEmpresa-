package GestionDatos;

import DTO.FichajeDTO;
import DAO.FichajeDAO;
import java.util.Date;

public class GestorFichajes {
    private FichajeDAO fichajeDAO;

    public GestorFichajes() {
        this.fichajeDAO = new FichajeDAO();
    }

    public void registrarFichaje(FichajeDTO fichaje) {
        fichajeDAO.registrarFichaje(fichaje);
    }
}
