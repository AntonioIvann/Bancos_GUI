//Clase MovimientoDAO(Data Access Object) para gestionar los querys de movimientos

//PAquete com.banco.dao
package com.banco.dao;

//Librerias

//Conexion para la base de datos
import com.banco.config.ConexionBD;
//Modelo Movimiento
import com.banco.modelo.Movimiento;
//Importa las clases JDBC necesarias, connection, PreparedStatemente, ResultSet, etc
import java.sql.*;
//Para convertir TimeStamp a LocalDateTime
import java.time.LocalDateTime;
//Librerias para usar arreglos
import java.util.ArrayList;
import java.util.List;

//Clase MovimientoDA0
public class MovimientoDAO {
    // Crear un nuevo movimiento y devuelve su ID
    public int crear(Movimiento movimiento) {
        //Consulta SQL para insertar un movimiento
        String sql = "INSERT INTO movimientos (idt, cantidad, tipo, descripcion) VALUES (?, ?, ?, ?) RETURNING idm";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Asignar valores
            ps.setInt(1, movimiento.getIdt());
            ps.setDouble(2, movimiento.getCantidad());
            ps.setString(3, movimiento.getTipo());
            ps.setString(4, movimiento.getDescripcion());

            //Ejecuta la consulta
            try (ResultSet rs = ps.executeQuery()) {

                //Si hay resultado, devuelve el idm nuevo
                if (rs.next()) {
                    return rs.getInt("idm");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //Imprime error si falta la consulta
        }
        return -1;
        //Devuelve -1 si ocurrio un error
    }

    // Obtiene los movimientos por ID de tarjeta de un cliente
    public List<Movimiento> obtenerPorTarjeta(int idt) {
        // Lista donde se guardarán los resultados
        List<Movimiento> movimientos = new ArrayList<>();
        // Consulta filtrada por idt
        String sql = "SELECT idm, idt, cantidad, tipo, descripcion, fecha_movimiento FROM movimientos WHERE idt = ? ORDER BY fecha_movimiento DESC";
        //Conexion y preparacion de la consulta
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Parametros idt de la tarjeta
            ps.setInt(1, idt);
            //Ejecuta el select
            try (ResultSet rs = ps.executeQuery()) {
                //Recorre las filas encontradas
                while (rs.next()) {
                    //Convierte la fila en un objeto movimiento
                    movimientos.add(mapeoMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //Imprime error si falta algo
        }
        return movimientos;
        //Devuelve la lista (vacia si no se encontraron movimientos)
    }

     //Obtiene todos los movimientos registrados en el sistema
    public List<Movimiento> obtenerTodos() {
        //Lista donde se guardan los movimientos
        List<Movimiento> movimientos = new ArrayList<>();
        //Consulta sin filtros
        String sql = "SELECT idm, idt, cantidad, tipo, descripcion, fecha_movimiento FROM movimientos ORDER BY fecha_movimiento DESC";
        //Statement porque no hay parametros en la consulta
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            //Recorre todos los movimientos encontrados
            while (rs.next()) {
                movimientos.add(mapeoMovimiento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //Imprime error en consola
        }
        return movimientos;
        //Devuelve lista completa
    }

    // Mapea e imprime los datos del movimiento realzado
    private Movimiento mapeoMovimiento(ResultSet rs) throws SQLException {
        //Crear objeto movimiento
        Movimiento movimiento = new Movimiento();
        //ASignar valores
        movimiento.setIdm(rs.getInt("idm"));
        movimiento.setIdt(rs.getInt("idt"));
        movimiento.setCantidad(rs.getDouble("cantidad"));
        movimiento.setTipo(rs.getString("tipo"));
        movimiento.setDescripcion(rs.getString("descripcion"));

        //Convertir de TimeStamp a LocalDateTime
        Timestamp timestamp = rs.getTimestamp("fecha_movimiento");
        if (timestamp != null) {
            movimiento.setFechaMovimiento(timestamp.toLocalDateTime());
        }

        return movimiento;
        //Devuelve el movimiento ya mapeado
    }
}
