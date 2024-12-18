
package umu.tds.persistencia;

import java.util.List;
import umu.tds.modelos.ContactoIndividual;

public interface ContactoIndividualDAO {
	public void registrarContacto();
	public void modificarContacto();
	public ContactoIndividual recuperarContacto();
	public List<ContactoIndividual> recuperarTodosContactos();
}
