package umu.tds.modelos;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Mensaje> {
	
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel;
	private JLabel imageLabel;

	public MensajeRenderer() {
		setLayout(new BorderLayout(5, 5));

		nameLabel = new JLabel();
		imageLabel = new JLabel();

		add(imageLabel, BorderLayout.WEST);
		add(nameLabel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		nameLabel.setText(mensaje.toString());

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
/*
			URL imageUrl = new URL("https://robohash.org/" + mensaje.getReceptor() + "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
			
*/
			URL imageUrl = new URL("https://robohash.org/" + mensaje.getReceptor() + "?size=50x50");
		    
		    // Crear la carpeta local específica si no existe
		    File directorio = new File("C:\\Users\\gojia\\Downloads\\appchat-iconos");
		    if (!directorio.exists()) {
		        directorio.mkdirs(); // Crear directorio y subdirectorios si es necesario
		    }
		    
		    // Ruta del archivo local
		    File localFile = new File(directorio, mensaje.getReceptor().getNombre()+".png");
		    
		    // Descargar y guardar la imagen
		    Image image = ImageIO.read(imageUrl);
		    ImageIO.write((java.awt.image.RenderedImage) image, "png", localFile);
		    
		    // Leer la imagen desde el archivo local
		    Image localImage = ImageIO.read(localFile);
		    ImageIcon imageIcon = new ImageIcon(localImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		    
		    // Establecer el ícono en el JLabel
		    imageLabel.setIcon(imageIcon);

		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
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
