package umu.tds.modelos;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class Grupo {
	
	//Podemos hacer que Grupo y Contacto heredan de una clase general ya que estos dos comparten atributos
	//y metodos que opinas
	
	
	//Atributos de la Clase
		private final String nombre;
		private URL imagen;
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
		
		public Grupo(String nombre,Contacto... usuarios) {
			this.nombre = nombre;
			this.miembros = new HashSet<Contacto>();
			Collections.addAll(this.miembros, usuarios);
		}
		
		public Grupo(String nombre, URL imagen, Contacto... usuarios) {
			this(nombre,usuarios);
			this.imagen = imagen;
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

		
		
	//Redeficiones de metodos
		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			else if (this == o) return true;
			else if (this.getClass() != o.getClass()) return false;
				
			Grupo objeto = (Grupo) o;
			return (this.nombre.equals(objeto.nombre));
				
		}
			
		@Override
		public int hashCode() {
			return Objects.hash(nombre);
		}
		
	//Funcionalidades
		public boolean nuevoMiembro(Contacto c) {
			return (this.miembros.add(c));
		}
		
	
}
