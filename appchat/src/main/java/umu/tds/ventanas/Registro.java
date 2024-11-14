package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZoneId;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import com.toedter.calendar.JDateChooser;

import umu.tds.appchat.AppChat;

import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

public class Registro extends JFrame {

	private JPanel contentPane;
	private JTextField nombreField;
	private JTextField apellidosField;
	private JTextField telefonoField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField imagenField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registro frame = new Registro();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Registro() {
		setTitle("Registro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 597, 386);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resources/chat.png")));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20, 20, 136, 0, 0, 20, 0};
		gbl_contentPane.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);
		
		nombreField = new JTextField();
		GridBagConstraints gbc_nombreField = new GridBagConstraints();
		gbc_nombreField.gridwidth = 3;
		gbc_nombreField.insets = new Insets(0, 0, 5, 5);
		gbc_nombreField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombreField.gridx = 2;
		gbc_nombreField.gridy = 1;
		contentPane.add(nombreField, gbc_nombreField);
		nombreField.setColumns(10);
		
	
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.EAST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 2;
		contentPane.add(lblApellidos, gbc_lblApellidos);
		
		apellidosField = new JTextField();
		GridBagConstraints gbc_apellidosField = new GridBagConstraints();
		gbc_apellidosField.gridwidth = 3;
		gbc_apellidosField.insets = new Insets(0, 0, 5, 5);
		gbc_apellidosField.fill = GridBagConstraints.HORIZONTAL;
		gbc_apellidosField.gridx = 2;
		gbc_apellidosField.gridy = 2;
		contentPane.add(apellidosField, gbc_apellidosField);
		apellidosField.setColumns(10);
		
		JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
		lblTelfono.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblTelfono = new GridBagConstraints();
		gbc_lblTelfono.anchor = GridBagConstraints.EAST;
		gbc_lblTelfono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelfono.gridx = 1;
		gbc_lblTelfono.gridy = 3;
		contentPane.add(lblTelfono, gbc_lblTelfono);
		
		telefonoField = new JTextField();
		GridBagConstraints gbc_telefonoField = new GridBagConstraints();
		gbc_telefonoField.insets = new Insets(0, 0, 5, 5);
		gbc_telefonoField.fill = GridBagConstraints.HORIZONTAL;
		gbc_telefonoField.gridx = 2;
		gbc_telefonoField.gridy = 3;
		contentPane.add(telefonoField, gbc_telefonoField);
		telefonoField.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.EAST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 1;
		gbc_lblContrasea.gridy = 4;
		contentPane.add(lblContrasea, gbc_lblContrasea);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 4;
		contentPane.add(passwordField, gbc_passwordField);
		
		JLabel lblContrasea_1 = new JLabel("Conf. contrase\u00F1a:");
		lblContrasea_1.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblContrasea_1 = new GridBagConstraints();
		gbc_lblContrasea_1.anchor = GridBagConstraints.EAST;
		gbc_lblContrasea_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea_1.gridx = 3;
		gbc_lblContrasea_1.gridy = 4;
		contentPane.add(lblContrasea_1, gbc_lblContrasea_1);
		
		passwordField_1 = new JPasswordField();
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 4;
		gbc_passwordField_1.gridy = 4;
		contentPane.add(passwordField_1, gbc_passwordField_1);
		
		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblFecha = new GridBagConstraints();
		gbc_lblFecha.anchor = GridBagConstraints.EAST;
		gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
		gbc_lblFecha.gridx = 1;
		gbc_lblFecha.gridy = 5;
		contentPane.add(lblFecha, gbc_lblFecha);
		
		JDateChooser fecha = new JDateChooser();
		GridBagConstraints gbc_fecha = new GridBagConstraints();
		gbc_fecha.insets = new Insets(0, 0, 5, 5);
		gbc_fecha.fill = GridBagConstraints.BOTH;
		gbc_fecha.gridx = 2;
		gbc_fecha.gridy = 5;
		contentPane.add(fecha, gbc_fecha);
		
		JLabel lblSaludo = new JLabel("Saludo:");
		lblSaludo.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblSaludo = new GridBagConstraints();
		gbc_lblSaludo.anchor = GridBagConstraints.EAST;
		gbc_lblSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaludo.gridx = 1;
		gbc_lblSaludo.gridy = 6;
		contentPane.add(lblSaludo, gbc_lblSaludo);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 6;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel lblImagen = new JLabel("Imagen:");
		lblImagen.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.anchor = GridBagConstraints.EAST;
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 3;
		gbc_lblImagen.gridy = 6;
		contentPane.add(lblImagen, gbc_lblImagen);
		
		imagenField = new JTextField();
		GridBagConstraints gbc_imagenField = new GridBagConstraints();
		gbc_imagenField.insets = new Insets(0, 0, 5, 5);
		gbc_imagenField.fill = GridBagConstraints.HORIZONTAL;
		gbc_imagenField.gridx = 4;
		gbc_imagenField.gridy = 6;
		contentPane.add(imagenField, gbc_imagenField);
		imagenField.setColumns(10);
		
		JLabel lblImagen_1 = new JLabel("");
		lblImagen_1.setIcon(new ImageIcon(Registro.class.getResource("/resources/usuario_64.png")));
		GridBagConstraints gbc_lblImagen_1 = new GridBagConstraints();
		gbc_lblImagen_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen_1.gridx = 4;
		gbc_lblImagen_1.gridy = 7;
		contentPane.add(lblImagen_1, gbc_lblImagen_1);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 8;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.setForeground(new Color(255, 255, 255));
		botonCancelar.setBackground(new Color(81, 116, 255));
		botonCancelar.setFont(new Font("Gill Sans MT", Font.BOLD, 12));
		botonCancelar.addActionListener(evento -> {
			this.dispose();
		});
		panel.add(botonCancelar);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.setForeground(new Color(255, 255, 255));
		botonAceptar.setBackground(new Color(81, 116, 255));
		botonAceptar.setFont(new Font("Gill Sans MT", Font.BOLD, 12));
		botonAceptar.addActionListener(evento -> {
			
			if ((fecha.getDate() != null) && AppChat.registrarUsuario(nombreField.getText(), apellidosField.getText(), telefonoField.getText(), (fecha.getDate().toInstant().atZone(ZoneId.systemDefault())).toLocalDate(), "", new String(passwordField.getPassword()))) {
				RegistroCorrecto frame = new RegistroCorrecto();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				this.dispose();
			}
			
			else {
				
					RegistroFallido frame = new RegistroFallido();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
						
				}
			});
		
		panel.add(botonAceptar);
	}

}