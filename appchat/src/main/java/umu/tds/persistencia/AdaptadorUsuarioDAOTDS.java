package umu.tds.persistencia;

import beans.Entidad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Usuario;

public class AdaptadorUsuarioDAOTDS implements UsuarioDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioDAOTDS instancia = new AdaptadorUsuarioDAOTDS();
	
	public static AdaptadorUsuarioDAOTDS getUsuarioDAO() {
		return instancia;
	}
	
	private AdaptadorUsuarioDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	
	@Override
	public void registrarUsuario(Usuario usuario) {
		/*
		 * 
		//Comprobacion de que usuario ya no tenga asociado una entidad
		Entidad eUsuario = null;
		try {
			eUsuario=servPersistencia.recuperarEntidad(usuario.getID());
		}catch(NullPointerException e) {}
		if (eUsuario == null) return;
		
		//Registramos sus objetos
		
		//tengo dudas sobre como se implementa la lista de mensajes
		
		*/
		
		
		
	}

	@Override
	public void modificarUsuario() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario recuperarUsuario() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
