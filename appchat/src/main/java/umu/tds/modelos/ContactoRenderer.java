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


public class ContactoRenderer extends JPanel implements ListCellRenderer<Contacto> {
    
    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;
    private JLabel nombreLabel;
    private JLabel tipoOtelfLabel;

    public ContactoRenderer() {
        setLayout(new BorderLayout(5, 5));
      
        imageLabel = new JLabel();
        nombreLabel = new JLabel();
        tipoOtelfLabel = new JLabel();

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(nombreLabel, BorderLayout.CENTER);
        textPanel.add(tipoOtelfLabel, BorderLayout.SOUTH);

        add(imageLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto contacto, int index,
            boolean isSelected, boolean cellHasFocus) {
    	
        // Set the text fields
    	nombreLabel.setText(contacto.getNombre());
    	
    	// Si es un ContactoIndividual, mostrar el número de teléfono
    	if (contacto instanceof ContactoIndividual) {
    	    String telefono = ((ContactoIndividual) contacto).getTelefono();
    	    tipoOtelfLabel.setText("Teléfono: " + telefono);
    	} else {
    	    tipoOtelfLabel.setText("Grupo");
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
	    		fileImagenContacto = new File("imagenesUsuarios\\"+ AppChat.getInstancia().getTelefonoUsuario(), "Grupo-" + contactoGrupo.getNombre()+"-"+ contactoGrupo.getAnfitrion() + ".png");
	    	
	    	}
    		if (fileImagenContacto.exists()) {
    			Image localImage = ImageIO.read(fileImagenContacto);
    			imageLabel.setIcon(new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    		}
    		
    		//Se obtiene la carpeta con las imágenes de los contacto
    		else {
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