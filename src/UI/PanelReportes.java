package UI;

import controller.EstudianteController;
import controller.ReporteController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Estudiante;
import model.LibroPrestadoResumen;
import model.Prestamo;
import util.AplicacionException;

/**
 * Representa la vista para consultar los reportes del sistema de biblioteca.
 */
public class PanelReportes extends JPanel {

    private final ReporteController reporteController;
    private final EstudianteController estudianteController;

    private JTable tablaLibrosPrestados;
    private JTable tablaHistorial;
    private JTable tablaPrestamosActuales;

    private DefaultTableModel modeloLibrosPrestados;
    private DefaultTableModel modeloHistorial;
    private DefaultTableModel modeloPrestamosActuales;

    private JComboBox<Estudiante> cmbEstudiante;

    /**
     * Crea el panel de reportes.
     */
    public PanelReportes() {
        this.reporteController = new ReporteController();
        this.estudianteController = new EstudianteController();

        configurarPanel();
        crearComponentes();
        cargarEstudiantes();
        cargarLibrosMasPrestados();
        cargarPrestamosActuales();
    }

    // Métodos

    /**
     * Configura las propiedades generales del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Crea y organiza los componentes del panel.
     */
    private void crearComponentes() {
        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Libros más prestados", crearPanelLibrosMasPrestados());
        pestanas.addTab("Historial por estudiante", crearPanelHistorial());
        pestanas.addTab("Préstamos actuales", crearPanelPrestamosActuales());

        add(lblTitulo, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
    }

    /**
     * Crea el panel del reporte de libros más prestados.
     *
     * @return panel del reporte
     */
    private JPanel crearPanelLibrosMasPrestados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Categoría", "Cantidad de préstamos"};

        modeloLibrosPrestados = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibrosPrestados = new JTable(modeloLibrosPrestados);
        tablaLibrosPrestados.setAutoCreateRowSorter(true);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarLibrosMasPrestados());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnActualizar);

        panel.add(new JScrollPane(tablaLibrosPrestados), BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea el panel del historial de préstamos por estudiante.
     *
     * @return panel del historial
     */
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelFiltro = new JPanel(new BorderLayout(10, 0));

        cmbEstudiante = new JComboBox<>();
        configurarRendererEstudiantes();

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> cargarHistorial());

        panelFiltro.add(new JLabel("Estudiante:"), BorderLayout.WEST);
        panelFiltro.add(cmbEstudiante, BorderLayout.CENTER);
        panelFiltro.add(btnConsultar, BorderLayout.EAST);

        String[] columnas = {"ID", "Libro", "Fecha préstamo", "Fecha devolución", "Estado"};

        modeloHistorial = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setAutoCreateRowSorter(true);

        panel.add(panelFiltro, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel del reporte de préstamos actualmente activos.
     *
     * @return panel del reporte
     */
    private JPanel crearPanelPrestamosActuales() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Estudiante", "Libro", "Fecha préstamo", "Fecha devolución", "Estado"};

        modeloPrestamosActuales = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPrestamosActuales = new JTable(modeloPrestamosActuales);
        tablaPrestamosActuales.setAutoCreateRowSorter(true);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarPrestamosActuales());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnActualizar);

        panel.add(new JScrollPane(tablaPrestamosActuales), BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Configura la forma en que se visualizan los estudiantes en el JComboBox.
     */
    private void configurarRendererEstudiantes() {
        cmbEstudiante.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean tieneFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, tieneFoco);

                if (valor instanceof Estudiante estudiante) {
                    setText(estudiante.getNombre() + " - " + estudiante.getRut());
                }

                return this;
            }
        });
    }

    /**
     * Carga los estudiantes disponibles para consultar su historial.
     */
    private void cargarEstudiantes() {
        try {
            List<Estudiante> estudiantes = estudianteController.obtenerEstudiantes();

            cmbEstudiante.removeAllItems();

            for (Estudiante estudiante : estudiantes) {
                cmbEstudiante.addItem(estudiante);
            }

            cmbEstudiante.setSelectedIndex(-1);

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga el reporte de libros más prestados.
     */
    private void cargarLibrosMasPrestados() {
        try {
            List<LibroPrestadoResumen> resumenes = reporteController.obtenerLibrosMasPrestados();

            modeloLibrosPrestados.setRowCount(0);

            for (LibroPrestadoResumen resumen : resumenes) {
                modeloLibrosPrestados.addRow(new Object[]{
                    resumen.getLibro().getId(),
                    resumen.getLibro().getTitulo(),
                    resumen.getLibro().getAutor(),
                    resumen.getLibro().getIsbn(),
                    resumen.getLibro().getCategoria().getNombre(),
                    resumen.getCantidadPrestamos()
                });
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga el historial de préstamos del estudiante seleccionado.
     */
    private void cargarHistorial() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();

        if (estudiante == null) {
            mostrarError("Debe seleccionar un estudiante.");
            return;
        }

        try {
            List<Prestamo> prestamos = reporteController.obtenerHistorialPorEstudiante(estudiante.getId());

            modeloHistorial.setRowCount(0);

            for (Prestamo prestamo : prestamos) {
                String estado = obtenerEstadoPrestamo(prestamo);

                modeloHistorial.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getLibro().getTitulo(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    estado
                });
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga el reporte de préstamos actualmente pendientes.
     */
    private void cargarPrestamosActuales() {
        try {
            List<Prestamo> prestamos = reporteController.obtenerPrestamosActuales();

            modeloPrestamosActuales.setRowCount(0);

            for (Prestamo prestamo : prestamos) {
                String estado = obtenerEstadoPrestamo(prestamo);

                modeloPrestamosActuales.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getEstudiante().getNombre(),
                    prestamo.getLibro().getTitulo(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    estado
                });
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Obtiene el estado visible de un préstamo.
     *
     * @param prestamo préstamo que se desea evaluar
     * @return estado del préstamo
     */
    private String obtenerEstadoPrestamo(Prestamo prestamo) {
        if (prestamo.isDevuelto()) {
            return "Devuelto";
        }

        if (prestamo.estaAtrasado()) {
            return "Atrasado";
        }

        return "Vigente";
    }

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param mensaje mensaje que se desea mostrar
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.WARNING_MESSAGE);
    }
}