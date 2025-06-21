package umu.tds.modelos;

import java.time.LocalDate;

public class DescuentoPorFecha extends Descuento{

	//Atributos
	private final LocalDate fechaInf;
	private final LocalDate fechaSup;
	
	/**
	 * Constructor de la clase DescuentoPorFecha.
	 * @param fechaInf la fecha inferior del rango para que el descuento sea aplicable.
	 * @param fechaSup la fecha superior del rango para que el descuento sea aplicable.
	 * @param porcentaje el porcentaje de descuento a aplicar.
	 * @param precio el precio base sobre el cual se calculará el descuento.
	 * @throws IllegalArgumentException si la fecha inferior es posterior a la fecha superior.
	 */
	public DescuentoPorFecha(LocalDate fechaInf, LocalDate fechaSup, double porcentaje, double precio ) {
		super(porcentaje);
		if (fechaInf.isAfter(fechaSup)) throw new IllegalArgumentException("Inconsistencia en el intervalo de fechas.");
		this.fechaInf = fechaInf;
		this.fechaSup = fechaSup;
		
	}

	/**
	 * Verifica si el descuento por fecha es aplicable a un usuario dado.
	 * El descuento es aplicable si la fecha de registro del usuario
	 * está dentro del rango de fechas especificado (inclusive).
	 * @param usuario el usuario para el cual se verifica la aplicabilidad del descuento.
	 * @return true si el descuento es aplicable, false en caso contrario.
	 */
	@Override
	protected boolean esDescuentoAplicable(Usuario usuario) {
		LocalDate fechaRegistro = usuario.getFechaRegistro();
		return (!(fechaRegistro.isBefore(fechaInf) || fechaRegistro.isAfter(fechaSup)));
	}
	
	

}