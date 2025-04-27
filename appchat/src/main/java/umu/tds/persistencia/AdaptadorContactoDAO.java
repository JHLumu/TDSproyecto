

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
import umu.tds.modelos.Contacto.TipoContacto;

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
		System.out.println("\n[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Inicio de registro de contacto.");
		//Comprobamos que contacto no se encuentre registrado ya
		
		Entidad eContacto = null;
		try {
			System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Se comprueba si ya hay una entidad asociada.");
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		}catch(NullPointerException e) {};
		if (eContacto != null) {
			System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Contacto ya ha sido registrado anteriormente.");
			return;
			}
		System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "No tiene una entidad asociada. Se crea la entidad.");
		//Se deberian registrar sus objetos referenciados, pero se tiene en cuenta
		//que para crear un contacto ya se valida si el usuario se encuentra registrado
		
		//Se crea la entidad
		eContacto = new Entidad();
		eContacto.setNombre("Contacto");
		
		
		System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Se asocian sus propiedades comunes.");
		//Propiedades comunes
		ArrayList<Propiedad> propiedades =  new ArrayList<Propiedad>();
		System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: Nombre de contacto: " + contacto);
		propiedades.addAll(Arrays.asList(
				new Propiedad("nombre", contacto.getNombre())
				
				));
		
		//Propiedades si es ContactoIndividual
		
		if (contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL)) {
			System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Se añaden propiedades si es tipo ContactoIndividual.");
			
			ContactoIndividual aux = (ContactoIndividual) contacto;
			System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: URL de la imagen del contacto: "+ contacto.getURLImagen());
			propiedades.addAll( Arrays.asList(
					new Propiedad("usuario", String.valueOf(aux.getUsuario().getCodigo())),
					new Propiedad("imagen", contacto.getURLImagen().toExternalForm())
					));
		}
		
		//Propiedades si es Grupo
		else if (contacto.getTipoContacto().equals(TipoContacto.GRUPO)) {
			System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: " + "Se añaden propiedades si es tipo Grupo.");
			Grupo aux = (Grupo) contacto;
			propiedades.add(
					new Propiedad("miembros", obtenerIdsMiembros(aux.getMiembros()))
					);
		}
		
		//Se asocia a la entidad las propiedaddes y se registra la entidad
		eContacto.setPropiedades(propiedades);
		eContacto = servPersistencia.registrarEntidad(eContacto);
		System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: eContacto tiene id " + eContacto.getId());
		System.out.println("[DEBUG AdaptadorContactoDAO registrarContacto]: eContacto tiene usuario referencia " + servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario"));
		contacto.setCodigo(eContacto.getId());
	
		
	}

	@Override
	public void modificarContacto(Contacto contacto) {
		System.out.println("\n[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Inicio de modificación de contacto " + contacto.getCodigo());
		//Se recupera la entidad asociada al contacto
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		//Se recorren sus propiedades 
		
		for (Propiedad p : eContacto.getPropiedades()) {
			String pNombre = p.getNombre();
			
			if (pNombre.equals("nombre")) {
				p.setValor(contacto.getNombre());
				System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se modifica el nombre de contacto.");
			}
			
			else if (pNombre.equals("imagen")) {
				p.setValor(String.valueOf(contacto.getURLImagen()));
				System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se modifica la imagen de contacto");
			}
			
			//REVISAR: Eliminar si no se implementa la funcionalidad de cambiar el telefono
			else if (pNombre.equals("usuario")) {
				p.setValor(String.valueOf(((ContactoIndividual) contacto).getUsuario().getCodigo()));
				System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se modifica el Usuario asociado a contacto");
			}
			
			else if (pNombre.equals("miembros")) {
				p.setValor(obtenerIdsMiembros(((Grupo) contacto).getMiembros()));
				System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se modifica la lista de miembros del grupo.");
			} 
			
			else if (pNombre.equals("imagen")) {
				p.setValor(contacto.getURLImagen().toExternalForm());
				System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se modifica la lista de miembros del grupo.");
			}
			
			servPersistencia.modificarPropiedad(p);
			System.out.println("[DEBUG AdaptadorContactoDAO modificarContacto]: " + "Se guardan los cambios de contacto.");
		}
		
		
	}

	@Override
	public Contacto recuperarContacto(int id) throws NumberFormatException, MalformedURLException {
		System.out.println("\n[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "Inicio de recuperar contacto.");
		//Si el objeto se encuentra en el pool, se retorna
		
		PoolDAO poolContacto = PoolDAO.getUnicaInstancia();
		if (poolContacto.contiene(id)) {
			System.out.println("[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "Contacto se encuentra en el Pool. Se devuelve contacto");
			return (Contacto) poolContacto.getObjeto(id);
			
		}
		
		//Si no esta en el pool, se recupera la entidad y aquellas propiedades de campos primitivos
		Entidad eContacto = servPersistencia.recuperarEntidad(id);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");	
		String imagenString = servPersistencia.recuperarPropiedadEntidad(eContacto, "imagen");
		URL imagen = null;
		if (imagenString != null) imagen = new URL(imagenString); 
		
			
		
		System.out.println("[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "Se recupera la entidad con sus propiedades primitivas.");
		
		//Se guarda en el pool antes de recuperar los objetos referenciados
		
		//Para poder crear el objeto Contacto necesitamos saber de que tipo es
		String idUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");
		System.out.println("[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "idUsuario igual a " + idUsuario);
		
		Contacto contacto = null;
		
		//Si tiene asignado un Usuario, es Contacto Individual; si no, es Grupo
		if ((idUsuario!=null)) {
			
			System.out.println("[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "Se recupera el usuario asociado a Contacto si es tipo ContactoIndividual.");
			Usuario usuario = FactoriaDAO.getFactoriaDAO().getUsuarioDAO().recuperarUsuario(Integer.parseInt(idUsuario));
			contacto = new ContactoIndividual(nombre, usuario);
			
			
		}
		
		//Si no tiene asignado un Usuario, es un Grupo. Recuperamos los miembros del grupo
		else {
			
			System.out.println("[DEBUG AdaptadorContactoDAO recuperarContacto]: " + "Se recupera la lista de miembros si es tipo Grupo.");
			String idMiembros = servPersistencia.recuperarPropiedadEntidad(eContacto, "miembros");
			contacto = new Grupo(nombre, imagen, obtenerMiembrosporIds(idMiembros));
			
		}
		contacto.setCodigo(id);
		poolContacto.addObjeto(id, contacto);
		return contacto;
		
	}
	
	
	//Funciones auxiliares
	
	private String obtenerIdsMiembros(ContactoIndividual[] grupo) {
		String aux = Stream.of(grupo)
				.map(c -> String.valueOf(c.getCodigo()))
				.collect(Collectors.joining(" "));
		System.out.println("\n[DEBUG AdaptadorContactoDAO obtenerIdsMiembros]: Resultado:" + aux);
		return aux;

	}
	
	private ContactoIndividual[] obtenerMiembrosporIds(String idsMiembros) throws NumberFormatException, MalformedURLException{
		List<ContactoIndividual> resultado = new LinkedList<ContactoIndividual>();
		if (idsMiembros == null) return resultado.toArray(new ContactoIndividual[0]);
		for (String idMiembro: idsMiembros.split(" ")) {
		resultado.add((ContactoIndividual) FactoriaDAO.getFactoriaDAO().getContactoDAO().recuperarContacto(Integer.valueOf(idMiembro)));
		}
		return resultado.toArray(new ContactoIndividual[0]);
	}
	

}