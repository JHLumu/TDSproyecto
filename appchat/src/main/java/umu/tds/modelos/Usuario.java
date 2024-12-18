package umu.tds.modelos;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Usuario {
	
	//Atributo estatico para asignar identificadores unicos a cada usuario
		private static int ID_USUARIO = 1;
	
	//El siguiente atributo se utiliza como identificador unico de usuario
		private final int id;
	
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
		
		
	//El siguiente atributo es una coleccion que representa una lista de Contactos
	//Se utiliza como colección un conjunto para así, evitar contactos repetidos 
		private final HashSet<ContactoIndividual> listaContactos; 
		
	//El siguiente atributo es una coleccion que representa una lista de Grupos
	//Se utiliza como colección un conjunto para así, evitar grupos repetidos 	
		private final HashSet<Grupo> listaGrupos; 
		
	//El siguiente atributo se un mapa que contiene para cada contacto, su lista de mensajes asociada
		private Map<Usuario, List<Mensaje>> conversaciones;
		
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
			this.listaContactos =b.listaContactos;
			this.listaGrupos =b.listaGrupos;
			this.nombre = b.nombre;
			this.telefono = b.telefono;
			this.apellidos = b.apellidos;
			this.fechaNac = b.fechaNac;
			this.saludo = b.saludo;
			this.imagenPerfil = b.imagen;
			//this.imagenPerfil = imagenPerfil;
			this.email = b.email;
			this.password = b.password;
			this.id = ID_USUARIO;
			ID_USUARIO++;
		}

	//Metodos getter y setter
		
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
		
		/*
		public URL getImagenPerfil() {
			return imagenPerfil;
		}
		*/
		public String getEmail() {
			return email;
		}
		
		public int getID() {
			return id;
		}

	//Funcionalidades
		
		public boolean crearContacto(String nombre, Usuario usuario) {
			//Ya se tiene que haber verificado que el telefono se encuentra registrado en el sistema
			return (this.listaContactos.add(new ContactoIndividual(nombre, usuario)));
		}
		
		public boolean crearGrupo(String nombre, URL imagen) {
			return (this.listaGrupos.add(new Grupo(nombre, imagen)));
		}
		
		public boolean crearGrupo(String nombre) {
			return (this.listaGrupos.add(new Grupo(nombre)));
		}
		
		public boolean introducirMiembroaGrupo(Contacto c, Grupo g) {
			return (g.nuevoMiembro(c));
		}
		
		
		public void enviarMensaje(Mensaje mensaje) {
			if (!mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje no coincide con este usuario.");
	        }

	        Usuario receptor = mensaje.getReceptor();

	        // Agregar el mensaje a la conversación con el receptor
	        conversaciones.computeIfAbsent(receptor, k -> new ArrayList<>()).add(mensaje);

	        // Agregar el mensaje a la conversación del receptor con este usuario
	        receptor.recibirMensaje(mensaje);
	    }

	    private void recibirMensaje(Mensaje mensaje) {
	        Usuario emisor = mensaje.getEmisor();
	        conversaciones.computeIfAbsent(emisor, k -> new ArrayList<>()).add(mensaje);
	    }
	    public List<Mensaje> getChatMensaje(Usuario otroUsuario) {
	        return conversaciones.getOrDefault(otroUsuario, new ArrayList<>());
	    }
	/*
		public void cambiarImagen(URL imagen) {
			this.imagenPerfil = imagen;
		}
		
	*/

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
	    	private HashSet<ContactoIndividual> listaContactos = new HashSet<>();
	        private HashSet<Grupo> listaGrupos = new HashSet<>();
	        private Map<Usuario, List<Mensaje>> conversaciones = new HashMap<>();
	        
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



