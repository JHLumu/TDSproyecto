package umu.tds.modelos;

import javax.swing.*;
import umu.tds.appchat.AppChat;
import java.awt.*;

/**
 * Implementación específica del renderizador para la lista de contactos con mensajes.
 */
public class ContactoMensajeRenderer extends BaseContactoRenderer implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;
    
    // Componentes específicos de este renderizador
    private JLabel mensajeLabel;
    private JLabel fechaLabel;
 // Añadir a la clase ContactoMensajeRenderer
    private JLabel indicadorLabel;

    // Modificar el método inicializarContenidoPanel
    @Override
    protected void inicializarContenidoPanel() {
        contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        // Resto del contenido en columna a la derecha
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre en la parte superior con fuente más grande
        nombreLabel = new JLabel();
        nombreLabel.setFont(new Font(nombreLabel.getFont().getName(), Font.BOLD, 14));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        contenidoPanel.add(nombreLabel, gbc);
        
        // Separador entre nombre y mensaje
        separador = new JSeparator();
        separador.setForeground(SEPARATOR_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(1, 0, 3, 0);
        contenidoPanel.add(separador, gbc);
        
        // Panel específico para el mensaje con alineación vertical centrada
        JPanel mensajePanel = new JPanel(new BorderLayout());
        mensajePanel.setOpaque(false);
        mensajeLabel = new JLabel();
        mensajeLabel.setFont(new Font(mensajeLabel.getFont().getName(), Font.PLAIN, 12));
        mensajeLabel.setVerticalAlignment(JLabel.CENTER);
        mensajePanel.add(mensajeLabel, BorderLayout.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.weighty = 1.0; // Esto hace que el panel de mensaje tome el espacio vertical disponible
        gbc.fill = GridBagConstraints.BOTH;
        
     // Indicador E/R a la izquierda
        indicadorLabel = new JLabel();
        indicadorLabel.setFont(new Font(indicadorLabel.getFont().getName(), Font.BOLD, 12));
        indicadorLabel.setHorizontalAlignment(JLabel.CENTER);
        indicadorLabel.setPreferredSize(new Dimension(20, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 5);
        mensajePanel.add(indicadorLabel, BorderLayout.WEST);
        contenidoPanel.add(mensajePanel, gbc);
        
        // Fecha en la parte inferior
        fechaLabel = new JLabel();
        fechaLabel.setFont(new Font(fechaLabel.getFont().getName(), Font.ITALIC, 10));
        fechaLabel.setForeground(PHONE_TEXT_COLOR);
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 0, 0);
        contenidoPanel.add(fechaLabel, gbc);
    }

    // Modificar el método getListCellRendererComponent
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, 
                                                  Object contacto, 
                                                  int index,
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
        if(contacto instanceof Contacto || contacto instanceof MensajeCoincidencia) {
            // Limpiar componentes
            mensajeLabel.setText("");
            mensajeLabel.setIcon(null);
            
            // Obtener nombre según el tipo de objeto
            String nombre = (contacto instanceof Contacto) ? 
                ((Contacto)contacto).getNombre() : 
                ((MensajeCoincidencia)contacto).getNombre();
            
            nombreLabel.setText(nombre);
            
            // Obtener la información procesada del controlador
            AppChat controlador = AppChat.getInstancia();
            AppChat.InfoMensajeVista infoMensaje = controlador.prepararInfoParaVista(contacto);
            
            // Configurar el indicador E/R según si el mensaje es enviado o recibido
            if (infoMensaje.esMensajeEnviado()) {
                indicadorLabel.setText("E");
                indicadorLabel.setForeground(new Color(0, 100, 0)); // Verde oscuro para enviados
            } else {
                indicadorLabel.setText("R");
                indicadorLabel.setForeground(new Color(0, 0, 150)); // Azul oscuro para recibidos
            }
            
            // Configurar el mensaje con los datos ya procesados
            if (infoMensaje.esEmoji()) {
                mensajeLabel.setIcon(infoMensaje.getEmojiRedimensionado());
            } else {
                mensajeLabel.setText(infoMensaje.getTextoFormateado());
            }
            
            // Configurar la fecha ya formateada
            fechaLabel.setText(infoMensaje.getHoraFormateada());
            
            // Configurar la imagen
            if (contacto instanceof Contacto) {
                configurarImagen((Contacto)contacto);
            } else {
                configurarImagen(((MensajeCoincidencia)contacto).getContacto());
            }
            
            // Aplicar estilos según selección y posición
            aplicarEstilos(list, index, isSelected);
        }
        return this;
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
