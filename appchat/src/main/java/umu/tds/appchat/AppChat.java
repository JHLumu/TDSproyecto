package umu.tds.appchat;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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
import umu.tds.utils.TDSObservable;
import umu.tds.ventanas.ListaContactos;

//Clase Controlador entre modelos y ventanas
public class AppChat extends TDSObservable{
	
	//Constantes
	private static final double PRECIO_SUSCRIPCION = 9.99;
	private static final String DIRECTORIO_IMAGENES_USUARIO = "imagenesUsuarios";
	
	//Servidor de persistencia elegido
	public static String SERVIDOR_PERSISTENCIA_ELEGIDO = "umu.tds.persistencia.FactoriaDAOTDS";
	
	//Instancias de adaptadores
	private ContactoDAO contactoDAO;
	private MensajeDAO mensajeDAO;
	private UsuarioDAO usuarioDAO;
	private FactoriaDAO factoria;
	
	//Instancias de Catalogos
	private CatalogoUsuarios catalogoUsuarios;
	
	private Usuario sesionUsuario;
	
	//Uso de factoria abstracta para obtener los tipos DAO
	
	//Patron Singleton
	private static AppChat instancia = new AppChat(SERVIDOR_PERSISTENCIA_ELEGIDO);
	
	private AppChat(String factoria) {	
		this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
		this.factoria = FactoriaDAO.getInstancia(factoria);
		this.contactoDAO = this.factoria.getContactoDAO();
		this.mensajeDAO = this.factoria.getMensajeDAO();
		this.usuarioDAO = this.factoria.getUsuarioDAO();
	};
	
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
		
		usuarioDAO.registrarUsuario(usuario);
		catalogoUsuarios.nuevoUsuario(usuario);
		
		//Se obtiene la carpeta con las imágenes de los contacto
	    
		File directorioBase = new File(DIRECTORIO_IMAGENES_USUARIO);
		if (!directorioBase.exists()) directorioBase.mkdir();
		File imagenUsuario = new File(directorioBase, nombre+"-"+telefono+".png");
		try {
			ImageIO.write((RenderedImage) getImagen(imagen), "png", imagenUsuario);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

		
		return true;
	}
	
	//Necesidad de Patron Builder para construir el usuario?
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, URL imagen) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		return this.registrarUsuario(nombre, apellidos, telefono, fechaNac, email, password, "", imagen);
	}
	
	public boolean iniciarSesionUsuario(String telefono, String contraseña) {
		//Se tiene que verificar en el repositorio si los datos son correctos
		
		if(!catalogoUsuarios.sonCredencialesCorrectas(telefono, contraseña)) return false;
		this.sesionUsuario = catalogoUsuarios.getUsuario(telefono);
		return true;
		
	}
	
	public void setUsuarioPremium(boolean premium) {
		this.sesionUsuario.setPremium(premium);
		this.usuarioDAO.modificarUsuario(sesionUsuario);
	}
	
	public boolean isUsuarioPremium() {
		return this.sesionUsuario.isPremium();
	}

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
	
	
	public int nuevoGrupo(String nombre, URL urlImagen) {
	    // Primero comprobamos si ya existe un grupo con ese nombre
	    if (this.sesionUsuario.recuperarGrupo(nombre) != null) {
	        System.out.println("\n[DEBUG Controlador nuevoGrupo]: Ya existe un grupo con ese nombre.");
	        return 0; // 0 significa que ya existe
	    }

	    // Si no existe, seguimos
	    Image imagen = getImagen(urlImagen);
	    if (imagen != null) {
	        // Crear directorio si no existe
	        File directorioBase = new File(DIRECTORIO_IMAGENES_USUARIO + "\\" + getTelefonoUsuario());
	        if (!directorioBase.exists()) {
	            directorioBase.mkdir();
	        }

	        // Guardar la imagen del grupo
	        File imagenGrupo = new File(directorioBase, "Grupo-" + nombre + "-" + getTelefonoUsuario() + ".png");
	        try {
	            ImageIO.write((RenderedImage) imagen, "png", imagenGrupo);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Crear el grupo y registrarlo
	        
	        this.sesionUsuario.crearGrupo(nombre, urlImagen);
	        Grupo grupo = this.sesionUsuario.recuperarGrupo(nombre);

	        // Notificar cambios
	        setChanged(Estado.INFO_CONTACTO);
	        contactoDAO.registrarContacto(grupo);
	        usuarioDAO.modificarUsuario(sesionUsuario);

	        System.out.println("\n[DEBUG Controlador nuevoGrupo]: Se notifica a los observadores de añadir grupo.");
	        notifyObservers(Estado.INFO_CONTACTO);
	        
	        return 1; // 1 significa creado correctamente
	    } else {
	        System.out.println("\n[DEBUG Controlador nuevoGrupo]: Imagen no válida.");
	        return -1; // Imagen inválida
	    }
	}
	
	public File getGrupoFoto(Grupo contacto) {
		String nombre = contacto.getNombre();
		URL urlImagen = contacto.getURLImagen();
		
		
	    
        // Crear directorio si no existe
        File directorioBase = new File(DIRECTORIO_IMAGENES_USUARIO + "\\" + getTelefonoUsuario());
        if (!directorioBase.exists()) {
            directorioBase.mkdir();
        }
        
	        // Guardar la imagen del grupo
 
        File imagenGrupo = new File(directorioBase, "Grupo-" + nombre + "-" + contacto.getAnfitrion() + ".png");
        	
        if(!imagenGrupo.exists()) {
        	Image imagen = getImagen(urlImagen);
        	if (imagen != null) {
		        try {
		            ImageIO.write((RenderedImage) imagen, "png", imagenGrupo);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        return imagenGrupo;
        	}
        	System.out.println("\n[DEBUG Controlador fotoGrupo]: Fallo al descargar foto válida.");
	        System.err.println(urlImagen.toString());
        	return null;
	        
	    } else {
	    	return imagenGrupo;
	    } 
	}

	
	public List<Contacto> obtenerListaContactos(){
		//REVISAR: Sorted falla
		List<Contacto> contactos = this.sesionUsuario.getListaContacto();
		return contactos;
	}
	
	public List<Contacto> obtenerListaContactosIndividuales() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>(); // Devuelve una lista vacía si no hay sesión iniciada
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) // Filtrar sólo ContactoIndividual
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre())) // Ordenar alfabéticamente por nombre
	            .collect(Collectors.toList()); // Convertir a lista
	}
	
	public List<Contacto> obtenerListaContactosGrupo() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>(); // Devuelve una lista vacía si no hay sesión iniciada
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.GRUPO)) // Filtrar sólo Grupos
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre())) // Ordenar alfabéticamente por nombre
	            .collect(Collectors.toList()); // Convertir a lista
	}


	public List<Contacto> obtenerListaChatMensajes(){
		//REVISAR: Sorted falla
		List<Contacto> contactos = this.sesionUsuario.getListaContactosConMensajes();
		return contactos;
	}
	
	
	//Esto es mas cosa del patron dao que del controlador
	public List <Mensaje> obtenerChatContacto(Contacto contacto){
		
			List<Mensaje> chat = this.sesionUsuario.getChatMensaje(contacto);
			int i = 0;
			for(Mensaje m : chat) {
				System.out.println("Lista Mensajes " + i + " : " + m);
				i++;
			}
			return chat;
		
	}
	
	public boolean enviarMensaje(Contacto contacto, Object entrada) {
		if (!(entrada instanceof String || entrada instanceof Integer)) {
	        System.out.println("Tipo de mensaje no soportado");
	        return false;
	    }

	    if (contacto instanceof ContactoIndividual) {
	        Usuario receptor = ((ContactoIndividual) contacto).getUsuario();
	        Mensaje mensaje;

	        // Crear mensaje según el tipo de entrada
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

	public Image getImagen(Object urlObj) {
	    try {
	        URL url;

	        // Determinar si el objeto es String o URL
	        if (urlObj instanceof String) {
	            url = new URL((String) urlObj);
	        } else if (urlObj instanceof URL) {
	            url = (URL) urlObj;
	        } else {
	            throw new IllegalArgumentException("El parámetro debe ser una URL o una cadena válida.");
	        }

	        // Descargar la imagen desde la URL
	        System.out.println("[DEBUG getImagen]: Obteniendo imagen desde URL: " + url);
	        return ImageIO.read(url);

	    } catch (IOException e) {
	        System.err.println("[ERROR getImagen]: No se pudo descargar la imagen.");
	        e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	        System.err.println("[ERROR getImagen]: " + e.getMessage());
	    }

	    return null; // Retorna null en caso de error
	}

	
	public Image getFotoPerfilSesion() {
		return this.sesionUsuario.getFotoPerfilUsuario();
	}
	
	public void setImagenPerfil(Image imagen) {
		// Directorio base del usuario de la sesión actual
	    String directorioBase = "imagenPerfilContactos\\" + 
	    						getNombreUsuario() +  "-" + 
	    						getTelefonoUsuario();

	    File directorio = new File(directorioBase);
	    if (!directorio.exists()) {
	        directorio.mkdirs(); // Crear directorio si no existe
	    }
	    
	    File localFile = new File(directorio, this.getNombreUsuario() + "_" + this.getTelefonoUsuario() +".png");
	    try {
			ImageIO.write((java.awt.image.RenderedImage) imagen, "png", localFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	public void cambiarFotoPerfil(URL nuevaFoto) {
		this.sesionUsuario.setURLImagen(nuevaFoto);
		Image nuevaImagen = this.getImagen(nuevaFoto);
		File fileImagenContacto = new File("imagenesUsuarios", this.sesionUsuario.getNombre()+"-"+ this.sesionUsuario.getTelefono() + ".png");
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

	public Object getUltimoMensajeContacto(Contacto contacto) {
		// TODO Auto-generated method stub
		
		Mensaje m = this.sesionUsuario.getUltimoChatMensaje(contacto);
		if (m != null)
			if (!m.getTexto().isEmpty())
				return m.getTexto();
			else 
				return m.getEmoticono();
		else
			return null;
	}

	public List<Contacto> obtenerListaMiembrosGrupo(Grupo grupo) {
		
		return grupo.getMiembros();
	}

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

	

	
}
