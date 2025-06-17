package umu.tds.appchat.servicios.mensajes;

/**
 * Clase para encapsular los criterios de búsqueda de mensajes
 */
public class CriterioBuscarMensaje {
    private final String textFiltro;
    private final String nameFiltro;
    private final String phoneFiltro;
    
    public CriterioBuscarMensaje(String textFiltro, String nameFiltro, String phoneFiltro) {
        this.textFiltro = textFiltro;
        this.nameFiltro = nameFiltro;
        this.phoneFiltro = phoneFiltro;
    }
    
    public String getTextFiltro() { return textFiltro; }
    public String getNombreFiltro() { return nameFiltro; }
    public String getTelfFiltro() { return phoneFiltro; }
    
    public boolean hasTextFiltro() {
        return textFiltro != null && !textFiltro.isEmpty();
    }
    
    public boolean hasContactFiltro() {
        return (nameFiltro != null && !nameFiltro.isEmpty()) || 
               (phoneFiltro != null && !phoneFiltro.isEmpty());
    }
}