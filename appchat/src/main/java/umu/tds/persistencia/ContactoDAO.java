package umu.tds.persistencia;

import java.util.List;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;

public interface ContactoDAO {

	public void registrarContacto(Contacto contacto);
	public void modificarContacto(Contacto contacto);
	public Contacto recuperarContacto(Contacto contacto);
	public List<Contacto> recuperarTodosContactos();
	
}
