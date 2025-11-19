//Clase MovimientoDAO(Data Access Object) para gestionar los querys de movimientos

package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Movimiento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {
    // Crear un nuevo movimiento y devuelve su ID
    public int crear(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (idt, cantidad, tipo, descripcion) VALUES (?, ?, ?, ?) RETURNING idm";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, movimiento.getIdt());
            ps.setDouble(2, movimiento.getCantidad());
            ps.setString(3, movimiento.getTipo());
            ps.setString(4, movimiento.getDescripcion());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idm");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    // Obtiene los movimientos por ID de tarjeta de un cliente
    public List<Movimiento> obtenerPorTarjeta(int idt) {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT idm, idt, cantidad, tipo, descripcion, fecha_movimiento FROM movimientos WHERE idt = ? ORDER BY fecha_movimiento DESC";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idt);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapeoMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimientos;
    }
     // Obtiene todos los movimientos registrados en el sistema
    public List<Movimiento> obtenerTodos() {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT idm, idt, cantidad, tipo, descripcion, fecha_movimiento FROM movimientos ORDER BY fecha_movimiento DESC";
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movimientos.add(mapeoMovimiento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimientos;
    }
    // Mapea e imprime los datos del movimiento realzado
    private Movimiento mapeoMovimiento(ResultSet rs) throws SQLException {
        Movimiento movimiento = new Movimiento();
        movimiento.setIdm(rs.getInt("idm"));
        movimiento.setIdt(rs.getInt("idt"));
        movimiento.setCantidad(rs.getDouble("cantidad"));
        movimiento.setTipo(rs.getString("tipo"));
        movimiento.setDescripcion(rs.getString("descripcion"));

        Timestamp timestamp = rs.getTimestamp("fecha_movimiento");
        if (timestamp != null) {
            movimiento.setFechaMovimiento(timestamp.toLocalDateTime());
        }

        return movimiento;
    }
}
