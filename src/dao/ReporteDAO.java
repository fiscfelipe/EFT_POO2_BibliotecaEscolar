package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 * Gestiona las consultas necesarias para generar los reportes del sistema.
 */
public class ReporteDAO {

    /**
     * Obtiene los libros ordenados según la cantidad de préstamos registrados.
     *
     * @return lista de libros con su cantidad total de préstamos
     * @throws SQLException si ocurre un error durante la consulta
     */
    public List<LibroPrestadoResumen> obtenerLibrosMasPrestados() throws SQLException {
        List<LibroPrestadoResumen> resumenes = new ArrayList<>();

        String sql = "SELECT l.id AS libro_id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, "
                + "c.id AS categoria_id, c.nombre AS categoria_nombre, COUNT(p.id) AS cantidad_prestamos "
                + "FROM libros l "
                + "INNER JOIN categorias c ON l.id_categoria = c.id "
                + "LEFT JOIN prestamos p ON l.id = p.id_libro "
                + "GROUP BY l.id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, c.id, c.nombre "
                + "ORDER BY cantidad_prestamos DESC, l.titulo ASC";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Categoria categoria = new Categoria(resultado.getInt("categoria_id"), resultado.getString("categoria_nombre"));

                Libro libro = new Libro(resultado.getInt("libro_id"), resultado.getString("titulo"), resultado.getString("autor"), resultado.getString("isbn"), resultado.getString("editorial"), resultado.getInt("stock"), categoria);

                LibroPrestadoResumen resumen = new LibroPrestadoResumen(libro, resultado.getInt("cantidad_prestamos"));

                resumenes.add(resumen);
            }
        }

        return resumenes;
    }

    /**
     * Obtiene el historial completo de préstamos correspondiente a un estudiante.
     *
     * @param idEstudiante identificador del estudiante
     * @return lista de préstamos registrados para el estudiante
     * @throws SQLException si ocurre un error durante la consulta
     */
    public List<Prestamo> obtenerHistorialPorEstudiante(int idEstudiante) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT p.id AS prestamo_id, p.fecha_prestamo, p.fecha_devolucion, p.devuelto, "
                + "e.id AS estudiante_id, e.nombre AS estudiante_nombre, e.rut AS estudiante_rut, e.curso AS estudiante_curso, e.correo AS estudiante_correo, "
                + "l.id AS libro_id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, "
                + "c.id AS categoria_id, c.nombre AS categoria_nombre "
                + "FROM prestamos p "
                + "INNER JOIN estudiantes e ON p.id_estudiante = e.id "
                + "INNER JOIN libros l ON p.id_libro = l.id "
                + "INNER JOIN categorias c ON l.id_categoria = c.id "
                + "WHERE p.id_estudiante = ? "
                + "ORDER BY p.fecha_prestamo DESC, p.id DESC";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idEstudiante);

            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                while (resultado.next()) {
                    prestamos.add(construirPrestamo(resultado));
                }
            }
        }

        return prestamos;
    }

    /**
     * Obtiene los préstamos que actualmente se encuentran pendientes de devolución.
     *
     * @return lista de préstamos actualmente activos
     * @throws SQLException si ocurre un error durante la consulta
     */
    public List<Prestamo> obtenerPrestamosActuales() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT p.id AS prestamo_id, p.fecha_prestamo, p.fecha_devolucion, p.devuelto, "
                + "e.id AS estudiante_id, e.nombre AS estudiante_nombre, e.rut AS estudiante_rut, e.curso AS estudiante_curso, e.correo AS estudiante_correo, "
                + "l.id AS libro_id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, "
                + "c.id AS categoria_id, c.nombre AS categoria_nombre "
                + "FROM prestamos p "
                + "INNER JOIN estudiantes e ON p.id_estudiante = e.id "
                + "INNER JOIN libros l ON p.id_libro = l.id "
                + "INNER JOIN categorias c ON l.id_categoria = c.id "
                + "WHERE p.devuelto = false "
                + "ORDER BY p.fecha_devolucion ASC, p.fecha_prestamo ASC";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                prestamos.add(construirPrestamo(resultado));
            }
        }

        return prestamos;
    }

    /**
     * Construye un préstamo a partir de los datos obtenidos desde una consulta de reportes.
     *
     * @param resultado resultado de la consulta SQL
     * @return préstamo construido con sus datos relacionados
     * @throws SQLException si ocurre un error al obtener los datos
     */
    private Prestamo construirPrestamo(ResultSet resultado) throws SQLException {
        Estudiante estudiante = new Estudiante(resultado.getInt("estudiante_id"), resultado.getString("estudiante_nombre"), resultado.getString("estudiante_rut"), resultado.getString("estudiante_curso"), resultado.getString("estudiante_correo"));

        Categoria categoria = new Categoria(resultado.getInt("categoria_id"), resultado.getString("categoria_nombre"));

        Libro libro = new Libro(resultado.getInt("libro_id"), resultado.getString("titulo"), resultado.getString("autor"), resultado.getString("isbn"), resultado.getString("editorial"), resultado.getInt("stock"), categoria);

        LocalDate fechaPrestamo = resultado.getDate("fecha_prestamo").toLocalDate();

        LocalDate fechaDevolucion = null;
        Date fechaDevolucionSql = resultado.getDate("fecha_devolucion");

        if (fechaDevolucionSql != null) {
            fechaDevolucion = fechaDevolucionSql.toLocalDate();
        }

        return new Prestamo(resultado.getInt("prestamo_id"), estudiante, libro, fechaPrestamo, fechaDevolucion, resultado.getBoolean("devuelto"));
    }
}