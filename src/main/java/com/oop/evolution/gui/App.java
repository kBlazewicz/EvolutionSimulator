package com.oop.evolution.gui;

import com.oop.evolution.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDate;
import java.time.LocalTime;

public class App extends Application {

    private final Label daysLabel = new Label();
    private final Label daysLabel2 = new Label();
    private final Label genome = new Label();
    private final Label genome2 = new Label();
    private final GridPane grid1 = new GridPane();
    private final GridPane grid2 = new GridPane();
    private final XYChart.Series seriesAnimalsPopulation = new XYChart.Series();
    private final XYChart.Series seriesPlantsPopulation = new XYChart.Series();
    private final XYChart.Series seriesAverageEnergy = new XYChart.Series();
    private final XYChart.Series seriesAverageChildrenAmount = new XYChart.Series();
    private final XYChart.Series seriesAverageLifeTime = new XYChart.Series();
    private final XYChart.Series series2AnimalsPopulation = new XYChart.Series();
    private final XYChart.Series series2PlantsPopulation = new XYChart.Series();
    private final XYChart.Series series2AverageEnergy = new XYChart.Series();
    private final XYChart.Series series2AverageChildrenAmount = new XYChart.Series();
    private final XYChart.Series series2AverageLifeTime = new XYChart.Series();
    private final double size = 400;
    private boolean tracking = false;
    private int startTrackingChildAmount;
    private Animal trackedAnimal;
    private Label trackedGenome = new Label();
    private Label trackedDescendantAmount = new Label();
    private Label trackedDeath = new Label();
    private Label trackedChildrenAmount = new Label();
    private SimulationEngine engine;
    private SimulationEngine engine2;
    private Stage window;
    private Scene scene;
    private int height = 1;
    private int width = 1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setX(200);
        window.setY(200);
        grid1.setGridLinesVisible(true);
        grid2.setGridLinesVisible(true);

        //form Scene
        Scene scene2 = new Scene(createForm(), 1400, 1000);

        //Main scene
        scene = new Scene(createRoot(), 1400, 800);

        window.setScene(scene2);
        window.show();

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (engine != null) {
                    engine.shutdown();
                }
                if (engine2 != null) {
                    engine2.shutdown();
                }
            }
        });
    }

    private void seriesBuilder(LineChart<Number, Number> lineChart2, XYChart.Series series2AnimalsPopulation, XYChart.Series series2PlantsPopulation, XYChart.Series series2AverageEnergy, XYChart.Series series2AverageChildrenAmount, XYChart.Series series2AverageLifeTime) {
        series2AnimalsPopulation.setName("Animals amount");
        lineChart2.getData().add(series2AnimalsPopulation);

        series2PlantsPopulation.setName("Plants amount");
        lineChart2.getData().add(series2PlantsPopulation);

        series2AverageEnergy.setName("Average energy");
        lineChart2.getData().add(series2AverageEnergy);

        series2AverageChildrenAmount.setName("Average children amount");
        lineChart2.getData().add(series2AverageChildrenAmount);

        series2AverageLifeTime.setName("Average length of life");
        lineChart2.getData().add(series2AverageLifeTime);
    }

    private void renderGrid(GridPane grid, SimulationEngine engine) throws InterruptedException {

        grid.getChildren().clear();
        AbstractMap map = engine.getMap();
        Vector2d size = map.getSize();
        if (tracking) {
            trackedGenome.setText("Genome: " + trackedAnimal.getGenome().getGenome());
            trackedDescendantAmount.setText("Descendant: " + trackedAnimal.getDescendantsAmount());
            trackedDeath.setText("Death day: " + trackedAnimal.getDeathTime());
            int children = trackedAnimal.getChildrenAmount() - startTrackingChildAmount;
            trackedChildrenAmount.setText("Children amount: " + children);
        }
        for (int i = 0; i < size.x + 1; i++) {
            for (int j = 0; j < size.y + 1; j++) {
                Vector2d position = new Vector2d(i, j);
                Pane b = new Pane();
                b.setMinSize(20, 20);
                if (!map.animalsAt(position).isEmpty()) {
                    double percentage = map.strongestAnimal(map.animalsAt(position)).getEnergyPercentage();

                    if (new GenomeComparator().compare(map.strongestAnimal(map.animalsAt(position)).getGenome().getGenome(), engine.getMap().getDominantGenome()) == 0) {
                        b.setStyle("-fx-background-color: #000000; ");
                    } else if (percentage < 0.3) {
                        b.setStyle("-fx-background-color: #e796e7; ");
                    } else if (percentage < 0.6) {
                        b.setStyle("-fx-background-color: #e764b7; ");
                    } else if (percentage < 0.9) {
                        b.setStyle("-fx-background-color: #e728a7; ");
                    } else {
                        b.setStyle("-fx-background-color: #b30255; ");
                    }
                    b.setOnMouseClicked(event -> {
                        trackedAnimal = map.strongestAnimal(map.animalsAt(position));
                        tracking = true;
                        startTrackingChildAmount = trackedAnimal.getChildrenAmount();
                    });

                } else if (map.isPlantOnField(position)) {
                    b.setStyle("-fx-background-color: #08ff00; ");
                } else if (map.isJungle(position)) {
                    b.setStyle("-fx-background-color: #4c771f; ");
                } else {
                    b.setStyle("-fx-background-color: #dbcf85; ");
                }
                b.setMinSize(this.size / width, this.size / height);
                grid.add(b, i, j, 1, 1);
            }
        }
    }

    private void updateMap1() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int date1 = engine.getDate();
                daysLabel.setText("current day on Map 1: " + date1);
                refreshStatistics(date1, genome, engine, seriesAnimalsPopulation, seriesPlantsPopulation, seriesAverageEnergy, seriesAverageChildrenAmount, seriesAverageLifeTime, grid1);
            }
        });
    }

    private void updateMap2() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int date2 = engine2.getDate();
                daysLabel2.setText("current day on Map 2: " + date2);
                refreshStatistics(date2, genome2, engine2, series2AnimalsPopulation, series2PlantsPopulation, series2AverageEnergy, series2AverageChildrenAmount, series2AverageLifeTime, grid2);
            }
        });
    }

    private void refreshStatistics(int date1, Label genome, SimulationEngine engine, XYChart.Series seriesAnimalsPopulation, XYChart.Series seriesPlantsPopulation, XYChart.Series seriesAverageEnergy, XYChart.Series seriesAverageChildrenAmount, XYChart.Series seriesAverageLifeTime, GridPane grid1) {
        genome.setText("Dominant genome: " + engine.getMap().getDominantGenome());
        seriesAnimalsPopulation.getData().add(new XYChart.Data(date1, engine.getMap().getAnimalArrayList().size()));
        seriesPlantsPopulation.getData().add(new XYChart.Data(date1, engine.getMap().numberOfPlants()));
        seriesAverageEnergy.getData().add(new XYChart.Data(date1, engine.averageEnergy()));
        seriesAverageChildrenAmount.getData().add(new XYChart.Data(date1, engine.averageAmountOfChildren()));
        seriesAverageLifeTime.getData().add(new XYChart.Data(date1, engine.averageLifeTime()));
        try {
            renderGrid(grid1, engine);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HBox createRoot() {
        final Button play = new Button("Play1");
        final Button pause = new Button("Pause1");
        final Button play2 = new Button("Play2");
        final Button pause2 = new Button("Pause2");
        final Button off = new Button("Off");
        final Button saveData = new Button("Save Data");
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);

        VBox buttons = new VBox();
        VBox Map1 = new VBox();
        VBox Map2 = new VBox();

        Map1.getChildren().add(play);
        Map1.getChildren().add(pause);
        Map1.getChildren().add(grid1);
        Map1.getChildren().add(daysLabel);
        Map1.getChildren().add(genome);

        Map2.getChildren().add(play2);
        Map2.getChildren().add(pause2);
        Map2.getChildren().add(grid2);
        Map2.getChildren().add(daysLabel2);
        Map2.getChildren().add(genome2);


        buttons.getChildren().add(saveData);
        buttons.getChildren().add(off);
        buttons.getChildren().add(trackedGenome);
        buttons.getChildren().add(trackedDescendantAmount);
        buttons.getChildren().add(trackedDeath);
        buttons.getChildren().add(trackedChildrenAmount);
        root.getChildren().add(Map1);
        root.getChildren().add(Map2);
        root.getChildren().add(buttons);
        //chart1
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");

        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Map 1 Statisitcs");
        Map1.getChildren().add(lineChart);


        //chart2
        final NumberAxis xAxis2 = new NumberAxis();
        final NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setLabel("Day");

        final LineChart<Number, Number> lineChart2 =
                new LineChart<Number, Number>(xAxis2, yAxis2);

        lineChart2.setTitle("Map 2 Statisitcs");
        Map2.getChildren().add(lineChart2);

        seriesBuilder(lineChart2, series2AnimalsPopulation, series2PlantsPopulation, series2AverageEnergy,
                series2AverageChildrenAmount, series2AverageLifeTime);
        seriesBuilder(lineChart, seriesAnimalsPopulation, seriesPlantsPopulation, seriesAverageEnergy,
                seriesAverageChildrenAmount, seriesAverageLifeTime);


        play.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine.start();
            }
        });
        pause.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine.getMap().getDominantGenome();
                engine.stop();
            }
        });
        play2.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine2.start();
            }
        });
        pause2.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine2.stop();
                engine2.getMap().getDominantGenome();
            }
        });
        off.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine.shutdown();
                engine2.shutdown();
            }
        });
        saveData.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String data1 = "Wrapped," + engine.getDate() + "," + engine.getMap().getAnimalArrayList().size() + "," +
                        engine.getMap().numberOfPlants() + "," + engine.averageEnergy() + ","
                        + engine.averageAmountOfChildren() + "," + engine.averageLifeTime() + "," + LocalDate.now() + " " + LocalTime.now();

                String data2 = "Bounded," + engine2.getDate() + "," + engine2.getMap().getAnimalArrayList().size() + "," +
                        engine2.getMap().numberOfPlants() + "," + engine2.averageEnergy() + ","
                        + engine2.averageAmountOfChildren() + "," + engine2.averageLifeTime() + "," + LocalDate.now() + " " + LocalTime.now();
                try {
                    HandlerCSV.addRecord(data1);
                    HandlerCSV.addRecord(data2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    private GridPane createForm() {
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));

        Text formTxt = new Text("Create simulation");
        formTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
        formGrid.add(formTxt, 0, 0);

        //Form Field
        Label lblWidth = new Label("Widht");
        TextField txtWidth = new TextField("15");
        formGrid.add(lblWidth, 0, 1);
        formGrid.add(txtWidth, 1, 1);

        //Form Field
        Label lblHeight = new Label("Height");
        TextField txtHeight = new TextField("15");
        formGrid.add(lblHeight, 0, 2);
        formGrid.add(txtHeight, 1, 2);

        //Form Field
        Label lblStartEnergy = new Label("StartEnergy");
        TextField txtStartEnergy = new TextField("100");
        formGrid.add(lblStartEnergy, 0, 3);
        formGrid.add(txtStartEnergy, 1, 3);

        //Form Field
        Label lblMoveEnergy = new Label("MoveEnergy");
        TextField txtMoveEnergy = new TextField("3");
        formGrid.add(lblMoveEnergy, 0, 4);
        formGrid.add(txtMoveEnergy, 1, 4);

        //Form Field
        Label lblAnimalsAmount = new Label("AnimalsAmount");
        TextField txtAnimalsAmount = new TextField("30");
        formGrid.add(lblAnimalsAmount, 0, 5);
        formGrid.add(txtAnimalsAmount, 1, 5);

        //Form Field
        Label lblPlantEnergySource = new Label("PlantEnergySource");
        TextField txtPlantEnergySource = new TextField("50");
        formGrid.add(lblPlantEnergySource, 0, 6);
        formGrid.add(txtPlantEnergySource, 1, 6);

        //Form Field
        Label lblJungleRatio = new Label("JungleRatio");
        TextField txtJungleRatio = new TextField("0.3");
        formGrid.add(lblJungleRatio, 0, 7);
        formGrid.add(txtJungleRatio, 1, 7);


        Button parametersBtn = new Button("Set");
        formGrid.add(parametersBtn, 1, 8);
        parametersBtn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    window.setScene(scene);
                    stop();
                    engine = new SimulationEngine(Integer.parseInt(txtWidth.getText()),
                            Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtStartEnergy.getText()),
                            Integer.parseInt(txtMoveEnergy.getText()), Integer.parseInt(txtAnimalsAmount.getText()),
                            Double.parseDouble(txtJungleRatio.getText()),
                            Integer.parseInt(txtPlantEnergySource.getText()), false);

                    engine2 = new SimulationEngine(Integer.parseInt(txtWidth.getText()),
                            Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtStartEnergy.getText()),
                            Integer.parseInt(txtMoveEnergy.getText()), Integer.parseInt(txtAnimalsAmount.getText()),
                            Double.parseDouble(txtJungleRatio.getText()),
                            Integer.parseInt(txtPlantEnergySource.getText()), true);

                    width = Integer.parseInt(txtWidth.getText());
                    height = Integer.parseInt(txtHeight.getText());
                    renderGrid(grid1, engine);
                    renderGrid(grid2, engine2);
                    engine.addMapChangeListener(new IMapChangeObserver() {
                        @Override
                        public void refreshMap() throws InterruptedException {
                            updateMap1();
                        }
                    });
                    engine2.addMapChangeListener(new IMapChangeObserver() {
                        @Override
                        public void refreshMap() throws InterruptedException {
                            updateMap2();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return formGrid;
    }
}