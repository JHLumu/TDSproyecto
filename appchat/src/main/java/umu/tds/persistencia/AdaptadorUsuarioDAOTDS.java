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
	private FactoriaDAO factoria;
	
	public static AdaptadorUsuarioDAOTDS getUsuarioDAO() {
		return instancia;
	}
	
	private AdaptadorUsuarioDAOTDS(){
		this.factoria = FactoriaDAOTDS.getFactoriaDAO();
		this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		this.formateador = DateTimeFormatter.ofPattern("d-MMM-y", new Locale("es", "ES"));
		
	};
	
	
	
	@Override
	public void registrarUsuario(Usuario usuario) {
		//Comprobacion de que usuario ya no tenga asociado una entidad
		Entidad eUsuario = null;
		try {
			eUsuario=servPersistencia.recuperarEntidad(usuario.getCodigo());
		}catch(NullPointerException e) {e.printStackTrace();}
		if (eUsuario != null) return;
		
		//Registramos sus objetos: Deberia registrarse los contactos individuales y los mensajes
		for (Mensaje mensaje : usuario.getMensajesEnviados()) factoria.getMensajeDAO().registrarMensaje(mensaje);
		
		for (Mensaje mensaje : usuario.getMensajesRecibidos()) factoria.getMensajeDAO().registrarMensaje(mensaje);
		
		for (Contacto contacto : usuario.getListaContacto()) factoria.getContactoDAO().registrarContacto(contacto);
		
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
						new Propiedad("lista de mensajes recibidos", obtenerIdsMensajes(usuario.getMensajesRecibidos())),
						new Propiedad("lista de mensajes enviados", obtenerIdsMensajes(usuario.getMensajesEnviados())),
						new Propiedad("imagen", usuario.getURLImagen().toExternalForm()),
						new Propiedad("fecha de nacimiento", usuario.getFechaNacimiento().format(formateador).toString()),
						new Propiedad("saludo", usuario.getSaludo()),
						new Propiedad("premium", String.valueOf(usuario.isPremium())),
						new Propiedad("fecha de registro", usuario.getFechaRegistro().format(formateador).toString())
						)));
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());		
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
			
			else if (prop.getNombre().equals("fecha de nacimiento")) prop.setValor(usuario.getFechaNacimiento().format(formateador));
			
			else if (prop.getNombre().equals("lista de contactos")) prop.setValor(obtenerIdsContactos(usuario.getListaContacto()));
			
			else if (prop.getNombre().equals("lista de mensajes recibidos")) prop.setValor(obtenerIdsMensajes(usuario.getMensajesRecibidos()));
	
			else if (prop.getNombre().equals("lista de mensajes enviados")) prop.setValor(obtenerIdsMensajes(usuario.getMensajesEnviados()));
			
			else if (prop.getNombre().equals("imagen")) prop.setValor(usuario.getURLImagen().toExternalForm());
			
			else if (prop.getNombre().equals("saludo")) prop.setValor(usuario.getSaludo());
			
			else if (prop.getNombre().equals("premium")) prop.setValor(String.valueOf(usuario.isPremium()));
			
			else if (prop.getNombre().equals("fecha de registro")) prop.setValor(usuario.getFechaRegistro().format(formateador));
			
			servPersistencia.modificarPropiedad(prop);
		}
		
	}
	
	@Override
	public Usuario recuperarUsuario(int id) throws MalformedURLException {
		
		//Si el objeto se encuentra en el pool, se retorna
		PoolDAO poolUsuario = PoolDAO.getUnicaInstancia();
		if (poolUsuario.contiene(id)) return (Usuario) poolUsuario.getObjeto(id);

		
		//Si no lo está, se recupera Entidad y aquellas Propiedades de campos primitivos
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String idLista = servPersistencia.recuperarPropiedadEntidad(eUsuario, "lista de contactos");
		String idListaMensajesRecibidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "lista de mensajes recibidos");
		String idListaMensajesEnviados = servPersistencia.recuperarPropiedadEntidad(eUsuario, "lista de mensajes enviados");
		String imagenPath = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		String fechaString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha de nacimiento");
		String premium = servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		String registroString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha de registro");
		
		//Convertimos aquellos campos necesarios a objetos
		URL imagen = null;
		LocalDate fecha = null;
		LocalDate registro = null;
		if (imagenPath != null) imagen = new URL(imagenPath);
		if (fechaString !=null) fecha = LocalDate.parse(fechaString, formateador);
		if (registroString != null) registro = LocalDate.parse(registroString, formateador);
		
		//Se crea el objeto con esas propiedas y se introduce en el pool
				Usuario usuario = new BuilderUsuario()
									.nombre(nombre)
									.telefono(telefono)
									.apellidos(apellidos)
									.email(email)
									.password(password)
									.imagenDePerfil(imagen)
									.fechaNac(fecha)
									.saludo(saludo)
									.premium(Boolean.valueOf(premium))
									.fechaRegistro(registro)
									.build();
		usuario.setCodigo(id);
		poolUsuario.addObjeto(id, usuario);
		List<Contacto> lista = obtenerContactosAPartirDeIds(idLista);
		usuario.setListaContacto(lista);
		List<Mensaje> listaMensajesRecibidos = obtenerMensajesAPartirDeIds(idListaMensajesRecibidos);
		usuario.setMensajesRecibidos(listaMensajesRecibidos);
		List<Mensaje> listaMensajesEnviados = obtenerMensajesAPartirDeIds(idListaMensajesEnviados);
		usuario.setMensajesEnviados(listaMensajesEnviados);
		
		//Devolvemos el objeto
		return usuario;

	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException{
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("Usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();
		
		for (Entidad eUsuario : eUsuarios) usuarios.add(recuperarUsuario(eUsuario.getId()));
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
				Contacto contacto = (FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.valueOf(idContacto)));
				resultado.add(contacto);
			}
		
		}
		return resultado;
	}
	
	private String obtenerIdsMensajes(List<Mensaje> lista) {
		String aux = lista.stream()
				.map(c -> String.valueOf(c.getCodigo()))
				.collect(Collectors.joining(" "));
		return aux;
				
	}
	
	private List<Mensaje> obtenerMensajesAPartirDeIds(String idMensajes) throws NumberFormatException, MalformedURLException{
		List<Mensaje> resultado = new LinkedList<Mensaje>();
		if (idMensajes != null && !idMensajes.isEmpty()) {
			
			for (String idMensaje: idMensajes.split(" ")) {
				Mensaje mensaje = (FactoriaDAO.getFactoriaDAO().getMensajeDAO().recuperarMensaje(Integer.valueOf(idMensaje)));
				resultado.add(mensaje);
			}
		}
		return resultado;
	}
	
}
