package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JList;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoRenderer;
import umu.tds.modelos.Grupo;
import umu.tds.utils.Estado;
import umu.tds.utils.ImagenUtils;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class GestionarGrupo extends JDialog implements TDSObserver {

	private static final long serialVersionUID = 1L;
	protected static final String ContactoIndividual = null;
	private final AppChat controlador;
	private JPanel contentPane;
	private DefaultListModel<Contacto> listaContactos;
	private DefaultListModel<Contacto> listaMiembros;
	private Grupo grupo;

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
		gbl_panelCentral.rowHeights = new int[]{0, 246, 0};
		gbl_panelCentral.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentral.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
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
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		panelCentral.add(list, gbc_list);

		JList<Contacto> list_1 = inicializacionListaMiembros();
		list_1.setBackground(Color.WHITE);
		list_1.setBorder(new LineBorder(Color.BLACK));
		GridBagConstraints gbc_list_1 = new GridBagConstraints();
		gbc_list_1.gridwidth = 2;
		gbc_list_1.fill = GridBagConstraints.BOTH;
		gbc_list_1.gridx = 1;
		gbc_list_1.gridy = 1;
		panelCentral.add(list_1, gbc_list_1);
		
	}

	private JList<Contacto> inicializacionListaContactos() {
		JList<Contacto> list = new JList<>();
		list.setBackground(Color.WHITE);
		list.setBorder(new LineBorder(Color.BLACK));
		listaContactos = new DefaultListModel<>();
		actualizarListaContactos();
		list.setCellRenderer(new ContactoRenderer());
		list.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int index = list.locationToIndex(e.getPoint());
		        if (index != -1) {
		        	if(e.getClickCount() == 2) {
		        		Contacto seleccionado = list.getModel().getElementAt(index);
			            System.out.println("\n[DEBUG GestionGrupos eliminarMiembro]: Miembro -> " + seleccionado);
			            controlador.nuevoMiembroGrupo(GestionarGrupo.this.grupo, seleccionado);
		        	}
		            
		        }
		    }
		});
		list.setModel(listaContactos);
		return list;
	}
	
	private JList<Contacto> inicializacionListaMiembros() {
		JList<Contacto> list = new JList<>();
		list.setBackground(Color.WHITE);
		list.setBorder(new LineBorder(Color.BLACK));
		listaMiembros = new DefaultListModel<>();
		actualizarListaMiembros();
		list.setCellRenderer(new ContactoRenderer());
		list.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int index = list.locationToIndex(e.getPoint());
		        if (index != -1) {
		        	if(e.getClickCount() == 2) {
		        		Contacto seleccionado = list.getModel().getElementAt(index);
			            System.out.println("\n[DEBUG GestionGrupos eliminarMiembro]: Miembro -> " + seleccionado);
			            controlador.eliminarMiembroGrupo(GestionarGrupo.this.grupo, seleccionado);
		        	}
		            
		        }
		    }
		});
		list.setModel(listaMiembros);
		return list;
	}


	private void inicializacionPanelSuperior(Grupo seleccionado) {
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
		
		JLabel nombreGrupo = new JLabel(seleccionado.getNombre());
		nombreGrupo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		GridBagConstraints gbc_nombreGrupo = new GridBagConstraints();
		gbc_nombreGrupo.insets = new Insets(0, 0, 5, 5);
		gbc_nombreGrupo.gridx = 1;
		gbc_nombreGrupo.gridy = 1;
		contenidoSuperior.add(nombreGrupo, gbc_nombreGrupo);
		
		
		
		JLabel imagenPerfil = new JLabel("");
		GridBagConstraints gbc_imagenPerfil = new GridBagConstraints();
		gbc_imagenPerfil.insets = new Insets(0, 0, 5, 5);
		gbc_imagenPerfil.gridx = 1;
		gbc_imagenPerfil.gridy = 2;
		
		
		Image localImage = ImagenUtils.getImagen(seleccionado);
		if (localImage != null) imagenPerfil.setIcon(new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
			
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
				this.actualizarListaMiembros();
			}
		}
	}

	private void actualizarListaContactos() {
		listaContactos.clear();
		List<Contacto> contactos = this.controlador.obtenerListaContactosIndividuales();
		List<Contacto> miembros = this.controlador.obtenerListaMiembrosGrupo(this.grupo);
		for (Contacto contacto : contactos) {
			if (!miembros.contains(contacto))
				listaContactos.addElement(contacto);
		}
	}
	
	private void actualizarListaMiembros() {
		listaMiembros.clear();
		List<Contacto> miembros = this.controlador.obtenerListaMiembrosGrupo(this.grupo);
		for (Contacto miembro : miembros) {
			listaMiembros.addElement(miembro);
		}
	}

	@Override
	public void dispose() {
		this.controlador.deleteObserver(Estado.INFO_CONTACTO, this);
		super.dispose();
	}
	
	public GestionarGrupo(Contacto grupo) {
		this.grupo=(Grupo) grupo;
		this.controlador = AppChat.getInstancia();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 899, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(this.controlador.getURLIcon())));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		inicializacionPanelCentral();
		inicializacionPanelSuperior((Grupo)grupo);
		inicializacionPanelInferior();
		this.controlador.addObserver(Estado.INFO_CONTACTO, this);
	}
	

}
