package umu.tds.persistencia;

/**
 * <p>Clase abstracta que define la interfaz de la factoría para obtener los objetos DAO (Data Access Object).</p>
 * <p>Sigue el patrón Singleton para asegurar que solo exista una instancia de la factoría.</p>
 */
public abstract class FactoriaDAO {

	/** Instancia única de la factoría DAO. */
	protected static FactoriaDAO instancia = null;
	
	/**
	 * Constructor protegido para evitar la instanciación directa.
	 */
	protected FactoriaDAO() {};
	
	/**
	 * Obtiene la instancia de la factoría DAO.
	 *
	 * @return La instancia de la factoría DAO.
	 */
	public static FactoriaDAO getFactoriaDAO() {
		return instancia;
	}
	
	/**
	 * Obtiene la instancia de la factoría DAO, inicializándola si es necesario.
	 * Utiliza el nombre de la clase para cargar dinámicamente la implementación de la factoría.
	 *
	 * @param nombre El nombre completamente cualificado de la clase de la factoría DAO a instanciar.
	 * @return La instancia de la factoría DAO.
	 */
	public static FactoriaDAO getInstancia(String nombre)  {
		 if (instancia == null)
			try {
				instancia = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 return instancia ;
		 }
	
	/**
	 * Método abstracto para obtener la interfaz DAO de Usuario.
	 *
	 * @return La implementación de {@link UsuarioDAO}.
	 */
	public abstract UsuarioDAO getUsuarioDAO();
	
	/**
	 * Método abstracto para obtener la interfaz DAO de Mensaje.
	 *
	 * @return La implementación de {@link MensajeDAO}.
	 */
	public abstract MensajeDAO getMensajeDAO();
	
	/**
	 * Método abstracto para obtener la interfaz DAO de Contacto.
	 *
	 * @return La implementación de {@link ContactoDAO}.
	 */
	public abstract ContactoDAO getContactoDAO();
	
}