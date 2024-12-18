package umu.tds.modelos;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.ImageIcon;

public class Mensaje {
	
	//Atributos de la clase
		private String texto; 
		private ImageIcon emoticono;
		private final Usuario emisor;
		private final Usuario receptor;
		private final LocalDate fechaEnvio;
		private final LocalTime horaEnvio;
	
	//A
		
	//Constructor de la clase
		
		/**
		 * Crea una nueva instancia de "Mensaje" con el usuario emisor, usuario receptor y el contenido que se envia
		 *
		 * @param emisor el usuario que envia el mensaje
		 * @param receptor el usuario que recibe el mensaje
		 * @param contenido el contenido del mensaje
		 *
		 */
		public Mensaje(Usuario emisor, Usuario receptor, String texto) {
			this.texto = texto;
			this.emisor = emisor;
			this.receptor = receptor;
			this.fechaEnvio = LocalDate.now();
			this.horaEnvio = LocalTime.now();
		}
		
		
		/**
		 * Crea una nueva instancia de "Mensaje" con el usuario emisor, usuario receptor y el contenido que se envia
		 *
		 * @param emisor el usuario que envia el mensaje
		 * @param receptor el usuario que recibe el mensaje
		 * @param contenido el contenido del mensaje
		 *
		 */
		public Mensaje(Usuario emisor, Usuario receptor, ImageIcon emoticono) {
			this(emisor,receptor, "");
			this.emoticono = emoticono;
		}

	//Metodos getter y setter
		public String getTexto() {
			return texto;
		}

		public ImageIcon getEmoticono() {
			return emoticono;
		}

		public Usuario getEmisor() {
			return emisor;
		}

		public Usuario getReceptor() {
			return receptor;
		}

		public LocalDate getFechaEnvio() {
			return fechaEnvio;
		}

		public LocalTime getHoraEnvio() {
			return horaEnvio;
		}
	
	public String getEmisorNombre() {
		return emisor.getNombre();
	}
	
	public String getEmisorTelf() {
		return emisor.getTelefono();
	}
	
	public boolean esEmitidoPor(Usuario usuario) {
        return this.emisor.equals(usuario);
    }
	
	@Override
	public String toString() {
		return emisor.getNombre() + " " + texto;
	}

	
}
