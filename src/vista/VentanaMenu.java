package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaMenu extends JFrame {

    private String rolUsuario;
    private String nombreUsuario;
    
    private JLabel lblBienvenido;
    private JLabel lblRolInfo;
    private JButton btnVerUsuarios;
    private JButton btnEditarUsuarios;
    private JButton btnCerrarSesion;
    private JPanel jPanel1;

    public VentanaMenu() {
        this("Invitado", "empleado");
    }

    public VentanaMenu(String usuario, String rol) {
        this.nombreUsuario = usuario;
        this.rolUsuario = rol != null ? rol.toLowerCase().trim() : "empleado";
        initComponents();
        aplicarPermisosPorRol();
    }

    private void initComponents() {
        setTitle("Menú Principal - Sistema de Gestión Seguro");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        jPanel1 = new JPanel();
        jPanel1.setBackground(new Color(44, 62, 80));
        jPanel1.setBounds(0, 0, 480, 380);
        jPanel1.setLayout(null);

        // Etiqueta de Bienvenida
        lblBienvenido = new JLabel("Bienvenido, " + nombreUsuario, SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenido.setForeground(Color.WHITE);
        lblBienvenido.setBounds(40, 25, 400, 30);
        jPanel1.add(lblBienvenido);

        // Etiqueta informativa del rol
        lblRolInfo = new JLabel("Rol actual: " + rolUsuario.toUpperCase(), SwingConstants.CENTER);
        lblRolInfo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRolInfo.setForeground(new Color(189, 195, 199));
        lblRolInfo.setBounds(40, 60, 400, 20);
        jPanel1.add(lblRolInfo);

        // Botón para ver el reporte / grid de usuarios (Disponible para todos, con restricciones AES según rol)
        btnVerUsuarios = new JButton("Ver / Filtrar Usuarios (Reporte)");
        btnVerUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVerUsuarios.setBounds(65, 110, 350, 40);
        btnVerUsuarios.addActionListener(e -> abrirVentanaVerUsuarios());
        jPanel1.add(btnVerUsuarios);

        // Botón para la gestión CRUD (Exclusivo de Administrador)
        btnEditarUsuarios = new JButton("Gestión de Usuarios - CRUD (Solo Admin)");
        btnEditarUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEditarUsuarios.setBounds(65, 170, 350, 40);
        btnEditarUsuarios.addActionListener(e -> abrirVentanaCRUD());
        jPanel1.add(btnEditarUsuarios);

        // Botón de Cerrar Sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBounds(65, 250, 350, 40);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        jPanel1.add(btnCerrarSesion);

        add(jPanel1);
    }

    private void aplicarPermisosPorRol() {
        // Validación estricta de seguridad en la UI según el rol obtenido de la BD
        if (!rolUsuario.equals("administrador")) {
            btnEditarUsuarios.setEnabled(false);
            btnEditarUsuarios.setBackground(new Color(127, 140, 141));
            btnEditarUsuarios.setToolTipText("Acceso denegado: Se requieren privilegios de Administrador.");
        }
    }

    private void abrirVentanaVerUsuarios() {
        // Pasamos el rol para que VentanaVerUsuarios sepa si debe enmascarar los datos AES o mostrarlos
        VentanaVerUsuarios ventanaVer = new VentanaVerUsuarios(this.rolUsuario);
        ventanaVer.setVisible(true);
    }

    private void abrirVentanaCRUD() {
        if (rolUsuario.equals("administrador")) {
            VentanaCRUDUsuarios ventanaCRUD = new VentanaCRUDUsuarios();
            ventanaCRUD.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No tienes permisos para acceder a la gestión CRUD.", 
                "Acceso Restringido", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro de que deseas cerrar sesión?", 
            "Cerrar Sesión", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
            this.dispose();
        }
    }
}