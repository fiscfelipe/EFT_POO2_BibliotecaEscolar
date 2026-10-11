package controller;

import dao.CategoriaDAO;
import java.sql.SQLException;
import java.util.List;
import model.Categoria;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;

/**
 * Gestiona las operaciones relacionadas con las categorías entre la vista y la capa de acceso a datos.
 */
public class CategoriaController {

    private final CategoriaDAO categoriaDAO;

    /**
     * Constructor para crear el controlador de categorías.
     */
    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO();
    }

    /**
     * Registra una nueva categoría.
     *
     * @param categoria categoría que se desea registrar
     * @return true si la categoría fue registrada correctamente
     * @throws AplicacionException si ocurre un problema durante el registro
     */
    public boolean crearCategoria(Categoria categoria) throws AplicacionException {
        try {
            boolean creada = categoriaDAO.create(categoria);

            if (!creada) {
                throw new AplicacionException("No se pudo registrar la categoría.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar la categoría debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Obtiene todas las categorías registradas.
     *
     * @return lista de categorías registradas
     * @throws AplicacionException si ocurre un problema al consultar las categorías
     */
    public List<Categoria> obtenerCategorias() throws AplicacionException {
        try {
            return categoriaDAO.readAll();
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la lista de categorías desde la base de datos.", ex);
        }
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param categoria categoría con los datos actualizados
     * @return true si la categoría fue actualizada correctamente
     * @throws AplicacionException si ocurre un problema durante la actualización
     */
    public boolean actualizarCategoria(Categoria categoria) throws AplicacionException {
        try {
            boolean actualizada = categoriaDAO.update(categoria);

            if (!actualizada) {
                throw new AplicacionException("No se encontró la categoría que se desea actualizar.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo actualizar la categoría debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Elimina una categoría según su identificador.
     *
     * @param id identificador de la categoría
     * @return true si la categoría fue eliminada correctamente
     * @throws AplicacionException si ocurre un problema durante la eliminación
     */
    public boolean eliminarCategoria(int id) throws AplicacionException {
        try {
            boolean eliminada = categoriaDAO.delete(id);

            if (!eliminada) {
                throw new AplicacionException("No se encontró la categoría que se desea eliminar.");
            }

            return true;
        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esRegistroReferenciado(ex)) {
                throw new AplicacionException("No se puede eliminar la categoría porque existen libros asociados a ella.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo eliminar la categoría debido a un problema en la base de datos.", ex);
        }
    }
}