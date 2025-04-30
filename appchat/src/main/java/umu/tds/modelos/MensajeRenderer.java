package umu.tds.modelos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import tds.BubbleText;
import umu.tds.appchat.AppChat;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Contacto> {

    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;
    private JLabel nombreLabel;
    private JLabel mensajeLabel;
    private JLabel fechaLabel;
    private JSeparator separador;

    public MensajeRenderer() {
        setLayout(new BorderLayout(8, 0));
        
        // Establecer una altura mínima para cada elemento
        setPreferredSize(new Dimension(0, 80)); // Altura fija para todos los elementos
        
        // Agregar padding interno
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Panel para la imagen de perfil con borde
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 1));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(54, 54)); // Tamaño fijo para el panel de imagen
        
        imageLabel = new JLabel();
        imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Panel para el contenido de texto con layout más controlado
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre en la parte superior con fuente más grande
        nombreLabel = new JLabel();
        nombreLabel.setFont(new Font(nombreLabel.getFont().getName(), Font.BOLD, 14));
        gbc.insets = new Insets(0, 0, 2, 0);
        textPanel.add(nombreLabel, gbc);
        
        // Separador entre nombre y mensaje
        separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 220));
        gbc.insets = new Insets(1, 0, 3, 0);
        textPanel.add(separador, gbc);
        
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
        textPanel.add(mensajePanel, gbc);
        
        // Fecha en la parte inferior
        fechaLabel = new JLabel();
        fechaLabel.setFont(new Font(fechaLabel.getFont().getName(), Font.ITALIC, 10));
        fechaLabel.setForeground(new Color(100, 100, 100));
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 0, 0);
        textPanel.add(fechaLabel, gbc);
        
        // Agregar paneles principales
        add(imagePanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto contacto, int index,
            boolean isSelected, boolean cellHasFocus) {
        
        // Limpiar componentes
        mensajeLabel.setText("");
        mensajeLabel.setIcon(null);
        
        AppChat controlador = AppChat.getInstancia();
        
        // Establecer nombre del contacto
        nombreLabel.setText(contacto.getNombre());
        
        // Procesar y establecer el último mensaje
        Object mensaje = controlador.getUltimoMensajeContacto(contacto);
        if(mensaje instanceof String) {
            String mensajeTexto = (String) mensaje;
            // Truncar mensaje si es demasiado largo (añadir "...")
            if(mensajeTexto.length() > 16) {
                mensajeTexto = mensajeTexto.substring(0, 15) + "...";
            }
            mensajeLabel.setText(mensajeTexto);
        }
        else {
            // Aquí es donde se establece el emoji
            mensajeLabel.setText("");
            
            // Obtener el emoji original
            ImageIcon emojiIcon = BubbleText.getEmoji((Integer) mensaje);
            Image img = emojiIcon.getImage();
            // Redimensionar a un tamaño más pequeño
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            mensajeLabel.setIcon(new ImageIcon(newImg));
        }
        
        // Asegurar que el separador siempre sea visible
        separador.setVisible(true);
        
        // Obtener y formatear la fecha del último mensaje
        LocalDateTime fechaMensaje = controlador.getUltimoMensajeFecha(contacto);
        if(fechaMensaje != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            fechaLabel.setText(fechaMensaje.format(formatter));
        } else {
            fechaLabel.setText("");
        }

        try {
            File fileImagenContacto = null;

            if(contacto instanceof ContactoIndividual) {
                ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
                if(controlador.esContacto(contacto))
                    fileImagenContacto = new File("imagenesUsuarios", contactoIndividual.getUsuario().getNombre()+"-"+ contactoIndividual.getTelefono()+".png");
            } else {
                Grupo contactoGrupo = (Grupo) contacto;
                fileImagenContacto = controlador.getGrupoFoto(contactoGrupo);
            }

            if (fileImagenContacto != null && fileImagenContacto.exists()) {
                Image localImage = ImageIO.read(fileImagenContacto);
                imageLabel.setIcon(new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            }
            else {
                System.out.println("[DEBUG MensajeRenderer getListCellRendererComponent]: Usando ícono predeterminado para: " + contacto.getNombre());
                ImageIcon defaultIcon = new ImageIcon(ContactoRenderer.class.getResource("/resources/usuario_64.png"));
                Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledDefaultImage));
            }

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            imageLabel.setIcon(null);
        }

        // Configurar colores y bordes según selección
        if (isSelected) {
            setBackground(new Color(210, 230, 255));
            setForeground(new Color(0, 0, 100));
            nombreLabel.setForeground(new Color(0, 0, 120));
            mensajeLabel.setForeground(new Color(20, 20, 80));
            separador.setForeground(new Color(150, 170, 210));
            
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
        } else {
            // Alternar colores para filas pares e impares
            if (index % 2 == 0) {
                setBackground(new Color(250, 250, 255));
            } else {
                setBackground(new Color(240, 245, 250));
            }
            
            nombreLabel.setForeground(list.getForeground());
            mensajeLabel.setForeground(new Color(60, 60, 60));
            separador.setForeground(new Color(200, 200, 220));
            
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
        
        // Asegurar que los componentes sean opacos correctamente
        setOpaque(true);
        
        return this;
    }
}
