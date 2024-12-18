package umu.tds.persistencia;

import java.util.List;
import umu.tds.modelos.Mensaje;

public interface MensajeDAO {

	public void registrarMensaje();
	public void modificarMensaje();
	public Mensaje recuperarMensaje();
	public List<Mensaje> recuperarTodosMensajes();
	
}
