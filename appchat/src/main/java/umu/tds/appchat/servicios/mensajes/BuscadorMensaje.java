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
 * Clase especializada en la búsqueda de mensajes con diferentes criterios
 */
public class BuscadorMensaje {
    
    /**
     * Busca mensajes que contienen un texto específico, opcionalmente filtrando por emisor y/o receptor
     * 
     * @param usuario Usuario actual
     * @param buscarCriterio Criterios de búsqueda
     * @return Lista de mensajes coincidentes ordenados
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
     * Localiza la posición de un mensaje específico en el chat con un contacto
     * 
     * @param usuario Usuario actual
     * @param contacto Contacto
     * @param targetMensaje Mensaje a localizar
     * @return Índice del mensaje o -1 si no se encuentra
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
     * Filtra contactos según los criterios de búsqueda
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
     * Obtiene el teléfono de un contacto
     */
    private String getContactTelf(Contacto contacto) {
        return contacto instanceof ContactoIndividual ? 
                ((ContactoIndividual) contacto).getTelefono() :
                ((Grupo) contacto).getAnfitrion();
    }
    
    /**
     * Procesa la búsqueda de emojis
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
     * Procesa la búsqueda de texto
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
     * Calcula la puntuación de coincidencia de texto
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
     * Calcula la puntuación de coincidencia de contacto
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
     * Ordena los resultados de búsqueda
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
