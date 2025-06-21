package umu.tds.utils;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;

/**
 * Interfaz para definir un listener que será notificado cuando se realice una acción de búsqueda o filtro.
 * Proporciona un callback con los resultados de la acción.
 */
public interface BuscarFiltroListener {
	/**
	 * Método llamado cuando se realiza una acción de búsqueda o filtro.
	 * @param contacto El {@link Contacto} relacionado con la acción (puede ser null si la acción no se aplica a un contacto específico).
	 * @param mensaje El {@link Mensaje} relacionado con la acción (puede ser null si la acción no se aplica a un mensaje específico).
	 */
	void onAccionRealizada(Contacto contacto, Mensaje mensaje);
}