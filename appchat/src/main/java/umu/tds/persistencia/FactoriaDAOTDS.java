package umu.tds.persistencia;

public class FactoriaDAOTDS extends FactoriaDAO {

	protected FactoriaDAOTDS() {};
	
	
	public static FactoriaDAO getFactoriaDAO() {
		if (instancia == null) instancia = new FactoriaDAOTDS();
		return instancia;
	}
	
	
	
	
	@Override
	public GrupoDAO getGrupoDAO() {
		return AdaptadorGrupoDAOTDS.getGrupoDAO();
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAOTDS.getUsuarioDAO();
	}

	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAOTDS.getMensajeDAO();
	}

	@Override
	public ContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividualDAOTDS.getContactoIndividualDAO();
	}

	
}
