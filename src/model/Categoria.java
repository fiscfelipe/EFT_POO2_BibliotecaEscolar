package model;

/**
 * Representa una categoría de libros dentro del sistema.
 */
public class Categoria {

    private int id;
    private String nombre;

    /**
     * Constructor para crear una categoría.
     *
     * @param id identificador de la categoría
     * @param nombre nombre de la categoría
     */
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Métodos

    /**
     * Devuelve una representación textual de los datos de la categoría.
     *
     * @return información de la categoría en formato de texto
     */
    @Override
    public String toString() {
        return "Categoria{" +
                "\n  id=" + id +
                "\n  nombre='" + nombre + '\'' +
                "\n}";
    }
}