package umu.tds.persistencia;

import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.ContactoIndividual;

public class AdaptadorContactoIndividualDAOTDS implements ContactoIndividualDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividualDAOTDS instancia = new AdaptadorContactoIndividualDAOTDS();
	
	public static AdaptadorContactoIndividualDAOTDS getContactoIndividualDAO() {
		return instancia;
	}
	
	private AdaptadorContactoIndividualDAOTDS(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	@Override
	public void registrarContacto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificarContacto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContactoIndividual recuperarContacto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactoIndividual> recuperarTodosContactos() {
		// TODO Auto-generated method stub
		return null;
	}

}
