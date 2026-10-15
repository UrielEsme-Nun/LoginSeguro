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
        setTitle("Panel de Administración CRUD");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(new Color(44, 62, 80));
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Panel de Administración CRUD", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(40, 30, 400, 30);
        panel.add(lblTitulo);

        // Botón Agregar
        btnAgregar = new JButton("Agregar Nuevo Usuario");
        btnAgregar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAgregar.setBounds(50, 95, 380, 45);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> abrirFormularioAgregar());
        panel.add(btnAgregar);

        // Botón Modificar
        btnModificar = new JButton("Modificar Usuario por Código ID");
        btnModificar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnModificar.setBounds(50, 160, 380, 45);
        btnModificar.setFocusPainted(false);
        btnModificar.addActionListener(e -> solicitarIdParaModificar());
        panel.add(btnModificar);

        // Botón Eliminar
        btnEliminar = new JButton("Eliminar Usuario por Código ID");
        btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEliminar.setBounds(50, 225, 380, 45);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> solicitarIdParaEliminar());
        panel.add(btnEliminar);

        // Botón Regresar
        btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegresar.setBackground(new Color(127, 140, 141));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBounds(50, 300, 380, 40);
        btnRegresar.addActionListener(e -> this.dispose());
        panel.add(btnRegresar);

        add(panel, BorderLayout.CENTER);
    }

    private void abrirFormularioAgregar() {
        FormularioAgregarUsuario formAgregar = new FormularioAgregarUsuario(this);
        formAgregar.setVisible(true);
    }

    private void solicitarIdParaModificar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el Código ID del usuario a modificar:", "Modificar Usuario", JOptionPane.QUESTION_MESSAGE);
        if (codigo != null && !codigo.trim().isEmpty()) {
            UsuarioDAO dao = new UsuarioDAO();
            Map<String, String> datos = dao.obtenerUsuarioPorCodigoCompleto(codigo.trim());
            
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