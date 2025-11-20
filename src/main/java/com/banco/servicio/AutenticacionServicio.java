package com.banco.servicio;

//Librerias
import com.banco.dao.UsuarioDAO;
import com.banco.dao.ClienteDAO;
import com.banco.modelo.Usuario;
import com.banco.modelo.Cliente;
import com.banco.utilidad.EncriptadorPassword;

//Clase AutenticacionServicio
public class AutenticacionServicio {
    //Atributos
    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO;
    private Usuario usuarioActual;

    //Constructor
    public AutenticacionServicio() {
        //Inicializar los DA0s
        this.usuarioDAO = new UsuarioDAO();
        this.clienteDAO = new ClienteDAO();
    }

    //Metodo login (Inicia sesion si el usuario existe y la constraseña es correcta)
    public boolean login(String nombreUsuario, String contraseña) {
        //Buscar el usuario en la BD por nombre
        Usuario usuario = usuarioDAO.obtenerPorUsuario(nombreUsuario);
        //Verificar la contraseña usando el encriptador
        if (usuario != null && EncriptadorPassword.verificar(contraseña, usuario.getContraseña())) {
            //Si coincide, guarda el usuario como sesion actual
            this.usuarioActual = usuario;
            //Devuelve true si el login fue exitoso
            return true;
        }
        //Devuelve false si el login fallo
        return false;
    }

    //Metodo registrarse (Registra un nuevo cliente y un nuevo usuario en el sistema)
    public boolean registrase(String nombreUsuario, String contraseña, String apellidoPaterno, 
                             String apellidoMaterno, String nombre, boolean esAdmin) {
        // Validar que el usuario no exista
        if (usuarioDAO.usuarioExiste(nombreUsuario)) {
            return false;
        }
        //Encriptar la contraseña
        String contraseñaEncriptada = EncriptadorPassword.encriptar(contraseña);
        //Si la encriptacion falla, devuelve falso.
        if (contraseñaEncriptada == null) {
            return false;
        }

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setApellidoPaterno(apellidoPaterno);
        cliente.setApellidoMaterno(apellidoMaterno);
        cliente.setNombre(nombre);

        //Guarda el cliente en la base de datos
        int idCliente = clienteDAO.crear(cliente);
        //Si no se pudo guardar, devuelve falso
        if (idCliente <= 0) {
            return false;
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setUsuario(nombreUsuario);
        usuario.setContraseña(contraseñaEncriptada); // Usar contraseña encriptada
        cliente.setIdc(idCliente);
        usuario.setCliente(cliente);
        usuario.setEsAdmin(esAdmin);

        //Registra el usuario en la base de datos
        return usuarioDAO.registrarUsuario(usuario);
    }

    //Metodo para obtener usuario actual (devuelve el usuario que inicio sesion actualmente)
    public Usuario obtenerUsuarioActual() {
        //Devuelve el usuario actual
        return usuarioActual;
    }

    //Metodo para cerrar sesion
    public void cerrarSesion() {
        //Borra la sesion del usuario actual y la cierra
        this.usuarioActual = null;
    }

    //Metodo para validar si la sesion esta activa
    public boolean tieneSessionActiva() {
        //Devuelve "true", hay un usuario con sesion activa
        //Devuelve "false", no hay nadie logueado
        return usuarioActual != null;
    }
}
