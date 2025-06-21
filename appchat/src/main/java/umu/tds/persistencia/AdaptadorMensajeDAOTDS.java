package umu.tds.persistencia;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

/**
 * <p>Adaptador que implementa la interfaz {@link MensajeDAO} utilizando el servicio de persistencia TDS.</p>
 * <p>Esta clase se encarga de mapear objetos {@link Mensaje} a entidades persistibles
 * y viceversa, gestionando su almacenamiento y recuperación.</p>
 * <p>Sigue el patrón Singleton para asegurar una única instancia del adaptador.</p>
 */
public class AdaptadorMensajeDAOTDS implements MensajeDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeDAOTDS instancia = new AdaptadorMensajeDAOTDS();
	
	/**
	 * Obtiene la única instancia del AdaptadorMensajeDAOTDS.
	 *
	 * @return La instancia de {@link AdaptadorMensajeDAOTDS}.
	 */
	public static AdaptadorMensajeDAOTDS getMensajeDAO() {
		return instancia;
	}
	
	/**
	 * Constructor privado para aplicar el patrón Singleton.
	 * Inicializa el servicio de persistencia.
	 */
	private AdaptadorMensajeDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	/**
	 * {@inheritDoc}
	 * <p>Registra un nuevo mensaje en el sistema de persistencia.</p>
	 * <p>Antes de registrar, comprueba si el mensaje ya está registrado.</p>
	 * @param mensaje El objeto {@link Mensaje} a registrar.
	 */
	@Override
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;
		try {
			
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
			
		}catch(NullPointerException e) {};
		
		if (eMensaje != null) {
			return;
		}
		
		eMensaje = new Entidad();
		eMensaje.setNombre("Mensaje");
		
		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		propiedades.addAll(Arrays.asList(
				new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("emoticono", String.valueOf(mensaje.getEmoticono())),
				new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
				new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
				new Propiedad("fecha", mensaje.getFechaEnvio().toString()),
				new Propiedad("grupo", String.valueOf(mensaje.getIDGrupo()))
				));
		eMensaje.setPropiedades(propiedades);
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		mensaje.setCodigo(eMensaje.getId());
		
	}
	
	/**
	 * {@inheritDoc}
	 * <p>Recupera un mensaje de la base de datos por su identificador único.</p>
	 * <p>Si el mensaje ya está en el pool de objetos, lo retorna directamente. De lo contrario,
	 * lo recupera del servicio de persistencia, lo construye y lo añade al pool.</p>
	 * @param id El ID del mensaje a recuperar.
	 * @return El objeto {@link Mensaje} recuperado.
	 * @throws NumberFormatException Si el formato de un ID de emisor, receptor o grupo es incorrecto.
	 * @throws MalformedURLException Si ocurre un error al construir una URL interna (por ejemplo, para un adjunto del mensaje).
	 */
	@Override
	public Mensaje recuperarMensaje(int id) throws NumberFormatException, MalformedURLException {

		PoolDAO poolMensaje = PoolDAO.getUnicaInstancia();
		if (poolMensaje.contiene(id)) {
			return (Mensaje) poolMensaje.getObjeto(id);
		}
		
		Entidad eMensaje = servPersistencia.recuperarEntidad(id);
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		
		String idEmisor = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor");
		Usuario emisor = FactoriaDAO.getFactoriaDAO().getUsuarioDAO().recuperarUsuario(Integer.parseInt(idEmisor));

		
		String idReceptor = servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor");
		Usuario receptor = FactoriaDAO.getFactoriaDAO().getUsuarioDAO().recuperarUsuario(Integer.parseInt(idReceptor));
		
		String emoticonoString = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticono");
		int emoticono = Integer.parseInt(emoticonoString);
		
		String grupoString = servPersistencia.recuperarPropiedadEntidad(eMensaje, "grupo");
		Grupo grupo = null;  
		if (!grupoString.equals("-1")) {  
		    grupo = (Grupo) FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.parseInt(grupoString));  
		}
		
		String fechaString = servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha");
		LocalDateTime fecha = null ;
		
		if(fechaString != null) fecha = LocalDateTime.parse(fechaString);
		
		Mensaje mensaje = null;
		
		if (texto.isEmpty()) {
			
			mensaje = new Mensaje(emisor, receptor, emoticono, grupo);
			
		} else {
			
			mensaje = new Mensaje(emisor, receptor, texto, grupo);

		}
		
		mensaje.setCodigo(id);
		mensaje.setFechaEnvio(fecha);
		poolMensaje.addObjeto(id, mensaje);
		
		return mensaje;
	}

}
