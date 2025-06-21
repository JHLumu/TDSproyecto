

package umu.tds.persistencia;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Usuario;
import umu.tds.modelos.Contacto.TipoContacto;

	public class AdaptadorContactoDAO implements ContactoDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoDAO instancia = new AdaptadorContactoDAO();
	
	public static AdaptadorContactoDAO getContactoDAO() {
		return instancia;
	}
	
	private AdaptadorContactoDAO(){
		
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
	};
	
	
	@Override
	public void registrarContacto(Contacto contacto) {

		//Comprobamos que contacto no se encuentre registrado ya		
		Entidad eContacto = null;
		try {
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		}catch(NullPointerException e) {e.printStackTrace();};
		if (eContacto != null) {
			return;
			}
		//Se deberian registrar sus objetos referenciados, pero se tiene en cuenta
		//que para crear un contacto ya se valida si el usuario se encuentra registrado
		
		//Se crea la entidad
		eContacto = new Entidad();
		eContacto.setNombre("Contacto");

		//Propiedades comunes
		ArrayList<Propiedad> propiedades =  new ArrayList<Propiedad>();
		propiedades.add(new Propiedad("nombre", contacto.getNombre()));
		
		//Propiedades si es ContactoIndividual
		
		if (contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) {
			ContactoIndividual aux = (ContactoIndividual) contacto;
			propiedades.addAll(Arrays.asList(
					new Propiedad("usuario", String.valueOf(aux.getUsuario().getCodigo())),
					new Propiedad("tipo", "INDIVIDUAL")
					));
		}
		
		//Propiedades si es Grupo
		else if (contacto.getTipoContacto().equals(TipoContacto.GRUPO)) {
			Grupo aux = (Grupo) contacto;
			URL imagenURL = contacto.getURLImagen();
			if (imagenURL != null) propiedades.add(new Propiedad("imagen", imagenURL.toExternalForm()));
			propiedades.addAll( Arrays.asList(
					new Propiedad("miembros", obtenerIdsMiembros(aux.getMiembros())),
					new Propiedad("anfitrion", aux.getAnfitrion()),
					new Propiedad("tipo", "GRUPO")
					));
		}
		
		//Se asocia a la entidad las propiedaddes y se registra la entidad
		eContacto.setPropiedades(propiedades);
		eContacto = servPersistencia.registrarEntidad(eContacto);
		contacto.setCodigo(eContacto.getId());



	}

	@Override
	public void modificarContacto(Contacto contacto) {		
		//Se recupera la entidad asociada al contacto
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		//Se recorren sus propiedades 
		
		for (Propiedad p : eContacto.getPropiedades()) {
			
			String pNombre = p.getNombre();
			
			if (pNombre.equals("nombre")) p.setValor(contacto.getNombre());
			
			else if (pNombre.equals("imagen")) p.setValor(String.valueOf(contacto.getURLImagen()));
			
			else if (pNombre.equals("usuario")) p.setValor(String.valueOf(((ContactoIndividual) contacto).getUsuario().getCodigo()));
			
			else if (pNombre.equals("miembros")) p.setValor(obtenerIdsMiembros(((Grupo) contacto).getMiembros())); 
			
			servPersistencia.modificarPropiedad(p);
	
		}	
		
	}

	@Override
	public Contacto recuperarContacto(int id) throws NumberFormatException, MalformedURLException {
		//Si el objeto se encuentra en el pool, se retorna
		
		PoolDAO poolContacto = PoolDAO.getUnicaInstancia();
		if (poolContacto.contiene(id)) return (Contacto) poolContacto.getObjeto(id);
		
		//Si no esta en el pool, se recupera la entidad y aquellas propiedades de campos primitivos
		Entidad eContacto = servPersistencia.recuperarEntidad(id);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");	
		String tipo = servPersistencia.recuperarPropiedadEntidad(eContacto, "tipo");
		
		//Se guarda en el pool antes de recuperar los objetos referenciados
		
		//Para poder crear el objeto Contacto necesitamos saber de que tipo es
		String idUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");
		
		Contacto contacto = null;
		
		//Si tiene asignado un Usuario, es Contacto Individual; si no, es Grupo
		if ("INDIVIDUAL".equals(tipo)) {
			Usuario usuario = FactoriaDAO.getFactoriaDAO().getUsuarioDAO().recuperarUsuario(Integer.parseInt(idUsuario));
			contacto = new ContactoIndividual(nombre, usuario);
		}
		
		//Si no tiene asignado un Usuario, es un Grupo. Recuperamos los miembros del grupo
		else {
			String imagenString = servPersistencia.recuperarPropiedadEntidad(eContacto, "imagen");
			URL imagen = null;
			if (imagenString != null) imagen = new URL(imagenString); 
			String idMiembros = servPersistencia.recuperarPropiedadEntidad(eContacto, "miembros");
			String anfitrion = servPersistencia.recuperarPropiedadEntidad(eContacto, "anfitrion");
			contacto = new Grupo(nombre, imagen, anfitrion, obtenerMiembrosporIds(idMiembros));
			
		}
		contacto.setCodigo(id);
		poolContacto.addObjeto(id, contacto);
		return contacto;
		
	}
	
	
	//Funciones auxiliares
	
	private String obtenerIdsMiembros(List<Contacto> lista) {
		
		String aux = lista.stream()
				.map(c -> String.valueOf(c.getCodigo()))
				.collect(Collectors.joining(" "));
		return aux;

	}
	
	private List<Contacto> obtenerMiembrosporIds(String idsMiembros) throws NumberFormatException, MalformedURLException{
		List<Contacto> resultado = new LinkedList<Contacto>();
		if (idsMiembros != null && !idsMiembros.isEmpty()) {
			
			for (String idContacto: idsMiembros.split(" ")) {
				Contacto contacto = (FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.valueOf(idContacto)));
				resultado.add(contacto);
			}
		
		}
		return resultado;
	}
	

}