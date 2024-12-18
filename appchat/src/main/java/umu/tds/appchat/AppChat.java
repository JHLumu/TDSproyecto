package umu.tds.appchat;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
	private ContactoIndividualDAO contactoIndividualDAO;
	private MensajeDAO mensajeDAO;
	private UsuarioDAO usuarioDAO;
	private GrupoDAO grupoDAO;
	private FactoriaDAO factoria;
	
	//Uso de factoria abstracta para obtener los tipos DAO
	
	//Patron Singleton
	private static AppChat instancia = new AppChat(SERVIDOR_PERSISTENCIA_ELEGIDO);
	
	private AppChat(String factoria) {	
		this.factoria = FactoriaDAO.getInstancia(factoria);
		this.contactoIndividualDAO = this.factoria.getContactoIndividualDAO();
		this.mensajeDAO = this.factoria.getMensajeDAO();
		this.usuarioDAO = this.factoria.getUsuarioDAO();
		this.grupoDAO = this.factoria.getGrupoDAO();
	};
	
	public static AppChat getInstancia() {return instancia;}
	
	//Usado hasta que se implemente el repositorio, se usa esto como prueba
	public static Usuario sesionUsuario;
	public static HashMap<String,Usuario> usuariosRegistrados = new HashMap<String,Usuario>();
	
	public boolean crearContacto(Usuario usuario, String nombre, String telefono) {
		//Tendriamos que verificar en el Repositorio si el telefono se encuentra registrado en el sistema
		return (usuario.crearContacto(nombre, usuario));
	}
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		if(usuariosRegistrados.containsKey(telefono)) return false;
		
		Usuario usuario = new Usuario.BuilderUsuario(nombre, telefono)
				.apellidos(apellidos)
				.email(email)
				.password(password)
				.saludo(saludo)
				.build();
		
		usuariosRegistrados.put(telefono,usuario); 
		
		//Se incluye el metodo de UsuarioDAO que registre al usuario en la base de datos
		
		return true;
	}
	
	//Necesidad de Patron Builder para construir el usuario?
	
	public boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		
		Usuario usuario = new Usuario.BuilderUsuario(nombre, telefono)
									.apellidos(apellidos)
									.email(email)
									.password(password)
									.build();
		
		usuariosRegistrados.put(telefono,usuario); 
		return true;
	}
	
	public boolean iniciarSesionUsuario(String telefono, String contraseña) {
		//Se tiene que verificar en el repositorio si los datos son correctos
		if (!usuariosRegistrados.keySet().contains(telefono)) return false;
		else if (!usuariosRegistrados.get(telefono).getPassword().equals(contraseña)) return false;
		sesionUsuario = usuariosRegistrados.get(telefono);
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
