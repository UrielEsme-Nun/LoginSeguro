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
        setTitle("Reporte General de Usuarios - Azure MySQL");
        setSize(1050, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Listado de Usuarios Registrados en Base de Datos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelFiltro.add(new JLabel("Filtrar por Código ID:"));

        txtFiltroCodigo = new JTextField(15);
        panelFiltro.add(txtFiltroCodigo);

        btnFiltrar = new JButton("Buscar");
        btnFiltrar.addActionListener(e -> filtrarPorCodigo());
        panelFiltro.add(btnFiltrar);

        btnVerTodos = new JButton("Mostrar Todos");
        btnVerTodos.addActionListener(e -> {
            txtFiltroCodigo.setText("");
            cargarDatosTabla(null);
        });
        panelFiltro.add(btnVerTodos);

        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();
        String[] columnas = {
            "ID", "Código ID", "Usuario", "Rol", "Nombre", "Apellido", 
            "Correo", "Género", "Depto.", "Puesto", "Estatus", 
            "Teléfono (AES)", "Salario (AES)", "Dirección (AES)"
        };
        for (String col : columnas) {
            modeloTabla.addColumn(col);
        }

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnRegresar = new JButton("Cerrar Ventana");
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