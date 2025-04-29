package umu.tds.modelos;

import java.net.URL;
import java.util.Objects;


//Clase abstracta para implementar el Patron Composite
//Tanto Grupos como Contactos Individuales son 
//tratados de igual manera, aqui se define la 
//estructura comun de ambos

public abstract class Contacto implements Comparable<Contacto> {

    // Atributos comunes a todos los contactos
    private String nombre;
    private URL imagen;
    private int codigo;

    // Constructor para contactos sin imagen
    public Contacto(String nombre) {
        this(nombre, null);
    }

    // Constructor para contactos con imagen
    public Contacto(String nombre, URL imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.codigo = 0;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(URL imagen) {
        this.imagen = imagen;
    }
    
    public URL getURLImagen() {
    	return this.imagen;
    }

	public int getCodigo() {
		return this.codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
    
    // Métodos abstractos (obligatorios en subclases)
    public abstract TipoContacto getTipoContacto();

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        // Comparación específica basada en el tipo
        if (this instanceof ContactoIndividual && contacto instanceof ContactoIndividual) {
            return Objects.equals(((ContactoIndividual) this).getUsuario().getTelefono(),
                    ((ContactoIndividual) contacto).getUsuario().getTelefono());
        } else if (this instanceof Grupo && contacto instanceof Grupo) {
            return Objects.equals(this.nombre, contacto.nombre) && Objects.equals(((Grupo)this).getAnfitrion(), ((Grupo) contacto).getAnfitrion()) ;
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (this instanceof ContactoIndividual) {
            return Objects.hash(((ContactoIndividual) this).getUsuario().getTelefono());
        } else if (this instanceof Grupo) {
            return Objects.hash(this.nombre);
        }
        return super.hashCode();
    }

    @Override
    public int compareTo(Contacto otro) {
        if (otro == null) {
            throw new NullPointerException("El contacto comparado no puede ser null");
        }

        // Priorizamos los grupos sobre los contactos individuales
        if (this instanceof Grupo && !(otro instanceof Grupo)) {
            return -1; // Este contacto (Grupo) va antes
        } else if (!(this instanceof Grupo) && otro instanceof Grupo) {
            return 1; // El otro contacto (Grupo) va antes
        }

        // Si son del mismo tipo entonces se compara por nombre
        return this.nombre.compareTo(otro.nombre);
    }
    
    public static enum TipoContacto {
    	INDIVIDUAL, GRUPO;
    }
    
}

