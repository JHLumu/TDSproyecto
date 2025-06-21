package umu.tds.modelos;

import java.net.URL;
import java.util.Objects;


/**
 * Clase abstracta para implementar el Patrón Composite.
 * Tanto los Grupos como los Contactos Individuales son tratados de igual manera,
 * aquí se define la estructura común de ambos.
 */
public abstract class Contacto implements Comparable<Contacto> {

    // Atributos comunes a todos los contactos
    private String nombre;
    private URL imagen;
    private int codigo;

    /**
     * Constructor para contactos sin imagen.
     * @param nombre El nombre del contacto.
     */
    public Contacto(String nombre) {
        this(nombre, null);
    }

    /**
     * Constructor para contactos con imagen.
     * @param nombre El nombre del contacto.
     * @param imagen La URL de la imagen del contacto.
     */
    public Contacto(String nombre, URL imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.codigo = 0;
    }

    /**
     * Obtiene el nombre del contacto.
     * @return El nombre del contacto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del contacto.
     * @param nombre El nuevo nombre del contacto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la imagen del contacto.
     * @param imagen La URL de la nueva imagen del contacto.
     */
    public void setImagen(URL imagen) {
        this.imagen = imagen;
    }
    
    /**
     * Obtiene la URL de la imagen del contacto.
     * @return La URL de la imagen del contacto.
     */
    public URL getURLImagen() {
    	return this.imagen;
    }

    /**
     * Obtiene el código identificador del contacto.
     * @return El código del contacto.
     */
	public int getCodigo() {
		return this.codigo;
	}
	
	/**
	 * Establece el código identificador del contacto.
	 * @param codigo El nuevo código del contacto.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
    
    /**
     * Método abstracto para obtener el tipo de contacto.
     * Debe ser implementado por las subclases.
     * @return El tipo de contacto (Individual o Grupo).
     */
    public abstract TipoContacto getTipoContacto();

    
    /**
     * Compara este objeto Contacto con el objeto especificado para determinar si son iguales.
     * La igualdad se basa en el número de teléfono para {@link ContactoIndividual} y en el nombre y anfitrión para {@link Grupo}.
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
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

    /**
     * Devuelve un valor de código hash para el objeto.
     * El código hash se basa en el número de teléfono para {@link ContactoIndividual} y en el nombre para {@link Grupo}.
     * @return Un valor de código hash para este objeto.
     */
    @Override
    public int hashCode() {
        if (this instanceof ContactoIndividual) {
            return Objects.hash(((ContactoIndividual) this).getUsuario().getTelefono());
        } else if (this instanceof Grupo) {
            return Objects.hash(this.nombre);
        }
        return super.hashCode();
    }

    /**
     * Compara este objeto con el objeto especificado para el orden.
     * Los grupos tienen prioridad sobre los contactos individuales. Si son del mismo tipo, se comparan por nombre.
     * @param otro El objeto Contacto a comparar.
     * @return Un entero negativo, cero o un entero positivo si este objeto es menor, igual o mayor que el objeto especificado.
     * @throws NullPointerException Si el contacto comparado es null.
     */
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
    
    /**
     * Enumeración que define los tipos de contacto posibles.
     */
    public static enum TipoContacto {
    	INDIVIDUAL, GRUPO;
    }
    
}