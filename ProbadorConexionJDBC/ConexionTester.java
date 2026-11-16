import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;

public class ConexionTester extends JFrame {

    private final IConexionServiceable conexionService;

    private JTextField txtUrl;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnProbarConexion;
    private JTextArea txtResultado;

    public ConexionTester(IConexionServiceable conexionService) {
        this.conexionService = conexionService;
        configurarVentana();
        inicializarComponentes();
    }

    public ConexionTester() {
        this(new ConexionServiceImpl());
    }

    public void configurarVentana() {
        setTitle("Probador de Conexión JDBC - IUD");
        setSize(580, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    public void inicializarComponentes() {
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setBackground(new Color(0, 43, 73));
        JLabel lblTitulo = new JLabel("Validador de Conexión a Bases de Datos - IUD");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelHeader.add(lblTitulo);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 12));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 5, 15),
                BorderFactory.createTitledBorder("Configuración de Conexión JDBC")
        ));

        JLabel lblUrl = new JLabel("URL de conexión:");
        lblUrl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUrl = new JTextField("jdbc:mysql://localhost:3306/iud_academico");
        txtUrl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblUsuario = new JLabel("Nombre de usuario (vacío si es SQLite):");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario = new JTextField("root");
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblContrasena = new JLabel("Contraseña (vacío si es SQLite):");
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panelFormulario.add(lblUrl);
        panelFormulario.add(txtUrl);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblContrasena);
        panelFormulario.add(txtContrasena);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnProbarConexion = new JButton("Probar conexión");
        btnProbarConexion.setBackground(new Color(0, 168, 150));
        btnProbarConexion.setForeground(Color.WHITE);
        btnProbarConexion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProbarConexion.setFocusPainted(false);
        panelBoton.add(btnProbarConexion);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelFormulario, BorderLayout.CENTER);
        panelCentro.add(panelBoton, BorderLayout.SOUTH);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelResultado = new JPanel(new BorderLayout());
        panelResultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 15, 15, 15),
                BorderFactory.createTitledBorder("Resultado de la Prueba")
        ));

        txtResultado = new JTextArea(6, 40);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        panelResultado.add(scrollResultado, BorderLayout.CENTER);
        add(panelResultado, BorderLayout.SOUTH);

        // ActionListener con expresión lambda (igual que VentanaGestionAcademica)
        btnProbarConexion.addActionListener(e -> ejecutarPruebaConexion());
    }

    private void ejecutarPruebaConexion() {
        String url = txtUrl.getText();
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        try {
            String mensajeExito = conexionService.validarConexion(url, usuario, contrasena);
            txtResultado.setForeground(new Color(0, 128, 0));
            txtResultado.setText(mensajeExito);
        } catch (ClassNotFoundException ex) {
            txtResultado.setForeground(Color.RED);
            txtResultado.setText("Fallo de conexión (ClassNotFoundException):\n"
                    + "No se encontró el driver JDBC en el proyecto.\n"
                    + "Detalle técnico: " + ex.getMessage());
        } catch (SQLException ex) {
            txtResultado.setForeground(Color.RED);
            txtResultado.setText("Fallo de conexión (SQLException):\n"
                    + "No fue posible conectar con la base de datos.\n"
                    + "Detalle técnico: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IConexionServiceable servicio = new ConexionServiceImpl();
            ConexionTester ventana = new ConexionTester(servicio);
            ventana.setVisible(true);
        });
    }
}
