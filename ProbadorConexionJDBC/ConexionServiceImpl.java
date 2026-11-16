import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionServiceImpl implements IConexionServiceable {

    @Override
    public String validarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException {

        if (url == null || url.trim().isEmpty()) {
            throw new SQLException("La URL de conexión no puede estar vacía.");
        }

        if (url.startsWith("jdbc:mysql:")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } else if (url.startsWith("jdbc:sqlite:")) {
            Class.forName("org.sqlite.JDBC");
        } else if (url.startsWith("jdbc:postgresql:")) {
            Class.forName("org.postgresql.Driver");
        }

        try (Connection conn = obtenerConexion(url.trim(), usuario, contrasena)) {
            if (conn != null && !conn.isClosed()) {
                return "Conexión exitosa.\n"
                        + "Base de datos: " + conn.getMetaData().getDatabaseProductName() + "\n"
                        + "La conexión se cerró correctamente.";
            }
            throw new SQLException("No se pudo establecer la conexión.");
        }
    }

    private Connection obtenerConexion(String url, String usuario, String contrasena) throws SQLException {
        if ((usuario == null || usuario.trim().isEmpty()) && (contrasena == null || contrasena.isEmpty())) {
            return DriverManager.getConnection(url);
        } else {
            return DriverManager.getConnection(url, usuario.trim(), contrasena);
        }
    }
}