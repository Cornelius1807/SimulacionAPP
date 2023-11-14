

package simuladorprocesosapp;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimuladorProcesosApp extends Application {

    private Stage primaryStage;
    private List<Proceso> listaProcesos = new ArrayList<>();
    private int cantidadProcesos = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Simulador de Procesos");

        Label bienvenidaLabel = new Label("Bienvenido al Simulador de Procesos");
        bienvenidaLabel.setFont(new Font("Bauhaus 93 Regular", 40));

        Button comenzarButton = new Button("Comenzar");
        comenzarButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: 'Bauhaus 93 Regular';"
        );

        comenzarButton.setOnAction(event -> mostrarPaginaOpciones());

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50, 0, 0, 0));

        root.getChildren().addAll(bienvenidaLabel, comenzarButton);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void mostrarPaginaOpciones() {
        Label tituloOpciones = new Label("Elige el Algoritmo de Simulación que Deseas Utilizar");
        tituloOpciones.setFont(new Font("Bauhaus 93 Regular", 20));

        VBox opcionesPane = new VBox(20);
        opcionesPane.setAlignment(Pos.CENTER);
        opcionesPane.setPadding(new Insets(50, 0, 0, 0));

        Button roundRobinButton = new Button("Algoritmo Round-Robin");
        Button sjfButton = new Button("Algoritmo SJF");

        roundRobinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bauhaus 93 Regular';");
        sjfButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: 'Bauhaus 93 Regular';");

        roundRobinButton.setOnAction(event -> mostrarIngresoProcesos("Algoritmo Round-Robin"));
        sjfButton.setOnAction(event -> mostrarIngresoProcesos("Algoritmo SJF"));

        opcionesPane.getChildren().addAll(tituloOpciones, roundRobinButton, sjfButton);

        Scene opcionesScene = new Scene(opcionesPane, 800, 600);

        primaryStage.setScene(opcionesScene);
    }

    private void mostrarIngresoProcesos(String algoritmoSeleccionado) {
        listaProcesos.clear();
        cantidadProcesos = 0;

        Label tituloIngreso = new Label("Ingresa la cantidad de procesos:");
        tituloIngreso.setFont(new Font("Bauhaus 93 Regular", 20));

        TextField cantidadProcesosField = new TextField();
        Button siguienteButton = new Button("Siguiente");

        VBox ingresoPane = new VBox(20);
        ingresoPane.setAlignment(Pos.CENTER);
        ingresoPane.setPadding(new Insets(50, 0, 0, 0));
        ingresoPane.getChildren().addAll(tituloIngreso, cantidadProcesosField, siguienteButton);

        Scene ingresoScene = new Scene(ingresoPane, 800, 600);

        siguienteButton.setOnAction(event -> {
            try {
                cantidadProcesos = Integer.parseInt(cantidadProcesosField.getText());
                if (cantidadProcesos > 0) {
                    mostrarIngresoDetalles(algoritmoSeleccionado);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        });

        primaryStage.setScene(ingresoScene);
    }

    private void mostrarIngresoDetalles(String algoritmoSeleccionado) {
        Label tituloDetalles = new Label("Ingresa los detalles del proceso:");
        tituloDetalles.setFont(new Font("Bauhaus 93 Regular", 20));

        TextField nombreProcesoField = new TextField("Nombre del Proceso");
        TextField tiempoEjecucionField = new TextField("Tiempo de Ejecución");
        TextField tiempoLlegadaField = new TextField("Tiempo de Llegada");

        Button siguienteButton = new Button("Siguiente");

        VBox detallesPane = new VBox(20);
        detallesPane.setAlignment(Pos.CENTER);
        detallesPane.setPadding(new Insets(50, 0, 0, 0));
        detallesPane.getChildren().addAll(
                tituloDetalles, nombreProcesoField, tiempoEjecucionField, tiempoLlegadaField, siguienteButton);

        Scene detallesScene = new Scene(detallesPane, 800, 600);

        final int tiempoComienzo = 0;

        siguienteButton.setOnAction(event -> {
            try {
                String nombre = nombreProcesoField.getText();
                int tiempoEjecucion = Integer.parseInt(tiempoEjecucionField.getText());
                int tiempoLlegada = Integer.parseInt(tiempoLlegadaField.getText());

                Proceso proceso = new Proceso(nombre, tiempoEjecucion, tiempoLlegada);
                listaProcesos.add(proceso);

                cantidadProcesos--;

                proceso.tiempoFinalizacion = tiempoComienzo + proceso.tiempoEjecucion;

                if (cantidadProcesos > 0) {
                    mostrarIngresoDetalles(algoritmoSeleccionado);
                } else {
                    mostrarDatosEnviados(algoritmoSeleccionado);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        });

        primaryStage.setScene(detallesScene);
    }

    private void mostrarDatosEnviados(String algoritmoSeleccionado) {
        Label tituloDatosEnviados = new Label("Datos Enviados para " + algoritmoSeleccionado);
        tituloDatosEnviados.setFont(new Font("Bauhaus 93 Regular", 20));

        Collections.sort(listaProcesos, Comparator.comparingInt(p -> p.tiempoLlegada));

        int quantum = 20;
        int tiempoActual = listaProcesos.get(0).tiempoLlegada;
        List<Proceso> procesosPendientes = new ArrayList<>(listaProcesos);
        List<Proceso> procesosTerminados = new ArrayList<>();

        while (!procesosPendientes.isEmpty()) {
            Proceso procesoActual = procesosPendientes.remove(0);

            procesoActual.tiempoComienzo = Math.max(tiempoActual, procesoActual.tiempoLlegada);

            if (procesoActual.tiempoEjecucion > quantum) {
                procesoActual.tiempoEjecucion -= quantum;
                tiempoActual += quantum;
                procesosPendientes.add(procesoActual);
            } else {
                tiempoActual += procesoActual.tiempoEjecucion;
                procesoActual.tiempoEjecucion = 0;
                procesoActual.tiempoFinalizacion = tiempoActual;
                procesosTerminados.add(procesoActual);
            }

            if (!procesosPendientes.isEmpty()) {
                tiempoActual += 1;
            }
        }

        tiempoActual = 0;
        for (Proceso proceso : procesosTerminados) {
            System.out.println("Tiempo " + tiempoActual + ": " + proceso.nombre + " entra a ejecución");
            tiempoActual = proceso.tiempoFinalizacion;
        }
        System.out.println("Tiempo " + tiempoActual + ": Se ejecutaron todos los procesos");

        TableView<Proceso> tablaProcesos = new TableView<>();
        TableColumn<Proceso, String> procesoColumna = new TableColumn<>("Proceso");
        TableColumn<Proceso, Integer> tiempoEjecucionColumna = new TableColumn<>("Tiempo de Ejecución");
        TableColumn<Proceso, Integer> tiempoLlegadaColumna = new TableColumn<>("Tiempo de Llegada");
        TableColumn<Proceso, Integer> tiempoComienzoColumna = new TableColumn<>("Tiempo de Comienzo");
        TableColumn<Proceso, Integer> tiempoFinalizacionColumna = new TableColumn<>("Tiempo de Finalización");

        procesoColumna.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nombre));
        tiempoEjecucionColumna.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().tiempoEjecucion).asObject());
        tiempoLlegadaColumna.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().tiempoLlegada).asObject());
        tiempoComienzoColumna.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().tiempoComienzo).asObject());
        tiempoFinalizacionColumna.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().tiempoFinalizacion).asObject());

        tablaProcesos.getColumns().addAll(procesoColumna, tiempoEjecucionColumna, tiempoLlegadaColumna, tiempoComienzoColumna, tiempoFinalizacionColumna);

        ObservableList<Proceso> procesosObservableList = FXCollections.observableArrayList(procesosTerminados);
        tablaProcesos.setItems(procesosObservableList);

        Button regresarButton = new Button("Regresar");
        regresarButton.setOnAction(event -> mostrarPaginaOpciones());

        VBox datosEnviadosPane = new VBox(20);
        datosEnviadosPane.setAlignment(Pos.CENTER);
        datosEnviadosPane.setPadding(new Insets(50, 0, 0, 0));
        datosEnviadosPane.getChildren().addAll(tituloDatosEnviados, tablaProcesos, regresarButton);

        Scene datosEnviadosScene = new Scene(datosEnviadosPane, 800, 600);

        primaryStage.setScene(datosEnviadosScene);
    }

    private static class Proceso {
        private String nombre;
        private int tiempoEjecucion;
        private int tiempoLlegada;
        private int tiempoComienzo;
        private int tiempoFinalizacion;

        Proceso(String nombre, int tiempoEjecucion, int tiempoLlegada) {
            this.nombre = nombre;
            this.tiempoEjecucion = tiempoEjecucion;
            this.tiempoLlegada = tiempoLlegada;
        }

        @Override
        public String toString() {
            return "Proceso{" +
                    "nombre='" + nombre + '\'' +
                    ", tiempoEjecucion=" + tiempoEjecucion +
                    ", tiempoLlegada=" + tiempoLlegada +
                    ", tiempoComienzo=" + tiempoComienzo +
                    ", tiempoFinalizacion=" + tiempoFinalizacion +
                    '}';
        }
    }
}
