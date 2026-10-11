package UI;

import controller.LibroController;
import controller.PrestamoController;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import util.AplicacionException;

/**
 * Representa la vista de préstamos y devoluciones disponible para un estudiante autenticado.
 */
public class PanelPrestamosEstudiante extends JPanel {

    private final Estudiante estudiante;
    private final PrestamoController prestamoController;
    private final LibroController libroController;

    private JComboBox<Libro> cmbLibro;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrarPrestamo;
    private JButton btnRegistrarDevolucion;

    private int idPrestamoSeleccionado;

    /**
     * Crea el panel de préstamos correspondiente al estudiante autenticado.
     *
     * @param estudiante estudiante asociado al usuario que inició sesión
     */
    public PanelPrestamosEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.prestamoController = new PrestamoController();
        this.libroController = new LibroController();
        this.idPrestamoSeleccionado = 0;

        configurarPanel();
        crearComponentes();
        cargarLibros();
        cargarPrestamos();
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
        JLabel lblTituloPanel = new JLabel("Mis préstamos y devoluciones");
        lblTituloPanel.setFont(lblTituloPanel.getFont().deriveFont(Font.BOLD, 20f));

        add(lblTituloPanel, BorderLayout.NORTH);

        JPanel panelRegistro = crearPanelRegistro();
        JPanel panelTabla = crearPanelTabla();

        JSplitPane panelDividido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelRegistro, panelTabla);
        panelDividido.setResizeWeight(0.28);
        panelDividido.setBorder(null);

        add(panelDividido, BorderLayout.CENTER);
    }

    /**
     * Crea el formulario para registrar un préstamo del estudiante autenticado.
     *
     * @return panel de registro de préstamos
     */
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar préstamo"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 5, 5, 5);
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 1.0;

        cmbLibro = new JComboBox<>();
        configurarRendererLibros();

        agregarCampo(panelCampos, restricciones, 0, "Estudiante:", new JLabel(estudiante.getNombre() + " - " + estudiante.getRut()));
        agregarCampo(panelCampos, restricciones, 1, "Libro:", cmbLibro);
        agregarCampo(panelCampos, restricciones, 2, "Fecha del préstamo:", new JLabel(LocalDate.now().toString()));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnRegistrarPrestamo = new JButton("Registrar préstamo");
        panelBotones.add(btnRegistrarPrestamo);

        panel.add(panelCampos, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnRegistrarPrestamo.addActionListener(e -> registrarPrestamo());

        return panel;
    }

    /**
     * Crea la tabla de préstamos pendientes del estudiante.
     *
     * @return panel que contiene la tabla de préstamos
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Mis préstamos pendientes"));

        String[] columnas = {"ID", "Libro", "Fecha préstamo", "Fecha devolución", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPrestamos = new JTable(modeloTabla);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrestamos.setAutoCreateRowSorter(true);

        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarPrestamo();
            }
        });

        btnRegistrarDevolucion = new JButton("Registrar devolución");
        btnRegistrarDevolucion.setEnabled(false);
        btnRegistrarDevolucion.addActionListener(e -> registrarDevolucion());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnRegistrarDevolucion);

        panel.add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Agrega un campo con su etiqueta al formulario.
     *
     * @param panel panel donde se agregará el campo
     * @param restricciones configuración de GridBagLayout
     * @param fila fila donde se ubicará el campo
     * @param textoEtiqueta texto de la etiqueta
     * @param componente componente de entrada
     */
    private void agregarCampo(JPanel panel, GridBagConstraints restricciones, int fila, String textoEtiqueta, JComponent componente) {
        restricciones.gridy = fila;
        restricciones.gridx = 0;
        restricciones.weightx = 0.0;
        panel.add(new JLabel(textoEtiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1.0;
        panel.add(componente, restricciones);
    }

    /**
     * Configura la forma en que se visualizan los libros disponibles.
     */
    private void configurarRendererLibros() {
        cmbLibro.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean tieneFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, tieneFoco);

                if (valor instanceof Libro libro) {
                    setText(libro.getTitulo() + " - stock: " + libro.getStock());
                }

                return this;
            }
        });
    }

    /**
     * Carga los libros registrados en la base de datos.
     */
    private void cargarLibros() {
        try {
            List<Libro> libros = libroController.obtenerLibros();

            cmbLibro.removeAllItems();

            for (Libro libro : libros) {
                cmbLibro.addItem(libro);
            }

            cmbLibro.setSelectedIndex(-1);

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga los préstamos pendientes correspondientes al estudiante autenticado.
     */
    private void cargarPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoController.obtenerPrestamos();

            modeloTabla.setRowCount(0);

            for (Prestamo prestamo : prestamos) {
                if (prestamo.getEstudiante().getId() == estudiante.getId() && !prestamo.isDevuelto()) {
                    String estado = "Vigente";

                    if (prestamo.estaAtrasado()) {
                        estado = "Atrasado";
                    }

                    modeloTabla.addRow(new Object[]{
                        prestamo.getId(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion(),
                        estado
                    });
                }
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Registra un nuevo préstamo para el estudiante autenticado en segundo plano.
     */
    private void registrarPrestamo() {
        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (libro == null) {
            mostrarError("Debe seleccionar un libro.");
            return;
        }

        Prestamo prestamo = new Prestamo(0, estudiante, libro, LocalDate.now(), null, false);

        btnRegistrarPrestamo.setEnabled(false);

        SwingWorker<Boolean, Void> trabajador = new SwingWorker<>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                return prestamoController.crearPrestamo(prestamo);
            }

            @Override
            protected void done() {
                try {
                    get();

                    JOptionPane.showMessageDialog(PanelPrestamosEstudiante.this, "Préstamo registrado correctamente. Fecha de devolución: " + prestamo.getFechaDevolucion(), "Préstamo registrado", JOptionPane.INFORMATION_MESSAGE);

                    cargarLibros();
                    cargarPrestamos();

                } catch (Exception ex) {
                    mostrarError(obtenerMensajeError(ex));

                } finally {
                    btnRegistrarPrestamo.setEnabled(true);
                }
            }
        };

        trabajador.execute();
    }

    /**
     * Registra la devolución del préstamo seleccionado en segundo plano.
     */
    private void registrarDevolucion() {
        if (idPrestamoSeleccionado == 0) {
            mostrarError("Debe seleccionar un préstamo para registrar la devolución.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea registrar la devolución del préstamo seleccionado?", "Confirmar devolución", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int idPrestamo = idPrestamoSeleccionado;

        btnRegistrarDevolucion.setEnabled(false);

        SwingWorker<Boolean, Void> trabajador = new SwingWorker<>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                return prestamoController.registrarDevolucion(idPrestamo);
            }

            @Override
            protected void done() {
                try {
                    get();

                    JOptionPane.showMessageDialog(PanelPrestamosEstudiante.this, "Devolución registrada correctamente.", "Devolución registrada", JOptionPane.INFORMATION_MESSAGE);

                    idPrestamoSeleccionado = 0;
                    tablaPrestamos.clearSelection();

                    cargarLibros();
                    cargarPrestamos();

                } catch (Exception ex) {
                    mostrarError(obtenerMensajeError(ex));

                } finally {
                    btnRegistrarDevolucion.setEnabled(idPrestamoSeleccionado != 0);
                }
            }
        };

        trabajador.execute();
    }

    /**
     * Guarda el identificador del préstamo seleccionado.
     */
    private void seleccionarPrestamo() {
        int filaVista = tablaPrestamos.getSelectedRow();

        if (filaVista == -1) {
            idPrestamoSeleccionado = 0;
            btnRegistrarDevolucion.setEnabled(false);
            return;
        }

        int filaModelo = tablaPrestamos.convertRowIndexToModel(filaVista);

        idPrestamoSeleccionado = (int) modeloTabla.getValueAt(filaModelo, 0);
        btnRegistrarDevolucion.setEnabled(true);
    }

    /**
     * Obtiene el mensaje original producido durante una operación ejecutada por SwingWorker.
     *
     * @param ex excepción recibida al finalizar la operación
     * @return mensaje que se mostrará al usuario
     */
    private String obtenerMensajeError(Exception ex) {
        Throwable causa = ex;

        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        if (causa.getMessage() != null && !causa.getMessage().isBlank()) {
            return causa.getMessage();
        }

        return "No fue posible completar la operación.";
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