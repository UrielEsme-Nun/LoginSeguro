package dao;

import conexion.ConexionBaseDatos;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class UsuarioDAO {

    private static final String LLAVE_AES = "MiLlaveSecreta";

    // 1. Registrar usuario nuevo (con SHA-256 para password y AES_ENCRYPT para datos sensibles)
    public boolean registrarUsuario(String codigoId, String usuario, String password, String rol, 
                                    String nombre, String apellido, String correo, String fechaNac, 
                                    String genero, String depto, String puesto, 
                                    String telefono, String salario, String direccion) {
        
        String sql = "INSERT INTO usuarios ("
                + "codigo_identificacion, nombre_usuario, password_hash, rol, "
                + "primer_nombre, apellido, correo, fecha_nacimiento, genero, "
                + "departamento, puesto, telefono_cifrado, salario_cifrado, direccion_cifrada, estatus"
                + ") VALUES (?, ?, SHA2(?, 256), ?, ?, ?, ?, ?, ?, ?, ?, AES_ENCRYPT(?, ?), AES_ENCRYPT(?, ?), AES_ENCRYPT(?, ?), 'Activo')";

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, codigoId);
            pst.setString(2, usuario);
            pst.setString(3, password);
            pst.setString(4, rol);
            pst.setString(5, nombre);
            pst.setString(6, apellido);
            pst.setString(7, correo);
            pst.setString(8, fechaNac);
            pst.setString(9, genero);
            pst.setString(10, depto);
            pst.setString(11, puesto);
            pst.setString(12, telefono);
            pst.setString(13, LLAVE_AES);
            pst.setString(14, salario);
            pst.setString(15, LLAVE_AES);
            pst.setString(16, direccion);
            pst.setString(17, LLAVE_AES);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar en Azure: " + e.getMessage());
            return false;
        }
    }

    // 2. Obtener usuarios para la tabla (descifra AES si es admin, enmascara si es empleado)
    public Vector<Object[]> obtenerUsuarios(String rolUsuario, String filtroCodigo) {
        Vector<Object[]> lista = new Vector<>();
        boolean esAdmin = "administrador".equalsIgnoreCase(rolUsuario);

        String sql;
        if (esAdmin) {
            sql = "SELECT id, codigo_identificacion, nombre_usuario, rol, primer_nombre, apellido, correo, "
                + "genero, departamento, puesto, estatus, "
                + "CAST(AES_DECRYPT(telefono_cifrado, ?) AS CHAR) AS tel, "
                + "CAST(AES_DECRYPT(salario_cifrado, ?) AS CHAR) AS sal, "
                + "CAST(AES_DECRYPT(direccion_cifrada, ?) AS CHAR) AS dir "
                + "FROM usuarios";
        } else {
            sql = "SELECT id, codigo_identificacion, nombre_usuario, rol, primer_nombre, apellido, correo, "
                + "genero, departamento, puesto, estatus, "
                + "'********' AS tel, '********' AS sal, '********' AS dir "
                + "FROM usuarios";
        }

        if (filtroCodigo != null && !filtroCodigo.trim().isEmpty()) {
            sql += " WHERE codigo_identificacion = ?";
        }

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            if (esAdmin) {
                pst.setString(1, LLAVE_AES);
                pst.setString(2, LLAVE_AES);
                pst.setString(3, LLAVE_AES);
                if (filtroCodigo != null && !filtroCodigo.trim().isEmpty()) {
                    pst.setString(4, filtroCodigo.trim());
                }
            } else {
                if (filtroCodigo != null && !filtroCodigo.trim().isEmpty()) {
                    pst.setString(1, filtroCodigo.trim());
                }
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[14];
                    fila[0] = rs.getInt("id");
                    fila[1] = rs.getString("codigo_identificacion");
                    fila[2] = rs.getString("nombre_usuario");
                    fila[3] = rs.getString("rol");
                    fila[4] = rs.getString("primer_nombre");
                    fila[5] = rs.getString("apellido");
                    fila[6] = rs.getString("correo");
                    fila[7] = rs.getString("genero");
                    fila[8] = rs.getString("departamento");
                    fila[9] = rs.getString("puesto");
                    fila[10] = rs.getString("estatus");
                    fila[11] = rs.getString("tel");
                    fila[12] = rs.getString("sal");
                    fila[13] = rs.getString("dir");
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // 3. Buscar usuario por Código ID para precargar el formulario de modificación
    public Map<String, String> obtenerUsuarioPorCodigo(String codigoId) {
        Map<String, String> datos = new HashMap<>();
        String sql = "SELECT nombre_usuario, rol, primer_nombre, apellido, correo, "
                   + "DATE_FORMAT(fecha_nacimiento, '%Y-%m-%d') as fnac, genero, departamento, puesto, "
                   + "CAST(AES_DECRYPT(telefono_cifrado, ?) AS CHAR) AS tel, "
                   + "CAST(AES_DECRYPT(salario_cifrado, ?) AS CHAR) AS sal, "
                   + "CAST(AES_DECRYPT(direccion_cifrada, ?) AS CHAR) AS dir "
                   + "FROM usuarios WHERE codigo_identificacion = ?";

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, LLAVE_AES);
            pst.setString(2, LLAVE_AES);
            pst.setString(3, LLAVE_AES);
            pst.setString(4, codigoId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    datos.put("nombre_usuario", rs.getString("nombre_usuario"));
                    datos.put("rol", rs.getString("rol"));
                    datos.put("primer_nombre", rs.getString("primer_nombre"));
                    datos.put("apellido", rs.getString("apellido"));
                    datos.put("correo", rs.getString("correo"));
                    datos.put("fecha_nacimiento", rs.getString("fnac"));
                    datos.put("genero", rs.getString("genero"));
                    datos.put("departamento", rs.getString("departamento"));
                    datos.put("puesto", rs.getString("puesto"));
                    datos.put("telefono", rs.getString("tel"));
                    datos.put("salario", rs.getString("sal"));
                    datos.put("direccion", rs.getString("dir"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario para modificar: " + e.getMessage());
        }
        return datos;
    }

    // 4. Actualizar usuario existente por Código ID
    public boolean actualizarUsuario(String codigoId, String usuario, String rol, 
                                     String nombre, String apellido, String correo, String fechaNac, 
                                     String genero, String depto, String puesto, 
                                     String telefono, String salario, String direccion) {
        
        String sql = "UPDATE usuarios SET nombre_usuario = ?, rol = ?, primer_nombre = ?, apellido = ?, "
                + "correo = ?, fecha_nacimiento = ?, genero = ?, departamento = ?, puesto = ?, "
                + "telefono_cifrado = AES_ENCRYPT(?, ?), salario_cifrado = AES_ENCRYPT(?, ?), "
                + "direccion_cifrada = AES_ENCRYPT(?, ?) WHERE codigo_identificacion = ?";

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, usuario);
            pst.setString(2, rol);
            pst.setString(3, nombre);
            pst.setString(4, apellido);
            pst.setString(5, correo);
            pst.setString(6, fechaNac);
            pst.setString(7, genero);
            pst.setString(8, depto);
            pst.setString(9, puesto);
            pst.setString(10, telefono);
            pst.setString(11, LLAVE_AES);
            pst.setString(12, salario);
            pst.setString(13, LLAVE_AES);
            pst.setString(14, direccion);
            pst.setString(15, LLAVE_AES);
            pst.setString(16, codigoId);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // 5. Eliminar usuario por Código ID
    public boolean eliminarUsuario(String codigoId) {
        String sql = "DELETE FROM usuarios WHERE codigo_identificacion = ?";
        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, codigoId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}