package umu.tds.appchat.servicios.vistas;

import javax.swing.ImageIcon;

/**
 * ViewModel que encapsula la información procesada de un mensaje para la vista.
 * Aplicando el patrón DTO (Data Transfer Object) para transferir datos entre capas.
 */
public class InfoMensajeVista {
    private String textoFormateado;
    private ImageIcon emojiRedimensionado;
    private String horaFormateada;
    private boolean esEmoji;
    private boolean esMensajeEnviado;

    /**
     * Comprueba si el mensaje fue enviado por el usuario actual.
     * @return true si el mensaje fue enviado por el usuario actual, false en caso contrario.
     */
    public boolean esMensajeEnviado() {
        return esMensajeEnviado;
    }

    /**
     * Establece si el mensaje fue enviado por el usuario actual.
     * @param enviado true si el mensaje fue enviado por el usuario actual, false en caso contrario.
     */
    public void setEsMensajeEnviado(boolean enviado) {
        this.esMensajeEnviado = enviado;
    }

    /**
     * Obtiene el texto del mensaje formateado para la vista.
     * @return El texto del mensaje formateado.
     */
    public String getTextoFormateado() {
        return textoFormateado;
    }

    /**
     * Establece el texto del mensaje formateado para la vista.
     * @param texto El texto del mensaje a formatear.
     */
    public void setTextoFormateado(String texto) {
        this.textoFormateado = texto;
    }

    /**
     * Obtiene el ImageIcon del emoji redimensionado para la vista.
     * @return El ImageIcon del emoji redimensionado.
     */
    public ImageIcon getEmojiRedimensionado() {
        return emojiRedimensionado;
    }

    /**
     * Establece el ImageIcon del emoji redimensionado para la vista y actualiza el estado de si es emoji.
     * @param emoji El ImageIcon del emoji redimensionado.
     */
    public void setEmojiRedimensionado(ImageIcon emoji) {
        this.emojiRedimensionado = emoji;
        this.esEmoji = (emoji != null);
    }

    /**
     * Obtiene la hora del mensaje formateada para la vista.
     * @return La hora del mensaje formateada.
     */
    public String getHoraFormateada() {
        return horaFormateada;
    }

    /**
     * Establece la hora del mensaje formateada para la vista.
     * @param hora La hora del mensaje a formatear.
     */
    public void setHoraFormateada(String hora) {
        this.horaFormateada = hora;
    }

    /**
     * Comprueba si el mensaje es un emoji.
     * @return true si el mensaje es un emoji, false en caso contrario.
     */
    public boolean esEmoji() {
        return esEmoji;
    }
}