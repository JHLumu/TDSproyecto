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
	
	public MensajeCoincidencia(Mensaje mensaje, Contacto contacto) {
		this.mensaje = mensaje;
		this.contacto = contacto;
	}
	
	public Mensaje getMensaje() {
		return mensaje;
	}
	
	public Object getContenido() {
		return mensaje.getContenido();
	}
	
	public LocalDateTime getFechaEnvio() {
		return mensaje.getFechaEnvio();
	}
	
	public Contacto getContacto() {
		return contacto;
	}
	
	public String getNombre() {
		return contacto.getNombre();
	}

	public boolean esEmisor(String telf) {
		// TODO Auto-generated method stub
		return mensaje.esEmisor(telf);
	}
	
}
