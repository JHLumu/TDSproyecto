

package umu.tds.persistencia;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Usuario;

public class AdaptadorContactoDAO implements ContactoDAO {
	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoDAO instancia = new AdaptadorContactoDAO();
	
	public static AdaptadorContactoDAO getContactoIndividualDAO() {
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
		}catch(NullPointerException e) {};
		if (eContacto != null) return;
		
		//Se deberian registrar sus objetos referenciados, pero se tiene en cuenta
		//que para crear un contacto ya se valida si el usuario se encuentra registrado
		
		//Se crea la entidad
		eContacto = new Entidad();
		eContacto.setNombre("Contacto");
		
		//Propiedades comunes
		ArrayList<Propiedad> propiedades =  new ArrayList<Propiedad>();
		propiedades.addAll(Arrays.asList(
				new Propiedad("nombre", contacto.getNombre()),
				new Propiedad("imagen", String.valueOf(contacto.getImagen()))
				));
		
		//Propiedades si es ContactoIndividual
		if (contacto.getTipoContacto().equals("Individual")) {
			ContactoIndividual aux = (ContactoIndividual) contacto;
			propiedades.add(
					new Propiedad("Usuario", String.valueOf(aux.getUsuario().getCodigo()))
					);
		}
		
		//Propiedades si es Grupo
		else if (contacto.getTipoContacto().equals("Grupo")) {
			Grupo aux = (Grupo) contacto;
			propiedades.add(
					new Propiedad("miembros", obtenerIdsMiembros(aux.getMiembros()))
					);
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
			
			else if (pNombre.equals("imagen")) p.setValor(String.valueOf(contacto.getImagen()));
			
			else if (pNombre.equals("Usuario")) p.setValor(String.valueOf(((ContactoIndividual) contacto).getUsuario().getCodigo()));
			
			else if (pNombre.equals("miembros")) p.setValor(obtenerIdsMiembros(((Grupo) contacto).getMiembros()));
			
			servPersistencia.modificarPropiedad(p);
		}
		
		
	}

	@Override
	public Contacto recuperarContacto(int id) {
		
		//Si el objeto se encuentra en el pool, se retorna
		PoolDAO poolContacto = PoolDAO.getUnicaInstancia();
		if (poolContacto.contiene(id)) return (Contacto) poolContacto.getObjeto(id);
		
		//Si no esta en el pool, se recupera la entidad y aquellas propiedades de campos primitivos
		Entidad eContacto = servPersistencia.recuperarEntidad(id);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");	
		String imagenString = servPersistencia.recuperarPropiedadEntidad(eContacto, "imagen");
		URL imagen = null;
		if (imagenString != null) {
			try {
				imagen = new URL(imagenString);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		//Para poder crear el objeto Contacto necesitamos saber de que tipo es

		Contacto contacto = null;
		
		String idUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "Usuario");
		
		//Si tiene asignado un Usuario, es Contacto Individual; si no, es Grupo
		if ((idUsuario!=null) && (!idUsuario.isEmpty())) {
			
			Usuario usuario = FactoriaDAO.getFactoriaDAO().getUsuarioDAO().recuperarUsuario(Integer.parseInt(idUsuario));
			contacto = new ContactoIndividual(nombre,usuario);
			
		}
		
		//Si no tiene asignado un Usuario, es un Grupo. Recuperamos los miembros del grupo
		else {
			String idMiembros = servPersistencia.recuperarPropiedadEntidad(eContacto, "miembros");
			contacto = new Grupo(nombre, obtenerMiembrosporIds(idMiembros));
		}
		
		poolContacto.addObjeto(id, contacto);
		return contacto;
		
	}
	
	
	//Funciones auxiliares
	
	private String obtenerIdsMiembros(ContactoIndividual[] grupo) {
		String aux = Stream.of(grupo)
				.map(c -> c.getNombre())
				.collect(Collectors.joining(" "));
		return aux;

	}
	
	private ContactoIndividual[] obtenerMiembrosporIds(String idsMiembros){
		List<ContactoIndividual> resultado = new LinkedList<ContactoIndividual>();
		for (String idMiembro: idsMiembros.split(" ")) {
		resultado.add((ContactoIndividual) FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.valueOf(idMiembro)));
		}
		return resultado.toArray(new ContactoIndividual[0]);
	}
	

}