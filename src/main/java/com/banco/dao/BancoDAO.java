// Clase BancoDAO ((Data Access Object) para gestionar los querys de bancos)

package com.banco.dao;

// Importamos librerias sql para obtener datos de nuestra base de datos

import com.banco.config.ConexionBD;
import com.banco.modelo.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDAO {
    // Método para crear un banco nuevo, definimos el método público, devuelve un entero y recibe un objeto 'Banco'
public int crear(Banco banco) {
    // Define la consulta SQL, 'INSERT INTO bancos (nombre)' es para insertar datos en la tabla, 
    // 'VALUES (?)': El '?' ees un marcador de posicion de insercion de datos, 'RETURNING idb' Pide a la BD que devuelva el ID generado tras insertar.
    String sql = "INSERT INTO bancos (nombre) VALUES (?) RETURNING idb";
    
    // Try-with-resources, esta estructura asegura que la 'Connection' y el 'PreparedStatement' se cierren automáticamente
    // al terminar, incluso si ocurre un error. Evita fugas de memoria.
    try (Connection conexion = ConexionBD.obtenerConexion(); // Obtiene la conexión a la BD
         PreparedStatement ps = conexion.prepareStatement(sql)) { // Prepara el estado de conexion
        // Asignar valores a los parámetros, reemplaza el primer '?' (índice 1) con el nombre que viene dentro del objeto 'banco'.
        ps.setString(1, banco.getNombre());
        // Ejecutar la consulta.
        // Se usa 'executeQuery' en lugar de 'executeUpdate' porque gracias al 'RETURNING idb',
        // la consulta devolverá un resultado (el ID), como si fuera un SELECT.
        // También usamos try-with-resources aquí para cerrar el ResultSet automáticamente.
        try (ResultSet rs = ps.executeQuery()) {
            // Verificar si hay resultados.
            // rs.next() mueve el cursor a la primera fila del resultado.
            if (rs.next()) {
                // Si hay fila, extraemos el valor de la columna "idb" (el ID generado) y lo retornamos.
                return rs.getInt("idb");
            }
        }
    } catch (SQLException e) {
        // Manejo de errores.
        // Si algo falla (BD caída, error de sintaxis, etc.), se captura la excepción.
        e.printStackTrace(); // Imprime el error en la consola para depurar.
    }
    // Retorno por defecto.
    // Si hubo un error o no se generó el ID, devolvemos -1 para indicar fallo.
    return -1;
}

    //Método para obtener un banco por su ID
    public Banco obtenerPorId(int idb) { 
    // Método que recibe un ID y devuelve un objeto Banco correspondiente, si existe.
    String sql = "SELECT idb, nombre FROM bancos WHERE idb = ?";
    // Consulta SQL con parámetro (?) para evitar inyección SQL.
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Se abre una conexión a la base de datos y se prepara la sentencia SQL.
        // try-with-resources asegura que ambos recursos se cierren automáticamente.
        ps.setInt(1, idb);
        // Se asigna el valor del parámetro a la consulta en la posición 1 (primer ?).
        try (ResultSet rs = ps.executeQuery()) {
            // Se ejecuta la consulta y se obtiene el resultado dentro de un ResultSet.
            // También se usa try-with-resources para cerrarlo automáticamente.
            if (rs.next()) {
                // Si existe al menos un registro en el resultado...
                return mapeoBanco(rs);
                // Se llama al método que convierte el ResultSet en un objeto Banco.
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // En caso de error SQL, se imprime el stack trace para depuración.
    }
    return null;
    // Si no se encontró ningún banco o hubo error, se devuelve null.
}

// Método que obtiene todos los bancos de la base de datos
public List<Banco> obtenerTodos() {
    // Se crea una lista vacía donde se guardarán los objetos Banco obtenidos
    List<Banco> bancos = new ArrayList<>();
    // Consulta SQL para seleccionar idb y nombre de la tabla 'bancos', ordenados por nombre
    String sql = "SELECT idb, nombre FROM bancos ORDER BY nombre";
    // Se inicia un bloque try-with-resources para manejar automáticamente los recursos de BD
    try (Connection conexion = ConexionBD.obtenerConexion(); // Obtiene una conexión a la base de datos
         Statement stmt = conexion.createStatement();        // Crea un Statement para ejecutar la consulta
         ResultSet rs = stmt.executeQuery(sql)) {            // Ejecuta la consulta SQL y obtiene el resultado
        // Recorre todas las filas del ResultSet
        while (rs.next()) {
            // Convierte la fila actual del ResultSet en un objeto Banco usando mapeoBanco
            bancos.add(mapeoBanco(rs));
        }
    } catch (SQLException e) {
        // Imprime el error si ocurre una excepción SQL
        e.printStackTrace();
    }
    // Retorna la lista final con todos los bancos obtenidos
    return bancos;
}

    //Método para actualizar un banco existente
public boolean actualizar(Banco banco) {
    // Sentencia SQL que actualiza el nombre donde coincida el ID
    String sql = "UPDATE bancos SET nombre = ? WHERE idb = ?";
    // Se utiliza try-with-resources para cerrar automáticamente la conexión y el PreparedStatement
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Se asigna el valor del nuevo nombre al primer parámetro "?"
        ps.setString(1, banco.getNombre());
        // Se asigna el id del banco al segundo parámetro "?"
        ps.setInt(2, banco.getIdb());
        // Ejecuta la actualización.
        // executeUpdate() devuelve el número de filas afectadas.
        // Si es mayor que 0, significa que se actualizó correctamente.
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        // En caso de error en la base de datos, imprime el error en consola
        e.printStackTrace();
        // Y devuelve false indicando que la operación falló
        return false;
    }
}

    //Método para eliminar un banco por su ID
public boolean eliminar(int idb) {
    // Se define la consulta SQL. El '?' es un marcador de posición (wildcard) que se sustituirá más adelante de forma segura.
    String sql = "DELETE FROM bancos WHERE idb = ?";
    // Inicio del bloque 'try-with-resources'.
    // Obtiene la conexión llamando al método estático 'obtenerConexion' de la clase 'ConexionBD'.
    // Prepara la sentencia SQL (ps) utilizando esa conexión.
    // Al usar este bloque, Java cerrará automáticamente la conexión y el PreparedStatement al finalizar, liberando recursos.
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        // Sustituye el primer '?' de la consulta SQL (índice 1) con el valor de la variable 'idb'.
        ps.setInt(1, idb);
        // Ejecuta la orden en la base de datos. 
        // 'executeUpdate()' devuelve el número de filas afectadas.
        // Si es > 0, significa que se borró algo y retorna 'true'. Si es 0, no existía el ID y retorna 'false'.
        return ps.executeUpdate() > 0;
        // Si ocurre cualquier error de base de datos (conexión caída, error de sintaxis, tabla no existe, etc.), se captura aquí.
    } catch (SQLException e) {
        // Imprime el detalle del error en la consola para poder depurar (corregir) el problema.
        e.printStackTrace();
        // Retorna 'false' indicando que la operación de eliminación falló.
        return false;
    }
}

    //Método auxiliar para mapear los datos obtenidos de la base de datos a un objeto Banco
    // Define un método privado que devuelve un objeto 'Banco' y recibe un 'ResultSet' (el resultado de la consulta SQL).
// "throws SQLException" indica que si hay un error leyendo la base de datos, este método lanzará esa excepción hacia arriba.
private Banco mapeoBanco(ResultSet rs) throws SQLException {
    // Crea una nueva instancia vacía de la clase 'Banco' para empezar a llenarla con datos.
    Banco banco = new Banco();
    // Obtiene el entero de la columna "idb" del ResultSet.
    // Usa el método setter (setIdb) para guardar ese valor en el objeto 'banco'.
    banco.setIdb(rs.getInt("idb"));
    // Obtiene la cadena de texto de la columna "nombre" del ResultSet.
    // Usa el método setter (setNombre) para guardar ese valor en el objeto 'banco'.
    banco.setNombre(rs.getString("nombre"));
    // Devuelve el objeto 'banco' ya lleno con la información de la base de datos para ser usado.
    return banco;
}
}