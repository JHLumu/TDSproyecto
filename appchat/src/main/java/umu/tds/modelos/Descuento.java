package umu.tds.modelos;

import java.time.LocalDate;

import umu.tds.appchat.AppChat;

public abstract class Descuento {
	
	//Atributos
	
	private final double porcentajeDescuento;
	
	protected Descuento(double porcentaje ) {
		 if (porcentaje < 0) throw new IllegalArgumentException("Porcentaje es inferior a cero.");
		 else if (porcentaje > 100) throw new IllegalArgumentException("Porcentaje es superior a uno.");
		 this.porcentajeDescuento = porcentaje;
	}
	
	
	protected abstract boolean esDescuentoAplicable(Usuario usuario);
	
	public double calcularDescuento(Usuario usuario, double precio) {
		if (esDescuentoAplicable(usuario)) return (this.porcentajeDescuento*precio);
		else return 0;
	}
}
