package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;

/**
 * Gestiona las operaciones de acceso a datos de las categorías.
 */
public class CategoriaDAO implements CrudDAO<Categoria> {

    /**
     * Registra una nueva categoría en la base de datos.
     *
     * @param categoria categoría que se desea registrar
     * @return true si la categoría fue registrada correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean create(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre) "
                + "VALUES (?)";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, categoria.getNombre());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene todas las categorías registradas en la base de datos.
     *
     * @return lista de categorías registradas
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public List<Categoria> readAll() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, nombre FROM categorias";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Categoria categoria = new Categoria(resultado.getInt("id"), resultado.getString("nombre"));

                categorias.add(categoria);
            }
        }

        return categorias;
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param categoria categoría con los datos actualizados
     * @return true si la categoría fue actualizada correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias "
                + "SET nombre = ? "
                + "WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, categoria.getNombre());
            sentencia.setInt(2, categoria.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina una categoría de la base de datos según su identificador.
     *
     * @param id identificador de la categoría
     * @return true si la categoría fue eliminada correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }
}