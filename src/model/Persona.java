package model;

/**
 * Representa una persona registrada en el sistema de biblioteca.
 *
 * Define los atributos comunes compartidos por usuarios y estudiantes.
 */
public abstract class Persona {

    private int id;
    private String nombre;
    private String rut;
    private String correo;

    /**
     * Constructor para crear una persona.
     *
     * @param id identificador de la persona
     * @param nombre nombre completo
     * @param rut RUT de la persona
     * @param correo correo electrónico
     */
    public Persona(int id, String nombre, String rut, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRut() {
        return rut;
    }

    public String getCorreo() {
        return correo;
    }

    // Métodos

    /**
     * Devuelve una descripción breve de la persona según su tipo concreto.
     *
     * @return descripción de la persona
     */
    public abstract String getDescripcion();

    /**
     * Devuelve el tipo de persona representada por la subclase.
     *
     * @return tipo de persona
     */
    public abstract String getTipoPersona();

    /**
     * Devuelve una representación textual de los datos de la persona.
     *
     * @return información de la persona en formato de texto
     */
    @Override
    public String toString() {
        return "Persona{" +
                "\n  id=" + id +
                "\n  nombre='" + nombre + '\'' +
                "\n  rut='" + rut + '\'' +
                "\n  correo='" + correo + '\'' +
                "\n}";
    }
}