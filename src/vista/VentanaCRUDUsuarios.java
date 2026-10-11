package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VentanaCRUDUsuarios extends JFrame {

    private JButton btnAgregar, btnModificar, btnEliminar, btnRegresar;

    public VentanaCRUDUsuarios() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión y CRUD de Usuarios - Panel Administrador");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        panel.setBounds(0, 0, 420, 340);
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Panel de Administración CRUD", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(35, 25, 350, 30);
        panel.add(lblTitulo);

        // Botón Agregar
        btnAgregar = new JButton("Agregar Nuevo Usuario");
        btnAgregar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAgregar.setBounds(60, 80, 300, 40);
        btnAgregar.addActionListener(e -> abrirFormularioAgregar());
        panel.add(btnAgregar);

        // Botón Modificar
        btnModificar = new JButton("Modificar Usuario por Código ID");
        btnModificar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnModificar.setBounds(60, 135, 300, 40);
        btnModificar.addActionListener(e -> solicitarIdParaModificar());
        panel.add(btnModificar);

        // Botón Eliminar
        btnEliminar = new JButton("Eliminar Usuario por Código ID");
        btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEliminar.setBounds(60, 190, 300, 40);
        btnEliminar.addActionListener(e -> solicitarIdParaEliminar());
        panel.add(btnEliminar);

        // Botón Regresar
        btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegresar.setBounds(110, 255, 200, 30);
        btnRegresar.addActionListener(e -> this.dispose());
        panel.add(btnRegresar);

        add(panel);
    }

    private void abrirFormularioAgregar() {
        FormularioAgregarUsuario formAgregar = new FormularioAgregarUsuario(this);
        formAgregar.setVisible(true);
    }

    private void solicitarIdParaModificar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el Código ID del usuario a modificar:", "Modificar Usuario", JOptionPane.QUESTION_MESSAGE);
        if (codigo != null && !codigo.trim().isEmpty()) {
            UsuarioDAO dao = new UsuarioDAO();
            Map<String, String> datos = dao.obtenerUsuarioPorCodigo(codigo.trim());
            
            if (datos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El Código ID '" + codigo.trim() + "' no existe en la base de datos.", "No encontrado", JOptionPane.WARNING_MESSAGE);
            } else {
                FormularioModificarUsuario formModificar = new FormularioModificarUsuario(codigo.trim());
                formModificar.setVisible(true);
            }
        }
    }

    private void solicitarIdParaEliminar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el Código ID del usuario a eliminar:", "Eliminar Usuario", JOptionPane.QUESTION_MESSAGE);
        if (codigo != null && !codigo.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Confirma que desea eliminar permanentemente al usuario con Código ID: " + codigo + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                UsuarioDAO usuarioDao = new UsuarioDAO();
                boolean eliminado = usuarioDao.eliminarUsuario(codigo.trim());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente de la base de datos.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar. Verifique que el Código ID exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}