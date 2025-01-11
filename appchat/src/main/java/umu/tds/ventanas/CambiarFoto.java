package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import umu.tds.appchat.AppChat;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(CambiarFoto.class.getResource("/Resources/chat.png")));
		setTitle("Cambiar foto de perfil");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 597, 386);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{20, 150, 20, 0};
		gbl_panel.rowHeights = new int[]{20, 20, 0, 0, 20, 20, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblImagenTxt = new JLabel("Imagen:");
		GridBagConstraints gbc_lblImagenTxt = new GridBagConstraints();
		gbc_lblImagenTxt.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagenTxt.gridx = 1;
		gbc_lblImagenTxt.gridy = 1;
		panel.add(lblImagenTxt, gbc_lblImagenTxt);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblImagen = new JLabel("");
		lblImagen.setIcon(new ImageIcon(AppChat.getInstancia().getFotoPerfilSesion().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.gridwidth = 1;
		gbc_lblImagen.gridheight = 1;
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 1;
		gbc_lblImagen.gridy = 3;
		panel.add(lblImagen, gbc_lblImagen);
		
		contentPane.add(panel);
		
		JButton btnAceptarImagen = new JButton("Aceptar");
		GridBagConstraints gbc_btnAceptarImagen = new GridBagConstraints();
		gbc_btnAceptarImagen.insets = new Insets(0, 0, 5, 5);
		gbc_btnAceptarImagen.gridx = 1;
		gbc_btnAceptarImagen.gridy = 4;
		
		btnAceptarImagen.addActionListener(evento -> {
			Image imagen = AppChat.getInstancia().getImagen(textField.getText());
		     if(imagen != null) {
		    	try {
					this.fotoPerfil = new URL(textField.getText());
					this.fotoCorrecta = true;
					AppChat.getInstancia().setImagenPerfil(imagen);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	 lblImagen.setIcon(new ImageIcon(imagen.getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
		     } else {
		    	 this.fotoCorrecta=false;
		    	 JOptionPane.showMessageDialog(this, 
		                 "No se pudo descargar la imagen desde la URL proporcionada.",
		                 "AppChat",
		                 JOptionPane.ERROR_MESSAGE);
		     }
		});
		panel.add(btnAceptarImagen, gbc_btnAceptarImagen);
		
		this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	if(fotoCorrecta == true) {
	        		AppChat.getInstancia().cambiarFotoPerfil(fotoPerfil);
	        	}
	        }
		});
		
	}
		

}
