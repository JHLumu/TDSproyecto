package umu.tds.modelos;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Usuario {
	
	//El siguiente atributo se utiliza como identificador unico de usuario
		private int codigo;
	
	//Atributos de la Clase
	//Como no se menciona nada en los requisitos sobre la posibilidad de cambiar algun dato del usuario
	//las siguientes son finales
	
		private final String nombre;
		private final String apellidos;
		private final String telefono;
		private final LocalDate fechaNac;
		private URL imagenPerfil;
		private final String email;	
		private final String password;
		
	//El siguiente atributo es opcional
		private String saludo;
		
	//El siguiente atributo es un booleano que representa si un usuario ha activado el Premium o no
		private boolean esPremium = false;
		
		
	//El siguiente atributo es una coleccion que representa una lista de Contactos y Grupos
	//Se utiliza como colección un conjunto para así, evitar contactos y grupos repetidos 
		private final HashSet<Contacto> listaContactos; 
		
	//Los siguientes atributos guardan los mensajes enviados y recibidos del usuario
		
		private final List<Mensaje> mensajesRecibidos;
		private final List<Mensaje> mensajesEnviados;
		
	//Constructores de la clase
		
		/**
		 * Crea una nueva instancia de "Usuario" con el nombre, apellidos, numero de telefono, fecha de nacimiento, imagen de perfil, correo y contraseña
		 *
		 * @param nombre el nombre del usuario
		 * @param apellidos los apellidos del usuario
		 * @param telefono el numero de telefono del usuario
		 * @param fecha la fecha de nacimiento del usuario
		 * @param imagen la imagen de perfil del usuario
		 * @param email el correo electronico del usuario
		 * @param contraseña la contraseña del usuario
		 *
		 */
		public Usuario(BuilderUsuario b) {
			this.listaContactos =new HashSet<Contacto>();
			this.mensajesRecibidos = new LinkedList<Mensaje>();
			this.mensajesEnviados = new LinkedList<Mensaje>();
			this.nombre = b.nombre;
			this.telefono = b.telefono;
			this.apellidos = b.apellidos;
			this.fechaNac = b.fechaNac;
			this.saludo = b.saludo;
			this.imagenPerfil = b.imagen;
			this.email = b.email;
			this.password = b.password;
			this.codigo = 0;	
		}

	//Metodos getter y setter
		
		public List<Contacto> getListaContacto(){
			return new LinkedList<Contacto>(this.listaContactos);
		}
		
		public String getSaludo() {
			return saludo;
		}

		private void CambiarSaludo(String saludo) {
			this.saludo = saludo;
		}

		public boolean EsPremium() {
			return esPremium;
		}

		private void setEsPremium(boolean esPremium) {
			this.esPremium = esPremium;
		}

		public String getNombre() {
			return nombre;
		}

		public String getApellidos() {
			return apellidos;
		}

		public String getTelefono() {
			return telefono;
		}

		public LocalDate getFechaNacimiento() {
			return fechaNac;
		}

		public String getPassword() {
			return password;
		}
		
		
		public URL getImagenPerfil() {
			return imagenPerfil;
		}
		
		public String getEmail() {
			return email;
		}
		
		public int getCodigo() {
			return codigo;
		}
		
		public void setCodigo(int id) {
			this.codigo = id;
		}

	//Funcionalidades
		
		public boolean crearContacto(String nombre, Usuario usuario) {
			return (this.listaContactos.add(new ContactoIndividual(nombre, usuario)));
		}
		
		public boolean crearGrupo(String nombre, URL imagen) {
			return (this.listaContactos.add(new Grupo(nombre, imagen)));
		}
		
		public boolean crearGrupo(String nombre) {
			return this.crearGrupo(nombre, null);
		}
		
		public boolean introducirMiembroaGrupo(Contacto c, Grupo g) {
			if (! this.listaContactos.contains(g)) return false;
			return (g.nuevoMiembro(c));
		}
		
		public void enviarMensaje(Mensaje mensaje) {
			if (!mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje no coincide con este usuario.");
	        }

	       //Agregar el mensaje a la lista de mensajes enviados
			this.mensajesEnviados.add(mensaje);
			
	        // REVISAR: Llamar al receptor para que reciba el mensaje
	        mensaje.getReceptor().recibirMensaje(mensaje);
	    }

	    private void recibirMensaje(Mensaje mensaje) {
	       //Se recibe el mensaje por parte del emisor y se guarda en la lista de mensajes recibidos
	    }
	    public List<Mensaje> getChatMensaje(Usuario otroUsuario) {
	        //Uso de streams. Recupera aquellos mensajes
	    	List<Mensaje> aux = Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	    			.flatMap(mensajes -> mensajes.stream()
	    					.filter(msg -> msg.getEmisor().equals(otroUsuario) || msg.getReceptor().equals(otroUsuario)))
	    			.sorted()
	    			.collect(Collectors.toList());
	    	return aux;
	    	
	    	
	    }
	
		public void cambiarImagen(URL imagen) {
			this.imagenPerfil = imagen;
		}
		
		public List<Mensaje> getMensajesRecibidos() {
			return new LinkedList<Mensaje>(this.mensajesRecibidos);
			
		}
		
		public List<Mensaje> getMensajesEnviados() {
			return new LinkedList<Mensaje>(this.mensajesEnviados);
			
		}


	    public static class BuilderUsuario {
	    	//Atributos, iguales que la clase Usuario
	    	private String nombre;
	    	private String apellidos;
	    	private String telefono;
	    	private LocalDate fechaNac;
	    	private String email;
	    	private URL imagen;
	    	private String password;
	    	private String saludo="";
	    	private boolean esPremium = false;
	        
	        public BuilderUsuario(String nombre, String telefono) {
	        	this.nombre = nombre;
	        	this.telefono = telefono;
	        }
	        
	        public BuilderUsuario apellidos(String apellidos) {this.apellidos=apellidos;return this;}
	        public BuilderUsuario fechaNac(LocalDate fechaNac) {this.fechaNac=fechaNac;return this;}
	        public BuilderUsuario email(String email) {this.email=email;return this;}
	        public BuilderUsuario password(String password) {this.password=password;return this;}
	        public BuilderUsuario saludo(String saludo) {this.saludo=saludo;return this;}
	        public BuilderUsuario imagenDePerfil(URL imagen) {this.imagen = imagen;return this;}
	        public Usuario build() {return new Usuario(this);}
	    }
		
}



