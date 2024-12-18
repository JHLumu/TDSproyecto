package umu.tds.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Grupo;

public class AdaptadorGrupoDAOTDS implements GrupoDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoDAOTDS instancia = new AdaptadorGrupoDAOTDS();
	
	public static AdaptadorGrupoDAOTDS getGrupoDAO() {
		return instancia;
	}
	
	private AdaptadorGrupoDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	
	@Override
	public void registrarGrupo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificarGrupo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Grupo recuperaGrupo() {
		// TODO Auto-generated method stub
		return null;
	}

}
