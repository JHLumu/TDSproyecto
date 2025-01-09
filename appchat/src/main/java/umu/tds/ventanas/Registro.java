package umu.tds.ventanas;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nombreField;
	private JTextField apellidosField;
	private JTextField telefonoField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JDateChooser fecha;
	private JTextArea saludoArea;
	private JTextField imagenField;
	private JTextField emailField;
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
	
	private boolean validacionTelefono() {
		//Devuelve falso si telefono esta vacio o no cumple con el formato de 9 numeros sin espacios
		String telefono = this.telefonoField.getText();
		if (telefono.isEmpty()) return false;
		System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono no es vacio.");
		Pattern expresionRegularTelefonos = Pattern.compile("^[0-9]{9}$");
		Matcher matcher = expresionRegularTelefonos.matcher(telefono);
		boolean resultado = matcher.matches();
		System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono: " + telefono);
		return resultado;
	}
	
	
	private boolean validacionPasswords() {
		//Validar el formato de las contraseñas (si añadimos)
		if (this.passwordField.getPassword() == null || this.passwordField_1.getPassword() == null) return false;
		String password = new String(this.passwordField.getPassword());
		String password1 = new String(this.passwordField_1.getPassword());
		boolean resultado = (password.equals(password1));
		System.out.println("[DEBUG Registro validacionPasswords]: " + "Password son iguales: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionPasswords]: " + "Password: " + password);
		return resultado;
	}
	
	private boolean validacionEmail() {
		//Se sigue el formato estandar de RFC 5322 Y RFC 5321
		String email = this.emailField.getText();
		if (email.isEmpty()) return false;
		Pattern expresionRegularEmails = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = expresionRegularEmails.matcher(email);
		boolean resultado = matcher.matches();
		System.out.println("[DEBUG Registro validacionEmail]: " + "Email cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionEmail]: " + "Email: " + email);
		return resultado;
	}
	
	private boolean validacionFecha() {
		//Se verifica si el usuario que se quiere registrado tiene al menos 16 años de edad.
		Date instante = this.fecha.getDate();
		if (instante == null) return false;
		LocalDate fecha = instante.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		boolean resultado = ((LocalDate.now().getYear() - fecha.getYear()) >= 16);
		System.out.println("[DEBUG Registro validacionFecha]: " + "Fecha cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionFecha]: " + "Fecha: " + fecha.toString());
		return resultado;
		
	}
	
	private boolean validacionImagen() {
		Image imagen = (AppChat.getInstancia().getImagen(this.imagenField.getText()));
		boolean resultado = (imagen != null);
		System.out.println("[DEBUG Registro validacionImagen]: " + "Imagen cumple formato: " + resultado);
		return resultado;
		
	}
	
	private boolean validacionNombreCompleto() {
		String nombre = this.nombreField.getText();
		String apellidos = this.apellidosField.getText();
		if (nombre.isEmpty() || apellidos.isEmpty()) return false;
		Pattern expresionRegular = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+([ -][A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$");
		Matcher matcherNombre = expresionRegular.matcher(nombre);
		Matcher matcherApellidos = expresionRegular.matcher(apellidos);
		boolean resultado1 = matcherNombre.matches();
		boolean resultado2 = matcherApellidos.matches();
		System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Nombre cumple formato: " + resultado1);
		System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Apellidos cumple formato: " + resultado2);
		if (resultado1 && resultado2) System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Nombre completo: " + nombre + 
				 " " + apellidos);
		return (resultado1 && resultado2);
	}
	
	private boolean validacionCampos() {
		
		return (validacionNombreCompleto() && validacionTelefono() 
				&& validacionPasswords() && validacionEmail() 
				&& validacionFecha() && validacionImagen());
			
	}
	

	boolean registroUsuario() {
		try {
			return AppChat.getInstancia().registrarUsuario(
					nombreField.getText(), 
					apellidosField.getText(), 
					telefonoField.getText(), 
					(fecha.getDate().toInstant().atZone(ZoneId.systemDefault())).toLocalDate(),
					emailField.getText(),
					new String(passwordField.getPassword()),
					saludoArea.getText(), 
					new URL(imagenField.getText()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Registro() {
		setResizable(false);
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
		gbl_contentPane.columnWidths = new int[]{20, 20, 136, 0, 121, 0, 20, 0};
		gbl_contentPane.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);
		
		nombreField = new JTextField();
		GridBagConstraints gbc_nombreField = new GridBagConstraints();
		gbc_nombreField.gridwidth = 4;
		gbc_nombreField.insets = new Insets(0, 0, 5, 5);
		gbc_nombreField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombreField.gridx = 2;
		gbc_nombreField.gridy = 1;
		contentPane.add(nombreField, gbc_nombreField);
		nombreField.setColumns(10);
		
	
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.EAST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 2;
		contentPane.add(lblApellidos, gbc_lblApellidos);
		
		apellidosField = new JTextField();
		GridBagConstraints gbc_apellidosField = new GridBagConstraints();
		gbc_apellidosField.gridwidth = 4;
		gbc_apellidosField.insets = new Insets(0, 0, 5, 5);
		gbc_apellidosField.fill = GridBagConstraints.HORIZONTAL;
		gbc_apellidosField.gridx = 2;
		gbc_apellidosField.gridy = 2;
		contentPane.add(apellidosField, gbc_apellidosField);
		apellidosField.setColumns(10);
		
		JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
		lblTelfono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
		
		JLabel lblEmail = new JLabel("email:");
		lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 3;
		gbc_lblEmail.gridy = 3;
		contentPane.add(lblEmail, gbc_lblEmail);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.gridwidth = 2;
		gbc_emailField.insets = new Insets(0, 0, 5, 5);
		gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailField.gridx = 4;
		gbc_emailField.gridy = 3;
		contentPane.add(emailField, gbc_emailField);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
		lblContrasea_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblContrasea_1 = new GridBagConstraints();
		gbc_lblContrasea_1.anchor = GridBagConstraints.EAST;
		gbc_lblContrasea_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea_1.gridx = 3;
		gbc_lblContrasea_1.gridy = 4;
		contentPane.add(lblContrasea_1, gbc_lblContrasea_1);
		
		
		
		passwordField_1 = new JPasswordField();
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.gridwidth = 2;
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 4;
		gbc_passwordField_1.gridy = 4;
		contentPane.add(passwordField_1, gbc_passwordField_1);
		
		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblFecha = new GridBagConstraints();
		gbc_lblFecha.anchor = GridBagConstraints.EAST;
		gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
		gbc_lblFecha.gridx = 1;
		gbc_lblFecha.gridy = 5;
		contentPane.add(lblFecha, gbc_lblFecha);
		
		fecha = new JDateChooser();
		GridBagConstraints gbc_fecha = new GridBagConstraints();
		gbc_fecha.insets = new Insets(0, 0, 5, 5);
		gbc_fecha.fill = GridBagConstraints.BOTH;
		gbc_fecha.gridx = 2;
		gbc_fecha.gridy = 5;
		contentPane.add(fecha, gbc_fecha);
		
		JLabel lblSaludo = new JLabel("Saludo:");
		lblSaludo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
		
		saludoArea = new JTextArea();
		scrollPane.setViewportView(saludoArea);
		
		JLabel lblImagen = new JLabel("Imagen:");
		lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.anchor = GridBagConstraints.EAST;
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 3;
		gbc_lblImagen.gridy = 6;
		contentPane.add(lblImagen, gbc_lblImagen);
		
		
		JLabel lblImagen_1 = new JLabel("");
		lblImagen_1.setIcon(new ImageIcon(Registro.class.getResource("/resources/usuario_64.png")));
		GridBagConstraints gbc_lblImagen_1 = new GridBagConstraints();
		gbc_lblImagen_1.gridwidth = 2;
		gbc_lblImagen_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen_1.gridx = 4;
		gbc_lblImagen_1.gridy = 7;
		contentPane.add(lblImagen_1, gbc_lblImagen_1);
		
		imagenField = new JTextField();
		GridBagConstraints gbc_imagenlField = new GridBagConstraints();
		gbc_imagenlField.insets = new Insets(0, 0, 5, 5);
		gbc_imagenlField.fill = GridBagConstraints.HORIZONTAL;
		gbc_imagenlField.gridx = 4;
		gbc_imagenlField.gridy = 6;
		contentPane.add(imagenField, gbc_imagenlField);
		imagenField.setColumns(10);
		
		
		JButton botonImagen = new JButton("Aceptar");
		botonImagen.setBackground(new Color(79, 87, 255));
		botonImagen.setForeground(Color.WHITE);
		botonImagen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		GridBagConstraints gbc_imagenField = new GridBagConstraints();
		gbc_imagenField.insets = new Insets(0, 0, 5, 5);
		gbc_imagenField.gridx = 5;
		gbc_imagenField.gridy = 6;
		
		
		botonImagen.addActionListener(evento -> {
			Image imagen = AppChat.getInstancia().getImagen(imagenField.getText());
		     if(imagen != null) {
		    	 lblImagen_1.setIcon(new ImageIcon(imagen.getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
		     } else {
		    	 JOptionPane.showMessageDialog(this, 
		                 "No se pudo descargar la imagen desde la URL proporcionada.",
		                 "AppChat",
		                 JOptionPane.ERROR_MESSAGE);
		     }
		});
		contentPane.add(botonImagen, gbc_imagenField);
		
		
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 8;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.setBackground(new Color(79, 87, 255));
		botonCancelar.setForeground(Color.WHITE);
		botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		botonCancelar.addActionListener(evento -> {
			this.dispose();
		});
		panel.add(botonCancelar);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.setBackground(new Color(79, 87, 255));
		botonAceptar.setForeground(Color.WHITE);
		botonAceptar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		botonAceptar.addActionListener(evento -> {
			
			
			
			if (validacionCampos() && registroUsuario()) {
	
				 JOptionPane.showMessageDialog(this,"Se ha registrado correctamente","AppChat",JOptionPane.PLAIN_MESSAGE);
				 this.dispose();
				
			}
			
			else {
				 JOptionPane.showMessageDialog(this, 
		                    "Registro fallido: Por favor, revise los campos.",
		                    "AppChat",
		                    JOptionPane.ERROR_MESSAGE);
						
				}
			});
		
		panel.add(botonAceptar);
	}

}