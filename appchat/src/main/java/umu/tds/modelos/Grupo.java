package umu.tds.modelos;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;


public class Grupo extends Contacto {

    // Atributos específicos para Grupo
    private final HashSet<ContactoIndividual> miembros;

    // Constructor para crear un grupo con miembros sin imagen
    public Grupo(String nombre, ContactoIndividual... usuarios) {
        super(nombre);
        this.miembros = new HashSet<>();
        Collections.addAll(this.miembros, usuarios);
    }

    // Constructor para crear un grupo con miembros y con imagen
    public Grupo(String nombre, URL imagen, ContactoIndividual... usuarios) {
        super(nombre, imagen);
        this.miembros = new HashSet<>();
        Collections.addAll(this.miembros, usuarios);
    }

    // Métodos getter y setter
    public ContactoIndividual[] getMiembros() {
        return this.miembros.toArray(new ContactoIndividual[0]);
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
