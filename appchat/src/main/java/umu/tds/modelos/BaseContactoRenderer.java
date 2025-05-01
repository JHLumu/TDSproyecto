package umu.tds.modelos;

import javax.swing.*;

import umu.tds.appchat.AppChat;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase base abstracta para renderizadores de contactos.
 * Implementa el patrón Template Method para compartir estructura y estilos.
 */
public abstract class BaseContactoRenderer extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // Constantes compartidas para la apariencia visual
    protected static final int CELL_HEIGHT = 80;
    protected static final int ICON_SIZE = 54;
    protected static final int IMAGE_SIZE = 50;
    protected static final int PADDING = 6;
    
    // Colores compartidos para consistencia visual
    protected static final Color BORDER_COLOR = new Color(180, 180, 200);
    protected static final Color SEPARATOR_COLOR = new Color(200, 200, 220);
    protected static final Color PHONE_TEXT_COLOR = new Color(100, 100, 100);
    protected static final Color ROW_COLOR_EVEN = new Color(250, 250, 255);
    protected static final Color ROW_COLOR_ODD = new Color(240, 245, 250);
    protected static final Color SELECTED_BG_COLOR = new Color(210, 230, 255);
    protected static final Color SELECTED_TEXT_COLOR = new Color(20, 50, 120);
    protected static final Color SELECTED_BORDER_COLOR = new Color(100, 150, 200);
    protected static final Color SELECTED_SEPARATOR_COLOR = new Color(150, 170, 210);
    
    // Componentes de UI compartidos
    protected JPanel imagenPanel;
    protected JLabel imagenLabel;
    protected JPanel contenidoPanel;
    protected JLabel nombreLabel;
    protected JSeparator separador;
    
    /**
     * Constructor que configura la estructura básica del renderizador
     */
    public BaseContactoRenderer() {
        // Configuración del panel principal
        setLayout(new BorderLayout(PADDING, 0));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        setPreferredSize(new Dimension(0, CELL_HEIGHT));
        
        // Inicializar componentes comunes
        inicializarComponentes();
        
        // Agregar paneles al layout principal
        add(imagenPanel, BorderLayout.WEST);
        add(contenidoPanel, BorderLayout.CENTER);
    }
    
    /**
     * Inicializa los componentes básicos compartidos
     */
    protected void inicializarComponentes() {
        // Inicializar panel de imagen
        inicializarImagenPanel();
        
        // Inicializar panel de contenido
        inicializarContenidoPanel();
    }
    
    /**
     * Inicializa el panel de imagen con su configuración
     */
    protected void inicializarImagenPanel() {
        imagenPanel = new JPanel(new BorderLayout());
        imagenPanel.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        imagenPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        imagenPanel.setBackground(Color.WHITE);
        
        imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        imagenPanel.add(imagenLabel, BorderLayout.CENTER);
    }
    
    /**
     * Inicializa el panel de contenido base
     * Las clases hijas implementarán esto según sus necesidades específicas
     */
    protected abstract void inicializarContenidoPanel();
    
    /**
     * Configura la imagen del contacto
     */
    protected void configurarImagen(Contacto contacto) {
        try {
            File fileImagenContacto = getContactImageFile(contacto);
            
            if (fileImagenContacto != null && fileImagenContacto.exists()) {
                Image localImage = ImageIO.read(fileImagenContacto);
                imagenLabel.setIcon(new ImageIcon(localImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH)));
            } else {
                cargarImagenPredeterminada(contacto);
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            cargarImagenPredeterminada(contacto);
        }
    }
    
    /**
     * Obtiene el archivo de imagen según el tipo de contacto
     */
    protected File getContactImageFile(Contacto contacto) {
        AppChat controlador = AppChat.getInstancia();
        
        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
            if (controlador.esContacto(contacto)) {
                return new File("imagenesUsuarios", contactoIndividual.getUsuario().getNombre() + "-" + 
                               contactoIndividual.getTelefono() + ".png");
            }
        } else {
            Grupo contactoGrupo = (Grupo) contacto;
            return controlador.getGrupoFoto(contactoGrupo);
        }
        
        return null;
    }
    
    /**
     * Carga la imagen predeterminada cuando no existe una personalizada
     */
    protected void cargarImagenPredeterminada(Contacto contacto) {
        ImageIcon defaultIcon;
        
        if (contacto instanceof ContactoIndividual) {
            defaultIcon = new ImageIcon(getClass().getResource("/resources/usuario_64.png"));
        } else {
            defaultIcon = null;
        }
        
        if (defaultIcon != null) {
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(scaledDefaultImage));
        } else {
            System.err.println("No se encontró el icono predeterminado para: " + contacto.getNombre());
            imagenLabel.setIcon(null);
        }
    }
    
    /**
     * Aplica los estilos visuales según selección y posición
     */
    protected void aplicarEstilos(JList<?> list, int index, boolean isSelected) {
        // Colores alternados para filas
        Color background = (index % 2 == 0) ? ROW_COLOR_EVEN : ROW_COLOR_ODD;
        
        if (isSelected) {
            // Estilo para elementos seleccionados
            background = SELECTED_BG_COLOR;
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(PADDING-2, PADDING-2, PADDING-2, PADDING-2)
            ));
            aplicarEstilosSeleccionado();
        } else {
            // Estilo para elementos no seleccionados
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1),
                BorderFactory.createEmptyBorder(PADDING-1, PADDING-1, PADDING-1, PADDING-1)
            ));
            aplicarEstilosNoSeleccionado(list);
        }
        
        setBackground(background);
        setOpaque(true);
    }
    
    /**
     * Aplica estilos específicos para elementos seleccionados
     */
    protected abstract void aplicarEstilosSeleccionado();
    
    /**
     * Aplica estilos específicos para elementos no seleccionados
     */
    protected abstract void aplicarEstilosNoSeleccionado(JList<?> list);
}