package umu.tds.modelos;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		 * Crea una nueva instancia de "Usuario" utilizando un patrón Builder.
		 *
		 * @param b el objeto BuilderUsuario que contiene los datos para construir el usuario.
		 *
		 */
		public Usuario(BuilderUsuario b) {
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
			this.fechaRegistro = b.fechaRegistro;
		}

	//Metodos getter y setter
		
		/**
		 * Obtiene una copia de la lista de contactos del usuario.
		 * @return una lista de Contacto.
		 */
		public List<Contacto> getListaContacto(){
			return new LinkedList<Contacto>(this.listaContactos);
		}
		
		/**
		 * Obtiene el saludo actual del usuario.
		 * @return el saludo del usuario.
		 */
		public String getSaludo() {
			return saludo;
		}

		/**
		 * Establece un nuevo saludo para el usuario.
		 * @param saludo el nuevo saludo.
		 */
		public void setSaludo(String saludo) {
			this.saludo = saludo;
		}

		/**
		 * Verifica si el usuario tiene la membresía Premium activada.
		 * @return true si el usuario es Premium, false en caso contrario.
		 */
		public boolean isPremium() {
			return premium;
		}

		/**
		 * Establece el estado de la membresía Premium del usuario.
		 * @param esPremium true para activar Premium, false para desactivarlo.
		 */
		public void setPremium(boolean esPremium) {
			this.premium = esPremium;
		}

		/**
		 * Obtiene el nombre del usuario.
		 * @return el nombre del usuario.
		 */
		public String getNombre() {
			return nombre;
		}

		/**
		 * Obtiene los apellidos del usuario.
		 * @return los apellidos del usuario.
		 */
		public String getApellidos() {
			return apellidos;
		}

		/**
		 * Obtiene el número de teléfono del usuario.
		 * @return el número de teléfono del usuario.
		 */
		public String getTelefono() {
			return telefono;
		}

		/**
		 * Obtiene la fecha de nacimiento del usuario.
		 * @return la fecha de nacimiento del usuario.
		 */
		public LocalDate getFechaNacimiento() {
			return fechaNac;
		}

		/**
		 * Obtiene la contraseña del usuario.
		 * @return la contraseña del usuario.
		 */
		public String getPassword() {
			return password;
		}
		
		
		/**
		 * Obtiene la URL de la imagen de perfil del usuario.
		 * @return la URL de la imagen de perfil.
		 */
		public URL getURLImagen() {
			return imagenPerfil;
		}
		
		/**
		 * Establece la URL de la imagen de perfil del usuario.
		 * @param imagen la nueva URL de la imagen de perfil.
		 */
		public void setURLImagen(URL imagen) {
			this.imagenPerfil = imagen;
		}
		
		
		/**
		 * Obtiene el correo electrónico del usuario.
		 * @return el correo electrónico del usuario.
		 */
		public String getEmail() {
			return email;
		}
		
		/**
		 * Obtiene el código identificador del usuario.
		 * @return el código del usuario.
		 */
		public int getCodigo() {
			return codigo;
		}
		
		/**
		 * Establece el código identificador del usuario.
		 * @param id el nuevo código del usuario.
		 */
		public void setCodigo(int id) {
			this.codigo = id;
		}

		/**
		 * Obtiene la fecha de registro del usuario.
		 * @return la fecha de registro del usuario.
		 */
		public LocalDate getFechaRegistro() {
			return this.fechaRegistro;
		}
	//Funcionalidades
		
		/**
		 * Crea un nuevo contacto individual y lo añade a la lista de contactos del usuario.
		 * @param nombre el nombre del nuevo contacto.
		 * @param usuario el objeto Usuario asociado al nuevo contacto.
		 * @return true si el contacto se creó y añadió exitosamente, false si ya existía.
		 */
		public boolean crearContacto(String nombre, Usuario usuario) {
			ContactoIndividual contacto = new ContactoIndividual(nombre, usuario);
			if (this.listaContactos.contains(contacto)) return false;
			return this.listaContactos.add(contacto);
		}
	
		/**
		 * Recupera un contacto individual de la lista de contactos del usuario por su número de teléfono.
		 * @param telefono el número de teléfono del contacto a recuperar.
		 * @return el objeto ContactoIndividual si se encuentra, o null en caso contrario.
		 */
		public ContactoIndividual recuperarContactoIndividual(String telefono) {
		    for (Contacto contacto : this.listaContactos) {
		        if (contacto.getTipoContacto() == TipoContacto.INDIVIDUAL) {
		            ContactoIndividual individual = (ContactoIndividual) contacto;
		            if (individual.getTelefono().equals(telefono)) {
		                return individual;
		            }
		        }
		    }
		    return null;
		}

		/**
		 * Recupera un grupo de la lista de contactos del usuario por su nombre.
		 * @param nombre el nombre del grupo a recuperar.
		 * @return el objeto Grupo si se encuentra, o null en caso contrario.
		 */
		public Grupo recuperarGrupo(String nombre) {
		    for (Contacto contacto : this.listaContactos) {
		        if (contacto.getTipoContacto() == TipoContacto.GRUPO) {
		            Grupo grupo = (Grupo) contacto;
		            if (grupo.getNombre().equals(nombre)) return grupo;
		        }
		    }
		    return null;
		}

		/**
		 * Crea un nuevo grupo y lo añade a la lista de contactos del usuario.
		 * @param nombre el nombre del nuevo grupo.
		 * @param imagen la URL de la imagen del grupo.
		 * @return true si el grupo se creó y añadió exitosamente.
		 */
		public boolean crearGrupo(String nombre, URL imagen) {
			Grupo contacto = new Grupo(nombre, imagen, this.telefono);
			return this.listaContactos.add(contacto);
		}
		
		/**
		 * Crea un nuevo grupo sin imagen y lo añade a la lista de contactos del usuario.
		 * @param nombre el nombre del nuevo grupo.
		 * @return true si el grupo se creó y añadió exitosamente.
		 */
		public boolean crearGrupo(String nombre) {
			return this.crearGrupo(nombre, null);
		}
		
		/**
		 * Introduce un nuevo miembro a un grupo existente.
		 * @param c el contacto individual a añadir como miembro.
		 * @param g el grupo al que se va a añadir el miembro.
		 * @return true si el miembro se añadió exitosamente, false si el grupo no existe en la lista de contactos del usuario.
		 */
		public boolean introducirMiembroaGrupo(ContactoIndividual c, Grupo g) {
			if (! this.listaContactos.contains(g)) return false;
			return (g.nuevoMiembro(c));
		}
		
		/**
		 * Añade un mensaje a la lista de mensajes enviados por el usuario.
		 * @param mensaje el mensaje a añadir.
		 * @throws IllegalArgumentException si el emisor del mensaje no coincide con este usuario.
		 */
		public void enviarMensaje(Mensaje mensaje) {
			if (!mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje no coincide con este usuario.");
	        }

	       //Agregar el mensaje a la lista de mensajes enviados
			this.mensajesEnviados.add(mensaje);
			
	    }

		/**
		 * Añade un mensaje a la lista de mensajes recibidos por el usuario.
		 * @param mensaje el mensaje a añadir.
		 * @throws IllegalArgumentException si el emisor del mensaje coincide con este usuario.
		 */
	    public void recibirMensaje(Mensaje mensaje) {
	       //Se recibe el mensaje por parte del emisor y se guarda en la lista de mensajes recibidos
	    	if (mensaje.esEmitidoPor(this)) {
	            throw new IllegalArgumentException("El emisor del mensaje coincide con este usuario.");
	        }

	       //Agregar el mensaje a la lista de mensajes enviados
			this.mensajesRecibidos.add(mensaje);
	    }
	    
	    /**
	     * Obtiene la lista de mensajes de un chat específico (individual o de grupo).
	     * @param contacto el contacto (individual o grupo) del que se desean obtener los mensajes.
	     * @return una lista de Mensaje correspondientes al chat.
	     */
	    public List<Mensaje> getChatMensaje(Contacto contacto) {
	    	List<Mensaje> mensajes = new LinkedList<Mensaje>();

	        if (contacto instanceof ContactoIndividual) {
	            return mensajes = getMensajesIndividual((ContactoIndividual) contacto);
	        } else if (contacto instanceof Grupo) {
	            return mensajes = getMensajesGrupo((Grupo) contacto);
	        }
			return mensajes; 
	    	
	    }
	    
	    /**
	     * Obtiene el último mensaje de un chat específico (individual o de grupo).
	     * @param contacto el contacto (individual o grupo) del que se desea obtener el último mensaje.
	     * @return el último Mensaje del chat, o null si no hay mensajes.
	     */
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

	    /**
	     * Obtiene los mensajes de un chat de grupo.
	     * @param contacto el grupo del que se desean obtener los mensajes.
	     * @return una lista de Mensaje del chat de grupo.
	     */
	    private List<Mensaje> getMensajesGrupo(Grupo contacto) {
	        return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(List::stream)
	                .filter(msg -> msg.getIDGrupo() != -1)
	                .filter(msg -> msg.getEmisor().equals(msg.getReceptor())) // Mensajes de grupo tienen emisor y receptor iguales (referencia al grupo)
	                .filter(msg -> msg.getGrupo().equals(contacto))
	                .sorted()
	                .collect(Collectors.toList());
	    }

	    /**
	     * Obtiene los mensajes de un chat individual.
	     * @param contacto el contacto individual del que se desean obtener los mensajes.
	     * @return una lista de Mensaje del chat individual.
	     */
	    private List<Mensaje> getMensajesIndividual(ContactoIndividual contacto) {
	        Usuario otroUsuario = contacto.getUsuario();
	        return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
	                .flatMap(List::stream)
	                .filter(msg ->
	                    (msg.getEmisor().equals(this) && msg.getReceptor().equals(otroUsuario)) ||
	                    (msg.getReceptor().equals(this) && msg.getEmisor().equals(otroUsuario))
	                )
	                .sorted()
	                .collect(Collectors.toList());
	    }
		
		/**
		 * Obtiene una copia de la lista de mensajes recibidos por el usuario.
		 * @return una lista de Mensaje.
		 */
		public List<Mensaje> getMensajesRecibidos() {
			return new LinkedList<Mensaje>(this.mensajesRecibidos);
			
		}
		
		/**
		 * Obtiene una copia de la lista de mensajes enviados por el usuario.
		 * @return una lista de Mensaje.
		 */
		public List<Mensaje> getMensajesEnviados() {
			return new LinkedList<Mensaje>(this.mensajesEnviados);
			
		}
		
		/**
		 * Establece la lista de mensajes recibidos del usuario.
		 * @param lista la nueva lista de mensajes recibidos.
		 */
		public void setMensajesRecibidos(List<Mensaje> lista) {
			this.mensajesRecibidos = new LinkedList<Mensaje>(lista);
			
		}
		
		/**
		 * Establece la lista de mensajes enviados del usuario.
		 * @param lista la nueva lista de mensajes enviados.
		 */
		public void setMensajesEnviados(List<Mensaje> lista) {
			this.mensajesEnviados = new LinkedList<Mensaje>(lista);
			
		}
		
		/**
		 * Establece la lista de contactos del usuario.
		 * @param lista la nueva lista de contactos.
		 */
		public void setListaContacto(List<Contacto> lista) {this.listaContactos = new LinkedList<Contacto>(lista);}
		
		
		/**
		 * Obtiene una lista de todos los contactos (individuales y grupos) con los que el usuario ha tenido mensajes,
		 * ordenados por la fecha del último mensaje (más reciente primero).
		 * @return una lista de Contacto con mensajes.
		 */
		public List<Contacto> getListaContactosConMensajes() {
		    Set<Usuario> usuariosConMensajes = obtenerUsuariosConMensajesIndividuales(); // solo individuales
		    Set<Grupo> gruposConMensajes = obtenerGruposConMensajes(); // solo grupos

		    List<Contacto> contactosIndividuales = obtenerContactosIndividualesConMensajes(usuariosConMensajes);
		    List<Contacto> nuevosContactos = crearContactosIndividualesNuevos(usuariosConMensajes, contactosIndividuales);

		    List<Contacto> gruposExistentes = obtenerGruposExistentesConMensajes(gruposConMensajes);
		    //List<Contacto> nuevosGrupos = crearGruposNuevos(gruposConMensajes, gruposExistentes);

		    List<Contacto> todosLosContactos = new ArrayList<>();
		    todosLosContactos.addAll(contactosIndividuales);
		    todosLosContactos.addAll(nuevosContactos);
		    todosLosContactos.addAll(gruposExistentes);

		    // Ordenar por fecha del último mensaje
		    todosLosContactos.sort((c1, c2) -> {
		        LocalDateTime fecha1 = getUltimoChatMensaje(c1).getFechaEnvio();
		        LocalDateTime fecha2 = getUltimoChatMensaje(c2).getFechaEnvio();
		        // Orden descendente (más reciente primero)
		        return fecha2.compareTo(fecha1);
		    });
		    

		    return todosLosContactos;
		}

		/**
		 * Obtiene un conjunto de usuarios con los que el usuario ha intercambiado mensajes individuales.
		 * @return un conjunto de Usuario.
		 */
		private Set<Usuario> obtenerUsuariosConMensajesIndividuales() {
			return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
		            .flatMap(List::stream)
		            //.filter(msg -> msg.getIDGrupo() == -1) //filtrar solo mensajes individuales
		            .filter(msg -> !msg.igualEmisorReceptor()) // Excluir mensajes de grupo
		            .flatMap(msg -> {
		                Usuario emisor = msg.getEmisor();
		                Usuario receptor = msg.getReceptor();
		                if (emisor.equals(this)) return Stream.of(receptor);
		                else return Stream.of(emisor);
		            })
		            .collect(Collectors.toSet());
		}

		/**
		 * Obtiene un conjunto de grupos con los que el usuario ha interactuado.
		 * @return un conjunto de Grupo.
		 */
		private Set<Grupo> obtenerGruposConMensajes() {
		    return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
		            .flatMap(List::stream)
		            .filter(msg -> msg.getIDGrupo() != -1)
		            .map(Mensaje::getGrupo)
		            .filter(Objects::nonNull)
		            .collect(Collectors.toSet());
		}

		/**
		 * Filtra los contactos individuales existentes del usuario que tienen mensajes con los usuarios dados.
		 * @param usuariosConMensajes el conjunto de usuarios con los que se han intercambiado mensajes.
		 * @return una lista de Contacto que son contactos individuales existentes y tienen mensajes.
		 */
		private List<Contacto> obtenerContactosIndividualesConMensajes(Set<Usuario> usuariosConMensajes) {
		    return this.listaContactos.stream()
		            .filter(c -> c instanceof ContactoIndividual)
		            .filter(c -> usuariosConMensajes.contains(((ContactoIndividual) c).getUsuario()))
		            .collect(Collectors.toList());
		}

		/**
		 * Crea nuevos objetos ContactoIndividual para aquellos usuarios con los que se han intercambiado mensajes
		 * pero que aún no están en la lista de contactos del usuario.
		 * @param usuariosConMensajes el conjunto de usuarios con los que se han intercambiado mensajes.
		 * @param existentes la lista de contactos individuales ya existentes.
		 * @return una lista de Contacto que son nuevos contactos individuales.
		 */
		private List<Contacto> crearContactosIndividualesNuevos(Set<Usuario> usuariosConMensajes, List<Contacto> existentes) {
		    Set<Usuario> usuariosExistentes = existentes.stream()
		            .map(c -> ((ContactoIndividual) c).getUsuario())
		            .collect(Collectors.toSet());

		    return usuariosConMensajes.stream()
		            .filter(u -> !usuariosExistentes.contains(u))
		            .map(u -> new ContactoIndividual(u.getTelefono(), u))
		            .collect(Collectors.toList());
		}

		/**
		 * Filtra los grupos existentes del usuario que tienen mensajes.
		 * @param gruposConMensajes el conjunto de grupos con los que se han intercambiado mensajes.
		 * @return una lista de Contacto que son grupos existentes y tienen mensajes.
		 */
		private List<Contacto> obtenerGruposExistentesConMensajes(Set<Grupo> gruposConMensajes) {
		    return this.listaContactos.stream()
		            .filter(c -> c instanceof Grupo)
		            .filter(c -> gruposConMensajes.contains((Grupo) c))
		            .collect(Collectors.toList());
		}


	    /**
	     * Clase interna para construir objetos Usuario utilizando el patrón Builder.
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
	    	private final List<Contacto> listaContactos; 
	    	private boolean premium;
			private final List<Mensaje> listaDeMensajesRecibidos;
			private final List<Mensaje> listaDeMensajesEnviados;
			private LocalDate fechaRegistro;
	        
	        /**
	         * Constructor del BuilderUsuario. Inicializa las listas de contactos y mensajes como vacías.
	         */
	        public BuilderUsuario() {
	        	this.listaContactos = new LinkedList<Contacto>();
	        	this.listaDeMensajesRecibidos = new LinkedList<Mensaje>();
	        	this.listaDeMensajesEnviados = new LinkedList<Mensaje>();
	        	this.fechaRegistro = LocalDate.now();
	        }
	        
	        /**
	         * Establece el nombre del usuario.
	         * @param nombre el nombre.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario nombre(String nombre) {this.nombre=nombre;return this;}
	        /**
	         * Establece el teléfono del usuario.
	         * @param telefono el teléfono.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario telefono(String telefono) {this.telefono=telefono;return this;}
	        /**
	         * Establece los apellidos del usuario.
	         * @param apellidos los apellidos.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario apellidos(String apellidos) {this.apellidos=apellidos;return this;}
	        /**
	         * Establece la fecha de nacimiento del usuario.
	         * @param fechaNac la fecha de nacimiento.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario fechaNac(LocalDate fechaNac) {this.fechaNac=fechaNac;return this;}
	        /**
	         * Establece el email del usuario.
	         * @param email el email.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario email(String email) {this.email=email;return this;}
	        /**
	         * Establece la contraseña del usuario.
	         * @param password la contraseña.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario password(String password) {this.password=password;return this;}
	        /**
	         * Establece el saludo del usuario.
	         * @param saludo el saludo.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario saludo(String saludo) {this.saludo=saludo;return this;}
	        /**
	         * Establece si el usuario es premium.
	         * @param premium true si es premium, false en caso contrario.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario premium(boolean premium) {this.premium=premium; return this;}
	        /**
	         * Establece la imagen de perfil del usuario.
	         * @param imagen la URL de la imagen.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario imagenDePerfil(URL imagen) {
	        	if (imagen == null) return this;
	        	this.imagen = imagen;return this;
	        	}
	        /**
	         * Añade una lista de contactos al usuario.
	         * @param lista la lista de contactos.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario listaDeContactos(List<Contacto> lista) {this.listaContactos.addAll(lista);return this;}
	        /**
	         * Añade una lista de mensajes recibidos al usuario.
	         * @param listaR la lista de mensajes recibidos.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario listaDeMensajesRecibidos(List<Mensaje> listaR) {this.listaDeMensajesRecibidos.addAll(listaR);return this;}
	        /**
	         * Añade una lista de mensajes enviados al usuario.
	         * @param listaE la lista de mensajes enviados.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario listaDeMensajesEnviados(List<Mensaje> listaE) {this.listaDeMensajesEnviados.addAll(listaE);return this;}
	        /**
	         * Establece la fecha de registro del usuario.
	         * @param fecha la fecha de registro.
	         * @return el BuilderUsuario para encadenar llamadas.
	         */
	        public BuilderUsuario fechaRegistro(LocalDate fecha) {this.fechaRegistro = fecha; return this;}
	        
	        /**
	         * Construye y devuelve una nueva instancia de Usuario con los atributos configurados.
	         * @return una nueva instancia de Usuario.
	         */
	        public Usuario build() {return new Usuario(this);}
	    }
	    
	    /**
	     * Compara este objeto Usuario con el objeto especificado.
	     * La comparación se basa únicamente en el número de teléfono.
	     * @param o el objeto a comparar.
	     * @return true si los objetos son iguales, false en caso contrario.
	     */
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Usuario)) return false;
	        Usuario usuario = (Usuario) o;
	        return telefono.equals(usuario.telefono);
	    }
	    
	    /**
	     * Devuelve un valor de código hash para el objeto.
	     * El código hash se basa en el número de teléfono.
	     * @return un valor de código hash para este objeto.
	     */
	    @Override
	    public int hashCode() {
	        return Objects.hash(telefono);
	    }

		/**
		 * Obtiene una lista combinada y ordenada de todos los mensajes enviados y recibidos por el usuario.
		 * @return una lista de Mensaje ordenada cronológicamente.
		 */
		public List<Mensaje> getMensajes() {
			
			return Stream.of(this.mensajesEnviados, this.mensajesRecibidos)
					.flatMap(List::stream)
	                .sorted()
	                .collect(Collectors.toList());
			
		}

		/**
		 * Añade un nuevo miembro a un grupo existente del usuario.
		 * @param grupo el grupo al que se desea añadir el miembro.
		 * @param contacto el contacto (que debe ser individual) a añadir como miembro.
		 * @return el objeto Grupo modificado, o null si el contacto no es individual.
		 */
		public Grupo nuevoMiembroGrupo(Grupo grupo, Contacto contacto) {
			if (!(contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL))) {
	            return null;
	        }
	        
	        Grupo recuperado = recuperarGrupo(grupo.getNombre());
	        ContactoIndividual miembro = recuperarContactoIndividual(
	                ((ContactoIndividual) contacto).getTelefono());
	        
	        recuperado.nuevoMiembro(miembro);
	        
	        return recuperado;
		}

		/**
		 * Elimina un miembro de un grupo existente del usuario.
		 * @param grupo el grupo del que se desea eliminar el miembro.
		 * @param contacto el contacto (que debe ser individual) a eliminar como miembro.
		 * @return el objeto Grupo modificado, o null si el contacto no es individual.
		 */
		public Grupo eliminarMiembroGrupo(Grupo grupo, Contacto contacto) {
			if (!(contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL))) {
	            return null;
	        }
	        
	        Grupo recuperado = recuperarGrupo(grupo.getNombre());
	        ContactoIndividual miembro = recuperarContactoIndividual(
	                ((ContactoIndividual) contacto).getTelefono());
	        
	        recuperado.eliminarMiembro(miembro);
	        
	        return recuperado;
		}		
}