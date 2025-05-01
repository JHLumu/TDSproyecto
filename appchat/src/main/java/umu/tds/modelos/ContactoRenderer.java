package umu.tds.modelos;

import javax.swing.*;
import java.awt.*;

/**
 * Implementación específica del renderizador para la lista de contactos.
 */
public class ContactoRenderer extends BaseContactoRenderer implements ListCellRenderer<Contacto> {

    private static final long serialVersionUID = 1L;
    private static final int MAX_GREETING_LENGTH = 40;
    
    // Componentes específicos de este renderizador
    private JLabel saludoLabel;
    private JLabel telefonoLabel;
    
    /**
     * Constructor
     */
    public ContactoRenderer() {
        super();
    }
    
    @Override
    protected void inicializarContenidoPanel() {
        contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Nombre del contacto (parte superior)
        nombreLabel = new JLabel();
        nombreLabel.setFont(new Font(nombreLabel.getFont().getName(), Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 2, 5);
        contenidoPanel.add(nombreLabel, gbc);
        
        // Separador horizontal
        separador = new JSeparator();
        separador.setForeground(SEPARATOR_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 5, 2, 5);
        contenidoPanel.add(separador, gbc);
        
        // Área de saludo (parte central)
        saludoLabel = new JLabel();
        saludoLabel.setFont(new Font(saludoLabel.getFont().getName(), Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(2, 5, 2, 5);
        contenidoPanel.add(saludoLabel, gbc);
        
        // Teléfono o anfitrión (parte inferior)
        telefonoLabel = new JLabel();
        telefonoLabel.setFont(new Font(telefonoLabel.getFont().getName(), Font.ITALIC, 10));
        telefonoLabel.setForeground(PHONE_TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(2, 5, 0, 5);
        contenidoPanel.add(telefonoLabel, gbc);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, 
                                                  Contacto contacto, 
                                                  int index,
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
        
        // Configuración básica del contacto
        nombreLabel.setText(contacto.getNombre());
        
        // Configurar el saludo
        configurarSaludo(contacto);
        
        // Configurar teléfono o información de anfitrión
        configurarTelefono(contacto);
        
        // Configurar la imagen
        configurarImagen(contacto);
        
        // Aplicar estilos según selección y posición
        aplicarEstilos(list, index, isSelected);
        
        return this;
    }
    
    /**
     * Configura el texto o ícono del saludo según el tipo de contacto
     */
    private void configurarSaludo(Contacto contacto) {
        if (contacto instanceof ContactoIndividual) {
            String saludo = ((ContactoIndividual) contacto).getSaludo();
            if (saludo != null && !saludo.isEmpty()) {
                // Truncar texto si es demasiado largo
                if (saludo.length() > MAX_GREETING_LENGTH) {
                    saludo = saludo.substring(0, MAX_GREETING_LENGTH) + "...";
                }
                saludoLabel.setText(saludo);
                saludoLabel.setIcon(null);
            } else {
                saludoLabel.setText("<Sin saludo>");
                saludoLabel.setIcon(null);
            }
        } else {
            // Para grupos
            saludoLabel.setText("Grupo de chat");
            saludoLabel.setIcon(null);
        }
    }
    
    /**
     * Configura la información de teléfono o anfitrión según el tipo de contacto
     */
    private void configurarTelefono(Contacto contacto) {
        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual individual = (ContactoIndividual) contacto;
            String telefono = individual.getTelefono();
            // Formatear el teléfono para presentación
            telefonoLabel.setText("Tel: " + telefono);
        } else if (contacto instanceof Grupo) {
            Grupo grupo = (Grupo) contacto;
            String anfitrion = grupo.getAnfitrion();
            telefonoLabel.setText("Anfitrión: " + anfitrion);
        }
    }
    
    @Override
    protected void aplicarEstilosSeleccionado() {
        nombreLabel.setForeground(SELECTED_TEXT_COLOR);
        saludoLabel.setForeground(SELECTED_TEXT_COLOR);
        telefonoLabel.setForeground(new Color(60, 90, 140));
        separador.setForeground(SELECTED_SEPARATOR_COLOR);
    }
    
    @Override
    protected void aplicarEstilosNoSeleccionado(JList<?> list) {
        nombreLabel.setForeground(list.getForeground());
        saludoLabel.setForeground(new Color(60, 60, 60));
        telefonoLabel.setForeground(PHONE_TEXT_COLOR);
        separador.setForeground(SEPARATOR_COLOR);
    }
}