package umu.tds.ventanas;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import umu.tds.appchat.AppChat;
import umu.tds.utils.Estado;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoRenderer;

public class ListaContactos extends JDialog implements TDSObserver {

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

		JLabel lblGrupo = new JLabel("Grupo");
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

		JButton btnGrupo = new JButton("Añadir Grupo");
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
		JButton btnContacto = new JButton("Añadir Contacto");
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
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
		Component horizontalGlue = Box.createHorizontalGlue();
		panelSuperior.add(horizontalGlue);
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
	
	public ListaContactos() {
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
