//Clase ClienteDAO(Data Access Object) para gestionar los querys de clientes

package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    // Método para crear un nuevo cliente en la base de datos
    public int crear(Cliente cliente) {
        String sql = "INSERT INTO clientes (apellido_paterno, apellido_materno, nombre) VALUES (?, ?, ?) RETURNING idc";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getApellidoPaterno());
            ps.setString(2, cliente.getApellidoMaterno());
            ps.setString(3, cliente.getNombre());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idc");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    // Método para obtener un cliente por su ID
    public Cliente obtenerPorId(int idc) {
        String sql = "SELECT idc, apellido_paterno, apellido_materno, nombre FROM clientes WHERE idc = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Método para obtener todos los clientes registrados
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT idc, apellido_paterno, apellido_materno, nombre FROM clientes ORDER BY nombre";
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapeoCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
    // Método para actualizar los datos de un cliente existente
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET apellido_paterno = ?, apellido_materno = ?, nombre = ? WHERE idc = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getApellidoPaterno());
            ps.setString(2, cliente.getApellidoMaterno());
            ps.setString(3, cliente.getNombre());
            ps.setInt(4, cliente.getIdc());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Método para eliminar un cliente por su ID  
    public boolean eliminar(int idc) {
        String sql = "DELETE FROM clientes WHERE idc = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Método privado para mapear un ResultSet a un objeto Cliente
    private Cliente mapeoCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdc(rs.getInt("idc"));
        cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
        cliente.setApellidoMaterno(rs.getString("apellido_materno"));
        cliente.setNombre(rs.getString("nombre"));
        return cliente;
    }
}
