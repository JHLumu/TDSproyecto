package umu.tds.modelos;

public class Mensaje {
	
	private String texto;
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public Mensaje(String usuario, String texto) {
		super();
		this.texto = texto;
		this.usuario = usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
	@Override
	public String toString() {
		return usuario + " " + texto;
	}
}
