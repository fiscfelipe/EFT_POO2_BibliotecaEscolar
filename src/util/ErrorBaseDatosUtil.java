package util;

import java.sql.SQLException;

/**
 * Centraliza la identificación de errores SQL comunes utilizados por los controladores.
 */
public class ErrorBaseDatosUtil {

    /**
     * Indica si una excepción SQL corresponde a un problema de conexión con la base de datos.
     *
     * @param ex excepción SQL que se desea evaluar
     * @return true si corresponde a un problema de conexión
     */
    public static boolean esErrorConexion(SQLException ex) {
        if (ex.getSQLState() != null && ex.getSQLState().startsWith("08")) {
            return true;
        }

        return false;
    }

    /**
     * Indica si una excepción SQL corresponde a un valor duplicado.
     *
     * @param ex excepción SQL que se desea evaluar
     * @return true si corresponde a un valor duplicado
     */
    public static boolean esDuplicado(SQLException ex) {
        if (ex.getErrorCode() == 1062) {
            return true;
        }

        return false;
    }

    /**
     * Indica si una excepción SQL corresponde a un registro que está siendo referenciado por otra tabla.
     *
     * @param ex excepción SQL que se desea evaluar
     * @return true si corresponde a un registro referenciado
     */
    public static boolean esRegistroReferenciado(SQLException ex) {
        if (ex.getErrorCode() == 1451) {
            return true;
        }

        return false;
    }

    /**
     * Indica si una excepción SQL corresponde a una clave foránea inexistente.
     *
     * @param ex excepción SQL que se desea evaluar
     * @return true si corresponde a una clave foránea inexistente
     */
    public static boolean esClaveForaneaInexistente(SQLException ex) {
        if (ex.getErrorCode() == 1452) {
            return true;
        }

        return false;
    }
}