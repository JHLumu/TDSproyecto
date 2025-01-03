package umu.tds.persistencia;

import java.net.MalformedURLException;
import java.util.List;

import umu.tds.modelos.Usuario;

public interface UsuarioDAO {

	public void registrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int id) throws MalformedURLException;
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException;
}
