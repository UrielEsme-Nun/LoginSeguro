package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class VentanaVerUsuarios extends JFrame {

    private String rolUsuario;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtFiltroCodigo;
    private JButton btnFiltrar, btnVerTodos, btnRegresar;

    public VentanaVerUsuarios(String rol) {
        this.rolUsuario = rol != null ? rol.toLowerCase().trim() : "empleado";
        initComponents();
        cargarDatosTabla(null);
    }

    private void initComponents() {
        setTitle("Reporte General de Usuarios - Sistema Seguro");
        setSize(1100, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Superior con Estilo
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(245, 247, 250));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Listado General de Usuarios Registrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        // Panel del Buscador / Filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelFiltro.setBackground(new Color(245, 247, 250));

        JLabel lblFiltro = new JLabel("Filtrar por Código ID:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(lblFiltro);

        txtFiltroCodigo = new JTextField(18);
        txtFiltroCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(txtFiltroCodigo);

        btnFiltrar = new JButton("Buscar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setBackground(new Color(41, 128, 185));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> filtrarPorCodigo());
        panelFiltro.add(btnFiltrar);

        btnVerTodos = new JButton("Mostrar Todos");
        btnVerTodos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerTodos.setFocusPainted(false);
        btnVerTodos.addActionListener(e -> {
            txtFiltroCodigo.setText("");
            cargarDatosTabla(null);
        });
        panelFiltro.add(btnVerTodos);

        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        // Configuración de la Tabla
        modeloTabla = new DefaultTableModel();
        String[] columnas = {
            "ID", "Código ID", "Usuario", "Rol", "Nombre", "Apellido", 
            "Correo", "Acerca de", "Género", "Depto.", "Puesto", "Estatus", 
            "Teléfono (AES)", "Salario (AES)", "Dirección (AES)"
        };
        for (String col : columnas) {
            modeloTabla.addColumn(col);
        }

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaUsuarios.getTableHeader().setBackground(new Color(230, 235, 240));
        tablaUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior con Botón de Cierre
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBackground(new Color(245, 247, 250));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        btnRegresar = new JButton("Cerrar Ventana");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegresar.setPreferredSize(new Dimension(150, 35));
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e -> this.dispose());
        panelSur.add(btnRegresar);

        add(panelSur, BorderLayout.SOUTH);
    }

    private void cargarDatosTabla(String filtro) {
        modeloTabla.setRowCount(0);
        UsuarioDAO dao = new UsuarioDAO();
        Vector<Object[]> registros = dao.obtenerUsuarios(rolUsuario, filtro);

        for (Object[] fila : registros) {
            modeloTabla.addRow(fila);
        }

        if (filtro != null && !filtro.isEmpty() && registros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el Código ID: " + filtro, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrarPorCodigo() {
        String codigoBuscado = txtFiltroCodigo.getText().trim();
        if (codigoBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un Código ID para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosTabla(codigoBuscado);
    }
}