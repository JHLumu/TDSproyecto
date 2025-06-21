package umu.tds.persistencia;

import java.net.MalformedURLException;
import umu.tds.modelos.Mensaje;

/**
 * Interfaz que define las operaciones de persistencia para la entidad {@link Mensaje}.
 */
public interface MensajeDAO {

	/**
	 * Registra un nuevo mensaje en la base de datos.
	 *
	 * @param mensaje El objeto {@link Mensaje} a registrar.
	 */
	public void registrarMensaje(Mensaje mensaje);
	
	/**
	 * Recupera un mensaje de la base de datos por su identificador único.
	 *
	 * @param id El ID del mensaje a recuperar.
	 * @return El objeto {@link Mensaje} recuperado, o null si no se encuentra.
	 * @throws NumberFormatException Si el formato de un número recuperado de la base de datos es incorrecto.
	 * @throws MalformedURLException Si ocurre un error al construir una URL interna (por ejemplo, para un adjunto del mensaje).
	 */
	public Mensaje recuperarMensaje(int id) throws NumberFormatException, MalformedURLException;
	
}