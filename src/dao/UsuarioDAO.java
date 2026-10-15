package dao;

import conexion.ConexionBaseDatos;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class UsuarioDAO {

    private static final String LLAVE_AES = "MiLlaveSecreta";

    // 1. Registrar usuario nuevo con todos los campos y cifrado AES/SHA-2
    public boolean registrarUsuarioCompleto(String codigoId, String usuario, String password, String rol, 
                                            String nombre, String apellido, String correo, String acercaDe, 
                                            String telefono, String salario, String direccion, 
                                            String fechaNac, String genero, String depto, String puesto, String estatus) {
        
        String sql = "INSERT INTO usuarios ("
                + "codigo_identificacion, nombre_usuario, password_hash, rol, "
                + "primer_nombre, apellido, correo, acerca_de, "
                + "telefono_cifrado, salario_cifrado, direccion_cifrada, "
                + "fecha_nacimiento, genero, departamento, puesto, estatus"
                + ") VALUES (?, ?, SHA2(?, 256), ?, ?, ?, ?, ?, AES_ENCRYPT(?, ?), AES_ENCRYPT(?, ?), AES_ENCRYPT(?, ?), ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, codigoId);
            pst.setString(2, usuario);
            pst.setString(3, password);
            pst.setString(4, rol);
            pst.setString(5, nombre);
            pst.setString(6, apellido);
            pst.setString(7, correo);
            pst.setString(8, acercaDe);
            pst.setString(9, telefono);
            pst.setString(10, LLAVE_AES);
            pst.setString(11, salario);
            pst.setString(12, LLAVE_AES);
            pst.setString(13, direccion);
            pst.setString(14, LLAVE_AES);
            pst.setString(15, fechaNac);
            pst.setString(16, genero);
            pst.setString(17, depto);
            pst.setString(18, puesto);
            pst.setString(19, estatus);

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
                + "acerca_de, genero, departamento, puesto, estatus, "
                + "CAST(AES_DECRYPT(telefono_cifrado, ?) AS CHAR) AS tel, "
                + "CAST(AES_DECRYPT(salario_cifrado, ?) AS CHAR) AS sal, "
                + "CAST(AES_DECRYPT(direccion_cifrada, ?) AS CHAR) AS dir "
                + "FROM usuarios";
        } else {
            sql = "SELECT id, codigo_identificacion, nombre_usuario, rol, primer_nombre, apellido, correo, "
                + "acerca_de, genero, departamento, puesto, estatus, "
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
                    Object[] fila = new Object[15];
                    fila[0] = rs.getInt("id");
                    fila[1] = rs.getString("codigo_identificacion");
                    fila[2] = rs.getString("nombre_usuario");
                    fila[3] = rs.getString("rol");
                    fila[4] = rs.getString("primer_nombre");
                    fila[5] = rs.getString("apellido");
                    fila[6] = rs.getString("correo");
                    fila[7] = rs.getString("acerca_de");
                    fila[8] = rs.getString("genero");
                    fila[9] = rs.getString("departamento");
                    fila[10] = rs.getString("puesto");
                    fila[11] = rs.getString("estatus");
                    fila[12] = rs.getString("tel");
                    fila[13] = rs.getString("sal");
                    fila[14] = rs.getString("dir");
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // 3. Buscar usuario por Código ID para precargar el formulario de modificación
    public Map<String, String> obtenerUsuarioPorCodigoCompleto(String codigoId) {
        Map<String, String> datos = new HashMap<>();
        String sql = "SELECT nombre_usuario, rol, primer_nombre, apellido, correo, acerca_de, "
                   + "DATE_FORMAT(fecha_nacimiento, '%Y-%m-%d') as fnac, genero, departamento, puesto, estatus, "
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
                    datos.put("acerca_de", rs.getString("acerca_de"));
                    datos.put("fecha_nacimiento", rs.getString("fnac"));
                    datos.put("genero", rs.getString("genero"));
                    datos.put("departamento", rs.getString("departamento"));
                    datos.put("puesto", rs.getString("puesto"));
                    datos.put("estatus", rs.getString("estatus"));
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
    public boolean actualizarUsuarioCompleto(String codigoId, String usuario, String rol, 
                                             String nombre, String apellido, String correo, String acercaDe, 
                                             String telefono, String salario, String direccion, 
                                             String fechaNac, String genero, String depto, String puesto, String estatus) {
        
        String sql = "UPDATE usuarios SET nombre_usuario = ?, rol = ?, primer_nombre = ?, apellido = ?, "
                + "correo = ?, acerca_de = ?, fecha_nacimiento = ?, genero = ?, departamento = ?, puesto = ?, estatus = ?, "
                + "telefono_cifrado = AES_ENCRYPT(?, ?), salario_cifrado = AES_ENCRYPT(?, ?), "
                + "direccion_cifrada = AES_ENCRYPT(?, ?) WHERE codigo_identificacion = ?";

        try (Connection con = ConexionBaseDatos.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, usuario);
            pst.setString(2, rol);
            pst.setString(3, nombre);
            pst.setString(4, apellido);
            pst.setString(5, correo);
            pst.setString(6, acercaDe);
            pst.setString(7, fechaNac);
            pst.setString(8, genero);
            pst.setString(9, depto);
            pst.setString(10, puesto);
            pst.setString(11, estatus);
            pst.setString(12, telefono);
            pst.setString(13, LLAVE_AES);
            pst.setString(14, salario);
            pst.setString(15, LLAVE_AES);
            pst.setString(16, direccion);
            pst.setString(17, LLAVE_AES);
            pst.setString(18, codigoId);

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