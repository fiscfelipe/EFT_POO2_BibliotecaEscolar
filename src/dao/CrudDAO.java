package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Define las operaciones CRUD básicas que deben implementar los DAO.
 *
 * @param <T> tipo de objeto gestionado por el DAO
 */
public interface CrudDAO<T> {

    /**
     * Registra un nuevo objeto en la base de datos.
     *
     * @param objeto objeto que se desea registrar
     * @return true si el objeto fue registrado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    boolean create(T objeto) throws SQLException;

    /**
     * Obtiene todos los objetos registrados en la base de datos.
     *
     * @return lista de objetos registrados
     * @throws SQLException si ocurre un error durante la operación
     */
    List<T> readAll() throws SQLException;

    /**
     * Actualiza un objeto existente en la base de datos.
     *
     * @param objeto objeto con los datos actualizados
     * @return true si el objeto fue actualizado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    boolean update(T objeto) throws SQLException;

    /**
     * Elimina un objeto de la base de datos según su identificador.
     *
     * @param id identificador del objeto
     * @return true si el objeto fue eliminado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    boolean delete(int id) throws SQLException;
}