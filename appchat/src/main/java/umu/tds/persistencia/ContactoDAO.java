package umu.tds.persistencia;

import java.net.MalformedURLException;

import umu.tds.modelos.Contacto;


/**
 * Interfaz que define las operaciones de persistencia para la entidad {@link Contacto}.
 */
public interface ContactoDAO {

	/**
	 * Registra un nuevo contacto en la base de datos.
	 *
	 * @param contacto El objeto {@link Contacto} a registrar.
	 */
	public void registrarContacto(Contacto contacto);
	
	/**
	 * Modifica un contacto existente en la base de datos.
	 *
	 * @param contacto El objeto {@link Contacto} a modificar.
	 */
	public void modificarContacto(Contacto contacto);
	
	/**
	 * Recupera un contacto de la base de datos por su identificador único.
	 *
	 * @param id El ID del contacto a recuperar.
	 * @return El objeto {@link Contacto} recuperado, o null si no se encuentra.
	 * @throws NumberFormatException Si el formato de un número recuperado de la base de datos es incorrecto.
	 * @throws MalformedURLException Si ocurre un error al construir una URL interna (por ejemplo, para una foto de perfil del contacto).
	 */
	public Contacto recuperarContacto(int id) throws NumberFormatException, MalformedURLException;
	
}