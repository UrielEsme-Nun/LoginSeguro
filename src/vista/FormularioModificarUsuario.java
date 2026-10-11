package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FormularioModificarUsuario extends JFrame {

    private String codigoIdObjetivo;
    private JTextField txtUsuario, txtRol, txtNombre, txtApellido, txtCorreo, txtFechaNac, txtGenero, txtDepto, txtPuesto;
    private JTextField txtTelefono, txtSalario, txtDireccion;
    private JButton btnActualizar, btnCancelar;

    public FormularioModificarUsuario(String codigoId) {
        this.codigoIdObjetivo = codigoId;
        initComponents();
        cargarDatosActuales();
    }

    private void initComponents() {
        setTitle("Modificar Usuario - Código ID: " + codigoIdObjetivo);
        setSize(520, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Modificar datos del usuario (" + codigoIdObjetivo + ")", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(30, 15, 450, 30);
        add(lblTitulo);

        int y = 60;
        int salto = 38;

        txtUsuario = agregarCampo("Nombre de Usuario:", y);
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
        txtTelefono = agregarCampo("Teléfono:", y);
        y += salto;
        txtSalario = agregarCampo("Salario:", y);
        y += salto;
        txtDireccion = agregarCampo("Dirección:", y);

        y += 45;
        btnActualizar = new JButton("Guardar Cambios");
        btnActualizar.setBounds(110, y, 140, 35);
        btnActualizar.addActionListener(e -> actualizarEnBD());
        add(btnActualizar);

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

    private void cargarDatosActuales() {
        UsuarioDAO dao = new UsuarioDAO();
        Map<String, String> datos = dao.obtenerUsuarioPorCodigo(codigoIdObjetivo);

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con el Código ID: " + codigoIdObjetivo, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        txtUsuario.setText(datos.get("nombre_usuario"));
        txtRol.setText(datos.get("rol"));
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
    }

    private void actualizarEnBD() {
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.actualizarUsuario(
            codigoIdObjetivo,
            txtUsuario.getText().trim(),
            txtRol.getText().trim(),
            txtNombre.getText().trim(),
            txtApellido.getText().trim(),
            txtCorreo.getText().trim(),
            txtFechaNac.getText().trim(),
            txtGenero.getText().trim(),
            txtDepto.getText().trim(),
            txtPuesto.getText().trim(),
            txtTelefono.getText().trim(),
            txtSalario.getText().trim(),
            txtDireccion.getText().trim()
        );

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Usuario actualizado correctamente en Azure!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}