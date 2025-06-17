package umu.tds.ventanas;


import javax.swing.JFrame;
import javax.swing.JTextField;

import umu.tds.appchat.AppChat;
import umu.tds.appchat.servicios.mensajes.MensajeCoincidencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoMensajeRenderer;
import umu.tds.modelos.Mensaje;
import umu.tds.utils.BuscarFiltroListener;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

public class BuscarConFiltro extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtTexto;
	private JTextField txtTelfono;
	private JTextField txtContacto;
	private JList<MensajeCoincidencia> list;
	private DefaultListModel<MensajeCoincidencia> modeloLista;
	private BuscarFiltroListener buscarFiltroListener;
	
	public BuscarConFiltro(BuscarFiltroListener listener) {
		this.buscarFiltroListener = listener;
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
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(new ImageIcon(BuscarConFiltro.class.getResource("/Resources/lupa.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_btnBuscar = new GridBagConstraints();
		gbc_btnBuscar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBuscar.gridx = 5;
		gbc_btnBuscar.gridy = 4;
		panelNorte.add(btnBuscar, gbc_btnBuscar);
		
		JPanel panelCentro = new JPanel();
		getContentPane().add(panelCentro, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{20, 218, 20, 0};
		gbl_panelCentro.rowHeights = new int[]{10, 1, 20, 0};
		gbl_panelCentro.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		// Inicializar modelo y lista
		modeloLista = new DefaultListModel<MensajeCoincidencia>();
		list = new JList<MensajeCoincidencia>(modeloLista);
		
		JScrollPane scrollPane = new JScrollPane(list);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		panelCentro.add(scrollPane, gbc_scrollPane);
		
		// Configurar el botón de búsqueda
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarBusqueda();
			}
		});
		
		// Configurar el listener para doble clic en la lista
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					MensajeCoincidencia seleccionado = list.getSelectedValue();
					if (seleccionado != null) {
						abrirSeleccionado(seleccionado);
					}
				}
			}
		});
	}
	

	
	/**
	 * Realiza la búsqueda según los filtros ingresados
	 */
	private void realizarBusqueda() {
		String textoFiltro = txtTexto.getText().trim();
		String telefonoFiltro = txtTelfono.getText().trim();
		String nombreContactoFiltro = txtContacto.getText().trim();
		
		// Determinar el tipo de búsqueda basado en los filtros proporcionados
		boolean buscarPorTexto = !textoFiltro.isEmpty();
		boolean buscarPorTelefono = !telefonoFiltro.isEmpty();
		boolean buscarPorNombre = !nombreContactoFiltro.isEmpty();
		
		if (!buscarPorTexto && !buscarPorTelefono && !buscarPorNombre) {
			// Sin filtros, mostrar todos los mensajes ordenados por contacto y luego por fecha
			mostrarTodosMensajes();
			return;
		}
		buscarMensajes(textoFiltro, buscarPorNombre ? nombreContactoFiltro : null, buscarPorTelefono ? telefonoFiltro : null);
		/*
		if (buscarPorTexto) {
			// Búsqueda de mensajes que contienen el texto
			buscarMensajes(textoFiltro, buscarPorNombre ? nombreContactoFiltro : null, buscarPorTelefono ? telefonoFiltro : null);
		} else {
			// Búsqueda de contactos por nombre y/o teléfono
			buscarContactos(nombreContactoFiltro, telefonoFiltro);
		}
		*/
	}
	/**
	 * Muestra todos los mensajes ordenados primero por contacto y luego por fecha
	 */
	private void mostrarTodosMensajes() {
	    modeloLista.clear();
	    list.setCellRenderer(new ContactoMensajeRenderer());
	    
	    List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactos();
	    List<MensajeCoincidencia> todosMensajes = new ArrayList<>();
	    
	    // Recopilar todos los mensajes de todos los contactos
	    for (Contacto contacto : contactos) {
	        List<Mensaje> mensajesContacto = AppChat.getInstancia().obtenerChatContacto(contacto);
	        if (mensajesContacto != null && !mensajesContacto.isEmpty()) {
	            for (Mensaje mensaje : mensajesContacto) {
	                todosMensajes.add(new MensajeCoincidencia(mensaje, contacto));
	            }
	        }
	    }
	    
	    if (todosMensajes.isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	                "No hay mensajes disponibles", 
	                "Sin mensajes", 
	                JOptionPane.INFORMATION_MESSAGE);
	    } else {
	        // Ordenar los mensajes por contacto y luego por fecha
	        todosMensajes.sort(Comparator
	                .comparing((MensajeCoincidencia mc) -> mc.getContacto().getNombre())
	                .thenComparing(mc -> mc.getMensaje().getFechaEnvio()));
	        
	        // Agregar todos los mensajes ordenados a la lista
	        for (MensajeCoincidencia mensaje : todosMensajes) {
	            modeloLista.addElement(mensaje);
	        }
	        
	        JOptionPane.showMessageDialog(this, 
	                "Mostrando todos los mensajes ordenados por contacto y fecha", 
	                "Resultado de búsqueda", 
	                JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	/**
	 * Busca mensajes que contienen un texto específico, opcionalmente filtrando por contacto y/o teléfono
	 * 
	 * @param textoFiltro Texto a buscar en los mensajes
	 * @param nombreFiltro Filtro de nombre de contacto (puede ser null)
	 * @param telefonoFiltro Filtro de teléfono de contacto (puede ser null)
	 */
	private void buscarMensajes(String textoFiltro, String nombreFiltro, String telefonoFiltro) {
	    modeloLista.clear();
	    list.setCellRenderer(new ContactoMensajeRenderer());
	    List<MensajeCoincidencia> mensajesCoincidentes = AppChat.getInstancia().buscarMensajes(textoFiltro, nombreFiltro, telefonoFiltro);

	    // Agregar los mensajes ordenados a la lista
	    if (mensajesCoincidentes.isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	                "No se encontraron mensajes que coincidan con los criterios de búsqueda", 
	                "Sin resultados", 
	                JOptionPane.INFORMATION_MESSAGE);
	    } else {
	        for (MensajeCoincidencia mc : mensajesCoincidentes) {
	            modeloLista.addElement(mc); // CORREGIDO: Añadir el objeto MensajeCoincidencia completo
	        }
	    }
	}
	
	/**
	 * Abre el elemento seleccionado (contacto o mensaje)
	 * 
	 * @param seleccionado El objeto seleccionado
	 */
	private void abrirSeleccionado(MensajeCoincidencia seleccionado) {
			Mensaje mensaje = seleccionado.getMensaje();
			Contacto contacto = seleccionado.getContacto();
			buscarFiltroListener.onAccionRealizada(contacto, mensaje);
			// Aquí podrías abrir la conversación en el punto de este mensaje
			JOptionPane.showMessageDialog(this, 
					"Mensaje seleccionado: " + mensaje.getTexto(), 
					"Ver mensaje", 
					JOptionPane.INFORMATION_MESSAGE);
	}
	
}
