package controller;

import dao.EstudianteDAO;
import java.sql.SQLException;
import java.util.List;
import model.Estudiante;
import util.AplicacionException;
import util.ErrorBaseDatosUtil;

/**
 * Gestiona las operaciones relacionadas con los estudiantes entre la vista y la capa de acceso a datos.
 */
public class EstudianteController {

    private final EstudianteDAO estudianteDAO;

    /**
     * Constructor para crear el controlador de estudiantes.
     */
    public EstudianteController() {
        this.estudianteDAO = new EstudianteDAO();
    }

    /**
     * Registra un nuevo estudiante.
     *
     * @param estudiante estudiante que se desea registrar
     * @return true si el estudiante fue registrado correctamente
     * @throws AplicacionException si ocurre un problema durante el registro
     */
    public boolean crearEstudiante(Estudiante estudiante) throws AplicacionException {
        try {
            return estudianteDAO.create(estudiante);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo registrar el estudiante porque el RUT ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo registrar el estudiante debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Obtiene todos los estudiantes registrados.
     *
     * @return lista de estudiantes registrados
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public List<Estudiante> obtenerEstudiantes() throws AplicacionException {
        try {
            return estudianteDAO.readAll();

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la lista de estudiantes desde la base de datos.", ex);
        }
    }

    /**
     * Actualiza los datos de un estudiante existente.
     *
     * @param estudiante estudiante con los datos actualizados
     * @return true si el estudiante fue actualizado correctamente
     * @throws AplicacionException si ocurre un problema durante la actualización
     */
    public boolean actualizarEstudiante(Estudiante estudiante) throws AplicacionException {
        try {
            return estudianteDAO.update(estudiante);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esDuplicado(ex)) {
                throw new AplicacionException("No se pudo actualizar el estudiante porque el RUT ya se encuentra registrado.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo actualizar el estudiante debido a un problema al guardar sus datos.", ex);
        }
    }

    /**
     * Elimina un estudiante según su identificador.
     *
     * @param id identificador del estudiante
     * @return true si el estudiante fue eliminado correctamente
     * @throws AplicacionException si ocurre un problema durante la eliminación
     */
    public boolean eliminarEstudiante(int id) throws AplicacionException {
        try {
            return estudianteDAO.delete(id);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esRegistroReferenciado(ex)) {
                throw new AplicacionException("No se puede eliminar el estudiante porque tiene préstamos asociados.", ex);
            }

            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo eliminar el estudiante debido a un problema al actualizar la base de datos.", ex);
        }
    }

    /**
     * Busca un estudiante según su RUT.
     *
     * @param rut RUT del estudiante que se desea buscar
     * @return estudiante encontrado o null si no existe un estudiante con ese RUT
     * @throws AplicacionException si ocurre un problema durante la consulta
     */
    public Estudiante buscarEstudiantePorRut(String rut) throws AplicacionException {
        if (rut == null || rut.isBlank()) {
            throw new AplicacionException("No se puede buscar un estudiante sin RUT.");
        }

        try {
            return estudianteDAO.buscarPorRut(rut);

        } catch (SQLException ex) {
            if (ErrorBaseDatosUtil.esErrorConexion(ex)) {
                throw new AplicacionException("No fue posible conectarse a la base de datos. Verifique que MySQL esté iniciado.", ex);
            }

            throw new AplicacionException("No se pudo obtener la información del estudiante desde la base de datos.", ex);
        }
    }
}