package umu.tds.appchat.servicios;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

/**
 * Servicio para la exportación de conversaciones a formato PDF.
 */
public class ServicioExportacion {

	/**
	 * Exporta una conversación entre un usuario y un contacto a un archivo PDF.
	 * * @param usuario El usuario actual cuya conversación se va a exportar.
	 * @param contacto El contacto (individual o grupo) con el que se tuvo la conversación.
	 * @param destino El archivo de destino donde se guardará el PDF.
	 * @return true si la exportación fue exitosa, false en caso contrario.
	 */
	public static boolean exportarConversacion(Usuario usuario, Contacto contacto, File destino) {

	    List<Mensaje> mensajes = usuario.getChatMensaje(contacto);
	    String miTelefono = usuario.getTelefono();
	    String miNombre   = usuario.getNombre();
	    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	  
	    try (FileOutputStream fos = new FileOutputStream(destino)) {
	        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
	        PdfWriter.getInstance(doc, fos);
	        doc.open();

	        for (Mensaje m : mensajes) {
	            boolean enviadoPorMi = m.esEmisor(miTelefono);

	            String quien;
	            if (enviadoPorMi) quien = miNombre;
	            else quien = contacto.getNombre();
	            
	            String fechaHora = m.getFechaEnvio().format(formatoHora);
	            String linea;
	           
	            if (m.getEmoticono() != -1 ) {
	            	
	            	Image emoji = AppChat.getInstancia().obtenerEmojiRedimensionado(m.getEmoticono()).getImage();
	            	BufferedImage bufferedEmoji = new BufferedImage(emoji.getWidth(null), emoji.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	            	Graphics2D g = bufferedEmoji.createGraphics();
	            	g.drawImage(emoji, 0, 0, null);
	            	g.dispose();
	            	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	            	ImageIO.write(bufferedEmoji, "png", bytes);
	            	com.itextpdf.text.Image imagen = com.itextpdf.text.Image.getInstance(bytes.toByteArray());
	            	imagen.scaleToFit(30, 30);
	            	imagen.setSpacingBefore(5f);
	            	imagen.setSpacingAfter(5f);
	            	Paragraph lineaPrevia = new Paragraph(quien + " (" + fechaHora + "):");
	                if (enviadoPorMi) {
	                	imagen.setAlignment(Element.ALIGN_RIGHT);
	                	lineaPrevia.setAlignment(Element.ALIGN_RIGHT);
	                } else {
	                	imagen.setAlignment(Element.ALIGN_LEFT);
	                	lineaPrevia.setAlignment(Element.ALIGN_LEFT);
	                }
	                doc.add(lineaPrevia);
	                doc.add(imagen);
	            	
	            	
	            }
	            
	            else {linea = quien + " (" + fechaHora + "): " + m.getTexto();
	            
	            Paragraph parrafo = new Paragraph(linea);
	            if (enviadoPorMi) parrafo.setAlignment(Element.ALIGN_RIGHT);
	            else parrafo.setAlignment(Element.ALIGN_LEFT);
	            
	            parrafo.setSpacingAfter(5f);
	            doc.add(parrafo);
	            }
	        }

	        doc.close();
	        return true;

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}
	
}