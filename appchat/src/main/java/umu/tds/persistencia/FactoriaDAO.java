package umu.tds.persistencia;


public abstract class FactoriaDAO {

	protected static FactoriaDAO instancia = null;
	
	protected FactoriaDAO() {};
	
	public static FactoriaDAO getFactoriaDAO() {
		return instancia;
	}
	
	public static FactoriaDAO getInstancia(String nombre)  {
		 if (instancia == null)
			try {
				instancia = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 return instancia ;
		 }
	
	public abstract UsuarioDAO getUsuarioDAO();
	public abstract MensajeDAO getMensajeDAO();
	public abstract ContactoDAO getContactoDAO();
	
}
