package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import java.awt.Dimension;

public class RegistroCorrecto extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
	
					RegistroCorrecto frame = new RegistroCorrecto();
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
	public RegistroCorrecto() {
		setMinimumSize(new Dimension(300, 200));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 307, 196);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRegistroCorrecto = new JLabel("Se ha registrado correctamente.");
		lblRegistroCorrecto.setFont(new Font("Dialog", Font.BOLD, 15));
		lblRegistroCorrecto.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblRegistroCorrecto, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.setForeground(new Color(255, 255, 255));
		botonAceptar.setBackground(new Color(81, 116, 255));
		botonAceptar.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
		botonAceptar.addActionListener(evento -> {
			this.dispose();
		
		});
		panel.add(botonAceptar, BorderLayout.NORTH);
	}

}
