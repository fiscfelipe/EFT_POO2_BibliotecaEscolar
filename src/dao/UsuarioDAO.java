package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

/**
 * Gestiona las operaciones de acceso a datos de los usuarios.
 */
public class UsuarioDAO implements CrudDAO<Usuario> {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario usuario que se desea registrar
     * @return true si el usuario fue registrado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean create(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, rut, correo, contraseña, rol) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getRut());
            sentencia.setString(3, usuario.getCorreo());
            sentencia.setString(4, usuario.getContrasena());
            sentencia.setString(5, usuario.getRol());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return lista de usuarios registrados
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public List<Usuario> readAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id, nombre, rut, correo, contraseña, rol FROM usuarios";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Usuario usuario = new Usuario(resultado.getInt("id"), resultado.getString("nombre"), resultado.getString("rut"), resultado.getString("correo"), resultado.getString("contraseña"), resultado.getString("rol"));

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario usuario con los datos actualizados
     * @return true si el usuario fue actualizado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, rut = ?, correo = ?, contraseña = ?, rol = ? WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getRut());
            sentencia.setString(3, usuario.getCorreo());
            sentencia.setString(4, usuario.getContrasena());
            sentencia.setString(5, usuario.getRol());
            sentencia.setInt(6, usuario.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un usuario de la base de datos según su identificador.
     *
     * @param id identificador del usuario
     * @return true si el usuario fue eliminado correctamente
     * @throws SQLException si ocurre un error durante la operación
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Busca un usuario según su correo y contraseña para realizar la autenticación.
     *
     * @param correo correo ingresado por el usuario
     * @param contrasena contraseña ingresada por el usuario
     * @return usuario encontrado o null si las credenciales no coinciden
     * @throws SQLException si ocurre un error durante la consulta
     */
    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT id, nombre, rut, correo, contraseña, rol FROM usuarios WHERE correo = ? AND contraseña = ?";

        try (
                Connection conexion = DatabaseConnection.getInstance().getConnection();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, correo);
            sentencia.setString(2, contrasena);

            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return new Usuario(resultado.getInt("id"), resultado.getString("nombre"), resultado.getString("rut"), resultado.getString("correo"), resultado.getString("contraseña"), resultado.getString("rol"));
                }
            }
        }

        return null;
    }
}