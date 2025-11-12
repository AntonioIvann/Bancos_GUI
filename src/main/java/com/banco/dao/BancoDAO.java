package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDAO {

    public int crear(Banco banco) {
        String sql = "INSERT INTO bancos (nombre) VALUES (?) RETURNING idb";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, banco.getNombre());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idb");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Banco obtenerPorId(int idb) {
        String sql = "SELECT idb, nombre FROM bancos WHERE idb = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idb);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoBanco(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Banco> obtenerTodos() {
        List<Banco> bancos = new ArrayList<>();
        String sql = "SELECT idb, nombre FROM bancos ORDER BY nombre";
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bancos.add(mapeoBanco(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bancos;
    }

    public boolean actualizar(Banco banco) {
        String sql = "UPDATE bancos SET nombre = ? WHERE idb = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, banco.getNombre());
            ps.setInt(2, banco.getIdb());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idb) {
        String sql = "DELETE FROM bancos WHERE idb = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idb);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Banco mapeoBanco(ResultSet rs) throws SQLException {
        Banco banco = new Banco();
        banco.setIdb(rs.getInt("idb"));
        banco.setNombre(rs.getString("nombre"));
        return banco;
    }
}
