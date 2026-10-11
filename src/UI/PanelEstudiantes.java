package UI;

import controller.EstudianteController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Estudiante;
import util.AplicacionException;

/**
 * Representa la vista para gestionar los estudiantes de la biblioteca.
 */
public class PanelEstudiantes extends JPanel {

    private final EstudianteController estudianteController;

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;

    private int idEstudianteSeleccionado;

    /**
     * Crea el panel de gestión de estudiantes.
     */
    public PanelEstudiantes() {
        this.estudianteController = new EstudianteController();
        this.idEstudianteSeleccionado = 0;

        configurarPanel();
        crearComponentes();
        cargarEstudiantes();
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
        JLabel lblTituloPanel = new JLabel("Gestión de estudiantes");
        lblTituloPanel.setFont(lblTituloPanel.getFont().deriveFont(Font.BOLD, 20f));

        add(lblTituloPanel, BorderLayout.NORTH);

        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelTabla = crearPanelTabla();

        JSplitPane panelDividido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFormulario, panelTabla);
        panelDividido.setResizeWeight(0.35);
        panelDividido.setBorder(null);

        add(panelDividido, BorderLayout.CENTER);
    }

    /**
     * Crea el formulario utilizado para ingresar y modificar estudiantes.
     *
     * @return panel con los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del estudiante"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 5, 5, 5);
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 1.0;

        txtNombre = new JTextField();
        txtRut = new JTextField();
        txtCurso = new JTextField();
        txtCorreo = new JTextField();

        agregarCampo(panelCampos, restricciones, 0, 0, "Nombre:", txtNombre);
        agregarCampo(panelCampos, restricciones, 0, 2, "RUT:", txtRut);
        agregarCampo(panelCampos, restricciones, 1, 0, "Curso:", txtCurso);
        agregarCampo(panelCampos, restricciones, 1, 2, "Correo:", txtCorreo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        panel.add(panelCampos, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnActualizar.addActionListener(e -> actualizarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());

        actualizarEstadoBotones();

        return panel;
    }

    /**
     * Crea la tabla utilizada para visualizar los estudiantes registrados.
     *
     * @return panel que contiene la tabla de estudiantes
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Estudiantes registrados"));

        String[] columnas = {"ID", "Nombre", "RUT", "Curso", "Correo"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setAutoCreateRowSorter(true);

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarEstudiante();
            }
        });

        panel.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Agrega un campo con su etiqueta al formulario.
     *
     * @param panel panel donde se agrega el campo
     * @param restricciones configuración de GridBagLayout
     * @param fila fila donde se ubicará el campo
     * @param columna columna inicial donde se ubicará el campo
     * @param textoEtiqueta texto de la etiqueta
     * @param componente componente de entrada
     */
    private void agregarCampo(JPanel panel, GridBagConstraints restricciones, int fila, int columna, String textoEtiqueta, JComponent componente) {
        restricciones.gridy = fila;
        restricciones.gridx = columna;
        restricciones.weightx = 0.0;
        panel.add(new JLabel(textoEtiqueta), restricciones);

        restricciones.gridx = columna + 1;
        restricciones.weightx = 1.0;
        panel.add(componente, restricciones);
    }

    /**
     * Carga los estudiantes registrados en la tabla.
     */
    private void cargarEstudiantes() {
        try {
            List<Estudiante> estudiantes = estudianteController.obtenerEstudiantes();

            modeloTabla.setRowCount(0);

            for (Estudiante estudiante : estudiantes) {
                modeloTabla.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
                });
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Guarda un nuevo estudiante utilizando los datos ingresados en el formulario.
     */
    private void guardarEstudiante() {
        try {
            Estudiante estudiante = obtenerEstudianteFormulario(0);

            estudianteController.crearEstudiante(estudiante);

            JOptionPane.showMessageDialog(this, "Estudiante registrado correctamente.", "Estudiante registrado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarEstudiantes();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Actualiza el estudiante seleccionado utilizando los datos del formulario.
     */
    private void actualizarEstudiante() {
        if (idEstudianteSeleccionado == 0) {
            mostrarError("Debe seleccionar un estudiante para actualizar.");
            return;
        }

        try {
            Estudiante estudiante = obtenerEstudianteFormulario(idEstudianteSeleccionado);

            estudianteController.actualizarEstudiante(estudiante);

            JOptionPane.showMessageDialog(this, "Estudiante actualizado correctamente.", "Estudiante actualizado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarEstudiantes();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Elimina el estudiante seleccionado.
     */
    private void eliminarEstudiante() {
        if (idEstudianteSeleccionado == 0) {
            mostrarError("Debe seleccionar un estudiante para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el estudiante seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            estudianteController.eliminarEstudiante(idEstudianteSeleccionado);

            JOptionPane.showMessageDialog(this, "Estudiante eliminado correctamente.", "Estudiante eliminado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarEstudiantes();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Construye un estudiante utilizando los valores ingresados en el formulario.
     *
     * @param id identificador que tendrá el estudiante
     * @return estudiante construido con los datos del formulario
     * @throws AplicacionException si los datos ingresados no son válidos
     */
    private Estudiante obtenerEstudianteFormulario(int id) throws AplicacionException {
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isBlank()) {
            throw new AplicacionException("Debe ingresar el nombre del estudiante.");
        }

        if (rut.isBlank()) {
            throw new AplicacionException("Debe ingresar el RUT del estudiante.");
        }

        if (curso.isBlank()) {
            throw new AplicacionException("Debe ingresar el curso del estudiante.");
        }

        if (correo.isBlank()) {
            throw new AplicacionException("Debe ingresar el correo del estudiante.");
        }

        return new Estudiante(id, nombre, rut, curso, correo);
    }

    /**
     * Carga en el formulario los datos del estudiante seleccionado en la tabla.
     */
    private void seleccionarEstudiante() {
        int filaVista = tablaEstudiantes.getSelectedRow();

        if (filaVista == -1) {
            return;
        }

        int filaModelo = tablaEstudiantes.convertRowIndexToModel(filaVista);

        idEstudianteSeleccionado = (int) modeloTabla.getValueAt(filaModelo, 0);

        txtNombre.setText((String) modeloTabla.getValueAt(filaModelo, 1));
        txtRut.setText((String) modeloTabla.getValueAt(filaModelo, 2));
        txtCurso.setText((String) modeloTabla.getValueAt(filaModelo, 3));
        txtCorreo.setText((String) modeloTabla.getValueAt(filaModelo, 4));

        actualizarEstadoBotones();
    }

    /**
     * Limpia el formulario y elimina la selección actual.
     */
    private void limpiarFormulario() {
        idEstudianteSeleccionado = 0;

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        tablaEstudiantes.clearSelection();

        actualizarEstadoBotones();
        txtNombre.requestFocus();
    }

    /**
     * Actualiza los botones según exista o no un estudiante seleccionado.
     */
    private void actualizarEstadoBotones() {
        boolean estudianteSeleccionado = idEstudianteSeleccionado != 0;

        btnGuardar.setEnabled(!estudianteSeleccionado);
        btnActualizar.setEnabled(estudianteSeleccionado);
        btnEliminar.setEnabled(estudianteSeleccionado);
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