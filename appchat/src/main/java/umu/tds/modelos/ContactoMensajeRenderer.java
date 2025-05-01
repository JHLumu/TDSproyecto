package umu.tds.modelos;

import javax.swing.*;

import tds.BubbleText;
import umu.tds.appchat.AppChat;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementación específica del renderizador para la lista de contactos con mensajes.
 */
public class ContactoMensajeRenderer extends BaseContactoRenderer implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;
    private static final int MAX_MESSAGE_LENGTH = 16;
    
    // Componentes específicos de este renderizador
    private JLabel mensajeLabel;
    private JLabel fechaLabel;
    
    /**
     * Constructor
     */
    public ContactoMensajeRenderer() {
        super();
    }
    
    @Override
    protected void inicializarContenidoPanel() {
        contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre en la parte superior con fuente más grande
        nombreLabel = new JLabel();
        nombreLabel.setFont(new Font(nombreLabel.getFont().getName(), Font.BOLD, 14));
        gbc.insets = new Insets(0, 0, 2, 0);
        contenidoPanel.add(nombreLabel, gbc);
        
        // Separador entre nombre y mensaje
        separador = new JSeparator();
        separador.setForeground(SEPARATOR_COLOR);
        gbc.insets = new Insets(1, 0, 3, 0);
        contenidoPanel.add(separador, gbc);
        
        // Panel específico para el mensaje con alineación vertical centrada
        JPanel mensajePanel = new JPanel(new BorderLayout());
        mensajePanel.setOpaque(false);
        mensajeLabel = new JLabel();
        mensajeLabel.setFont(new Font(mensajeLabel.getFont().getName(), Font.PLAIN, 12));
        mensajeLabel.setVerticalAlignment(JLabel.CENTER);
        mensajePanel.add(mensajeLabel, BorderLayout.CENTER);
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.weighty = 1.0; // Esto hace que el panel de mensaje tome el espacio vertical disponible
        gbc.fill = GridBagConstraints.BOTH;
        contenidoPanel.add(mensajePanel, gbc);
        
        // Fecha en la parte inferior
        fechaLabel = new JLabel();
        fechaLabel.setFont(new Font(fechaLabel.getFont().getName(), Font.ITALIC, 10));
        fechaLabel.setForeground(PHONE_TEXT_COLOR);
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 0, 0);
        contenidoPanel.add(fechaLabel, gbc);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, 
                                                  Object contacto, 
                                                  int index,
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
        if(contacto instanceof Contacto) {
        	Contacto c = (Contacto) contacto;
	        // Limpiar componentes
	        mensajeLabel.setText("");
	        mensajeLabel.setIcon(null);
	        
	        // Configurar nombre y mensaje
	        nombreLabel.setText(c.getNombre());
	        configurarMensaje(c);
	        configurarFecha(c);
	        
	        // Configurar la imagen
	        configurarImagen(c);
	        
	        // Aplicar estilos según selección y posición
	        aplicarEstilos(list, index, isSelected);
	        
        } else if (contacto instanceof MensajeCoincidencia) {
        	MensajeCoincidencia c = (MensajeCoincidencia) contacto;
	        // Limpiar componentes
	        mensajeLabel.setText("");
	        mensajeLabel.setIcon(null);
	        
	        // Configurar nombre y mensaje
	        nombreLabel.setText(c.getNombre());
	        configurarMensaje(c);
	        configurarFecha(c);
	        
	        // Configurar la imagen
	        configurarImagen(c.getContacto());
	        
	        // Aplicar estilos según selección y posición
	        aplicarEstilos(list, index, isSelected);
        }
		return this;
    }
    
    /**
     * Configura el contenido del último mensaje
     */
    private void configurarMensaje(Object contacto) {
        AppChat controlador = AppChat.getInstancia();
        Object mensaje;
        ImageIcon emojiIcon;
        
        // Procesar y establecer el último mensaje
        if (contacto instanceof Contacto) {
        	Contacto c = (Contacto) contacto;
        	mensaje = controlador.getUltimoMensajeContacto(c);
            
        } else {
        	MensajeCoincidencia c = (MensajeCoincidencia) contacto;
        	mensaje = c.getContenido();
        }
        
        if (mensaje instanceof String) {
            String mensajeTexto = (String) mensaje;
            // Truncar mensaje si es demasiado largo (añadir "...")
            if (mensajeTexto.length() > MAX_MESSAGE_LENGTH) {
                mensajeTexto = mensajeTexto.substring(0, MAX_MESSAGE_LENGTH - 1) + "...";
            }
            mensajeLabel.setText(mensajeTexto);
        } else if (mensaje instanceof Integer) {
            // Aquí es donde se establece el emoji
            mensajeLabel.setText("");
            
            // Obtener el emoji original
            emojiIcon = BubbleText.getEmoji((Integer) mensaje);
            if (emojiIcon != null) {
                Image img = emojiIcon.getImage();
                // Redimensionar a un tamaño más pequeño
                Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                mensajeLabel.setIcon(new ImageIcon(newImg));
            }
        }
        
    }
    
    /**
     * Configura la fecha del último mensaje
     */
    private void configurarFecha(Object contacto) {
        AppChat controlador = AppChat.getInstancia();
        LocalDateTime fecha;
        if (contacto instanceof Contacto) {
        	Contacto c = (Contacto) contacto;
        	fecha = controlador.getUltimoMensajeFecha(c);
            
        } else {
        	MensajeCoincidencia c = (MensajeCoincidencia) contacto;
        	fecha = c.getFechaEnvio();
        }
        if (fecha != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            fechaLabel.setText(fecha.format(formatter));
        } else {
            fechaLabel.setText("");
        }
    }
    
    @Override
    protected void aplicarEstilosSeleccionado() {
        nombreLabel.setForeground(new Color(0, 0, 120));
        mensajeLabel.setForeground(new Color(20, 20, 80));
        fechaLabel.setForeground(new Color(60, 90, 140));
        separador.setForeground(SELECTED_SEPARATOR_COLOR);
    }
    
    @Override
    protected void aplicarEstilosNoSeleccionado(JList<?> list) {
        nombreLabel.setForeground(list.getForeground());
        mensajeLabel.setForeground(new Color(60, 60, 60));
        fechaLabel.setForeground(PHONE_TEXT_COLOR);
        separador.setForeground(SEPARATOR_COLOR);
    }
}
