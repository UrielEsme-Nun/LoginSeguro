package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    
    // Configura aquí los datos de la base de datos en Azure MySQL
    private static final String URL = "jdbc:mysql://urieldb.mysql.database.azure.com:3306/login_seguro?useSSL=true&serverTimezone=UTC";
    private static final String USUARIO = "adminuriel";
    private static final String CONTRASENA = "Root1234";

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Cargar explícitamente el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("¡Conexión exitosa a la base de datos en Azure!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL (JDBC): " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de SQL al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}