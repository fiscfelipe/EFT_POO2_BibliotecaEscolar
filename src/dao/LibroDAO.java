package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.Libro;

/**
 * Gestiona las operaciones de acceso a datos de los libros.
 */
public class LibroDAO implements CrudDAO<Libro> {

    /**
     * Registra un nuevo libro en la base de datos.
     *
     * @param libro libro que se desea registrar
     * @return true si el libro fue registrado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean create(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, libro.getTitulo());
            sentencia.setString(2, libro.getAutor());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setString(4, libro.getEditorial());
            sentencia.setInt(5, libro.getStock());
            sentencia.setInt(6, libro.getCategoria().getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene todos los libros registrados en la base de datos.
     *
     * @return lista de libros registrados
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public List<Libro> readAll() throws SQLException {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT l.id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, "
                + "c.id AS categoria_id, c.nombre AS categoria_nombre "
                + "FROM libros l "
                + "INNER JOIN categorias c ON l.id_categoria = c.id";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Categoria categoria = new Categoria(resultado.getInt("categoria_id"), resultado.getString("categoria_nombre"));

                Libro libro = new Libro(resultado.getInt("id"), resultado.getString("titulo"), resultado.getString("autor"), resultado.getString("isbn"), resultado.getString("editorial"), resultado.getInt("stock"), categoria);

                libros.add(libro);
            }
        }

        return libros;
    }

    /**
     * Actualiza los datos de un libro existente.
     *
     * @param libro libro con los datos actualizados
     * @return true si el libro fue actualizado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean update(Libro libro) throws SQLException {
        String sql = "UPDATE libros "
                + "SET titulo = ?, autor = ?, isbn = ?, editorial = ?, stock = ?, id_categoria = ? "
                + "WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, libro.getTitulo());
            sentencia.setString(2, libro.getAutor());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setString(4, libro.getEditorial());
            sentencia.setInt(5, libro.getStock());
            sentencia.setInt(6, libro.getCategoria().getId());
            sentencia.setInt(7, libro.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un libro de la base de datos según su identificador.
     *
     * @param id identificador del libro
     * @return true si el libro fue eliminado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }
}