package umu.tds.modelos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import umu.tds.appchat.AppChat;

/**
 * Renderizador personalizado para mostrar contactos en un JList con diseño moderno
 * similar a una aplicación de mensajería.
 */
public class ContactoRenderer extends JPanel implements ListCellRenderer<Contacto> {

    private static final long serialVersionUID = 1L;
    
    // Constantes para la apariencia visual
    private static final int CELL_HEIGHT = 80;
    private static final int ICON_SIZE = 54;
    private static final int IMAGE_SIZE = 50;
    private static final int PADDING = 6;
    private static final int MAX_GREETING_LENGTH = 40;
    
    // Colores
    private static final Color BORDER_COLOR = new Color(180, 180, 200);
    private static final Color SEPARATOR_COLOR = new Color(200, 200, 220);
    private static final Color PHONE_TEXT_COLOR = new Color(100, 100, 100);
    private static final Color ROW_COLOR_EVEN = new Color(250, 250, 255);
    private static final Color ROW_COLOR_ODD = new Color(240, 245, 250);
    private static final Color SELECTED_BG_COLOR = new Color(210, 230, 255);
    private static final Color SELECTED_BORDER_COLOR = new Color(100, 150, 200);
    private static final Color SELECTED_TEXT_COLOR = new Color(20, 50, 120);
    private static final Color SELECTED_SEPARATOR_COLOR = new Color(150, 170, 210);
    
    // Componentes de la UI
    private JPanel imagenPanel;
    private JLabel imagenLabel;
    private JPanel contenidoPanel;
    private JLabel nombreLabel;
    private JSeparator separador;
    private JLabel saludoLabel;
    private JLabel telefonoLabel;
    
    /**
     * Constructor que configura la estructura del renderizador
     */
    public ContactoRenderer() {
        // Configuración del panel principal
        setLayout(new BorderLayout(PADDING, PADDING));
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        setPreferredSize(new Dimension(0, CELL_HEIGHT));
        
        // Panel de imagen (izquierda)
        inicializarImagenPanel();
        
        // Panel de contenido (derecha)
        inicializarContenidoPanel();
        
        // Agregar paneles al layout principal
        add(imagenPanel, BorderLayout.WEST);
        add(contenidoPanel, BorderLayout.CENTER);
    }
    
    /**
     * Inicializa el panel de imagen con su configuración
     */
    private void inicializarImagenPanel() {
        imagenPanel = new JPanel(new BorderLayout());
        imagenPanel.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        imagenPanel.setBorder(new LineBorder(BORDER_COLOR, 1));
        imagenPanel.setBackground(Color.WHITE);
        
        imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        imagenPanel.add(imagenLabel, BorderLayout.CENTER);
    }
    
    /**
     * Inicializa el panel de contenido con GridBagLayout
     */
    private void inicializarContenidoPanel() {
        contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        
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
            if (saludo != null) {
                // Truncar texto si es demasiado largo
                if (saludo.length() > MAX_GREETING_LENGTH) {
                    saludo = saludo.substring(0, MAX_GREETING_LENGTH) + "...";
                }
                saludoLabel.setText(saludo);
                saludoLabel.setIcon(null);
            } else {
                saludoLabel.setText("");
                // Aquí podrías poner un ícono predeterminado si no hay saludo
            }
        } else {
            // Implementar aquí el manejo de saludos para Grupos si es necesario
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
    
    /**
     * Configura la imagen del contacto
     */
    private void configurarImagen(Contacto contacto) {
        try {
            File imageFile = getContactImageFile(contacto);
            
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                Image scaledImage = originalImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                loadDefaultImage(contacto);
            }
        } catch (IOException | IllegalStateException e) {
            System.err.println("Error cargando imagen para: " + contacto.getNombre());
            loadDefaultImage(contacto);
        }
    }
    
    /**
     * Obtiene el archivo de imagen según el tipo de contacto
     */
    private File getContactImageFile(Contacto contacto) {
        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual individual = (ContactoIndividual) contacto;
            String fileName = individual.getUsuario().getNombre() + "-" + individual.getTelefono() + ".png";
            return new File("imagenesUsuarios", fileName);
        } else {
            Grupo grupo = (Grupo) contacto;
            String anfitrion = AppChat.getInstancia().getTelefonoContacto(grupo);
            String fileName = "Grupo-" + grupo.getNombre() + "-" + grupo.getAnfitrion() + ".png";
            return new File("imagenesUsuarios" + File.separator + anfitrion, fileName);
        }
    }
    
    /**
     * Carga la imagen predeterminada cuando no existe una personalizada
     */
    private void loadDefaultImage(Contacto contacto) {
        if (contacto instanceof ContactoIndividual) {
            imagenLabel.setIcon(new ImageIcon(ContactoRenderer.class.getResource("/resources/usuario_64.png")));
        } else {
            imagenLabel.setIcon(new ImageIcon(ContactoRenderer.class.getResource("/resources/grupo_64.png")));
        }
    }
    
    /**
     * Aplica los estilos visuales según selección y posición
     */
    private void aplicarEstilos(JList<? extends Contacto> list, int index, boolean isSelected) {
        // Colores alternados para filas
        Color background = (index % 2 == 0) ? ROW_COLOR_EVEN : ROW_COLOR_ODD;
        
        if (isSelected) {
            // Estilo para elementos seleccionados
            background = SELECTED_BG_COLOR;
            setBorder(new CompoundBorder(
                new LineBorder(SELECTED_BORDER_COLOR, 2),
                new EmptyBorder(PADDING-2, PADDING-2, PADDING-2, PADDING-2)
            ));
            nombreLabel.setForeground(SELECTED_TEXT_COLOR);
            saludoLabel.setForeground(SELECTED_TEXT_COLOR);
            telefonoLabel.setForeground(SELECTED_TEXT_COLOR);
            separador.setForeground(SELECTED_SEPARATOR_COLOR);
        } else {
            // Estilo para elementos no seleccionados
            setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
            nombreLabel.setForeground(list.getForeground());
            saludoLabel.setForeground(list.getForeground());
            telefonoLabel.setForeground(PHONE_TEXT_COLOR);
            separador.setForeground(SEPARATOR_COLOR);
        }
        
        setBackground(background);
        contenidoPanel.setBackground(background);
    }
}