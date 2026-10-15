package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class FormularioAgregarUsuario extends JFrame {

    private JTextField txtCodigoId, txtUsuario, txtNombre, txtApellido, txtCorreo, txtFechaNac, txtGenero, txtDepto, txtPuesto;
    private JPasswordField txtPassword;
    private JTextField txtTelefono, txtSalario, txtDireccion, txtAcercaDe;
    private JComboBox<String> cmbRol, cmbEstatus;
    private JButton btnGuardar, btnCancelar;

    public FormularioAgregarUsuario(JFrame parent) {
        initComponents();
    }

    private void initComponents() {
        setTitle("Registrar Nuevo Usuario - Sistema Seguro");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Principal con diseño limpio y moderno
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título Superior
        JLabel lblTitulo = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1; // Restaurar ancho
        int fila = 1;

        // Campos del formulario
        txtCodigoId = agregarCampoFila(panelPrincipal, gbc, fila++, "Código ID (Máx. 50):", 50);
        txtUsuario = agregarCampoFila(panelPrincipal, gbc, fila++, "Nombre de Usuario (Máx. 50):", 50);
        
        // Contraseña
        gbc.gridx = 0; gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panelPrincipal.add(txtPassword, gbc);
        fila++;

        // Rol (JComboBox para evitar incongruencias)
        gbc.gridx = 0; gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Rol del Sistema:"), gbc);
        cmbRol = new JComboBox<>(new String[]{"empleado", "administrador"});
        gbc.gridx = 1;
        panelPrincipal.add(cmbRol, gbc);
        fila++;

        txtNombre = agregarCampoFila(panelPrincipal, gbc, fila++, "Primer Nombre:", 50);
        txtApellido = agregarCampoFila(panelPrincipal, gbc, fila++, "Apellido:", 50);
        txtCorreo = agregarCampoFila(panelPrincipal, gbc, fila++, "Correo Electrónico:", 100);
        txtFechaNac = agregarCampoFila(panelPrincipal, gbc, fila++, "F. Nacimiento (YYYY-MM-DD):", 10);
        txtGenero = agregarCampoFila(panelPrincipal, gbc, fila++, "Género:", 20);
        txtDepto = agregarCampoFila(panelPrincipal, gbc, fila++, "Departamento:", 50);
        txtPuesto = agregarCampoFila(panelPrincipal, gbc, fila++, "Puesto:", 50);
        
        // Datos Sensibles (AES)
        txtTelefono = agregarCampoFila(panelPrincipal, gbc, fila++, "Teléfono (Solo números):", 20);
        txtSalario = agregarCampoFila(panelPrincipal, gbc, fila++, "Salario (Ej. 15000.00):", 20);
        txtDireccion = agregarCampoFila(panelPrincipal, gbc, fila++, "Dirección:", 255);
        txtAcercaDe = agregarCampoFila(panelPrincipal, gbc, fila++, "Acerca de (Opcional):", 255);

        // Estatus
        gbc.gridx = 0; gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Estatus:"), gbc);
        cmbEstatus = new JComboBox<>(new String[]{"Activo", "Inactivo", "Suspendido"});
        gbc.gridx = 1;
        panelPrincipal.add(cmbEstatus, gbc);
        fila++;

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel de Botones Inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(new Color(230, 235, 240));

        btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setPreferredSize(new Dimension(150, 35));
        btnGuardar.addActionListener(e -> validarYGuardar());
        panelBotones.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(110, 35));
        btnCancelar.addActionListener(e -> this.dispose());
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JTextField agregarCampoFila(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, int columnas) {
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        JTextField txt = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txt, gbc);
        return txt;
    }

    private void validarYGuardar() {
        String codigo = txtCodigoId.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();
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
        String acercaDe = txtAcercaDe.getText().trim();
        String estatus = (String) cmbEstatus.getSelectedItem();

        // VALIDACIONES ESTRICTAS DE INTEGRIDAD
        if (codigo.isEmpty() || usuario.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() || salario.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (codigo.length() > 50 || usuario.length() > 50) {
            JOptionPane.showMessageDialog(this, "El Código ID y el Usuario no pueden exceder los 50 caracteres.", "Error de longitud", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación de formato de correo electrónico
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", correo)) {
            JOptionPane.showMessageDialog(this, "El formato del correo electrónico no es válido.", "Validación de Correo", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return;
        }

        // Validación de formato de fecha YYYY-MM-DD
        if (!fechaNac.isEmpty() && !Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", fechaNac)) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe tener el formato estricto: YYYY-MM-DD", "Formato de Fecha Inválido", JOptionPane.WARNING_MESSAGE);
            txtFechaNac.requestFocus();
            return;
        }

        // Validación de Teléfono (Solo números y longitud adecuada)
        if (!Pattern.matches("^\\d+$", telefono)) {
            JOptionPane.showMessageDialog(this, "El teléfono solo debe contener números.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return;
        }

        // Validación de Salario (Numérico decimal)
        try {
            Double.parseDouble(salario);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un valor numérico válido (ej. 12500.50).", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
            txtSalario.requestFocus();
            return;
        }

        // Invocación al DAO real
        UsuarioDAO usuarioDao = new UsuarioDAO();
        boolean exito = usuarioDao.registrarUsuarioCompleto(codigo, usuario, password, rol, nombre, apellido, 
                                                             correo, acercaDe, telefono, salario, direccion, 
                                                             fechaNac, genero, depto, puesto, estatus);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado y protegido con éxito en Azure MySQL!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. Verifique que el Código ID o Usuario no estén duplicados.", "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}