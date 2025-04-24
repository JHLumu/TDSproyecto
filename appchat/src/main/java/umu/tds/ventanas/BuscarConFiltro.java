package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import umu.tds.appchat.AppChat;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.JList;

public class BuscarConFiltro extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtTexto;
	private JTextField txtTelfono;
	private JTextField txtContacto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					BuscarConFiltro window = new BuscarConFiltro();
					window.setVisible(true);
					window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public BuscarConFiltro() {
		setSize(350, 500);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
		setTitle("Buscar");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorte = new JPanel();
		getContentPane().add(panelNorte, BorderLayout.NORTH);
		GridBagLayout gbl_panelNorte = new GridBagLayout();
		gbl_panelNorte.columnWidths = new int[]{20, 0, 5, 0, 5, 0, 20, 0};
		gbl_panelNorte.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0};
		gbl_panelNorte.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelNorte.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelNorte.setLayout(gbl_panelNorte);
		
		JLabel lblNewLabel = new JLabel("Texto");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		panelNorte.add(lblNewLabel, gbc_lblNewLabel);
		
		txtTexto = new JTextField();
		GridBagConstraints gbc_txtTexto = new GridBagConstraints();
		gbc_txtTexto.gridwidth = 5;
		gbc_txtTexto.insets = new Insets(0, 0, 5, 5);
		gbc_txtTexto.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTexto.gridx = 1;
		gbc_txtTexto.gridy = 2;
		panelNorte.add(txtTexto, gbc_txtTexto);
		txtTexto.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Teléfono");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		panelNorte.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Contacto");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 3;
		gbc_lblNewLabel_2.gridy = 3;
		panelNorte.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		txtTelfono = new JTextField();
		GridBagConstraints gbc_txtTelfono = new GridBagConstraints();
		gbc_txtTelfono.insets = new Insets(0, 0, 5, 5);
		gbc_txtTelfono.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTelfono.gridx = 1;
		gbc_txtTelfono.gridy = 4;
		panelNorte.add(txtTelfono, gbc_txtTelfono);
		txtTelfono.setColumns(10);
		
		txtContacto = new JTextField();
		GridBagConstraints gbc_txtContacto = new GridBagConstraints();
		gbc_txtContacto.insets = new Insets(0, 0, 5, 5);
		gbc_txtContacto.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContacto.gridx = 3;
		gbc_txtContacto.gridy = 4;
		panelNorte.add(txtContacto, gbc_txtContacto);
		txtContacto.setColumns(10);
		
		JButton btnNewButton = new JButton("Buscar");
		btnNewButton.setIcon(new ImageIcon(new ImageIcon(BuscarConFiltro.class.getResource("/Resources/lupa.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 5;
		gbc_btnNewButton.gridy = 4;
		panelNorte.add(btnNewButton, gbc_btnNewButton);
		
		JPanel panelCentro = new JPanel();
		getContentPane().add(panelCentro, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{20, 218, 20, 0};
		gbl_panelCentro.rowHeights = new int[]{10, 1, 20, 0};
		gbl_panelCentro.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		JList list = new JList();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 1;
		panelCentro.add(list, gbc_list);
	}

	
	
}
