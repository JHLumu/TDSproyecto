package umu.tds.lanzador;

import java.awt.EventQueue;

import javax.swing.UIManager;

import umu.tds.ventanas.Login;

public class Lanzador {

	public static void main(final String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				    Login window = new Login();
					window.setVisible(true);
					window.setLocationRelativeTo(null);
				} catch (Exception e) {				
					e.printStackTrace();
				}
			}
		});
	}
	
}
