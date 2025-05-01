package umu.tds.modelos;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Grupo extends Contacto {

    // Atributos específicos para Grupo
	private final String anfitrion;
    private List<Contacto> miembros;

    // Constructor para crear un grupo con miembros sin imagen
    public Grupo(String nombre, URL imagen, String anfitrion) {
    	super(nombre, imagen);
    	this.anfitrion = anfitrion;
    	this.miembros = new LinkedList<>();
    }

    // Constructor para crear un grupo con miembros y con imagen
    public Grupo(String nombre, URL imagen, String anfitrion, List<Contacto> contactos) {
        this(nombre, imagen, anfitrion);
        this.miembros = contactos;
    }

    // Métodos getter y setter
    public List <Contacto> getMiembros() {
        return miembros;
    }
    
    public String getAnfitrion() {
    	return anfitrion;
    }

    // Redefinición de equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;              // misma referencia
        if (!(o instanceof Grupo)) return false; // null o distinto tipo

        Grupo otro = (Grupo) o;
        return Objects.equals(this.getNombre(), otro.getNombre())
            && Objects.equals(this.getAnfitrion(), otro.getAnfitrion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getNombre(), anfitrion);
    }

    // Funcionalidad para agregar un nuevo miembro
    public boolean nuevoMiembro(ContactoIndividual c) {
        return this.miembros.add(c);
    }

    // Implementación del método abstracto
    @Override
    public TipoContacto getTipoContacto() {
        return TipoContacto.GRUPO;
    }

	public void eliminarMiembro(ContactoIndividual c) {
		// TODO Auto-generated method stub
		this.miembros.remove(c);
	}
}
