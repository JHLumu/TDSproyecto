package umu.tds.lanzador;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import umu.tds.appchat.AppChat;

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
					new URL("https://static.wikia.nocookie.net/las-aventuras-de-super-mario-bros-3/images/6/6d/Luigi.png/revision/latest?cb=20151118165038&path-prefix=es")
					);
			AppChat.getInstancia().registrarUsuario(
					"Jose Luis", 
					"Martinez", 
					"123454321", 
					LocalDate.of(2004, 7, 22),
					"josel@appchat.com",
					"1234",
					"Hola soy Jose Luis, estoy usando App Chat!", 
					new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToeoB0trn1U5LVbKvZYixTbbgs3mDn3WDRQA&s")
					);
			System.out.println("[DEBUG Lanzador registro]: Se han registrado a los integrantes del grupo.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(final String[] args){
		
		try {
			registro();
			
		} catch (Exception e) {				
			e.printStackTrace();
		}
	}
	
}
