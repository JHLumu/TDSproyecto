package umu.tds.persistencia;

import umu.tds.modelos.Grupo;

public interface GrupoDAO {

	public void registrarGrupo();
	public void modificarGrupo();
	public Grupo recuperaGrupo();
	
}
