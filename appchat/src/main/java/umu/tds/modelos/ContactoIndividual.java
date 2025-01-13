package umu.tds.modelos;

//Clase que hereda de Contacto que representa 
//el objeto simple en la estructura del patron
//Composite
public class ContactoIndividual extends Contacto {

    // Atributo específico: el usuario asociado al contacto
    private Usuario usuario;

    // Constructor
    public ContactoIndividual(String nombre, Usuario usuario) {
        super(nombre, usuario.getImagenPerfil());  // Llamamos al constructor de la clase base (Contacto)
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
    public TipoContacto getTipoContacto() {
        return TipoContacto.INDIVIDUAL;
    }

    // Método para mostrar detalles del contacto
    public String mostrarDetalles() {
        return "Nombre: " + usuario.getNombre() +
        		"\nTeléfono: " + usuario.getTelefono() +
               "\nCorreo Electrónico: " + usuario.getEmail();
    }
    
    public String getSaludo() {
    	return usuario.getSaludo();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario || o instanceof ContactoIndividual)) return false;
        if(o instanceof Usuario) {
        	Usuario usuario = (Usuario) o;
        	return this.getUsuario().equals(usuario);
        } else {
        	ContactoIndividual contacto = (ContactoIndividual) o;
        	return this.getUsuario().equals(contacto.getUsuario());
        }
        
    }
}

