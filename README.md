# Biblioteca Escolar

Aplicación de escritorio desarrollada en Java para la gestión de una biblioteca escolar.

Permite autenticar usuarios según su rol, gestionar libros y estudiantes, registrar préstamos y devoluciones, controlar el stock y consultar reportes.

## Tecnologías utilizadas

- Java JDK 23
- Java Swing
- MySQL
- JDBC
- MySQL Connector/J
- NetBeans
- Patrón MVC
- Patrón DAO
- Patrón Singleton

## Estructura principal

```text
src/
├── UI/
│   ├── PanelConsultasEstudiante.java
│   ├── PanelEstudiantes.java
│   ├── PanelLibros.java
│   ├── PanelPrestamos.java
│   ├── PanelPrestamosEstudiante.java
│   ├── PanelReportes.java
│   ├── VentanaLogin.java
│   └── VentanaPrincipal.java
├── app/
│   └── Main.java
├── controller/
│   ├── CategoriaController.java
│   ├── EstudianteController.java
│   ├── LibroController.java
│   ├── PrestamoController.java
│   ├── ReporteController.java
│   └── UsuarioController.java
├── dao/
│   ├── CategoriaDAO.java
│   ├── CrudDAO.java
│   ├── DatabaseConnection.java
│   ├── EstudianteDAO.java
│   ├── LibroDAO.java
│   ├── PrestamoDAO.java
│   ├── ReporteDAO.java
│   └── UsuarioDAO.java
├── model/
│   ├── Categoria.java
│   ├── Estudiante.java
│   ├── Libro.java
│   ├── LibroPrestadoResumen.java
│   ├── Persona.java
│   ├── Prestamo.java
│   ├── ResultadoDevolucion.java
│   └── Usuario.java
└── util/
    ├── AplicacionException.java
    ├── ErrorBaseDatosUtil.java
    └── RegistroNoEncontradoException.java

lib/
sql/
nbproject/
build.xml
manifest.mf
config.properties
```

## Base de datos

El proyecto utiliza una base de datos MySQL llamada:

```text
biblioteca
```

Los scripts necesarios se encuentran en la carpeta `sql/`:

```text
PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql
PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql
```

Ejecutar primero el script de creación y luego el script de poblamiento.

## Configuración de la conexión

La conexión se configura mediante el archivo externo:

```text
config.properties
```

Ejemplo:

```properties
db.url=jdbc:mysql://localhost:3306/biblioteca
db.user=root
db.password=
```

Completar `db.password` con la contraseña local de MySQL antes de ejecutar la aplicación.

## Ejecución desde NetBeans

1. Abrir el proyecto en NetBeans.
2. Verificar que el proyecto utilice JDK 23.
3. Confirmar que `app.Main` esté configurada como clase principal.
4. Verificar que MySQL esté iniciado y que la base `biblioteca` exista.
5. Completar `config.properties`.
6. Ejecutar el proyecto.

## Generación del archivo ejecutable

El archivo ejecutable se genera dentro de `dist/`.

Para ejecutarlo fuera de NetBeans, mantener juntos:

```text
BibliotecaEscolar.jar
config.properties
lib/
```

Luego ejecutar:

```bash
java -jar BibliotecaEscolar.jar
```

El equipo debe disponer de Java 23 o una versión compatible superior.

## Funcionalidades

### Bibliotecario

- Gestión de libros.
- Gestión de estudiantes.
- Registro de préstamos.
- Registro de devoluciones.
- Control automático de stock.
- Visualización de reportes.

### Estudiante

- Registro y devolución de sus propios préstamos.
- Consulta de su historial.
- Acceso restringido según rol.

## Diseño y arquitectura

El proyecto utiliza una separación por capas:

```text
UI
↓
Controller
↓
DAO
↓
MySQL
```

También incorpora:

- `Persona` como clase abstracta.
- Herencia mediante `Usuario` y `Estudiante`.
- Polimorfismo mediante métodos sobrescritos como `getDescripcion()`.
- Interfaz genérica `CrudDAO<T>`.
- Singleton en `DatabaseConnection`.
- `SwingWorker` para ejecutar préstamos en segundo plano.
- Métodos `synchronized` y transacciones con `SELECT ... FOR UPDATE` para mantener la consistencia del stock.

## Consideraciones

- El sistema requiere MySQL en ejecución.
- El archivo `config.properties` debe configurarse según el entorno local.
- El proyecto fue desarrollado y probado con JDK 23.
