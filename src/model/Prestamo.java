package model;

import java.time.LocalDate;

/**
 * Representa un préstamo de un libro realizado por un estudiante.
 */
public class Prestamo {

    private int id;
    private Estudiante estudiante;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    /**
     * Constructor para crear un préstamo.
     *
     * @param id identificador del préstamo
     * @param estudiante estudiante asociado al préstamo
     * @param libro libro asociado al préstamo
     * @param fechaPrestamo fecha en que se realiza el préstamo
     * @param fechaDevolucion fecha establecida para la devolución
     * @param devuelto indica si el libro fue devuelto
     */
    public Prestamo(int id, Estudiante estudiante, Libro libro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean devuelto) {
        this.id = id;
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    // Getters

    public int getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    // Métodos

    /**
     * Indica si el préstamo se encuentra atrasado.
     *
     * @return true si no ha sido devuelto y la fecha de devolución ya pasó
     */
    public boolean estaAtrasado() {
        if (!devuelto && fechaDevolucion != null && fechaDevolucion.isBefore(LocalDate.now())) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve una representación textual de los datos del préstamo.
     *
     * Incluye información básica del estudiante y del libro asociados. Si alguno de estos objetos es nulo, se indica en la salida.
     *
     * @return información del préstamo en formato de texto
     */
    @Override
    public String toString() {
        String texto = "Prestamo{" +
                "\n  id=" + id +
                "\n  fechaPrestamo=" + fechaPrestamo +
                "\n  fechaDevolucion=" + fechaDevolucion +
                "\n  devuelto=" + devuelto;

        if (estudiante != null) {
            texto += "\n  estudiante:" +
                    "\n    id=" + estudiante.getId() +
                    "\n    nombre='" + estudiante.getNombre() + '\'' +
                    "\n    rut='" + estudiante.getRut() + '\'';
        } else {
            texto += "\n  estudiante=null";
        }

        if (libro != null) {
            texto += "\n  libro:" +
                    "\n    id=" + libro.getId() +
                    "\n    titulo='" + libro.getTitulo() + '\'' +
                    "\n    isbn='" + libro.getIsbn() + '\'';
        } else {
            texto += "\n  libro=null";
        }

        texto += "\n}";

        return texto;
    }
}