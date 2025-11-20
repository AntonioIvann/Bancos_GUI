package com.banco.servicio;

//Librerias
import com.banco.dao.TarjetaDAO;
import com.banco.modelo.Tarjeta;
import java.util.List;
import java.util.Random;

//Clase TarjetaServicio
public class TarjetaServicio {
    //Atributo privado TarjetaDA0
    private TarjetaDAO tarjetaDAO;

    // Constructor
    public TarjetaServicio() {
        //Se inicializa un atributo tarjetaDA0, creando una nueva instancia TarjetaDA0
        this.tarjetaDAO = new TarjetaDAO();
    }

    //Metodo para generar un numero de tarjeta
    public String generarNumeroTarjeta() {
        //Crea un StringBuilder para construir cadenas de texto de manera eficiente, agregando los dígitos uno por uno.
        StringBuilder numero = new StringBuilder();
        Random random = new Random();
        //Crea un objeto de tipo Random, que permite generar números aleatorios.
        for (int i = 0; i < 16; i++) {
            //Inicia una iteracion del 1 al 16 en +1, por cada digito de la tarjeta
            numero.append(random.nextInt(10));
            //Genera un número aleatorio entre 0 y 9, (devuelve valores del 0-9)
            //Lo agrega al String que se esta formando
        }
        return numero.toString();
        //Convierte el StringBuilder a un String y lo devuelve como el número final de la tarjeta.
    }

    //Metodo para crear la tarjeta, con idc, idb
    public boolean crearTarjeta(int idc, int idb) {
        //Manda a llamar el metodo numeroTarjeta y lo renombra en el generarNumeroTarjeta();
        String numeroTarjeta = generarNumeroTarjeta();
        
        // Verificar que no exista
        while (tarjetaDAO.obtenerPorNumero(numeroTarjeta) != null) {
            numeroTarjeta = generarNumeroTarjeta();
        }

        //Crea el objeto tarjeta con, idc, idb, numeroTarjeta
        Tarjeta tarjeta = new Tarjeta(idc, idb, numeroTarjeta);
        //Devuelve la tarjeta si es menor a 0, (numero de id)
        return tarjetaDAO.crear(tarjeta) > 0;
    }

    //Crea un arreglo List, con idc
    public List<Tarjeta> obtenerTarjetasCliente(int idc) {
        //Devuelve la tarjeta con el id de cliente
        return tarjetaDAO.obtenerPorCliente(idc);
    }

    //Crea el metodo obtenerTarjeta dentro del objeto Tarjeta, con idc
    public Tarjeta obtenerTarjeta(int idt) {
        //Devuelve la tarjeta con el id de tarjeta
        return tarjetaDAO.obtenerPorId(idt);
    }

    //Crea el metodo actualizar tarjeta, con tarjeta
    public boolean actualizarTarjeta(Tarjeta tarjeta) {
        //Devuelve la tarjeta actualizada
        return tarjetaDAO.actualizar(tarjeta);
    }
}
