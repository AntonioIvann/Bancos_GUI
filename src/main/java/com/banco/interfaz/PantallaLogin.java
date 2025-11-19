package com.banco.interfaz;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.banco.servicio.AutenticacionServicio;
import com.banco.modelo.Usuario;

public class PantallaLogin {
    private Stage stage;
    private AutenticacionServicio autenticacionServicio;

    public PantallaLogin(Stage stage, AutenticacionServicio autenticacionServicio) {
        this.stage = stage;
        this.autenticacionServicio = autenticacionServicio;
    }

    public void mostrar() {
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20; -fx-background-color: #f5f5f5;");
        root.setAlignment(Pos.CENTER);

        Label titulo = new Label("SISTEMA GESTOR BANCARIO");
        titulo.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

        // Panel de Login
        VBox panelLogin = crearPanelLogin();
        
        // Panel de Registro
        VBox panelRegistro = crearPanelRegistro();
        panelRegistro.setVisible(false);
        panelRegistro.setManaged(false);

        HBox botonesMenu = new HBox(10);
        botonesMenu.setAlignment(Pos.CENTER);
        Button btnLogin = new Button("Iniciar Sesión");
        Button btnRegistro = new Button("Registrarse");
        Button btnSalir = new Button("Finalizar");

        btnLogin.setStyle("-fx-padding: 10; -fx-font-size: 14;");
        btnRegistro.setStyle("-fx-padding: 10; -fx-font-size: 14;");
        btnSalir.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #d32f2f; -fx-text-fill: white;");

        btnLogin.setOnAction(e -> {
            panelLogin.setVisible(true);
            panelLogin.setManaged(true);
            panelRegistro.setVisible(false);
            panelRegistro.setManaged(false);
        });

        btnRegistro.setOnAction(e -> {
            panelLogin.setVisible(false);
            panelLogin.setManaged(false);
            panelRegistro.setVisible(true);
            panelRegistro.setManaged(true);
        });

        btnSalir.setOnAction(e -> stage.close());

        botonesMenu.getChildren().addAll(btnLogin, btnRegistro, btnSalir);

        root.getChildren().addAll(titulo, botonesMenu, panelLogin, panelRegistro);

        Scene scene = new Scene(root, 600, 750);
        stage.setScene(scene);
        stage.setTitle("Sistema Bancario - Login");
        stage.show();
    }

    private VBox crearPanelLogin() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 30; -fx-background-color: white;");
        panel.setPrefWidth(400);
        panel.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Iniciar Sesión");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

        Label lblUsuario = new Label("Usuario:");
        lblUsuario.setStyle("-fx-font-size: 14;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingrese su usuario");
        txtUsuario.setPrefWidth(300);

        Label lblContraseña = new Label("Contraseña:");
        lblContraseña.setStyle("-fx-font-size: 14;");
        PasswordField txtContraseña = new PasswordField();
        txtContraseña.setPromptText("Ingrese su contraseña");
        txtContraseña.setPrefWidth(300);

        Button btnIniciarSesion = new Button("Iniciar Sesión");
        btnIniciarSesion.setPrefWidth(200);
        btnIniciarSesion.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #1976d2; -fx-text-fill: white;");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12;");

        btnIniciarSesion.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = txtContraseña.getText();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                lblError.setText("Ingrese usuario y contraseña");
                return;
            }

            try {
                if (autenticacionServicio.login(usuario, contraseña)) {
                    Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
                    lblError.setText("¡Inicio de sesión exitoso!");
                    lblError.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12;");
                    
                    // Cambiar a dashboard después de 800ms
                    new Thread(() -> {
                        try {
                            Thread.sleep(800);
                            javafx.application.Platform.runLater(() -> {
                                if (usuarioActual.isEsAdmin()) {
                                    new DashboardAdmin(stage, autenticacionServicio).mostrar();
                                } else {
                                    new DashboardUsuario(stage, autenticacionServicio).mostrar();
                                }
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                } else {
                    lblError.setText("Usuario o contraseña incorrectos");
                }
            } catch (Exception ex) {
                lblError.setText("Error en la conexión: " + ex.getMessage());
            }

            txtUsuario.clear();
            txtContraseña.clear();
        });

        panel.getChildren().addAll(titulo, lblUsuario, txtUsuario, lblContraseña, txtContraseña, 
                                    btnIniciarSesion, lblError);

        return panel;
    }

    private VBox crearPanelRegistro() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 20; -fx-background-color: white;");
        panel.setPrefWidth(400);
        panel.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);

        Label titulo = new Label("Registrarse");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

        Label lblUsuario = new Label("Usuario:");
        lblUsuario.setStyle("-fx-font-size: 12;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Min. 4 caracteres");
        txtUsuario.setPrefWidth(300);

        Label lblContraseña = new Label("Contraseña:");
        lblContraseña.setStyle("-fx-font-size: 12;");
        PasswordField txtContraseña = new PasswordField();
        txtContraseña.setPromptText("Min. 6 caracteres");
        txtContraseña.setPrefWidth(300);

        Label lblApellidoP = new Label("Apellido Paterno:");
        lblApellidoP.setStyle("-fx-font-size: 12;");
        TextField txtApellidoP = new TextField();
        txtApellidoP.setPromptText("Apellido paterno");
        txtApellidoP.setPrefWidth(300);

        Label lblApellidoM = new Label("Apellido Materno:");
        lblApellidoM.setStyle("-fx-font-size: 12;");
        TextField txtApellidoM = new TextField();
        txtApellidoM.setPromptText("Apellido materno");
        txtApellidoM.setPrefWidth(300);

        Label lblNombre = new Label("Nombre:");
        lblNombre.setStyle("-fx-font-size: 12;");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.setPrefWidth(300);

        Label lblTipo = new Label("Tipo de Usuario:");
        lblTipo.setStyle("-fx-font-size: 12;");
        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Usuario Normal", "Administrador");
        cbTipo.setValue("Usuario Normal");
        cbTipo.setPrefWidth(300);

        Button btnRegistrase = new Button("Registrarse");
        btnRegistrase.setPrefWidth(200);
        btnRegistrase.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #1976d2; -fx-text-fill: white;");

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12;");

        btnRegistrase.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = txtContraseña.getText();
            String apellidoP = txtApellidoP.getText().trim();
            String apellidoM = txtApellidoM.getText().trim();
            String nombre = txtNombre.getText().trim();
            boolean esAdmin = cbTipo.getValue().equals("Administrador");

            if (usuario.isEmpty() || contraseña.isEmpty() || apellidoP.isEmpty() || 
                apellidoM.isEmpty() || nombre.isEmpty()) {
                lblMensaje.setText("Todos los campos son obligatorios");
                return;
            }

            if (usuario.length() < 4) {
                lblMensaje.setText("El usuario debe tener al menos 4 caracteres");
                return;
            }

            if (contraseña.length() < 6) {
                lblMensaje.setText("La contraseña debe tener al menos 6 caracteres");
                return;
            }

            try {
                if (autenticacionServicio.registrase(usuario, contraseña, apellidoP, apellidoM, nombre, esAdmin)) {
                    lblMensaje.setText("Registro exitoso. Inicia sesión con tus credenciales");
                    lblMensaje.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12;");
                    
                    txtUsuario.clear();
                    txtContraseña.clear();
                    txtApellidoP.clear();
                    txtApellidoM.clear();
                    txtNombre.clear();
                } else {
                    lblMensaje.setText("Error al registrar. El usuario podría existir");
                }
            } catch (Exception ex) {
                lblMensaje.setText("Error en el registro: " + ex.getMessage());
            }
        });

        VBox contenedor = new VBox(10);
        contenedor.setStyle("-fx-padding: 20;");
        contenedor.getChildren().addAll(titulo, lblUsuario, txtUsuario, lblContraseña, txtContraseña,
                                         lblApellidoP, txtApellidoP, lblApellidoM, txtApellidoM,
                                         lblNombre, txtNombre, lblTipo, cbTipo,
                                         btnRegistrase, lblMensaje);
        
        return contenedor;
    }
}
