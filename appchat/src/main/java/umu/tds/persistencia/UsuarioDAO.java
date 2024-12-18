package umu.tds.persistencia;

import umu.tds.modelos.Usuario;

public interface UsuarioDAO {

	public void registrarUsuario(Usuario usuario);
	public void modificarUsuario();
	public Usuario recuperarUsuario();
	
}
