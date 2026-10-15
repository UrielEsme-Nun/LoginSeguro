package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.regex.Pattern;

public class FormularioModificarUsuario extends JFrame {

    private String codigoIdObjetivo;
    private JTextField txtUsuario, txtNombre, txtApellido, txtCorreo, txtFechaNac, txtGenero, txtDepto, txtPuesto;
    private JTextField txtTelefono, txtSalario, txtDireccion, txtAcercaDe;
    private JComboBox<String> cmbRol, cmbEstatus;
    private JButton btnActualizar, btnCancelar;

    public FormularioModificarUsuario(String codigoId) {
        this.codigoIdObjetivo = codigoId;
        initComponents();
        cargarDatosActuales();
    }

    private void initComponents() {
        setTitle("Modificar Usuario - " + codigoIdObjetivo);
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Modificar Datos del Usuario (" + codigoIdObjetivo + ")", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        int fila = 1;

        txtUsuario = agregarCampoFila(panelPrincipal, gbc, fila++, "Nombre de Usuario:", 50);
        
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
        
        txtTelefono = agregarCampoFila(panelPrincipal, gbc, fila++, "Teléfono:", 20);
        txtSalario = agregarCampoFila(panelPrincipal, gbc, fila++, "Salario:", 20);
        txtDireccion = agregarCampoFila(panelPrincipal, gbc, fila++, "Dirección:", 255);
        txtAcercaDe = agregarCampoFila(panelPrincipal, gbc, fila++, "Acerca de:", 255);

        gbc.gridx = 0; gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Estatus:"), gbc);
        cmbEstatus = new JComboBox<>(new String[]{"Activo", "Inactivo", "Suspendido"});
        gbc.gridx = 1;
        panelPrincipal.add(cmbEstatus, gbc);
        fila++;

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(new Color(230, 235, 240));

        btnActualizar = new JButton("Guardar Cambios");
        btnActualizar.setBackground(new Color(41, 128, 185));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.setPreferredSize(new Dimension(160, 35));
        btnActualizar.addActionListener(e -> validarYActualizar());
        panelBotones.add(btnActualizar);

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

    private void cargarDatosActuales() {
        UsuarioDAO dao = new UsuarioDAO();
        Map<String, String> datos = dao.obtenerUsuarioPorCodigoCompleto(codigoIdObjetivo);

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        txtUsuario.setText(datos.get("nombre_usuario"));
        cmbRol.setSelectedItem(datos.get("rol"));
        txtNombre.setText(datos.get("primer_nombre"));
        txtApellido.setText(datos.get("apellido"));
        txtCorreo.setText(datos.get("correo"));
        txtFechaNac.setText(datos.get("fecha_nacimiento"));
        txtGenero.setText(datos.get("genero"));
        txtDepto.setText(datos.get("departamento"));
        txtPuesto.setText(datos.get("puesto"));
        txtTelefono.setText(datos.get("telefono"));
        txtSalario.setText(datos.get("salario"));
        txtDireccion.setText(datos.get("direccion"));
        txtAcercaDe.setText(datos.get("acerca_de"));
        cmbEstatus.setSelectedItem(datos.get("estatus"));
    }

    private void validarYActualizar() {
        String usuario = txtUsuario.getText().trim();
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

        if (usuario.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() || salario.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hay campos obligatorios vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", correo)) {
            JOptionPane.showMessageDialog(this, "Formato de correo inválido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!fechaNac.isEmpty() && !Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", fechaNac)) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato YYYY-MM-DD.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(salario);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.actualizarUsuarioCompleto(codigoIdObjetivo, usuario, rol, nombre, apellido, correo, 
                                                      acercaDe, telefono, salario, direccion, fechaNac, 
                                                      genero, depto, puesto, estatus);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Modificación guardada con éxito en Azure!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}