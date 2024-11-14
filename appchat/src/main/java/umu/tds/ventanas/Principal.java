package umu.tds.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import umu.tds.appchat.AppChat;
import umu.tds.modelos.Mensaje;
import umu.tds.ventanas.MensajeRenderer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class Principal extends JFrame {

	private JPanel contentPane;

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
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		JComboBox comboBoxContactos = new JComboBox();
		comboBoxContactos.setModel(new DefaultComboBoxModel(new String[] {"contacto 1", "contacto 2", "contacto 3"}));
		panelNorte.add(comboBoxContactos);
		
		JButton btnEnv = new JButton("");
		btnEnv.setIcon(new ImageIcon(Principal.class.getResource("/resources/enviar.png")));
		panelNorte.add(btnEnv);
		
		JButton btnBuscar = new JButton("");
		btnBuscar.setIcon(new ImageIcon(Principal.class.getResource("/resources/lupa.png")));
		panelNorte.add(btnBuscar);
		
		JButton btnPremium = new JButton("");
		btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/resources/moneda.png")));
		panelNorte.add(btnPremium);
		
		JButton btnContactos = new JButton("");
		btnContactos.setIcon(new ImageIcon(Principal.class.getResource("/resources/agenda.png")));
		panelNorte.add(btnContactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);
		
		JButton btnAvatar = new JButton("Nombre Usuario");
		btnAvatar.setIcon(new ImageIcon(Principal.class.getResource("/resources/avatar.png")));
		panelNorte.add(btnAvatar);
		
		JPanel panelMensaje = new JPanel();
		contentPane.add(panelMensaje, BorderLayout.WEST);
		panelMensaje.setLayout(new BorderLayout(0, 0));
		
		
		
		JList<Mensaje> list = new JList <Mensaje>();
		list.setCellRenderer(new MensajeRenderer());
		list.setModel(new AbstractListModel() {
			private static final long serialVersiononUID = 1L;
			List<Mensaje> values = AppChat.obtenerListaMensajesRecientesPorUsuario();
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				return values.get(index);
			}
		});
		panelMensaje.add(list);
	}

}
