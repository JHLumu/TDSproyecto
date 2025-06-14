package umu.tds.utils;

import java.awt.Image;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {

	private static boolean validacionTelefono(String telefono) {
		//Devuelve falso si telefono esta vacio o no cumple con el formato de 9 numeros sin espacios
		if (telefono.isEmpty()) return false;
		System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono no es vacio.");
		Pattern expresionRegularTelefonos = Pattern.compile("^[0-9]{9}$");
		Matcher matcher = expresionRegularTelefonos.matcher(telefono);
		boolean resultado = matcher.matches();
		System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionTelefono]: " + "Telefono: " + telefono);
		return resultado;
	}
	
	
	private static boolean validacionPasswords(String password, String password1) {
		//Validar el formato de las contraseñas (si añadimos)
		if (password == null || password1 == null) return false;
		boolean resultado = (password.equals(password1));
		System.out.println("[DEBUG Registro validacionPasswords]: " + "Password son iguales: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionPasswords]: " + "Password: " + password);
		return resultado;
	}
	
	private static boolean validacionEmail(String email) {
		//Se sigue el formato estandar de RFC 5322 Y RFC 5321
		if (email.isEmpty()) return false;
		Pattern expresionRegularEmails = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = expresionRegularEmails.matcher(email);
		boolean resultado = matcher.matches();
		System.out.println("[DEBUG Registro validacionEmail]: " + "Email cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionEmail]: " + "Email: " + email);
		return resultado;
	}
	
	private static boolean validacionFecha(Date instante) {
		//Se verifica si el usuario que se quiere registrado tiene al menos 16 años de edad.
		if (instante == null) return false;
		LocalDate fecha = instante.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		boolean resultado = ((LocalDate.now().getYear() - fecha.getYear()) >= 16);
		System.out.println("[DEBUG Registro validacionFecha]: " + "Fecha cumple formato: " + resultado);
		if (resultado) System.out.println("[DEBUG Registro validacionFecha]: " + "Fecha: " + fecha.toString());
		return resultado;
		
	}
	
	private static boolean validacionImagen(String url) {
		Image imagen = ImagenUtils.getImagenAPartirDeURL(url);
		boolean resultado = (imagen != null);
		System.out.println("[DEBUG Registro validacionImagen]: " + "Imagen cumple formato: " + resultado);
		return resultado;
		
	}
	
	private static boolean validacionNombreCompleto(String nombre, String apellidos) {
		if (nombre.isEmpty() || apellidos.isEmpty()) return false;
		Pattern expresionRegular = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+([ -][A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$");
		Matcher matcherNombre = expresionRegular.matcher(nombre);
		Matcher matcherApellidos = expresionRegular.matcher(apellidos);
		boolean resultado1 = matcherNombre.matches();
		boolean resultado2 = matcherApellidos.matches();
		System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Nombre cumple formato: " + resultado1);
		System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Apellidos cumple formato: " + resultado2);
		if (resultado1 && resultado2) System.out.println("[DEBUG Registro validacionNombreCompleto]: " + "Nombre completo: " + nombre + 
				 " " + apellidos);
		return (resultado1 && resultado2);
	}
	
	public static boolean validacionCamposRegistro(Date fecha, String ...datos) {
		return (validacionNombreCompleto(datos[0], datos[1]) && validacionTelefono(datos[2]) 
				&& validacionPasswords(datos[3], datos[4]) && validacionEmail(datos[5]) 
				&& validacionFecha(fecha) && validacionImagen(datos[6]));

	}
}
