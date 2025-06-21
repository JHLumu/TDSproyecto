package umu.tds.modelos;


import java.net.URL;

/**
 * Clase que hereda de Contacto y representa el objeto simple en la estructura del patrón Composite.
 */
public class ContactoIndividual extends Contacto {

    // Atributo específico: el usuario asociado al contacto
    private Usuario usuario;

    /**
     * Constructor de la clase ContactoIndividual.
     * @param nombre El nombre del contacto individual.
     * @param usuario El objeto Usuario asociado a este contacto.
     */
    public ContactoIndividual(String nombre, Usuario usuario) {
        super(nombre, usuario.getURLImagen());  // Llamamos al constructor de la clase base (Contacto)
        this.usuario = usuario;
        
    }
    
    /**
     * Obtiene el nombre del usuario asociado a este contacto individual.
     * @return El nombre del usuario.
     */
    public String getNombreUsuario() {
    	return usuario.getNombre();
    }

    /**
     * Obtiene el objeto Usuario asociado a este contacto individual.
     * @return El objeto Usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el objeto Usuario asociado a este contacto individual.
     * También actualiza el nombre del contacto si el usuario cambia.
     * @param usuario El nuevo objeto Usuario a asociar.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.setNombre(usuario.getNombre()); // Actualizar el nombre si el usuario cambia
    }
    
    /**
     * Obtiene el número de teléfono del usuario asociado a este contacto individual.
     * @return El número de teléfono del usuario.
     */
    public String getTelefono() {
        return usuario.getTelefono();
    }

    /**
     * Implementación del método abstracto para obtener el tipo de contacto.
     * @return El tipo de contacto, que es {@link TipoContacto#INDIVIDUAL}.
     */
    @Override
    public TipoContacto getTipoContacto() {
        return TipoContacto.INDIVIDUAL;
    }

    /**
     * Método para mostrar los detalles del contacto individual.
     * @return Una cadena con el nombre, teléfono y correo electrónico del usuario.
     */
    public String mostrarDetalles() {
        return "Nombre: " + usuario.getNombre() +
        		"\nTeléfono: " + usuario.getTelefono() +
               "\nCorreo Electrónico: " + usuario.getEmail();
    }
    
    /**
     * Obtiene el saludo del usuario asociado a este contacto individual.
     * @return El saludo del usuario.
     */
    public String getSaludo() {
    	return usuario.getSaludo();
    }
    
    /**
     * Obtiene la URL de la imagen del usuario asociado a este contacto individual.
     * @return La URL de la imagen del usuario.
     */
    @Override
    public URL getURLImagen() {
    	return usuario.getURLImagen();
    }
    
    /**
     * Compara este objeto ContactoIndividual con el objeto especificado para determinar si son iguales.
     * La igualdad se basa en el usuario asociado.
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
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