package umu.tds.appchat;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tds.BubbleText;
import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Contacto.TipoContacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Descuento;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.*;
import umu.tds.utils.ColoresAppChat;
import umu.tds.utils.Estado;
import umu.tds.utils.ImagenUtils;
import umu.tds.utils.TDSObservable;

/**
 * Clase que actúa de Controlador entre la capa de Modelo y la capa
 * Ventana de AppChat.
 * 
 */
public class AppChat extends TDSObservable{
	
	//Constantes
	private static final double PRECIO_SUSCRIPCION = 9.99;
	
	
	
	/**
	 * Servidor de Persistencia elegido.
	 * 
	 */
	public static String SERVIDOR_PERSISTENCIA_ELEGIDO = "umu.tds.persistencia.FactoriaDAOTDS";
	
	/*
	 * Instancias de Adaptadores. 
	 */
	private ContactoDAO contactoDAO;
	private MensajeDAO mensajeDAO;
	private UsuarioDAO usuarioDAO;
	private FactoriaDAO factoria;
	
	/*
	 * Instancias de Catalogos. 
	 */
	private CatalogoUsuarios catalogoUsuarios;
	
	/*
	 * Atributo que guarda la instancia de Usuario actual. 
	 */
	private Usuario sesionUsuario;
	
	/**
	 * Instancia única de AppChat.  <p>
	 * Patron Singleton: Se asegura que todas las clases usen la misma instancia AppChat
	 */
	private static AppChat instancia = new AppChat(SERVIDOR_PERSISTENCIA_ELEGIDO);
	
	
	/**
	 * Constructor de la clase AppChat. <p>
	 * Patron Singleton: La visibilidad del constructor es privado para evitar que otras clases creen instancias AppChat.
	 * 
	 * @param factoria Tipo de Factoría Abstracta. Usado para la persistencia.
	 */
	private AppChat(String factoria) {	
		this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
		this.factoria = FactoriaDAO.getInstancia(factoria);
		this.contactoDAO = this.factoria.getContactoDAO();
		this.mensajeDAO = this.factoria.getMensajeDAO();
		this.usuarioDAO = this.factoria.getUsuarioDAO();
	};
	
	/**
	 * Método para obtener la instancia única de AppChat. <p>
	 * Patron Singleton: Se proporciona al usuario un método estático para obtener la instancia de AppChat.
	 */
	public static AppChat getInstancia() {return instancia;}

	
	public String getNombreUsuario() {
		return this.sesionUsuario.getNombre();
	}
	
	public String getTelefonoUsuario() {
		return this.sesionUsuario.getTelefono();
	}
	
	public String getApellidosUsuario() {
		return this.sesionUsuario.getApellidos();
	}
	
	public String getFechaNacimientoUsuario() {
		System.out.println("[DEBUG AppChat getFechaNacimientoUsuario]: Fecha nacimiento usuario: " + this.sesionUsuario.getFechaNacimiento());
		return this.sesionUsuario.getFechaNacimiento().toString();
	}
	
	public String getCorreoUsuario() {
		return this.sesionUsuario.getEmail();
	}
	
	public String getSaludoUsuario() {
		return this.sesionUsuario.getSaludo(); 
	}
	
	public static double getPrecioSuscripcion() {
		return PRECIO_SUSCRIPCION;
	}
	
	
	/**
	 * Calcula el máximo descuento que se puede aplicar al usuario actual.
	 *
	 * @param descuentos Lista de decuentos que se encuentran activos en el sistema.
	 * @return Máximo descuento aplicable para el usuario actual.
	 */
	public double getDescuentoAplicable(List<Descuento> descuentosActivos) {
		
		Optional<Double> res =  descuentosActivos.stream()
				.map(d -> d.calcularDescuento(sesionUsuario, PRECIO_SUSCRIPCION))
				.max(new Comparator<Double>() {
			
					@Override
					public int compare(Double o1, Double o2) {
						return Double.compare(o1, o2);
					}
			
				});
		
		return res.orElse(0.0);
		
	}
	
	/**
	 * Procesa el formulario de registros, valida sus datos y registra al usuario
	 * en el sistema de manera persistente.
	 *  
	 * @param nombre Nombre del usuario a registrar.
	 * @param apellidos Apellidos del usuario a registrar.
	 * @param telefono Teléfono del usuario a registrar.
	 * @param fecha Fecha de Nacimiento del usuario a registrar.
	 * @param email Correo electrónico del usuario a registrar.
	 * @param password Contraseña del usuario a registrar.
	 * @param saludo Saludo usuario a registrar.
	 * @return false si teléfono ya está registrado en el sistema, true en caso contrario.
	 */
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo, URL imagen) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
	
		if (this.catalogoUsuarios.estaUsuarioRegistrado(telefono)) return false;
		
		Usuario usuario = new Usuario.BuilderUsuario()
				.nombre(nombre)
				.telefono(telefono)
				.apellidos(apellidos)
				.email(email)
				.password(password)
				.saludo(saludo)
				.fechaNac(fechaNac)
				.imagenDePerfil(imagen)
				.build();
		
		boolean imagenGuardada = ImagenUtils.guardarImagen(usuario);
		System.out.println("[DEBUG AppChat registrarUsuario]: resultado de guardarImagen: " + imagenGuardada);
		if (imagenGuardada) {
		usuarioDAO.registrarUsuario(usuario);
		catalogoUsuarios.nuevoUsuario(usuario);    	
		return true;
		}
		else return false;
	}
	
	
	/**
	 * Procesa el formulario de registros, valida sus datos y registra al usuario
	 * en el sistema de manera persistente.
	 *  
	 * @param nombre Nombre del usuario a registrar.
	 * @param apellidos Apellidos del usuario a registrar.
	 * @param telefono Teléfono del usuario a registrar.
	 * @param fecha Fecha de Nacimiento del usuario a registrar.
	 * @param email Correo electrónico del usuario a registrar.
	 * @param password Contraseña del usuario a registrar.
	 * @return false si teléfono ya está registrado en el sistema, true en caso contrario.
	 */
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, URL imagen) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		return this.registrarUsuario(nombre, apellidos, telefono, fechaNac, email, password, "", imagen);
	}
	
	
	/**
	 * Procesa el formulario de inicio de sesión y autentica al usuario.
	 * Para ello, valida si el teléfono se encuentra registrado en el sistema,
	 * en ese caso valida si la contraseña pasada es la correcta.
	 * 
	 * @param telefono Teléfono del usuario a validar.
	 * @param password Contraseña del usuario a validar.
	 * @return false si el teléfono no se encuentra registrado o la contraseña es incorrecta, true en otro caso.
	 */
	public boolean iniciarSesionUsuario(String telefono, String contraseña) {
		//Se tiene que verificar en el repositorio si los datos son correctos
		
		if(!catalogoUsuarios.sonCredencialesCorrectas(telefono, contraseña)) return false;
		this.sesionUsuario = catalogoUsuarios.getUsuario(telefono);
		return true;
		
	}
	
	/**
	 * Cambia el estado Premium del usuario actual, persistiendo el cambio.
	 * 
	 * @param premium Nuevo estado premium del usuario.
	 * */
	public void setUsuarioPremium(boolean premium) {
		this.sesionUsuario.setPremium(premium);
		this.usuarioDAO.modificarUsuario(sesionUsuario);
	}
	
	public boolean isUsuarioPremium() {
		return this.sesionUsuario.isPremium();
	}

	/**
	 * Devuelve el color de los componentes de la aplicación según el estado Premium del
	 * usuario actual.
	 * 
	 * @param id: Color primario (1) o secundario (2).
	 * @return Color.
	 * */
	public Color getColorGUI(int id) {
		boolean premium = this.isUsuarioPremium();
		if (id == 1) {
			
			if (premium) return ColoresAppChat.COLOR_PREMIUM;
			else return ColoresAppChat.COLOR_NOPREMIUM;
		}
		
		else if (id == 2) {
			if (premium) return ColoresAppChat.COLOR_PREMIUM_2;
			else return ColoresAppChat.COLOR_NOPREMIUM_2;
		}
		
		else return null;
	}
	
	/**
	 * Devuelve la ruta de la imagen del icono de la aplicación,
	 * según el estado Premium del Usuario.
	 * 
	 * @return Ruta de la imagen del icono de la aplicación. 
	 * */
	public String getURLIcon() {
		if (this.isUsuarioPremium()) return "/Resources/chat_premium.png";
		else return "/Resources/chat.png";
	}
	
	
	
	
	
	public boolean crearGrupo(Usuario u, String nombre, URL imagen) {
		//Se tiene que verificar que el nombre no este vacio (se hace en la capa de presentacion¿?)
		//Se tiene que registrar la entidad
		return (u.crearGrupo(nombre, imagen));
	}
	
	public boolean crearGrupo(Usuario u, String nombre) {
		//Se tiene que verificar que el nombre no este vacio (se hace en la capa de presentacion¿?)
		//Se tiene que registrar la entidad
		return (u.crearGrupo(nombre));
	}
	
	
	public boolean introducirMiembroAGrupo(Usuario u, ContactoIndividual c, Grupo g) {
		return u.introducirMiembroaGrupo(c, g);
	}
	
	
	/**
	 *
	 * Crea un nuevo contacto, lo registra de manera persistente
	 * y notifica a la capa vista de la actualización de 
	 * la lista de contactos.
	 * 
	 * @param nombre Nombre del contacto
	 * @param telefono Teléfono del contacto. Debe estar registrado en el sistema.
	 * @return -1 si el teléfono no se encuentra registrado, 0 si ya está registrado en la lista de contactos, 1 si el registro es un éxito.
	 * 
	 */
	public int nuevoContacto(String nombre, String telefono) {
		
		if (!catalogoUsuarios.estaUsuarioRegistrado(telefono)) {
			System.out.println("\n[DEBUG Controlador nuevoContacto]: Teléfono no se encuentra registrado.");
			return -1;//-1 quiere decir que el telefono no está registrado
		}
		Usuario usuarioAsociado = catalogoUsuarios.getUsuario(telefono);
		if (!this.sesionUsuario.crearContacto(nombre,usuarioAsociado)) {
			System.out.println("\n[DEBUG Controlador nuevoContacto]: Teléfono ya está registrado en la lista de contactos del usuario.");
			return 0; //0 quiere decir que el teléfono ya está en la lista de contactos
		}
		System.out.println("\n[DEBUG Controlador nuevoContacto]: Contacto registrado. Se registra el contacto y se modifica el usuario.");
		System.out.println("\n[DEBUG Controlador nuevoContacto]: Contacto registrado. Usuario asociado: " + usuarioAsociado);
		ContactoIndividual contacto = this.sesionUsuario.recuperarContactoIndividual(telefono);
		System.out.println("\n[DEBUG Controlador nuevoContacto]: Contacto:" + contacto);
		
		setChanged(Estado.INFO_CONTACTO);
		contactoDAO.registrarContacto(contacto);
		usuarioDAO.modificarUsuario(sesionUsuario);
		// Notificar a los observadores sobre el nuevo contacto
		System.out.println("\n[DEBUG Controlador nuevoContacto]: Se notifica a los observadores de añadir contacto.");
		notifyObservers(Estado.INFO_CONTACTO);    
	    return 1;

	}
	
	/**
	 * 
	 * Crea un nuevo grupo vacío, lo registra de manera persistente
	 * y notifica a la capa vista de la actualización de la lista
	 * de contactos.
	 * 
	 * @param nombre Nombre del grupo.
	 * @param urlImagen URL de la imagen del grupo. Opcional.
	 * @return -1 si la imagen no es válida, 0 si existe un grupo con ese mismo nombre, 1 en caso de que el registro haya sido un éxito.
	 * */
	public int nuevoGrupo(String nombre, URL urlImagen) {
		
		Image imagen = null;
		
	    // Primero comprobamos si ya existe un grupo con ese nombre
	    if (this.sesionUsuario.recuperarGrupo(nombre) != null) {
	        System.out.println("\n[DEBUG Controlador nuevoGrupo]: Ya existe un grupo con ese nombre.");
	        return 0;
	    }

	    this.sesionUsuario.crearGrupo(nombre, urlImagen);
	    Grupo grupo = this.sesionUsuario.recuperarGrupo(nombre);
	 
	    if (urlImagen != null && !ImagenUtils.guardarImagen(grupo)) return -1;
	       
	    // Notificar cambios.
	    setChanged(Estado.INFO_CONTACTO);
	    contactoDAO.registrarContacto(grupo);
	    usuarioDAO.modificarUsuario(sesionUsuario);

	    System.out.println("\n[DEBUG Controlador nuevoGrupo]: Se notifica a los observadores de añadir grupo.");
	    notifyObservers(Estado.INFO_CONTACTO);
	        
	    return 1;
	    

	}
	
	
	public List<Contacto> obtenerListaContactos(){
		List<Contacto> contactos = this.sesionUsuario.getListaContacto();
		return contactos;
	}
	
	public List<Contacto> obtenerListaContactosIndividuales() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>();
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL))
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
	            .collect(Collectors.toList());
	}
	
	public List<Contacto> obtenerListaContactosGrupo() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>(); // Devuelve una lista vacía si no hay sesión iniciada
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.GRUPO))
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
	            .collect(Collectors.toList()); 
	}


	public List<Contacto> obtenerListaChatMensajes(){
		List<Contacto> contactos = this.sesionUsuario.getListaContactosConMensajes();
		return contactos;
	}
	
	
	/**
	 * Recupera los mensajes enviados entre el usuario actual y un contacto.
	 * 
	 * @param contacto Contacto del usuario.
	 * @return Lista de mensajes, tanto envíados como recibidos, entre estos dos usuarios.
	 * */
	public List <Mensaje> obtenerChatContacto(Contacto contacto){
		
			List<Mensaje> chat = this.sesionUsuario.getChatMensaje(contacto);
			int i = 0;
			for(Mensaje m : chat) {
				System.out.println("Lista Mensajes " + i + " : " + m);
				i++;
			}
			return chat;
		
	}
	
	
	//TODO: Revisar y explicar lógica.
	/**
	 * Registra el envío y la recepción del mensaje en el usuario
	 * emisor y receptor.
	 * 
	 * @param contacto Puede ser tanto un contacto individual como un grupo.
	 * @param entrada Puede ser tanto texto plano como un emoji.
	 * @return true si el mensaje se ha enviado correctamente, false en caso contrario.
	 * */
	public boolean enviarMensaje(Contacto contacto, Object entrada) {
		if (!(entrada instanceof String || entrada instanceof Integer)) {
	        System.out.println("Tipo de mensaje no soportado");
	        return false;
	    }

	    if (contacto instanceof ContactoIndividual) {
	        Usuario receptor = ((ContactoIndividual) contacto).getUsuario();
	        Mensaje mensaje;

	        // Crear mensaje según el tipo de entrada: Si es tipo String, es un mensaje con texto, mientras que si es tipo Integer, es un emoji.
	        if (entrada instanceof String) {
	            mensaje = new Mensaje(sesionUsuario, receptor, (String) entrada, null);
	        } else {
	            mensaje = new Mensaje(sesionUsuario, receptor, (Integer) entrada, null);
	        }

	        mensajeDAO.registrarMensaje(mensaje);
	        sesionUsuario.enviarMensaje(mensaje);
	        usuarioDAO.modificarUsuario(sesionUsuario);

	        receptor.recibirMensaje(mensaje);
	        usuarioDAO.modificarUsuario(receptor);

	        return true;

	    } else if (contacto instanceof Grupo) {
	        Grupo grupo = (Grupo) contacto;
	        Mensaje mensaje;
	        
	        if(soyAnfitrion(contacto)) {
	        	List<Contacto> miembros = grupo.getMiembros();
	        	if (entrada instanceof String) {
		            mensaje = new Mensaje(sesionUsuario, sesionUsuario, (String) entrada, grupo);
		        } else {
		            mensaje = new Mensaje(sesionUsuario, sesionUsuario, (Integer) entrada, grupo);
		        }

		        mensajeDAO.registrarMensaje(mensaje);
		        sesionUsuario.enviarMensaje(mensaje);
		        usuarioDAO.modificarUsuario(sesionUsuario);

		        for (Contacto miembro : miembros) {
		            if (miembro instanceof ContactoIndividual) {
		                Usuario receptor = ((ContactoIndividual) miembro).getUsuario();
		                receptor.recibirMensaje(mensaje);
		                usuarioDAO.modificarUsuario(receptor);
		            }
		        }

		        return true;
	        } else {
	        	Usuario anfitrion = catalogoUsuarios.getUsuario(grupo.getAnfitrion());
	        	if (entrada instanceof String) {
		            mensaje = new Mensaje(sesionUsuario, anfitrion,(String) entrada, grupo);
		        } else {
		            mensaje = new Mensaje(sesionUsuario, anfitrion, (Integer) entrada, grupo);
		        }

		        mensajeDAO.registrarMensaje(mensaje);
		        sesionUsuario.enviarMensaje(mensaje);
		        usuarioDAO.modificarUsuario(sesionUsuario);
		        anfitrion.recibirMensaje(mensaje);
		        usuarioDAO.modificarUsuario(anfitrion);

		        return true;
	        }
	        
	    }

	    System.out.println("Error en el envío");
	    return false;
	}
	
	private boolean soyAnfitrion(Contacto contacto) {
		return ((Grupo) contacto).getAnfitrion().equals(getTelefonoUsuario());
	}
	
	
	
	
	/**
	 * 
	 * Devuelve la imagen del usuario actual.
	 * 
	 * @return Imagen del usuario.
	 * 
	 * */
	public Image getImagenUsuarioActual() {
		return ImagenUtils.getImagen(this.sesionUsuario);
	}
	
	/**
	 * 
	 * Actualiza la imagen de perfil de un usuario y registra
	 * el cambio de manera persistente.
	 * 
	 * @param url URL de la imagen.
	 * @param imagen Imagen descargada.
	 * 
	 * */
	public void cambiarFotoPerfil(URL fotoURL, Image foto) {
		this.sesionUsuario.setURLImagen(fotoURL);
		Image nuevaImagen = foto;
		File fileImagenContacto = ImagenUtils.getFile(this.sesionUsuario);
		try {
			ImageIO.write((RenderedImage) nuevaImagen, "png", fileImagenContacto);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		setChanged(Estado.NUEVA_FOTO_USUARIO);
		usuarioDAO.modificarUsuario(sesionUsuario);
		
		notifyObservers(Estado.NUEVA_FOTO_USUARIO);
	}

	
	
	public void cambiarSaludo(String saludo) {
		this.sesionUsuario.setSaludo(saludo);
		usuarioDAO.modificarUsuario(sesionUsuario);
		
	}


	/**
	 * 
	 * Devuelve el último mensaje envíado o recibido entre el usuario actual 
	 * y un contacto.
	 * 
	 * @param contacto Puede ser tanto un contacto individual como un grupo.
	 * @return Mensaje (puede ser texto plano o un emoji).
	 * 
	 * */
	public Object getUltimoMensajeContacto(Contacto contacto) {

		Mensaje m = this.sesionUsuario.getUltimoChatMensaje(contacto);
		if (m != null)
			return m.getContenido();
		else
			return null;
	}

	public List<Contacto> obtenerListaMiembrosGrupo(Grupo grupo) {
		
		return grupo.getMiembros();
	}

	
	//#TODO: Revisar si es necesario que seleccionado sea tipo Contacto y no ContactoIndividual.
	/*
	 *
	 * Añade un nuevo contacto a un grupo, registra el cambio en el sistema
	 * de manera persistente y notifica a la capa vista de la actualización
	 * de la lista de contactos.
	 * 
	 * @param grupo: Grupo seleccionado en el que se añadirá un contacto.
	 * @param seleccionado: Contacto a añadir.
	 * 
	 */
	public void nuevoMiembroGrupo(Grupo grupo, Contacto seleccionado) {
		Grupo recuperado = this.sesionUsuario.recuperarGrupo(grupo.getNombre());
		System.out.println("\n[DEBUG Controlador nuevoMiembro]: Miembro -> " + seleccionado);
		ContactoIndividual miembro = this.sesionUsuario.recuperarContactoIndividual(((ContactoIndividual) seleccionado).getTelefono());
		recuperado.nuevoMiembro(miembro);
		setChanged(Estado.INFO_CONTACTO);
		contactoDAO.modificarContacto(recuperado);
		usuarioDAO.modificarUsuario(sesionUsuario);
		// Notificar a los observadores sobre el nuevo contacto
		System.out.println("\n[DEBUG Controlador nuevoMiembro]: Se notifica a los observadores de añadir miembro.");
		notifyObservers(Estado.INFO_CONTACTO);   
	}

	
	/**
	 *
	 * Elimina un miembro existente de un grupo, registra el cambio en el sistema
	 * de manera persistente y notifica a la capa vista de la actualización
	 * de la lista de contactos.
	 * 
	 * @param grupo Grupo seleccionado en el que se eliminará un contacto.
	 * @param seleccionado Contacto a eliminar.
	 * 
	 */
	public void eliminarMiembroGrupo(Grupo grupo, Contacto seleccionado) {
		Grupo recuperado = this.sesionUsuario.recuperarGrupo(grupo.getNombre());
		System.out.println("\n[DEBUG Controlador eliminarMiembro]: Miembro -> " + seleccionado);
		ContactoIndividual miembro = this.sesionUsuario.recuperarContactoIndividual(((ContactoIndividual) seleccionado).getTelefono());
		recuperado.eliminarMiembro(miembro);
		setChanged(Estado.INFO_CONTACTO);
		contactoDAO.modificarContacto(recuperado);
		usuarioDAO.modificarUsuario(sesionUsuario);
		// Notificar a los observadores sobre el nuevo contacto
		System.out.println("\n[DEBUG Controlador eliminarMiembro]: Se notifica a los observadores de eliminar miembro.");
		notifyObservers(Estado.INFO_CONTACTO);   
	}

	public boolean esContacto(Contacto contacto) {
		
		return obtenerListaContactos().contains(contacto);
	}
	
	
	//TODO: Revisar y Explicar lógica.
	public List<BubbleText> pintarMensajesBurbuja(Contacto contacto, JPanel chat) {
	    List<Mensaje> mensajes = obtenerChatContacto(contacto);
	    List<BubbleText> burbujas = new ArrayList<>();

	    if (mensajes == null || mensajes.isEmpty()) {
	        return burbujas;
	    }

	    String telefonoUsuario = getTelefonoUsuario();

	    for (Mensaje mensaje : mensajes) {
	        Object contenido = (mensaje.getEmoticono() == -1) ? mensaje.getTexto() : mensaje.getEmoticono();

	        boolean esUsuario = mensaje.getEmisorTelf().equals(telefonoUsuario);
	        String autor;
	        int tipoMensaje;

	        if (esUsuario) {
	            autor = getNombreUsuario();
	            if (mensaje.getIDGrupo() != -1 && !mensaje.getReceptorTelf().equals(telefonoUsuario)) {
	                autor += " <G> * " + mensaje.getNombreGrupo() + " *";
	            }
	            tipoMensaje = BubbleText.SENT;
	        } else {
	            if (contacto instanceof ContactoIndividual) {
	                autor = contacto.getNombre();
	                if (mensaje.getIDGrupo() != -1) {
	                    autor += " <G> * " + mensaje.getNombreGrupo() + " *";
	                }
	            } else {
	                String telefonoAnfitrion = ((Grupo) contacto).getAnfitrion();
	                ContactoIndividual anfitrion = (ContactoIndividual) this.sesionUsuario.getAnfitrionConMensajes(telefonoAnfitrion);
	                autor = (anfitrion != null) ? anfitrion.getNombre() : telefonoAnfitrion;
	            }
	            tipoMensaje = BubbleText.RECEIVED;
	        }

	        BubbleText bubble = (contenido instanceof String)
	            ? new BubbleText(chat, (String) contenido, Color.WHITE, autor, tipoMensaje, 14)
	            : new BubbleText(chat, (Integer) contenido, Color.WHITE, autor, tipoMensaje, 14);

	        burbujas.add(bubble);
	    }

	    return burbujas;
	}

	public String getTelefonoContacto(Contacto seleccionado) {
		
		return seleccionado instanceof ContactoIndividual ? 
				((ContactoIndividual) seleccionado).getTelefono() :
				((Grupo) seleccionado).getAnfitrion()											
			;
	}

	public LocalDateTime getUltimoMensajeFecha(Contacto contacto) {
		Mensaje m = this.sesionUsuario.getUltimoChatMensaje(contacto);
		return m.getFechaEnvio();
	}
}
