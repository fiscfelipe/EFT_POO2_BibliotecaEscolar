package UI;

import controller.UsuarioController;
import java.awt.*;
import javax.swing.*;
import model.Usuario;
import util.AplicacionException;

/**
 * Representa la ventana de autenticación del sistema de biblioteca.
 */
public class VentanaLogin extends JFrame {

    private final UsuarioController usuarioController;

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;

    /**
     * Crea la ventana de inicio de sesión.
     */
    public VentanaLogin() {
        this.usuarioController = new UsuarioController();

        configurarVentana();
        crearComponentes();
    }

    // Métodos

    /**
     * Configura las propiedades generales de la ventana de inicio de sesión.
     */
    private void configurarVentana() {
        setTitle("Biblioteca Escolar - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 320);
        setMinimumSize(new Dimension(450, 320));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Crea y organiza los componentes de la ventana.
     */
    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 1.0;
        restricciones.gridx = 0;

        JLabel lblTitulo = new JLabel("Biblioteca Escolar");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));

        JLabel lblCorreo = new JLabel("Correo:");
        txtCorreo = new JTextField();

        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField();

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setPreferredSize(new Dimension(150, 40));

        restricciones.gridy = 0;
        restricciones.insets = new Insets(0, 0, 25, 0);
        panelPrincipal.add(lblTitulo, restricciones);

        restricciones.gridy = 1;
        restricciones.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(lblCorreo, restricciones);

        restricciones.gridy = 2;
        restricciones.insets = new Insets(0, 0, 15, 0);
        panelPrincipal.add(txtCorreo, restricciones);

        restricciones.gridy = 3;
        restricciones.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(lblContrasena, restricciones);

        restricciones.gridy = 4;
        restricciones.insets = new Insets(0, 0, 25, 0);
        panelPrincipal.add(txtContrasena, restricciones);

        restricciones.gridy = 5;
        restricciones.insets = new Insets(0, 0, 0, 0);
        panelPrincipal.add(btnIngresar, restricciones);

        add(panelPrincipal);

        btnIngresar.addActionListener(e -> iniciarSesion());

        getRootPane().setDefaultButton(btnIngresar);
    }

    /**
     * Intenta autenticar al usuario con las credenciales ingresadas.
     */
    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        try {
            Usuario usuario = usuarioController.autenticarUsuario(correo, contrasena);

            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(usuario);
            ventanaPrincipal.setVisible(true);

            dispose();

        } catch (AplicacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No fue posible iniciar sesión", JOptionPane.WARNING_MESSAGE);
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }
}