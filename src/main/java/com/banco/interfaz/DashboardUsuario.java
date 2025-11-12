package com.banco.interfaz;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.banco.servicio.AutenticacionServicio;
import com.banco.servicio.TarjetaServicio;
import com.banco.servicio.MovimientoServicio;
import com.banco.servicio.BancoServicio;
import com.banco.dao.ClienteDAO;
import com.banco.modelo.*;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class DashboardUsuario {
    private Stage stage;
    private AutenticacionServicio autenticacionServicio;
    private TarjetaServicio tarjetaServicio;
    private MovimientoServicio movimientoServicio;
    private BancoServicio bancoServicio;
    private ClienteDAO clienteDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DashboardUsuario(Stage stage, AutenticacionServicio autenticacionServicio) {
        this.stage = stage;
        this.autenticacionServicio = autenticacionServicio;
        this.tarjetaServicio = new TarjetaServicio();
        this.movimientoServicio = new MovimientoServicio();
        this.bancoServicio = new BancoServicio();
        this.clienteDAO = new ClienteDAO();
    }

    public void mostrar() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = crearHeader();
        root.setTop(header);

        // Menú lateral
        VBox menu = crearMenuLateral(root);
        root.setLeft(menu);

        // Área principal
        VBox contenido = crearContenidoMisTarjetas();
        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Dashboard Usuario");
        stage.show();
    }

    private VBox crearHeader() {
        VBox header = new VBox(10);
        header.setStyle("-fx-padding: 20; -fx-background-color: #1976d2;");
        header.setAlignment(Pos.CENTER_LEFT);

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        Label titulo = new Label("BIENVENIDO, " + usuarioActual.getCliente().getNombreCompleto().toUpperCase());
        titulo.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white;");

        header.getChildren().add(titulo);
        return header;
    }

    private VBox crearMenuLateral(BorderPane root) {
        VBox menu = new VBox(10);
        menu.setStyle("-fx-padding: 20; -fx-background-color: #263238; -fx-min-width: 200;");
        menu.setAlignment(Pos.TOP_CENTER);

        Label lblMenu = new Label("MENÚ");
        lblMenu.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white;");

        Button btnMisTarjetas = new Button("Mis Tarjetas");
        btnMisTarjetas.setPrefWidth(180);
        btnMisTarjetas.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnMisTarjetas.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoMisTarjetas());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });

        Button btnCrearTarjeta = new Button("Crear Tarjeta");
        btnCrearTarjeta.setPrefWidth(180);
        btnCrearTarjeta.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnCrearTarjeta.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoCrearTarjeta());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });

        Button btnMovimientos = new Button("Movimientos");
        btnMovimientos.setPrefWidth(180);
        btnMovimientos.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnMovimientos.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoMovimientos());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });

        Button btnTransacciones = new Button("Transacciones");
        btnTransacciones.setPrefWidth(180);
        btnTransacciones.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnTransacciones.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoTransacciones());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setPrefWidth(180);
        btnCerrarSesion.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #d32f2f; -fx-text-fill: white;");
        btnCerrarSesion.setOnAction(e -> {
            autenticacionServicio.cerrarSesion();
            new PantallaLogin(stage, autenticacionServicio).mostrar();
        });

        menu.getChildren().addAll(lblMenu, btnMisTarjetas, btnCrearTarjeta, btnMovimientos, 
                                   btnTransacciones, new Separator(), btnCerrarSesion);
        return menu;
    }

    private VBox crearContenidoMisTarjetas() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Mis Tarjetas");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        
        try {
            List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasCliente(usuarioActual.getCliente().getIdc());

            if (tarjetas.isEmpty()) {
                Label lblVacio = new Label("No tienes tarjetas registradas. Crea una nueva.");
                lblVacio.setStyle("-fx-font-size: 14; -fx-padding: 20;");
                contenido.getChildren().addAll(titulo, lblVacio);
            } else {
                for (Tarjeta tarjeta : tarjetas) {
                    VBox cardTarjeta = crearCardTarjeta(tarjeta);
                    contenido.getChildren().add(cardTarjeta);
                }
            }
        } catch (Exception ex) {
            Label lblError = new Label("Error al cargar tarjetas: " + ex.getMessage());
            lblError.setStyle("-fx-text-fill: #d32f2f;");
            contenido.getChildren().addAll(titulo, lblError);
        }

        return contenido;
    }

    private VBox crearCardTarjeta(Tarjeta tarjeta) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #1976d2; -fx-border-radius: 5; -fx-padding: 15; -fx-background-color: white;");

        try {
            Banco banco = bancoServicio.obtenerPorId(tarjeta.getIdb());
            Label lblBanco = new Label("Banco: " + (banco != null ? banco.getNombre() : "Desconocido"));
            lblBanco.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

            Label lblNumero = new Label("Tarjeta: " + tarjeta.getNumeroTarjeta());
            lblNumero.setStyle("-fx-font-size: 14;");

            Label lblSaldo = new Label("Saldo: $" + String.format("%.2f", tarjeta.getSaldo()));
            lblSaldo.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #4caf50;");

            Label lblEstado = new Label("Estado: " + (tarjeta.isActiva() ? "Activa" : "Inactiva"));
            lblEstado.setStyle("-fx-font-size: 12; -fx-text-fill: " + (tarjeta.isActiva() ? "#4caf50" : "#d32f2f") + ";");

            card.getChildren().addAll(lblBanco, lblNumero, lblSaldo, lblEstado);
        } catch (Exception ex) {
            Label lblError = new Label("Error al cargar datos de la tarjeta");
            lblError.setStyle("-fx-text-fill: #d32f2f;");
            card.getChildren().add(lblError);
        }

        return card;
    }

    private VBox crearContenidoCrearTarjeta() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Crear Nueva Tarjeta");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        VBox panel = new VBox(15);
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 20; -fx-background-color: white;");

        Label lblBanco = new Label("Selecciona un banco:");
        lblBanco.setStyle("-fx-font-size: 14;");

        ComboBox<Banco> cbBancos = new ComboBox<>();
        cbBancos.setPrefWidth(300);
        
        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        try {
            List<Banco> bancos = bancoServicio.obtenerTodos();
            cbBancos.getItems().addAll(bancos);
            if (!bancos.isEmpty()) {
                cbBancos.setValue(bancos.get(0));
            } else {
                lblMensaje.setText("No hay bancos disponibles. Contacta al administrador.");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error al cargar bancos: " + ex.getMessage());
            lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
        }

        Button btnCrear = new Button("Crear Tarjeta");
        btnCrear.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #4caf50; -fx-text-fill: white;");
        btnCrear.setOnAction(e -> {
            if (cbBancos.getValue() != null) {
                Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
                try {
                    if (tarjetaServicio.crearTarjeta(usuarioActual.getCliente().getIdc(), cbBancos.getValue().getIdb())) {
                        mostrarAlerta("Tarjeta creada exitosamente", Alert.AlertType.INFORMATION);
                    } else {
                        mostrarAlerta("Error al crear tarjeta", Alert.AlertType.ERROR);
                    }
                } catch (Exception ex) {
                    mostrarAlerta("Error: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                mostrarAlerta("Selecciona un banco", Alert.AlertType.WARNING);
            }
        });

        panel.getChildren().addAll(lblBanco, cbBancos, btnCrear, lblMensaje);
        contenido.getChildren().addAll(titulo, panel);
        return contenido;
    }

    private VBox crearContenidoMovimientos() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Mis Movimientos");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        
        try {
            List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasCliente(usuarioActual.getCliente().getIdc());

            if (tarjetas.isEmpty()) {
                Label lblVacio = new Label("No tienes tarjetas para ver movimientos.");
                lblVacio.setStyle("-fx-font-size: 14; -fx-padding: 20;");
                contenido.getChildren().addAll(titulo, lblVacio);
                return contenido;
            }

            HBox selectorTarjeta = new HBox(10);
            selectorTarjeta.setAlignment(Pos.CENTER_LEFT);
            Label lblTarjeta = new Label("Selecciona tarjeta:");
            ComboBox<Tarjeta> cbTarjetas = new ComboBox<>();
            cbTarjetas.getItems().addAll(tarjetas);
            cbTarjetas.setValue(tarjetas.get(0));
            cbTarjetas.setPrefWidth(400);

            TextArea areaMovimientos = new TextArea();
            areaMovimientos.setPrefHeight(350);
            areaMovimientos.setEditable(false);
            areaMovimientos.setWrapText(true);

            cbTarjetas.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    try {
                        List<Movimiento> movimientos = movimientoServicio.obtenerHistorial(newVal.getIdt());
                        StringBuilder sb = new StringBuilder();
                        sb.append("=== HISTORIAL DE MOVIMIENTOS ===\n");
                        sb.append("Tarjeta: ").append(newVal.getNumeroTarjeta()).append("\n");
                        sb.append("Saldo Actual: $").append(String.format("%.2f", newVal.getSaldo())).append("\n");
                        sb.append("===============================\n\n");
                        
                        if (movimientos.isEmpty()) {
                            sb.append("No hay movimientos registrados.");
                        } else {
                            for (Movimiento mov : movimientos) {
                                sb.append("[").append(mov.getFechaMovimiento().format(formatter)).append("]\n");
                                sb.append("Tipo: ").append(mov.getTipo()).append("\n");
                                sb.append("Cantidad: $").append(String.format("%.2f", mov.getCantidad())).append("\n");
                                sb.append("Descripción: ").append(mov.getDescripcion()).append("\n");
                                sb.append("---\n");
                            }
                        }
                        areaMovimientos.setText(sb.toString());
                    } catch (Exception ex) {
                        areaMovimientos.setText("Error al cargar movimientos: " + ex.getMessage());
                    }
                }
            });

            // Trigger initial load
            cbTarjetas.getSelectionModel().clearAndSelect(0);

            selectorTarjeta.getChildren().addAll(lblTarjeta, cbTarjetas);
            contenido.getChildren().addAll(titulo, selectorTarjeta, areaMovimientos);
        } catch (Exception ex) {
            Label lblError = new Label("Error: " + ex.getMessage());
            lblError.setStyle("-fx-text-fill: #d32f2f;");
            contenido.getChildren().addAll(titulo, lblError);
        }

        return contenido;
    }

    private VBox crearContenidoTransacciones() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Realizar Transacciones");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(400);

        Tab tabAbono = crearTabAbono();
        Tab tabRetiro = crearTabRetiro();
        Tab tabTransferencia = crearTabTransferencia();

        tabPane.getTabs().addAll(tabAbono, tabRetiro, tabTransferencia);

        contenido.getChildren().addAll(titulo, tabPane);
        return contenido;
    }

    private Tab crearTabAbono() {
        Tab tab = new Tab("Abono");
        tab.setClosable(false);
        VBox panel = new VBox(15);
        panel.setStyle("-fx-padding: 20;");

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasCliente(usuarioActual.getCliente().getIdc());

        Label lblTarjeta = new Label("Tarjeta:");
        ComboBox<Tarjeta> cbTarjetas = new ComboBox<>();
        cbTarjetas.getItems().addAll(tarjetas);
        if (!tarjetas.isEmpty()) cbTarjetas.setValue(tarjetas.get(0));
        cbTarjetas.setPrefWidth(400);

        Label lblCantidad = new Label("Cantidad:");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Ingresa la cantidad");
        txtCantidad.setPrefWidth(400);

        Label lblDescripcion = new Label("Descripción:");
        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Concepto del abono");
        txtDescripcion.setPrefWidth(400);

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-font-size: 12;");

        Button btnAbono = new Button("Abonar");
        btnAbono.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #4caf50; -fx-text-fill: white;");
        btnAbono.setOnAction(e -> {
            try {
                if (cbTarjetas.getValue() != null && !txtCantidad.getText().isEmpty()) {
                    double cantidad = Double.parseDouble(txtCantidad.getText());
                    if (cantidad <= 0) {
                        lblMensaje.setText("La cantidad debe ser mayor a 0");
                        lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                        return;
                    }
                    String descripcion = txtDescripcion.getText().isEmpty() ? "Abono manual" : txtDescripcion.getText();
                    
                    if (movimientoServicio.realizarAbono(cbTarjetas.getValue().getIdt(), cantidad, descripcion)) {
                        lblMensaje.setText("Abono de $" + String.format("%.2f", cantidad) + " realizado exitosamente");
                        lblMensaje.setStyle("-fx-text-fill: #4caf50;");
                        txtCantidad.clear();
                        txtDescripcion.clear();
                    } else {
                        lblMensaje.setText("Error al realizar abono");
                        lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                    }
                } else {
                    lblMensaje.setText("Completa todos los campos");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                }
            } catch (NumberFormatException ex) {
                lblMensaje.setText("Ingresa una cantidad válida");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
            }
        });

        panel.getChildren().addAll(lblTarjeta, cbTarjetas, lblCantidad, txtCantidad, 
                                    lblDescripcion, txtDescripcion, btnAbono, lblMensaje);
        tab.setContent(panel);
        return tab;
    }

    private Tab crearTabRetiro() {
        Tab tab = new Tab("Retiro");
        tab.setClosable(false);
        VBox panel = new VBox(15);
        panel.setStyle("-fx-padding: 20;");

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasCliente(usuarioActual.getCliente().getIdc());

        Label lblTarjeta = new Label("Tarjeta:");
        ComboBox<Tarjeta> cbTarjetas = new ComboBox<>();
        cbTarjetas.getItems().addAll(tarjetas);
        if (!tarjetas.isEmpty()) cbTarjetas.setValue(tarjetas.get(0));
        cbTarjetas.setPrefWidth(400);

        Label lblCantidad = new Label("Cantidad:");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Ingresa la cantidad");
        txtCantidad.setPrefWidth(400);

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-font-size: 12;");

        Button btnRetiro = new Button("Retirar");
        btnRetiro.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #ff9800; -fx-text-fill: white;");
        btnRetiro.setOnAction(e -> {
            try {
                if (cbTarjetas.getValue() != null && !txtCantidad.getText().isEmpty()) {
                    double cantidad = Double.parseDouble(txtCantidad.getText());
                    if (cantidad <= 0) {
                        lblMensaje.setText("La cantidad debe ser mayor a 0");
                        lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                        return;
                    }
                    
                    if (movimientoServicio.realizarRetiro(cbTarjetas.getValue().getIdt(), cantidad, "Retiro manual")) {
                        lblMensaje.setText("Retiro de $" + String.format("%.2f", cantidad) + " realizado exitosamente");
                        lblMensaje.setStyle("-fx-text-fill: #4caf50;");
                        txtCantidad.clear();
                    } else {
                        lblMensaje.setText("Saldo insuficiente o tarjeta inactiva");
                        lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                    }
                } else {
                    lblMensaje.setText("Completa todos los campos");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                }
            } catch (NumberFormatException ex) {
                lblMensaje.setText("Ingresa una cantidad válida");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
            }
        });

        panel.getChildren().addAll(lblTarjeta, cbTarjetas, lblCantidad, txtCantidad, btnRetiro, lblMensaje);
        tab.setContent(panel);
        return tab;
    }

    private Tab crearTabTransferencia() {
        Tab tab = new Tab("Transferencias");
        tab.setClosable(false);
        VBox panel = new VBox(15);
        panel.setStyle("-fx-padding: 20;");

        Usuario usuarioActual = autenticacionServicio.obtenerUsuarioActual();
        List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasCliente(usuarioActual.getCliente().getIdc());

        Label lblOrigen = new Label("Tarjeta Origen:");
        ComboBox<Tarjeta> cbOrigen = new ComboBox<>();
        cbOrigen.getItems().addAll(tarjetas);
        if (!tarjetas.isEmpty()) cbOrigen.setValue(tarjetas.get(0));
        cbOrigen.setPrefWidth(400);

        Label lblDestino = new Label("Número Tarjeta Destino:");
        TextField txtDestino = new TextField();
        txtDestino.setPromptText("Ingresa número de tarjeta destino");
        txtDestino.setPrefWidth(400);

        Label lblCantidad = new Label("Cantidad:");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Ingresa la cantidad");
        txtCantidad.setPrefWidth(400);

        CheckBox cbInterbancaria = new CheckBox("¿Transferencia interbancaria? (comisión 1%)");

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-font-size: 12;");

        Button btnTransferir = new Button("Transferir");
        btnTransferir.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-color: #2196f3; -fx-text-fill: white;");
        btnTransferir.setOnAction(e -> {
            try {
                if (cbOrigen.getValue() == null || txtDestino.getText().isEmpty() || txtCantidad.getText().isEmpty()) {
                    lblMensaje.setText("Completa todos los campos");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                    return;
                }

                Tarjeta tarjetaDestino = new com.banco.dao.TarjetaDAO().obtenerPorNumero(txtDestino.getText());
                if (tarjetaDestino == null) {
                    lblMensaje.setText("Tarjeta destino no encontrada");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                    return;
                }

                double cantidad = Double.parseDouble(txtCantidad.getText());
                if (cantidad <= 0) {
                    lblMensaje.setText("La cantidad debe ser mayor a 0");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                    return;
                }

                boolean exitoso = false;

                if (cbInterbancaria.isSelected()) {
                    exitoso = movimientoServicio.realizarTransferenciaInterbancaria(
                        cbOrigen.getValue().getIdt(), tarjetaDestino.getIdt(), cantidad);
                    if (exitoso) {
                        lblMensaje.setText("Transferencia interbancaria de $" + String.format("%.2f", cantidad) + " realizada (comisión 1%)");
                        lblMensaje.setStyle("-fx-text-fill: #4caf50;");
                    }
                } else {
                    exitoso = movimientoServicio.realizarTransferencia(
                        cbOrigen.getValue().getIdt(), tarjetaDestino.getIdt(), cantidad);
                    if (exitoso) {
                        lblMensaje.setText("Transferencia de $" + String.format("%.2f", cantidad) + " realizada exitosamente");
                        lblMensaje.setStyle("-fx-text-fill: #4caf50;");
                    }
                }

                if (!exitoso) {
                    lblMensaje.setText("Error: Verifica saldo y que sean del mismo banco si no es interbancaria");
                    lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
                }
                txtDestino.clear();
                txtCantidad.clear();
                cbInterbancaria.setSelected(false);
            } catch (NumberFormatException ex) {
                lblMensaje.setText("Ingresa una cantidad válida");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
            } catch (Exception ex) {
                lblMensaje.setText("Error: " + ex.getMessage());
                lblMensaje.setStyle("-fx-text-fill: #d32f2f;");
            }
        });

        panel.getChildren().addAll(lblOrigen, cbOrigen, lblDestino, txtDestino, lblCantidad, 
                                    txtCantidad, cbInterbancaria, btnTransferir, lblMensaje);
        tab.setContent(panel);
        return tab;
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
