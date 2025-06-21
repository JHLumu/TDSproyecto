package umu.tds.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Clase observable que permite a otros objetos (observadores) ser notificados de cambios
 * en su estado, específicamente cuando ciertos eventos definidos por la enumeración {@link Estado} ocurren.
 * Implementa el patrón Observer.
 */
public class TDSObservable {

	private Set<Estado> changed; // Indica si ha habido cambios
    private final Map<Estado, List<TDSObserver>> obs;

    /**
     * Constructor para TDSObservable. Inicializa la estructura de datos para los observadores
     * y el conjunto de estados que han cambiado.
     */
    public TDSObservable() {
        obs = new HashMap<>();
        changed = new HashSet<>();
    }
    
    /**
     * Obtiene el conjunto de estados que han sido marcados como "cambiados".
     * @return Un {@code Set} de {@link Estado} que indica los cambios pendientes.
     */
    public synchronized Set<Estado> getChanges(){
    	return changed;
    }
    
    /**
     * Establece el conjunto de estados que han sido marcados como "cambiados".
     * @param estados Un {@code Set} de {@link Estado} con los nuevos estados cambiados.
     */
    public synchronized void setChanges(Set<Estado> estados){
    	this.changed = new HashSet<Estado>(estados);
    }

    /**
     * Agrega un observador para un estado específico. Si el observador ya está registrado para ese estado, no se añade de nuevo.
     * @param e El {@link Estado} para el cual el observador desea ser notificado.
     * @param o El {@link TDSObserver} a agregar.
     */
    public synchronized void addObserver(Estado e, TDSObserver o) {
    	obs.computeIfAbsent(e, k -> new ArrayList<>());
        if (!obs.get(e).contains(o)) obs.get(e).add(o);
    }

    /**
     * Elimina un observador de un estado específico. Si el observador es el último para ese estado,
     * la entrada del estado se elimina del mapa.
     * @param e El {@link Estado} del cual se desea eliminar el observador.
     * @param o El {@link TDSObserver} a eliminar.
     */
    public synchronized void deleteObserver(Estado e, TDSObserver o) {
    	 List<TDSObserver> observadores = obs.get(e);
         if (observadores != null) {
             observadores.remove(o);
             if (observadores.isEmpty()) {
                 obs.remove(e);
             }
         }
    }

    /**
     * Notifica a todos los observadores registrados para un estado específico.
     * Si no hay cambios para ese estado o no hay observadores, no se realiza ninguna acción.
     * Después de la notificación, el estado se marca como no cambiado.
     * @param e El {@link Estado} por el cual se va a notificar a los observadores.
     */
    public void notifyObservers(Estado e) {
    	List<TDSObserver> observadoresCopy;

        synchronized (this) {
            if (!hasChanged(e)) return; // Si no hay cambios, no notificar

            List<TDSObserver> observadores = obs.get(e);
            if (observadores == null || observadores.isEmpty()) {
                clearChanged(e); // Limpia el estado de "cambiado" si no hay observadores
                return;
            }

            // Crear copias para evitar ConcurrentModificationException y minimizar el tiempo de bloqueo
            observadoresCopy = new ArrayList<>(observadores);

            clearChanged(e); // Limpia el estado de "cambiado"
        }

        // Notificar a los observadores fuera del bloque sincronizado para evitar bloqueos prolongados
        for (TDSObserver observer : observadoresCopy) {
            try {
                observer.update(this, e);
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    /**
     * Marca un estado específico como "cambiado", indicando que hay una actualización pendiente
     * para los observadores de ese estado.
     * @param e El {@link Estado} a marcar como cambiado.
     */
    public synchronized void setChanged(Estado e) {
        changed.add(e);
    }

    /**
     * Limpia el estado de "cambiado" para un estado específico, indicando que ya no hay actualizaciones
     * pendientes para los observadores de ese estado.
     * @param e El {@link Estado} a limpiar.
     */
    public synchronized void clearChanged(Estado e) {
    	changed.remove(e);
    }

    /**
     * Comprueba si un estado específico ha sido marcado como "cambiado".
     * @param e El {@link Estado} a comprobar.
     * @return true si el estado ha cambiado, false en caso contrario.
     */
    public synchronized boolean hasChanged(Estado e) {
        return changed.contains(e);
    }
}