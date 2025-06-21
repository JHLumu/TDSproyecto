package umu.tds.appchat;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import tds.BubbleText;
import umu.tds.appchat.servicios.ServicioContactos;
import umu.tds.appchat.servicios.ServicioMensajes;
import umu.tds.appchat.servicios.ServicioVistas;
import umu.tds.appchat.servicios.mensajes.CriterioBuscarMensaje;
import umu.tds.appchat.servicios.mensajes.MensajeCoincidencia;
import umu.tds.appchat.servicios.vistas.InfoMensajeVista;
import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Descuento;
import umu.tds.modelos.DescuentoPorFecha;
import umu.tds.modelos.DescuentoPorMensajes;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.Usuario;
import umu.tds.persistencia.*;
import umu.tds.utils.ColoresAppChat;
import umu.tds.utils.Estado;
import umu.tds.utils.ImagenUtils;
import umu.tds.utils.TDSObservable;

/**
 * Controlador principal que gestiona la comunicación entre la capa de Modelo y la capa
 * de Vista de la aplicación AppChat.
 * Implementa el patrón Singleton para garantizar una única instancia en toda la aplicación.
 */
public class AppChat extends TDSObservable {
    
    // Constantes
    private static final double PRECIO_SUSCRIPCION = 9.99;
    private static final String SERVIDOR_PERSISTENCIA_ELEGIDO = "umu.tds.persistencia.FactoriaDAOTDS";
    
    // Singleton
    private static final AppChat instancia = new AppChat(SERVIDOR_PERSISTENCIA_ELEGIDO);
    
    // DAOs para acceso a datos
    private final ContactoDAO contactoDAO;
    private final MensajeDAO mensajeDAO;
    private final UsuarioDAO usuarioDAO;
    private final ServicioMensajes mensajeServ;
    private final ServicioContactos contactoServ;
    private final ServicioVistas viewServ;
    private final FactoriaDAO factoria;
    
    // Catálogos
    private final CatalogoUsuarios catalogoUsuarios;
    
    // Usuario en sesión actual
    private Usuario sesionUsuario;
    
    // Descuentos por defecto del sistema
    public static final DescuentoPorFecha DESCUENTO_POR_FECHA_POR_DEFECTO = new DescuentoPorFecha( LocalDate.now(), LocalDate.now().plusYears(1), 0.5, AppChat.PRECIO_SUSCRIPCION);
    public static final DescuentoPorMensajes DESCUENTO_POR_MENSAJES_POR_DEFECTO = new DescuentoPorMensajes(0.3, 10);
    
    /**
     * Constructor privado (patrón Singleton)
     * 
     * @param factoriaPath Ruta de la factoría de persistencia
     */
    private AppChat(String factoriaPath) {    
        this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
        this.factoria = FactoriaDAO.getInstancia(factoriaPath);
        this.contactoDAO = this.factoria.getContactoDAO();
        this.mensajeDAO = this.factoria.getMensajeDAO();
        this.usuarioDAO = this.factoria.getUsuarioDAO();
        this.mensajeServ = new ServicioMensajes(mensajeDAO, usuarioDAO);
        this.contactoServ = new ServicioContactos(contactoDAO, usuarioDAO, this);
        this.viewServ = new ServicioVistas(mensajeServ);
    }
    
    /**
     * Obtiene la instancia única de AppChat
     * 
     * @return La instancia de AppChat
     */
    public static AppChat getInstancia() {
        return instancia;
    }
    
    /**
     * Obtiene el precio de la suscripción premium
     * 
     * @return Precio de la suscripción
     */
    public static double getPrecioSuscripcion() {
        return PRECIO_SUSCRIPCION;
    }
    
    // ---------- MÉTODOS DE GESTIÓN DE USUARIOS ----------
    
    /**
     * Inicia sesión de un usuario en el sistema
     * 
     * @param telefono Teléfono del usuario
     * @param contraseña Contraseña del usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public boolean iniciarSesionUsuario(String telefono, String contraseña) {
        if (!catalogoUsuarios.sonCredencialesCorrectas(telefono, contraseña)) {
            return false;
        }
        this.sesionUsuario = catalogoUsuarios.getUsuario(telefono);
        return true;
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * 
     * @param nombre Nombre del usuario
     * @param apellidos Apellidos del usuario
     * @param telefono Teléfono del usuario (identificador único)
     * @param fechaNac Fecha de nacimiento
     * @param email Correo electrónico
     * @param password Contraseña
     * @param saludo Saludo personalizado (opcional)
     * @param imagen URL de la imagen de perfil
     * @return true si el registro fue exitoso, false si el teléfono ya está registrado o hubo un error
     */
    public boolean registrarUsuario(String nombre, String apellidos, String telefono, 
                                  LocalDate fechaNac, String email, String password, 
                                  String saludo, URL imagen) {
        // Verificar si el teléfono ya está registrado
        if (this.catalogoUsuarios.estaUsuarioRegistrado(telefono)) {
            return false;
        }
        
        // Crear el usuario con el patrón Builder
        Usuario usuario = new Usuario.BuilderUsuario()
                .nombre(nombre)
                .telefono(telefono)
                .apellidos(apellidos)
                .email(email)
                .password(password)
                .saludo(saludo)
                .fechaNac(fechaNac)
                .imagenDePerfil(imagen)
                .build();
        
        // Guardar la imagen y persistir el usuario
        if (ImagenUtils.guardarImagen(usuario)) {
            usuarioDAO.registrarUsuario(usuario);
            catalogoUsuarios.nuevoUsuario(usuario);        
            return true;
        }
        return false;
    }
    
    /**
     * Sobrecarga del método registrarUsuario sin saludo personalizado
     */
    public boolean registrarUsuario(String nombre, String apellidos, String telefono, 
                                  LocalDate fechaNac, String email, String password, 
                                  URL imagen) {
        return this.registrarUsuario(nombre, apellidos, telefono, fechaNac, email, password, "", imagen);
    }
    
    /**
     * Actualiza el estado premium del usuario actual
     * 
     * @param premium Nuevo estado premium
     */
    public void setUsuarioPremium(boolean premium) {
        this.sesionUsuario.setPremium(premium);
        this.usuarioDAO.modificarUsuario(sesionUsuario);
    }
    
    /**
     * Verifica si el usuario actual es premium
     * 
     * @return true si el usuario es premium, false en caso contrario
     */
    public boolean isUsuarioPremium() {
        return this.sesionUsuario.isPremium();
    }
    
    /**
     * Calcula el máximo descuento aplicable al usuario actual con los descuentos por defecto.
     * 
     * @return Valor del descuento máximo aplicable
     */
    public double getDescuentoAplicable() {
    	return getDescuentoAplicable(AppChat.DESCUENTO_POR_FECHA_POR_DEFECTO, AppChat.DESCUENTO_POR_MENSAJES_POR_DEFECTO);
    }
    
    /**
     * Calcula el máximo descuento aplicable al usuario actual.
     * 
     * @param descuentosActivos Lista de descuentos activos en el sistema.
     * @return Valor del descuento máximo aplicable.
     */
    public double getDescuentoAplicable(Descuento... descuentosActivos) {
        Optional<Double> res = Stream.of(descuentosActivos)
                .map(d -> d.calcularDescuento(sesionUsuario, PRECIO_SUSCRIPCION))
                .max(Double::compare);
        
        return res.orElse(0.0);
    }
    
    /**
     * Calcula el precio de la suscripción aplicado el máximo descuento de los por defecto.
     * @return Precio total de la suscripción.
     */
    public double calcularPrecioSuscripcion() {
    	return AppChat.getPrecioSuscripcion() - getDescuentoAplicable();
    }
    
    /**
     * Calcula el precio de la suscripción una vez aplicado el máximo descuento aplicable de una lista de descuentos.
     * @param descuentosActivos Lista de descuentos activos en el sistema.
     * @return Precio total de la suscripción.
     */
    public double calcularPrecioSuscripcion(Descuento... descuentosActivos) {
    	return AppChat.getPrecioSuscripcion() - getDescuentoAplicable(descuentosActivos);
    }
    
    /**
     * Actualiza el saludo del usuario actual-
     * 
     * @param saludo Nuevo saludo
     */
    public void cambiarSaludo(String saludo) {
        this.sesionUsuario.setSaludo(saludo);
        usuarioDAO.modificarUsuario(sesionUsuario);
    }
    
    /**
     * Actualiza la imagen de perfil del usuario actual
     * 
     * @param fotoURL URL de la nueva imagen
     * @param foto Objeto Image de la nueva foto
     */
    public void cambiarFotoPerfil(URL fotoURL, Image foto) {
        this.sesionUsuario.setURLImagen(fotoURL);
        
        File fileImagenContacto = ImagenUtils.getFile(this.sesionUsuario);
        try {
            ImageIO.write((RenderedImage) foto, "png", fileImagenContacto);
            
            setChanged(Estado.NUEVA_FOTO_USUARIO);
            usuarioDAO.modificarUsuario(sesionUsuario);
            notifyObservers(Estado.NUEVA_FOTO_USUARIO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Usuario getUsuarioActual() {
    	return this.sesionUsuario;
    }

    
    // ---------- MÉTODOS DE INTERFAZ GRÁFICA ----------
    
    /**
     * Obtiene el color para la interfaz según el estado premium
     * 
     * @param id 1 para color primario, 2 para color secundario
     * @return Color correspondiente
     */
    public Color getColorGUI(int id) {
        boolean premium = this.isUsuarioPremium();
        
        if (id == 1) {
            return premium ? ColoresAppChat.COLOR_PREMIUM : ColoresAppChat.COLOR_NOPREMIUM;
        } else if (id == 2) {
            return premium ? ColoresAppChat.COLOR_PREMIUM_2 : ColoresAppChat.COLOR_NOPREMIUM_2;
        }
        
        return null;
    }
    
    /**
     * Obtiene la URL del icono según el estado premium
     */
    public String getURLIcon() {
        return this.isUsuarioPremium() ? "/Resources/chat_premium.png" : "/Resources/chat.png";
    }
    
    // ---------- GESTIÓN DE CONTACTOS ----------
    
    /**
     * Crea un nuevo contacto individual para el usuario actual
     * 
     * @param nombre Nombre del contacto
     * @param telefono Teléfono del contacto
     * @return -1 si el teléfono no está registrado, 0 si ya es contacto, 1 si se añadió correctamente
     */
    public int nuevoContacto(String nombre, String telefono) {
        return contactoServ.crearContactoIndividual(sesionUsuario, nombre, telefono);
    }
    
    
    public Contacto nuevoContacto(String telefono) {
    	return contactoServ.crearContactoIndividual(telefono);
    }
    
    
    /**
     * Obtiene la lista completa de contactos del usuario
     */
    public List<Contacto> obtenerListaContactos() {
        return contactoServ.obtenerTodosLosContactos(sesionUsuario);
    }
    
    /**
     * Obtiene sólo los contactos individuales, ordenados por nombre
     */
    public List<Contacto> obtenerListaContactosIndividuales() {
        return contactoServ.obtenerContactosIndividuales(sesionUsuario);
    }
    
    /**
     * Obtiene sólo los grupos, ordenados por nombre
     */
    public List<Contacto> obtenerListaContactosGrupo() {
        return contactoServ.obtenerGrupos(sesionUsuario);
    }
    
    /**
     * Obtiene la lista de contactos con los que se ha intercambiado mensajes
     */
    public List<Contacto> obtenerListaChatMensajes() {
        return contactoServ.obtenerContactosConMensajes(sesionUsuario);
    }
    
    /**
     * Verifica si un contacto pertenece a la lista de contactos del usuario
     */
    public boolean esContacto(Contacto contacto) {
        return contactoServ.esContacto(sesionUsuario, contacto);
    }
    
    /**
     * Obtiene el teléfono de un contacto o del anfitrión si es un grupo
     */
    public String getTelefonoContacto(Contacto contacto) {
        return contactoServ.obtenerTelefonoContacto(contacto);
    }
    
    // ---------- GESTIÓN DE GRUPOS ----------
    
    /**
     * Crea un nuevo grupo vacío
     * 
     * @param nombre Nombre del grupo
     * @param urlImagen URL de la imagen del grupo (opcional)
     * @return -1 si hay error con la imagen, 0 si ya existe un grupo con ese nombre, 1 si se creó correctamente
     */
    public int nuevoGrupo(String nombre, URL urlImagen) {
        return contactoServ.crearGrupo(sesionUsuario, nombre, urlImagen);
    }
    
    /**
     * Añade un miembro a un grupo
     * 
     * @param grupo Grupo al que añadir el miembro
     * @param contacto Contacto individual que se añadirá como miembro
     */
    public void nuevoMiembroGrupo(Grupo grupo, Contacto contacto) {
       
        contactoServ.agregarMiembroGrupo(sesionUsuario, grupo, contacto);
        
    }
    
    /**
     * Elimina un miembro de un grupo
     * 
     * @param grupo Grupo del que eliminar el miembro
     * @param contacto Contacto individual que se eliminará como miembro
     */
    public void eliminarMiembroGrupo(Grupo grupo, Contacto contacto) {
        contactoServ.eliminarMiembroGrupo(sesionUsuario, grupo, contacto);
    }
    
    /**
     * Obtiene la lista de miembros de un grupo
     */
    public List<Contacto> obtenerListaMiembrosGrupo(Grupo grupo) {
        return contactoServ.obtenerMiembrosGrupo(grupo);
    }
    
    // ---------- GESTIÓN DE MENSAJES ----------
      

    public boolean enviarMensaje(Contacto contacto, Object entrada) {
        return mensajeServ.enviarMensaje(sesionUsuario, contacto, entrada);
    }
    
    public boolean enviarMensaje(String telefono, Object entrada) {
    	return mensajeServ.enviarMensaje(sesionUsuario, telefono, entrada);
    }
    
    public void enviarMensaje(String text, String telf) {
		Usuario usuario = this.catalogoUsuarios.getUsuario(telf);
		
		mensajeServ.enviarMensajeTelefono(sesionUsuario, usuario, text);
		
	}
    
    /**
    * Busca mensajes que contienen un texto específico, opcionalmente filtrando por emisor y/o receptor
    * Si no se proporciona texto de búsqueda, devuelve todos los mensajes que cumplan con los filtros de emisor/receptor
    * 
    * Soporta búsqueda de emojis con el formato "Emoji:<número de 0 a 23>"
    * 
    * Los resultados se ordenan por:
    * - Si hay textoFiltro: ordenados por precisión de coincidencia
    * - Si no hay textoFiltro: ordenados por coincidencia de contacto y luego por fecha de envío
    *
    * **@param** textoFiltro Texto a buscar en los mensajes (puede ser null o vacío)
    * **@param** nombreFiltro Filtro de nombre de contacto (puede ser null)
    * **@param** telefonoFiltro Filtro de teléfono de contacto (puede ser null)
    * **@return** Lista de mensajes coincidentes ordenados según los criterios establecidos
    */
    public List<MensajeCoincidencia> buscarMensajes(String textoFiltro, String nombreFiltro, String telefonoFiltro) {
        return mensajeServ.buscarMensajes(sesionUsuario, new CriterioBuscarMensaje(textoFiltro, nombreFiltro, telefonoFiltro));
    }

    
    // ---------- MÉTODOS DE PRESENTACIÓN ----------
    
    /**
     * Prepara la información de un mensaje para mostrar en la vista
     */
    public InfoMensajeVista prepararInfoParaVista(Object item) {
        return viewServ.prepararInfoMensajeParaVista(item, sesionUsuario);
    }
    


	/**
     * Formatea un mensaje de texto para la vista (trunca si es muy largo)
     */
    public String formatearMensajeParaVista(String mensajeTexto) {
        return viewServ.formatearTextoMensaje(mensajeTexto);
    }
    
    /**
     * Formatea la fecha de un mensaje para mostrar solo la hora
     */
    public String formatearFechaMensaje(LocalDateTime fecha) {
        return viewServ.formatearFechaMensaje(fecha);
    }
    
    /**
     * Redimensiona un emoji para la vista
     */
    public ImageIcon obtenerEmojiRedimensionado(Integer emojiCode) {
        return viewServ.obtenerEmojiRedimensionado(emojiCode);
    }
    
    /**
     * Formatea el saludo de un contacto para la vista
     */
    public String formatearSaludoVista(Contacto contacto) {
        return viewServ.formatearSaludoVista(contacto);
    }
    
    /**
     * Genera las burbujas de chat para mostrar en la interfaz
     * 
     * @param contacto Contacto del chat
     * @param chat Panel donde se mostrarán las burbujas
     * @return Lista de burbujas de texto
     */
    public List<BubbleText> pintarMensajesBurbuja(Contacto contacto, JPanel chat) {
        return viewServ.generarBurbujasMensajes(contacto, chat, sesionUsuario);
    }
    
    public int ubicarMensaje(Contacto c, Mensaje mObjetivo) {
       return mensajeServ.ubicarMensaje(sesionUsuario, c, mObjetivo);
    }

}
