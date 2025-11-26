package com.banco.interfaz;

// Importación de clases necesarias para controles, layouts y funciones de JavaFX

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
import com.banco.dao.*;
import com.banco.modelo.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Clase que representa la ventana principal de esta pantalla

public class DashboardAdmin {
    //Stage principal donde se muestra la ventana
    private Stage stage;
    //Servicio que maneja la logica(autenticacion, movimientos, registros)
    private AutenticacionServicio autenticacionServicio;
    private BancoDAO bancoDAO;
    private ClienteDAO clienteDAO;
    private TarjetaDAO tarjetaDAO;
    private MovimientoDAO movimientoDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Cambio de pantalla reutilizando el mismo Stage
    public DashboardAdmin(Stage stage, AutenticacionServicio autenticacionServicio) {
        this.stage = stage;
        this.autenticacionServicio = autenticacionServicio;
        this.bancoDAO = new BancoDAO();
        this.clienteDAO = new ClienteDAO();
        this.tarjetaDAO = new TarjetaDAO();
        this.movimientoDAO = new MovimientoDAO();
    }

    // Metodo principal que arma y muestra toda la interfaz grafica

    public void mostrar() {
        // Contenedor principal donde se colocarán los paneles
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = crearHeader();
        root.setTop(header);

        // Menú lateral
        VBox menu = crearMenuLateral(root);
        root.setLeft(menu);

        // Área principal
        VBox contenido = crearContenidoBancos();
        root.setCenter(contenido);

        ScrollPane scrollPrincipal = new ScrollPane(contenido);
        scrollPrincipal.setFitToWidth(true);
        root.setCenter(scrollPrincipal);

        // Se crea la escena con el diseño final y se asigna al Stage
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Dashboard Administrador");
        stage.show();
    }

    private VBox crearHeader() {
        // Panel vertical para organizar los elementos
        VBox header = new VBox(10);
        header.setStyle("-fx-padding: 20; -fx-background-color: #1976d2;");
        header.setAlignment(Pos.CENTER_LEFT);
        // Etiqueta de texto para mostrar información al usuario

        Label titulo = new Label("PANEL ADMINISTRATIVO");
        titulo.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white;");

        Label usuario = new Label("Admin: " + autenticacionServicio.obtenerUsuarioActual().getUsuario());
        usuario.setStyle("-fx-font-size: 12; -fx-text-fill: #e0e0e0;");

        header.getChildren().addAll(titulo, usuario);
        return header;
    }
    // Método para crear el menú lateral con botones de navegación
    private VBox crearMenuLateral(BorderPane root) {
        VBox menu = new VBox(10);
        menu.setStyle("-fx-padding: 20; -fx-background-color: #263238; -fx-min-width: 200;");
        menu.setAlignment(Pos.TOP_CENTER);

        // Etiqueta que actúa como título del menú
        Label lblMenu = new Label("MENÚ");
        lblMenu.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white;");
        // Etiqueta de texto que muestra el titulo gestión de bancos
        Button btnBancos = new Button("Gestión Bancos");
        btnBancos.setPrefWidth(180);
        btnBancos.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnBancos.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoBancos());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });
        // Etiqueta de texto que muestra el titulo gestión de clientes
        Button btnClientes = new Button("Gestión Clientes");
        btnClientes.setPrefWidth(180);
        btnClientes.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnClientes.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoClientes());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });
        // Etiqueta de texto que muestra el titulo ver movimientos
        Button btnMovimientos = new Button("Ver Movimientos");
        btnMovimientos.setPrefWidth(180);
        btnMovimientos.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnMovimientos.setOnAction(e -> {
            ScrollPane scroll = new ScrollPane(crearContenidoMovimientos());
            scroll.setFitToWidth(true);
            root.setCenter(scroll);
        });
        // Etiqueta de texto que muestra el titulo cerrar sesión
        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setPrefWidth(180);
        btnCerrarSesion.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #d32f2f; -fx-text-fill: white;");
        btnCerrarSesion.setOnAction(e -> {
            autenticacionServicio.cerrarSesion();
            new PantallaLogin(stage, autenticacionServicio).mostrar();
        });

        menu.getChildren().addAll(lblMenu, btnBancos, btnClientes, btnMovimientos, 
                                   new Separator(), btnCerrarSesion);
        return menu;
    }
    // Método auxiliar para crear el contenido de la pantalla de gestión de bancos
    private VBox crearContenidoBancos() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Gestión de Bancos");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Panel para agregar banco
        HBox panelAgregar = new HBox(10);
        panelAgregar.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 15; -fx-background-color: white;");
        panelAgregar.setAlignment(Pos.CENTER_LEFT);

        // Campo de entrada donde el usuario escribe datos
        TextField txtNombreBanco = new TextField();
        txtNombreBanco.setPromptText("Nombre del nuevo banco");
        txtNombreBanco.setPrefWidth(300);

        Button btnAgregar = new Button("Agregar Banco");
        btnAgregar.setStyle("-fx-padding: 8; -fx-font-size: 12; -fx-background-color: #4caf50; -fx-text-fill: white;");
        btnAgregar.setOnAction(e -> {
            String nombre = txtNombreBanco.getText().trim();
            if (!nombre.isEmpty()) {
                Banco banco = new Banco(nombre);
                try {
                    int idBanco = bancoDAO.crear(banco);
                    if (idBanco > 0) {
                        txtNombreBanco.clear();
                        mostrarAlerta("Banco '" + nombre + "' agregado exitosamente", Alert.AlertType.INFORMATION);
                        // Refrescar tabla
                        actualizarTablaBancos(contenido);
                    } else {
                        mostrarAlerta("Error al agregar banco", Alert.AlertType.ERROR);
                    }
                } catch (Exception ex) {
                    mostrarAlerta("Error: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                mostrarAlerta("Ingresa un nombre para el banco", Alert.AlertType.WARNING);
            }
        });

        panelAgregar.getChildren().addAll(new Label("Nuevo Banco:"), txtNombreBanco, btnAgregar);

        // Tabla de bancos
        TableView<Banco> tabla = new TableView<>();
        tabla.setPrefHeight(350);

        TableColumn<Banco, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdb()));
        colId.setPrefWidth(50);

        TableColumn<Banco, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getNombre()));
        colNombre.setPrefWidth(300);

        TableColumn<Banco, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setPrefWidth(150);
        colAcciones.setCellFactory(param -> new TableCell<Banco, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEditar.setStyle("-fx-padding: 5; -fx-font-size: 10;");
                btnEliminar.setStyle("-fx-padding: 5; -fx-font-size: 10; -fx-background-color: #d32f2f; -fx-text-fill: white;");

                btnEditar.setOnAction(event -> {
                    Banco banco = getTableView().getItems().get(getIndex());
                    mostrarAlerta("Editar banco: " + banco.getNombre(), Alert.AlertType.INFORMATION);
                });

                btnEliminar.setOnAction(event -> {
                    try {
                        Banco banco = getTableView().getItems().get(getIndex());
                        if (bancoDAO.eliminar(banco.getIdb())) {
                            getTableView().getItems().remove(getIndex());
                            mostrarAlerta("Banco eliminado exitosamente", Alert.AlertType.INFORMATION);
                        } else {
                            mostrarAlerta("Error al eliminar el banco contiene datos", Alert.AlertType.INFORMATION);
                        }
                    } catch (Exception ex) {
                        mostrarAlerta("Error al eliminar: Banco contiene datos" + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botones = new HBox(5);
                    botones.getChildren().addAll(btnEditar, btnEliminar);
                    setGraphic(botones);
                }
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colAcciones);

        // Cargar datos
        try {
            ObservableList<Banco> bancos = FXCollections.observableArrayList(bancoDAO.obtenerTodos());
            tabla.setItems(bancos);
        } catch (Exception ex) {
            mostrarAlerta("Error al cargar bancos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }

        contenido.getChildren().addAll(titulo, panelAgregar, new Label("Bancos registrados:"), tabla);
        return contenido;
    }

    private void actualizarTablaBancos(VBox contenido) {
        // Método auxiliar para refrescar la tabla de bancos
    }

    private VBox crearContenidoClientes() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Gestión de Clientes");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Tabla de clientes
        TableView<Cliente> tabla = new TableView<>();
        tabla.setPrefHeight(400);

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdc()));
        colId.setPrefWidth(50);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre Completo");
        colNombre.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getNombreCompleto()));
        colNombre.setPrefWidth(400);

 TableColumn<Cliente, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setPrefWidth(150);
        colAcciones.setCellFactory(param -> new TableCell<Cliente, Void>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setStyle("-fx-padding: 5; -fx-font-size: 10; -fx-background-color: #d32f2f; -fx-text-fill: white;");

                btnEliminar.setOnAction(event -> {
                    try {
                        Cliente cliente = getTableView().getItems().get(getIndex());
                        if (clienteDAO.eliminar(cliente.getIdc())) {
                            getTableView().getItems().remove(getIndex());
                            mostrarAlerta("Cliente eliminado exitosamente", Alert.AlertType.INFORMATION);
                        }
                    } catch (Exception ex) {
                        mostrarAlerta("Error al eliminar: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colAcciones);

        // Cargar datos
        try {
            ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteDAO.obtenerTodos());
            tabla.setItems(clientes);
        } catch (Exception ex) {
            mostrarAlerta("Error al cargar clientes: " + ex.getMessage(), Alert.AlertType.ERROR);
        }

        contenido.getChildren().addAll(titulo, tabla);
        return contenido;
    }

    private VBox crearContenidoMovimientos() {
        VBox contenido = new VBox(15);
        contenido.setStyle("-fx-padding: 20;");

        Label titulo = new Label("Historial de Movimientos");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Tabla de movimientos
        TableView<Movimiento> tabla = new TableView<>();
        tabla.setPrefHeight(400);

        TableColumn<Movimiento, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdm()));
        colId.setPrefWidth(50);

        TableColumn<Movimiento, Integer> colTarjeta = new TableColumn<>("Tarjeta ID");
        colTarjeta.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdt()));
        colTarjeta.setPrefWidth(80);

        TableColumn<Movimiento, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCantidad()));
        colCantidad.setPrefWidth(100);

        TableColumn<Movimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTipo()));
        colTipo.setPrefWidth(80);

        TableColumn<Movimiento, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDescripcion()));
        colDescripcion.setPrefWidth(250);

        TableColumn<Movimiento, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaMovimiento();
            return new javafx.beans.property.SimpleObjectProperty<>(
                fecha != null ? fecha.format(formatter) : "");
        });
        colFecha.setPrefWidth(150);

        tabla.getColumns().addAll(colId, colTarjeta, colCantidad, colTipo, colDescripcion, colFecha);

        // Cargar datos
        try {
            ObservableList<Movimiento> movimientos = FXCollections.observableArrayList(movimientoDAO.obtenerTodos());
            tabla.setItems(movimientos);
        } catch (Exception ex) {
            mostrarAlerta("Error al cargar movimientos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }

        contenido.getChildren().addAll(titulo, tabla);
        return contenido;
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
