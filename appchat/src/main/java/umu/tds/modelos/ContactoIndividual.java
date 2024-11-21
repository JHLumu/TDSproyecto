package umu.tds.modelos;

public class ContactoIndividual extends Contacto {

    // Atributo específico: el usuario asociado al contacto
    private Usuario usuario;

    // Constructor
    public ContactoIndividual(String nombre, Usuario usuario) {
        super(nombre);  // Llamamos al constructor de la clase base (Contacto)
        this.usuario = usuario;
    }

    // Getter y setter para el usuario
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.setNombre(usuario.getNombre()); // Actualizar el nombre si el usuario cambia
    }
    
    public String getTelefono() {
        return usuario.getTelefono();
    }

    // Implementación del método abstracto
    @Override
    public String getTipoContacto() {
        return "Individual";
    }

    // Método para mostrar detalles del contacto
    public String mostrarDetalles() {
        return "Nombre: " + usuario.getNombre() +
        		"\nTeléfono: " + usuario.getTelefono() +
               "\nCorreo Electrónico: " + usuario.getEmail();
    }
}

