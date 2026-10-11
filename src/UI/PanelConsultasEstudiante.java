package UI;

import controller.ReporteController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Estudiante;
import model.Prestamo;
import util.AplicacionException;

/**
 * Representa la vista de consulta del historial de préstamos de un estudiante autenticado.
 */
public class PanelConsultasEstudiante extends JPanel {

    private final Estudiante estudiante;
    private final ReporteController reporteController;

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    /**
     * Crea el panel de consultas correspondiente al estudiante autenticado.
     *
     * @param estudiante estudiante asociado al usuario que inició sesión
     */
    public PanelConsultasEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.reporteController = new ReporteController();

        configurarPanel();
        crearComponentes();
        cargarHistorial();
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
        JPanel panelEncabezado = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("Mi historial de préstamos");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));

        JLabel lblEstudiante = new JLabel(estudiante.getNombre() + " - " + estudiante.getRut());

        panelEncabezado.add(lblTitulo, BorderLayout.NORTH);
        panelEncabezado.add(lblEstudiante, BorderLayout.SOUTH);

        String[] columnas = {"ID", "Libro", "Fecha préstamo", "Fecha devolución", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setAutoCreateRowSorter(true);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarHistorial());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnActualizar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    /**
     * Carga el historial completo de préstamos del estudiante autenticado.
     */
    private void cargarHistorial() {
        try {
            List<Prestamo> prestamos = reporteController.obtenerHistorialPorEstudiante(estudiante.getId());

            modeloTabla.setRowCount(0);

            for (Prestamo prestamo : prestamos) {
                modeloTabla.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getLibro().getTitulo(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    obtenerEstadoPrestamo(prestamo)
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