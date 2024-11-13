package umu.tds.modelos;

import java.net.URL;
import java.util.Objects;

public class Contacto {

	//Atributos de la clase
		private String nombre;
		private String telefono;
		private URL imagen;
	
	//Constructor de la clase
		public Contacto(String nombre, String telefono) {
			this.nombre = nombre;
			this.telefono = telefono;
		}
		
		public Contacto(String nombre, String telefono, URL imagen) {
			this(nombre,telefono);
			this.imagen = imagen;
		}
			

	//Metodos getter y setter
		
		public String getNombre() {
			return nombre;
		}

		public String getTelefono() {
			return telefono;
		}


		public URL getImagen() {
			return imagen;
		}

		public void setImagen(URL imagen) {
			this.imagen = imagen;
		}
		
		
		
	//Redeficiones de metodos
		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			else if (this == o) return true;
			else if (this.getClass() != o.getClass()) return false;
			
			Contacto objeto = (Contacto) o;
			return (this.telefono == objeto.telefono);
			
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(telefono);
		}
	
	
	
	
	
	
}
