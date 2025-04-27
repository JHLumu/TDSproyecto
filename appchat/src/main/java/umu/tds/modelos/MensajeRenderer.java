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
    		File fileImagenContacto;
    	    if(contacto instanceof ContactoIndividual) {
	    		ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
	    		//Incumple el patron de responsabilidad
	    		fileImagenContacto = new File("imagenesUsuarios", contactoIndividual.getUsuario().getNombre()+"-"+ contactoIndividual.getTelefono()+".png");
	    	} else {
	    		Grupo contactoGrupo = (Grupo) contacto;
	    		//Incumple el patron de responsabilidad
	    		fileImagenContacto = new File("imagenesUsuarios\\"+controlador.getTelefonoUsuario(), "Grupo-" + contactoGrupo.getNombre()+"-"+ contactoGrupo.getAnfitrion() + ".png");
	    	
	    	}
    		if (fileImagenContacto.exists()) {
    			Image localImage = ImageIO.read(fileImagenContacto);
    			imageLabel.setIcon(new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    		}
    		
    		//Se obtiene la carpeta con las imágenes de los contacto
    		else {
    	    	System.out.println("[DEBUG MensajeRenderer getListCellRendererComponent]: Usando ícono predeterminado para: " + contacto.getNombre());
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
