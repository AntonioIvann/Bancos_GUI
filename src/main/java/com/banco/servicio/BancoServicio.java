package com.banco.servicio;

//Librerias
import com.banco.dao.BancoDAO;
import com.banco.modelo.Banco;
import java.util.List;

//Clase BancoServicio
public class BancoServicio {
    //Atributo para usar los metodos del BancoDA0
    private BancoDAO bancoDAO;

    //Constructor
    public BancoServicio() {
        //inicializar objeto de BancoDA0
        this.bancoDAO = new BancoDAO();
    }

    //Metodo crear banco
    public boolean crearBanco(String nombre) {
        //Crea un objeto banco usando el nombre recibido
        Banco banco = new Banco(nombre);
        //Devuelve true si se creo correctamente
        return bancoDAO.crear(banco) > 0;
    }

    //Metodo para obtener todos
    public List<Banco> obtenerTodos() {
        //Devuelve el List de todos los bancos almacenados en la base de datos
        return bancoDAO.obtenerTodos();
    }

    //Metodo para obtener por id
    public Banco obtenerPorId(int id) {
        //Devuelve y busca el banco por su id, si no existe devuelve null
        return bancoDAO.obtenerPorId(id);
    }
    
    //Metodo para actualizar banco
    //Recibe un objeto banco (ya modificado)
    public boolean actualizarBanco(Banco banco) {
        //Devuelve y actualiza el banco
        return bancoDAO.actualizar(banco);
    }
    
    //Metodo para eliminar un banco
    public boolean eliminarBanco(int idb) {
        // Devuelve el banco y un true si su eliminacion fue correcta en la base de datos
        return bancoDAO.eliminar(idb);
    }
}
