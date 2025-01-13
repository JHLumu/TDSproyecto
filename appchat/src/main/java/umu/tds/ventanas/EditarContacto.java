package umu.tds.ventanas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class EditarContacto extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblNombre;
	
	public EditarContacto(String nombre, Icon icon, String telf, String saludo) {
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{20, 85, 0, 0, 20, 0};
		gbl_panel.rowHeights = new int[]{20, 0, 0, 0, 0, 20, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblNombre = new JLabel(nombre);
		lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 2;
		gbc_lblNombre.gridy = 1;
		panel.add(lblNombre, gbc_lblNombre);
		
		JButton btnEditar = new JButton("Editar");
		GridBagConstraints gbc_btnEditar = new GridBagConstraints();
		gbc_btnEditar.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditar.gridx = 3;
		gbc_btnEditar.gridy = 1;
		panel.add(btnEditar, gbc_btnEditar);
		
		JLabel lblImagen = new JLabel();
		lblImagen.setIcon(icon);
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 2;
		gbc_lblImagen.gridy = 2;
		panel.add(lblImagen, gbc_lblImagen);
		
		JLabel lblTelf = new JLabel(telf);
		lblTelf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblTelf.setForeground(new Color(25, 25, 112));
		GridBagConstraints gbc_lblTelf = new GridBagConstraints();
		gbc_lblTelf.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelf.gridx = 2;
		gbc_lblTelf.gridy = 3;
		panel.add(lblTelf, gbc_lblTelf);
		
		JLabel lblSaludo = new JLabel(saludo);
		lblSaludo.setBackground(new Color(200, 207, 251));
		lblSaludo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_lblSaludo = new GridBagConstraints();
		gbc_lblSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaludo.gridx = 2;
		gbc_lblSaludo.gridy = 4;
		panel.add(lblSaludo, gbc_lblSaludo);
	}

}
