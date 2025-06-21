package umu.tds.persistencia;
/*Esta clase implementa un pool para los adaptadores que lo necesiten*/

import java.util.Hashtable;

/**
 * <p>Esta clase implementa un patrón Pool para gestionar objetos DAO (Data Access Object).</p>
 * <p>Permite almacenar y recuperar objetos por un ID, lo que puede ser útil para adaptadores que necesiten
 * reutilizar instancias de objetos para mejorar el rendimiento.</p>
 * <p>Sigue el patrón Singleton para asegurar que solo exista una instancia del PoolDAO.</p>
 */
public class PoolDAO {
	/** Instancia única del PoolDAO. */
	private static PoolDAO unicaInstancia;
	
	/** Tabla hash que almacena los objetos del pool, usando un entero como clave. */
	private Hashtable<Integer, Object> pool;

	/**
	 * Constructor privado para evitar la instanciación directa y asegurar el patrón Singleton.
	 */
	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}

	/**
	 * Obtiene la única instancia del PoolDAO, creándola si aún no existe.
	 *
	 * @return La única instancia de {@link PoolDAO}.
	 */
	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new PoolDAO();
		return unicaInstancia;
		
	}
	
	/**
	 * Recupera un objeto del pool utilizando su ID.
	 *
	 * @param id El ID del objeto a recuperar.
	 * @return El objeto asociado al ID, o {@code null} si no se encuentra.
	 */
	public Object getObjeto(int id) {
		return pool.get(id);
	} // devuelve null si no encuentra el objeto

	/**
	 * Añade un objeto al pool con un ID específico.
	 *
	 * @param id El ID único para el objeto.
	 * @param objeto El objeto a añadir al pool.
	 */
	public void addObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	/**
	 * Verifica si el pool contiene un objeto con el ID especificado.
	 *
	 * @param id El ID a verificar.
	 * @return {@code true} si el pool contiene un objeto con el ID, {@code false} en caso contrario.
	 */
	public boolean contiene(int id) {
		return pool.containsKey(id);
	}
}