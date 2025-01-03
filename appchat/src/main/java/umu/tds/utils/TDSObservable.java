package umu.tds.utils;

import java.util.Vector;

public class TDSObservable {

    private boolean changed = false; // Indica si ha habido cambios
    private Vector<TDSObserver> obs; // Lista de observadores

    public TDSObservable() {
        obs = new Vector<>();
    }

    // Agregar un observador
    public synchronized void addObserver(TDSObserver o) {
        if (o == null) throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    // Eliminar un observador
    public synchronized void deleteObserver(TDSObserver o) {
        obs.removeElement(o);
    }

    // Notificar a todos los observadores
    public void notifyObservers(Object arg) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed) return; // Si no hay cambios, no notificar
            arrLocal = obs.toArray();
            clearChanged(); // Limpia el estado de "cambiado"
        }

        // Notificar a cada observador
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((TDSObserver) arrLocal[i]).update(this, arg);
        }
    }

    // Marcar como cambiado
    public synchronized void setChanged() {
        changed = true;
    }

    // Limpiar el estado de "cambiado"
    public synchronized void clearChanged() {
        changed = false;
    }

    // Comprobar si ha cambiado
    public synchronized boolean hasChanged() {
        return changed;
    }
}

