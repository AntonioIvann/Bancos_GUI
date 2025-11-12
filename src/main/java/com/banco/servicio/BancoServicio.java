package com.banco.servicio;

import com.banco.dao.BancoDAO;
import com.banco.modelo.Banco;
import java.util.List;

public class BancoServicio {
    private BancoDAO bancoDAO;

    public BancoServicio() {
        this.bancoDAO = new BancoDAO();
    }

    public boolean crearBanco(String nombre) {
        Banco banco = new Banco(nombre);
        return bancoDAO.crear(banco) > 0;
    }

    public List<Banco> obtenerTodos() {
        return bancoDAO.obtenerTodos();
    }

    public Banco obtenerPorId(int id) {
        return bancoDAO.obtenerPorId(id);
    }
    
    public boolean actualizarBanco(Banco banco) {
        return bancoDAO.actualizar(banco);
    }
    
    public boolean eliminarBanco(int idb) {
        return bancoDAO.eliminar(idb);
    }
}
