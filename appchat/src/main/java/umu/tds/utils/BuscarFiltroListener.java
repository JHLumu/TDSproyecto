package umu.tds.utils;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.Mensaje;

public interface BuscarFiltroListener {
	void onAccionRealizada(Contacto contacto, Mensaje mensaje);
}
