package umu.tds.appchat.servicios.mensajes;

import java.time.LocalDateTime;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;

/**
 * Clase para almacenar un mensaje junto con su puntuación de coincidencia
 */
public class MensajeCoincidencia{
	private Mensaje mensaje;
	private Contacto contacto;
	
	/**
	 * Constructor de MensajeCoincidencia.
	 * @param mensaje El mensaje que coincide.
	 * @param contacto El contacto asociado al mensaje.
	 */
	public MensajeCoincidencia(Mensaje mensaje, Contacto contacto) {
		this.mensaje = mensaje;
		this.contacto = contacto;
	}
	
	/**
	 * Obtiene el mensaje original.
	 * @return El objeto Mensaje.
	 */
	public Mensaje getMensaje() {
		return mensaje;
	}
	
	/**
	 * Obtiene el contenido del mensaje.
	 * @return El contenido del mensaje (String o Integer si es un emoji).
	 */
	public Object getContenido() {
		return mensaje.getContenido();
	}
	
	/**
	 * Obtiene la fecha y hora de envío del mensaje.
	 * @return La fecha y hora de envío.
	 */
	public LocalDateTime getFechaEnvio() {
		return mensaje.getFechaEnvio();
	}
	
	/**
	 * Obtiene el contacto asociado al mensaje.
	 * @return El objeto Contacto.
	 */
	public Contacto getContacto() {
		return contacto;
	}
	
	/**
	 * Obtiene el nombre del contacto asociado al mensaje.
	 * @return El nombre del contacto.
	 */
	public String getNombre() {
		return contacto.getNombre();
	}

	/**
	 * Verifica si el emisor del mensaje coincide con el número de teléfono dado.
	 * @param telf El número de teléfono a comparar.
	 * @return true si el emisor es el mismo, false en caso contrario.
	 */
	public boolean esEmisor(String telf) {
		return mensaje.esEmisor(telf);
	}
	
}