package umu.tds.persistencia;

import java.util.List;

import umu.tds.modelos.Usuario;

public interface UsuarioDAO {

	public void registrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int id);
	public List<Usuario> recuperarTodosUsuarios();
}
