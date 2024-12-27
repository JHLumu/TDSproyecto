

package umu.tds.persistencia;

import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;

public class AdaptadorContactoDAO implements ContactoDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoDAO instancia = new AdaptadorContactoDAO();
	
	public static AdaptadorContactoDAO getContactoIndividualDAO() {
		return instancia;
	}
	
	private AdaptadorContactoDAO(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	@Override
	public void registrarContacto(Contacto contacto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificarContacto(Contacto contacto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContactoIndividual recuperarContacto(Contacto contacto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contacto> recuperarTodosContactos() {
		// TODO Auto-generated method stub
		return null;
	}

}