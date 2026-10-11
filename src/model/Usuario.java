package model;

/**
 * Representa un usuario registrado en el sistema de biblioteca.
 *
 * Un usuario puede tener rol de bibliotecario o estudiante.
 */
public class Usuario extends Persona {

    private String contrasena;
    private String rol;

    /**
     * Constructor para crear un usuario.
     *
     * @param id identificador del usuario
     * @param nombre nombre completo
     * @param rut RUT del usuario
     * @param correo correo electrónico
     * @param contrasena contraseña del usuario
     * @param rol rol del usuario
     */
    public Usuario(int id, String nombre, String rut, String correo, String contrasena, String rol) {
        super(id, nombre, rut, correo);
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Setters

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Getters

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    // Métodos

    /**
     * Devuelve una descripción breve del usuario.
     *
     * @return rol del usuario
     */
    @Override
    public String getDescripcion() {
        return "Rol: " + rol;
    }

    /**
     * Devuelve una representación textual de los datos del usuario.
     *
     * No incluye la contraseña por razones de seguridad.
     *
     * @return información del usuario en formato de texto
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "\n  id=" + getId() +
                "\n  nombre='" + getNombre() + '\'' +
                "\n  rut='" + getRut() + '\'' +
                "\n  correo='" + getCorreo() + '\'' +
                "\n  rol='" + rol + '\'' +
                "\n}";
    }

    /**
     * Devuelve el tipo de persona representado por esta clase.
     *
     * @return tipo de persona
     */
    @Override
    public String getTipoPersona() {
        return "USUARIO";
    }

    /**
     * Indica si el usuario posee rol de bibliotecario.
     *
     * @return true si el usuario es bibliotecario
     */
    public boolean esBibliotecario() {
        return "bibliotecario".equalsIgnoreCase(rol);
    }

    /**
     * Indica si el usuario posee rol de estudiante.
     *
     * @return true si el usuario es estudiante
     */
    public boolean esEstudiante() {
        return "estudiante".equalsIgnoreCase(rol);
    }
}