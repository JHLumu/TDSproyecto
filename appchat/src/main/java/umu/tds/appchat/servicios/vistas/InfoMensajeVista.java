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

    // Getters y setters
    public boolean esMensajeEnviado() { 
        return esMensajeEnviado; 
    }
    
    public void setEsMensajeEnviado(boolean enviado) { 
        this.esMensajeEnviado = enviado; 
    }
    
    public String getTextoFormateado() { 
        return textoFormateado; 
    }
    
    public void setTextoFormateado(String texto) { 
        this.textoFormateado = texto; 
    }
    
    public ImageIcon getEmojiRedimensionado() { 
        return emojiRedimensionado; 
    }
    
    public void setEmojiRedimensionado(ImageIcon emoji) { 
        this.emojiRedimensionado = emoji;
        this.esEmoji = (emoji != null);
    }
    
    public String getHoraFormateada() { 
        return horaFormateada; 
    }
    
    public void setHoraFormateada(String hora) { 
        this.horaFormateada = hora; 
    }
    
    public boolean esEmoji() { 
        return esEmoji; 
    }
}