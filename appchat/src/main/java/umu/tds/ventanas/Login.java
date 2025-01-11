package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import umu.tds.appchat.AppChat;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Login extends JFrame{

	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				    Login window = new Login();
					window.setVisible(true);
					window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		getContentPane().setBackground(new Color(255, 255, 255));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setMinimumSize(new Dimension(500, 355));
		setBackground(Color.WHITE);
		setForeground(Color.WHITE);
		getContentPane().setMinimumSize(new Dimension(500, 350));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resources/chat.png")));
		
		setTitle("AppChat");
		setBounds(100, 100, 500, 352);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		JPanel panelCentro = new JPanel();
		panelCentro.setBackground(new Color(255, 255, 255));
		getContentPane().add(panelCentro, BorderLayout.CENTER);
	
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{126, 0, 153, 101, 0};
		gbl_panelCentro.rowHeights = new int[]{36, 0, 0, 30, 0, 15, 40, 0};
		gbl_panelCentro.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		JLabel labelTelf = new JLabel("Teléfono");
		labelTelf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_labelTelf = new GridBagConstraints();
		gbc_labelTelf.anchor = GridBagConstraints.WEST;
		gbc_labelTelf.insets = new Insets(0, 0, 5, 5);
		gbc_labelTelf.gridx = 1;
		gbc_labelTelf.gridy = 1;
		panelCentro.add(labelTelf, gbc_labelTelf);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		panelCentro.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel labelPass = new JLabel("Contraseña");
		labelPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_labelPass = new GridBagConstraints();
		gbc_labelPass.anchor = GridBagConstraints.WEST;
		gbc_labelPass.insets = new Insets(0, 0, 5, 5);
		gbc_labelPass.gridx = 1;
		gbc_labelPass.gridy = 2;
		panelCentro.add(labelPass, gbc_labelPass);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 2;
		panelCentro.add(passwordField, gbc_passwordField);
		
		JButton botonLogin = new JButton("Login");
		botonLogin.setForeground(new Color(255, 255, 255));
		botonLogin.setBackground(new Color(81, 116, 255));
		botonLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		botonLogin.addActionListener(evento -> {
			//Falta comprobar que los datos sean correctos e invocar el metodo del Controlador
			//Si el login es correcto
			if (AppChat.getInstancia().iniciarSesionUsuario(textField.getText(), new String(passwordField.getPassword()))){
				this.dispose();
				Principal frame = new Principal();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
			
			//Si el login falla
			else {
				
				 JOptionPane.showMessageDialog(this, 
		                    "Login Fallido",
		                    "AppChat",
		                    JOptionPane.ERROR_MESSAGE);
			}
			
		});

		GridBagConstraints gbc_botonLogin = new GridBagConstraints();
		gbc_botonLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_botonLogin.anchor = GridBagConstraints.NORTH;
		gbc_botonLogin.gridwidth = 2;
		gbc_botonLogin.insets = new Insets(0, 0, 5, 5);
		gbc_botonLogin.gridx = 1;
		gbc_botonLogin.gridy = 4;
		panelCentro.add(botonLogin, gbc_botonLogin);
		
		JPanel panelSur = new JPanel();
		panelSur.setBackground(new Color(255, 255, 255));
		getContentPane().add(panelSur, BorderLayout.SOUTH);
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel labelRegistro = new JLabel("¿No tienes una cuenta aún?");
		labelRegistro.setFont(new Font("Gill Sans MT", Font.PLAIN, 10));
		panelSur.add(labelRegistro);
		
		JButton botonRegistro = new JButton("Regístrate");
		botonRegistro.setBorderPainted(false);
		botonRegistro.setFont(new Font("Gill Sans MT", Font.BOLD, 10));
		botonRegistro.setBackground(new Color(255, 255, 255));
		//Acciones de los botones
		//Al pulsar el boton Registro, se abre la ventana Registro
		botonRegistro.addActionListener(evento -> {
			Registro frame = new Registro();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});
				
		
		panelSur.add(botonRegistro);
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(255, 255, 255));
		getContentPane().add(panelNorte, BorderLayout.NORTH);
		
		JLabel nombreAPP = new JLabel("AppChat");
		nombreAPP.setForeground(new Color(81, 116, 255));
		nombreAPP.setBackground(new Color(255, 255, 253));
		nombreAPP.setFont(new Font("Gill Sans Ultra Bold", Font.PLAIN, 35));
		panelNorte.add(nombreAPP);
		
		JPanel panelEste = new JPanel();
		panelEste.setBackground(new Color(255, 255, 255));
		getContentPane().add(panelEste, BorderLayout.EAST);
		
		JPanel panelOeste = new JPanel();
		panelOeste.setBackground(new Color(255, 255, 255));
		getContentPane().add(panelOeste, BorderLayout.WEST);
		
		
		
		
		
		
		
		
	}

}
