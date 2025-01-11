package umu.tds.modelos;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.persistencia.FactoriaDAO;
import umu.tds.persistencia.FactoriaDAOTDS;
import umu.tds.persistencia.UsuarioDAO;

public class CatalogoUsuarios {

	//Instancia unica
	private static CatalogoUsuarios catalogo = new CatalogoUsuarios();

	//Mapa que guarda para cada telefono la instancia usuario
	private HashMap<String,Usuario> usuarios;
	
	//Atributos
	private FactoriaDAO factoria;
	private UsuarioDAO usuarioDAO;
	
	private CatalogoUsuarios() {
		this.usuarios = new HashMap<String,Usuario>();
		factoria = FactoriaDAO.getInstancia(FactoriaDAOTDS.class.getName());
		usuarioDAO = factoria.getUsuarioDAO();
		cargarCatalogo();
	}
	
	public static CatalogoUsuarios getInstancia() {
		
		return catalogo;
		
	}
	
	
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono);
	}
	
	public void nuevoUsuario(Usuario usuario) {
		usuarios.put(usuario.getTelefono(), usuario);
	}
	
	public boolean estaUsuarioRegistrado(String telefono) {
		return (getUsuario(telefono) != null);
	}
	
	public boolean sonCredencialesCorrectas(String telefono, String password) {
		if (!estaUsuarioRegistrado(telefono)) return false;
		return (usuarios.get(telefono).getPassword().equals(password));
	}
	
	private void cargarCatalogo() {
		
		List<Usuario> aux;
		try {
			aux = usuarioDAO.recuperarTodosUsuarios();
			System.out.println("\n");
			for (Usuario usuario : aux) {
				usuarios.put(usuario.getTelefono(), usuario);
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "ID Usuario:" + usuario.getCodigo());
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "Nombre Usuario:" + usuario.getNombre());
				List<String> lista = usuario.getListaContacto().stream()
						.map(c -> c.getNombre())
						.collect(Collectors.toList());
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "Lista de Contacto Usuario:" + lista);
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "Imagen Usuario:" + usuario.getImagenPerfil().toExternalForm());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
