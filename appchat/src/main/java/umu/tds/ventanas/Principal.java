package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import tds.BubbleText;
import umu.tds.appchat.AppChat;
import umu.tds.modelos.Mensaje;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		comboBoxContactos.setBackground(new Color(81, 116, 255));
		comboBoxContactos.setMaximumSize(new Dimension(10000, 16000));
		comboBoxContactos.setMinimumSize(new Dimension(0, 25));
		comboBoxContactos.setPreferredSize(new Dimension(0, 25));
		comboBoxContactos.setModel(new DefaultComboBoxModel<String>(new String[] {"contacto 1", "contacto 2", "contacto 3"}));
		panelNorte.add(comboBoxContactos);
		
		Border borde = BorderFactory.createLineBorder(Color.BLACK,2);
		
		Component horizontalGlue_5 = Box.createHorizontalGlue();
		horizontalGlue_5.setMaximumSize(new Dimension(15600, 0));
		panelNorte.add(horizontalGlue_5);
		
		JButton btnEnv = new JButton("");
		btnEnv.setPreferredSize(new Dimension(25, 25));
		btnEnv.setForeground(new Color(255, 255, 255));
		btnEnv.setBackground(new Color(81, 116, 255));
		btnEnv.setIcon(new ImageIcon(Principal.class.getResource("/resources/enviar.png")));
		btnEnv.setBorder(borde);
		panelNorte.add(btnEnv);
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalGlue_4.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_4);
		
		
		JButton btnBuscar = new JButton("");
		btnBuscar.setPreferredSize(new Dimension(25, 25));
		btnBuscar.setForeground(new Color(255, 255, 255));
		btnBuscar.setBackground(new Color(81, 116, 255));
		btnBuscar.setBorder(borde);
		btnBuscar.setIcon(new ImageIcon(Principal.class.getResource("/resources/lupa.png")));
		panelNorte.add(btnBuscar);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalGlue_3.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_3);
		
		JButton btnPremium = new JButton("");
		btnPremium.setPreferredSize(new Dimension(25, 25));
		btnPremium.setForeground(new Color(255, 255, 255));
		btnPremium.setBackground(new Color(81, 116, 255));
		btnPremium.setBorder(borde);
		btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/resources/moneda.png")));
		panelNorte.add(btnPremium);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalGlue_2.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue_2);
		
		JButton btnContactos = new JButton("");
		btnContactos.setPreferredSize(new Dimension(25, 25));
		btnContactos.setForeground(new Color(255, 255, 255));
		btnContactos.setBackground(new Color(81, 116, 255));
		btnContactos.setBorder(borde);
		btnContactos.setIcon(new ImageIcon(Principal.class.getResource("/resources/agenda.png")));
		panelNorte.add(btnContactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setMaximumSize(new Dimension(100, 0));
		panelNorte.add(horizontalGlue);
		
		JButton btnAvatar = new JButton("Nombre Usuario");
		btnAvatar.setForeground(new Color(0, 0, 0));
		btnAvatar.setBackground(new Color(81, 116, 255));
		btnAvatar.setBorder(borde);
		btnAvatar.setIcon(new ImageIcon(Principal.class.getResource("/resources/avatar.png")));
		panelNorte.add(btnAvatar);
		
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
			List<Mensaje> values = AppChat.obtenerListaMensajesRecientesPorUsuario();
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
		chat.setSize(200,350);
		chat.setMinimumSize(new Dimension(200, 175));
		chat.setMaximumSize(new Dimension(300, 200));
		chat.setPreferredSize(new Dimension(300, 200));
		chat.setBorder(new LineBorder(new Color(0, 0, 0), 0));		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(300, 200));
		scrollPane.setMinimumSize(new Dimension(200, 23));
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		scrollPane.add(chat);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(0,0));
		panelCentro.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panelCentro, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panelCentro.add(panel, BorderLayout.SOUTH);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_1);
		
		JButton btnNewButton = new JButton("enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BubbleText burbuja;
				burbuja=new BubbleText(chat,textField.getText(), Color.GREEN, "J.Ramón", BubbleText.SENT);
				textField.setText("");
				chat.add(burbuja);
				
			}
		});
		panel.add(btnNewButton);
		

	}

}
