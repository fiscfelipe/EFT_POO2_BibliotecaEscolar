package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Estudiante;

/**
 * Gestiona las operaciones de acceso a datos de los estudiantes.
 */
public class EstudianteDAO implements CrudDAO<Estudiante> {

    /**
     * Registra un nuevo estudiante en la base de datos.
     *
     * @param estudiante estudiante que se desea registrar
     * @return true si el estudiante fue registrado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean create(Estudiante estudiante) throws SQLException {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, estudiante.getNombre());
            sentencia.setString(2, estudiante.getRut());
            sentencia.setString(3, estudiante.getCurso());
            sentencia.setString(4, estudiante.getCorreo());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene todos los estudiantes registrados en la base de datos.
     *
     * @return lista de estudiantes registrados
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public List<Estudiante> readAll() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT id, nombre, rut, curso, correo FROM estudiantes";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante(resultado.getInt("id"), resultado.getString("nombre"), resultado.getString("rut"), resultado.getString("curso"), resultado.getString("correo"));

                estudiantes.add(estudiante);
            }
        }

        return estudiantes;
    }

    /**
     * Actualiza los datos de un estudiante existente.
     *
     * @param estudiante estudiante con los datos actualizados
     * @return true si el estudiante fue actualizado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean update(Estudiante estudiante) throws SQLException {
        String sql = "UPDATE estudiantes SET nombre = ?, rut = ?, curso = ?, correo = ? WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, estudiante.getNombre());
            sentencia.setString(2, estudiante.getRut());
            sentencia.setString(3, estudiante.getCurso());
            sentencia.setString(4, estudiante.getCorreo());
            sentencia.setInt(5, estudiante.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un estudiante de la base de datos según su identificador.
     *
     * @param id identificador del estudiante
     * @return true si el estudiante fue eliminado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Busca un estudiante según su RUT.
     *
     * @param rut RUT del estudiante que se desea buscar
     * @return estudiante encontrado o null si no existe un estudiante con ese RUT
     * @throws SQLException si ocurre un error durante la consulta
     */
    public Estudiante buscarPorRut(String rut) throws SQLException {
        String sql = "SELECT id, nombre, rut, curso, correo FROM estudiantes WHERE rut = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, rut);

            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return new Estudiante(resultado.getInt("id"), resultado.getString("nombre"), resultado.getString("rut"), resultado.getString("curso"), resultado.getString("correo"));
                }
            }
        }

        return null;
    }
}