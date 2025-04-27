package umu.tds.modelos;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto.TipoContacto;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Contacto> {
    
    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;
    private JLabel nombreLabel;
    private JLabel mensaje;

    public MensajeRenderer() {
        setLayout(new BorderLayout(5, 5));
      
        imageLabel = new JLabel();
        nombreLabel = new JLabel();
        mensaje = new JLabel();

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(nombreLabel, BorderLayout.CENTER);
        textPanel.add(mensaje, BorderLayout.SOUTH);

        add(imageLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto contacto, int index,
            boolean isSelected, boolean cellHasFocus) {
    	
    	AppChat controlador = AppChat.getInstancia();
        // Set the text fields
    	nombreLabel.setText(contacto.getNombre());
    	
    	// Si es un ContactoIndividual, mostrar el número de teléfono
    	if (contacto instanceof ContactoIndividual) {
    	    String mensaje = controlador.getUltimoMensajeContacto(contacto);
    	    this.mensaje.setText(mensaje);
    	}

    	try {
    	    // Obtener el usuario de la sesión actual
    	    if (controlador.getNombreUsuario() == null) {
    	        throw new IllegalStateException("Usuario de sesión actual no encontrado");
    	    }

    	    // Directorio base del usuario de la sesión actual
    	    String directorioBase = "imagenPerfilContactos\\" + 
    	    						controlador.getNombreUsuario() +  "-" + 
    	    						controlador.getTelefonoUsuario();

    	    // Crear subdirectorio específico para el contacto
    	    String subcarpeta;
    	    if (contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) {
    	        String telefono = ((ContactoIndividual) contacto).getTelefono();
    	        subcarpeta = contacto.getNombre() + "-" + telefono;
    	    } else { // Caso para grupos
    	        subcarpeta = contacto.getNombre() + "-GRUPO";
    	    }
    	    
    	    File directorio = new File(directorioBase, subcarpeta);
    	    if (!directorio.exists()) {
    	        directorio.mkdirs(); // Crear directorio si no existe
    	    }
    	    
    	    
    	    File localFile;
    	    if (contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) {
    	    // Ruta al archivo local
    	    	localFile = new File(directorio, contacto.getNombre() + "_" + ((ContactoIndividual) contacto).getTelefono() +".png");
    	    } else {
    	    	localFile = new File(directorio, contacto.getNombre() + "_GRUPO.png");
    	    }
    	    
    	    // Cargar la imagen local y establecerla en el JLabel
    	    if (localFile.exists()) { // Asegurarse de que el archivo se creó o ya existía
    	    	  System.out.println("[DEBUG ContactoRenderer getListCellRendererComponent]: Cargando imagen desde: " + localFile.getAbsolutePath());
    	    	Image localImage = ImageIO.read(localFile);
    	        ImageIcon imageIcon = new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    	        imageLabel.setIcon(imageIcon);
    	    } else {
    	    	 System.out.println("[DEBUG ContactoRenderer getListCellRendererComponent]: Usando ícono predeterminado para: " + contacto.getNombre());
    	        // Si no existe imagen local, usar un ícono predeterminado
    	        imageLabel.setIcon(new ImageIcon(ContactoRenderer.class.getResource("/resources/usuario_64.png")));
    	    }

    	} catch (IOException | IllegalStateException e) {
    	    e.printStackTrace();
    	    imageLabel.setIcon(null); // En caso de error, no establecer imagen
    	}

        // Set background and foreground based on selection
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
