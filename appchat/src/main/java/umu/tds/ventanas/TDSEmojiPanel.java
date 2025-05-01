package umu.tds.ventanas;

import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import tds.BubbleText;

public class TDSEmojiPanel extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int selectedEmojiId = -1;

    
    public TDSEmojiPanel() {
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
                selectedEmojiId = emojiId; // ← Guardamos el ID

                Window window = SwingUtilities.getWindowAncestor(TDSEmojiPanel.this);
                if (window != null) {
                    window.dispose(); // Cerramos el diálogo
                }
            });
            
            add(emojiButton);
        }
    }
    
    public int getSelectedEmojiId() {
        return selectedEmojiId;
    }
}
