
//Clase principal del gestor bancario, inicia la aplicación JavaFX

package com.banco.aplicacion;

//Importación de clases necesarias para el funcionamiento de JavaFX

import javafx.application.Application;
import javafx.stage.Stage;
import com.banco.servicio.AutenticacionServicio;
import com.banco.interfaz.PantallaLogin;

public class AplicacionPrincipal extends Application {
    // Método principal para iniciar la aplicación
    @Override
    public void start(Stage primaryStage) {
        // Crear objeto de autenticación y pantalla de inicio de sesión
        try {
            AutenticacionServicio autenticacionServicio = new AutenticacionServicio();
            PantallaLogin pantallaLogin = new PantallaLogin(primaryStage, autenticacionServicio);
            pantallaLogin.mostrar();
            
            primaryStage.setOnCloseRequest(e -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        launch(args);
    }
}