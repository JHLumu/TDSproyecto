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
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "Contraseña Usuario:" + usuario.getPassword());
				System.out.println("[DEBUG CatalogoUsuarios cargarCatalogo]: " + "Telefono Usuario:" + usuario.getTelefono());
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
	
	public List<Usuario> usuarioEnListaContacto(Usuario u) {
	    // Validar si el usuario no es nulo
	    if (u == null) {
	        System.err.println("[ERROR] El usuario proporcionado es nulo.");
	        return null;  // Retorna una lista vacía si el usuario es nulo
	    }

	    System.out.println("[DEBUG] Buscando usuarios que tienen a " + u.getNombre() + " como contacto.");
	    System.out.println("[DEBUG] Total de usuarios registrados: " + this.usuarios.size());

	    // Filtrar usuarios que tienen al usuario 'u' en su lista de contactos
	    List<Usuario> usuariosQueTienenAlActual = this.usuarios.values().stream()
	        .filter(usuario -> {
	            boolean contiene = usuario.getListaContacto().stream()
	                .anyMatch(c -> {
	                    if (c instanceof ContactoIndividual) {
	                        ContactoIndividual cont = (ContactoIndividual) c;
	                        boolean equals = cont.getUsuario().equals(u);
	                        if (equals) {
	                            System.out.println("[DEBUG] El usuario " + usuario.getNombre() + " tiene a " + u.getNombre() + " en su lista de contactos.");
	                        }
	                        return equals;
	                    }
	                    return false;  // Si no es ContactoIndividual, no lo considera
	                });
	            return contiene;
	        })
	        .collect(Collectors.toList());

	    // Verificar el resultado del filtro
	    System.out.println("[DEBUG] Total de usuarios que contienen a " + u.getNombre() + " en su lista de contactos: " + usuariosQueTienenAlActual.size());

	    // Opcional: Mostrar los usuarios encontrados
	    usuariosQueTienenAlActual.forEach(usuario -> 
	        System.out.println("[DEBUG] Usuario encontrado: " + usuario.getNombre() + " - Teléfono: " + usuario.getTelefono())
	    );

	    return usuariosQueTienenAlActual;
	}


	
	
}
