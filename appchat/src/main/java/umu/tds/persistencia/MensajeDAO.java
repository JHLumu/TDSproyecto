package umu.tds.persistencia;

import java.util.List;
import umu.tds.modelos.Mensaje;

public interface MensajeDAO {

	public void registrarMensaje(Mensaje mensaje);
	public void modificarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje();
	public List<Mensaje> recuperarTodosMensajes();
	
}
