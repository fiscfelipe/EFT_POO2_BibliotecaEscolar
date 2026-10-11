package model;

/**
 * Representa un estudiante registrado en el sistema de biblioteca.
 */
public class Estudiante extends Persona {

    private String curso;

    /**
     * Constructor para crear un estudiante.
     *
     * @param id identificador del estudiante
     * @param nombre nombre completo
     * @param rut RUT del estudiante
     * @param curso curso del estudiante
     * @param correo correo electrónico
     */
    public Estudiante(int id, String nombre, String rut, String curso, String correo) {
        super(id, nombre, rut, correo);
        this.curso = curso;
    }

    // Setters

    public void setCurso(String curso) {
        this.curso = curso;
    }

    // Getters

    public String getCurso() {
        return curso;
    }

    // Métodos

    /**
     * Devuelve una descripción breve del estudiante.
     *
     * @return curso del estudiante
     */
    @Override
    public String getDescripcion() {
        return "Curso: " + curso;
    }

    /**
     * Devuelve una representación textual de los datos del estudiante.
     *
     * @return información del estudiante en formato de texto
     */
    @Override
    public String toString() {
        return "Estudiante{" +
                "\n  id=" + getId() +
                "\n  nombre='" + getNombre() + '\'' +
                "\n  rut='" + getRut() + '\'' +
                "\n  curso='" + curso + '\'' +
                "\n  correo='" + getCorreo() + '\'' +
                "\n}";
    }

    /**
     * Devuelve el tipo de persona representado por esta clase.
     *
     * @return tipo de persona
     */
    @Override
    public String getTipoPersona() {
        return "ESTUDIANTE";
    }
}