package umu.tds.ventanas;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import umu.tds.modelos.Grupo;

public class ListaContactos extends JDialog implements TDSObserver {

	private static final long serialVersionUID = 1L;
	protected static final String ContactoIndividual = null;
	private JPanel contentPane;
	private DefaultListModel<Contacto> listaContactosIndividuales;
	private DefaultListModel<Contacto> listaContactosGrupos;
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

		JList<Contacto> list = inicializacionListaIndividual();
		GridBagConstraints gbc_listaIndividual = new GridBagConstraints();
		gbc_listaIndividual.insets = new Insets(0, 0, 5, 5);
		gbc_listaIndividual.fill = GridBagConstraints.BOTH;
		gbc_listaIndividual.gridx = 0;
		gbc_listaIndividual.gridy = 1;
		panelCentral.add(list, gbc_listaIndividual);

		JList<Contacto> listaGrupal = inicializacionListaGrupos();
		GridBagConstraints gbc_listaGrupal = new GridBagConstraints();
		gbc_listaGrupal.gridwidth = 2;
		gbc_listaGrupal.insets = new Insets(0, 0, 5, 5);
		gbc_listaGrupal.fill = GridBagConstraints.BOTH;
		gbc_listaGrupal.gridx = 1;
		gbc_listaGrupal.gridy = 1;
		panelCentral.add(listaGrupal, gbc_listaGrupal);

		JButton btnContacto = inicializacionBotonContacto();
		GridBagConstraints gbc_btnContacto = new GridBagConstraints();
		gbc_btnContacto.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnContacto.insets = new Insets(0, 0, 0, 5);
		gbc_btnContacto.gridx = 0;
		gbc_btnContacto.gridy = 2;
		panelCentral.add(btnContacto, gbc_btnContacto);

		JButton btnGrupo = inicializacionBotonGrupo();
		GridBagConstraints gbc_btnGrupo = new GridBagConstraints();
		gbc_btnGrupo.gridwidth = 2;
		gbc_btnGrupo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGrupo.insets = new Insets(0, 0, 0, 5);
		gbc_btnGrupo.gridx = 1;
		gbc_btnGrupo.gridy = 2;
		panelCentral.add(btnGrupo, gbc_btnGrupo);
		
		
	}

	private JList<Contacto> inicializacionListaIndividual() {
		JList<Contacto> listaIndividual = new JList<>();
		listaIndividual.setBackground(Color.WHITE);
		listaIndividual.setBorder(new LineBorder(Color.BLACK));
		listaContactosIndividuales = new DefaultListModel<>();
		actualizarListaIndividual();
		listaIndividual.setCellRenderer(new ContactoRenderer());
		listaIndividual.setModel(listaContactosIndividuales);
		return listaIndividual;
	}
	
	private JList<Contacto> inicializacionListaGrupos() {
		JList<Contacto> listaGrupos = new JList<>();
		listaGrupos.setBackground(Color.WHITE);
		listaGrupos.setBorder(new LineBorder(Color.BLACK));
		listaContactosGrupos = new DefaultListModel<>();
		actualizarListaGrupos();
		listaGrupos.setCellRenderer(new ContactoRenderer());
		listaGrupos.setModel(listaContactosGrupos);
		listaGrupos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	int index = listaGrupos.locationToIndex(e.getPoint());
		        if (index != -1) {
			    	Contacto seleccionado = listaGrupos.getSelectedValue();
			        
			        if(e.getClickCount() == 2 && seleccionado instanceof Grupo && seleccionado != null){
			        	GestionarGrupo frame = new GestionarGrupo(seleccionado);
						frame.setModal(true);
						frame.setVisible(true);
						frame.setLocationRelativeTo(null);
			        }
		        }
		    }
		});
		return listaGrupos;
	}

	private JButton inicializacionBotonGrupo() {
		JButton btnGrupo = new JButton("Añadir Grupo");
		btnGrupo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnGrupo.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnGrupo.setForeground(Color.WHITE);
		btnGrupo.setBackground(this.colorPrimario);
		btnGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnGrupo.addActionListener(evento -> {
			NuevoGrupo frame = new NuevoGrupo();
			frame.setModal(true);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});
		return btnGrupo;
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
		actualizarListaIndividual();
		actualizarListaGrupos();
	}
	private void actualizarListaIndividual() {
		listaContactosIndividuales.clear();
		List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactosIndividuales();
		for (Contacto contacto : contactos) {
			listaContactosIndividuales.addElement(contacto);
		}
	}
	
	private void actualizarListaGrupos() {
		listaContactosGrupos.clear();
		List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactosGrupo();
		for (Contacto contacto : contactos) {
			listaContactosGrupos.addElement(contacto);
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
