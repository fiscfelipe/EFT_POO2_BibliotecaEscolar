package model;

/**
 * Representa el resumen de préstamos asociados a un libro para fines de reporte.
 */
public class LibroPrestadoResumen {

    private Libro libro;
    private int cantidadPrestamos;

    /**
     * Crea un resumen de préstamos para un libro.
     *
     * @param libro libro asociado al resumen
     * @param cantidadPrestamos cantidad total de préstamos registrados
     */
    public LibroPrestadoResumen(Libro libro, int cantidadPrestamos) {
        this.libro = libro;
        this.cantidadPrestamos = cantidadPrestamos;
    }

    // Setters
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public void setCantidadPrestamos(int cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;
    }

    // Getters
    public Libro getLibro() {
        return libro;
    }

    public int getCantidadPrestamos() {
        return cantidadPrestamos;
    }

    // Métodos
    @Override
    public String toString() {
        String texto = "LibroPrestadoResumen{\n";

        if (libro != null) {
            texto += "  libro:\n";
            texto += "    id=" + libro.getId() + "\n";
            texto += "    titulo='" + libro.getTitulo() + "'\n";
            texto += "    isbn='" + libro.getIsbn() + "'\n";
        } else {
            texto += "  libro=null\n";
        }

        texto += "  cantidadPrestamos=" + cantidadPrestamos + "\n";
        texto += "}";

        return texto;
    }
}