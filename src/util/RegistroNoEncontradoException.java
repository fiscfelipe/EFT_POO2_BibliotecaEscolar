package util;

/**
 * Representa un error producido cuando no se encuentra un registro necesario para completar una operación.
 */
public class RegistroNoEncontradoException extends RuntimeException {

    /**
     * Crea una excepción indicando que no se encontró el registro requerido.
     *
     * @param mensaje mensaje que describe el registro que no fue encontrado
     */
    public RegistroNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea una excepción indicando que no se encontró el registro requerido y conserva la causa original.
     *
     * @param mensaje mensaje que describe el registro que no fue encontrado
     * @param causa excepción original que provocó el error
     */
    public RegistroNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}