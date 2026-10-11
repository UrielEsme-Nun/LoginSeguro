package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JPanel jPanel1;

    public VentanaLogin() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Sistema Seguro Azure");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        jPanel1 = new JPanel();
        jPanel1.setBackground(new Color(41, 128, 185));
        jPanel1.setBounds(0, 0, 400, 320);
        jPanel1.setLayout(null);

        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(50, 25, 300, 30);
        jPanel1.add(lblTitulo);

        JLabel lblSub = new JLabel("Ingrese sus credenciales de acceso", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(236, 240, 241));
        lblSub.setBounds(50, 60, 300, 20);
        jPanel1.add(lblSub);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(50, 105, 100, 25);
        jPanel1.add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(50, 130, 300, 30);
        jPanel1.add(txtUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(50, 170, 100, 25);
        jPanel1.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 195, 300, 30);
        txtPassword.addActionListener(e -> realizarLogin()); // Permite presionar Enter para ingresar
        jPanel1.add(txtPassword);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBounds(130, 245, 140, 35);
        btnIngresar.addActionListener(e -> realizarLogin());
        jPanel1.add(btnIngresar);

        add(jPanel1);
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // 1. Validaciones básicas de campos vacíos
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, complete todos los campos.", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Consulta al DAO de acceso (Conexión a Azure y validación SHA-256)
        dao.LoginDAO loginDao = new dao.LoginDAO();
        boolean accesoPermitido = loginDao.validarAcceso(usuario, password);

        if (accesoPermitido) {
            // Obtenemos el rol dinámico desde la BD
            String rol = loginDao.obtenerRolUsuario(usuario);
            
            // Abrimos el Menú principal enviando el usuario y su rol correspondiente
            VentanaMenu menu = new VentanaMenu(usuario, rol);
            menu.setVisible(true);
            
            // Cerramos la ventana de login actual
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos.", 
                "Error de Autenticación", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }

    public static void main(String args[]) {
        // Ejecutar la aplicación iniciando por el Login
        java.awt.EventQueue.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}