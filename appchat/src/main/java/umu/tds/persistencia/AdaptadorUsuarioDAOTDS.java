package umu.tds.persistencia;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.modelos.Usuario.BuilderUsuario;

public class AdaptadorUsuarioDAOTDS implements UsuarioDAO {
	//REVISAR: La persistencia de fecha
	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioDAOTDS instancia = new AdaptadorUsuarioDAOTDS();
	private DateTimeFormatter formateador;
	
	
	public static AdaptadorUsuarioDAOTDS getUsuarioDAO() {
		System.out.println("\n[DEBUG AdaptadorUsuarioDAOTDS getUsuarioDAO]: " + "Se ha recuperado la instancia de AdaptadorUsuarioDAOTDS.");
		return instancia;
	}
	
	private AdaptadorUsuarioDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		this.formateador = DateTimeFormatter.ofPattern("d-MMM-y", new Locale("es", "ES"));
		
	};
	
	
	
	@Override
	public void registrarUsuario(Usuario usuario) {
		System.out.println("\n[DEBUG AdaptadorUsuarioDAOTDS registrarUsuario]: " + "Se ha entrado a registrar usuario en el servidor de persistencia.");
		//Comprobacion de que usuario ya no tenga asociado una entidad
		Entidad eUsuario = null;
		try {
			System.out.println("[DEBUG AdaptadorUsuarioDAOTDS registrarUsuario]: " + "Se comprueba si ya se encuentra registrado.");
			eUsuario=servPersistencia.recuperarEntidad(usuario.getCodigo());
		}catch(NullPointerException e) {}
		if (eUsuario != null) return;
		
		//Registramos sus objetos: Deberia registrarse los contactos individuales y los mensajes
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS registrarUsuario]: " + "No se encuentra registrado. Se crea la entidad");
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
						new Propiedad("password", usuario.getPassword()),
						new Propiedad("lista de contactos", obtenerIdsContactos(usuario.getListaContacto())),
						new Propiedad("imagen", usuario.getImagenPerfil().toExternalForm()),
						new Propiedad("fecha de nacimiento", usuario.getFechaNacimiento().format(formateador).toString())
						)));
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS]: " + "Se ha registrado la entidad.");
		usuario.setCodigo(eUsuario.getId());
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS]: " + "El id del usuario registrado es " + usuario.getCodigo());

	
	
		
	}

	@Override
	public void modificarUsuario(Usuario usuario) {
		System.out.println("\n[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Inicio de modificar usuario.");
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		//REVISAR: Eliminar aquellos campos en los que no se haya implementado la funcionalidad de cambiar al usuario.
		for (Propiedad prop : eUsuario.getPropiedades()) {
			
			if (prop.getNombre().equals("nombre")) {
				
				prop.setValor(usuario.getNombre());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado el nombre del usuario.");
			}
			else if (prop.getNombre().equals("apellidos")) {
				prop.setValor(usuario.getApellidos());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se han modificado los apellidos del usuario.");
			}
			else if (prop.getNombre().equals("telefono")) {
				prop.setValor(usuario.getTelefono());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado el telefono del usuario.");
			}
			
			else if (prop.getNombre().equals("email")) {
				prop.setValor(usuario.getEmail());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado el correo del usuario.");
				}
			else if (prop.getNombre().equals("password")) {
				prop.setValor(usuario.getPassword());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado la contraseña del usuario.");
			}
			else if (prop.getNombre().equals("fecha de nacimiento")) {
				prop.setValor(usuario.getFechaNacimiento().format(formateador));
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado la fecha de nacimiento del usuario.");
				}
			else if (prop.getNombre().equals("lista de contactos")) {
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Lista ID Contactos:" + this.obtenerIdsContactos(usuario.getListaContacto()));
				prop.setValor(obtenerIdsContactos(usuario.getListaContacto()));
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado la lista de contactos del usuario.");
				}
			else if (prop.getNombre().equals("imagen")) {
				prop.setValor(usuario.getImagenPerfil().toExternalForm());
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS modificarUsuario]: " + "Se ha modificado la foto de perfil del usuario.");
				}
			servPersistencia.modificarPropiedad(prop);
		}
		
	}
	
	@Override
	public Usuario recuperarUsuario(int id) throws MalformedURLException {
		System.out.println("\n[DEBUG AdaptadorUsuarioDAOTDS recuperarUsuario]: " + "Inicio de recuperar usuario.");
		//Si el objeto se encuentra en el pool, se retorna
		PoolDAO poolUsuario = PoolDAO.getUnicaInstancia();
		if (poolUsuario.contiene(id)) {
			System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarUsuario]: " + "Se devuelve el usuario al estar en el Pool.");
			return (Usuario) poolUsuario.getObjeto(id);
		}
		
		//Si no lo está, se recupera Entidad y aquellas Propiedades de campos primitivos
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String idLista = servPersistencia.recuperarPropiedadEntidad(eUsuario, "lista de contactos");
		String imagenPath = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		String fechaString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha de nacimiento");
		URL imagen = null;
		if (imagenPath != null) imagen = new URL(imagenPath);
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarUsuario]: " + "Lista ID Contactos:" + idLista);
		
		LocalDate fecha = null;
		if (fechaString !=null) fecha = LocalDate.parse(fechaString, formateador);
		
		//Se crea el objeto con esas propiedas y se introduce en el pool
				Usuario usuario = new BuilderUsuario()
									.nombre(nombre)
									.telefono(telefono)
									.apellidos(apellidos)
									.email(email)
									.password(password)
									.imagenDePerfil(imagen)
									.fechaNac(fecha)
									.build();
		usuario.setCodigo(id);
		poolUsuario.addObjeto(id, usuario);
		
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarUsuario]: " + "Se recupera la entidad y sus propiedades primitivas.");
		List<Contacto> lista = obtenerContactosAPartirDeIds(idLista);
		usuario.setListaContacto(lista);
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarUsuario]: " + "Se crea y se añade al pool.");
		
		//Se recuperan los objetos referenciados y se actualiza el objeto: 
		
		
		
		
		//Devolvemos el objeto
		return usuario;

	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException{
		System.out.println("\n[DEBUG AdaptadorUsuarioDAOTDS recuperarTodosUsuarios]: " + "Se ha entrado a recuperar todos los usuarios.");
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("Usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();
		
		for (Entidad eUsuario : eUsuarios) {
			System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarTodosUsuarios]: " + "Se recupera un usuario.");
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		System.out.println("[DEBUG AdaptadorUsuarioDAOTDS recuperarTodosUsuarios]: " + "Se devuelven los usuarios recuperados.");
		return usuarios;
		
	}

	private String obtenerIdsContactos(List<Contacto> lista) {
		String aux = lista.stream()
				.map(c -> String.valueOf(c.getCodigo()))
				.collect(Collectors.joining(" "));
		return aux;
				
	}
	
	private List<Contacto> obtenerContactosAPartirDeIds(String idContactos) throws NumberFormatException, MalformedURLException{
		List<Contacto> resultado = new LinkedList<Contacto>();
		if (idContactos != null && !idContactos.isEmpty()) {
			
			for (String idContacto: idContactos.split(" ")) {
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS obtenerContactosAPartirDeIds]: " + "idContacto: " + idContacto);
				Contacto contacto = (FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.valueOf(idContacto)));
				resultado.add(contacto);
				System.out.println("[DEBUG AdaptadorUsuarioDAOTDS obtenerContactosAPartirDeIds]: " + "Se recupera el contacto " + contacto.getNombre());
			}
		
		}
		return resultado;
	}
	
}
