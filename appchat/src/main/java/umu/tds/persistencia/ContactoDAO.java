package umu.tds.persistencia;

import umu.tds.modelos.Contacto;


public interface ContactoDAO {

	public void registrarContacto(Contacto contacto);
	public void modificarContacto(Contacto contacto);
	public Contacto recuperarContacto(int id);
	
}
