package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaMenu extends JFrame {

    private String usuarioActual;
    private String rolUsuario;
    private JButton btnVerUsuarios, btnCrudUsuarios, btnCerrarSesion;

    public VentanaMenu(String usuario, String rol) {
        this.usuarioActual = usuario;
        this.rolUsuario = rol != null ? rol.toLowerCase().trim() : "empleado";
        initComponents();
    }

    private void initComponents() {
        setTitle("Menú Principal - Sistema de Gestión Seguro");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal con el estilo corporativo oscuro
        JPanel panel = new JPanel();
        panel.setBackground(new Color(44, 62, 80));
        panel.setLayout(null);

        // Título de bienvenida
        JLabel lblBienvenido = new JLabel("Bienvenido, " + usuarioActual, SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBienvenido.setForeground(Color.WHITE);
        lblBienvenido.setBounds(40, 30, 400, 30);
        panel.add(lblBienvenido);

        JLabel lblRol = new JLabel("Rol actual: " + rolUsuario.toUpperCase(), SwingConstants.CENTER);
        lblRol.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRol.setForeground(new Color(189, 195, 199));
        lblRol.setBounds(40, 65, 400, 20);
        panel.add(lblRol);

        // Botón Ver / Filtrar Usuarios
        btnVerUsuarios = new JButton("Ver Usuarios");
        btnVerUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVerUsuarios.setBounds(50, 120, 380, 45);
        btnVerUsuarios.setFocusPainted(false);
        btnVerUsuarios.addActionListener(e -> abrirVentanaReporte());
        panel.add(btnVerUsuarios);

        // Botón Gestión de Usuarios (CRUD) - Restringido solo para Administradores
        btnCrudUsuarios = new JButton("Gestión de Usuarioss");
        btnCrudUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCrudUsuarios.setBounds(50, 185, 380, 45);
        btnCrudUsuarios.setFocusPainted(false);
        btnCrudUsuarios.addActionListener(e -> abrirVentanaCRUD());
        
        if (!rolUsuario.equals("administrador")) {
            btnCrudUsuarios.setEnabled(false);
            btnCrudUsuarios.setToolTipText("Acceso restringido solo a administradores");
        }
        panel.add(btnCrudUsuarios);

        // Botón Cerrar Sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBounds(50, 275, 380, 40);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panel.add(btnCerrarSesion);

        add(panel, BorderLayout.CENTER);
    }

    private void abrirVentanaReporte() {
        VentanaVerUsuarios verUsuarios = new VentanaVerUsuarios(rolUsuario);
        verUsuarios.setVisible(true);
    }

    private void abrirVentanaCRUD() {
        if (rolUsuario.equals("administrador")) {
            VentanaCRUDUsuarios crud = new VentanaCRUDUsuarios();
            crud.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No cuenta con privilegios de administrador.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cerrarSesion() {
        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
        this.dispose();
    }
}