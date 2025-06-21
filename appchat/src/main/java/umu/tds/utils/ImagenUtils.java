package umu.tds.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Usuario;

/**
 * Clase de utilidad para el manejo de imágenes en la aplicación, incluyendo la obtención,
 * carga, guardado y manejo de imágenes por defecto para usuarios y grupos.
 */
public class ImagenUtils {

	//Constantes
	/**
	 * Directorio base donde se guardan las imágenes de los usuarios.
	 */
	public static final String DIRECTORIO_IMAGENES_USUARIO = "imagenesUsuarios";
	/**
	 * Ruta de la imagen por defecto para usuarios individuales.
	 */
	public static final String IMAGEN_USUARIOS_PORDEFECTO = "/resources/usuario_64.png";
	/**
	 * Ruta de la imagen por defecto para grupos.
	 */
	public static final String IMAGEN_GRUPOS_PORDEFECTO = "/resources/grupo_64.png";

	
	
	/**
	 * Calcula la ruta de archivo para la imagen de un objeto dado, que puede ser un Usuario,
	 * ContactoIndividual o Grupo. Si el directorio padre no existe, lo crea.
	 * @param objeto Instancia de Usuario, ContactoIndividual o Grupo.
	 * @return Un objeto File que representa la ruta de la imagen del icono de la aplicación,
	 * o null si el objeto no es de un tipo soportado.
	 */
	public static File getFile(Object objeto) {
		File resultado = null;
		
		if (objeto instanceof Usuario) {
			Usuario usuario = (Usuario) objeto;
			File directorioBase = new File(ImagenUtils.DIRECTORIO_IMAGENES_USUARIO,  String.valueOf(usuario.getTelefono()));
			resultado = new File(directorioBase, usuario.getNombre()+"-"+usuario.getTelefono()+".png");
			
			
		}

		else if (objeto instanceof ContactoIndividual) {
			
			ContactoIndividual contactoIndividual = (ContactoIndividual) objeto;
			File directorioBase = new File(ImagenUtils.DIRECTORIO_IMAGENES_USUARIO, String.valueOf(contactoIndividual.getTelefono()));
			resultado = new File(directorioBase, contactoIndividual.getNombreUsuario()+"-"+contactoIndividual.getTelefono()+".png");
			
		
		}
		
		else if (objeto instanceof Grupo) {
			
			Grupo grupo = (Grupo) objeto;
			File directorioBase = new File(ImagenUtils.DIRECTORIO_IMAGENES_USUARIO, String.valueOf(grupo.getAnfitrion()));
			resultado = new File(directorioBase, "Grupo-" + grupo.getNombre()+".png");
			
			
		}
		
		else return null;
		
		//Siempre crea el directorio y devuelve la ruta, exista o no.
		File directorioPadre = resultado.getParentFile();
		if (directorioPadre != null && !directorioPadre.exists()) directorioPadre.mkdirs();
		return resultado;
		
	}
	
	/**
	 * Obtiene la URL de la imagen asociada a un objeto dado, que puede ser un Usuario o Contacto.
	 * @param objeto Instancia de Usuario o Contacto.
	 * @return La URL de la imagen del objeto, o null si el objeto no es de un tipo soportado.
	 */
	public static URL getURL(Object objeto) {
		if (objeto instanceof Usuario) {
			
			Usuario usuario = (Usuario) objeto;
			return usuario.getURLImagen();
			
		}
		
		else if (objeto instanceof Contacto) {
			
			Contacto contacto = (Contacto) objeto;
			return contacto.getURLImagen();
			
		}
		
		else return null;
		
	}
	
	/**
	 * Obtiene la imagen por defecto para un objeto dado (Usuario, ContactoIndividual o Grupo).
	 * @param objeto Instancia de Usuario, ContactoIndividual o Grupo.
	 * @return La imagen por defecto correspondiente, o null si ocurre un error o el objeto no es de un tipo soportado.
	 */
	public static Image getImagenPorDefecto(Object objeto) {
		
		URL ficheroLocal = null;
		Image resultado = null;
		if (objeto instanceof Usuario || objeto instanceof ContactoIndividual) ficheroLocal = ImagenUtils.class.getResource(ImagenUtils.IMAGEN_USUARIOS_PORDEFECTO);
		else if (objeto instanceof Grupo) ficheroLocal = ImagenUtils.class.getResource(ImagenUtils.IMAGEN_GRUPOS_PORDEFECTO);
		else return null;
			
		try {
			if (ficheroLocal != null) resultado = ImageIO.read(ficheroLocal);
			return resultado;
		
		} catch (IOException e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Carga una imagen a partir de una URL o una cadena que representa una URL.
	 * @param objeto Un objeto URL o una cadena String que representa una URL.
	 * @return La imagen cargada, o null si la URL es inválida o ocurre un error de E/S.
	 */
	public static Image getImagenAPartirDeURL(Object objeto) {
		
		URL url = null;
		Image resultado = null;
		
		// 1) Convertir el parámetro a URL
	    if (objeto instanceof URL) {
	        url = (URL) objeto;
	    } else if (objeto instanceof String) {
	        try {
	            url = new URL((String) objeto);
	        } catch (MalformedURLException e) {
	        	e.printStackTrace();
	            return null;
	        }
	    }
	    
	    else return null;

	
		try {
			resultado = ImageIO.read(url);
			
		} catch (IOException e) {e.printStackTrace();}
		
		return resultado;
	}
	/**
	 * Devuelve la imagen de un usuario, contacto o grupo. Primero intenta cargar la imagen
	 * de manera local, si no está disponible, intenta descargarla desde la URL asociada.
	 * Si ambos intentos fallan, devuelve la imagen por defecto.
	 * @param objeto Instancia de Usuario, Contacto o Grupo.
	 * @return La imagen del objeto (local, descargada o por defecto), o null si no se puede obtener ninguna imagen.
	 */
	public static Image getImagen(Object objeto) {
		
		Image resultado = null;
	
		//1. Comprobar local.
		//2. Comprobar URL.
		//3. Imagen por defecto.
		
		
		//Paso 1: Comprobar si se tiene la imagen de manera local.
		File ficheroLocal = ImagenUtils.getFile(objeto);
		if (ficheroLocal != null) {
			try {
				resultado = ImageIO.read(ficheroLocal);
				return resultado;
			} catch (IOException e) {}
			
		}
		
		//Paso 2: Comprobar si la URL es válida y en caso de que sea asi, descargar imagen
			resultado = ImagenUtils.getImagenAPartirDeURL(objeto);
			if (resultado != null) {
				try {
					// Intenta guardar la imagen descargada localmente para el usuario actual.
					// Nota: Podría ser un error guardar la imagen del 'objeto' en el path del 'usuarioActual'.
					ImageIO.write((BufferedImage) resultado, "png", ImagenUtils.getFile(AppChat.getInstancia().getUsuarioActual()));
				} catch (IOException e) {e.printStackTrace();}
				return resultado;
				
			}	

		//Paso 3: Devolver imagen por defecto
		return ImagenUtils.getImagenPorDefecto(objeto);
		
	}
	
	/**
	 * Guarda la imagen de un objeto (Usuario, Contacto, Grupo) desde su URL asociada a un archivo local.
	 * @param objeto Instancia de Usuario, Contacto o Grupo de la cual se desea guardar la imagen.
	 * @return true si la imagen se guardó exitosamente, false en caso contrario.
	 */
	public static boolean guardarImagen(Object objeto) {
		boolean resultado = false;
		File ficheroLocal = ImagenUtils.getFile(objeto);
		URL URLimagen = ImagenUtils.getURL(objeto);
		BufferedImage imagen = null;
		
		if (ficheroLocal != null && URLimagen != null) {
			try {
				imagen = ImageIO.read(URLimagen);
				ImageIO.write(imagen, "png", ficheroLocal);
				resultado = true;
			} catch (IOException e) {e.printStackTrace();}
		}
		return resultado;	
	}
}