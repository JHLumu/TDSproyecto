package umu.tds.appchat;

import java.util.Arrays;
import java.util.List;

import umu.tds.modelos.Mensaje;

public class AppChat {

	public static List <Mensaje> obtenerListaMensajesRecientesPorUsuario(){
		Mensaje[] values = new Mensaje [] {
				 new Mensaje("Jose", "Hola"),
			     new Mensaje("Ana", "Qu� tal"),
			     new Mensaje("Maria", "Adi�s")
				
		};
		return Arrays.asList(values);
	}
}
