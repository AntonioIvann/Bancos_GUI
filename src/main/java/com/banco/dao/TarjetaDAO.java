package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Tarjeta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarjetaDAO {

    public int crear(Tarjeta tarjeta) {
        String sql = "INSERT INTO tarjetas (idc, idb, numero_tarjeta, saldo) VALUES (?, ?, ?, ?) RETURNING idt";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, tarjeta.getIdc());
            ps.setInt(2, tarjeta.getIdb());
            ps.setString(3, tarjeta.getNumeroTarjeta());
            ps.setDouble(4, tarjeta.getSaldo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Tarjeta obtenerPorId(int idt) {
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE idt = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoTarjeta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Tarjeta obtenerPorNumero(String numeroTarjeta) {
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE numero_tarjeta = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, numeroTarjeta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoTarjeta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tarjeta> obtenerPorCliente(int idc) {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE idc = ? ORDER BY fecha_creacion DESC";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tarjetas.add(mapeoTarjeta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarjetas;
    }

    public List<Tarjeta> obtenerTodas() {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas ORDER BY fecha_creacion DESC";
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tarjetas.add(mapeoTarjeta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarjetas;
    }

    public boolean actualizar(Tarjeta tarjeta) {
        String sql = "UPDATE tarjetas SET saldo = ?, activa = ? WHERE idt = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, tarjeta.getSaldo());
            ps.setBoolean(2, tarjeta.isActiva());
            ps.setInt(3, tarjeta.getIdt());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idt) {
        String sql = "DELETE FROM tarjetas WHERE idt = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idt);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Tarjeta mapeoTarjeta(ResultSet rs) throws SQLException {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdt(rs.getInt("idt"));
        tarjeta.setIdc(rs.getInt("idc"));
        tarjeta.setIdb(rs.getInt("idb"));
        tarjeta.setNumeroTarjeta(rs.getString("numero_tarjeta"));
        tarjeta.setSaldo(rs.getDouble("saldo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            tarjeta.setFechaCreacion(timestamp.toLocalDateTime());
        }
        
        tarjeta.setActiva(rs.getBoolean("activa"));
        return tarjeta;
    }
}
