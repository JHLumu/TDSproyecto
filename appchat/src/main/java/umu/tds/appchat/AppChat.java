package umu.tds.appchat;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.*;
import umu.tds.utils.TDSObservable;

//Clase Controlador entre modelos y ventanas
public class AppChat extends TDSObservable{
	
	//Servidor de persistencia elegido
	public static String SERVIDOR_PERSISTENCIA_ELEGIDO = "umu.tds.persistencia.FactoriaDAOTDS";
	
	
	//Instancias de adaptadores
	private ContactoDAO contactoDAO;
	private MensajeDAO mensajeDAO;
	private UsuarioDAO usuarioDAO;
	private FactoriaDAO factoria;
	
	//Instancias de Catalogos
	private CatalogoUsuarios catalogoUsuarios;
	
	
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
	
	//Usado hasta que se implemente el repositorio, se usa esto como prueba
	private Usuario sesionUsuario;
	private static HashMap<String,Usuario> usuariosRegistrados = new HashMap<String,Usuario>();
	
	//Para evitar tener dos metodos iguales podriamos usar un Optional para saludo
	
	public String getNombreUsuario() {
		return this.sesionUsuario.getNombre();
	}
	
	public String getTelefonoUsuario() {
		return this.sesionUsuario.getTelefono();
	}
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo, URL imagen) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
	
		if (this.catalogoUsuarios.estaUsuarioRegistrado(telefono)) return false;
		
		Usuario usuario = new Usuario.BuilderUsuario(nombre, telefono)
				.apellidos(apellidos)
				.email(email)
				.password(password)
				.saludo(saludo)
				.fechaNac(fechaNac)
				.imagenDePerfil(imagen)
				.build();
		
		usuarioDAO.registrarUsuario(usuario);
		catalogoUsuarios.nuevoUsuario(usuario);
		usuariosRegistrados.put(telefono,usuario); 
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
	/*
	public static boolean cambiarImagen(Usuario usuario, URL imagen) {
		//Se tiene que verificar si los datos son correctos ( se hace en la capa de presentacion¿?)
		return usuario.cambiarImagen(imagen);
	}
	
	*/
	
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
	
	
	public boolean nuevoContacto(String nombre, String telefono) {
		System.out.println("\n[DEBUG Controlador nuevoContacto]: Inicio de crear contacto.");
		if (! catalogoUsuarios.estaUsuarioRegistrado(telefono)) return false;
		ContactoIndividual contacto = this.sesionUsuario.crearContacto(nombre, catalogoUsuarios.getUsuario(telefono));
		 if ( contacto == null) return false;
		 else {
			 contactoDAO.registrarContacto(contacto);
			 usuarioDAO.modificarUsuario(sesionUsuario);
			 
			// Notificar a los observadores sobre el nuevo contacto
	            setChanged();
	            notifyObservers("nuevoContacto");
	            
			 return true;
		 }
	
	}
	
	public String[] obtenerListaContactos(){
		//REVISAR: Sorted falla
		String[] nombresContactos = this.sesionUsuario.getListaContacto().stream()
				.map(c -> c.getNombre())
				.toArray(String[]::new);
		return nombresContactos;
	}
	
	public List<Contacto> obtenerListaContactosIndividuales() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>(); // Devuelve una lista vacía si no hay sesión iniciada
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals("Individual")) // Filtrar sólo ContactoIndividual
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre())) // Ordenar alfabéticamente por nombre
	            .toList(); // Convertir a lista
	}
	
	public List<Contacto> obtenerListaContactosGrupo() {
	    if (this.sesionUsuario == null) {
	        return new LinkedList<>(); // Devuelve una lista vacía si no hay sesión iniciada
	    }

	    return this.sesionUsuario.getListaContacto().stream()
	            .filter(contacto -> contacto.getTipoContacto().equals("Grupo")) // Filtrar sólo Grupos
	            .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre())) // Ordenar alfabéticamente por nombre
	            .toList(); // Convertir a lista
	}


	
	
	
	//Esto es mas cosa del patron dao que del controlador
	public List <Mensaje> obtenerListaMensajesRecientesPorUsuario(){
		
		/*
		Usuario ana = new Usuario("Ana", "", "", LocalDate.now(),"", "");
		Usuario jose = new Usuario("Jose", "", "", LocalDate.now(),"", "");
		Usuario maria = new Usuario("Maria", "", "", LocalDate.now(),"", "");
		Mensaje[] values = new Mensaje [] {
				 new Mensaje(ana,jose, "Holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
			     new Mensaje(jose,ana, "Que tal"),
			     new Mensaje(maria,jose, "Adios")
				
		};
		return Arrays.asList(values);
		*/
		return new LinkedList<Mensaje>();
	}
	
	public void enviarMensaje(Contacto contacto) {
		
	}
	
	public static Image getImagen(Object urlObj) {
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
	        System.out.println("[DEBUG getImagen]: Descargando imagen desde URL: " + url);
	        return ImageIO.read(url);

	    } catch (IOException e) {
	        System.err.println("[ERROR getImagen]: No se pudo descargar la imagen.");
	        e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	        System.err.println("[ERROR getImagen]: " + e.getMessage());
	    }

	    return null; // Retorna null en caso de error
	}


	public boolean validarCampos(String nombre, String apellidos, String telefono, String email, String contraseña, String confcontraseña,
			LocalDate fechaNac, String imagenURL) {
		//Debido a que en los requisitos de la aplicacion, no indica nada sobre un formato para los nombres y los apellidos, estos no se van a comprobar.
				String password = new String(contraseña);
				String password1 = new String(confcontraseña);
				
				//Comprobacion de que todos los campos obligatorias se han rellenado
				if(nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()|| password.isEmpty() || password1.isEmpty() || (fechaNac == null) || (getImagen(imagenURL) == null)) return false;
				
				
				else {
					
					System.out.println("[DEBUG Registro validacionCampos]: " + "Campos de Registro:");
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Nombre: " + nombre);
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Apellidos: " + apellidos);
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Telefono: " + telefono);
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Password: " + password);
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Confirmar Password: " + password1);
					System.out.println("[DEBUG Registro validacionCampos]: "  + "Fecha: " + fechaNac);
					
					//Comprobacion sobre el formato de los datos y sobre la contraseña
					if (!password.equals(password1)) return false;
					return true;	
				}
	}

	
}
