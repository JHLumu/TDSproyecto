package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import tds.BubbleText;
import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Contacto.TipoContacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.ContactoMensajeRenderer;
import umu.tds.modelos.Mensaje;
import umu.tds.utils.BuscarFiltroListener;
import umu.tds.utils.Estado;
import umu.tds.utils.TDSObservable;
import umu.tds.utils.TDSObserver;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.event.ActionEvent;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


public class Principal extends JFrame implements TDSObserver, BuscarFiltroListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	private final AppChat controlador;
	private JButton btnUsuario;
	private Color colorBotones;
	private JComboBox<Object> comboBoxContactos;
	private JPanel chat;
	private DefaultComboBoxModel<Object> listaContactosComboBox;
	private DefaultListModel<Contacto> listaContactosLista;
	private boolean seleccionPanel; // true para comboBox, false para lista
	private JScrollPane scrollPane;
	private JPanel panelMensaje;
	private JPanel panelCentro;
	private JPanel panelNorte;
	private JPanel barraIntro;
	private JList<Contacto> listaPanelChat;

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

	private void actualizarColor(Component comp) {
	    
		if (comp instanceof JButton) {
	        ((JButton) comp).setBackground(this.colorBotones);
	    }
	    
		else if (comp.equals(this.comboBoxContactos)) {
			this.comboBoxContactos.setBackground(this.colorBotones);
		}
	    
	    else if (comp instanceof JPanel) {
	        for (Component subComp : ((JPanel) comp).getComponents()) {
	            actualizarColor(subComp);
	        }
	    }
	}
	
	public void recargarPrincipal() {
		this.colorBotones = this.controlador.getColorGUI(1);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
		for (Component comp : this.getContentPane().getComponents()) {
			actualizarColor(comp);
	    }
		SwingUtilities.updateComponentTreeUI(this);
	}
	
	private void inicializacionPanelNorte() {
		
		 panelNorte = new JPanel();
		panelNorte.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelNorte.setBackground(new Color(255, 255, 255));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		comboBoxContactos = new JComboBox<Object>();
		comboBoxContactos.setName("contacto o telefono");
		comboBoxContactos.setToolTipText("");
		comboBoxContactos.setSize(new Dimension(150, 40));
		comboBoxContactos.setBackground(this.colorBotones);
		comboBoxContactos.setMaximumSize(new Dimension(100000, 30));
		comboBoxContactos.setMinimumSize(new Dimension(150, 40));
		comboBoxContactos.setPreferredSize(new Dimension(100, 20));
		
		listaContactosComboBox = new DefaultComboBoxModel<Object>();
		actualizarListaContactosComboBox(); // Cargar contactos inicialmente
		comboBoxContactos.setModel(listaContactosComboBox);
		comboBoxContactos.addActionListener(e -> {
		    Object seleccionado = null;
		    this.seleccionPanel = true;
		    if (comboBoxContactos.getSelectedIndex() != 0) {
		    	seleccionado = comboBoxContactos.getSelectedItem();
		        actualizarPanelChat((Contacto) seleccionado);
		        SwingUtilities.invokeLater(() -> {
		            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		        });
		    } else {
		        // Es el placeholder, por ejemplo: "Contacto o Teléfono"
		        // Podés limpiar el panel o no hacer nada
		    	chat.removeAll();
		    	chat.revalidate();
		    	chat.repaint();
		    	
		    }
		    
		});
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
		btnEnv.setBackground(this.colorBotones);
		btnEnv.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/enviar.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnEnv);
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalGlue_4.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_4);
		
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirBuscador();

				
			}
		});
		btnBuscar.setPreferredSize(new Dimension(90, 40));
		btnBuscar.setForeground(new Color(255, 255, 255));
		btnBuscar.setBackground(this.colorBotones);
		btnBuscar.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/lupa.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnBuscar);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalGlue_3.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_3);
		
		JButton btnPremium = new JButton("Premium");
		btnPremium.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnPremium.addActionListener(evento -> {
	
			if (this.controlador.isUsuarioPremium()) {
				 
				int res = JOptionPane.showConfirmDialog(this,
						 "¿Desea cancelar la suscripción mensual?",
						 "AppChat",
						 JOptionPane.YES_NO_OPTION );
				
				if (res == JOptionPane.YES_OPTION) {
					this.controlador.setUsuarioPremium(false);
					this.recargarPrincipal();
					JOptionPane.showMessageDialog(this, 
		                    "Se ha cancelado la suscripción correctamente.",
		                    "AppChat",
		                    JOptionPane.INFORMATION_MESSAGE);
					
					}
				
			}
			
			else {
				VentanaPremium frame = new VentanaPremium(this);
				frame.setModal(true);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);				
			}
		});
		
		
		btnPremium.setPreferredSize(new Dimension(100, 40));
		btnPremium.setForeground(new Color(255, 255, 255));
		btnPremium.setBackground(this.colorBotones);
		btnPremium.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/moneda.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		panelNorte.add(btnPremium);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalGlue_2.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_2);
		
		JButton btnContactos = new JButton("Lista de Contactos");
		btnContactos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnContactos.setPreferredSize(new Dimension(150, 32));
		btnContactos.setForeground(new Color(255, 255, 255));
		btnContactos.setBackground(this.colorBotones);
		btnContactos.setIcon(new ImageIcon(new ImageIcon(Principal.class.getResource("/resources/agenda.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		btnContactos.addActionListener(evento -> {
			ListaContactos frame = new ListaContactos();
			frame.setModal(true);
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
		btnUsuario.setBackground(this.colorBotones);
		
		Image imagenUsuario = this.controlador.getImagenUsuarioActual();
		if (imagenUsuario != null) btnUsuario.setIcon(new ImageIcon(imagenUsuario.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		
	
		
		btnUsuario.addActionListener(evento -> {
				EditarUsuario frame = new EditarUsuario();
				frame.setModal(true);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
		});
		panelNorte.add(btnUsuario);

	}
	
	private void inicializacionPanelCentro() {
		
		chat = new JPanel();
		chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS));
	
		scrollPane = new JScrollPane(chat);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(0,0));
		panelCentro.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);
		
		barraIntro = new JPanel();
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
		emoticonoBtn.setBackground(this.colorBotones);
		emoticonoBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		emoticonoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TDSEmojiPanel emojiPanel = new TDSEmojiPanel();
				JDialog emojiDialog = new JDialog();
			    emojiDialog.setTitle("Select Emoji");
			    emojiDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resources/chat.png")));
			    emojiDialog.add(emojiPanel);
			    emojiDialog.pack();
			    emojiDialog.setLocationRelativeTo(null);
			    emojiDialog.setModal(true);
			    emojiDialog.setVisible(true);
			    
			    int selectedEmoji = emojiPanel.getSelectedEmojiId();
		        if (selectedEmoji != -1) {
		        	Contacto contacto = null;
					if(seleccionPanel && (comboBoxContactos.getSelectedIndex() != 0)) {
						contacto = (Contacto) comboBoxContactos.getSelectedItem();	
					}				
					else {
						contacto = listaPanelChat.getSelectedValue();
					}
					if(controlador.enviarMensaje(contacto, selectedEmoji));
					actualizarListaContactosMensajes();
					actualizarPanelChat(contacto);
					
		        }
		        SwingUtilities.invokeLater(() -> {
				    JScrollBar vertical = scrollPane.getVerticalScrollBar();
				    vertical.setValue(vertical.getMaximum());
				});
				textArea.setText("");
			    
				
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
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(300, 30));
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
			});
		
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.gridx = 2;
		gbc_textArea.gridy = 0;
		barraIntro.add(textArea, gbc_textArea);
		textArea.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(this.colorBotones);
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Contacto contacto = null;
				if(seleccionPanel && (comboBoxContactos.getSelectedIndex() != 0)) {
					contacto = (Contacto) comboBoxContactos.getSelectedItem();	
				}				
				else {
					contacto = listaPanelChat.getSelectedValue();
				}
				if(controlador.enviarMensaje(contacto, textArea.getText()));
				actualizarListaContactosMensajes();
				actualizarPanelChat(contacto);

				SwingUtilities.invokeLater(() -> {
				    JScrollBar vertical = scrollPane.getVerticalScrollBar();
				    vertical.setValue(vertical.getMaximum());
				});
				textArea.setText("");
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
		
		
		AppChat.getInstancia().addObserver(Estado.INFO_CONTACTO, this);
	}
	
	private void inicializacionPanelMensaje() {
		
		panelMensaje = new JPanel();
		panelMensaje.setPreferredSize(new Dimension(200, 0));
		panelMensaje.setMaximumSize(new Dimension(500, 200));
		panelMensaje.setLayout(new BorderLayout(0, 0));
		contentPane.add(panelMensaje, BorderLayout.WEST);
		
		listaPanelChat = new JList <Contacto>();
		listaPanelChat.setBorder(new LineBorder(new Color(0, 0, 0)));
		listaPanelChat.setPreferredSize(new Dimension(0, 25));
		listaPanelChat.setCellRenderer(new ContactoMensajeRenderer());
		listaContactosLista = new DefaultListModel<>();
		actualizarListaContactosMensajes();
		listaPanelChat.setModel(listaContactosLista);
		listaPanelChat.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int index = listaPanelChat.locationToIndex(e.getPoint());
		        if (index != -1) {
		            Contacto seleccionado = listaPanelChat.getSelectedValue();
		            
		            if(seleccionado instanceof Contacto && seleccionado != null){
		                if (e.getClickCount() == 1) {
		                	Principal.this.seleccionPanel = false;
		                    actualizarPanelChat(seleccionado);
		                    SwingUtilities.invokeLater(() -> {
		                        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		                    });
		                } else if (e.getClickCount() == 2 && !controlador.esContacto(seleccionado) && seleccionado instanceof ContactoIndividual) {
		                	
		                	NuevoContacto frame = new NuevoContacto(seleccionado);
		        			frame.setModal(true);
		        			frame.setVisible(true);
		        			frame.setLocationRelativeTo(null);
		                    
		                }
		            }
		        }
		    }
		});
		
		panelMensaje.add(listaPanelChat);

		
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
                this.actualizarListaContactosComboBox();
                this.actualizarListaContactosMensajes();
                
            } else if (estadoActual.equals(Estado.NUEVA_FOTO_USUARIO)) {
            	btnUsuario.setIcon(new ImageIcon(this.controlador.getImagenUsuarioActual().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
            }
         
        }
    }
    
    private void actualizarListaContactosComboBox() {
        DefaultComboBoxModel<Object> modeloCombo = listaContactosComboBox;
        modeloCombo.removeAllElements();
        modeloCombo.addElement("Contacto o Teléfono"); // Placeholder
        for (Contacto contacto : this.controlador.obtenerListaContactos()) {
            modeloCombo.addElement(contacto);
        }

        // Establecer cómo se ve cada ítem en el combo
        comboBoxContactos.setRenderer(new DefaultListCellRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Contacto) {
                    Contacto c = (Contacto) value;
                    if(c.getTipoContacto() == TipoContacto.INDIVIDUAL)
                    	setText("🔸 " + c.getNombre()); // o cualquier otro formato
                    else
                    	setText("G🔸 " + c.getNombre());
                } else {
                	if (value != null) {
                	    setText(value.toString());
                	} else {
                	    setText(""); // o "Seleccionar..." si quieres mostrar algo por defecto
                	}
                    //setText(value.toString()); // para el placeholder
                }
                return this;
            }
        });
        
        
        
        this.revalidate();
        this.repaint();
    }
    
    private void actualizarListaContactosMensajes() {
        Contacto seleccionado = listaPanelChat.getSelectedValue(); // Guardar selección actual
        
    	DefaultListModel<Contacto> modelolista = listaContactosLista;
        modelolista.removeAllElements();

        for (Contacto contacto : this.controlador.obtenerListaChatMensajes()) {
            modelolista.addElement(contacto);
        }
        
     // Restaurar la selección si el contacto sigue existiendo en la nueva lista
        if (seleccionado != null) {
            for (int i = 0; i < modelolista.getSize(); i++) {
                if (modelolista.getElementAt(i).equals(seleccionado)) {
                    listaPanelChat.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        
        this.revalidate();
        this.repaint();
    }
    private void actualizarPanelChat(Contacto contacto) {
        this.chat.removeAll();
        List<BubbleText> burbujas = controlador.pintarMensajesBurbuja(contacto, this.chat);
  
        for (BubbleText burbuja : burbujas) {

            this.chat.add(burbuja);
        }


        this.chat.revalidate();
        this.chat.repaint();
    }
    
	@Override
	public void onAccionRealizada(Contacto c, Mensaje mObjetivo) {
		int targetIndex = controlador.ubicarMensaje(c, mObjetivo);
		actualizarPanelChat(c);
		if (targetIndex >= 0) {
	        SwingUtilities.invokeLater(() -> {
	            Component comp = chat.getComponent(targetIndex);
	            chat.scrollRectToVisible(comp.getBounds());

	            if (comp instanceof JComponent) {
	                JComponent jc = (JComponent) comp;
	                // Guarda el borde original para restaurarlo después
	                Border originalBorder = jc.getBorder();

	                // Aplica un borde grueso de color brillante
	                jc.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));

	                // Crea un Timer que en 2000 ms restaure el borde original
	                new javax.swing.Timer(2000, e -> {
	                    jc.setBorder(originalBorder);
	                    ((javax.swing.Timer)e.getSource()).stop();
	                }).start();
	            }
	        });
	    }
		
	}
	
	private void abrirBuscador() {
        BuscarConFiltro buscador = new BuscarConFiltro(this);

        buscador.setVisible(true);
		buscador.setLocationRelativeTo(null);
    }

	/**
	 * Create the frame.
	 */
	public Principal() {
		
		//Se obtiene la instancia del controlador
		this.controlador = AppChat.getInstancia();
		
		this.colorBotones = this.controlador.getColorGUI(1);
		this.controlador.addObserver(Estado.NUEVA_FOTO_USUARIO, this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 746, 425);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
		setForeground(new Color(0, 0, 0));
		setTitle("AppChat");
		
		inicializacionPanelNorte();
		inicializacionPanelMensaje();
		inicializacionPanelCentro();
		
	}

   

}

