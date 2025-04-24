package umu.tds.ventanas;

import javax.swing.border.LineBorder;

import umu.tds.appchat.AppChat;
import umu.tds.utils.ColoresAppChat;

import javax.swing.*;
import java.awt.*;
public class VentanaPremium extends JDialog {

	private static final long serialVersionUID = 1L;
	private Principal ventanaPrincipal;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					VentanaPremium frame = new VentanaPremium(null);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPremium(Principal ventana) {
		
		this.ventanaPrincipal = ventana;
		
		setResizable(false);
        setTitle("AppChat");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(AppChat.getInstancia().getURLIcon())));
        getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel central con 2 columnas
        JPanel centro = new JPanel();
        getContentPane().add(centro, BorderLayout.CENTER);
        centro.setLayout(new GridLayout(1, 2));

        // Panel izquierdo: info premium
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setBorder(new LineBorder(new Color(130, 135, 144)));
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Suscríbete a la versión Premium de AppChat");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        

        JLabel lblTexto1 = new JLabel("Disfruta de las siguientes ventajas:");
        lblTexto1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTexto1.setForeground(new Color(25, 25, 112));
        lblTexto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Component verticalStrut_1 = Box.createVerticalStrut(20);
        panelIzquierdo.add(verticalStrut_1);

        panelIzquierdo.add(lblTitulo);
        
        Component verticalStrut = Box.createVerticalStrut(80);
        panelIzquierdo.add(verticalStrut);
        panelIzquierdo.add(lblTexto1);

        String[] ventajas = {
            "• Exportación de conversaciones a PDF",
            "• Nuevo diseño de ventanas",
            "• Acceso exclusivo a nuevas funcionalidades"
        };

        for (String ventaja : ventajas) {
            JLabel lblVentaja = new JLabel(ventaja);
            lblVentaja.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblVentaja.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzquierdo.add(Box.createRigidArea(new Dimension(0, 5)));
            panelIzquierdo.add(lblVentaja);
        }

        centro.add(panelIzquierdo);

     // Panel derecho: datos de suscripción
        GridBagLayout gbl_panelDerecho = new GridBagLayout();
        gbl_panelDerecho.columnWeights = new double[]{1.0, 0.0};
        gbl_panelDerecho.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0};
        JPanel panelDerecho = new JPanel(gbl_panelDerecho);
        panelDerecho.setBackground(new Color(200, 207, 251));
        panelDerecho.setBorder(new LineBorder(Color.BLACK));

        // 1) Título (ocupa las dos columnas)
        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.fill = GridBagConstraints.VERTICAL;
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;
        gbcTitulo.gridwidth = 2;
        gbcTitulo.insets = new Insets(10, 10, 10, 10);
        gbcTitulo.anchor = GridBagConstraints.WEST;
        JLabel lblTexto2 = new JLabel("¿Listo para probar una nueva experiencia?");
        lblTexto2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTexto2.setForeground(new Color(25, 25, 112));
        panelDerecho.add(lblTexto2, gbcTitulo);

        // 2) Precio mensual
        GridBagConstraints gbcPrecioLabel = new GridBagConstraints();
        gbcPrecioLabel.gridx  = 0;
        gbcPrecioLabel.gridy  = 1;
        gbcPrecioLabel.anchor = GridBagConstraints.WEST;
        gbcPrecioLabel.insets = new Insets( 5, 10, 5, 5);
        JLabel lblSuscripcion = new JLabel("Precio mensual:");
        lblSuscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelDerecho.add(lblSuscripcion, gbcPrecioLabel);

        GridBagConstraints gbcPrecioValor = new GridBagConstraints();
        gbcPrecioValor.gridx = 1;
        gbcPrecioValor.gridy = 1;
        gbcPrecioValor.anchor = GridBagConstraints.EAST;
        gbcPrecioValor.insets = new Insets( 5, 5, 5, 10);
        JLabel lblPrecioSuscripcion = new JLabel(
            String.format("%.2f€", AppChat.getPrecioSuscripcion())
        );
        lblPrecioSuscripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelDerecho.add(lblPrecioSuscripcion, gbcPrecioValor);

        // 3) Descuento
        GridBagConstraints gbcDescLabel = new GridBagConstraints();
        gbcDescLabel.gridx = 0;
        gbcDescLabel.gridy = 2;
        gbcDescLabel.anchor = GridBagConstraints.WEST;
        gbcDescLabel.insets = new Insets( 5, 10, 5, 5);
        JLabel lblDescuento = new JLabel("Descuento aplicado:");
        lblDescuento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelDerecho.add(lblDescuento, gbcDescLabel);

        GridBagConstraints gbcDescValor = new GridBagConstraints();
        gbcDescValor.gridx = 1;
        gbcDescValor.gridy = 2;
        gbcDescValor.anchor = GridBagConstraints.EAST;
        gbcDescValor.insets = new Insets( 5, 5, 5, 10);
        JLabel lblPrecioDescuento = new JLabel(
            String.format("-%.0f€", 0.0)
        );
        lblPrecioDescuento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelDerecho.add(lblPrecioDescuento, gbcDescValor);

       
        GridBagConstraints gbcTotalLabel = new GridBagConstraints();
        gbcTotalLabel.gridx = 0;
        gbcTotalLabel.gridy = 3;
        gbcTotalLabel.anchor = GridBagConstraints.WEST;
        gbcTotalLabel.insets = new Insets( 5, 10, 10, 5);
        JLabel lblTotal = new JLabel("Total a pagar:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelDerecho.add(lblTotal, gbcTotalLabel);

        GridBagConstraints gbcTotalValor = new GridBagConstraints();
        gbcTotalValor.gridx     = 1;
        gbcTotalValor.gridy     = 3;
        gbcTotalValor.anchor    = GridBagConstraints.EAST;
        gbcTotalValor.insets    = new Insets( 5, 5, 10, 10);
        double total = AppChat.getPrecioSuscripcion();
        JLabel lblPrecioTotal = new JLabel(String.format("%.2f€", total));
        lblPrecioTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelDerecho.add(lblPrecioTotal, gbcTotalValor);

       
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);
        JButton botonCancelar = new JButton("Quizás en otro momento...");
        JButton botonSuscribirse = new JButton("Suscribirse");
        botonSuscribirse.setForeground(new Color(255, 255, 255));
        botonSuscribirse.setBackground(ColoresAppChat.COLOR_NOPREMIUM);
        botonSuscribirse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botonSuscribirse.addActionListener(evento -> {
        	AppChat.getInstancia().setUsuarioPremium(true);
        	this.ventanaPrincipal.recargarPrincipal();
        	this.dispose();
        	JOptionPane.showMessageDialog(this, 
                    "Se ha suscrito correctamente a AppChat.",
                    "AppChat",
                    JOptionPane.INFORMATION_MESSAGE);	
        });
        
        
        botonCancelar.setForeground(Color.BLACK);
        botonCancelar.setBackground(Color.WHITE);
        botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botonCancelar.addActionListener(evento -> {
        	this.dispose();
        });
        
        botones.add(botonCancelar);
        botones.add(botonSuscribirse);
        

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx     = 0;
        gbcBotones.gridy     = 4;
        gbcBotones.gridwidth = 2;
        gbcBotones.insets    = new Insets(10, 10, 10, 10);
        gbcBotones.anchor    = GridBagConstraints.CENTER;
        panelDerecho.add(botones, gbcBotones);

       
        centro.add(panelDerecho);

        
    }

}
