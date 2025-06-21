package umu.tds.appchat.servicios;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.TipoContacto;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.ContactoDAO;
import umu.tds.persistencia.UsuarioDAO;
import umu.tds.utils.Estado;
import umu.tds.utils.ImagenUtils;
import umu.tds.utils.TDSObservable;

/**
 * Servicio para la gestión de contactos (individuales y grupos)
 * Maneja la creación, modificación y consulta de contactos
 */
public class ServicioContactos{
    
    private final ContactoDAO contactoDAO;
    private final UsuarioDAO usuarioDAO;
    private final CatalogoUsuarios catalogoUsuarios;
    private final TDSObservable observable;
    
    /**
     * Constructor del servicio de contactos
     * 
     * @param contactoDAO DAO para persistencia de contactos
     * @param usuarioDAO DAO para persistencia de usuarios
     * @param observable Observable para notificar cambios
     */
    public ServicioContactos(ContactoDAO contactoDAO, UsuarioDAO usuarioDAO, TDSObservable observable) {
        this.contactoDAO = contactoDAO;
        this.usuarioDAO = usuarioDAO;
        this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
        this.observable = observable;
    }
    
    // ---------- GESTIÓN DE CONTACTOS INDIVIDUALES ----------
    
    /**
     * Crea un nuevo contacto individual para el usuario
     * 
     * @param usuario Usuario que crea el contacto
     * @param nombre Nombre del contacto
     * @param telefono Teléfono del contacto
     * @return Resultado de la operación: -1 si el teléfono no está registrado, 
     *         0 si ya es contacto, 1 si se añadió correctamente
     */
    public int crearContactoIndividual(Usuario usuario, String nombre, String telefono) {
        // Verificar si el teléfono está registrado en el sistema
        if (!catalogoUsuarios.estaUsuarioRegistrado(telefono)) {
            return -1;
        }
        
        // Obtener el usuario asociado al teléfono
        Usuario usuarioAsociado = catalogoUsuarios.getUsuario(telefono);
        
        // Intentar crear el contacto
        if (!usuario.crearContacto(nombre, usuarioAsociado)) {
            return 0;
        }
        
        // Obtener el contacto creado y persistir los cambios
        ContactoIndividual contacto = usuario.recuperarContactoIndividual(telefono);
        
        // Persistir y notificar cambios
        persistirCambiosContacto(contacto, usuario);
        
        return 1;
    }
    
    /**
     * Crea una instancia ContactoIndividual dado un teléfono
     * 
     * @param telefono Teléfono del contacto
     * @return Instancia ContactoIndividual si el teléfono está registrado, null en caso contrario. 
     */
    public Contacto crearContactoIndividual(String telefono) {
        // Verificar si el teléfono está registrado en el sistema
        if (!catalogoUsuarios.estaUsuarioRegistrado(telefono)) return null;        

        // Obtener el usuario asociado al teléfono
        Usuario usuarioAsociado = catalogoUsuarios.getUsuario(telefono);

        // Obtener el contacto creado y persistir los cambios
        ContactoIndividual contacto = new ContactoIndividual(telefono, usuarioAsociado);
        
        return contacto;
    }
    
    // ---------- GESTIÓN DE GRUPOS ----------
    
    /**
     * Crea un nuevo grupo vacío
     * 
     * @param usuario Usuario que crea el grupo
     * @param nombre Nombre del grupo
     * @param urlImagen URL de la imagen del grupo (opcional)
     * @return Resultado de la operación
     */
    public int crearGrupo(Usuario usuario, String nombre, URL urlImagen) {
        // Verificar si ya existe un grupo con ese nombre
        if (usuario.recuperarGrupo(nombre) != null) {
            return 0;
        }

        // Crear el grupo
        usuario.crearGrupo(nombre, urlImagen);
        Grupo grupo = usuario.recuperarGrupo(nombre);
     
        // Guardar la imagen si se proporcionó
        if (urlImagen != null && !ImagenUtils.guardarImagen(grupo)) {
            return -1;
        }
           
        // Persistir y notificar cambios
        persistirCambiosContacto(grupo, usuario);
            
        return 1;
    }
    
    /**
     * Añade un miembro a un grupo
     * 
     * @param usuario Usuario propietario del grupo
     * @param grupo Grupo al que añadir el miembro
     * @param contacto Contacto individual que se añadirá como miembro
     */
    public void agregarMiembroGrupo(Usuario usuario, Grupo grupo, Contacto contacto) {
        
        Grupo grupoRecuperado = usuario.nuevoMiembroGrupo(grupo, contacto);
        
        if (grupoRecuperado != null) {
        	// Persistir cambios
            contactoDAO.modificarContacto(grupoRecuperado);
            usuarioDAO.modificarUsuario(usuario);
            notificarCambios();
        }
    }
    
    /**
     * Elimina un miembro de un grupo
     * 
     * @param usuario Usuario propietario del grupo
     * @param grupo Grupo del que eliminar el miembro
     * @param contacto Contacto individual que se eliminará como miembro
     */
    public void eliminarMiembroGrupo(Usuario usuario, Grupo grupo, Contacto contacto) {
        Grupo grupoRecuperado = usuario.eliminarMiembroGrupo(grupo, contacto);
        
        if (grupoRecuperado != null) {
        	// Persistir cambios
            contactoDAO.modificarContacto(grupoRecuperado);
            usuarioDAO.modificarUsuario(usuario);
            notificarCambios();
        }
        
        
        
    }
    
    // ---------- CONSULTAS DE CONTACTOS ----------
    
    /**
     * Obtiene la lista completa de contactos del usuario
     * @param usuario El usuario del que se quieren obtener los contactos.
     * @return Una lista de objetos Contacto.
     */
    public List<Contacto> obtenerTodosLosContactos(Usuario usuario) {
        return usuario.getListaContacto();
    }
    
    /**
     * Obtiene sólo los contactos individuales, ordenados por nombre
     * @param usuario El usuario del que se quieren obtener los contactos individuales.
     * @return Una lista de objetos Contacto que son contactos individuales.
     */
    public List<Contacto> obtenerContactosIndividuales(Usuario usuario) {
        if (usuario == null) {
            return new LinkedList<>();
        }

        return usuario.getListaContacto().stream()
                .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL))
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene sólo los grupos, ordenados por nombre
     * @param usuario El usuario del que se quieren obtener los grupos.
     * @return Una lista de objetos Contacto que son grupos.
     */
    public List<Contacto> obtenerGrupos(Usuario usuario) {
        if (usuario == null) {
            return new LinkedList<>();
        }

        return usuario.getListaContacto().stream()
                .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.GRUPO))
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene la lista de contactos con los que se ha intercambiado mensajes
     * @param usuario El usuario del que se quieren obtener los contactos con mensajes.
     * @return Una lista de objetos Contacto.
     */
    public List<Contacto> obtenerContactosConMensajes(Usuario usuario) {
        return usuario.getListaContactosConMensajes();
    }
    
    /**
     * Verifica si un contacto pertenece a la lista de contactos del usuario
     * @param usuario El usuario.
     * @param contacto El contacto a verificar.
     * @return true si el contacto pertenece a la lista de contactos del usuario, false en caso contrario.
     */
    public boolean esContacto(Usuario usuario, Contacto contacto) {
        return usuario.getListaContacto().contains(contacto);
    }
    
    /**
     * Obtiene el teléfono de un contacto o del anfitrión si es un grupo
     * @param contacto El contacto del que se quiere obtener el teléfono.
     * @return El número de teléfono del contacto si es individual, o el del anfitrión si es un grupo.
     */
    public String obtenerTelefonoContacto(Contacto contacto) {
        return contacto instanceof ContactoIndividual ? 
                ((ContactoIndividual) contacto).getTelefono() :
                ((Grupo) contacto).getAnfitrion();
    }
    
    /**
     * Obtiene la lista de miembros de un grupo
     * @param grupo El grupo del que se quieren obtener los miembros.
     * @return Una lista de objetos Contacto que son miembros del grupo.
     */
    public List<Contacto> obtenerMiembrosGrupo(Grupo grupo) {
        return grupo.getMiembros();
    }
    
    // ---------- MÉTODOS AUXILIARES ----------
    
    /**
     * Persiste los cambios de un contacto y notifica a los observadores
     * @param contacto El contacto cuyos cambios se van a persistir.
     * @param usuario El usuario al que pertenece el contacto.
     */
    private void persistirCambiosContacto(Contacto contacto, Usuario usuario) {
        contactoDAO.registrarContacto(contacto);
        usuarioDAO.modificarUsuario(usuario);
        notificarCambios();
    }
    
    /**
     * Notifica cambios a los observadores
     */
    private void notificarCambios() {
        observable.setChanged(Estado.INFO_CONTACTO);
        observable.notifyObservers(Estado.INFO_CONTACTO);
    }

}