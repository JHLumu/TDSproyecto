package umu.tds.modelos;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;

public class Grupo {
	
	//Atributos de la Clase
		private final String nombre;
		private final URL imagen;
		private final HashSet<Contacto> miembros;
	
	//Constructor de la Clase
	
	/**
	 * Crea una nueva instancia de "Grupo" con el nombre, imagen e contactos del grupo.
	 *
	 * @param nombre el nombre del grupo
	 * @param imagen la imagen del grupo
	 * @param contactos los contactos que pertenecen al grupo
	 *
	 */
		public Grupo(String nombre, URL imagen, Contacto... usuarios) {
			this.nombre = nombre;
			this.imagen = imagen;
			this.miembros = new HashSet<Contacto>();
			Collections.addAll(this.miembros, usuarios);
		}

	//Metodos getter y setter
		public String getNombre() {
			return nombre;
		}

		public URL getImagen() {
			return imagen;
		}

		public HashSet<Contacto> getMiembros() {
			return miembros;
		}

		
	
	
}
