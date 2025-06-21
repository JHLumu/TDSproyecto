package umu.tds.modelos;

import java.time.LocalDateTime;

public class Mensaje implements Comparable<Mensaje>{
	
	//Atributos de la clase
	private int codigo;
	private String texto; 
	private int emoticono;
	private final Usuario emisor;
	private final Usuario receptor;
	private LocalDateTime fechaEnvio;
	private final Grupo grupo;
	
	//A
		
	//Constructor de la clase
		
	/**
	 * Crea una nueva instancia de "Mensaje" con el usuario emisor, usuario receptor y el contenido que se envia
	 *
	 * @param emisor el usuario que envia el mensaje
	 * @param receptor el usuario que recibe el mensaje
	 * @param texto el contenido de texto del mensaje
	 * @param grupo el grupo al que pertenece el mensaje, o null si es un mensaje individual
	 *
	 */
	public Mensaje(Usuario emisor, Usuario receptor, String texto, Grupo grupo) {
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.fechaEnvio = LocalDateTime.now();
		this.codigo = 0;
		this.grupo = grupo;
		this.emoticono = -1;
	}
	
	
	/**
	 * Crea una nueva instancia de "Mensaje" con el usuario emisor, usuario receptor y el contenido que se envia, en este caso un emoticono.
	 *
	 * @param emisor el usuario que envia el mensaje
	 * @param receptor el usuario que recibe el mensaje
	 * @param emoticono el código del emoticono del mensaje
	 * @param grupo el grupo al que pertenece el mensaje, o null si es un mensaje individual
	 *
	 */
	public Mensaje(Usuario emisor, Usuario receptor, int emoticono, Grupo grupo) {
		this(emisor,receptor, "", grupo);
		this.emoticono = emoticono;
	}


	//Metodos getter y setter
	/**
	 * Obtiene el texto del mensaje.
	 * @return el texto del mensaje.
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Obtiene el código del emoticono del mensaje.
	 * @return el código del emoticono.
	 */
	public int getEmoticono() {
		return emoticono;
	}

	/**
	 * Obtiene el usuario emisor del mensaje.
	 * @return el usuario emisor.
	 */
	public Usuario getEmisor() {
		return emisor;
	}

	/**
	 * Obtiene el usuario receptor del mensaje.
	 * @return el usuario receptor.
	 */
	public Usuario getReceptor() {
		return receptor;
	}

	/**
	 * Obtiene la fecha y hora de envío del mensaje.
	 * @return la fecha y hora de envío.
	 */
	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}
	
	/**
	 * Establece la fecha y hora de envío del mensaje.
	 * @param fecha la nueva fecha y hora de envío.
	 */
	public void setFechaEnvio(LocalDateTime fecha) {
		this.fechaEnvio = fecha;
	}
	
	/**
	 * Obtiene el código identificador del mensaje.
	 * @return el código del mensaje.
	 */
	public int getCodigo() {
		return codigo;
	}


	/**
	 * Establece el código identificador del mensaje.
	 * @param codigo el nuevo código del mensaje.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
	/**
	 * Obtiene el nombre del usuario emisor.
	 * @return el nombre del emisor.
	 */
	public String getEmisorNombre() {
		return emisor.getNombre();
	}
	
	/**
	 * Obtiene el número de teléfono del usuario emisor.
	 * @return el teléfono del emisor.
	 */
	public String getEmisorTelf() {
		return emisor.getTelefono();
	}
	
	/**
	 * Verifica si el mensaje fue emitido por un usuario específico.
	 * @param usuario el usuario a comparar.
	 * @return true si el mensaje fue emitido por el usuario, false en caso contrario.
	 */
	public boolean esEmitidoPor(Usuario usuario) {
        return this.emisor.equals(usuario);
    }
	
	/**
	 * Devuelve una representación en cadena del objeto Mensaje.
	 * @return una cadena que representa el mensaje.
	 */
	@Override
	public String toString() {
		return emisor.getNombre() + " " + receptor.getNombre() + " " + texto + " Emoticono: " + emoticono + " " + fechaEnvio;
	}

	/**
	 * Compara este mensaje con otro mensaje basado en su fecha de envío.
	 * Un mensaje A va después de un mensaje B si A ha ocurrido después de B.
	 * @param o el mensaje con el que se va a comparar.
	 * @return un valor negativo si este mensaje es anterior, cero si son iguales, o un valor positivo si este mensaje es posterior.
	 */
	@Override
	public int compareTo(Mensaje o) {
	    return this.fechaEnvio.compareTo(o.getFechaEnvio());
	}


	/**
	 * Obtiene el ID del grupo al que pertenece el mensaje.
	 * @return el ID del grupo, o -1 si el mensaje no pertenece a un grupo.
	 */
	public int getIDGrupo() {
		if(grupo!= null)
			return this.grupo.getCodigo();
		else
			return -1;
	}
	
	/**
	 * Obtiene el objeto Grupo al que pertenece el mensaje.
	 * @return el objeto Grupo, o null si el mensaje no pertenece a un grupo.
	 */
	public Grupo getGrupo() {
		return this.grupo;
	}
	 
	/**
	 * Obtiene el nombre del grupo al que pertenece el mensaje.
	 * @return el nombre del grupo.
	 */
	public String getNombreGrupo() {
		return this.grupo.getNombre();
	}

	/**
	 * Obtiene el número de teléfono del usuario receptor.
	 * @return el teléfono del receptor.
	 */
	public String getReceptorTelf() {
		return this.receptor.getTelefono();
	}
	
	/**
	 * Obtiene el contenido del mensaje, que puede ser texto o un emoticono.
	 * @return el contenido del mensaje (String si es texto, Integer si es emoticono).
	 */
	public Object getContenido() {
		if (!getTexto().isEmpty())
			return getTexto();
		else 
			return getEmoticono();
	}
	
	/**
	 * Verifica si el emisor y el receptor del mensaje son el mismo usuario.
	 * Esto es útil para identificar mensajes de grupo.
	 * @return true si el emisor y el receptor son el mismo, false en caso contrario.
	 */
	public boolean igualEmisorReceptor() {
		return this.emisor.equals(receptor);
	}
	
	/**
	 * Verifica si el emisor del mensaje tiene el número de teléfono especificado.
	 * @param telf el número de teléfono a verificar.
	 * @return true si el emisor tiene el teléfono especificado, false en caso contrario.
	 */
	public boolean esEmisor(String telf) {
		return this.emisor.getTelefono().equals(telf);
	}
	
}