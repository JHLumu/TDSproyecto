package umu.tds.modelos;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
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

import javax.imageio.ImageIO;

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

		public void setSaludo(String saludo) {
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
		
		
		public URL getURLImagen() {
			return imagenPerfil;
		}
		
		public void setURLImagen(URL imagen) {
			this.imagenPerfil = imagen;
		}
		
		public Image getFotoPerfilUsuario() {
			File fileImagenContacto = new File("imagenesUsuarios", nombre+"-"+ telefono + ".png");
    	    
    		if (fileImagenContacto.exists()) {
    			Image localImage;
				try {
					localImage = ImageIO.read(fileImagenContacto);
					return localImage;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
    			
    		}
    		
    		else return null;
    		
    		
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
		/*
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
		*/
		public ContactoIndividual recuperarContactoIndividual(String telefono) {
		    for (Contacto contacto : this.listaContactos) {
		        if (contacto.getTipoContacto() == TipoContacto.INDIVIDUAL) {
		            ContactoIndividual individual = (ContactoIndividual) contacto;
		            if (individual.getTelefono().equals(telefono)) {
		                System.out.println("[DEBUG recuperarContactoIndividual]: Contacto Individual encontrado: " + individual.getNombre());
		                return individual;
		            }
		        }
		    }
		    System.out.println("[DEBUG recuperarContactoIndividual]: No se encontró contacto individual con teléfono: " + telefono);
		    return null;
		}

		public Grupo recuperarGrupo(String nombre) {
		    for (Contacto contacto : this.listaContactos) {
		        if (contacto.getTipoContacto() == TipoContacto.GRUPO) {
		            Grupo grupo = (Grupo) contacto;
		            if (grupo.getNombre().equals(nombre)) {
		                System.out.println("[DEBUG recuperarGrupo]: Grupo encontrado: " + grupo.getNombre());
		                return grupo;
		            }
		        }
		    }
		    System.out.println("[DEBUG recuperarGrupo]: No se encontró grupo con nombre: " + nombre);
		    return null;
		}


		public boolean crearGrupo(String nombre, URL imagen) {
			Grupo contacto = new Grupo(nombre, imagen, this.telefono);
			return this.listaContactos.add(contacto);
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
	    
	    public List<Mensaje> getChatMensaje(Contacto contacto) {
	    	List<Mensaje> mensajes = new LinkedList<Mensaje>();

	        if (contacto instanceof ContactoIndividual) {
	            return mensajes = getMensajesIndividual((ContactoIndividual) contacto);
	        } else if (contacto instanceof Grupo) {
	            return mensajes = getMensajesGrupo((Grupo) contacto);
	        }
			return mensajes; 
	    	
	    }
	    
	    public Mensaje getUltimoChatMensaje(Contacto contacto) {
	        List<Mensaje> mensajesFiltrados;

	        if (contacto instanceof ContactoIndividual) {
	            mensajesFiltrados = getMensajesIndividual((ContactoIndividual) contacto);
	        } else if (contacto instanceof Grupo) {
	            mensajesFiltrados = getMensajesGrupo((Grupo) contacto);
	        } else {
	            return null;
	        }

	        return mensajesFiltrados.stream()
	                .max(Comparator.naturalOrder()) // Asumiendo que Mensaje implementa Comparable por fecha
	                .orElse(null);
	    }

	    private List<Mensaje> getMensajesGrupo(Grupo contacto) {
	        return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(List::stream)
	                .filter(msg -> msg.getIDGrupo() != -1)
	                .filter(msg -> msg.getGrupo().equals(contacto))
	                .sorted()
	                .collect(Collectors.toList());
	    }

	    private List<Mensaje> getMensajesIndividual(ContactoIndividual contacto) {
	        Usuario otroUsuario = contacto.getUsuario();
	        return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(List::stream)
	                .filter(msg -> msg.getIDGrupo() == -1)
	                .filter(msg ->
	                    (msg.getEmisor().equals(this) && msg.getReceptor().equals(otroUsuario)) ||
	                    (msg.getReceptor().equals(this) && msg.getEmisor().equals(otroUsuario))
	                )
	                .sorted()
	                .collect(Collectors.toList());
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
		
		
	
		public List<Contacto> getListaContactosConMensajes() {
		    Set<Usuario> usuariosConMensajes = obtenerUsuariosConMensajesIndividuales(); // solo individuales
		    Set<Grupo> gruposConMensajes = obtenerGruposConMensajes(); // solo grupos

		    List<Contacto> contactosIndividuales = obtenerContactosIndividualesConMensajes(usuariosConMensajes);
		    List<Contacto> nuevosContactos = crearContactosIndividualesNuevos(usuariosConMensajes, contactosIndividuales);

		    List<Contacto> gruposExistentes = obtenerGruposExistentesConMensajes(gruposConMensajes);
		    List<Contacto> nuevosGrupos = crearGruposNuevos(gruposConMensajes, gruposExistentes);

		    List<Contacto> todosLosContactos = new ArrayList<>();
		    todosLosContactos.addAll(contactosIndividuales);
		    todosLosContactos.addAll(nuevosContactos);
		    todosLosContactos.addAll(gruposExistentes);
		    todosLosContactos.addAll(nuevosGrupos);

		    return todosLosContactos;
		}

		private Set<Usuario> obtenerUsuariosConMensajesIndividuales() {
			return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
		            .flatMap(List::stream)
		            .filter(msg -> msg.getIDGrupo() == -1)
		            .flatMap(msg -> {
		                Usuario emisor = msg.getEmisor();
		                Usuario receptor = msg.getReceptor();
		                if (emisor.equals(this)) return Stream.of(receptor);
		                else return Stream.of(emisor);
		            })
		            .collect(Collectors.toSet());
		}

		private Set<Grupo> obtenerGruposConMensajes() {
		    return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
		            .flatMap(List::stream)
		            .filter(msg -> msg.getIDGrupo() != -1)
		            .map(Mensaje::getGrupo)
		            .filter(Objects::nonNull)
		            .collect(Collectors.toSet());
		}

		private List<Contacto> obtenerContactosIndividualesConMensajes(Set<Usuario> usuariosConMensajes) {
		    return this.listaContactos.stream()
		            .filter(c -> c instanceof ContactoIndividual)
		            .filter(c -> usuariosConMensajes.contains(((ContactoIndividual) c).getUsuario()))
		            .collect(Collectors.toList());
		}

		private List<Contacto> crearContactosIndividualesNuevos(Set<Usuario> usuariosConMensajes, List<Contacto> existentes) {
		    Set<Usuario> usuariosExistentes = existentes.stream()
		            .map(c -> ((ContactoIndividual) c).getUsuario())
		            .collect(Collectors.toSet());

		    return usuariosConMensajes.stream()
		            .filter(u -> !usuariosExistentes.contains(u))
		            .map(u -> new ContactoIndividual(u.getTelefono(), u))
		            .collect(Collectors.toList());
		}

		private List<Contacto> obtenerGruposExistentesConMensajes(Set<Grupo> gruposConMensajes) {
		    return this.listaContactos.stream()
		            .filter(c -> c instanceof Grupo)
		            .filter(c -> gruposConMensajes.contains((Grupo) c))
		            .collect(Collectors.toList());
		}

		private List<Contacto> crearGruposNuevos(Set<Grupo> gruposConMensajes, List<Contacto> existentes) {
		    Set<Grupo> gruposExistentes = existentes.stream()
		            .map(c -> (Grupo) c)
		            .collect(Collectors.toSet());

		    return gruposConMensajes.stream()
		            .filter(g -> !gruposExistentes.contains(g))
		            .collect(Collectors.toList());
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
		
		
}



