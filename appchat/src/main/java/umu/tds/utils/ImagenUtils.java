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

public class ImagenUtils {

	//Constantes
	public static final String DIRECTORIO_IMAGENES_USUARIO = "imagenesUsuarios";
	public static final String IMAGEN_USUARIOS_PORDEFECTO = "/resources/usuario_64.png";
	public static final String IMAGEN_GRUPOS_PORDEFECTO = "/resources/grupo_64.png";

	
	
	/**
	 * Calcula la ruta de la imagen tanto de un usuario, un contacto individual o un grupo
	 * 
	 * @param objeto instancia Usuario, ContactoIndividual o Grupo.
	 * @return Ruta de la imagen del icono de la aplicación si existe, null en caso contrario.
	 * */
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
	
	public static URL getURL(Object objeto) {
		System.out.println("soy gilipollas");
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
	
	public static Image getImagenPorDefecto(Object objeto) {
		
		URL ficheroLocal = null;
		Image resultado = null;
		if (objeto instanceof Usuario || objeto instanceof ContactoIndividual) ficheroLocal = ImagenUtils.class.getResource(ImagenUtils.IMAGEN_USUARIOS_PORDEFECTO);
		else if (objeto instanceof Grupo) ficheroLocal = ImagenUtils.class.getResource(ImagenUtils.IMAGEN_GRUPOS_PORDEFECTO);
		else return null;
			
		try {
			if (ficheroLocal != null) resultado = ImageIO.read(ficheroLocal);
			System.out.println("[DEBUG ImagenUtils getImagenPorDefecto]: Se ha leido correctamente la imagen por defecto.");
			return resultado;
		
		} catch (IOException e) {}
		
		
		System.out.println("[DEBUG ImagenUtils getImagenPorDefecto]: Se devuelve null.");
		return null;
	}
	
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
	            System.err.println("[ERROR ImagenUtils getImagenAPartirDeURL] URL mal formada: " + objeto);
	            return null;
	        }
	    } else {
	        System.err.println("[ERROR ImagenUtils getImagenAPartirDeURL] Parámetro debe ser URL o String, fue: " + objeto);
	        return null;
	    }
	
		try {
			resultado = ImageIO.read(url);
			 if (resultado == null) System.err.println("[ERROR ImagenUtils getImagenAPartirDeURL] No se pudo leer la imagen desde: " + url);
			
			
		} catch (IOException e) { System.err.println("[ERROR ImagenUtils getImagenAPartirDeURL] No se pudo descargar la imagen desde: " + url);}
		
		return resultado;
	}
	
	//TODO: Revisar comportamiento del cambio de imagen del usuario
	/**
	 * 
	 * Devuelve la imagen de un usuario, contacto o grupo. En caso de que
	 * la imagen no se encuentre de manera local, se intentará descargarla
	 * de nuevo a partir de la URL. En caso de que no sea posible o haya fallado, se 
	 * devolverá la imagen por defecto.
	 * 
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
				System.out.println("[DEBUG ImagenUtils getImagen]: Se devuelve la imagen al estar guardado de manera local.");
				return resultado;
			} catch (IOException e) {}
			
		}
		
		//Paso 2: Comprobar si la URL es válida y en caso de que sea asi, descargar imagen
			resultado = ImagenUtils.getImagenAPartirDeURL(objeto);
			if (resultado != null) {
				
				System.out.println("[DEBUG ImagenUtils getImagen]: Se ha leido correctamente la imagen tras descargarse por URL.");
				
				try {
					ImageIO.write((BufferedImage) resultado, "png", ImagenUtils.getFile(AppChat.getInstancia().getUsuarioActual()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return resultado;
				
			}	

		//Paso 3: Devolver imagen por defecto
		return ImagenUtils.getImagenPorDefecto(objeto);
		
	}
	
	
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		return resultado;
		
	}
	
	
	
}




