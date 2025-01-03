package umu.tds.persistencia;

import java.net.MalformedURLException;

import umu.tds.modelos.Contacto;


public interface ContactoDAO {

	public void registrarContacto(Contacto contacto);
	public void modificarContacto(Contacto contacto);
	public Contacto recuperarContacto(int id) throws NumberFormatException, MalformedURLException;
	
}
