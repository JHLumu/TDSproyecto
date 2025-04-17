package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import tds.BubbleText;
import umu.tds.appchat.AppChat;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.MensajeRenderer;
import umu.tds.modelos.TDSEmojiPanel;
import umu.tds.utils.Estado;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.BevelBorder;


public class Principal extends JFrame implements TDSObserver {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
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
		panelNorte.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelNorte.setBackground(new Color(255, 255, 255));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		JComboBox<String> comboBoxContactos = new JComboBox<String>();
		comboBoxContactos.setName("contacto o telefono");
		comboBoxContactos.setToolTipText("");
		comboBoxContactos.setSize(new Dimension(150, 40));
		comboBoxContactos.setBackground(new Color(81, 116, 255));
		comboBoxContactos.setMaximumSize(new Dimension(100000, 30));
		comboBoxContactos.setMinimumSize(new Dimension(150, 40));
		comboBoxContactos.setPreferredSize(new Dimension(100, 20));
		
		listaContactos = new DefaultComboBoxModel<String>();
		actualizarListaContactos(); // Cargar contactos inicialmente
		comboBoxContactos.setModel(listaContactos);
		panelNorte.add(comboBoxContactos);
		
		JButton btnEnv = new JButton("Enviar");
		btnEnv.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnEnv.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnEnv.setMinimumSize(new Dimension(80, 40));
		btnEnv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnv.setPreferredSize(new Dimension(90, 40));
		btnEnv.setForeground(new Color(255, 255, 255));
		btnEnv.setBackground(new Color(81, 116, 255));
		btnEnv.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/enviar.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnEnv);
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalGlue_4.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_4);
		
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuscarConFiltro frame = new BuscarConFiltro();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				
			}
		});
		btnBuscar.setPreferredSize(new Dimension(90, 40));
		btnBuscar.setForeground(new Color(255, 255, 255));
		btnBuscar.setBackground(new Color(81, 116, 255));
		btnBuscar.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/lupa.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnBuscar);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalGlue_3.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_3);
		
		JButton btnPremium = new JButton("Premium");
		btnPremium.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnPremium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPremium.setPreferredSize(new Dimension(100, 40));
		btnPremium.setForeground(new Color(255, 255, 255));
		btnPremium.setBackground(new Color(81, 116, 255));
		btnPremium.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/moneda.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnPremium);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalGlue_2.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_2);
		
		JButton btnContactos = new JButton("Lista de Contactos");
		btnContactos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnContactos.setPreferredSize(new Dimension(150, 32));
		btnContactos.setForeground(new Color(255, 255, 255));
		btnContactos.setBackground(new Color(81, 116, 255));
		btnContactos.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/agenda.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
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
		btnUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnUsuario.setForeground(new Color(255, 255, 255));
		btnUsuario.setBackground(new Color(81, 116, 255));
		btnUsuario.setIcon(new ImageIcon(this.controlador.getFotoPerfilSesion().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		btnUsuario.addActionListener(evento -> {
				EditarUsuario frame = new EditarUsuario();
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
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setPreferredSize(new Dimension(0, 25));
		list.setCellRenderer(new MensajeRenderer());
		//REVISAR: Asignar list.model cuando se implementen los mensajes
		panelMensaje.add(list);
		
		JPanel chat = new JPanel();
		chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS));
	
		JScrollPane scrollPane = new JScrollPane(chat);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(0,0));
		panelCentro.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);
		
		JPanel barraIntro = new JPanel();
		barraIntro.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		barraIntro.setBackground(new Color(255, 255, 255));
		panelCentro.add(barraIntro, BorderLayout.SOUTH);
		GridBagLayout gbl_barraIntro = new GridBagLayout();
		gbl_barraIntro.columnWidths = new int[]{20, 0, 96, 20, 20, 1, 0};
		gbl_barraIntro.rowHeights = new int[]{21, 0};
		gbl_barraIntro.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_barraIntro.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		barraIntro.setLayout(gbl_barraIntro);
		
		JButton emoticonoBtn = new JButton("Emoticono");
		emoticonoBtn.setForeground(new Color(255, 255, 255));
		emoticonoBtn.setBackground(new Color(81, 116, 255));
		emoticonoBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		emoticonoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog emojiDialog = new JDialog();
			    emojiDialog.setTitle("Select Emoji");
			    emojiDialog.add(new TDSEmojiPanel(chat));
			    emojiDialog.pack();
			    emojiDialog.setLocationRelativeTo(null);
			    emojiDialog.setModal(true);
			    emojiDialog.setVisible(true);
				
			}
		});
		
		GridBagConstraints gbc_Emoticono = new GridBagConstraints();
		gbc_Emoticono.fill = GridBagConstraints.HORIZONTAL;
		gbc_Emoticono.gridwidth = 2;
		gbc_Emoticono.insets = new Insets(0, 0, 0, 5);
		gbc_Emoticono.gridx = 0;
		gbc_Emoticono.gridy = 0;
		barraIntro.add(emoticonoBtn, gbc_Emoticono);
		
		textArea = new JTextArea(1, 30);
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(300, 30));
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			});
		
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		barraIntro.add(textArea, gbc_textField);
		textArea.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(81, 116, 255));
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BubbleText burbuja = new BubbleText(chat,textArea.getText(), Color.WHITE, AppChat.getInstancia().getNombreUsuario(), BubbleText.SENT,14);
				textArea.setText("");
				chat.add(burbuja);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
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
		
	}
	
	private void ajustarTamañoAreaTexto() {
		 int lineas = textArea.getLineCount();
		 int altura = 20 * lineas; // Ajusta el valor según el tamaño de fuente
		 textArea.setPreferredSize(new Dimension(300, altura));
		 textArea.revalidate();
		}
	
	// Implementación del método update de TDSObserver
    @Override
    public void update(TDSObservable o, Object arg) {
    	if (arg instanceof Estado) {
            Estado estadoActual = (Estado) arg;
            System.out.println("[DEBUG Principal update] Estado recibido en ListaContactos: " + estadoActual);
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
        super.dispose();
    }
   

}
