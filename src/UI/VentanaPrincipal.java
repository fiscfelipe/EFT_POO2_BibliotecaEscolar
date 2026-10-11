package UI;

import controller.EstudianteController;
import java.awt.*;
import javax.swing.*;
import model.Estudiante;
import model.Persona;
import model.Usuario;
import util.AplicacionException;

/**
 * Representa la ventana principal del sistema de biblioteca.
 */
public class VentanaPrincipal extends JFrame {

    private final Usuario usuarioAutenticado;

    private JPanel panelMenu;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    private Estudiante estudianteAutenticado;

    /**
     * Crea la ventana principal para el usuario que inició sesión.
     *
     * @param usuarioAutenticado usuario que inició sesión en el sistema
     */
    public VentanaPrincipal(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;

        cargarEstudianteAutenticado();
        configurarVentana();
        crearComponentes();
    }

    // Métodos

    /**
     * Busca la información de estudiante asociada al usuario autenticado mediante su RUT.
     */
    private void cargarEstudianteAutenticado() {
        if (!usuarioAutenticado.esEstudiante()) {
            return;
        }

        EstudianteController estudianteController = new EstudianteController();

        try {
            estudianteAutenticado = estudianteController.buscarEstudiantePorRut(usuarioAutenticado.getRut());

            if (estudianteAutenticado == null) {
                JOptionPane.showMessageDialog(null, "No se encontró información de estudiante asociada al usuario autenticado.", "Estudiante no encontrado", JOptionPane.WARNING_MESSAGE);
            }

        } catch (AplicacionException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Configura las propiedades generales de la ventana principal.
     */
    private void configurarVentana() {
        setTitle("Biblioteca Escolar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
    }

    /**
     * Crea y organiza los componentes principales de la ventana.
     */
    private void crearComponentes() {
        setLayout(new BorderLayout());

        crearPanelContenido();
        crearMenu();

        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    /**
     * Crea el menú lateral de navegación.
     */
    private void crearMenu() {
        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblSistema = new JLabel("Biblioteca Escolar");
        lblSistema.setAlignmentX(Component.LEFT_ALIGNMENT);

        Persona personaMenu = usuarioAutenticado;

        if (usuarioAutenticado.esEstudiante() && estudianteAutenticado != null) {
            personaMenu = estudianteAutenticado;
        }

        JLabel lblUsuario = new JLabel(personaMenu.getNombre());
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(personaMenu.getDescripcion());
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelMenu.add(lblSistema);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(lblUsuario);
        panelMenu.add(Box.createVerticalStrut(5));
        panelMenu.add(lblDescripcion);
        panelMenu.add(Box.createVerticalStrut(30));

        if (usuarioAutenticado.esBibliotecario()) {
            agregarBotonMenu("Libros", "LIBROS");
            agregarBotonMenu("Estudiantes", "ESTUDIANTES");
            agregarBotonMenu("Préstamos y devoluciones", "PRESTAMOS");
            agregarBotonMenu("Reportes", "REPORTES");
        }

        if (usuarioAutenticado.esEstudiante() && estudianteAutenticado != null) {
            agregarBotonMenu("Préstamos y devoluciones", "PRESTAMOS");
            agregarBotonMenu("Consultas", "CONSULTAS");
        }

        panelMenu.add(Box.createVerticalGlue());

        JButton btnCerrarSesion = crearBotonMenu("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelMenu.add(btnCerrarSesion);
    }

    /**
     * Crea el área central que contendrá las diferentes vistas del sistema.
     */
    private void crearPanelContenido() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        JPanel panelInicio = new JPanel(new GridBagLayout());
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioAutenticado.getNombre());
        panelInicio.add(lblBienvenida);

        panelContenido.add(panelInicio, "INICIO");

        if (usuarioAutenticado.esBibliotecario()) {
            panelContenido.add(new PanelLibros(), "LIBROS");
            panelContenido.add(new PanelEstudiantes(), "ESTUDIANTES");
            panelContenido.add(new PanelPrestamos(), "PRESTAMOS");
            panelContenido.add(new PanelReportes(), "REPORTES");
        }

        if (usuarioAutenticado.esEstudiante() && estudianteAutenticado != null) {
            panelContenido.add(new PanelPrestamosEstudiante(estudianteAutenticado), "PRESTAMOS");
            panelContenido.add(new PanelConsultasEstudiante(estudianteAutenticado), "CONSULTAS");
        }

        cardLayout.show(panelContenido, "INICIO");
    }

    /**
     * Agrega un botón al menú lateral y lo conecta con una vista del CardLayout.
     *
     * @param texto texto que mostrará el botón
     * @param tarjeta nombre de la vista que mostrará el botón
     */
    private void agregarBotonMenu(String texto, String tarjeta) {
        JButton boton = crearBotonMenu(texto);

        boton.addActionListener(e -> cardLayout.show(panelContenido, tarjeta));

        panelMenu.add(boton);
        panelMenu.add(Box.createVerticalStrut(10));
    }

    /**
     * Crea un botón con el tamaño utilizado por el menú lateral.
     *
     * @param texto texto que mostrará el botón
     * @return botón configurado para el menú
     */
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);

        Dimension dimensionBoton = new Dimension(190, 40);

        boton.setPreferredSize(dimensionBoton);
        boton.setMinimumSize(dimensionBoton);
        boton.setMaximumSize(dimensionBoton);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);

        return boton;
    }

    /**
     * Cierra la sesión actual y regresa a la ventana de inicio de sesión.
     */
    private void cerrarSesion() {
        VentanaLogin ventanaLogin = new VentanaLogin();
        ventanaLogin.setVisible(true);

        dispose();
    }
}