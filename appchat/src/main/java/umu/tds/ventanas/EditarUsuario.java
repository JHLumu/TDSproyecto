package umu.tds.ventanas;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import umu.tds.appchat.AppChat;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Dimension;

public class EditarUsuario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private URL fotoPerfil;
    private boolean fotoCorrecta;
    private JTextArea saludoArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditarUsuario window = new EditarUsuario();
                    window.setVisible(true);
                    window.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EditarUsuario() {
    	setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(EditarUsuario.class.getResource("/Resources/chat.png")));
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 622, 400);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        GridBagLayout gbl_panelIzquierdo = new GridBagLayout();
        gbl_panelIzquierdo.columnWidths = new int[]{5, 0, 0, 5};
        gbl_panelIzquierdo.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0};
        gbl_panelIzquierdo.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        JPanel panelIzquierdo = new JPanel(gbl_panelIzquierdo);
        panelIzquierdo.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelIzquierdo.setBackground(new Color(200, 207, 251));
        contentPane.add(panelIzquierdo, BorderLayout.WEST);

        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.gridx = 1;
        gbc_lblNombre.gridy = 0;
        gbc_lblNombre.insets = new Insets(5, 5, 5, 5);
        gbc_lblNombre.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblNombre, gbc_lblNombre);

        GridBagConstraints gbc_lblNombreValor = new GridBagConstraints();
        gbc_lblNombreValor.gridx = 2;
        gbc_lblNombreValor.gridy = 0;
        gbc_lblNombreValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblNombreValor.anchor = GridBagConstraints.WEST;
        JLabel lblNombreValor = new JLabel(AppChat.getInstancia().getNombreUsuario());
        lblNombreValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombreValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblNombreValor, gbc_lblNombreValor);

        GridBagConstraints gbc_lblApellido = new GridBagConstraints();
        gbc_lblApellido.gridx = 1;
        gbc_lblApellido.gridy = 1;
        gbc_lblApellido.insets = new Insets(5, 5, 5, 5);
        gbc_lblApellido.anchor = GridBagConstraints.WEST;
        JLabel lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblApellido.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblApellido, gbc_lblApellido);

        GridBagConstraints gbc_lblApellidoValor = new GridBagConstraints();
        gbc_lblApellidoValor.gridx = 2;
        gbc_lblApellidoValor.gridy = 1;
        gbc_lblApellidoValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblApellidoValor.anchor = GridBagConstraints.WEST;
        JLabel lblApellidoValor = new JLabel(AppChat.getInstancia().getApellidosUsuario());
        lblApellidoValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblApellidoValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblApellidoValor, gbc_lblApellidoValor);

        GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
        gbc_lblTelefono.gridx = 1;
        gbc_lblTelefono.gridy = 2;
        gbc_lblTelefono.insets = new Insets(5, 5, 5, 5);
        gbc_lblTelefono.anchor = GridBagConstraints.WEST;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTelefono.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblTelefono, gbc_lblTelefono);

        GridBagConstraints gbc_lblTelefonoValor = new GridBagConstraints();
        gbc_lblTelefonoValor.gridx = 2;
        gbc_lblTelefonoValor.gridy = 2;
        gbc_lblTelefonoValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblTelefonoValor.anchor = GridBagConstraints.WEST;
        JLabel lblTelefonoValor = new JLabel(AppChat.getInstancia().getTelefonoUsuario());
        lblTelefonoValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTelefonoValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblTelefonoValor, gbc_lblTelefonoValor);

        GridBagConstraints gbc_lblEmail = new GridBagConstraints();
        gbc_lblEmail.gridx = 1;
        gbc_lblEmail.gridy = 3;
        gbc_lblEmail.insets = new Insets(5, 5, 5, 5);
        gbc_lblEmail.anchor = GridBagConstraints.WEST;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblEmail, gbc_lblEmail);

        GridBagConstraints gbc_lblEmailValor = new GridBagConstraints();
        gbc_lblEmailValor.gridx = 2;
        gbc_lblEmailValor.gridy = 3;
        gbc_lblEmailValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblEmailValor.anchor = GridBagConstraints.WEST;
        JLabel lblEmailValor = new JLabel(AppChat.getInstancia().getCorreoUsuario());
        lblEmailValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmailValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblEmailValor, gbc_lblEmailValor);

        GridBagConstraints gbc_lblFechaNacimiento = new GridBagConstraints();
        gbc_lblFechaNacimiento.gridx = 1;
        gbc_lblFechaNacimiento.gridy = 4;
        gbc_lblFechaNacimiento.insets = new Insets(5, 5, 5, 5);
        gbc_lblFechaNacimiento.anchor = GridBagConstraints.WEST;
        JLabel lblFechaNacimiento = new JLabel("Fecha de Nacimiento:");
        lblFechaNacimiento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFechaNacimiento.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblFechaNacimiento, gbc_lblFechaNacimiento);

        GridBagConstraints gbc_lblFechaNacimientoValor = new GridBagConstraints();
        gbc_lblFechaNacimientoValor.gridx = 2;
        gbc_lblFechaNacimientoValor.gridy = 4;
        gbc_lblFechaNacimientoValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblFechaNacimientoValor.anchor = GridBagConstraints.WEST;
        JLabel lblFechaNacimientoValor = new JLabel(AppChat.getInstancia().getFechaNacimientoUsuario());
        lblFechaNacimientoValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFechaNacimientoValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblFechaNacimientoValor, gbc_lblFechaNacimientoValor);

        GridBagConstraints gbc_lblSaludo = new GridBagConstraints();
        gbc_lblSaludo.gridx = 1;
        gbc_lblSaludo.gridy = 5;
        gbc_lblSaludo.insets = new Insets(5, 5, 5, 5);
        gbc_lblSaludo.anchor = GridBagConstraints.WEST;
        JLabel lblSaludo = new JLabel("Saludo:");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSaludo.setForeground(new Color(25, 25, 112));
        
        panelIzquierdo.add(lblSaludo, gbc_lblSaludo);
        
        
        JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(5, 5, 0, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		panelIzquierdo.add(scrollPane, gbc_scrollPane);
		
		saludoArea = new JTextArea();
		scrollPane.setViewportView(saludoArea);

        
        

        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBorder(new LineBorder(new Color(130, 135, 144)));
        panelDerecho.setBackground(new Color(255, 255, 255));
        contentPane.add(panelDerecho, BorderLayout.CENTER);

        GridBagConstraints gbc_lblURL = new GridBagConstraints();
        gbc_lblURL.insets = new Insets(5, 5, 5, 5);
        gbc_lblURL.gridx = 0;
        gbc_lblURL.gridy = 0;
        JLabel lblURL = new JLabel("Inserte la nueva URL de la Imagen:");
        lblURL.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblURL.setForeground(Color.BLACK);
        panelDerecho.add(lblURL, gbc_lblURL);

        GridBagConstraints gbc_lblImagen = new GridBagConstraints();
        gbc_lblImagen.insets = new Insets(10, 10, 10, 10);
        gbc_lblImagen.gridx = 0;
        gbc_lblImagen.gridy = 1;
        gbc_lblImagen.gridwidth = 2;
        JLabel lblImagen = new JLabel();
        lblImagen.setIcon(new ImageIcon(AppChat.getInstancia().getFotoPerfilSesion().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(new LineBorder(new Color(25, 25, 112), 2, true));
        panelDerecho.add(lblImagen, gbc_lblImagen);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(5, 5, 5, 5);
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 2;
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        textField = new JTextField();
        panelDerecho.add(textField, gbc_textField);

        GridBagConstraints gbc_btnAceptarImagen = new GridBagConstraints();
        gbc_btnAceptarImagen.insets = new Insets(10, 5, 0, 5);
        gbc_btnAceptarImagen.gridx = 0;
        gbc_btnAceptarImagen.gridy = 3;
        JButton btnAceptarImagen = new JButton("Aceptar");
        btnAceptarImagen.setForeground(new Color(255, 255, 255));
        btnAceptarImagen.setBackground(new Color(81, 116, 255));
        btnAceptarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelDerecho.add(btnAceptarImagen, gbc_btnAceptarImagen);

        btnAceptarImagen.addActionListener(evento -> {
            Image imagen = AppChat.getInstancia().getImagen(textField.getText());
            if (imagen != null) {
                try {
                    this.fotoPerfil = new URL(textField.getText());
                    this.fotoCorrecta = true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                lblImagen.setIcon(new ImageIcon(imagen.getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
            } else {
                this.fotoCorrecta = false;
                JOptionPane.showMessageDialog(this,
                        "No se pudo descargar la imagen desde la URL proporcionada.",
                        "AppChat",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        String saludoUsuario = AppChat.getInstancia().getSaludoUsuario();
        saludoArea.setText(saludoUsuario);
        saludoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        saludoArea.setForeground(new Color(25, 25, 112));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (fotoCorrecta) {
                    AppChat.getInstancia().cambiarFotoPerfil(fotoPerfil);
                };
                
                if(saludoUsuario ==null || !saludoUsuario.equals(saludoArea.getText())) {
                	AppChat.getInstancia().cambiarSaludo(saludoArea.getText());
                }
            }
        });
    }
}