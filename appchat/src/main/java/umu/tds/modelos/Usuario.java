package umu.tds.modelos;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import umu.tds.modelos.Contacto.TipoContacto;

public class Usuario {
	
	//El siguiente atributo se utiliza como identificador unico de usuario para la persistencia
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
		private boolean premium = false;
		
	//El siguiente atributo es una coleccion que representa una lista de Contactos y Grupos
	//Se utiliza como colección un conjunto para así, evitar contactos y grupos repetidos 
		private List<Contacto> listaContactos; 
		
	//Los siguientes atributos guardan los mensajes enviados y recibidos del usuario
		
		private List<Mensaje> mensajesRecibidos;
		private List<Mensaje> mensajesEnviados;
		
	//El siguiente atributo se ha definido debido a la implementación de DescuentoPorFecha
		private final LocalDate fechaRegistro;
		
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
			this.fechaRegistro = LocalDate.now();
			this.listaContactos =b.listaContactos;
			this.mensajesRecibidos = b.listaDeMensajesRecibidos;
			this.mensajesEnviados = b.listaDeMensajesEnviados;
			this.nombre = b.nombre;
			this.telefono = b.telefono;
			this.apellidos = b.apellidos;
			this.fechaNac = b.fechaNac;
			this.saludo = b.saludo;
			this.imagenPerfil = b.imagen;
			this.email = b.email;
			this.password = b.password;
			this.codigo = 0;
			this.premium = b.premium;
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

		public boolean isPremium() {
			return premium;
		}

		public void setPremium(boolean esPremium) {
			this.premium = esPremium;
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

		public LocalDate getFechaRegistro() {
			return this.fechaRegistro;
		}
	//Funcionalidades
		
		
		public boolean crearContacto(String nombre, Usuario usuario) {
			ContactoIndividual contacto = new ContactoIndividual(nombre, usuario);
			if (this.listaContactos.contains(contacto)) return false;
			return this.listaContactos.add(contacto);
			
		}
		
		public ContactoIndividual recuperarContacto(String telefono) {
			ContactoIndividual contacto = null;
			for (Contacto c : this.listaContactos) {
				System.out.println(c.getTipoContacto());
				if (c.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) System.out.println("me quiero matar");
				if (c.getTipoContacto().equals(TipoContacto.INDIVIDUAL) && ((ContactoIndividual) c).getTelefono().equals(telefono)) {contacto = (ContactoIndividual) c; break;} 
			}
			System.out.println("es que no puedo MAS JAJSAJSAJSJASJA" + contacto.getNombre());
			return contacto;
		}
		
		public boolean crearGrupo(String nombre, URL imagen) {
			return (this.listaContactos.add(new Grupo(nombre, imagen)));
		}
		
		public boolean crearGrupo(String nombre) {
			return this.crearGrupo(nombre, null);
		}
		
		public boolean introducirMiembroaGrupo(ContactoIndividual c, Grupo g) {
			if (! this.listaContactos.contains(g)) return false;
			return (g.nuevoMiembro(c));
		}
		
		public void enviarMensaje(Mensaje mensaje) {
			if (!mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje no coincide con este usuario.");
	        }

	       //Agregar el mensaje a la lista de mensajes enviados
			this.mensajesEnviados.add(mensaje);
			
	    }

	    public void recibirMensaje(Mensaje mensaje) {
	       //Se recibe el mensaje por parte del emisor y se guarda en la lista de mensajes recibidos
	    	if (mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje coincide con este usuario.");
	        }

	       //Agregar el mensaje a la lista de mensajes enviados
			this.mensajesRecibidos.add(mensaje);
	    }
	    public List<Mensaje> getChatMensaje(Usuario otroUsuario) {
	        //Uso de streams. Recupera aquellos mensajes
	    	List<Mensaje> aux = Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	    			.flatMap(mensajes -> mensajes.stream()
	    					.filter(msg -> msg.getEmisor().equals(otroUsuario) || msg.getReceptor().equals(otroUsuario)))
	    			.sorted()
	    			.collect(Collectors.toList());
	    	int i = 0;
			for(Mensaje m : aux) {
				System.out.println("Lista Mensajes filtrado " + i + " : " + m);
			}
	    	return aux;
	    	
	    	
	    }
	    
	    public Mensaje getUltimoChatMensaje(Usuario otroUsuario) {
	        return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(mensajes -> mensajes.stream()
	                        .filter(msg -> msg.getEmisor().equals(otroUsuario) || msg.getReceptor().equals(otroUsuario)))
	                .max(Comparator.naturalOrder()) // Asumiendo que Mensaje implementa Comparable por fecha
	                .orElse(null);
	    }

	    public List<Contacto> getListaContactosConMensajes() {
	    	List<Contacto> contactos = this.listaContactos;
	    	// Usuarios que ya están en la lista de contactos
	        Set<Usuario> usuariosEnContactos = contactos.stream()
	                .filter(c -> c instanceof ContactoIndividual) // Solo ContactoIndividual tiene Usuario
	                .map(c -> ((ContactoIndividual) c).getUsuario())
	                .collect(Collectors.toSet());

	        // Usuarios con los que he chateado pero no son contactos, ni soy yo
	        Set<Usuario> nuevosUsuarios = Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(List::stream)
	                .flatMap(msg -> Stream.of(msg.getEmisor(), msg.getReceptor()))
	                .filter(usuario -> !usuariosEnContactos.contains(usuario))
	                .filter(usuario -> !usuario.equals(this))
	                .collect(Collectors.toSet());

	        // Crear nuevos ContactoIndividual para esos usuarios
	        List<Contacto> nuevosContactos = nuevosUsuarios.stream()
	                .map(usuario -> new ContactoIndividual(usuario.getTelefono(), usuario)) // Nombre = teléfono
	                .collect(Collectors.toList());

	        // Crear nueva lista combinando existentes + nuevos
	        List<Contacto> todosLosContactos = new ArrayList<>(contactos);
	        todosLosContactos.addAll(nuevosContactos);

	        return todosLosContactos;
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
		
		public void setMensajesRecibidos(List<Mensaje> lista) {
			this.mensajesRecibidos = new LinkedList<Mensaje>(lista);
			
		}
		
		public void setMensajesEnviados(List<Mensaje> lista) {
			this.mensajesEnviados = new LinkedList<Mensaje>(lista);
			
		}
		
		
		public void setListaContacto(List<Contacto> lista) {this.listaContactos = new LinkedList<Contacto>(lista);}

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
	    	private final List<Contacto> listaContactos; 
	    	private boolean premium;
			private final List<Mensaje> listaDeMensajesRecibidos;
			private final List<Mensaje> listaDeMensajesEnviados;
	        
	        public BuilderUsuario() {
	        	this.listaContactos = new LinkedList<Contacto>();
	        	this.listaDeMensajesRecibidos = new LinkedList<Mensaje>();
	        	this.listaDeMensajesEnviados = new LinkedList<Mensaje>();
	        }
	        
	        public BuilderUsuario nombre(String nombre) {this.nombre=nombre;return this;}
	        public BuilderUsuario telefono(String telefono) {this.telefono=telefono;return this;}
	        public BuilderUsuario apellidos(String apellidos) {this.apellidos=apellidos;return this;}
	        public BuilderUsuario fechaNac(LocalDate fechaNac) {this.fechaNac=fechaNac;return this;}
	        public BuilderUsuario email(String email) {this.email=email;return this;}
	        public BuilderUsuario password(String password) {this.password=password;return this;}
	        public BuilderUsuario saludo(String saludo) {this.saludo=saludo;return this;}
	        public BuilderUsuario premium(boolean premium) {this.premium=premium; return this;}
	        public BuilderUsuario imagenDePerfil(URL imagen) {
	        	if (imagen == null) return this;
	        	this.imagen = imagen;return this;
	        	}
	        public BuilderUsuario listaDeContactos(List<Contacto> lista) {this.listaContactos.addAll(lista);return this;}
	        public BuilderUsuario listaDeMensajesRecibidos(List<Mensaje> listaR) {this.listaDeMensajesRecibidos.addAll(listaR);return this;}
	        public BuilderUsuario listaDeMensajesEnviados(List<Mensaje> listaE) {this.listaDeMensajesEnviados.addAll(listaE);return this;}
	        public Usuario build() {return new Usuario(this);}
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Usuario)) return false;
	        Usuario usuario = (Usuario) o;
	        return telefono.equals(usuario.telefono);
	    }
	    @Override
	    public int hashCode() {
	        return Objects.hash(telefono);
	    }

		public void setSaludo(String saludo) {
			this.saludo = saludo;
			
		}
		
		
}



