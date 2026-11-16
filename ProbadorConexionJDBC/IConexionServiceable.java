import java.sql.SQLException;

public interface IConexionServiceable {
    String validarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException;
}