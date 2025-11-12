package com.banco.servicio;

import com.banco.dao.UsuarioDAO;
import com.banco.dao.ClienteDAO;
import com.banco.modelo.Usuario;
import com.banco.modelo.Cliente;
import com.banco.utilidad.EncriptadorPassword;

public class AutenticacionServicio {
    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO;
    private Usuario usuarioActual;

    public AutenticacionServicio() {
        this.usuarioDAO = new UsuarioDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public boolean login(String nombreUsuario, String contraseña) {
        Usuario usuario = usuarioDAO.obtenerPorUsuario(nombreUsuario);
        if (usuario != null && EncriptadorPassword.verificar(contraseña, usuario.getContraseña())) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public boolean registrase(String nombreUsuario, String contraseña, String apellidoPaterno, 
                             String apellidoMaterno, String nombre, boolean esAdmin) {
        // Validar que el usuario no exista
        if (usuarioDAO.usuarioExiste(nombreUsuario)) {
            return false;
        }

        String contraseñaEncriptada = EncriptadorPassword.encriptar(contraseña);
        if (contraseñaEncriptada == null) {
            return false;
        }

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setApellidoPaterno(apellidoPaterno);
        cliente.setApellidoMaterno(apellidoMaterno);
        cliente.setNombre(nombre);

        int idCliente = clienteDAO.crear(cliente);
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

        return usuarioDAO.registrarUsuario(usuario);
    }

    public Usuario obtenerUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public boolean tieneSessionActiva() {
        return usuarioActual != null;
    }
}
