package controller;

import dao.ReporteDAO;
import java.sql.SQLException;
import java.util.List;
import model.LibroPrestadoResumen;
import model.Prestamo;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;

/**
 * Gestiona las operaciones relacionadas con los reportes entre la vista y la capa de acceso a datos.
 */
public class ReporteController {

    private final ReporteDAO reporteDAO;

    /**
     * Constructor para crear el controlador de reportes.
     */
    public ReporteController() {
        this.reporteDAO = new ReporteDAO();
    }

    /**
     * Obtiene los libros ordenados según la cantidad de préstamos registrados.
     *
     * @return lista de libros con su cantidad total de préstamos
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public List<LibroPrestadoResumen> obtenerLibrosMasPrestados() throws AplicacionException {
        try {
            return reporteDAO.obtenerLibrosMasPrestados();

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener el reporte de libros más prestados.", ex);
        }
    }

    /**
     * Obtiene el historial completo de préstamos de un estudiante.
     *
     * @param idEstudiante identificador del estudiante
     * @return lista de préstamos correspondientes al estudiante
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public List<Prestamo> obtenerHistorialPorEstudiante(int idEstudiante) throws AplicacionException {
        if (idEstudiante <= 0) {
            throw new AplicacionException("Debe seleccionar un estudiante válido.");
        }

        try {
            return reporteDAO.obtenerHistorialPorEstudiante(idEstudiante);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener el historial de préstamos del estudiante.", ex);
        }
    }

    /**
     * Obtiene los préstamos que actualmente se encuentran pendientes de devolución.
     *
     * @return lista de préstamos activos
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public List<Prestamo> obtenerPrestamosActuales() throws AplicacionException {
        try {
            return reporteDAO.obtenerPrestamosActuales();

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener el listado de préstamos actuales.", ex);
        }
    }
}