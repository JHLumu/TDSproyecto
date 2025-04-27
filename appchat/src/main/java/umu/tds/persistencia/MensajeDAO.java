package umu.tds.persistencia;

import java.net.MalformedURLException;
import umu.tds.modelos.Mensaje;

public interface MensajeDAO {

	public void registrarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje(int id) throws NumberFormatException, MalformedURLException;
	
}
