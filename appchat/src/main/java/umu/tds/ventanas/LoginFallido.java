package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;

public class LoginFallido extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFallido frame = new LoginFallido();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFallido() {
		setMinimumSize(new Dimension(450, 300));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(243, 13));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelCentro = new JPanel();
		panelCentro.setMinimumSize(new Dimension(243, 13));
		contentPane.add(panelCentro);
		panelCentro.setLayout(new BorderLayout(0, 0));
		
		JLabel loginIncorrecto = new JLabel("Login fallido: Teléfono y/o contraseña son incorrectos");
		loginIncorrecto.setFont(new Font("Dialog", Font.BOLD, 15));
		loginIncorrecto.setHorizontalAlignment(SwingConstants.CENTER);
		panelCentro.add(loginIncorrecto);
	}

}
