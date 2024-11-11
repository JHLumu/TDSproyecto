package umu.tds.appchat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

public class AppChat {

	public static List <Mensaje> obtenerListaMensajesRecientesPorUsuario(){
		
		Usuario ana = new Usuario("Ana", "", "", LocalDate.now(),"", "");
		Usuario jose = new Usuario("Jose", "", "", LocalDate.now(),"", "");
		Usuario maria = new Usuario("Maria", "", "", LocalDate.now(),"", "");
		Mensaje[] values = new Mensaje [] {
				 new Mensaje(ana,jose, "Hola"),
			     new Mensaje(jose,ana, "Qu� tal"),
			     new Mensaje(ana,maria, "Adi�s")
				
		};
		return Arrays.asList(values);
	}
}
