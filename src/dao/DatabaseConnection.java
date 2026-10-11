package dao;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;

/**
 * Gestiona las conexiones con la base de datos biblioteca.
 *
 * Implementa el patrón Singleton para garantizar una única instancia de
 * DatabaseConnection durante la ejecución. Los parámetros de conexión se
 * obtienen desde el archivo externo config.properties.
 */
public class DatabaseConnection {

    private static final String CONFIG_FILE = "config.properties";
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();

    /**
     * Constructor privado que impide la creación de instancias adicionales.
     */
    private DatabaseConnection() {
    }

    // Métodos

    /**
     * Devuelve la única instancia de DatabaseConnection.
     *
     * @return instancia única de DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    /**
     * Establece una conexión con la base de datos utilizando la configuración externa.
     *
     * @return conexión activa con MySQL
     * @throws SQLException si ocurre un error de conexión o configuración
     */
    public Connection getConnection() throws SQLException {
        Properties propiedades = cargarConfiguracion();

        String url = propiedades.getProperty("db.url");
        String user = propiedades.getProperty("db.user");
        String password = propiedades.getProperty("db.password");

        if (url == null || url.isBlank()) {
            throw new SQLException("No se ha configurado la URL de la base de datos.");
        }

        if (user == null || user.isBlank()) {
            throw new SQLException("No se ha configurado el usuario de la base de datos.");
        }

        if (password == null) {
            throw new SQLException("No se ha configurado la contraseña de la base de datos.");
        }

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Carga los parámetros de conexión desde config.properties.
     *
     * @return propiedades de configuración
     * @throws SQLException si el archivo no existe o no puede ser leído
     */
    private Properties cargarConfiguracion() throws SQLException {
        Path rutaConfiguracion = buscarArchivoConfiguracion();

        if (rutaConfiguracion == null) {
            throw new SQLException("No se encontró el archivo " + CONFIG_FILE + ". Debe estar en la carpeta del proyecto o junto al archivo JAR.");
        }

        Properties propiedades = new Properties();

        try (InputStream entrada = Files.newInputStream(rutaConfiguracion)) {
            propiedades.load(entrada);
            return propiedades;

        } catch (IOException ex) {
            throw new SQLException("No fue posible leer el archivo " + CONFIG_FILE + ".", ex);
        }
    }

    /**
     * Busca config.properties en ubicaciones compatibles con NetBeans y con la ejecución desde JAR.
     *
     * @return ruta encontrada o null si el archivo no existe
     */
    private Path buscarArchivoConfiguracion() {
        Path rutaActual = Paths.get(CONFIG_FILE).toAbsolutePath();

        if (Files.exists(rutaActual)) {
            return rutaActual;
        }

        try {
            Path ubicacionAplicacion = Paths.get(DatabaseConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath();

            if (Files.isRegularFile(ubicacionAplicacion)) {
                Path rutaJuntoAlJar = ubicacionAplicacion.getParent().resolve(CONFIG_FILE);

                if (Files.exists(rutaJuntoAlJar)) {
                    return rutaJuntoAlJar;
                }
            }

            if (Files.isDirectory(ubicacionAplicacion)) {
                Path directorio = ubicacionAplicacion;

                for (int i = 0; i < 4 && directorio != null; i++) {
                    Path rutaCandidata = directorio.resolve(CONFIG_FILE);

                    if (Files.exists(rutaCandidata)) {
                        return rutaCandidata;
                    }

                    directorio = directorio.getParent();
                }
            }

        } catch (URISyntaxException ex) {
            return null;
        }

        return null;
    }
}