package util;

/**
 * Representa errores de la aplicación que pueden ser comunicados al usuario mediante mensajes claros y comprensibles.
 */
public class AplicacionException extends Exception {

    /**
     * Crea una excepción de aplicación con un mensaje específico.
     *
     * @param mensaje mensaje que describe el problema
     */
    public AplicacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea una excepción de aplicación con un mensaje específico y conserva la causa original para depuración.
     *
     * @param mensaje mensaje que describe el problema
     * @param causa excepción original que provocó el error
     */
    public AplicacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}