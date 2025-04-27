package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoRenderer;
import umu.tds.utils.Estado;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.List;
import java.awt.Dimension;


public class GestionarGrupo extends JDialog implements TDSObserver {

	private static final long serialVersionUID = 1L;
	protected static final String ContactoIndividual = null;
	private JPanel contentPane;
	private DefaultListModel<Contacto> listaContactos;
	private Color colorPrimario;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				ListaContactos frame = new ListaContactos();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	

	private void inicializacionPanelCentral() {
		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentral = new GridBagLayout();
		gbl_panelCentral.columnWidths = new int[]{183, 0, 0, 0};
		gbl_panelCentral.rowHeights = new int[]{0, 246, 0, 0};
		gbl_panelCentral.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentral.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCentral.setLayout(gbl_panelCentral);

		JLabel lblLista = new JLabel("Lista de Contactos");
		lblLista.setFont(new Font("Segoe UI", Font.BOLD, 16));
		GridBagConstraints gbc_lblLista = new GridBagConstraints();
		gbc_lblLista.insets = new Insets(0, 0, 5, 5);
		gbc_lblLista.gridx = 0;
		gbc_lblLista.gridy = 0;
		panelCentral.add(lblLista, gbc_lblLista);

		JLabel lblGrupo = new JLabel("Miembros");
		lblGrupo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		GridBagConstraints gbc_lblGrupo = new GridBagConstraints();
		gbc_lblGrupo.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrupo.gridx = 1;
		gbc_lblGrupo.gridy = 0;
		panelCentral.add(lblGrupo, gbc_lblGrupo);

		JList<Contacto> list = inicializacionListaContactos();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		panelCentral.add(list, gbc_list);

		JList<?> list_1 = new JList<>();
		list_1.setBackground(Color.WHITE);
		list_1.setBorder(new LineBorder(Color.BLACK));
		GridBagConstraints gbc_list_1 = new GridBagConstraints();
		gbc_list_1.gridwidth = 2;
		gbc_list_1.insets = new Insets(0, 0, 5, 5);
		gbc_list_1.fill = GridBagConstraints.BOTH;
		gbc_list_1.gridx = 1;
		gbc_list_1.gridy = 1;
		panelCentral.add(list_1, gbc_list_1);

		JButton btnContacto = inicializacionBotonContacto();
		GridBagConstraints gbc_btnContacto = new GridBagConstraints();
		gbc_btnContacto.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnContacto.insets = new Insets(0, 0, 0, 5);
		gbc_btnContacto.gridx = 0;
		gbc_btnContacto.gridy = 2;
		panelCentral.add(btnContacto, gbc_btnContacto);

		JButton btnGrupo = new JButton("Eliminar");
		GridBagConstraints gbc_btnGrupo = new GridBagConstraints();
		gbc_btnGrupo.gridwidth = 2;
		gbc_btnGrupo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGrupo.insets = new Insets(0, 0, 0, 5);
		gbc_btnGrupo.gridx = 1;
		gbc_btnGrupo.gridy = 2;
		panelCentral.add(btnGrupo, gbc_btnGrupo);
		btnGrupo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnGrupo.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnGrupo.setForeground(Color.WHITE);
		btnGrupo.setBackground(this.colorPrimario);
		btnGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
	}

	private JList<Contacto> inicializacionListaContactos() {
		JList<Contacto> list = new JList<>();
		list.setBackground(Color.WHITE);
		list.setBorder(new LineBorder(Color.BLACK));
		listaContactos = new DefaultListModel<>();
		actualizarListaContactos();
		list.setCellRenderer(new ContactoRenderer());
		list.setModel(listaContactos);
		return list;
	}

	private JButton inicializacionBotonContacto() {
		JButton btnContacto = new JButton("Añadir");
		btnContacto.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnContacto.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnContacto.setForeground(Color.WHITE);
		btnContacto.setBackground(this.colorPrimario);
		btnContacto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnContacto.addActionListener(evento -> {
			NuevoContacto frame = new NuevoContacto();
			frame.setModal(true);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});
		return btnContacto;
	}

	private void inicializacionPanelSuperior() {
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		JPanel contenidoSuperior = new JPanel();
		panelSuperior.add(contenidoSuperior, BorderLayout.NORTH);
		GridBagLayout gbl_contenidoSuperior = new GridBagLayout();
		gbl_contenidoSuperior.columnWidths = new int[]{20, 0, 20, 0};
		gbl_contenidoSuperior.rowHeights = new int[]{20, 0, 0, 0, 20, 0};
		gbl_contenidoSuperior.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contenidoSuperior.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contenidoSuperior.setLayout(gbl_contenidoSuperior);
		
		JLabel nombreGrupo = new JLabel("Grupo");
		nombreGrupo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		GridBagConstraints gbc_nombreGrupo = new GridBagConstraints();
		gbc_nombreGrupo.insets = new Insets(0, 0, 5, 5);
		gbc_nombreGrupo.gridx = 1;
		gbc_nombreGrupo.gridy = 1;
		contenidoSuperior.add(nombreGrupo, gbc_nombreGrupo);
		
		JLabel imagenPerfil = new JLabel("New label");
		GridBagConstraints gbc_imagenPerfil = new GridBagConstraints();
		gbc_imagenPerfil.insets = new Insets(0, 0, 5, 5);
		gbc_imagenPerfil.gridx = 1;
		gbc_imagenPerfil.gridy = 2;
		contenidoSuperior.add(imagenPerfil, gbc_imagenPerfil);
		
		JButton btnNewButton = new JButton("Editar");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 3;
		contenidoSuperior.add(btnNewButton, gbc_btnNewButton);
	}

	private void inicializacionPanelInferior() {
		JPanel panelInferior = new JPanel();
		contentPane.add(panelInferior, BorderLayout.SOUTH);
	}

	@Override
	public void update(TDSObservable o, Object arg) {
		if (arg instanceof Estado) {
			Estado estadoActual = (Estado) arg;
			System.out.println("[DEBUG ListaContactos update]: Estado recibido en ListaContactos: " + estadoActual);
			if (estadoActual.equals(Estado.INFO_CONTACTO)) {
				System.out.println("[DEBUG ListaContactos update]: Actualizando contacto");
				this.actualizarListaContactos();
			}
		}
	}

	private void actualizarListaContactos() {
		listaContactos.clear();
		List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactosIndividuales();
		for (Contacto contacto : contactos) {
			listaContactos.addElement(contacto);
		}
	}

	@Override
	public void dispose() {
		AppChat.getInstancia().deleteObserver(Estado.INFO_CONTACTO, this);
		super.dispose();
	}
	
	public GestionarGrupo() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 899, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		this.colorPrimario = AppChat.getInstancia().getColorGUI(1);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		inicializacionPanelCentral();
		inicializacionPanelSuperior();
		inicializacionPanelInferior();
		AppChat.getInstancia().addObserver(Estado.INFO_CONTACTO, this);
	}
	

}
