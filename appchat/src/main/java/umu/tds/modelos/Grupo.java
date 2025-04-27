package umu.tds.modelos;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
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
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;

        Grupo grupo = (Grupo) o;
        return this.getNombre().equals(grupo.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre());
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
}
