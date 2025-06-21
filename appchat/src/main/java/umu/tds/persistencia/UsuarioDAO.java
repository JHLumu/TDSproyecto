package umu.tds.persistencia;

import java.net.MalformedURLException;
import java.util.List;

import umu.tds.modelos.Usuario;

/**
 * Interfaz que define las operaciones de persistencia para la entidad {@link Usuario}.
 */
public interface UsuarioDAO {

	/**
	 * Registra un nuevo usuario en la base de datos.
	 *
	 * @param usuario El objeto {@link Usuario} a registrar.
	 */
	public void registrarUsuario(Usuario usuario);
	
	/**
	 * Modifica un usuario existente en la base de datos.
	 *
	 * @param usuario El objeto {@link Usuario} a modificar.
	 */
	public void modificarUsuario(Usuario usuario);
	
	/**
	 * Recupera un usuario de la base de datos por su identificador único.
	 *
	 * @param id El ID del usuario a recuperar.
	 * @return El objeto {@link Usuario} recuperado, o null si no se encuentra.
	 * @throws MalformedURLException Si ocurre un error al construir una URL interna (por ejemplo, para una foto de perfil).
	 */
	public Usuario recuperarUsuario(int id) throws MalformedURLException;
	
	/**
	 * Recupera todos los usuarios de la base de datos.
	 *
	 * @return Una lista de todos los objetos {@link Usuario} registrados.
	 * @throws MalformedURLException Si ocurre un error al construir una URL interna (por ejemplo, para una foto de perfil).
	 */
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException;
}