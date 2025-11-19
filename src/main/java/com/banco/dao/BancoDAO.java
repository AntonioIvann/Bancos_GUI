//Clase BancoDAO ((Data Access Object) para gestionar los querys de bancos)

package com.banco.dao;

import com.banco.config.ConexionBD;
import com.banco.modelo.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDAO {
    //Método para crear un banco nuevo
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
    //Método para obtener un banco por su ID
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
    //Método para obtener todos los bancos registrados
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
    //Método para actualizar un banco existente
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
    //Método para eliminar un banco por su ID
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
    //Método auxiliar para mapear los datos obtenidos de la base de datos a un objeto Banco
    private Banco mapeoBanco(ResultSet rs) throws SQLException {
        Banco banco = new Banco();
        banco.setIdb(rs.getInt("idb"));
        banco.setNombre(rs.getString("nombre"));
        return banco;
    }
}
