package vista;

import javax.swing.*;
import java.awt.*;

public class FormularioAgregarUsuario extends JFrame {

    private JTextField txtCodigoId, txtUsuario, txtRol, txtNombre, txtApellido, txtCorreo, txtFechaNac, txtGenero, txtDepto, txtPuesto;
    private JPasswordField txtPassword;
    private JTextField txtTelefono, txtSalario, txtDireccion; // Datos que irán cifrados con AES_ENCRYPT
    private JButton btnGuardar, btnCancelar;

    public FormularioAgregarUsuario(JFrame parent) {
        initComponents();
    }

    private void initComponents() {
        setTitle("Registrar Nuevo Usuario - Azure MySQL");
        setSize(520, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Ingrese los datos del nuevo usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(40, 15, 420, 30);
        add(lblTitulo);

        int y = 60;
        int salto = 38;

        txtCodigoId = agregarCampo("Código ID Único:", y);
        y += salto;
        txtUsuario = agregarCampo("Nombre de Usuario:", y);
        y += salto;
        
        // Campo Contraseña (SHA-2)
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(40, y, 160, 25);
        add(lblPass);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(210, y, 250, 25);
        add(txtPassword);
        y += salto;

        txtRol = agregarCampo("Rol (administrador/empleado):", y);
        y += salto;
        txtNombre = agregarCampo("Primer Nombre:", y);
        y += salto;
        txtApellido = agregarCampo("Apellido:", y);
        y += salto;
        txtCorreo = agregarCampo("Correo Electrónico:", y);
        y += salto;
        txtFechaNac = agregarCampo("F. Nacimiento (YYYY-MM-DD):", y);
        y += salto;
        txtGenero = agregarCampo("Género:", y);
        y += salto;
        txtDepto = agregarCampo("Departamento:", y);
        y += salto;
        txtPuesto = agregarCampo("Puesto:", y);
        y += salto;
        
        // Datos Sensibles (Cifrados con AES_ENCRYPT)
        txtTelefono = agregarCampo("Teléfono (Cifrar AES):", y);
        y += salto;
        txtSalario = agregarCampo("Salario (Cifrar AES):", y);
        y += salto;
        txtDireccion = agregarCampo("Dirección (Cifrar AES):", y);

        // Botones
        y += 50;
        btnGuardar = new JButton("Guardar en BD");
        btnGuardar.setBounds(120, y, 130, 35);
        btnGuardar.addActionListener(e -> guardarUsuarioBD());
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(270, y, 110, 35);
        btnCancelar.addActionListener(e -> this.dispose());
        add(btnCancelar);
    }

    private JTextField agregarCampo(String etiqueta, int y) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(40, y, 160, 25);
        add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(210, y, 250, 25);
        add(txt);
        return txt;
    }

    private void guardarUsuarioBD() {
        String codigo = txtCodigoId.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String rol = txtRol.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String fechaNac = txtFechaNac.getText().trim();
        String genero = txtGenero.getText().trim();
        String depto = txtDepto.getText().trim();
        String puesto = txtPuesto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String salario = txtSalario.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (codigo.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos Código ID, Usuario y Contraseña son obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Invocación al DAO real para insertar en la base de datos de Azure
        dao.UsuarioDAO usuarioDao = new dao.UsuarioDAO();
        boolean exito = usuarioDao.registrarUsuario(codigo, usuario, password, rol, nombre, apellido, 
                                                   correo, fechaNac, genero, depto, puesto, 
                                                   telefono, salario, direccion);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado y cifrado correctamente en Azure MySQL!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario. Verifique que el Código ID o el Usuario no estén repetidos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}