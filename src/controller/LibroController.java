package controller;

import dao.LibroDAO;
import java.sql.SQLException;
import java.util.List;
import model.Libro;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;

/**
 * Gestiona las operaciones relacionadas con los libros entre la vista y la capa de acceso a datos.
 */
public class LibroController {

    private final LibroDAO libroDAO;

    /**
     * Constructor para crear el controlador de libros.
     */
    public LibroController() {
        this.libroDAO = new LibroDAO();
    }

    /**
     * Registra un nuevo libro.
     *
     * @param libro libro que se desea registrar
     * @return true si el libro fue registrado correctamente
     * @throws AplicacionException si ocurre un problema durante el registro
     */
    public boolean crearLibro(Libro libro) throws AplicacionException {
        try {
            boolean creado = libroDAO.create(libro);

            if (!creado) {
                throw new AplicacionException("No se pudo registrar el libro.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo registrar el libro porque el ISBN ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esClaveForaneaInexistente(ex)) {
                throw new AplicacionException("No se pudo registrar el libro porque la categoría seleccionada no existe.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar el libro debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Obtiene todos los libros registrados.
     *
     * @return lista de libros registrados
     * @throws AplicacionException si ocurre un problema al consultar los libros
     */
    public List<Libro> obtenerLibros() throws AplicacionException {
        try {
            return libroDAO.readAll();
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la lista de libros desde la base de datos.", ex);
        }
    }

    /**
     * Actualiza los datos de un libro existente.
     *
     * @param libro libro con los datos actualizados
     * @return true si el libro fue actualizado correctamente
     * @throws AplicacionException si ocurre un problema durante la actualización
     */
    public boolean actualizarLibro(Libro libro) throws AplicacionException {
        try {
            boolean actualizado = libroDAO.update(libro);

            if (!actualizado) {
                throw new AplicacionException("No se encontró el libro que se desea actualizar.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo actualizar el libro porque el ISBN ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esClaveForaneaInexistente(ex)) {
                throw new AplicacionException("No se pudo actualizar el libro porque la categoría seleccionada no existe.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo actualizar el libro debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Elimina un libro según su identificador.
     *
     * @param id identificador del libro
     * @return true si el libro fue eliminado correctamente
     * @throws AplicacionException si ocurre un problema durante la eliminación
     */
    public boolean eliminarLibro(int id) throws AplicacionException {
        try {
            boolean eliminado = libroDAO.delete(id);

            if (!eliminado) {
                throw new AplicacionException("No se encontró el libro que se desea eliminar.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esRegistroReferenciado(ex)) {
                throw new AplicacionException("No se puede eliminar el libro porque tiene préstamos asociados.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo eliminar el libro debido a un problema en la base de datos.", ex);
        }
    }
}