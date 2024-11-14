package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.JButton;

public class RegistroFallido extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroFallido frame = new RegistroFallido();
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
	public RegistroFallido() {
		setResizable(false);
		setMaximumSize(new Dimension(510, 200));
		setMinimumSize(new Dimension(510, 200));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 507, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resources/chat.png")));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		
		
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRegistroFallido = new JLabel("Registro fallido: No se han inicializado correctamente los campos");
		lblRegistroFallido.setFont(new Font("Dialog", Font.BOLD, 15));
		lblRegistroFallido.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegistroFallido.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblRegistroFallido, BorderLayout.CENTER);
		
		JPanel panelBoton = new JPanel();
		contentPane.add(panelBoton, BorderLayout.SOUTH);
		panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.setForeground(new Color(255, 255, 255));
		botonAceptar.setBackground(new Color(81, 116, 255));
		botonAceptar.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		botonAceptar.addActionListener(evento -> {
			this.dispose();	
		});
		panelBoton.add(botonAceptar);
		
	}

}
