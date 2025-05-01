package umu.tds.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoMensajeRenderer;
import umu.tds.modelos.ContactoRenderer;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.MensajeCoincidencia;

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
import java.util.List;
import java.util.stream.Collectors;

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
	private JList list;
	private DefaultListModel<Object> modeloLista;

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
		modeloLista = new DefaultListModel<>();
		list = new JList(modeloLista);
		
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
					Object seleccionado = list.getSelectedValue();
					if (seleccionado != null) {
						abrirSeleccionado(seleccionado);
					}
				}
			}
		});
		
		// Cargar contactos inicialmente
		cargarTodosLosContactos();
	}
	
	/**
	 * Carga todos los contactos en la lista
	 */
	private void cargarTodosLosContactos() {
		modeloLista.clear();
		list.setCellRenderer(new ContactoRenderer ());
		
		List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactos();
		for (Contacto contacto : contactos) {
			modeloLista.addElement(contacto);
		}
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
			// Sin filtros, mostrar todos los contactos
			cargarTodosLosContactos();
			return;
		}
		
		if (buscarPorTexto) {
			// Búsqueda de mensajes que contienen el texto
			buscarMensajes(textoFiltro, buscarPorNombre ? nombreContactoFiltro : null, buscarPorTelefono ? telefonoFiltro : null);
		} else {
			// Búsqueda de contactos por nombre y/o teléfono
			buscarContactos(nombreContactoFiltro, telefonoFiltro);
		}
	}
	
	/**
	 * Busca contactos por nombre y/o teléfono
	 * 
	 * @param nombreFiltro Filtro de nombre (puede ser null o vacío)
	 * @param telefonoFiltro Filtro de teléfono (puede ser null o vacío)
	 */
	private void buscarContactos(String nombreFiltro, String telefonoFiltro) {
		modeloLista.clear();
		list.setCellRenderer(new ContactoRenderer());
		
		List<Contacto> todosLosContactos = AppChat.getInstancia().obtenerListaContactos();
		List<Contacto> resultados = todosLosContactos.stream()
				.filter(contacto -> {
					boolean coincideNombre = nombreFiltro.isEmpty() || 
							contacto.getNombre().toLowerCase().contains(nombreFiltro.toLowerCase());
					
					boolean coincideTelefono = telefonoFiltro.isEmpty() || 
							AppChat.getInstancia().getTelefonoContacto(contacto).contains(telefonoFiltro);
					
					return coincideNombre && coincideTelefono;
				})
				.collect(Collectors.toList());
		
		if (resultados.isEmpty()) {
			JOptionPane.showMessageDialog(this, 
					"No se encontraron contactos que coincidan con los criterios de búsqueda", 
					"Sin resultados", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			for (Contacto contacto : resultados) {
				modeloLista.addElement(contacto);
			}
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
	    
	    List<Contacto> contactos = AppChat.getInstancia().obtenerListaContactos();
	    List<MensajeCoincidencia> mensajesCoincidentes = new ArrayList<>();
	    
	    // Primero filtramos los contactos si es necesario
	    if (nombreFiltro != null && !nombreFiltro.isEmpty() || telefonoFiltro != null && !telefonoFiltro.isEmpty()) {
	        contactos = contactos.stream()
	                .filter(contacto -> {
	                    boolean coincideNombre = nombreFiltro == null || nombreFiltro.isEmpty() || 
	                            contacto.getNombre().toLowerCase().contains(nombreFiltro.toLowerCase());
	                    
	                    boolean coincideTelefono = telefonoFiltro == null || telefonoFiltro.isEmpty() || 
	                            AppChat.getInstancia().getTelefonoContacto(contacto).contains(telefonoFiltro);
	                    
	                    return coincideNombre && coincideTelefono;
	                })
	                .collect(Collectors.toList());
	    }
	    
	    // Ahora buscamos en los mensajes de los contactos filtrados
	    for (Contacto contacto : contactos) {
	        List<Mensaje> mensajes = AppChat.getInstancia().obtenerChatContacto(contacto);
	        if (mensajes != null) {
	            for (Mensaje mensaje : mensajes) {
	                String textoMensaje = mensaje.getTexto();
	                if (textoMensaje != null && !textoMensaje.isEmpty()) {
	                    double puntuacion = calcularPuntuacionCoincidencia(textoMensaje, textoFiltro);
	                    if (puntuacion > 0) {
	                        mensajesCoincidentes.add(new MensajeCoincidencia(mensaje, contacto));
	                    }
	                }
	            }
	        }
	    }
	    
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
	 * Calcula la puntuación de coincidencia entre un texto de mensaje y un filtro
	 * Busca la cadena completa, no palabras individuales
	 * 
	 * @param textoMensaje Texto del mensaje
	 * @param filtro Filtro de búsqueda
	 * @return Puntuación de coincidencia (0-1), donde 1 es coincidencia exacta
	 */
	private double calcularPuntuacionCoincidencia(String textoMensaje, String filtro) {
	    if (textoMensaje == null || textoMensaje.isEmpty() || filtro == null || filtro.isEmpty()) {
	        return 0;
	    }
	    
	    String textoLower = textoMensaje.toLowerCase();
	    String filtroLower = filtro.toLowerCase();
	    
	    // Buscar la frase completa como una unidad
	    if (textoLower.contains(filtroLower)) {
	        // Coincidencia exacta de la frase completa
	        double relacionLongitud = (double) filtroLower.length() / textoLower.length();
	        // Dar más peso a coincidencias exactas
	        return 0.5 + (relacionLongitud * 0.5);
	    }
	    
	    // Si no contiene la frase exacta, no hay coincidencia
	    return 0;
	}
	
	/**
	 * Abre el elemento seleccionado (contacto o mensaje)
	 * 
	 * @param seleccionado El objeto seleccionado
	 */
	private void abrirSeleccionado(Object seleccionado) {
		if (seleccionado instanceof Contacto) {
			Contacto contacto = (Contacto) seleccionado;
			// Aquí abrirías la conversación con este contacto
			JOptionPane.showMessageDialog(this, 
					"Abriendo conversación con: " + contacto.getNombre(), 
					"Abrir conversación", 
					JOptionPane.INFORMATION_MESSAGE);
		} else if (seleccionado instanceof Mensaje) {
			Mensaje mensaje = (Mensaje) seleccionado;
			// Aquí podrías abrir la conversación en el punto de este mensaje
			JOptionPane.showMessageDialog(this, 
					"Mensaje seleccionado: " + mensaje.getTexto(), 
					"Ver mensaje", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}
