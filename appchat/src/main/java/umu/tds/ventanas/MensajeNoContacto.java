package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.UIManager;


import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Optional;
import java.awt.Dimension;


public class MensajeNoContacto extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField mensajeField;
	private JTextField telefonoField;
	private Color colorPrimario;
	private int i;
	private Principal framePrincipal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					MensajeNoContacto window = new MensajeNoContacto();
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
	public MensajeNoContacto() {
		super();
		initialize();
	}

	public MensajeNoContacto(Principal frame) {
		super();
		this.framePrincipal = frame;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.colorPrimario = AppChat.getInstancia().getColorGUI(1);
		setResizable(false);
		setMinimumSize(new Dimension(500, 355));
		getContentPane().setMinimumSize(new Dimension(500, 350));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
		setForeground(new Color(0, 0, 0));
		
		setTitle("AppChat");
		setBackground(new Color(255, 255, 255));
		getContentPane().setBackground(new Color(255, 255, 255));
		setBounds(100, 100, 363, 239);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(null);
		panelCentro.setBackground(new Color(255, 255, 253));
		getContentPane().add(panelCentro, BorderLayout.CENTER);
		
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{126, 0, 153, 101, 0};
		gbl_panelCentro.rowHeights = new int[]{0, 0, 84, 0, 0, 15, 15, 0};
		gbl_panelCentro.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		JLabel lblNewLabel = new JLabel("Introduzca el teléfono del usuario.");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 4;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panelCentro.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblTelefono = new JLabel("Teléfono");
		lblTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.anchor = GridBagConstraints.WEST;
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 4;
		panelCentro.add(lblTelefono, gbc_lblTelefono);
		
		this.telefonoField = new JTextField();
		this.telefonoField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_telefonoField = new GridBagConstraints();
		gbc_telefonoField.insets = new Insets(0, 0, 5, 5);
		gbc_telefonoField.fill = GridBagConstraints.HORIZONTAL;
		gbc_telefonoField.gridx = 2;
		gbc_telefonoField.gridy = 4;
		panelCentro.add(telefonoField, gbc_telefonoField);
	
		
		JPanel panelSur = new JPanel();
		panelSur.setBackground(new Color(255, 255, 253));
		getContentPane().add(panelSur, BorderLayout.SOUTH);
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setBorderPainted(false);
		btnAceptar.setBackground(this.colorPrimario);
		btnAceptar.setForeground(Color.WHITE);
		btnAceptar.setFont(new Font("Segoe UI", i = Font.PLAIN, 14));
		btnAceptar.addActionListener(evento -> {
			if (!this.telefonoField.getText().isEmpty()) nuevoContacto(this.telefonoField.getText());
		});
		
						
		panelSur.add(btnAceptar);
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(255, 255, 253));
		this.getContentPane().add(panelNorte, BorderLayout.NORTH);
		
		JPanel panelEste = new JPanel();
		panelEste.setBackground(new Color(255, 255, 253));
		this.getContentPane().add(panelEste, BorderLayout.EAST);
		
		JPanel panelOeste = new JPanel();
		panelOeste.setBackground(new Color(255, 255, 253));
		this.getContentPane().add(panelOeste, BorderLayout.WEST);
		
		
		
		

		
	}


	private void nuevoContacto(String telf) {
		Contacto contacto = AppChat.getInstancia().nuevoContacto(telf);
		if (!(contacto == null)) {
			
			 JOptionPane.showMessageDialog(this, 
		                "El teléfono introducido no está registrado", 
		                "AppChat", 
		                JOptionPane.ERROR_MESSAGE);
			
		}
		this.framePrincipal.setContactoSeleccionado(contacto);
		this.dispose();
	}

}
