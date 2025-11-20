package com.banco.servicio;

//Librerias
import com.banco.dao.MovimientoDAO;
import com.banco.dao.TarjetaDAO;
import com.banco.modelo.Movimiento;
import com.banco.modelo.Tarjeta;
import java.util.List;

// Clase MovimientoServicio
public class MovimientoServicio {
    //Atributo privado MovimientoDA0
    private MovimientoDAO movimientoDAO;
    //Atributo privado TarjetaDA0
    private TarjetaDAO tarjetaDAO;

    //Metodo MovimientoServicio
    public MovimientoServicio() {
        //Se inicializa un atributo movimientoDA0, creando una nueva instancia MovimientoDA0
        this.movimientoDAO = new MovimientoDAO();
        //Se inicializa un atributo tarjetaDA0, creando una nueva instancia TarjetaDA0
        this.tarjetaDAO = new TarjetaDAO();
    }

    //Metodo para realizar un abono, atributos idt, cantidad, descripcion
    public boolean realizarAbono(int idt, double cantidad, String descripcion) {
        //Obtiene la tarjeta por su ID
        Tarjeta tarjeta = tarjetaDAO.obtenerPorId(idt);
        //Si la tarjeta no existe retorna false
        if (tarjeta == null || !tarjeta.isActiva()) {
            return false;
        }

        //Suma el dinero al saldo actual
        tarjeta.setSaldo(tarjeta.getSaldo() + cantidad);
        //Actualiza la tarjeta en la base de datos
        tarjetaDAO.actualizar(tarjeta);

        //Registrar un movimiento de tipo credito, atributos idt, cantidad, credito, descripcion
        Movimiento movimiento = new Movimiento(idt, cantidad, "CREDITO", descripcion);
        //Devuelve solamente si el movimiento se creo.
        return movimientoDAO.crear(movimiento) > 0;
    }

    //Metodo para realizar un retiro, atributos idt, cantidad, descripcion
    public boolean realizarRetiro(int idt, double cantidad, String descripcion) {
        //Obtiene la tarjeta por su ID
        Tarjeta tarjeta = tarjetaDAO.obtenerPorId(idt);
        //Si no existe, devuelve falso
        if (tarjeta == null || !tarjeta.isActiva() || tarjeta.getSaldo() < cantidad) {
            return false;
        }

        //Actualiza el monto de la tarjeta restando la cantidad del abono
        tarjeta.setSaldo(tarjeta.getSaldo() - cantidad);
        //Actualiza la tarjeta en la base de datos
        tarjetaDAO.actualizar(tarjeta);

        //Registrar un movimiento de tipo credito, atributos idt, cantidad, debito, descripcion
        Movimiento movimiento = new Movimiento(idt, cantidad, "DEBITO", descripcion);
        //Devuelve solamente si el movimiento se creo.
        return movimientoDAO.crear(movimiento) > 0;
    }

    //Metodo para realizar una transferencia, atributos idtOrigen, idtDestino, cantidad
    public boolean realizarTransferencia(int idtOrigen, int idtDestino, double cantidad) {
        //Obtiene la tarjeta origen por su ID
        Tarjeta tarjetaOrigen = tarjetaDAO.obtenerPorId(idtOrigen);
        //Obtiene la tarjeta destino por su ID
        Tarjeta tarjetaDestino = tarjetaDAO.obtenerPorId(idtDestino);

        //Condicion, Si la tarjeta de origen/destino no existe, 
        if (tarjetaOrigen == null || tarjetaDestino == null || 
            //Condicion, Si la tarjeta esta activa/inactiva
            !tarjetaOrigen.isActiva() || !tarjetaDestino.isActiva() ||
            //Condicion, Si la tarjeta no tiene saldo suficiente
            tarjetaOrigen.getSaldo() < cantidad) {
            //Devuelve falso
            return false;
        }

        //Resta el saldo de la tarjeta origen
        tarjetaOrigen.setSaldo(tarjetaOrigen.getSaldo() - cantidad);
        //Suma el saldo a la tarjeta destino
        tarjetaDestino.setSaldo(tarjetaDestino.getSaldo() + cantidad);

        //Actualiza los datos de la tarjeta origen
        tarjetaDAO.actualizar(tarjetaOrigen);
        //Actualiza los datos de la tarjeta destino
        tarjetaDAO.actualizar(tarjetaDestino);

        //Crea un nuevo movimiento origen
        Movimiento movOrigen = new Movimiento(idtOrigen, cantidad, "DEBITO", 
            // Imprime el numero de tarjeta origen
            "Transferencia a tarjeta " + tarjetaDestino.getNumeroTarjeta());
        //Crea un nuevo movimiento destino
        Movimiento movDestino = new Movimiento(idtDestino, cantidad, "CREDITO", 
            // Imprime el numero de tarjeta destino
            "Transferencia de tarjeta " + tarjetaOrigen.getNumeroTarjeta());

        //Crea el movimiento origen
        movimientoDAO.crear(movOrigen);
        //Crea el movimiento destino
        movimientoDAO.crear(movDestino);

        return true;
    }

    //Metodo para realizar una transferencia interbancaria, atributos idtOrigen, idtDestino, cantidad
    public boolean realizarTransferenciaInterbancaria(int idtOrigen, int idtDestino, double cantidad) {
        //Comision donde a la cantidad le cobra 1% de comision
        double comision = cantidad * 0.01; // 1% de comisión
        //Cantidad total, cantidad mas la comision aplicada
        double cantidadTotal = cantidad + comision;

        //Busca en la base de datos la tarjeta cuyo ID es idtOrigen
        Tarjeta tarjetaOrigen = tarjetaDAO.obtenerPorId(idtOrigen);
        //busca en la base de datos la tarjeta cuyo ID es idtDestino
        Tarjeta tarjetaDestino = tarjetaDAO.obtenerPorId(idtDestino);

        //Condicion, Si las tarjetas de origen/destino no existen
        if (tarjetaOrigen == null || tarjetaDestino == null || 
            //Condicion, Si la tarjeta esta activa/inactiva
            !tarjetaOrigen.isActiva() || !tarjetaDestino.isActiva() ||
            //Condicion, Si la tarjeta tiene saldo suficiente
            tarjetaOrigen.getSaldo() < cantidadTotal ||
            //Condicion, Que sean de distintos bancos
            tarjetaOrigen.getIdb() == tarjetaDestino.getIdb()) {
            //Devuelve falso
            return false;
        }

        //Tarjeta origen se le resta la cantidad total (con comision) al saldo actual
        tarjetaOrigen.setSaldo(tarjetaOrigen.getSaldo() - cantidadTotal);
        //Tarjeta destion se le suma la cantidad total al saldo actual
        tarjetaDestino.setSaldo(tarjetaDestino.getSaldo() + cantidad);

        //Actualizamos la tarjeta origen en la base de datos
        tarjetaDAO.actualizar(tarjetaOrigen);
        //Actualizamos la tarjeta destino en la base de datos
        tarjetaDAO.actualizar(tarjetaDestino);

        //Registra el movimiento de la cuenta origen
        Movimiento movOrigen = new Movimiento(idtOrigen, cantidadTotal, "DEBITO", 
        //Imprime el mensaje, con la cantidad de comision, y la cantidad con comision
            "Transferencia interbancaria (comisión: $" + comision + ")");
        //Registra el movimiento de la cuenta destino
        Movimiento movDestino = new Movimiento(idtDestino, cantidad, "CREDITO", 
        //Imprime el mensaje
            "Transferencia interbancaria recibida");

        //Crear el movimiento origen
        movimientoDAO.crear(movOrigen);
        //Crea el movimiento destino
        movimientoDAO.crear(movDestino);

        return true;
    }

    //Metodo creacion de List de tipo Movimiento para una tarjeta especifica
    public List<Movimiento> obtenerHistorial(int idt) {
        //Llama al movimientoDA0 para obtener todos los movimientos en la base de datos con la tarjeta de ese ID
        return movimientoDAO.obtenerPorTarjeta(idt);
    }
}