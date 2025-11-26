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
    // "RETURNING idc" indica que tras insertar, la BD debe devolver el ID generado,
    // los signos de interrogación (?) son marcadores de posicion para los parámetros.
    String sql = "INSERT INTO clientes (apellido_paterno, apellido_materno, nombre) VALUES (?, ?, ?) RETURNING idc";
    // Inicio del bloque "try-with-resources".
    // Esto asegura que la 'conexion' y el 'ps' (PreparedStatement) se cierren automáticamente
    // al terminar, incluso si ocurre un error.
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Asignamos los valores a los marcadores de posición (?).
        // El número indica la posición del signo de interrogación (1, 2, 3).
        // Obtenemos los datos del objeto 'cliente' que recibimos como parámetro.
        ps.setString(1, cliente.getApellidoPaterno());
        ps.setString(2, cliente.getApellidoMaterno());
        ps.setString(3, cliente.getNombre());
        // Ejecutamos la consulta.
        // Usamos 'executeQuery' en lugar de 'executeUpdate' porque gracias al 
        // "RETURNING idc", la base de datos nos devolverá una tabla (ResultSet) con el ID.
        try (ResultSet rs = ps.executeQuery()) { 
            // Verificamos si el ResultSet tiene resultados (si la inserción fue exitosa).
            if (rs.next()) {
                // Si hay resultado, extraemos el valor entero de la columna "idc" y lo retornamos inmediatamente.
                return rs.getInt("idc");
            }
        }
        // Si algo falla (ej. base de datos caída, error de sintaxis), capturamos el error
        // e imprimimos la traza del error en la consola para depurar.
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Si llegamos aquí, significa que hubo un error o no se insertó nada y nos devuelve el siguiente numero del idc
    return -1;
}

    // Método para obtener un cliente por su ID
public Cliente obtenerPorId(int idc) {
    // Consulta SQL para obtener los datos del cliente según su idc.
    // El signo ? será reemplazado por el valor del parámetro idc.
    String sql = "SELECT idc, apellido_paterno, apellido_materno, nombre FROM clientes WHERE idc = ?";
    // Se usa try-with-resources para que la conexión y el PreparedStatement
    // se cierren automáticamente al finalizar.
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Se asigna el parámetro idc al primer signo ? de la consulta SQL.
        ps.setInt(1, idc);
        // Ejecuta la consulta y guarda el resultado en un ResultSet.
        try (ResultSet rs = ps.executeQuery()) {
            // Si existe un registro en la base de datos con ese ID
            if (rs.next()) {
                // se convierte ese registro en un objeto Cliente mediante el método mapeoCliente.
                return mapeoCliente(rs);
            }
        }
    // Si ocurre un error de SQL, se imprime la pila de errores.
    } catch (SQLException e) {
        e.printStackTrace();
    }
    // Si no se encontró un cliente con ese ID o ocurrió un error, retorna null.
    return null;
}

    // Método para obtener todos los clientes registrados
    public List<Cliente> obtenerTodos() {
    // Se crea una lista vacía donde se almacenarán los clientes obtenidos de la BD
    List<Cliente> clientes = new ArrayList<>();
    // Consulta SQL para obtener todos los clientes ordenados por nombre
    String sql = "SELECT idc, apellido_paterno, apellido_materno, nombre FROM clientes ORDER BY nombre";
    // Se usa try-with-resources para manejar automáticamente la conexión, el statement y el resultset
    try (Connection conexion = ConexionBD.obtenerConexion(); // Obtiene la conexión a la base de datos
         Statement stmt = conexion.createStatement();        // Crea un objeto Statement para ejecutar la consulta
         ResultSet rs = stmt.executeQuery(sql)) {            // Ejecuta la consulta SQL y guarda el resultado en rs
        // Recorre cada fila del resultado de la consulta
        while (rs.next()) {
            // Convierte la fila actual del ResultSet en un objeto Cliente y lo agrega a la lista
            clientes.add(mapeoCliente(rs));
        }
    } catch (SQLException e) {
        // En caso de error en la ejecución SQL, se imprime la traza del error
        e.printStackTrace();
    }
    // Retorna la lista completa de clientes obtenidos
    return clientes;
}

    // Método para actualizar los datos de un cliente existente
public boolean actualizar(Cliente cliente) {
    // Consulta SQL para actualizar los datos del cliente en la tabla "clientes".
    // Los signos ? se reemplazan después con los valores correspondientes.
    String sql = "UPDATE clientes SET apellido_paterno = ?, apellido_materno = ?, nombre = ? WHERE idc = ?";
    // Se usa try-with-resources para que la conexión y el PreparedStatement
    // se cierren automáticamente al finalizar el bloque.
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Se establece el valor para el primer parámetro (apellido_paterno)
        ps.setString(1, cliente.getApellidoPaterno());
        // Se establece el valor para el segundo parámetro (apellido_materno)
        ps.setString(2, cliente.getApellidoMaterno());
        // Se establece el valor para el tercer parámetro (nombre)
        ps.setString(3, cliente.getNombre());
        // Se establece el valor para el cuarto parámetro (idc) para identificar al cliente
        ps.setInt(4, cliente.getIdc());
        // executeUpdate() devuelve el número de filas afectadas por la actualización.
        // Si es mayor a 0, significa que sí se actualizó el registro.
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        // Si ocurre un error en la conexión o en la ejecución del SQL, se imprime el error.
        e.printStackTrace();
        // Y se devuelve false indicando que la actualización falló.
        return false;
    }
}

    // Método para eliminar un cliente por su ID
public boolean eliminar(int idc) {
    // Consulta SQL con parámetro para eliminar al cliente por su ID
    String sql = "DELETE FROM clientes WHERE idc = ?";
    // Uso de try-with-resources para abrir la conexión y el PreparedStatement
    // Esto asegura que los recursos se cierren automáticamente al finalizar
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Asigna el valor del parámetro idc al primer placeholder (?) del SQL
        ps.setInt(1, idc);
        // Ejecuta la operación DELETE y retorna true si al menos 1 fila fue afectada
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        // Si ocurre un error SQL, imprime la traza para depurar
        e.printStackTrace();
        // Retorna false indicando que la eliminación no se pudo realizar
        return false;
    }
}

    // Método privado para mapear un ResultSet a un objeto Cliente
private Cliente mapeoCliente(ResultSet rs) throws SQLException {
    // Se crea un nuevo objeto Cliente vacío
    Cliente cliente = new Cliente();
    // Se asigna al cliente el valor entero de la columna 'idc' del ResultSet
    cliente.setIdc(rs.getInt("idc"));
    // Se asigna al cliente el valor de la columna 'apellido_paterno' como String
    cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
    // Se asigna al cliente el valor de la columna 'apellido_materno' como String
    cliente.setApellidoMaterno(rs.getString("apellido_materno"));
    // Se asigna al cliente el valor de la columna 'nombre' obtenido desde la BD
    cliente.setNombre(rs.getString("nombre"));
    // Finalmente se devuelve el objeto Cliente completamente lleno con los datos
    return cliente;
}
}