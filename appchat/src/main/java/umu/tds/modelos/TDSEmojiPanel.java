package umu.tds.modelos;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import tds.BubbleText;
import umu.tds.appchat.AppChat;

public class TDSEmojiPanel extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel parentChat;
    
    public TDSEmojiPanel(JPanel chatPanel) {
        this.parentChat = chatPanel;
        setLayout(new GridLayout(4, 6, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear las previews
        for (int i = 0; i < 24; i++) {
            final int emojiId = i;
            JButton emojiButton = new JButton();
            

            ImageIcon preview = BubbleText.getEmoji(i);
            emojiButton.setIcon(preview);

            // añadir el elejido al padre
            emojiButton.addActionListener(e -> {
                BubbleText burbuja = new BubbleText(
                    parentChat, 
                    emojiId, 
                    Color.WHITE, 
                    AppChat.getInstancia().getNombreUsuario(), 
                    BubbleText.SENT, 
                    14
                );
                parentChat.add(burbuja);
                parentChat.revalidate();
                parentChat.repaint();
                
                Window window = SwingUtilities.getWindowAncestor(TDSEmojiPanel.this);
                window.dispose();
            });
            
            add(emojiButton);
        }
    }
}
