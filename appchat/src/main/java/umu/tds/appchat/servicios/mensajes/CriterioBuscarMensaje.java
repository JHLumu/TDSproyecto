package umu.tds.appchat.servicios.mensajes;

/**
 * Clase para encapsular los criterios de búsqueda de mensajes
 */
public class CriterioBuscarMensaje {
    private final String textFiltro;
    private final String nameFiltro;
    private final String phoneFiltro;
    
    /**
     * Constructor de CriterioBuscarMensaje.
     * @param textFiltro El texto a buscar en el contenido de los mensajes.
     * @param nameFiltro El nombre del contacto a filtrar.
     * @param phoneFiltro El número de teléfono del contacto a filtrar.
     */
    public CriterioBuscarMensaje(String textFiltro, String nameFiltro, String phoneFiltro) {
        this.textFiltro = textFiltro;
        this.nameFiltro = nameFiltro;
        this.phoneFiltro = phoneFiltro;
    }
    
    /**
     * Obtiene el filtro de texto.
     * @return El texto a buscar.
     */
    public String getTextFiltro() { return textFiltro; }
    
    /**
     * Obtiene el filtro de nombre.
     * @return El nombre del contacto a filtrar.
     */
    public String getNombreFiltro() { return nameFiltro; }
    
    /**
     * Obtiene el filtro de teléfono.
     * @return El número de teléfono del contacto a filtrar.
     */
    public String getTelfFiltro() { return phoneFiltro; }
    
    /**
     * Verifica si se ha especificado un filtro de texto.
     * @return true si hay un filtro de texto, false en caso contrario.
     */
    public boolean hasTextFiltro() {
        return textFiltro != null && !textFiltro.isEmpty();
    }
    
    /**
     * Verifica si se ha especificado un filtro de contacto (por nombre o teléfono).
     * @return true si hay un filtro de contacto, false en caso contrario.
     */
    public boolean hasContactFiltro() {
        return (nameFiltro != null && !nameFiltro.isEmpty()) || 
               (phoneFiltro != null && !phoneFiltro.isEmpty());
    }
}