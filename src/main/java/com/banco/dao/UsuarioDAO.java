package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Usuario;
import com.banco.modelo.Cliente;
import java.sql.*;
import java.time.LocalDateTime;

public class UsuarioDAO {

    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, contraseña, idc, es_admin) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContraseña());
            ps.setInt(3, usuario.getCliente().getIdc());
            ps.setBoolean(4, usuario.isEsAdmin());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerPorUsuario(String usuario) {
        String sql = "SELECT u.idu, u.usuario, u.contraseña, u.idc, u.es_admin, u.activo, u.fecha_creacion, " +
                     "c.idc, c.apellido_paterno, c.apellido_materno, c.nombre " +
                     "FROM usuarios u " +
                     "LEFT JOIN clientes c ON u.idc = c.idc " +
                     "WHERE u.usuario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean usuarioExiste(String usuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Usuario mapeoUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdu(rs.getInt("idu"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setContraseña(rs.getString("contraseña"));

        Cliente cliente = new Cliente();
        cliente.setIdc(rs.getInt("idc"));
        cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
        cliente.setApellidoMaterno(rs.getString("apellido_materno"));
        cliente.setNombre(rs.getString("nombre"));
        usuario.setCliente(cliente);

        usuario.setEsAdmin(rs.getBoolean("es_admin"));
        usuario.setActivo(rs.getBoolean("activo"));

        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            usuario.setFechaCreacion(timestamp.toLocalDateTime());
        }

        return usuario;
    }
}
