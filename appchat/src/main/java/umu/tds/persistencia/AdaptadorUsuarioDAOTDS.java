package umu.tds.persistencia;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.modelos.Usuario.BuilderUsuario;

public class AdaptadorUsuarioDAOTDS implements UsuarioDAO {
	
	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioDAOTDS instancia = new AdaptadorUsuarioDAOTDS();
	private DateTimeFormatter formateador;
	
	
	public static AdaptadorUsuarioDAOTDS getUsuarioDAO() {
		System.out.println("[DEBUG]: " + "Se ha recuperado la instancia de AdaptadorUsuarioDAOTDS.");
		return instancia;
	}
	
	private AdaptadorUsuarioDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		this.formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	};
	
	
	
	@Override
	public void registrarUsuario(Usuario usuario) {
		System.out.println("[DEBUG]: " + "Se ha entrado a registrar usuario en el servidor de persistencia.");
		//Comprobacion de que usuario ya no tenga asociado una entidad
		Entidad eUsuario = null;
		try {
			System.out.println("[DEBUG]: " + "Se comprueba si ya se encuentra registrado.");
			eUsuario=servPersistencia.recuperarEntidad(usuario.getCodigo());
		}catch(NullPointerException e) {}
		if (eUsuario != null) return;
		
		//Registramos sus objetos: Deberia registrarse los contactos individuales y los mensajes
		System.out.println("[DEBUG]: " + "No se encuentra registrado. Se crea la entidad");
		for (Mensaje mensaje : usuario.getMensajesEnviados()) {
			FactoriaDAOTDS.getFactoriaDAO().getMensajeDAO().registrarMensaje(mensaje);
		}
		
		for (Mensaje mensaje : usuario.getMensajesRecibidos()) {
			FactoriaDAOTDS.getFactoriaDAO().getMensajeDAO().registrarMensaje(mensaje);
		}
		
		for (Contacto contacto : usuario.getListaContacto()) {
			FactoriaDAOTDS.getFactoriaDAO().getContactoDAO().registrarContacto(contacto);
		}
		
		//Creamos la entidad
		eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		
		//Se asocian las propiedades a la entidad
		
		eUsuario.setPropiedades(
				new ArrayList<Propiedad>(Arrays.asList(
						new Propiedad("nombre", usuario.getNombre()), 
						new Propiedad("apellidos", usuario.getApellidos()),
						new Propiedad("telefono", usuario.getTelefono()),
						new Propiedad("email", usuario.getEmail()),
						new Propiedad("password", usuario.getPassword())
						
						)));
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		System.out.println("[DEBUG]: " + "Se ha registrado la entidad.");
		usuario.setCodigo(eUsuario.getId());
		System.out.println("[DEBUG]: " + "El id del usuario registrado es " + usuario.getCodigo());

	
	
		
	}

	@Override
	public void modificarUsuario(Usuario usuario) {
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		for (Propiedad prop : eUsuario.getPropiedades()) {
			
			if (prop.getNombre().equals("nombre")) prop.setValor(usuario.getNombre());
			else if (prop.getNombre().equals("apellidos")) prop.setValor(usuario.getApellidos());
			else if (prop.getNombre().equals("telefono")) prop.setValor(usuario.getTelefono());
			else if (prop.getNombre().equals("email")) prop.setValor(usuario.getEmail());
			else if (prop.getNombre().equals("password")) prop.setValor(usuario.getPassword());
			else if (prop.getNombre().equals("fecha")) prop.setValor(usuario.getFechaNacimiento().format(formateador));
			servPersistencia.modificarPropiedad(prop);
		}
		
	}
	
	
	

	@Override
	public Usuario recuperarUsuario(int id) {
		
		//Si el objeto se encuentra en el pool, se retorna
		PoolDAO poolUsuario = PoolDAO.getUnicaInstancia();
		if (poolUsuario.contiene(id)) return (Usuario) poolUsuario.getObjeto(id);
		
		//Si no lo está, se recupera Entidedad y aquellas Propiedades de campos primitivos
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		
		
		//Se crea el objeto con esas propiedas y se introduce en el pool
		Usuario usuario = new BuilderUsuario(nombre,telefono)
							.apellidos(apellidos)
							.email(email)
							.password(password)
							
							.build();
		
		usuario.setCodigo(id);
		poolUsuario.addObjeto(id, usuario);
		
		//Se recuperan los objetos referenciados y se actualiza el objeto: de momento no se hace nada
		
		//Devolvemos el objeto
		return usuario;

	}

	@Override
	public List<Usuario> recuperarTodosUsuarios(){
		System.out.println("[DEBUG]: " + "Se ha entrado a recuperar todos los usuarios.");
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("Usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();
		
		for (Entidad eUsuario : eUsuarios) {
			System.out.println("[DEBUG]: " + "Se recupera un usuario.");
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		System.out.println("[DEBUG]: " + "Se devuelven los usuarios recuperados.");
		return usuarios;
		
	}

}
