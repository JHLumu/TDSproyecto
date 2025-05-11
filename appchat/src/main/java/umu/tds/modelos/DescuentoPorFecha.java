package umu.tds.modelos;

import java.time.LocalDate;

public class DescuentoPorFecha extends Descuento{

	//Atributos
	private final LocalDate fechaInf;
	private final LocalDate fechaSup;
	
	public DescuentoPorFecha(LocalDate fechaInf, LocalDate fechaSup, double porcentaje, double precio ) {
		super(porcentaje);
		if (fechaInf.isAfter(fechaSup)) throw new IllegalArgumentException("Inconsistencia en el intervalo de fechas.");
		this.fechaInf = fechaInf;
		this.fechaSup = fechaSup;
		
	}

	@Override
	protected boolean esDescuentoAplicable(Usuario usuario) {
		LocalDate fechaRegistro = usuario.getFechaRegistro();
		return (!(fechaRegistro.isBefore(fechaInf) || fechaRegistro.isAfter(fechaSup)));
	}
	
	

}
