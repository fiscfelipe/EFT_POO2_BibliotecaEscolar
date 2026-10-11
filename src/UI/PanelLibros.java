package UI;

import controller.CategoriaController;
import controller.LibroController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import model.Libro;
import util.AplicacionException;

/**
 * Representa la vista para gestionar los libros de la biblioteca.
 */
public class PanelLibros extends JPanel {

    private final LibroController libroController;
    private final CategoriaController categoriaController;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JComboBox<Categoria> cmbCategoria;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;

    private int idLibroSeleccionado;

    /**
     * Crea el panel de gestión de libros.
     */
    public PanelLibros() {
        this.libroController = new LibroController();
        this.categoriaController = new CategoriaController();
        this.idLibroSeleccionado = 0;

        configurarPanel();
        crearComponentes();
        cargarCategorias();
        cargarLibros();
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
        JLabel lblTituloPanel = new JLabel("Gestión de libros");
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
     * Crea el formulario utilizado para ingresar y modificar libros.
     *
     * @return panel con los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del libro"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 5, 5, 5);
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 1.0;

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtEditorial = new JTextField();
        txtStock = new JTextField();

        cmbCategoria = new JComboBox<>();
        configurarRendererCategorias();

        agregarCampo(panelCampos, restricciones, 0, 0, "Título:", txtTitulo);
        agregarCampo(panelCampos, restricciones, 0, 2, "Autor:", txtAutor);
        agregarCampo(panelCampos, restricciones, 1, 0, "ISBN:", txtIsbn);
        agregarCampo(panelCampos, restricciones, 1, 2, "Editorial:", txtEditorial);
        agregarCampo(panelCampos, restricciones, 2, 0, "Stock:", txtStock);
        agregarCampo(panelCampos, restricciones, 2, 2, "Categoría:", cmbCategoria);

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
        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());

        actualizarEstadoBotones();

        return panel;
    }

    /**
     * Crea la tabla utilizada para visualizar los libros registrados.
     *
     * @return panel que contiene la tabla de libros
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Libros registrados"));

        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setAutoCreateRowSorter(true);

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarLibro();
            }
        });

        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

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
     * Configura la forma en que se visualizan las categorías en el JComboBox.
     */
    private void configurarRendererCategorias() {
        cmbCategoria.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean tieneFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, tieneFoco);

                if (valor instanceof Categoria categoria) {
                    setText(categoria.getNombre());
                }

                return this;
            }
        });
    }

    /**
     * Carga las categorías disponibles desde la base de datos.
     */
    private void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaController.obtenerCategorias();

            cmbCategoria.removeAllItems();

            for (Categoria categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga los libros registrados en la tabla.
     */
    private void cargarLibros() {
        try {
            List<Libro> libros = libroController.obtenerLibros();

            modeloTabla.setRowCount(0);

            for (Libro libro : libros) {
                String nombreCategoria = "";

                if (libro.getCategoria() != null) {
                    nombreCategoria = libro.getCategoria().getNombre();
                }

                modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    nombreCategoria
                });
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Guarda un nuevo libro utilizando los datos ingresados en el formulario.
     */
    private void guardarLibro() {
        try {
            Libro libro = obtenerLibroFormulario(0);

            libroController.crearLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro registrado correctamente.", "Libro registrado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarLibros();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Actualiza el libro seleccionado utilizando los datos del formulario.
     */
    private void actualizarLibro() {
        if (idLibroSeleccionado == 0) {
            mostrarError("Debe seleccionar un libro para actualizar.");
            return;
        }

        try {
            Libro libro = obtenerLibroFormulario(idLibroSeleccionado);

            libroController.actualizarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.", "Libro actualizado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarLibros();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Elimina el libro seleccionado.
     */
    private void eliminarLibro() {
        if (idLibroSeleccionado == 0) {
            mostrarError("Debe seleccionar un libro para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el libro seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            libroController.eliminarLibro(idLibroSeleccionado);

            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.", "Libro eliminado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarLibros();

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Construye un libro utilizando los valores ingresados en el formulario.
     *
     * @param id identificador que tendrá el libro
     * @return libro construido con los datos del formulario
     * @throws AplicacionException si los datos ingresados no son válidos
     */
    private Libro obtenerLibroFormulario(int id) throws AplicacionException {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String textoStock = txtStock.getText().trim();
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        if (titulo.isBlank()) {
            throw new AplicacionException("Debe ingresar el título del libro.");
        }

        if (autor.isBlank()) {
            throw new AplicacionException("Debe ingresar el autor del libro.");
        }

        if (isbn.isBlank()) {
            throw new AplicacionException("Debe ingresar el ISBN del libro.");
        }

        if (editorial.isBlank()) {
            throw new AplicacionException("Debe ingresar la editorial del libro.");
        }

        if (textoStock.isBlank()) {
            throw new AplicacionException("Debe ingresar el stock del libro.");
        }

        int stock;

        try {
            stock = Integer.parseInt(textoStock);
        } catch (NumberFormatException ex) {
            throw new AplicacionException("El stock debe ser un número entero.");
        }

        if (stock < 0) {
            throw new AplicacionException("El stock no puede ser negativo.");
        }

        if (categoria == null) {
            throw new AplicacionException("Debe seleccionar una categoría.");
        }

        return new Libro(id, titulo, autor, isbn, editorial, stock, categoria);
    }

    /**
     * Carga en el formulario los datos del libro seleccionado en la tabla.
     */
    private void seleccionarLibro() {
        int filaVista = tablaLibros.getSelectedRow();

        if (filaVista == -1) {
            return;
        }

        int filaModelo = tablaLibros.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);

        try {
            List<Libro> libros = libroController.obtenerLibros();

            for (Libro libro : libros) {
                if (libro.getId() == id) {
                    idLibroSeleccionado = libro.getId();

                    txtTitulo.setText(libro.getTitulo());
                    txtAutor.setText(libro.getAutor());
                    txtIsbn.setText(libro.getIsbn());
                    txtEditorial.setText(libro.getEditorial());
                    txtStock.setText(String.valueOf(libro.getStock()));

                    seleccionarCategoria(libro.getCategoria());

                    actualizarEstadoBotones();
                    return;
                }
            }

        } catch (AplicacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Selecciona en el JComboBox la categoría correspondiente al libro.
     *
     * @param categoria categoría que se desea seleccionar
     */
    private void seleccionarCategoria(Categoria categoria) {
        if (categoria == null) {
            cmbCategoria.setSelectedIndex(-1);
            return;
        }

        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria categoriaCombo = cmbCategoria.getItemAt(i);

            if (categoriaCombo.getId() == categoria.getId()) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Limpia el formulario y elimina la selección actual.
     */
    private void limpiarFormulario() {
        idLibroSeleccionado = 0;

        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        tablaLibros.clearSelection();

        actualizarEstadoBotones();
        txtTitulo.requestFocus();
    }

    /**
     * Actualiza los botones según exista o no un libro seleccionado.
     */
    private void actualizarEstadoBotones() {
        boolean libroSeleccionado = idLibroSeleccionado != 0;

        btnGuardar.setEnabled(!libroSeleccionado);
        btnActualizar.setEnabled(libroSeleccionado);
        btnEliminar.setEnabled(libroSeleccionado);
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