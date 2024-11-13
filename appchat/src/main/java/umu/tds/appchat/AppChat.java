package umu.tds.appchat;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import umu.tds.modelos.Contacto;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;

public class AppChat {
	
	public static boolean crearContacto(Usuario usuario, String nombre, String telefono, URL imagen) {
		//Tendriamos que verificar en el Repositorio si el telefono se encuentra registrado en el sistema
		return (usuario.crearContacto(nombre, telefono, imagen));
	}
	
	public static boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password, String saludo) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		return true;
	}
	
	public static boolean registrarUsuario(String nombre, String apellidos ,String telefono, LocalDate fechaNac, String email, String password) {
		//Se tiene que verificar si el telefono no esta registrado ya
		//Se tiene que verificar si los datos son correctos (se hace en la capa de presentacion¿?)
		return true;
	}
	
	public static boolean iniciarSesionUsuario(String telefono, String contraseña) {
		//Se tiene que verificar en el repositorio si los datos son correctos
		return true;
	}
	/*
	public static boolean cambiarImagen(Usuario usuario, URL imagen) {
		//Se tiene que verificar si los datos son correctos ( se hace en la capa de presentacion¿?)
		return usuario.cambiarImagen(imagen);
	}
	
	*/
	
	public static boolean crearGrupo(Usuario u, String nombre, URL imagen) {
		//Se tiene que verificar que el nombre no este vacio (se hace en la capa de presentacion¿?)
		return (u.crearGrupo(nombre, imagen));
	}
	
	public static boolean crearGrupo(Usuario u, String nombre) {
		//Se tiene que verificar que el nombre no este vacio (se hace en la capa de presentacion¿?)
		return (u.crearGrupo(nombre));
	}
	
	
	public static boolean introducirMiembroAGrupo(Usuario u, Contacto c, Grupo g) {
		return u.introducirMiembroaGrupo(c, g);
	}
	
	public static List <Mensaje> obtenerListaMensajesRecientesPorUsuario(){
		
		Usuario ana = new Usuario("Ana", "", "", LocalDate.now(),"", "");
		Usuario jose = new Usuario("Jose", "", "", LocalDate.now(),"", "");
		Usuario maria = new Usuario("Maria", "", "", LocalDate.now(),"", "");
		Mensaje[] values = new Mensaje [] {
				 new Mensaje(ana,jose, "Hola"),
			     new Mensaje(jose,ana, "Que tal"),
			     new Mensaje(ana,maria, "Adios")
				
		};
		return Arrays.asList(values);
	}
}
