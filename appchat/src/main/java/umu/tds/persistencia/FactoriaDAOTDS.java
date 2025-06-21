package umu.tds.persistencia;

/**
 * <p>Implementación concreta de {@link FactoriaDAO} para el sistema de persistencia TDS.</p>
 * <p>Esta clase proporciona las implementaciones específicas de los DAOs para Usuario, Mensaje y Contacto.</p>
 * <p>Sigue el patrón Singleton para asegurar que solo exista una instancia de esta factoría.</p>
 */
public class FactoriaDAOTDS extends FactoriaDAO {

	/**
	 * Constructor protegido para evitar la instanciación directa y asegurar el patrón Singleton.
	 */
	protected FactoriaDAOTDS() {};
	
	
	/**
	 * Obtiene la instancia de {@link FactoriaDAOTDS}.
	 * Si la instancia aún no ha sido creada, la inicializa.
	 *
	 * @return La instancia única de {@link FactoriaDAOTDS}.
	 */
	public static FactoriaDAO getFactoriaDAO() {
		if (instancia == null) instancia = new FactoriaDAOTDS();
		return instancia;
	}
	

	/**
	 * {@inheritDoc}
	 * <p>Retorna la implementación DAO para la entidad {@link umu.tds.modelos.Usuario}.</p>
	 * @return La instancia de {@link AdaptadorUsuarioDAOTDS}.
	 */
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAOTDS.getUsuarioDAO();
	}

	/**
	 * {@inheritDoc}
	 * <p>Retorna la implementación DAO para la entidad {@link umu.tds.modelos.Mensaje}.</p>
	 * @return La instancia de {@link AdaptadorMensajeDAOTDS}.
	 */
	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAOTDS.getMensajeDAO();
	}

	/**
	 * {@inheritDoc}
	 * <p>Retorna la implementación DAO para la entidad {@link umu.tds.modelos.Contacto}.</p>
	 * @return La instancia de {@link AdaptadorContactoDAO}.
	 */
	@Override
	public ContactoDAO getContactoDAO() {
		return AdaptadorContactoDAO.getContactoDAO();
	}

	
}
