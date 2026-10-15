package dao;

import conexion.ConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public boolean validarAcceso(String nombreUsuario, String passwordPlana) {
        String sql = "SELECT rol FROM usuarios WHERE nombre_usuario = ? AND password_hash = SHA2(?, 256)";

        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, nombreUsuario);
            sentencia.setString(2, passwordPlana);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return true; // Credenciales correctas
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la validación del login: " + e.getMessage());
        }
        return false;
    }
    
    public String obtenerRolUsuario(String nombreUsuario) {
        String sql = "SELECT rol FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, nombreUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getString("rol");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el rol: " + e.getMessage());
        }
        return "";
    }
}