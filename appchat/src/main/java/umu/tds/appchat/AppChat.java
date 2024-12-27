package umu.tds.appchat;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.*;

//Clase Controlador entre modelos y ventanas
public class AppChat {
	
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
	
	public boolean crearContacto(Usuario usuario, String nombre, String telefono) {
		//Tendriamos que verificar en el Repositorio si el telefono se encuentra registrado en el sistema
		return (usuario.crearContacto(nombre, usuario));
	}
	
	//Para evitar tener dos metodos iguales podriamos usar un Optional para saludo
	
	public String getNombreUsuario() {
		return this.sesionUsuario.getNombre();
	}
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
	
		if (this.catalogoUsuarios.estaUsuarioRegistrado(telefono)) return false;
		
		Usuario usuario = new Usuario.BuilderUsuario(nombre, telefono)
				.apellidos(apellidos)
				.email(email)
				.password(password)
				.saludo(saludo)
				.build();
		
		usuarioDAO.registrarUsuario(usuario);
		catalogoUsuarios.nuevoUsuario(usuario);
		usuariosRegistrados.put(telefono,usuario); 
		return true;
	}
	
	//Necesidad de Patron Builder para construir el usuario?
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		return this.registrarUsuario(nombre, apellidos, telefono, fechaNac, email, password, "");
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
	
	
	public boolean introducirMiembroAGrupo(Usuario u, Contacto c, Grupo g) {
		return u.introducirMiembroaGrupo(c, g);
	}
	
	
	public boolean nuevoContacto(String nombre, String telefono) {
		if (! catalogoUsuarios.estaUsuarioRegistrado(telefono)) return false;
		return this.sesionUsuario.crearContacto(nombre, catalogoUsuarios.getUsuario(telefono));
	
	}
	
	public String[] obtenerListaContactos(){
		String[] nombresContactos = this.sesionUsuario.getListaContacto().stream()
				.map(c -> c.getNombre())
				.sorted()
				.toArray(String[]::new);
		return nombresContactos;
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
}
