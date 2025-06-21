package umu.tds.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TDSObservable {

	private Set<Estado> changed; // Indica si ha habido cambios
    private final Map<Estado, List<TDSObserver>> obs;

    public TDSObservable() {
        obs = new HashMap<>();
        changed = new HashSet<>();
    }
    
    public synchronized Set<Estado> getChanges(){
    	return changed;
    }
    
    public synchronized void setChanges(Set<Estado> estados){
    	this.changed = new HashSet<Estado>(estados);
    }

    // Agregar un observador
    public synchronized void addObserver(Estado e, TDSObserver o) {
    	obs.computeIfAbsent(e, k -> new ArrayList<>());
        if (!obs.get(e).contains(o)) obs.get(e).add(o);
    }

    // Eliminar un observador
    public synchronized void deleteObserver(Estado e, TDSObserver o) {
    	 List<TDSObserver> observadores = obs.get(e);
         if (observadores != null) {
             observadores.remove(o);
             if (observadores.isEmpty()) {
                 obs.remove(e);
             }
         }
    }

    // Notificar a todos los observadores
   
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

    // Marcar como cambiado
    public synchronized void setChanged(Estado e) {
        changed.add(e);
    }

    // Limpiar el estado de "cambiado"
    public synchronized void clearChanged(Estado e) {
    	changed.remove(e);
    }

    // Comprobar si ha cambiado
    public synchronized boolean hasChanged(Estado e) {
        return changed.contains(e);
    }
}
