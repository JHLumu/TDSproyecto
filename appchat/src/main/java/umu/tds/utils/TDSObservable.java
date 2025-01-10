package umu.tds.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import umu.tds.modelos.Usuario;

public class TDSObservable {

	private final Map<Usuario, Set<Estado>> changed; // Indica si ha habido cambios
    private final Map<Usuario, List<TDSObserver>> obs;

    public TDSObservable() {
        obs = new HashMap<>();
        changed = new HashMap<>();
    }

    // Agregar un observador
    public synchronized void addObserver(Usuario u, TDSObserver o) {
    	obs.computeIfAbsent(u, k -> new ArrayList<>());

        if (!obs.get(u).contains(o)) {
            obs.get(u).add(o);
            System.out.println("[DEBUG] Observador agregado: " + o + " para el usuario: " + u.getNombre());
        } else {
            System.out.println("[DEBUG] Observador ya existe: " + o + " para el usuario: " + u.getNombre());
        }
    }

    // Eliminar un observador
    public synchronized void deleteObserver(Usuario u, TDSObserver o) {
    	 List<TDSObserver> observadores = obs.get(u);
         if (observadores != null) {
             observadores.remove(o);
             System.out.println("[DEBUG] Observador eliminado: " + o + " para el usuario: " + u.getNombre());
             if (observadores.isEmpty()) {
                 obs.remove(u);
             }
         }
    }

    // Notificar a todos los observadores
   
    public void notifyObservers(Usuario u) {
    	List<TDSObserver> observadoresCopy;
        Set<Estado> estadosCopy;

        synchronized (this) {
            if (!hasChanged(u)) return; // Si no hay cambios, no notificar

            List<TDSObserver> observadores = obs.get(u);
            if (observadores == null || observadores.isEmpty()) {
                clearChanged(u); // Limpia el estado de "cambiado" si no hay observadores
                return;
            }

            // Crear copias para evitar ConcurrentModificationException y minimizar el tiempo de bloqueo
            observadoresCopy = new ArrayList<>(observadores);
            estadosCopy = new HashSet<>(changed.get(u));

            clearChanged(u); // Limpia el estado de "cambiado"
        }

        // Notificar a los observadores fuera del bloque sincronizado para evitar bloqueos prolongados
        for (Estado est : estadosCopy) {
            for (TDSObserver observer : observadoresCopy) {
                try {
                    observer.update(this, est);
                } catch (Exception ex) {
                    // Manejar la excepción, por ejemplo, registrar el error
                    System.err.println("Error notificando al observador: " + ex.getMessage());
                    // Opcional: Puedes continuar notificando a otros observadores o decidir cómo manejarlo
                }
            }
        }
    }

    // Marcar como cambiado
    public synchronized void setChanged(Usuario u, Estado est) {
    	changed.computeIfAbsent(u, k -> new HashSet<>());
        changed.get(u).add(est);
    }

    // Limpiar el estado de "cambiado"
    public synchronized void clearChanged(Usuario u) {
    	changed.remove(u);
    }

    // Comprobar si ha cambiado
    public synchronized boolean hasChanged(Usuario u) {
    	Set<Estado> cambios = changed.get(u);
        return cambios != null && !cambios.isEmpty();
    }

	public synchronized void addObserver(TDSObserver o) {	
	}
	
	public synchronized void deleteObserver(TDSObserver o) {
	}
}

