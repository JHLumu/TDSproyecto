package umu.tds.persistencia;

import java.util.List;

import beans.Entidad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Mensaje;

public class AdaptadorMensajeDAOTDS implements MensajeDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeDAOTDS instancia = new AdaptadorMensajeDAOTDS();
	
	public static AdaptadorMensajeDAOTDS getMensajeDAO() {
		return instancia;
	}
	
	private AdaptadorMensajeDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	@Override
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;
		try {
			
			eMensaje = servPersistencia.recuperarEntidad(null);
			
		}catch(NullPointerException e) {}
		
	}

	@Override
	public void modificarMensaje(Mensaje mensaje) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mensaje recuperarMensaje() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Mensaje> recuperarTodosMensajes() {
		// TODO Auto-generated method stub
		return null;
	}

}
