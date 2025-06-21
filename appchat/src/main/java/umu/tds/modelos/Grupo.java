package umu.tds.modelos;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Grupo extends Contacto {

    // Atributos específicos para Grupo
	private final String anfitrion;
    private List<Contacto> miembros;

    /**
     * Constructor para crear un grupo con una imagen y un anfitrión.
     * Los miembros se inicializan como una lista vacía.
     * @param nombre el nombre del grupo.
     * @param imagen la URL de la imagen del grupo.
     * @param anfitrion el número de teléfono del usuario que creó el grupo.
     */
    public Grupo(String nombre, URL imagen, String anfitrion) {
    	super(nombre, imagen);
    	this.anfitrion = anfitrion;
    	this.miembros = new LinkedList<>();
    }

    /**
     * Constructor para crear un grupo con una imagen, un anfitrión y una lista inicial de miembros.
     * @param nombre el nombre del grupo.
     * @param imagen la URL de la imagen del grupo.
     * @param anfitrion el número de teléfono del usuario que creó el grupo.
     * @param contactos la lista inicial de Contacto que serán miembros del grupo.
     */
    public Grupo(String nombre, URL imagen, String anfitrion, List<Contacto> contactos) {
        this(nombre, imagen, anfitrion);
        this.miembros = contactos;
    }

    // Métodos getter y setter
    /**
     * Obtiene la lista de miembros del grupo.
     * @return una lista de Contacto que son miembros del grupo.
     */
    public List <Contacto> getMiembros() {
        return miembros;
    }
    
    /**
     * Obtiene el número de teléfono del anfitrión del grupo.
     * @return el número de teléfono del anfitrión.
     */
    public String getAnfitrion() {
    	return anfitrion;
    }

    // Redefinición de equals y hashCode
    /**
     * Compara este objeto Grupo con el objeto especificado.
     * La comparación se basa en el nombre del grupo y el anfitrión.
     * @param o el objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;              // misma referencia
        if (!(o instanceof Grupo)) return false; // null o distinto tipo

        Grupo otro = (Grupo) o;
        return Objects.equals(this.getNombre(), otro.getNombre())
            && Objects.equals(this.getAnfitrion(), otro.getAnfitrion());
    }

    /**
     * Devuelve un valor de código hash para el objeto.
     * El código hash se basa en el nombre del grupo y el anfitrión.
     * @return un valor de código hash para este objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getNombre(), anfitrion);
    }

    /**
     * Añade un nuevo miembro al grupo.
     * @param c el ContactoIndividual que se va a añadir como miembro.
     * @return true si el miembro se añadió exitosamente.
     */
    public boolean nuevoMiembro(ContactoIndividual c) {
        return this.miembros.add(c);
    }

    /**
     * Implementación del método abstracto para obtener el tipo de contacto.
     * @return el tipo de contacto, en este caso TipoContacto.GRUPO.
     */
    @Override
    public TipoContacto getTipoContacto() {
        return TipoContacto.GRUPO;
    }

	/**
	 * Elimina un miembro del grupo.
	 * @param c el ContactoIndividual que se va a eliminar del grupo.
	 */
	public void eliminarMiembro(ContactoIndividual c) {
		this.miembros.remove(c);
	}
}