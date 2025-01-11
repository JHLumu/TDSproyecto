package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import tds.BubbleText;
import umu.tds.appchat.AppChat;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.MensajeRenderer;
import umu.tds.utils.Estado;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIDefaults;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class Principal extends JFrame implements TDSObserver {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private final AppChat controlador;
	private JButton btnUsuario;
	DefaultComboBoxModel<String> listaContactos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BubbleText.noZoom();
					Principal frame = new Principal();
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
	public Principal() {
		
		//Se obtiene la instancia del controlador
		this.controlador = AppChat.getInstancia();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 746, 425);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resources/chat.png")));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(255, 255, 255));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		JComboBox<String> comboBoxContactos = new JComboBox<String>();
		comboBoxContactos.setName("contacto o telefono");
		comboBoxContactos.setToolTipText("");
		comboBoxContactos.setSize(new Dimension(100, 20));
		comboBoxContactos.setBackground(new Color(81, 116, 255));
		comboBoxContactos.setMaximumSize(new Dimension(100000, 16000));
		comboBoxContactos.setMinimumSize(new Dimension(100, 20));
		comboBoxContactos.setPreferredSize(new Dimension(100, 20));
		
		listaContactos = new DefaultComboBoxModel<String>();
		actualizarListaContactos(); // Cargar contactos inicialmente
		comboBoxContactos.setModel(listaContactos);
		panelNorte.add(comboBoxContactos);
		
		JButton btnEnv = new JButton("");
		btnEnv.setPreferredSize(new Dimension(40, 40));
		btnEnv.setForeground(new Color(255, 255, 255));
		btnEnv.setBackground(new Color(81, 116, 255));
		btnEnv.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/enviar.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		panelNorte.add(btnEnv);
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalGlue_4.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_4);
		
		
		JButton btnBuscar = new JButton("");
		btnBuscar.setPreferredSize(new Dimension(40, 40));
		btnBuscar.setForeground(new Color(255, 255, 255));
		btnBuscar.setBackground(new Color(81, 116, 255));
		btnBuscar.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/lupa.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		panelNorte.add(btnBuscar);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalGlue_3.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_3);
		
		JButton btnPremium = new JButton("");
		btnPremium.setPreferredSize(new Dimension(40, 40));
		btnPremium.setForeground(new Color(255, 255, 255));
		btnPremium.setBackground(new Color(81, 116, 255));
		btnPremium.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/moneda.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		panelNorte.add(btnPremium);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalGlue_2.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_2);
		
		JButton btnContactos = new JButton("");
		btnContactos.setPreferredSize(new Dimension(40, 40));
		btnContactos.setForeground(new Color(255, 255, 255));
		btnContactos.setBackground(new Color(81, 116, 255));
		btnContactos.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/agenda.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		btnContactos.addActionListener(evento -> {
			ListaContactos frame = new ListaContactos();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		});
		panelNorte.add(btnContactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue);
		
		btnUsuario = new JButton(this.controlador.getNombreUsuario());
		btnUsuario.setForeground(new Color(0, 0, 0));
		btnUsuario.setBackground(new Color(81, 116, 255));
		btnUsuario.setIcon(new ImageIcon(this.controlador.getFotoPerfilSesion().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		
		btnUsuario.addActionListener(evento -> {
				CambiarFoto frame = new CambiarFoto();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
		});
		panelNorte.add(btnUsuario);
		
		JPanel panelMensaje = new JPanel();
		panelMensaje.setPreferredSize(new Dimension(200, 0));
		panelMensaje.setMaximumSize(new Dimension(500, 200));
		contentPane.add(panelMensaje, BorderLayout.WEST);
		panelMensaje.setLayout(new BorderLayout(0, 0));
		
		JList<Mensaje> list = new JList <Mensaje>();
		list.setPreferredSize(new Dimension(0, 25));
		list.setCellRenderer(new MensajeRenderer());
		list.setModel(new AbstractListModel<Mensaje>() {
			private static final long serialVersionUID = 1L;
			List<Mensaje> values = AppChat.getInstancia().obtenerListaMensajesRecientesPorUsuario();
			public int getSize() {
				return values.size();
			}
			public Mensaje getElementAt(int index) {
				return values.get(index);
			}
		});
		panelMensaje.add(list);
		
		JPanel chat = new JPanel();
		chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS));
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(chat);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(0,0));
		panelCentro.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);
		
		JPanel barraIntro = new JPanel();
		panelCentro.add(barraIntro, BorderLayout.SOUTH);
		GridBagLayout gbl_barraIntro = new GridBagLayout();
		gbl_barraIntro.columnWidths = new int[]{20, 0, 96, 20, 20, 1, 0};
		gbl_barraIntro.rowHeights = new int[]{21, 0};
		gbl_barraIntro.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_barraIntro.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		barraIntro.setLayout(gbl_barraIntro);
		
		JButton Emoticono = new JButton("Emoticono");
		Emoticono.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BubbleText burbuja;
				
				burbuja=new BubbleText(chat, (int)(Math.random()*24), Color.GREEN,  AppChat.getInstancia().getNombreUsuario(), BubbleText.SENT, 18);
				textField.setText("");
				chat.add(burbuja);
			}
		});
		
		GridBagConstraints gbc_Emoticono = new GridBagConstraints();
		gbc_Emoticono.insets = new Insets(0, 0, 0, 5);
		gbc_Emoticono.gridx = 1;
		gbc_Emoticono.gridy = 0;
		barraIntro.add(Emoticono, gbc_Emoticono);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		barraIntro.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BubbleText burbuja = new BubbleText(chat,textField.getText(), Color.CYAN, AppChat.getInstancia().getNombreUsuario(), BubbleText.SENT,14);
	
				// Asocia las propiedades específicas a este componente
				burbuja.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente
				burbuja.setForeground(Color.BLACK); // Texto negro
				burbuja.setOpaque(false); // Asegúrate de que sea transparente
				
				UIDefaults bubbleTextOverrides = new UIDefaults();
				bubbleTextOverrides.put("Component.background", new Color(0, 0, 0, 0)); // Fondo transparente
				bubbleTextOverrides.put("Component.foreground", Color.BLACK); // Texto negro
				bubbleTextOverrides.put("Component.border", BorderFactory.createEmptyBorder()); // Sin bordes

				
				burbuja.putClientProperty("Nimbus.Overrides", bubbleTextOverrides);
				burbuja.putClientProperty("Nimbus.Overrides.InheritDefaults", true); // Hereda configuraciones generales de Nimbus
				
				chat.add(burbuja);	
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		barraIntro.add(btnNewButton, gbc_btnNewButton);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		GridBagConstraints gbc_horizontalGlue_1 = new GridBagConstraints();
		gbc_horizontalGlue_1.anchor = GridBagConstraints.WEST;
		gbc_horizontalGlue_1.gridx = 5;
		gbc_horizontalGlue_1.gridy = 0;
		barraIntro.add(horizontalGlue_1, gbc_horizontalGlue_1);
		
		AppChat.getInstancia().addObserver(Estado.NUEVA_FOTO_USUARIO, this);
		AppChat.getInstancia().addObserver(Estado.INFO_CONTACTO, this);
	}
	
	// Implementación del método update de TDSObserver
    @Override
    public void update(TDSObservable o, Object arg) {
    	if (arg instanceof Estado) {
            Estado estadoActual = (Estado) arg;
            
            if (estadoActual.equals(Estado.INFO_CONTACTO)) {
                this.actualizarListaContactos();
            } else if (estadoActual.equals(Estado.NUEVA_FOTO_USUARIO)) {
            	btnUsuario.setIcon(new ImageIcon(this.controlador.getFotoPerfilSesion().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            }
         
        }
    }

    // Método para actualizar la lista de contactos en la UI
    private void actualizarListaContactos() {
    	listaContactos.removeAllElements();
    	listaContactos.addElement("Contacto o Teléfono");
    	for (String nombreContacto: this.controlador.obtenerListaContactos()) listaContactos.addElement(nombreContacto);
    }

    // Opcional: Asegurarse de eliminar el observador cuando la ventana se cierra
    @Override
    public void dispose() {
        AppChat.getInstancia().deleteObserver(Estado.INFO_CONTACTO,this);
        AppChat.getInstancia().deleteObserver(Estado.NUEVA_FOTO_USUARIO,this);
        super.dispose();
    }
   

}
