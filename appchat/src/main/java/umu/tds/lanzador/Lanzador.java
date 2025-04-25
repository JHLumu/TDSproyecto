package umu.tds.lanzador;

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import javax.swing.UIManager;

import umu.tds.appchat.AppChat;
import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.ventanas.Login;

public class Lanzador {
	
	
	public static void registro() {
		try {
			AppChat.getInstancia().registrarUsuario(
					"Jiahui", 
					"Lin", 
					"123456789", 
					LocalDate.of(2004, 7, 22),
					"jiahui@appchat.com",
					"1234",
					"Hola soy Jiahui, estoy usando App Chat!", 
					new URL("https://i.pinimg.com/736x/fe/4c/7c/fe4c7cbb4be3bc37fa46f2255581d5ee.jpg")
					);
			
			AppChat.getInstancia().registrarUsuario(
					"Jesús", 
					"Sánchez", 
					"987654321", 
					LocalDate.of(2004, 8, 5),
					"jesus@appchat.com",
					"4321",
					"Hola soy Jesús, estoy usando App Chat!", 
					new URL("https://e7.pngegg.com/pngimages/1013/301/png-clipart-luigi-illustration-super-mario-bros-new-super-mario-bros-luigi-luigi-super-mario-bros-hand-thumbnail.png")
					);
			System.out.println("[DEBUG Lanzador registro]: Se han registrado a los integrantes del grupo.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(final String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (CatalogoUsuarios.getInstancia().getUsuario("123456789")==null) registro();
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
