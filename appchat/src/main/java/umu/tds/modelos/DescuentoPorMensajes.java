package umu.tds.modelos;

public class DescuentoPorMensajes extends Descuento {

	//Atributos
	private final int minMensajesEnviados;
	
	
	public DescuentoPorMensajes(double porcentaje, int n) {
		super(porcentaje);
		if (n < 0) throw new IllegalArgumentException("Número de mensajes enviado por mes es inferior a 0");
		this.minMensajesEnviados = n;
	}


	@Override
	protected boolean esDescuentoAplicable(Usuario usuario) {
		return (this.minMensajesEnviados <= usuario.getMensajesEnviados().size());
	}

	

}
