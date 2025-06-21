package umu.tds.modelos;

import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.List;
import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.FactoriaDAOTDS;
import umu.tds.persistencia.UsuarioDAO;

/**
 * Catálogo de usuarios que gestiona la carga, almacenamiento y acceso a los objetos {@link Usuario}.
 * Implementa el patrón Singleton para asegurar una única instancia del catálogo.
 */
public class CatalogoUsuarios {

	//Instancia unica del catálogo.
	private static CatalogoUsuarios catalogo = new CatalogoUsuarios();

	//Mapa que guarda para cada telefono la instancia de Usuario.
	private HashMap<String,Usuario> usuarios;
	
	//Atributos de Factoria DAO y Usuario DAO para la persistencia.
	private FactoriaDAO factoria;
	private UsuarioDAO usuarioDAO;
	
	/**
	 * Constructor privado para el patrón Singleton.
	 * Inicializa el mapa de usuarios y carga los usuarios desde la base de datos.
	 */
	private CatalogoUsuarios() {
		this.usuarios = new HashMap<String,Usuario>();
		factoria = FactoriaDAO.getInstancia(FactoriaDAOTDS.class.getName());
		usuarioDAO = factoria.getUsuarioDAO();
		cargarCatalogo();
		
		
	}
	
	/**
	 * Devuelve la única instancia del {@link CatalogoUsuarios}.
	 * @return La instancia del catálogo de usuarios.
	 */
	public static CatalogoUsuarios getInstancia() {
		
		return catalogo;
		
	}
	
	/**
	 * Obtiene un {@link Usuario} dado su número de teléfono.
	 * @param telefono El número de teléfono del usuario a buscar.
	 * @return El objeto Usuario si se encuentra, o null si no existe.
	 */
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono);
	}
	
	/**
	 * Añade un nuevo usuario al catálogo.
	 * @param usuario El objeto {@link Usuario} a añadir.
	 */
	public void nuevoUsuario(Usuario usuario) {
		usuarios.put(usuario.getTelefono(), usuario);
	}
	
	/**
	 * Verifica si un usuario está registrado en el catálogo.
	 * @param telefono El número de teléfono del usuario a verificar.
	 * @return true si el usuario está registrado, false en caso contrario.
	 */
	public boolean estaUsuarioRegistrado(String telefono) {
		return (getUsuario(telefono) != null);
	}
	
	/**
	 * Verifica si las credenciales (teléfono y contraseña) de un usuario son correctas.
	 * @param telefono El número de teléfono del usuario.
	 * @param password La contraseña del usuario.
	 * @return true si las credenciales son correctas, false en caso contrario.
	 */
	public boolean sonCredencialesCorrectas(String telefono, String password) {
		if (!estaUsuarioRegistrado(telefono)) return false;
		return (usuarios.get(telefono).getPassword().equals(password));
	}
	
	/**
	 * Carga todos los usuarios desde la capa de persistencia al catálogo.
	 * Los usuarios se almacenan en el mapa 'usuarios' usando su teléfono como clave.
	 */
	private void cargarCatalogo() {
		List<Usuario> aux;
		try {
			aux = usuarioDAO.recuperarTodosUsuarios();
			for(Usuario usuario : aux) {usuarios.put(usuario.getTelefono(), usuario);}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
}