package controller;

import dao.PrestamoDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Prestamo;
import model.ResultadoDevolucion;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;
import util.RegistroNoEncontradoException;

/**
 * Gestiona las operaciones relacionadas con los préstamos entre la vista y la capa de acceso a datos.
 */
public class PrestamoController {

    private final PrestamoDAO prestamoDAO;

    /**
     * Constructor para crear el controlador de préstamos.
     */
    public PrestamoController() {
        this.prestamoDAO = new PrestamoDAO();
    }

    /**
     * Registra un nuevo préstamo y descuenta una unidad del stock del libro.
     *
     * @param prestamo préstamo que se desea registrar
     * @return true si el préstamo fue registrado correctamente
     * @throws AplicacionException si ocurre un problema durante el registro
     */
    public boolean crearPrestamo(Prestamo prestamo) throws AplicacionException {
        validarPrestamo(prestamo);

        if (prestamo.getFechaDevolucion() == null) {
            prestamo.setFechaDevolucion(calcularFechaDevolucion(prestamo.getFechaPrestamo()));
        }

        try {
            boolean creado = prestamoDAO.create(prestamo);

            if (!creado) {
                throw new AplicacionException("No se puede realizar el préstamo porque el libro no tiene stock disponible.");
            }

            return true;

        } catch (RegistroNoEncontradoException ex) {
            throw new AplicacionException("No se pudo realizar el préstamo porque el libro seleccionado ya no existe.", ex);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esClaveForaneaInexistente(ex)) {
                throw new AplicacionException("No se pudo registrar el préstamo porque el estudiante o el libro seleccionado no existe.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar el préstamo debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Registra la devolución de un préstamo y devuelve una unidad al stock del libro.
     *
     * @param idPrestamo identificador del préstamo que se desea devolver
     * @return true si la devolución fue registrada correctamente
     * @throws AplicacionException si ocurre un problema durante la devolución
     */
    public boolean registrarDevolucion(int idPrestamo) throws AplicacionException {
        try {
            ResultadoDevolucion resultado = prestamoDAO.registrarDevolucion(idPrestamo);

            if (resultado == ResultadoDevolucion.PRESTAMO_INEXISTENTE) {
                throw new AplicacionException("No se encontró el préstamo que se desea devolver.");
            }

            if (resultado == ResultadoDevolucion.PRESTAMO_YA_DEVUELTO) {
                throw new AplicacionException("El préstamo seleccionado ya fue devuelto anteriormente.");
            }

            return true;

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar la devolución debido a un problema al actualizar los datos del préstamo.", ex);
        }
    }

    /**
     * Obtiene todos los préstamos registrados.
     *
     * @return lista de préstamos registrados
     * @throws AplicacionException si ocurre un problema al consultar los préstamos
     */
    public List<Prestamo> obtenerPrestamos() throws AplicacionException {
        try {
            return prestamoDAO.readAll();
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la lista de préstamos desde la base de datos.", ex);
        }
    }

    /**
     * Calcula la fecha de devolución de un préstamo considerando un plazo de 14 días.
     *
     * @param fechaPrestamo fecha en que se realiza el préstamo
     * @return fecha establecida para la devolución
     */
    public LocalDate calcularFechaDevolucion(LocalDate fechaPrestamo) {
        return fechaPrestamo.plusDays(14);
    }

    /**
     * Valida que un préstamo contenga los datos necesarios para ser procesado.
     *
     * @param prestamo préstamo que se desea validar
     * @throws AplicacionException si falta información necesaria
     */
    private void validarPrestamo(Prestamo prestamo) throws AplicacionException {
        if (prestamo == null) {
            throw new AplicacionException("No se puede procesar el préstamo porque no se recibieron sus datos.");
        }

        if (prestamo.getEstudiante() == null) {
            throw new AplicacionException("Debe seleccionar un estudiante para realizar el préstamo.");
        }

        if (prestamo.getLibro() == null) {
            throw new AplicacionException("Debe seleccionar un libro para realizar el préstamo.");
        }

        if (prestamo.getFechaPrestamo() == null) {
            throw new AplicacionException("El préstamo debe tener una fecha de inicio.");
        }
    }
}