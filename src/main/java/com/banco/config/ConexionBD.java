//Clase para conexión a la base de datos

package com.banco.config;
//Importación de clases necesarias para la conexión a la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    //Constantes necesarias para la conexión, URL, usuario y contraseña
    private static final String URL = "jdbc:postgresql://localhost:5432/bancos_sistemas";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "123456";
    //Método auxiliar para cargar la clase de conexión, evitando bucles al intentar establecer la conexión
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
    }
    //Método para cerrar la conexión a la base de datos
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
