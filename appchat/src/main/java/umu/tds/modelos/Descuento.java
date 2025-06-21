package umu.tds.modelos;

public abstract class Descuento {
	
	//Atributos
	
	private final double porcentajeDescuento;
	
	/**
	 * Constructor protegido para la clase abstracta Descuento.
	 * @param porcentaje el porcentaje de descuento a aplicar.
	 * @throws IllegalArgumentException si el porcentaje es inferior a cero o superior a uno.
	 */
	protected Descuento(double porcentaje ) {
		 if (porcentaje < 0) throw new IllegalArgumentException("Porcentaje es inferior a cero.");
		 else if (porcentaje > 100) throw new IllegalArgumentException("Porcentaje es superior a uno.");
		 this.porcentajeDescuento = porcentaje;
	}
	
	
	/**
	 * Método abstracto que debe ser implementado por las subclases para determinar
	 * si un descuento es aplicable a un usuario dado.
	 * @param usuario el usuario para el cual se verifica la aplicabilidad del descuento.
	 * @return true si el descuento es aplicable, false en caso contrario.
	 */
	protected abstract boolean esDescuentoAplicable(Usuario usuario);
	
	/**
	 * Calcula el monto del descuento a aplicar a un precio dado para un usuario específico.
	 * Si el descuento es aplicable, se calcula el porcentaje del precio; de lo contrario, el descuento es 0.
	 * @param usuario el usuario al que se le aplica el descuento.
	 * @param precio el precio base sobre el cual se calcula el descuento.
	 * @return el monto del descuento.
	 */
	public double calcularDescuento(Usuario usuario, double precio) {
		if (esDescuentoAplicable(usuario)) return (this.porcentajeDescuento*precio);
		else return 0;
	}
}