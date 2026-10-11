package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.RegistroNoEncontradoException;

/**
 * Gestiona las operaciones de acceso a datos de los préstamos.
 */
public class PrestamoDAO implements CrudDAO<Prestamo> {

    /**
     * Registra un nuevo préstamo y descuenta una unidad del stock del libro dentro de una misma transacción.
     *
     * @param prestamo préstamo que se desea registrar
     * @return true si el préstamo fue registrado y el stock actualizado, false si no existe stock disponible
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public synchronized boolean create(Prestamo prestamo) throws SQLException {
        String sqlStock = "SELECT stock FROM libros WHERE id = ? FOR UPDATE";
        String sqlPrestamo = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarStock = "UPDATE libros SET stock = stock - 1 WHERE id = ?";

        Connection conexion = DatabaseConnection.getInstance().getConnection();

        try {
            conexion.setAutoCommit(false);

            int stock;

            try (
                    PreparedStatement sentenciaStock = conexion.prepareStatement(sqlStock)
            ) {
                sentenciaStock.setInt(1, prestamo.getLibro().getId());

                try (
                        ResultSet resultado = sentenciaStock.executeQuery()
                ) {
                    if (!resultado.next()) {
                        throw new RegistroNoEncontradoException("No se encontró el libro asociado al préstamo.");
                    }

                    stock = resultado.getInt("stock");
                }
            }

            if (stock <= 0) {
                conexion.rollback();
                return false;
            }

            try (
                    PreparedStatement sentenciaPrestamo = conexion.prepareStatement(sqlPrestamo)
            ) {
                sentenciaPrestamo.setInt(1, prestamo.getEstudiante().getId());
                sentenciaPrestamo.setInt(2, prestamo.getLibro().getId());
                sentenciaPrestamo.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));

                if (prestamo.getFechaDevolucion() != null) {
                    sentenciaPrestamo.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
                } else {
                    sentenciaPrestamo.setNull(4, Types.DATE);
                }

                sentenciaPrestamo.setBoolean(5, prestamo.isDevuelto());

                int filasPrestamo = sentenciaPrestamo.executeUpdate();

                if (filasPrestamo == 0) {
                    throw new SQLException("No fue posible registrar el préstamo.");
                }
            }

            try (
                    PreparedStatement sentenciaStock = conexion.prepareStatement(sqlActualizarStock)
            ) {
                sentenciaStock.setInt(1, prestamo.getLibro().getId());

                int filasStock = sentenciaStock.executeUpdate();

                if (filasStock == 0) {
                    throw new SQLException("No fue posible actualizar el stock del libro.");
                }
            }

            conexion.commit();
            return true;

        } catch (RegistroNoEncontradoException ex) {
            conexion.rollback();
            throw ex;

        } catch (SQLException ex) {
            conexion.rollback();
            throw ex;

        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
    }

    /**
     * Obtiene todos los préstamos registrados en la base de datos.
     *
     * @return lista de préstamos registrados
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public List<Prestamo> readAll() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT p.id AS prestamo_id, p.fecha_prestamo, p.fecha_devolucion, p.devuelto, "
                + "e.id AS estudiante_id, e.nombre AS estudiante_nombre, e.rut AS estudiante_rut, e.curso AS estudiante_curso, e.correo AS estudiante_correo, "
                + "l.id AS libro_id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, "
                + "c.id AS categoria_id, c.nombre AS categoria_nombre "
                + "FROM prestamos p "
                + "INNER JOIN estudiantes e ON p.id_estudiante = e.id "
                + "INNER JOIN libros l ON p.id_libro = l.id "
                + "INNER JOIN categorias c ON l.id_categoria = c.id";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante(resultado.getInt("estudiante_id"), resultado.getString("estudiante_nombre"), resultado.getString("estudiante_rut"), resultado.getString("estudiante_curso"), resultado.getString("estudiante_correo"));

                Categoria categoria = new Categoria(resultado.getInt("categoria_id"), resultado.getString("categoria_nombre"));

                Libro libro = new Libro(resultado.getInt("libro_id"), resultado.getString("titulo"), resultado.getString("autor"), resultado.getString("isbn"), resultado.getString("editorial"), resultado.getInt("stock"), categoria);

                LocalDate fechaPrestamo = resultado.getDate("fecha_prestamo").toLocalDate();

                LocalDate fechaDevolucion = null;
                Date fechaDevolucionSql = resultado.getDate("fecha_devolucion");

                if (fechaDevolucionSql != null) {
                    fechaDevolucion = fechaDevolucionSql.toLocalDate();
                }

                Prestamo prestamo = new Prestamo(resultado.getInt("prestamo_id"), estudiante, libro, fechaPrestamo, fechaDevolucion, resultado.getBoolean("devuelto"));

                prestamos.add(prestamo);
            }
        }

        return prestamos;
    }

    /**
     * Actualiza los datos de un préstamo existente.
     *
     * @param prestamo préstamo con los datos actualizados
     * @return true si el préstamo fue actualizado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean update(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamos "
                + "SET id_estudiante = ?, id_libro = ?, fecha_prestamo = ?, fecha_devolucion = ?, devuelto = ? "
                + "WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, prestamo.getEstudiante().getId());
            sentencia.setInt(2, prestamo.getLibro().getId());
            sentencia.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));

            if (prestamo.getFechaDevolucion() != null) {
                sentencia.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            } else {
                sentencia.setNull(4, Types.DATE);
            }

            sentencia.setBoolean(5, prestamo.isDevuelto());
            sentencia.setInt(6, prestamo.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un préstamo de la base de datos según su identificador.
     *
     * @param id identificador del préstamo
     * @return true si el préstamo fue eliminado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Registra la devolución de un préstamo y devuelve una unidad al stock del libro dentro de una misma transacción.
     *
     * @param idPrestamo identificador del préstamo que se desea devolver
     * @return resultado de la operación de devolución
     * @throws SQLException si ocurre un error durante la operación
     */
    public synchronized ResultadoDevolucion registrarDevolucion(int idPrestamo) throws SQLException {
        String sqlPrestamo = "SELECT id_libro, devuelto FROM prestamos WHERE id = ? FOR UPDATE";
        String sqlDevolucion = "UPDATE prestamos SET devuelto = true WHERE id = ?";
        String sqlActualizarStock = "UPDATE libros SET stock = stock + 1 WHERE id = ?";

        Connection conexion = DatabaseConnection.getInstance().getConnection();

        try {
            conexion.setAutoCommit(false);

            int idLibro;

            try (
                    PreparedStatement sentenciaPrestamo = conexion.prepareStatement(sqlPrestamo)
            ) {
                sentenciaPrestamo.setInt(1, idPrestamo);

                try (
                        ResultSet resultado = sentenciaPrestamo.executeQuery()
                ) {
                    if (!resultado.next()) {
                        conexion.rollback();
                        return ResultadoDevolucion.PRESTAMO_INEXISTENTE;
                    }

                    if (resultado.getBoolean("devuelto")) {
                        conexion.rollback();
                        return ResultadoDevolucion.PRESTAMO_YA_DEVUELTO;
                    }

                    idLibro = resultado.getInt("id_libro");
                }
            }

            try (
                    PreparedStatement sentenciaDevolucion = conexion.prepareStatement(sqlDevolucion)
            ) {
                sentenciaDevolucion.setInt(1, idPrestamo);

                int filasDevolucion = sentenciaDevolucion.executeUpdate();

                if (filasDevolucion == 0) {
                    throw new SQLException("No fue posible registrar la devolución del préstamo.");
                }
            }

            try (
                    PreparedStatement sentenciaStock = conexion.prepareStatement(sqlActualizarStock)
            ) {
                sentenciaStock.setInt(1, idLibro);

                int filasStock = sentenciaStock.executeUpdate();

                if (filasStock == 0) {
                    throw new SQLException("No fue posible devolver la unidad al stock del libro.");
                }
            }

            conexion.commit();
            return ResultadoDevolucion.DEVOLUCION_EXITOSA;

        } catch (SQLException ex) {
            conexion.rollback();
            throw ex;

        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
    }
}