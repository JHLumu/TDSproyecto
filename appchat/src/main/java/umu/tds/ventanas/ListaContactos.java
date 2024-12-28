package umu.tds.ventanas;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import java.awt.Component;
import javax.swing.Box;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.Color;
import javax.swing.border.SoftBevelBorder;

import umu.tds.appchat.AppChat;

import javax.swing.border.BevelBorder;

import javax.swing.JList;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JLabel;

public class ListaContactos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					ListaContactos frame = new ListaContactos();
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
	public ListaContactos() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 899, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setIconImage(Toolkit.getDefaultToolkit().getImage(NuevoContacto.class.getResource("/Resources/chat.png")));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentral = new GridBagLayout();
		gbl_panelCentral.columnWidths = new int[]{183, 0, 0, 0};
		gbl_panelCentral.rowHeights = new int[]{0, 246, 0, 0};
		gbl_panelCentral.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentral.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCentral.setLayout(gbl_panelCentral);
		
		JLabel lblLista = new JLabel("Lista de Contactos");
		lblLista.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblLista = new GridBagConstraints();
		gbc_lblLista.insets = new Insets(0, 0, 5, 5);
		gbc_lblLista.gridx = 0;
		gbc_lblLista.gridy = 0;
		panelCentral.add(lblLista, gbc_lblLista);
		
		JLabel lblGrupo = new JLabel("Grupo");
		lblGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblGrupo = new GridBagConstraints();
		gbc_lblGrupo.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrupo.gridx = 1;
		gbc_lblGrupo.gridy = 0;
		panelCentral.add(lblGrupo, gbc_lblGrupo);
		
		
		JList<String> list = new JList<String>();
		list.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		DefaultListModel<String> listaContactos = new DefaultListModel<String>();
		
		for (String nombreContacto : AppChat.getInstancia().obtenerListaContactos()) listaContactos.addElement(nombreContacto);
		list.setModel(listaContactos);
		
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		panelCentral.add(list, gbc_list);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		panelCentral.add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JButton btnContacto = new JButton("Añadir Contacto");
		GridBagConstraints gbc_btnContacto = new GridBagConstraints();
		gbc_btnContacto.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnContacto.insets = new Insets(0, 0, 0, 5);
		gbc_btnContacto.gridx = 0;
		gbc_btnContacto.gridy = 2;
		panelCentral.add(btnContacto, gbc_btnContacto);
		btnContacto.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnContacto.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnContacto.setForeground(new Color(255, 255, 255));
		btnContacto.setBackground(new Color(81, 116, 255));
		btnContacto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		JButton btnGrupo = new JButton("Añadir Grupo");
		GridBagConstraints gbc_btnGrupo = new GridBagConstraints();
		gbc_btnGrupo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGrupo.insets = new Insets(0, 0, 0, 5);
		gbc_btnGrupo.gridx = 1;
		gbc_btnGrupo.gridy = 2;
		panelCentral.add(btnGrupo, gbc_btnGrupo);
		btnGrupo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnGrupo.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnGrupo.setForeground(new Color(255, 255, 255));
		btnGrupo.setBackground(new Color(81, 116, 255));
		btnGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnContacto.addActionListener(evento -> {
			
			NuevoContacto frame = new NuevoContacto();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			
			
		});
		
		JPanel panelInferior = new JPanel();
		contentPane.add(panelInferior, BorderLayout.SOUTH);
		
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelSuperior.add(horizontalGlue);
		
	}

}
