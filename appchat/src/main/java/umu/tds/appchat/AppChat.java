package umu.tds.appchat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import tds.BubbleText;
import umu.tds.modelos.CatalogoUsuarios;
import umu.tds.modelos.Contacto;
import umu.tds.modelos.Contacto.TipoContacto;
import umu.tds.modelos.ContactoIndividual;
import umu.tds.modelos.Descuento;
import umu.tds.modelos.DescuentoPorFecha;
import umu.tds.modelos.DescuentoPorMensajes;
import umu.tds.modelos.Grupo;
import umu.tds.modelos.Mensaje;
import umu.tds.modelos.MensajeCoincidencia;
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
    private static final int MAX_MESSAGE_PREVIEW_LENGTH = 16;
    
    // Singleton
    private static final AppChat instancia = new AppChat(SERVIDOR_PERSISTENCIA_ELEGIDO);
    
    // DAOs para acceso a datos
    private final ContactoDAO contactoDAO;
    private final MensajeDAO mensajeDAO;
    private final UsuarioDAO usuarioDAO;
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
     * Calcula el máximo descuento aplicable al usuario actual
     * 
     * @param descuentosActivos Lista de descuentos activos en el sistema
     * @return Valor del descuento máximo aplicable
     */
    
    public double getDescuentoAplicable() {
    	return getDescuentoAplicable(AppChat.DESCUENTO_POR_FECHA_POR_DEFECTO, AppChat.DESCUENTO_POR_MENSAJES_POR_DEFECTO);
    }
    
    public double getDescuentoAplicable(Descuento... descuentosActivos) {
        Optional<Double> res = Stream.of(descuentosActivos)
                .map(d -> d.calcularDescuento(sesionUsuario, PRECIO_SUSCRIPCION))
                .max(Double::compare);
        
        return res.orElse(0.0);
    }
    
    public double calcularPrecioSuscripcion() {
    	return AppChat.getPrecioSuscripcion() - getDescuentoAplicable();
    }
    
    public double calcularPrecioSuscripcion(Descuento... descuentosActivos) {
    	System.out.println("[DEBUG AppChat calcularPrecioSuscripcion]: " + AppChat.getPrecioSuscripcion() + " " + getDescuentoAplicable(descuentosActivos));
    	return AppChat.getPrecioSuscripcion() - getDescuentoAplicable(descuentosActivos);
    }
    
    /**
     * Actualiza el saludo del usuario actual
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
    
    // ---------- GETTERS DE INFORMACIÓN DEL USUARIO ----------
    
    /**
     * Obtiene el nombre del usuario en sesión
     */
    public String getNombreUsuario() {
        return this.sesionUsuario.getNombre();
    }
    
    /**
     * Obtiene el teléfono del usuario en sesión
     */
    public String getTelefonoUsuario() {
        return this.sesionUsuario.getTelefono();
    }
    
    /**
     * Obtiene los apellidos del usuario en sesión
     */
    public String getApellidosUsuario() {
        return this.sesionUsuario.getApellidos();
    }
    
    /**
     * Obtiene la fecha de nacimiento del usuario en sesión
     */
    public String getFechaNacimientoUsuario() {
        return this.sesionUsuario.getFechaNacimiento().toString();
    }
    
    /**
     * Obtiene el correo del usuario en sesión
     */
    public String getCorreoUsuario() {
        return this.sesionUsuario.getEmail();
    }
    
    /**
     * Obtiene el saludo del usuario en sesión
     */
    public String getSaludoUsuario() {
        return this.sesionUsuario.getSaludo(); 
    }
    
    /**
     * Obtiene la imagen del usuario en sesión
     */
    public Image getImagenUsuarioActual() {
        return ImagenUtils.getImagen(this.sesionUsuario);
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
        // Verificar si el teléfono está registrado en el sistema
        if (!catalogoUsuarios.estaUsuarioRegistrado(telefono)) {
            return -1; // Teléfono no registrado en el sistema
        }
        
        // Obtener el usuario asociado al teléfono
        Usuario usuarioAsociado = catalogoUsuarios.getUsuario(telefono);
        
        // Intentar crear el contacto
        if (!this.sesionUsuario.crearContacto(nombre, usuarioAsociado)) {
            return 0; // El contacto ya existe en la lista
        }
        
        // Obtener el contacto creado y persistir los cambios
        ContactoIndividual contacto = this.sesionUsuario.recuperarContactoIndividual(telefono);
        
        setChanged(Estado.INFO_CONTACTO);
        contactoDAO.registrarContacto(contacto);
        usuarioDAO.modificarUsuario(sesionUsuario);
        notifyObservers(Estado.INFO_CONTACTO);
        
        return 1; // Contacto añadido con éxito
    }
    
    /**
     * Obtiene la lista completa de contactos del usuario
     */
    public List<Contacto> obtenerListaContactos() {
        return this.sesionUsuario.getListaContacto();
    }
    
    /**
     * Obtiene sólo los contactos individuales, ordenados por nombre
     */
    public List<Contacto> obtenerListaContactosIndividuales() {
        if (this.sesionUsuario == null) {
            return new LinkedList<>();
        }

        return this.sesionUsuario.getListaContacto().stream()
                .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.INDIVIDUAL))
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene sólo los grupos, ordenados por nombre
     */
    public List<Contacto> obtenerListaContactosGrupo() {
        if (this.sesionUsuario == null) {
            return new LinkedList<>();
        }

        return this.sesionUsuario.getListaContacto().stream()
                .filter(contacto -> contacto.getTipoContacto().equals(TipoContacto.GRUPO))
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene la lista de contactos con los que se ha intercambiado mensajes
     */
    public List<Contacto> obtenerListaChatMensajes() {
        return this.sesionUsuario.getListaContactosConMensajes();
    }
    
    /**
     * Verifica si un contacto pertenece a la lista de contactos del usuario
     */
    public boolean esContacto(Contacto contacto) {
        return obtenerListaContactos().contains(contacto);
    }
    
    /**
     * Obtiene el teléfono de un contacto o del anfitrión si es un grupo
     */
    public String getTelefonoContacto(Contacto contacto) {
        return contacto instanceof ContactoIndividual ? 
                ((ContactoIndividual) contacto).getTelefono() :
                ((Grupo) contacto).getAnfitrion();
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
        // Verificar si ya existe un grupo con ese nombre
        if (this.sesionUsuario.recuperarGrupo(nombre) != null) {
            return 0; // Ya existe un grupo con ese nombre
        }

        // Crear el grupo
        this.sesionUsuario.crearGrupo(nombre, urlImagen);
        Grupo grupo = this.sesionUsuario.recuperarGrupo(nombre);
     
        // Guardar la imagen si se proporcionó
        if (urlImagen != null && !ImagenUtils.guardarImagen(grupo)) {
            return -1; // Error al guardar la imagen
        }
           
        // Persistir los cambios y notificar
        setChanged(Estado.INFO_CONTACTO);
        contactoDAO.registrarContacto(grupo);
        usuarioDAO.modificarUsuario(sesionUsuario);
        notifyObservers(Estado.INFO_CONTACTO);
            
        return 1; // Grupo creado con éxito
    }
    
    /**
     * Añade un miembro a un grupo
     * 
     * @param grupo Grupo al que añadir el miembro
     * @param contacto Contacto individual que se añadirá como miembro
     */
    public void nuevoMiembroGrupo(Grupo grupo, Contacto contacto) {
       
        Grupo recuperado = this.sesionUsuario.nuevoMiembroGrupo(grupo, contacto);
        
        if(recuperado != null) {
        	setChanged(Estado.INFO_CONTACTO);
            contactoDAO.modificarContacto(recuperado);
            usuarioDAO.modificarUsuario(sesionUsuario);
            notifyObservers(Estado.INFO_CONTACTO);
        }
        
    }
    
    /**
     * Elimina un miembro de un grupo
     * 
     * @param grupo Grupo del que eliminar el miembro
     * @param contacto Contacto individual que se eliminará como miembro
     */
    public void eliminarMiembroGrupo(Grupo grupo, Contacto contacto) {
        Grupo recuperado = this.sesionUsuario.eliminarMiembroGrupo(grupo, contacto);
        
        if(recuperado != null) {
        	setChanged(Estado.INFO_CONTACTO);
            contactoDAO.modificarContacto(recuperado);
            usuarioDAO.modificarUsuario(sesionUsuario);
            notifyObservers(Estado.INFO_CONTACTO);
        }
    }
    
    /**
     * Obtiene la lista de miembros de un grupo
     */
    public List<Contacto> obtenerListaMiembrosGrupo(Grupo grupo) {
        return grupo.getMiembros();
    }
    
    // ---------- GESTIÓN DE MENSAJES ----------
      
    /**
     * Obtiene todos los mensajes intercambiados con un contacto
     * 
     * @param contacto Contacto o grupo
     * @return Lista de mensajes ordenados cronológicamente
     */
    public List<Mensaje> obtenerChatContacto(Contacto contacto) {
        return this.sesionUsuario.getChatMensaje(contacto);
    }
    
    /**
     * Envía un mensaje a un contacto o grupo
     * 
     * @param contacto Destinatario (individual o grupo)
     * @param entrada Contenido del mensaje (String para texto, Integer para emoji)
     * @return true si se envió correctamente, false en caso contrario
     */
    public boolean enviarMensaje(Contacto contacto, Object entrada) {
        // Validar tipo de entrada
        if (!(entrada instanceof String || entrada instanceof Integer)) {
            return false;
        }

        // Caso: mensaje a contacto individual
        if (contacto instanceof ContactoIndividual) {
            return enviarMensajeIndividual((ContactoIndividual) contacto, entrada);
        } 
        // Caso: mensaje a grupo
        else if (contacto instanceof Grupo) {
            return enviarMensajeGrupo((Grupo) contacto, entrada);
        }

        return false;
    }
    
    public void enviarMensaje(String text, String telf) {
		Usuario usuario = this.catalogoUsuarios.getUsuario(telf);
		
		if(usuario != null && !text.isEmpty()) {
	        Mensaje mensaje = new Mensaje(sesionUsuario, usuario, text, null);

	        // Persistir y actualizar en ambos usuarios
	        setChanged(Estado.INFO_CONTACTO);
	        mensajeDAO.registrarMensaje(mensaje);
	        sesionUsuario.enviarMensaje(mensaje);
	        usuarioDAO.modificarUsuario(sesionUsuario);
	        usuario.recibirMensaje(mensaje);
	        usuarioDAO.modificarUsuario(usuario);
	        notifyObservers(Estado.INFO_CONTACTO);
		}
		
	}
    
    /**
     * Método auxiliar para enviar mensaje a un contacto individual
     */
    private boolean enviarMensajeIndividual(ContactoIndividual contacto, Object entrada) {
        Usuario receptor = contacto.getUsuario();
        Mensaje mensaje;

        // Crear mensaje según tipo de entrada
        if (entrada instanceof String) {
            mensaje = new Mensaje(sesionUsuario, receptor, (String) entrada, null);
        } else {
            mensaje = new Mensaje(sesionUsuario, receptor, (Integer) entrada, null);
        }

        // Persistir y actualizar en ambos usuarios
        
        mensajeDAO.registrarMensaje(mensaje);
        sesionUsuario.enviarMensaje(mensaje);
        usuarioDAO.modificarUsuario(sesionUsuario);
        receptor.recibirMensaje(mensaje);
        usuarioDAO.modificarUsuario(receptor);

        return true;
    }
    
    /**
     * Método auxiliar para enviar mensaje a un grupo
     */
    private boolean enviarMensajeGrupo(Grupo grupo, Object entrada) {
        Mensaje mensaje;

        List<Contacto> miembros = grupo.getMiembros();
        
        
        // Enviar a todos los miembros
        for (Contacto miembro : miembros) {
        	Usuario receptor = ((ContactoIndividual) miembro).getUsuario();
            // Crear mensaje
            if (entrada instanceof String) {
                mensaje = new Mensaje(sesionUsuario, receptor, (String) entrada, grupo);
            } else {
                mensaje = new Mensaje(sesionUsuario, receptor, (Integer) entrada, grupo);
            }
            
            // Persistir mensaje
            mensajeDAO.registrarMensaje(mensaje);
            sesionUsuario.enviarMensaje(mensaje);
            receptor.recibirMensaje(mensaje);
            usuarioDAO.modificarUsuario(receptor);
            
        }
        // Crear mensaje para grupo
        if (entrada instanceof String) {
            mensaje = new Mensaje(sesionUsuario, sesionUsuario, (String) entrada, grupo);
        } else {
            mensaje = new Mensaje(sesionUsuario, sesionUsuario, (Integer) entrada, grupo);
        }
        mensajeDAO.registrarMensaje(mensaje);
        sesionUsuario.enviarMensaje(mensaje);
        usuarioDAO.modificarUsuario(sesionUsuario);
        
        return true;

    }

	/**
     * Obtiene el contenido del último mensaje con un contacto
     */
    public Object getUltimoMensajeContacto(Contacto contacto) {
        Mensaje m = this.sesionUsuario.getUltimoChatMensaje(contacto);
        return m != null ? m.getContenido() : null;
    }
    
    /**
     * Obtiene la fecha del último mensaje con un contacto
     */
    public LocalDateTime getUltimoMensajeFecha(Contacto contacto) {
        Mensaje m = this.sesionUsuario.getUltimoChatMensaje(contacto);
        return m != null ? m.getFechaEnvio() : null;
    }
    
    public List<Mensaje> getTodoMensajes(){
    	return this.sesionUsuario.getMensajes();
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
        List<Contacto> contactos = AppChat.getInstancia().obtenerListaChatMensajes();
        List<MensajeCoincidencia> mensajesCoincidentes = new ArrayList<>();
        
        // Estructura para almacenar información de puntuación para ordenar posteriormente
        Map<MensajeCoincidencia, Double> puntuacionesTexto = new HashMap<>();
        Map<MensajeCoincidencia, Double> puntuacionesContacto = new HashMap<>();
        
        // Determinar si estamos buscando un emoji específico
        boolean busquedaEmoji = false;
        int emojiNumero = -1;
        
        if (textoFiltro.matches("(?i)Emoji:\\s*([0-9]|1[0-9]|2[0-3])")) {
            busquedaEmoji = true;
            // Extraer el número de emoji (0-24)
            emojiNumero = Integer.parseInt(textoFiltro.replaceAll("(?i)Emoji:\\s*", ""));
        }
        
        boolean busquedaPorTexto = (textoFiltro != null && !textoFiltro.isEmpty());
        boolean busquedaPorContacto = (nombreFiltro != null && !nombreFiltro.isEmpty()) || 
                                     (telefonoFiltro != null && !telefonoFiltro.isEmpty());

        // Primero filtramos los contactos si es necesario
        if (busquedaPorContacto) {
            contactos = contactos.stream()
                .filter(contacto -> {
                    boolean coincideNombre = nombreFiltro == null || nombreFiltro.isEmpty() ||
                        contacto.getNombre().toLowerCase().contains(nombreFiltro.toLowerCase());
                    
                    boolean coincideTelefono = telefonoFiltro == null || telefonoFiltro.isEmpty() ||
                        AppChat.getInstancia().getTelefonoContacto(contacto).contains(telefonoFiltro);
                    
                    return coincideNombre && coincideTelefono;
                })
                .collect(Collectors.toList());
        }

        // Ahora buscamos en los mensajes de los contactos filtrados
        for (Contacto contacto : contactos) {
            // Calculamos la puntuación de coincidencia del contacto
            double puntuacionContacto = calcularPuntuacionCoincidenciaContacto(
                contacto, nombreFiltro, telefonoFiltro);
                
            List<Mensaje> mensajes = AppChat.getInstancia().obtenerChatContacto(contacto);
            if (mensajes != null) {
                for (Mensaje mensaje : mensajes) {
                    // Verificar si estamos buscando emojis
                    if (busquedaEmoji) {
                        // Crear un MensajeCoincidencia para poder acceder a getContenido()
                        MensajeCoincidencia coincidencia = new MensajeCoincidencia(mensaje, contacto);
                        Object contenido = coincidencia.getContenido();
                        
                        // Verificar si el contenido es un entero (emoji)
                        if (contenido instanceof Integer) {
                            int emojiValue = (Integer) contenido;
                            
                            // Si coincide con el emoji buscado o si no se especificó un emoji específico
                            if (emojiNumero == -1 || emojiValue == emojiNumero) {
                                mensajesCoincidentes.add(coincidencia);
                                
                                // Asignamos puntuación máxima para emojis que coinciden exactamente
                                double puntuacionTexto = (emojiNumero == -1 || emojiValue == emojiNumero) ? 1.0 : 0.0;
                                puntuacionesTexto.put(coincidencia, puntuacionTexto);
                                puntuacionesContacto.put(coincidencia, puntuacionContacto);
                            }
                        }
                    } else {
                    	// Procesamiento normal para mensajes de texto **y emojis** cuando no hay filtro de texto
                        String textoMensaje = mensaje.getTexto();
                        Object contenido = new MensajeCoincidencia(mensaje, contacto).getContenido();

                        // Calculamos la puntuación de texto (si hay filtro) o la dejamos a 1.0
                        double puntuacionTexto = busquedaPorTexto
                            ? calcularPuntuacionCoincidencia(textoMensaje, textoFiltro)
                            : 1.0;

                        // Si coincide el texto O es un emoji (Integer) o no filtramos por texto
                        if (!busquedaPorTexto || puntuacionTexto > 0 || contenido instanceof Integer) {
                            MensajeCoincidencia coincidencia = new MensajeCoincidencia(mensaje, contacto);
                            mensajesCoincidentes.add(coincidencia);

                            // Para emojis sin filtro de texto, asignamos puntuación máxima
                            if (contenido instanceof Integer) {
                                puntuacionesTexto.put(coincidencia, 1.0);
                            } else {
                                puntuacionesTexto.put(coincidencia, puntuacionTexto);
                            }
                            puntuacionesContacto.put(coincidencia, puntuacionContacto);
                        }
                    }
                }
            }
        }

        // Ordenamos los resultados según el criterio correspondiente
        if (busquedaPorTexto) {
            // Si hay búsqueda por texto, ordenamos primero por puntuación de texto
            Collections.sort(mensajesCoincidentes, (m1, m2) -> {
                int comparacion = Double.compare(puntuacionesTexto.get(m2), puntuacionesTexto.get(m1));
                if (comparacion == 0) {
                    // Si empatan en puntuación de texto, ordenamos por fecha de envío (más reciente primero)
                    return m2.getFechaEnvio().compareTo(m1.getFechaEnvio());
                }
                return comparacion;
            });
        } else {
            // Si no hay búsqueda por texto, ordenamos por puntuación de contacto y luego por fecha
            Collections.sort(mensajesCoincidentes, (m1, m2) -> {
                int comparacion = Double.compare(puntuacionesContacto.get(m2), puntuacionesContacto.get(m1));
                if (comparacion == 0) {
                    // Si empatan en puntuación de contacto, ordenamos por fecha de envío (más reciente primero)
                    return m2.getFechaEnvio().compareTo(m1.getFechaEnvio());
                }
                return comparacion;
            });
        }

        return mensajesCoincidentes;
    }

    /**
     * Calcula la puntuación de coincidencia entre un texto de mensaje y un filtro de búsqueda
     * 
     * @param textoMensaje El texto del mensaje a evaluar
     * @param textoFiltro El texto de búsqueda
     * @return Puntuación de coincidencia (0 si no hay coincidencia)
     */
    private double calcularPuntuacionCoincidencia(String textoMensaje, String textoFiltro) {
        if (textoFiltro == null || textoFiltro.isEmpty()) {
            return 1.0; // Coincidencia máxima si no hay filtro
        }
        
        String mensajeLower = textoMensaje.toLowerCase();
        String filtroLower = textoFiltro.toLowerCase();
        
        if (mensajeLower.contains(filtroLower)) {
            // Mayor puntuación si el texto es más corto o si la coincidencia está al principio
            double puntuacionLongitud = 1.0 - ((double) textoMensaje.length() / 1000);
            if (puntuacionLongitud < 0.1) puntuacionLongitud = 0.1;
            
            int posicion = mensajeLower.indexOf(filtroLower);
            double puntuacionPosicion = 1.0 - ((double) posicion / textoMensaje.length());
            
            return 0.7 + (0.15 * puntuacionLongitud) + (0.15 * puntuacionPosicion);
        }
        
        return 0.0;
    }

    /**
     * Calcula la puntuación de coincidencia de un contacto con los filtros de nombre y teléfono
     * 
     * @param contacto El contacto a evaluar
     * @param nombreFiltro Filtro de nombre
     * @param telefonoFiltro Filtro de teléfono
     * @return Puntuación de coincidencia del contacto
     */
    private double calcularPuntuacionCoincidenciaContacto(Contacto contacto, String nombreFiltro, String telefonoFiltro) {
        double puntuacion = 1.0;
        
        if (nombreFiltro != null && !nombreFiltro.isEmpty()) {
            String nombreContacto = contacto.getNombre().toLowerCase();
            String filtroNombre = nombreFiltro.toLowerCase();
            
            if (nombreContacto.equals(filtroNombre)) {
                puntuacion *= 1.0; // Coincidencia exacta
            } else if (nombreContacto.contains(filtroNombre)) {
                // Mayor puntuación si el filtro está al principio del nombre
                int posicion = nombreContacto.indexOf(filtroNombre);
                double factorPosicion = 1.0 - ((double) posicion / nombreContacto.length());
                puntuacion *= 0.7 + (0.3 * factorPosicion);
            } else {
                puntuacion *= 0.0; // No hay coincidencia
                return 0.0;
            }
        }
        
        if (telefonoFiltro != null && !telefonoFiltro.isEmpty()) {
            String telefonoContacto = AppChat.getInstancia().getTelefonoContacto(contacto);
            
            if (telefonoContacto.equals(telefonoFiltro)) {
                puntuacion *= 1.0; // Coincidencia exacta
            } else if (telefonoContacto.contains(telefonoFiltro)) {
                puntuacion *= 0.8; // Coincidencia parcial
            } else {
                puntuacion *= 0.0; // No hay coincidencia
                return 0.0;
            }
        }
        
        return puntuacion;
    }
    
    // ---------- MÉTODOS DE PRESENTACIÓN ----------
    
    /**
     * Clase para encapsular la información procesada de un mensaje para la vista
     */
    public class InfoMensajeVista {
        private String textoFormateado;
        private ImageIcon emojiRedimensionado;
        private String horaFormateada;
        private boolean esEmoji;

        private boolean esMensajeEnviado;

        public boolean esMensajeEnviado() { return esMensajeEnviado; }
        public void setEsMensajeEnviado(boolean enviado) { this.esMensajeEnviado = enviado; }
        
        public String getTextoFormateado() { return textoFormateado; }
        public void setTextoFormateado(String texto) { this.textoFormateado = texto; }
        
        public ImageIcon getEmojiRedimensionado() { return emojiRedimensionado; }
        public void setEmojiRedimensionado(ImageIcon emoji) { 
            this.emojiRedimensionado = emoji;
            this.esEmoji = (emoji != null);
        }
        
        public String getHoraFormateada() { return horaFormateada; }
        public void setHoraFormateada(String hora) { this.horaFormateada = hora; }
        
        public boolean esEmoji() { return esEmoji; }
    }
    
    /**
     * Prepara la información de un mensaje para mostrar en la vista
     */
    public InfoMensajeVista prepararInfoParaVista(Object item) {
        InfoMensajeVista info = new InfoMensajeVista();
        
        Object contenido = null;
        LocalDateTime fecha = null;
        boolean esEnviado = false;

        // Extraer contenido y fecha según el tipo de objeto
        if (item instanceof Contacto) {
            Contacto contacto = (Contacto) item;
            esEnviado = esUltimoMensajeEnviado(contacto);
            contenido = getUltimoMensajeContacto(contacto);
            fecha = getUltimoMensajeFecha(contacto);
        } else if (item instanceof MensajeCoincidencia) {
            MensajeCoincidencia coincidencia = (MensajeCoincidencia) item;
            esEnviado = coincidencia.esEmisor(getTelefonoUsuario());
            contenido = coincidencia.getContenido();
            fecha = coincidencia.getFechaEnvio();
        } else {
            return info;
        }
        info.setEsMensajeEnviado(esEnviado);
        // Procesar el contenido según su tipo
        if (contenido instanceof String) {
            info.setTextoFormateado(formatearMensajeParaVista((String) contenido));
        } else if (contenido instanceof Integer) {
            info.setEmojiRedimensionado(obtenerEmojiRedimensionado((Integer) contenido));
        }
        
        // Formatear la fecha
        info.setHoraFormateada(formatearFechaMensaje(fecha));
        
        return info;
    }
    
    private boolean esUltimoMensajeEnviado(Contacto item) {
    	Mensaje m = this.sesionUsuario.getUltimoChatMensaje(item);
		return m.esEmisor(getTelefonoUsuario());
	}

	/**
     * Formatea un mensaje de texto para la vista (trunca si es muy largo)
     */
    public String formatearMensajeParaVista(String mensajeTexto) {
        if (mensajeTexto == null) {
            return "";
        }
        
        if (mensajeTexto.length() > MAX_MESSAGE_PREVIEW_LENGTH) {
            return mensajeTexto.substring(0, MAX_MESSAGE_PREVIEW_LENGTH - 1) + "...";
        }
        return mensajeTexto;
    }
    
    /**
     * Formatea la fecha de un mensaje para mostrar solo la hora
     */
    public String formatearFechaMensaje(LocalDateTime fecha) {
        if (fecha != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
            return fecha.format(formatter);
        }
        return "";
    }
    
    /**
     * Redimensiona un emoji para la vista
     */
    public ImageIcon obtenerEmojiRedimensionado(Integer emojiCode) {
        if (emojiCode == null) {
            return null;
        }
        
        ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
        if (emojiIcon != null) {
            Image img = emojiIcon.getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        }
        return null;
    }
    
    /**
     * Formatea el saludo de un contacto para la vista
     */
    public String formatearSaludoVista(Contacto contacto) {
        final int MAX_SALUDO_LENGTH = 40;
        
        if (contacto.getTipoContacto() == TipoContacto.INDIVIDUAL) {
            String saludo = ((ContactoIndividual) contacto).getSaludo();
            if (saludo != null && !saludo.isEmpty()) {
                // Truncar si es muy largo
                if (saludo.length() > MAX_SALUDO_LENGTH) {
                    saludo = saludo.substring(0, MAX_SALUDO_LENGTH) + "...";
                }
                return saludo;
            }
        }
        return "";
    }
    
    /**
     * Genera las burbujas de chat para mostrar en la interfaz
     * 
     * @param contacto Contacto del chat
     * @param chat Panel donde se mostrarán las burbujas
     * @return Lista de burbujas de texto
     */
    public List<BubbleText> pintarMensajesBurbuja(Contacto contacto, JPanel chat) {
        List<Mensaje> mensajes = obtenerChatContacto(contacto);
        List<BubbleText> burbujas = new ArrayList<>();

        if (mensajes == null || mensajes.isEmpty()) {
            return burbujas;
        }

        String telefonoUsuario = getTelefonoUsuario();

        for (Mensaje mensaje : mensajes) {
            Object contenido = (mensaje.getEmoticono() == -1) ? 
                    mensaje.getTexto() : mensaje.getEmoticono();

            boolean esUsuario = mensaje.esEmisor(telefonoUsuario);
            String autor = determinarAutorMensaje(contacto, mensaje, esUsuario);
            int tipoMensaje = esUsuario ? BubbleText.SENT : BubbleText.RECEIVED;

            BubbleText bubble = (contenido instanceof String)
                ? new BubbleText(chat, (String) contenido, Color.WHITE, autor, tipoMensaje, 14)
                : new BubbleText(chat, (Integer) contenido, Color.WHITE, autor, tipoMensaje, 14);

            burbujas.add(bubble);
        }

        return burbujas;
    }

    
    /**
     * Método auxiliar para determinar el autor a mostrar en la burbuja
     */
    private String determinarAutorMensaje(Contacto contacto, Mensaje mensaje, boolean esUsuario) {
        String autor;
        
        if (esUsuario) {
            autor = getNombreUsuario();
            if (mensaje.getIDGrupo() != -1 && !mensaje.getReceptorTelf().equals(getTelefonoUsuario())) {
                autor += " <G> *" + mensaje.getNombreGrupo() + "*";
            }
        } else {
        	autor = contacto.getNombre();
    
        }
        
        return autor += " <> " + formatearFechaMensaje(mensaje.getFechaEnvio());
        
    }

    public int ubicarMensaje(Contacto c, Mensaje mObjetivo) {
        List<Mensaje> mensajes = obtenerChatContacto(c);

        int i = 0;
        // Recorremos mientras queden elementos
        while (i < mensajes.size()) {
            Mensaje m = mensajes.get(i);
            // Comparamos valores de código (primitivos) y salimos en cuanto coincidamos
            if (m.getCodigo() == mObjetivo.getCodigo()) {
                return i;
            }
            i++;
        }

        // Si acabamos el bucle sin retorno, no lo encontramos
        return -1;
    }

	

   
    
    
    
}
