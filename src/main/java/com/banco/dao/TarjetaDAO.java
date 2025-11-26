//Clase TarjetaDAO (Data Access Object) para gestionar los querys de tarjetas

//Paquete com.banco.dao
package com.banco.dao;

//Librerias
//Para obtener conexiones a la base de datos
import com.banco.config.ConexionBD;
//Modelo tarjeta
import com.banco.modelo.Tarjeta;
//Clases JDBC, conection, PreparedStatement, ResultSet, etc
import java.sql.*;
import java.time.LocalDateTime;
//Arreglo ArrayList
import java.util.ArrayList;
//Arreglo List
import java.util.List;

//Clase TarjetaDA0
public class TarjetaDAO {
    //Metodo para crear una nueva tarjeta y lo inserta en la base de datos
    public int crear(Tarjeta tarjeta) {
        //Prepara un insert para guardar una tarjeta, hace que la base de datos devuelva el ID recien creado sin tener que hacer una consulta
        String sql = "INSERT INTO tarjetas (idc, idb, numero_tarjeta, saldo) VALUES (?, ?, ?, ?) RETURNING idt";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Asignar valores al query
            ps.setInt(1, tarjeta.getIdc());
            ps.setInt(2, tarjeta.getIdb());
            ps.setString(3, tarjeta.getNumeroTarjeta());
            ps.setDouble(4, tarjeta.getSaldo());

            //Obtener el ID creado
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //Devuelve y lee el ID de la tarjeta creada
                    return rs.getInt("idt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
        //Si falla, devuelve -1
    }

    //Metodo para obtiener los datos de una tarjeta por su ID
    public Tarjeta obtenerPorId(int idt) {
        //Busca una tarjeta por su ID. con sus respectivos valores
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE idt = ?";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            //Inserta ID en el WHERE
            ps.setInt(1, idt);
            //Ejecuta el query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoTarjeta(rs);
                    //Convierte el ResultSet en Objeto Tarjeta usando mapeoTarjeta
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        //Devuelve null
    }

    //Metodo para obtener los datos de una tarjeta por su numero
    public Tarjeta obtenerPorNumero(String numeroTarjeta) {
        //Busca una tarjeta por sus respectivos ID. con sus respectivos valores
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE numero_tarjeta = ?";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Aqui se pasa el numero de tarjeta del usuario que estamos buscando
            ps.setString(1, numeroTarjeta);
            //Ejecuta el query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapeoTarjeta(rs);
                    //Convierte el ResultSet en Objeto Tarjeta usando mapeoTarjeta
                }
            }
        } catch (SQLException e) {
            // Si ocurre un error relacionado con la base de datos,
            e.printStackTrace();
            // se captura la excepción y se imprime el detalle del error.
        }
        return null;
        //Devuelve null
    }

    //Metodo para obtener todas las tarjetas de un cliente
    public List<Tarjeta> obtenerPorCliente(int idc) {
        List<Tarjeta> tarjetas = new ArrayList<>();
        //Crea un arreglo de tipo tarjeta. para almacenar las tarjetas
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas WHERE idc = ? ORDER BY fecha_creacion DESC";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tarjetas.add(mapeoTarjeta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // se captura la excepción y se imprime el detalle del error.
        }
        return tarjetas;
        //Devuelve tarjetas
    }

    //Metodo para obtener todas las tarjetas registradas en el sistema
    public List<Tarjeta> obtenerTodas() {
        //Crea un arreglo de tipo tarjeta para almacenar las tarjetas
        List<Tarjeta> tarjetas = new ArrayList<>();
        //Consulta que devuelve todas las tarjetas que tiene un cliente, ordenadas por fecha_creacion_ DESC (la mas nueva primero)
        String sql = "SELECT idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa FROM tarjetas ORDER BY fecha_creacion DESC";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                //Cada fila de la BD se convierte en un objeto Tarjeta y se agrega a la lista
                tarjetas.add(mapeoTarjeta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // se captura la excepción y se imprime el detalle del error.
        }
        return tarjetas;
        //Devuelve tarjetas
    }

    //Metodo para actualizar los datos de una tarjeta existente
    public boolean actualizar(Tarjeta tarjeta) {
        //Consulta para actualizar el saldo y si esta activa (si esta bloqueada o habilitada/deshabilitada)
        String sql = "UPDATE tarjetas SET saldo = ?, activa = ? WHERE idt = ?";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            //Asignar valores a la tarjeta
            ps.setDouble(1, tarjeta.getSaldo());
            ps.setBoolean(2, tarjeta.isActiva());
            ps.setInt(3, tarjeta.getIdt());

            return ps.executeUpdate() > 0;
            //Devuelve true si actualizo al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            // se captura la excepción y se imprime el detalle del error.
            return false;
            //Devuelve falso
        }
    }

    //Metodo para eliminar una tarjeta por su ID
    public boolean eliminar(int idt) {
        //Consulta para borrar por su ID, regresa true si la tarjeta se elimino
        String sql = "DELETE FROM tarjetas WHERE idt = ?";
        //Obtiene una conexion a la base de datos
        try (Connection conexion = ConexionBD.obtenerConexion();
            //Crea un PreparedStatement que evita un SQL injection, y permite pasar parametros de forma segura
             PreparedStatement ps = conexion.prepareStatement(sql)) {

        //Aqui se pasa el ID que estamos buscando
            ps.setInt(1, idt);
            return ps.executeUpdate() > 0;
            //Devuelve true si actualizo al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            // se captura la excepción y se imprime el detalle del error.
            return false;
            //Devuelve falso
        }
    }

    //Metodo para mapear e imprimir los datos de una tarjeta
    //Convierte una fila de la base de datos en una instancia de Tarjeta
    private Tarjeta mapeoTarjeta(ResultSet rs) throws SQLException {
        //Creacion del objeto tarjeta
        Tarjeta tarjeta = new Tarjeta();
        //Asignar valores a la tarjeta
        tarjeta.setIdt(rs.getInt("idt"));
        tarjeta.setIdc(rs.getInt("idc"));
        tarjeta.setIdb(rs.getInt("idb"));
        tarjeta.setNumeroTarjeta(rs.getString("numero_tarjeta"));
        tarjeta.setSaldo(rs.getDouble("saldo"));
        
        //Procesar fecha de creacion
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            tarjeta.setFechaCreacion(timestamp.toLocalDateTime());
        }
        //Convierte TimeStamp de SQL a LOcalDateTime de Java

        //Define si la tarjeta esta habilitada
        tarjeta.setActiva(rs.getBoolean("activa"));
        return tarjeta;
        //Retorna la tarjeta ya mapeada
    }
}
