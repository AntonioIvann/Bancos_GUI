package com.banco.aplicacion;

import javafx.application.Application;
import javafx.stage.Stage;
import com.banco.servicio.AutenticacionServicio;
import com.banco.interfaz.PantallaLogin;

public class AplicacionPrincipal extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            AutenticacionServicio autenticacionServicio = new AutenticacionServicio();
            PantallaLogin pantallaLogin = new PantallaLogin(primaryStage, autenticacionServicio);
            pantallaLogin.mostrar();
            
            primaryStage.setOnCloseRequest(e -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
