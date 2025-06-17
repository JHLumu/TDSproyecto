package umu.tds.appchat.servicios;

import java.awt.Color;
import java.awt.Image;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import tds.BubbleText;
import umu.tds.appchat.servicios.mensajes.MensajeCoincidencia;
import umu.tds.appchat.servicios.vistas.InfoMensajeVista;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Contacto.TipoContacto;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

/**
 * Servicio responsable de gestionar la preparación de datos para la vista,
 * transformando entidades del modelo en ViewModels listos para presentar.
 * 
 */
public class ServicioVistas {
    
    // Constantes de configuración de la vista
    private static final int MAX_MESSAGE_PREVIEW_LENGTH = 16;
    private static final int MAX_SALUDO_LENGTH = 40;
    private static final int EMOJI_SIZE = 30;
    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
    
    // Servicios de dominio requeridos
    private final ServicioMensajes servicioMensajes;
    
    /**
     * Constructor que recibe las dependencias necesarias
     * 
     * @param servicioMensajes Servicio para obtener información de mensajes
     */
    public ServicioVistas(ServicioMensajes servicioMensajes) {
        this.servicioMensajes = servicioMensajes;
    }
    
    
    
    /**
     * Prepara la información de un mensaje para mostrar en la vista.
     * Actúa como un adaptador entre el modelo de dominio y la vista.
     * 
     * @param item Objeto que contiene información del mensaje (Contacto o MensajeCoincidencia)
     * @param usuario Usuario actual de la sesión
     * @return InfoMensajeVista con los datos formateados para la vista
     */
    public InfoMensajeVista prepararInfoMensajeParaVista(Object item, Usuario usuario) {
        InfoMensajeVista info = new InfoMensajeVista();
        
        Object contenido = null;
        LocalDateTime fecha = null;
        boolean esEnviado = false;

        // Extraer contenido y fecha según el tipo de objeto (patrón Strategy implícito)
        if (item instanceof Contacto) {
            Contacto contacto = (Contacto) item;
            esEnviado = servicioMensajes.isLastMensajeEnviado(usuario, contacto);
            contenido = servicioMensajes.getUltimoMensajeContenido(usuario, contacto);
            fecha = servicioMensajes.getUltimoMensajeFecha(usuario, contacto);
        } else if (item instanceof MensajeCoincidencia) {
            MensajeCoincidencia coincidencia = (MensajeCoincidencia) item;
            esEnviado = coincidencia.esEmisor(usuario.getTelefono());
            contenido = coincidencia.getContenido();
            fecha = coincidencia.getFechaEnvio();
        } else {
            return info; // Objeto no soportado
        }
        
        info.setEsMensajeEnviado(esEnviado);
        
        // Procesar el contenido según su tipo
        procesarContenidoMensaje(info, contenido);
        
        // Formatear la fecha
        info.setHoraFormateada(formatearFechaMensaje(fecha));
        
        return info;
    }
    
    /**
     * Procesa el contenido de un mensaje y lo asigna al InfoMensajeVista
     * 
     * @param info InfoMensajeVista a completar
     * @param contenido Contenido del mensaje (String o Integer)
     */
    private void procesarContenidoMensaje(InfoMensajeVista info, Object contenido) {
        if (contenido instanceof String) {
            info.setTextoFormateado(formatearTextoMensaje((String) contenido));
        } else if (contenido instanceof Integer) {
            info.setEmojiRedimensionado(obtenerEmojiRedimensionado((Integer) contenido));
        }
    }
    
    /**
     * Formatea un mensaje de texto para la vista (trunca si es muy largo)
     * 
     * @param mensajeTexto Texto original del mensaje
     * @return Texto formateado y truncado si es necesario
     */
    public String formatearTextoMensaje(String mensajeTexto) {
        if (mensajeTexto == null) {
            return "";
        }
        
        if (mensajeTexto.length() > MAX_MESSAGE_PREVIEW_LENGTH) {
            return mensajeTexto.substring(0, MAX_MESSAGE_PREVIEW_LENGTH - 1) + "...";
        }
        return mensajeTexto;
    }
    
    /**
     * Formatea la fecha de un mensaje para mostrar en la vista
     * 
     * @param fecha Fecha y hora del mensaje
     * @return Fecha formateada como string
     */
    public String formatearFechaMensaje(LocalDateTime fecha) {
        if (fecha != null) {
            return fecha.format(FECHA_FORMATTER);
        }
        return "";
    }
    
    /**
     * Obtiene un emoji redimensionado para la vista
     * 
     * @param emojiCode Código del emoji
     * @return ImageIcon del emoji redimensionado o null si no se encuentra
     */
    public ImageIcon obtenerEmojiRedimensionado(Integer emojiCode) {
        if (emojiCode == null) {
            return null;
        }
        
        ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
        if (emojiIcon != null) {
            Image img = emojiIcon.getImage();
            Image newImg = img.getScaledInstance(EMOJI_SIZE, EMOJI_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        }
        return null;
    }
    
    /**
     * Formatea el saludo de un contacto para la vista
     * 
     * @param contacto Contacto cuyo saludo se va a formatear
     * @return Saludo formateado y truncado si es necesario
     */
    public String formatearSaludoVista(Contacto contacto) {
        if (contacto.getTipoContacto() == TipoContacto.INDIVIDUAL) {
            String saludo = ((ContactoIndividual) contacto).getSaludo();
            if (saludo != null && !saludo.isEmpty()) {
                // Truncar si es muy largo
                if (saludo.length() > MAX_SALUDO_LENGTH) {
                    saludo = saludo.substring(0, MAX_SALUDO_LENGTH) + "...";
                }
                return saludo;
            }
        }
        return "";
    }
    
    /**
     * Genera las burbujas de chat para mostrar en la interfaz.
     * Transforma los mensajes del modelo en elementos visuales.
     * 
     * @param contacto Contacto del chat
     * @param chat Panel donde se mostrarán las burbujas
     * @param usuario Usuario actual
     * @return Lista de burbujas de texto listas para mostrar
     */
    public List<BubbleText> generarBurbujasMensajes(Contacto contacto, JPanel chat, Usuario usuario) {
        List<Mensaje> mensajes = usuario.getChatMensaje(contacto);
        List<BubbleText> burbujas = new ArrayList<>();

        if (mensajes == null || mensajes.isEmpty()) {
            return burbujas;
        }

        String telefonoUsuario = usuario.getTelefono();

        for (Mensaje mensaje : mensajes) {
            BubbleText bubble = crearBurbujaMensaje(mensaje, contacto, chat, usuario, telefonoUsuario);
            burbujas.add(bubble);
        }

        return burbujas;
    }
    
    /**
     * Crea una burbuja individual para un mensaje
     * 
     * @param mensaje Mensaje a convertir en burbuja
     * @param contacto Contacto del chat
     * @param chat Panel contenedor
     * @param usuario Usuario actual
     * @param telefonoUsuario Teléfono del usuario actual
     * @return BubbleText configurado
     */
    private BubbleText crearBurbujaMensaje(Mensaje mensaje, Contacto contacto, JPanel chat, 
                                         Usuario usuario, String telefonoUsuario) {
        Object contenido = (mensaje.getEmoticono() == -1) ? 
                mensaje.getTexto() : mensaje.getEmoticono();

        boolean esUsuario = mensaje.esEmisor(telefonoUsuario);
        String autor = determinarAutorMensaje(contacto, mensaje, esUsuario, usuario);
        int tipoMensaje = esUsuario ? BubbleText.SENT : BubbleText.RECEIVED;

        return (contenido instanceof String)
            ? new BubbleText(chat, (String) contenido, Color.WHITE, autor, tipoMensaje, 14)
            : new BubbleText(chat, (Integer) contenido, Color.WHITE, autor, tipoMensaje, 14);
    }
    
    /**
     * Determina el texto del autor a mostrar en la burbuja del mensaje
     * 
     * @param contacto Contacto del chat
     * @param mensaje Mensaje específico
     * @param esUsuario Si el mensaje fue enviado por el usuario actual
     * @param usuario Usuario actual
     * @return String con el nombre del autor formateado
     */
    private String determinarAutorMensaje(Contacto contacto, Mensaje mensaje, boolean esUsuario, Usuario usuario) {
        String autor;
        
        if (esUsuario) {
            autor = usuario.getNombre();
            if (mensaje.getIDGrupo() != -1 && !mensaje.getReceptorTelf().equals(usuario.getTelefono())) {
                autor += " <G> *" + mensaje.getNombreGrupo() + "*";
            }
        } else {
            autor = contacto.getNombre();
        }
        
        return autor + " <> " + formatearFechaMensaje(mensaje.getFechaEnvio());
    }
    
}