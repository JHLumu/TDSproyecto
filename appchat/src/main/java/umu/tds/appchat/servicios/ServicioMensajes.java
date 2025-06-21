package umu.tds.appchat.servicios;

import java.time.LocalDateTime;
import java.util.List;

import umu.tds.appchat.servicios.mensajes.BuscadorMensaje;
import umu.tds.appchat.servicios.mensajes.CriterioBuscarMensaje;
import umu.tds.appchat.servicios.mensajes.MensajeCoincidencia;
import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.MensajeDAO;
import umu.tds.persistencia.UsuarioDAO;

/**
 * Servicio que gestiona todas las operaciones relacionadas con mensajes
 */
public class ServicioMensajes{
    
    private final MensajeDAO mensajeDAO;
    private final UsuarioDAO usuarioDAO;
    private final BuscadorMensaje buscadorMensaje;
    
    /**
     * Constructor del servicio de mensajes.
     * @param mensajeDAO El DAO para la persistencia de mensajes.
     * @param usuarioDAO El DAO para la persistencia de usuarios.
     */
    public ServicioMensajes(MensajeDAO mensajeDAO, UsuarioDAO usuarioDAO) {
        this.mensajeDAO = mensajeDAO;
        this.usuarioDAO = usuarioDAO;
        this.buscadorMensaje = new BuscadorMensaje();
    }

    /**
     * Verifica si el contenido de un mensaje es válido (String o Integer).
     * @param contenido El contenido a validar.
     * @return true si el contenido es válido, false en caso contrario.
     */
    private boolean esContenidoValido(Object contenido) {
    	return ((contenido instanceof String || contenido instanceof Integer));
    }
    
    /**
     * Envía un mensaje a un contacto o grupo
     * @param emisor Usuario que envía el mensaje
     * @param contacto Destinatario (individual o grupo)
     * @param contenido Contenido del mensaje (String para texto, Integer para emoji)
     * @return true si se envió correctamente, false en caso contrario
     */
    public boolean enviarMensaje(Usuario emisor, Contacto contacto, Object contenido) {
        // Validar tipo de entrada
        if (!esContenidoValido(contenido)) return false;
        
        // Caso: mensaje a contacto individual
        if (contacto instanceof ContactoIndividual) {
            return enviarMensajeIndividual(emisor, (ContactoIndividual) contacto, contenido);
        } 
        // Caso: mensaje a grupo
        else if (contacto instanceof Grupo) {
            return enviarMensajeGrupo(emisor, (Grupo) contacto, contenido);
        }

        return false;
    }
    
    /**
     * Envía un mensaje a un usuario especificado por su número de teléfono.
     * @param emisor El usuario que envía el mensaje.
     * @param telefono El número de teléfono del destinatario.
     * @param contenido El contenido del mensaje (String para texto, Integer para emoji).
     * @return true si el mensaje se envió correctamente, false en caso contrario.
     */
    public boolean enviarMensaje(Usuario emisor, String telefono, Object contenido) {
    	 // Validar tipo de entrada
    	 if (!esContenidoValido(contenido)) return false;
    	 return enviarMensajeTelefono(emisor, CatalogoUsuarios.getInstancia().getUsuario(telefono), (String) contenido);
    	
    }
    
    /**
     * Envía un mensaje directo a un usuario por teléfono
     * @param emisor Usuario que envía el mensaje
     * @param receptor Usuario que recibe el mensaje
     * @param text Texto del mensaje
     * @return true si el mensaje se envió correctamente, false en caso contrario.
     */
    public boolean enviarMensajeTelefono(Usuario emisor, Usuario receptor, String text) {
        if (receptor != null && !text.isEmpty()) {
            Mensaje mensaje = new Mensaje(emisor, receptor, text, null);

            // Persistir y actualizar en ambos usuarios
            mensajeDAO.registrarMensaje(mensaje);
            emisor.enviarMensaje(mensaje);
            usuarioDAO.modificarUsuario(emisor);
            receptor.recibirMensaje(mensaje);
            usuarioDAO.modificarUsuario(receptor);
            return true;
        }
        else return false;
    }
    
    /**
     * Obtiene el contenido del último mensaje con un contacto
     * @param usuario Usuario actual
     * @param contacto Contacto
     * @return Contenido del último mensaje o null si no existe
     */
    public Object getUltimoMensajeContenido(Usuario usuario, Contacto contacto) {
        Mensaje mensaje = usuario.getUltimoChatMensaje(contacto);
        return mensaje != null ? mensaje.getContenido() : null;
    }
    
    /**
     * Obtiene la fecha del último mensaje con un contacto
     * @param usuario Usuario actual
     * @param contacto Contacto
     * @return Fecha del último mensaje o null si no existe
     */
    public LocalDateTime getUltimoMensajeFecha(Usuario usuario, Contacto contacto) {
        Mensaje mensaje = usuario.getUltimoChatMensaje(contacto);
        return mensaje != null ? mensaje.getFechaEnvio() : null;
    }
    
    /**
     * Obtiene todos los mensajes de un usuario
     * @param usuario Usuario
     * @return Lista de todos los mensajes del usuario
     */
    public List<Mensaje> getTodosMensajes(Usuario usuario) {
        return usuario.getMensajes();
    }
    
    /**
     * Busca mensajes que contienen un texto específico, opcionalmente filtrando por emisor y/o receptor
     * Delega la funcionalidad al BuscadorMensaje
     * @param usuario Usuario actual
     * @param buscarCriterio Criterios de búsqueda
     * @return Lista de mensajes coincidentes ordenados
     */
    public List<MensajeCoincidencia> buscarMensajes(Usuario usuario, CriterioBuscarMensaje buscarCriterio) {
        return buscadorMensaje.buscarMensajes(usuario, buscarCriterio);
    }
    
    /**
     * Localiza la posición de un mensaje específico en el chat con un contacto
     * Delega la funcionalidad al BuscadorMensaje
     * @param usuario Usuario actual
     * @param contacto Contacto
     * @param targetMensaje Mensaje a localizar
     * @return Índice del mensaje o -1 si no se encuentra
     */
    public int ubicarMensaje(Usuario usuario, Contacto contacto, Mensaje targetMensaje) {
        return buscadorMensaje.ubicarMensaje(usuario, contacto, targetMensaje);
    }
    
    /**
     * Verifica si el último mensaje con un contacto fue enviado por el usuario
     * @param usuario Usuario actual
     * @param contacto Contacto
     * @return true si el último mensaje fue enviado por el usuario
     */
    public boolean isLastMensajeEnviado(Usuario usuario, Contacto contacto) {
        Mensaje mensaje = usuario.getUltimoChatMensaje(contacto);
        return mensaje != null && mensaje.esEmisor(usuario.getTelefono());
    }
    
    // ---------- MÉTODOS PRIVADOS ----------
    
    /**
     * Envía un mensaje a un contacto individual
     * @param emisor El usuario que envía el mensaje.
     * @param contacto El contacto individual al que se envía el mensaje.
     * @param contenido El contenido del mensaje.
     * @return true si el mensaje se envió correctamente, false en caso contrario.
     */
    private boolean enviarMensajeIndividual(Usuario emisor, ContactoIndividual contacto, Object contenido) {
        Usuario receptor = contacto.getUsuario();
        Mensaje mensaje = crearMensaje(emisor, receptor, contenido, null);

        // Persistir y actualizar en ambos usuarios
        mensajeDAO.registrarMensaje(mensaje);
        emisor.enviarMensaje(mensaje);
        usuarioDAO.modificarUsuario(emisor);
        receptor.recibirMensaje(mensaje);
        usuarioDAO.modificarUsuario(receptor);

        return true;
    }
    
    /**
     * Envía un mensaje a un grupo
     * @param emisor El usuario que envía el mensaje.
     * @param grupo El grupo al que se envía el mensaje.
     * @param contenido El contenido del mensaje.
     * @return true si el mensaje se envió correctamente, false en caso contrario.
     */
    private boolean enviarMensajeGrupo(Usuario emisor, Grupo grupo, Object contenido) {
        List<Contacto> miembros = grupo.getMiembros();
        
        // Enviar a todos los miembros
        for (Contacto miembro : miembros) {
            Usuario receptor = ((ContactoIndividual) miembro).getUsuario();
            Mensaje mensaje = crearMensaje(emisor, receptor, contenido, grupo);
            
            // Persistir mensaje
            mensajeDAO.registrarMensaje(mensaje);
            emisor.enviarMensaje(mensaje);
            receptor.recibirMensaje(mensaje);
            usuarioDAO.modificarUsuario(receptor);
        }
        
        // Crear mensaje para el propio grupo
        Mensaje mensajeGrupo = crearMensaje(emisor, emisor, contenido, grupo);
        mensajeDAO.registrarMensaje(mensajeGrupo);
        emisor.enviarMensaje(mensajeGrupo);
        usuarioDAO.modificarUsuario(emisor);
        
        return true;
    }
    
    /**
     * Crea un mensaje según el tipo de contenido
     * @param emisor El usuario emisor del mensaje.
     * @param receptor El usuario receptor del mensaje.
     * @param contenido El contenido del mensaje (String o Integer).
     * @param grupo El grupo al que pertenece el mensaje, o null si es un mensaje individual.
     * @return Un objeto Mensaje.
     */
    private Mensaje crearMensaje(Usuario emisor, Usuario receptor, Object contenido, Grupo grupo) {
        if (contenido instanceof String) {
            return new Mensaje(emisor, receptor, (String) contenido, grupo);
        } else {
            return new Mensaje(emisor, receptor, (Integer) contenido, grupo);
        }
    }

}