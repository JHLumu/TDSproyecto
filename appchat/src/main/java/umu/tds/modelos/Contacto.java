package umu.tds.modelos;

import java.net.URL;
import java.util.Objects;

public abstract class Contacto {

    // Atributos comunes a todos los contactos
    private String nombre;
    private URL imagen;

    // Constructor para contactos sin imagen
    public Contacto(String nombre) {
        this.nombre = nombre;
    }

    // Constructor para contactos con imagen
    public Contacto(String nombre, URL imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public URL getImagen() {
        return imagen;
    }

    public void setImagen(URL imagen) {
        this.imagen = imagen;
    }

    // Métodos abstractos (obligatorios en subclases)
    public abstract String getTipoContacto();

    // Redefiniciones de métodos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacto contacto = (Contacto) o;
        return Objects.equals(nombre, contacto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
