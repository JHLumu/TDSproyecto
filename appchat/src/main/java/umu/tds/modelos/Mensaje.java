package umu.tds.modelos;

import java.time.LocalDateTime;

import javax.swing.ImageIcon;

public class Mensaje implements Comparable<Mensaje>{
	
	//Atributos de la clase
	private Integer codigo;
	private String texto; 
	private ImageIcon emoticono;
	private final Usuario emisor;
	private final Usuario receptor;
	private LocalDateTime fechaEnvio;
	
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
		this.fechaEnvio = LocalDateTime.now();
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

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}
	
	public void setFechaEnvio(LocalDateTime fecha) {
		this.fechaEnvio = fecha;
	}
	
	public Integer getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
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


	@Override
	//Un mensaje A va despues de un mensaje B si A ha ocurrido despues de B.
	public int compareTo(Mensaje o) {
		
		if (this.fechaEnvio.isAfter(o.getFechaEnvio())) 
			
			return 1;
		
		else if (this.fechaEnvio.isEqual(o.getFechaEnvio())) {
			
			return 0;
		}
		
		else return -1;
	}



	
	
	
}
