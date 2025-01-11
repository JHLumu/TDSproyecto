package umu.tds.ventanas;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class CambiarFoto extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private URL fotoPerfil;
	private boolean fotoCorrecta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    CambiarFoto window = new CambiarFoto();
					window.setVisible(true);
					window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the window.
	 */
	public CambiarFoto() {
        // Configuración básica del JFrame
        setIconImage(Toolkit.getDefaultToolkit().getImage(CambiarFoto.class.getResource("/Resources/chat.png")));
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        // Panel principal con BorderLayout
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Panel izquierdo (información del usuario)
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelIzquierdo.setBackground(new Color(200, 207, 251));
        contentPane.add(panelIzquierdo, BorderLayout.WEST);

        // Etiqueta "Nombre"
        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.insets = new Insets(5, 5, 5, 5);
        gbc_lblNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNombre.gridx = 0;
        gbc_lblNombre.gridy = 0;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblNombre, gbc_lblNombre);

        // Valor del nombre
        GridBagConstraints gbc_lblNombreValor = new GridBagConstraints();
        gbc_lblNombreValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblNombreValor.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNombreValor.gridx = 1;
        gbc_lblNombreValor.gridy = 0;
        JLabel lblNombreValor = new JLabel(AppChat.getInstancia().getNombreUsuario());
        lblNombreValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombreValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblNombreValor, gbc_lblNombreValor);

        // Etiqueta "Teléfono"
        GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
        gbc_lblTelefono.insets = new Insets(5, 5, 5, 5);
        gbc_lblTelefono.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTelefono.gridx = 0;
        gbc_lblTelefono.gridy = 1;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTelefono.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblTelefono, gbc_lblTelefono);

        // Valor del teléfono
        GridBagConstraints gbc_lblTelefonoValor = new GridBagConstraints();
        gbc_lblTelefonoValor.insets = new Insets(5, 5, 5, 5);
        gbc_lblTelefonoValor.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTelefonoValor.gridx = 1;
        gbc_lblTelefonoValor.gridy = 1;
        JLabel lblTelefonoValor = new JLabel(AppChat.getInstancia().getTelefonoUsuario());
        lblTelefonoValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTelefonoValor.setForeground(new Color(25, 25, 112));
        panelIzquierdo.add(lblTelefonoValor, gbc_lblTelefonoValor);

        // Panel derecho (imagen de perfil)
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBorder(new LineBorder(new Color(130, 135, 144)));
        panelDerecho.setBackground(new Color(255, 255, 255));
        contentPane.add(panelDerecho, BorderLayout.CENTER);
        
                // Campo para la URL de la imagen
                GridBagConstraints gbc_lblURL = new GridBagConstraints();
                gbc_lblURL.insets = new Insets(5, 5, 5, 5);
                gbc_lblURL.gridx = 0;
                gbc_lblURL.gridy = 0;
                JLabel lblURL = new JLabel("Inserte la nueva URL de la Imagen:");
                lblURL.setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblURL.setForeground(Color.BLACK);
                panelDerecho.add(lblURL, gbc_lblURL);

        // Imagen de perfil
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
        gbc_textField.gridy = 9;
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        textField = new JTextField();
       
        panelDerecho.add(textField, gbc_textField);

        // Botón "Aceptar"
        GridBagConstraints gbc_btnAceptarImagen = new GridBagConstraints();
        gbc_btnAceptarImagen.insets = new Insets(10, 5, 0, 5);
        gbc_btnAceptarImagen.gridx = 0;
        gbc_btnAceptarImagen.gridy = 10;
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

        // Evento al cerrar la ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (fotoCorrecta) {
                    AppChat.getInstancia().cambiarFotoPerfil(fotoPerfil);
                }
            }
        });
    }

}
