package model;

/**
 * Representa un libro registrado en el sistema de biblioteca.
 */
public class Libro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int stock;
    private Categoria categoria;

    /**
     * Constructor para crear un libro.
     *
     * @param id identificador del libro
     * @param titulo título del libro
     * @param autor autor del libro
     * @param isbn código ISBN del libro
     * @param editorial editorial del libro
     * @param stock cantidad disponible
     * @param categoria categoría del libro
     */
    public Libro(int id, String titulo, String autor, String isbn, String editorial, int stock, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public int getStock() {
        return stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    // Métodos

    /**
     * Devuelve una representación textual de los datos del libro.
     *
     * @return información del libro en formato de texto
     */
    @Override
    public String toString() {
        String texto = "Libro{" +
                "\n  id=" + id +
                "\n  titulo='" + titulo + '\'' +
                "\n  autor='" + autor + '\'' +
                "\n  isbn='" + isbn + '\'' +
                "\n  editorial='" + editorial + '\'' +
                "\n  stock=" + stock +
                "\n  categoria:";

        if (categoria != null) {
            texto += "\n    id=" + categoria.getId() +
                    "\n    nombre='" + categoria.getNombre() + '\'';
        } else {
            texto += "\n    null";
        }

        texto += "\n}";

        return texto;
    }
}