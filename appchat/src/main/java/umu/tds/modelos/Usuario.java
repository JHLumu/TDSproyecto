package umu.tds.modelos;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;

public class Usuario {

	//Atributos de la Clase
	//Como no se menciona nada en los requisitos sobre la posibilidad de cambiar algun dato del usuario
	//las siguientes son finales
	
		private final String nombre;
		private final String apellidos;
		private final String telefono;
		private final LocalDate fechaNac;
		//private final URL imagenPerfil;
		private final String email;	
		private final String password;
		
	//El siguiente atributo es opcional
		private String saludo;
		
	//El siguiente atributo es un booleano que representa si un usuario ha activado el Premium o no
		private boolean esPremium = false;
		
		
	//El siguiente atributo es una coleccion que representa una lista de Contactos
	//Se utiliza como colección un conjunto para así, evitar contactos repetidos 
		private final HashSet<Contacto> listaContactos; 
		
	//El siguiente atributo es una coleccion que representa una lista de Grupos
	//Se utiliza como colección un conjunto para así, evitar grupos repetidos 	
		private final HashSet<Grupo> listaGrupos; 
		
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
		public Usuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password) {
			this.listaContactos = new HashSet<Contacto>();
			this.listaGrupos = new HashSet<Grupo>();
			this.nombre = nombre;
			this.telefono = telefono;
			this.apellidos = apellidos;
			this.fechaNac = fechaNac;
			//this.imagenPerfil = imagenPerfil;
			this.email = email;
			this.password = password;
		}
		
		
		/**
		 * Crea una nueva instancia de "Usuario" con el nombre, apellidos, numero de telefono, fecha de nacimiento, imagen de perfil, correo, contraseña y un mensaje de saludo
		 *
		 * @param nombre el nombre del usuario
		 * @param apellidos los apellidos del usuario
		 * @param telefono el numero de telefono del usuario
		 * @param fecha la fecha de nacimiento del usuario
		 * @param imagen la imagen de perfil del usuario
		 * @param email el correo electronico del usuario
		 * @param contraseña la contraseña del usuario
		 * @param saludo el mensaje de saludo del usuario
		 *
		 */
		public Usuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo) {
			this(nombre,apellidos,telefono,fechaNac,email,password);
			this.saludo=saludo;
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

		/*
		public URL getImagenPerfil() {
			return imagenPerfil;
		}
		*/
		public String getEmail() {
			return email;
		}
		

	//Funcionalidades
		
		public boolean crearContacto(String nombre, String telefono, URL imagen) {
			//Ya se tiene que haber verificado que el telefono se encuentra registrado en el sistema
			return (this.listaContactos.add(new Contacto(nombre,telefono,imagen)));
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
		
		
		
	/*
		public void cambiarImagen(URL imagen) {
			this.imagenPerfil = imagen;
		}
		
	*/
	
		
}
