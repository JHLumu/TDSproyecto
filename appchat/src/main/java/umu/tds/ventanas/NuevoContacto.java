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
import umu.tds.modelos.Usuario;

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
import java.awt.Dimension;


public class NuevoContacto extends JDialog {

	/**
	 * 
	 */
	private Usuario sesionUsuario;
	private static final long serialVersionUID = 1L;
	private JTextField nombreField;
	private JTextField telefonoField;
	private Color colorPrimario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					NuevoContacto window = new NuevoContacto();
					window.setVisible(true);
					window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void nuevoContacto() {

		if ((nombreField.getText().isEmpty()) || (telefonoField.getText().isEmpty())) {
			
			JOptionPane.showMessageDialog(NuevoContacto.this, 
					"Se deben rellenar ambos campos.",
	                "AppChat",
	                JOptionPane.ERROR_MESSAGE);
			 return;
		}
		
		else if (telefonoField.getText().equals(this.sesionUsuario.getTelefono())) {
			
			JOptionPane.showMessageDialog(this, 
                    "No te puedes añadir a tí mismo.",
                    "AppChat",
                    JOptionPane.ERROR_MESSAGE);
		 return;
			
		}
		
		int resultado = AppChat.getInstancia().nuevoContacto(nombreField.getText(),telefonoField.getText());
			
		//Si el resultado es -1 el teléfono no está registrado
		if (resultado == -1) { 

			JOptionPane.showMessageDialog(NuevoContacto.this, 
					"El teléfono no se encuentra registrado.",
					"AppChat",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		
		else if (resultado == 0) {

			JOptionPane.showMessageDialog(NuevoContacto.this,
					"El teléfono ya se encuentra registrado en la lista de contactos.",
					"AppChat",
					JOptionPane.ERROR_MESSAGE);				
		}
		
		
		else {

			JOptionPane.showMessageDialog(NuevoContacto.this, 
					"Se ha registrado el teléfono como " + nombreField.getText(),
					"AppChat",
					JOptionPane.PLAIN_MESSAGE);
			this.dispose();
			
		}
			 
			
		
	}
	
	
	/**
	 * Create the application.
	 */
	public NuevoContacto() {
		super();
		initialize();
	}

	public NuevoContacto(Contacto seleccionado) {
		super();
		initialize();
		this.telefonoField.setText(AppChat.getInstancia().getTelefonoContacto(seleccionado));
		this.telefonoField.setEditable(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.sesionUsuario = AppChat.getInstancia().getUsuarioActual();
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
		
		JLabel lblNewLabel = new JLabel("Introduzca el nombre del contacto y su teléfono.");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 4;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panelCentro.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 3;
		panelCentro.add(lblNombre, gbc_lblNombre);
		
		this.nombreField = new JTextField();
		this.nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GridBagConstraints gbc_nombreField = new GridBagConstraints();
		gbc_nombreField.insets = new Insets(0, 0, 5, 5);
		gbc_nombreField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombreField.gridx = 2;
		gbc_nombreField.gridy = 3;
		panelCentro.add(this.nombreField, gbc_nombreField);
		this.nombreField.setColumns(10);
		
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
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.setBorderPainted(false);
		botonAceptar.setBackground(this.colorPrimario);
		botonAceptar.setForeground(Color.WHITE);
		botonAceptar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		botonAceptar.addActionListener(evento -> nuevoContacto());

		
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.setBorderPainted(false);
		botonCancelar.setBackground(this.colorPrimario);
		botonCancelar.setForeground(Color.WHITE);
		botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		botonCancelar.addActionListener(evento -> {
			this.dispose();
		});
						
		panelSur.add(botonAceptar);
		panelSur.add(botonCancelar);
		
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

}
