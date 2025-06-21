package umu.tds.utils;

/**
 * Interfaz para los observadores en el patrón Observer personalizado (TDSObserver).
 * Los implementadores de esta interfaz serán notificados de cambios en un {@link TDSObservable}.
 */
public interface TDSObserver {
    /**
     * Este método es llamado cuando un objeto observable notifica un cambio.
     * @param o El objeto {@link TDSObservable} que ha cambiado.
     * @param arg Un argumento opcional que permite al observable pasar información al observador sobre la naturaleza del cambio.
     */
    void update(TDSObservable o, Object arg);
}