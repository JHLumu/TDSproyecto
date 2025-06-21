package umu.tds.utils;

import java.awt.Image;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para la validación de diversos campos de entrada, como números de teléfono,
 * contraseñas, correos electrónicos, fechas de nacimiento, URLs de imágenes y nombres completos.
 */
public class Validador {

	/**
	 * Valida si un número de teléfono tiene el formato correcto (9 dígitos numéricos sin espacios).
	 * * @param telefono El número de teléfono a validar.
	 * @return true si el teléfono es válido, false en caso contrario (vacío o formato incorrecto).
	 */
	private static boolean validacionTelefono(String telefono) {
		//Devuelve falso si telefono esta vacio o no cumple con el formato de 9 numeros sin espacios
		if (telefono.isEmpty()) return false;
		Pattern expresionRegularTelefonos = Pattern.compile("^[0-9]{9}$");
		Matcher matcher = expresionRegularTelefonos.matcher(telefono);
		boolean resultado = matcher.matches();
		return resultado;
	}
	
	
	/**
	 * Valida si dos contraseñas son iguales. No valida el formato de las contraseñas.
	 * * @param password La primera contraseña.
	 * @param password1 La segunda contraseña para comparar.
	 * @return true si ambas contraseñas son iguales y no nulas, false en caso contrario.
	 */
	private static boolean validacionPasswords(String password, String password1) {
		//Validar el formato de las contraseñas (si añadimos)
		if (password == null || password1 == null) return false;
		boolean resultado = (password.equals(password1));
		return resultado;
	}
	
	/**
	 * Valida si un correo electrónico cumple con un formato estándar (RFC 5322 y RFC 5321).
	 * * @param email El correo electrónico a validar.
	 * @return true si el correo electrónico es válido, false en caso contrario (vacío o formato incorrecto).
	 */
	private static boolean validacionEmail(String email) {
		//Se sigue el formato estandar de RFC 5322 Y RFC 5321
		if (email.isEmpty()) return false;
		Pattern expresionRegularEmails = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = expresionRegularEmails.matcher(email);
		boolean resultado = matcher.matches();
		return resultado;
	}
	
	/**
	 * Valida si una fecha de nacimiento indica que la persona tiene al menos 16 años de edad.
	 * * @param instante La fecha de nacimiento como un objeto Date.
	 * @return true si la persona tiene 16 años o más, false en caso contrario o si la fecha es nula.
	 */
	private static boolean validacionFecha(Date instante) {
		//Se verifica si el usuario que se quiere registrado tiene al menos 16 años de edad.
		if (instante == null) return false;
		LocalDate fecha = instante.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		boolean resultado = ((LocalDate.now().getYear() - fecha.getYear()) >= 16);
		return resultado;
		
	}
	
	/**
	 * Valida si una URL de imagen es válida intentando cargar la imagen.
	 * * @param url La URL de la imagen a validar.
	 * @return true si la imagen puede ser cargada desde la URL, false en caso contrario.
	 */
	private static boolean validacionImagen(String url) {
		Image imagen = ImagenUtils.getImagenAPartirDeURL(url);
		boolean resultado = (imagen != null);
		return resultado;
		
	}
	
	/**
	 * Valida si el nombre y los apellidos cumplen con un formato alfabético y no están vacíos.
	 * Permite espacios y guiones entre palabras.
	 * * @param nombre El nombre a validar.
	 * @param apellidos Los apellidos a validar.
	 * @return true si tanto el nombre como los apellidos son válidos, false en caso contrario.
	 */
	private static boolean validacionNombreCompleto(String nombre, String apellidos) {
		if (nombre.isEmpty() || apellidos.isEmpty()) return false;
		Pattern expresionRegular = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+([ -][A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$");
		Matcher matcherNombre = expresionRegular.matcher(nombre);
		Matcher matcherApellidos = expresionRegular.matcher(apellidos);
		boolean resultado1 = matcherNombre.matches();
		boolean resultado2 = matcherApellidos.matches();
		return (resultado1 && resultado2);
	}
	
	/**
	 * Realiza una validación completa de todos los campos necesarios para el registro de un usuario.
	 * Los datos deben pasarse en el siguiente orden dentro del array de Strings:
	 * datos[0]: nombre, datos[1]: apellidos, datos[2]: teléfono, datos[3]: contraseña,
	 * datos[4]: confirmación de contraseña, datos[5]: email, datos[6]: URL de imagen.
	 * * @param fecha La fecha de nacimiento del usuario.
	 * @param datos Un array de Strings que contiene los demás campos de registro.
	 * @return true si todos los campos de registro son válidos, false en caso contrario.
	 */
	public static boolean validacionCamposRegistro(Date fecha, String ...datos) {
		return (validacionNombreCompleto(datos[0], datos[1]) && validacionTelefono(datos[2]) 
				&& validacionPasswords(datos[3], datos[4]) && validacionEmail(datos[5]) 
				&& validacionFecha(fecha) && validacionImagen(datos[6]));

	}
}