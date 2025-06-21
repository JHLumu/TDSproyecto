package umu.tds.appchat.servicios.mensajes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

/**
 * Clase especializada en la búsqueda de mensajes con diferentes criterios.
 */
public class BuscadorMensaje {
    
    /**
     * Busca mensajes que contienen un texto específico, opcionalmente filtrando por emisor y/o receptor.
     * Los resultados se ordenan según la relevancia del texto y el contacto.
     * @param usuario El usuario actual para el que se realiza la búsqueda.
     * @param buscarCriterio Los criterios de búsqueda a aplicar (texto, contacto, etc.).
     * @return Una lista de objetos {@link MensajeCoincidencia} que representan los mensajes coincidentes,
     * ordenados por relevancia descendente.
     */
    public List<MensajeCoincidencia> buscarMensajes(Usuario usuario, CriterioBuscarMensaje buscarCriterio) {
        List<Contacto> contactos = usuario.getListaContactosConMensajes();
        List<MensajeCoincidencia> mensajesCoincidentes = new ArrayList<>();
        
        // Estructura para almacenar información de puntuación para ordenar posteriormente
        Map<MensajeCoincidencia, Double> puntuacionesTexto = new HashMap<>();
        Map<MensajeCoincidencia, Double> puntuacionesContacto = new HashMap<>();
        
        // Determinar si estamos buscando un emoji específico
        boolean busquedaEmoji = false;
        int emojiNumero = -1;
        
        if (buscarCriterio.getTextFiltro() != null && 
            buscarCriterio.getTextFiltro().matches("(?i)Emoji:\\s*([0-9]|1[0-9]|2[0-3])")) {
            busquedaEmoji = true;
            emojiNumero = Integer.parseInt(buscarCriterio.getTextFiltro().replaceAll("(?i)Emoji:\\s*", ""));
        }
        
        boolean busquedaPorTexto = buscarCriterio.hasTextFiltro();
        boolean busquedaPorContacto = buscarCriterio.hasContactFiltro();

        // Filtrar contactos si es necesario
        if (busquedaPorContacto) {
            contactos = filtrarContacto(contactos, buscarCriterio);
        }

        // Buscar en los mensajes de los contactos filtrados
        for (Contacto contacto : contactos) {
            double puntuacionContacto = calculateContactMatchScore(contacto, buscarCriterio);
            
            List<Mensaje> mensajes = usuario.getChatMensaje(contacto);
            if (mensajes != null) {
                for (Mensaje mensaje : mensajes) {
                    if (busquedaEmoji) {
                        processEmojiSearch(mensaje, contacto, emojiNumero, mensajesCoincidentes, 
                                         puntuacionesTexto, puntuacionesContacto, puntuacionContacto);
                    } else {
                        processTextSearch(mensaje, contacto, buscarCriterio, busquedaPorTexto,
                                        mensajesCoincidentes, puntuacionesTexto, puntuacionesContacto, puntuacionContacto);
                    }
                }
            }
        }

        // Ordenar resultados
        sortSearchResults(mensajesCoincidentes, puntuacionesTexto, puntuacionesContacto, busquedaPorTexto);
        
        return mensajesCoincidentes;
    }
    
    /**
     * Localiza la posición (índice) de un mensaje específico dentro de la lista de mensajes de un chat con un contacto dado.
     * @param usuario El usuario actual.
     * @param contacto El contacto con el que se comparte el chat.
     * @param targetMensaje El mensaje a localizar.
     * @return El índice del mensaje dentro del chat, o -1 si el mensaje no se encuentra.
     */
    public int ubicarMensaje(Usuario usuario, Contacto contacto, Mensaje targetMensaje) {
        List<Mensaje> mensajes = usuario.getChatMensaje(contacto);
        
        for (int i = 0; i < mensajes.size(); i++) {
            if (mensajes.get(i).getCodigo() == targetMensaje.getCodigo()) {
                return i;
            }
        }
        
        return -1;
    }
    
    // ---------- MÉTODOS PRIVADOS ----------
    
    /**
     * Filtra una lista de contactos basándose en los criterios de nombre y/o teléfono especificados en {@code criteria}.
     * @param contactos La lista de contactos a filtrar.
     * @param criteria Los criterios de búsqueda de contacto.
     * @return Una nueva lista de contactos que cumplen con los criterios de filtrado.
     */
    private List<Contacto> filtrarContacto(List<Contacto> contactos, CriterioBuscarMensaje criteria) {
        return contactos.stream()
            .filter(contacto -> {
                boolean coincideNombre = criteria.getNombreFiltro() == null || criteria.getNombreFiltro().isEmpty() ||
                    contacto.getNombre().toLowerCase().contains(criteria.getNombreFiltro().toLowerCase());
                
                boolean coincideTelefono = criteria.getTelfFiltro() == null || criteria.getTelfFiltro().isEmpty() ||
                    getContactTelf(contacto).contains(criteria.getTelfFiltro());
                
                return coincideNombre && coincideTelefono;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene el número de teléfono de un contacto, distinguiendo entre {@link ContactoIndividual} y {@link Grupo}.
     * Para un {@link ContactoIndividual}, devuelve su número de teléfono. Para un {@link Grupo}, devuelve el teléfono del anfitrión.
     * @param contacto El contacto del que se desea obtener el teléfono.
     * @return El número de teléfono del contacto o del anfitrión del grupo.
     */
    private String getContactTelf(Contacto contacto) {
        return contacto instanceof ContactoIndividual ? 
                ((ContactoIndividual) contacto).getTelefono() :
                ((Grupo) contacto).getAnfitrion();
    }
    
    /**
     * Procesa la búsqueda de mensajes cuando el criterio es un emoji específico.
     * Agrega el mensaje a la lista de coincidencias si el contenido del mensaje es un emoji y coincide con el número de emoji buscado.
     * @param mensaje El mensaje a procesar.
     * @param contacto El contacto asociado al mensaje.
     * @param emojiNumero El número del emoji que se está buscando, o -1 si se buscan todos los emojis.
     * @param mensajesCoincidentes La lista de {@link MensajeCoincidencia} donde se añadirán los resultados.
     * @param puntuacionesTexto Un mapa para almacenar las puntuaciones de coincidencia de texto de cada mensaje.
     * @param puntuacionesContacto Un mapa para almacenar las puntuaciones de coincidencia de contacto de cada mensaje.
     * @param puntuacionContacto La puntuación de coincidencia del contacto asociado.
     */
    private void processEmojiSearch(Mensaje mensaje, Contacto contacto, int emojiNumero,
                                  List<MensajeCoincidencia> mensajesCoincidentes,
                                  Map<MensajeCoincidencia, Double> puntuacionesTexto,
                                  Map<MensajeCoincidencia, Double> puntuacionesContacto,
                                  double puntuacionContacto) {
        MensajeCoincidencia coincidencia = new MensajeCoincidencia(mensaje, contacto);
        Object contenido = coincidencia.getContenido();
        
        if (contenido instanceof Integer) {
            int emojiValue = (Integer) contenido;
            
            if (emojiNumero == -1 || emojiValue == emojiNumero) {
                mensajesCoincidentes.add(coincidencia);
                double puntuacionTexto = (emojiNumero == -1 || emojiValue == emojiNumero) ? 1.0 : 0.0;
                puntuacionesTexto.put(coincidencia, puntuacionTexto);
                puntuacionesContacto.put(coincidencia, puntuacionContacto);
            }
        }
    }
    
    /**
     * Procesa la búsqueda de mensajes cuando el criterio es un texto.
     * Calcula la puntuación de coincidencia del texto y, si es relevante, agrega el mensaje a la lista de coincidencias.
     * @param mensaje El mensaje a procesar.
     * @param contacto El contacto asociado al mensaje.
     * @param criteria Los criterios de búsqueda de mensajes.
     * @param busquedaPorTexto Indica si la búsqueda actual es por texto.
     * @param mensajesCoincidentes La lista de {@link MensajeCoincidencia} donde se añadirán los resultados.
     * @param puntuacionesTexto Un mapa para almacenar las puntuaciones de coincidencia de texto de cada mensaje.
     * @param puntuacionesContacto Un mapa para almacenar las puntuaciones de coincidencia de contacto de cada mensaje.
     * @param puntuacionContacto La puntuación de coincidencia del contacto asociado.
     */
    private void processTextSearch(Mensaje mensaje, Contacto contacto, CriterioBuscarMensaje criteria,
                                 boolean busquedaPorTexto, List<MensajeCoincidencia> mensajesCoincidentes,
                                 Map<MensajeCoincidencia, Double> puntuacionesTexto,
                                 Map<MensajeCoincidencia, Double> puntuacionesContacto,
                                 double puntuacionContacto) {
        String textoMensaje = mensaje.getTexto();
        Object contenido = new MensajeCoincidencia(mensaje, contacto).getContenido();

        double puntuacionTexto = busquedaPorTexto
            ? calculateTextMatchScore(textoMensaje, criteria.getTextFiltro())
            : 1.0;

        if (!busquedaPorTexto || puntuacionTexto > 0) {
            if (!(contenido instanceof Integer) || !busquedaPorTexto) {
                MensajeCoincidencia coincidencia = new MensajeCoincidencia(mensaje, contacto);
                mensajesCoincidentes.add(coincidencia);

                puntuacionesTexto.put(coincidencia, contenido instanceof Integer ? 1.0 : puntuacionTexto);
                puntuacionesContacto.put(coincidencia, puntuacionContacto);
            }
        }

    }
    
    /**
     * Calcula una puntuación de coincidencia para un texto de mensaje dado un filtro.
     * La puntuación se basa en si el filtro está contenido en el mensaje, su longitud relativa y su posición.
     * @param messageText El texto del mensaje a evaluar.
     * @param filter El texto de filtro a buscar.
     * @return Una puntuación double que indica la relevancia de la coincidencia (0.0 a 1.0).
     */
    private double calculateTextMatchScore(String messageText, String filter) {
        if (filter == null || filter.isEmpty()) {
            return 1.0;
        }
        
        String mensajeLower = messageText.toLowerCase();
        String filtroLower = filter.toLowerCase();
        
        if (mensajeLower.contains(filtroLower)) {
            double puntuacionLongitud = 1.0 - ((double) messageText.length() / 1000);
            if (puntuacionLongitud < 0.1) puntuacionLongitud = 0.1;
            
            int posicion = mensajeLower.indexOf(filtroLower);
            double puntuacionPosicion = 1.0 - ((double) posicion / messageText.length());
            
            return 0.7 + (0.15 * puntuacionLongitud) + (0.15 * puntuacionPosicion);
        }
        
        return 0.0;
    }
    
    /**
     * Calcula una puntuación de coincidencia para un contacto basándose en los criterios de nombre y/o teléfono.
     * La puntuación refleja qué tan bien el nombre y/o el teléfono del contacto coinciden con los filtros.
     * @param contacto El contacto a evaluar.
     * @param criteria Los criterios de búsqueda de contacto.
     * @return Una puntuación double que indica la relevancia de la coincidencia (0.0 a 1.0).
     */
    private double calculateContactMatchScore(Contacto contacto, CriterioBuscarMensaje criteria) {
        double puntuacion = 1.0;
        
        if (criteria.getNombreFiltro() != null && !criteria.getNombreFiltro().isEmpty()) {
            String nombreContacto = contacto.getNombre().toLowerCase();
            String filtroNombre = criteria.getNombreFiltro().toLowerCase();
            
            if (nombreContacto.equals(filtroNombre)) {
                puntuacion *= 1.0;
            } else if (nombreContacto.contains(filtroNombre)) {
                int posicion = nombreContacto.indexOf(filtroNombre);
                double factorPosicion = 1.0 - ((double) posicion / nombreContacto.length());
                puntuacion *= 0.7 + (0.3 * factorPosicion);
            } else {
                return 0.0;
            }
        }
        
        if (criteria.getTelfFiltro() != null && !criteria.getTelfFiltro().isEmpty()) {
            String telefonoContacto = getContactTelf(contacto);
            
            if (telefonoContacto.equals(criteria.getTelfFiltro())) {
                puntuacion *= 1.0;
            } else if (telefonoContacto.contains(criteria.getTelfFiltro())) {
                puntuacion *= 0.8;
            } else {
                return 0.0;
            }
        }
        
        return puntuacion;
    }
    
    /**
     * Ordena la lista de {@link MensajeCoincidencia} según las puntuaciones de texto y/o contacto.
     * Si {@code busquedaPorTexto} es true, se prioriza la puntuación de texto, y luego la fecha de envío descendente.
     * Si es false, se prioriza la puntuación de contacto, y luego la fecha de envío descendente.
     * @param mensajesCoincidentes La lista de mensajes coincidentes a ordenar.
     * @param puntuacionesTexto Un mapa con las puntuaciones de coincidencia de texto para cada mensaje.
     * @param puntuacionesContacto Un mapa con las puntuaciones de coincidencia de contacto para cada mensaje.
     * @param busquedaPorTexto Un booleano que indica si la búsqueda principal fue por texto.
     */
    private void sortSearchResults(List<MensajeCoincidencia> mensajesCoincidentes,
                                 Map<MensajeCoincidencia, Double> puntuacionesTexto,
                                 Map<MensajeCoincidencia, Double> puntuacionesContacto,
                                 boolean busquedaPorTexto) {
        if (busquedaPorTexto) {
            Collections.sort(mensajesCoincidentes, (m1, m2) -> {
                int comparacion = Double.compare(puntuacionesTexto.get(m2), puntuacionesTexto.get(m1));
                if (comparacion == 0) {
                    return m2.getFechaEnvio().compareTo(m1.getFechaEnvio());
                }
                return comparacion;
            });
        } else {
            Collections.sort(mensajesCoincidentes, (m1, m2) -> {
                int comparacion = Double.compare(puntuacionesContacto.get(m2), puntuacionesContacto.get(m1));
                if (comparacion == 0) {
                    return m2.getFechaEnvio().compareTo(m1.getFechaEnvio());
                }
                return comparacion;
            });
        }
    }
}