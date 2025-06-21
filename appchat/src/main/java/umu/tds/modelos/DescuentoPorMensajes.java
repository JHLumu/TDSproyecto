package umu.tds.modelos;

public class DescuentoPorMensajes extends Descuento {

	//Atributos
	private final int minMensajesEnviados;
	
	/**
	 * Constructor de la clase DescuentoPorMensajes.
	 * @param porcentaje el porcentaje de descuento a aplicar.
	 * @param n el número mínimo de mensajes enviados para que el descuento sea aplicable.
	 * @throws IllegalArgumentException si el número de mensajes enviados es inferior a 0.
	 */
	public DescuentoPorMensajes(double porcentaje, int n) {
		super(porcentaje);
		if (n < 0) throw new IllegalArgumentException("Número de mensajes enviado por mes es inferior a 0");
		this.minMensajesEnviados = n;
	}


	/**
	 * Verifica si el descuento por mensajes es aplicable a un usuario dado.
	 * El descuento es aplicable si el número de mensajes enviados por el usuario
	 * es mayor o igual al número mínimo de mensajes requeridos.
	 * @param usuario el usuario para el cual se verifica la aplicabilidad del descuento.
	 * @return true si el descuento es aplicable, false en caso contrario.
	 */
	@Override
	protected boolean esDescuentoAplicable(Usuario usuario) {
		return (this.minMensajesEnviados <= usuario.getMensajesEnviados().size());
	}

	

}