package app;

import UI.VentanaLogin;
import javax.swing.SwingUtilities;

/**
 * Inicia la aplicación de Biblioteca Escolar.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaLogin ventanaLogin = new VentanaLogin();
            ventanaLogin.setVisible(true);
        });
    }
}