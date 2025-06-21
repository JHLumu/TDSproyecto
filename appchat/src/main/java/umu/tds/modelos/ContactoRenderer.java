package umu.tds.modelos;

import javax.swing.*;

import umu.tds.appchat.AppChat;

import java.awt.*;

/**
 * Implementación específica del renderizador para la lista de contactos.
 */
public class ContactoRenderer extends BaseContactoRenderer implements ListCellRenderer<Contacto> {

    private static final long serialVersionUID = 1L;
    
    // Componentes específicos de este renderizador
    private JLabel saludoLabel;
    private JLabel telefonoLabel;
    
    /**
     * Constructor de la clase ContactoRenderer.
     */
    public ContactoRenderer() {
        super();
    }
    
    /**
     * Inicializa el contenido del panel, añadiendo los componentes JLabel para el nombre,
     * saludo y teléfono/anfitrión, junto con un separador.
     */
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

    /**
     * Devuelve un componente que ha sido configurado para mostrar el valor especificado.
     * Este método se llama para cada celda de la lista.
     * @param list la JList que está pidiendo el renderizador.
     * @param contacto el valor que debe mostrar la celda.
     * @param index el índice de la celda.
     * @param isSelected true si la celda especificada está seleccionada.
     * @param cellHasFocus true si la celda especificada tiene el foco.
     * @return un componente que ha sido configurado para mostrar el valor especificado.
     */
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
     * Configura el texto o ícono del saludo según el tipo de contacto.
     * Para ContactoIndividual, muestra el saludo del usuario.
     * Para Grupo, muestra el saludo predeterminado o un saludo específico de grupo.
     * @param contacto el Contacto para el cual se configurará el saludo.
     */
    private void configurarSaludo(Contacto contacto) {        
        saludoLabel.setText(AppChat.getInstancia().formatearSaludoVista(contacto));
        saludoLabel.setIcon(null);
    }
    
    /**
     * Configura la información de teléfono o anfitrión según el tipo de contacto.
     * Para ContactoIndividual, muestra el número de teléfono.
     * Para Grupo, muestra el nombre del anfitrión.
     * @param contacto el Contacto para el cual se configurará la información de teléfono/anfitrión.
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
    
    /**
     * Aplica los estilos visuales cuando la celda está seleccionada.
     * Cambia los colores de los textos y el separador para indicar la selección.
     */
    @Override
    protected void aplicarEstilosSeleccionado() {
        nombreLabel.setForeground(SELECTED_TEXT_COLOR);
        saludoLabel.setForeground(SELECTED_TEXT_COLOR);
        telefonoLabel.setForeground(new Color(60, 90, 140));
        separador.setForeground(SELECTED_SEPARATOR_COLOR);
    }
    
    /**
     * Aplica los estilos visuales cuando la celda no está seleccionada.
     * Restaura los colores de los textos y el separador a sus estados predeterminados.
     * @param list la JList que contiene la celda.
     */
    @Override
    protected void aplicarEstilosNoSeleccionado(JList<?> list) {
        nombreLabel.setForeground(list.getForeground());
        saludoLabel.setForeground(new Color(60, 60, 60));
        telefonoLabel.setForeground(PHONE_TEXT_COLOR);
        separador.setForeground(SEPARATOR_COLOR);
    }
}