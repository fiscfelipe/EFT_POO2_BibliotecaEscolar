package controller;

import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;
import model.Usuario;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;

/**
 * Gestiona las operaciones relacionadas con los usuarios entre la vista y la capa de acceso a datos.
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor para crear el controlador de usuarios.
     */
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param usuario usuario que se desea registrar
     * @return true si el usuario fue registrado correctamente
     * @throws AplicacionException si ocurre un problema durante el registro
     */
    public boolean crearUsuario(Usuario usuario) throws AplicacionException {
        try {
            return usuarioDAO.create(usuario);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo registrar el usuario porque el RUT ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar el usuario debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios registrados
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public List<Usuario> obtenerUsuarios() throws AplicacionException {
        try {
            return usuarioDAO.readAll();

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la lista de usuarios desde la base de datos.", ex);
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario usuario con los datos actualizados
     * @return true si el usuario fue actualizado correctamente
     * @throws AplicacionException si ocurre un problema durante la actualización
     */
    public boolean actualizarUsuario(Usuario usuario) throws AplicacionException {
        try {
            return usuarioDAO.update(usuario);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo actualizar el usuario porque el RUT ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo actualizar el usuario debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Elimina un usuario según su identificador.
     *
     * @param id identificador del usuario
     * @return true si el usuario fue eliminado correctamente
     * @throws AplicacionException si ocurre un problema durante la eliminación
     */
    public boolean eliminarUsuario(int id) throws AplicacionException {
        try {
            return usuarioDAO.delete(id);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo eliminar el usuario debido a un problema al actualizar la base de datos.", ex);
        }
    }

    /**
     * Autentica un usuario mediante su correo y contraseña.
     *
     * @param correo correo ingresado por el usuario
     * @param contrasena contraseña ingresada por el usuario
     * @return usuario autenticado
     * @throws AplicacionException si las credenciales son inválidas o ocurre un problema durante la autenticación
     */
    public Usuario autenticarUsuario(String correo, String contrasena) throws AplicacionException {
        if (correo == null || correo.isBlank()) {
            throw new AplicacionException("Debe ingresar su correo.");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new AplicacionException("Debe ingresar su contraseña.");
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(correo, contrasena);

            if (usuario == null) {
                throw new AplicacionException("Correo o contraseña incorrectos.");
            }

            return usuario;

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No fue posible iniciar sesión debido a un problema al consultar los datos del usuario.", ex);
        }
    }
}