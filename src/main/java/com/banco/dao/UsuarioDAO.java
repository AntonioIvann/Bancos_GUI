//Clase UsuarioDAO (Data Access Object) para gestionar los querys de usuarios

package com.banco.dao;

//Librerias

//Obtener conexiones SQL via JDBC
import com.banco.config.ConexionBD;
//MOdelos para mapear los datos del usuario
import com.banco.modelo.Usuario;
//Modelos para mapear los datos del cliente
import com.banco.modelo.Cliente;
//Manejar PreparedStatement, ResultSet, Connection, etc.
import java.sql.*;
//Manejar fechas de creacion del usuario
import java.time.LocalDateTime;

//Clase UsuarioDA0
public class UsuarioDAO {
    //Metodo para crear un nuevo usuario y devuelve su ID
    public boolean registrarUsuario(Usuario usuario) {
        //Define la consulta SQL para insertar un usuario nuevo
        String sql = "INSERT INTO usuarios (usuario, contraseña, idc, es_admin) VALUES (?, ?, ?, ?)";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
             //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //LLenar los parametros
            //Parametros del usuario
            ps.setString(1, usuario.getUsuario());
            //Parametros de la contraseña
            ps.setString(2, usuario.getContraseña());
            //Parametros del Idc
            ps.setInt(3, usuario.getCliente().getIdc());
            //Parametros si es admin
            ps.setBoolean(4, usuario.isEsAdmin());

            //Devuelve cuantas filas fueron modificadas/actualizadas.
            return ps.executeUpdate() > 0; //Si es mayor a 0, el insert es exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            //Si falla, se captura la excepcion y devuelve false
        }
    }
    //Metodo para obtener los datos de un usuario por su ID
    public Usuario obtenerPorUsuario(String usuario) {
        //Busca un usuario por su nombre
        //Devuelve todo: usuario mas su cliente asociado
        String sql = "SELECT u.idu, u.usuario, u.contraseña, u.idc, u.es_admin, u.activo, u.fecha_creacion, " +
                     "c.idc, c.apellido_paterno, c.apellido_materno, c.nombre " +
                     "FROM usuarios u " +
                     //Une datos de la tabla clientes
                     "LEFT JOIN clientes c ON u.idc = c.idc " +
                     "WHERE u.usuario = ?";
                     //Si el cliente existe, lo trae, si no solo trae al usuario

        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
        //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Aqui se pasa el nombre del usuario que estamos buscando
            ps.setString(1, usuario);

            //Si encuentra un registro manda el ResultSet al metodo MapeoUsuario que se encarga de crear el objeto Usuario con los datos
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        //Si no encuentra nada, devuelve null
    }
    //Metodo para obtiener todos los usuarios registrados en el sistema, para evitar duplicados
    public boolean usuarioExiste(String usuario) {
        //Hace una consulta donde, pregunta cuantos usuarios existen con ese nombre
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Aqui se pasa el nombre del usuario que estamos buscando
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                //Obtiene el numero de coincidencias
                if (rs.next()) {
                    //Si es mayor a 0, el usuario ya existe
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        //Si no encuentra nada, devuelve null
    }
    //Metodo para mapear e imprimir los datos del usuario realzados en la base de datos
    private Usuario mapeoUsuario(ResultSet rs) throws SQLException {
        //Crear el objeto Usuario y darle valores
        Usuario usuario = new Usuario();
        usuario.setIdu(rs.getInt("idu"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setContraseña(rs.getString("contraseña"));

        //Crear el objeto Cliente y darle valores
        Cliente cliente = new Cliente();
        cliente.setIdc(rs.getInt("idc"));
        cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
        cliente.setApellidoMaterno(rs.getString("apellido_materno"));
        cliente.setNombre(rs.getString("nombre"));
        usuario.setCliente(cliente);

        //Campos adicionales del usuario
        usuario.setEsAdmin(rs.getBoolean("es_admin"));
        usuario.setActivo(rs.getBoolean("activo"));

        //Procesar fecha de creacion
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            usuario.setFechaCreacion(timestamp.toLocalDateTime());
        }
        //Convierte TimeStamp de SQL a LOcalDateTime de Java

        return usuario;
        //Retorna el usuario ya mapeado.
    }
}
