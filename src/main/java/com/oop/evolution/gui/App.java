package com.oop.evolution.gui;

import com.oop.evolution.AbstractMap;
import com.oop.evolution.IMapChangeObserver;
import com.oop.evolution.SimulationEngine;
import com.oop.evolution.Vector2d;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
    private SimulationEngine engine;
    private SimulationEngine engine2;
    private final Label daysLabel = new Label("current day: ");
    private final Label daysLabel2 = new Label("current day: ");
    private final Button play = new Button("Play1");
    private Button pause = new Button("Pause1");
    private final Button play2 = new Button("Play2");
    private Button pause2 = new Button("Pause2");
    private final Button off = new Button("Off");
    private final GridPane grid1 = new GridPane();
    private final GridPane grid2 = new GridPane();
    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setX(200);
        window.setY(200);
        grid1.setGridLinesVisible(true);
        grid2.setGridLinesVisible(true);

        window.setTitle("Simulation");

        //form Scene
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));

        Text formTxt = new Text("Create simulation");
        formTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
        formGrid.add(formTxt, 0, 0);
        Scene scene2 = new Scene(formGrid, 1000, 1000);

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
        TextField txtPlantEnergySource = new TextField("150");
        formGrid.add(lblPlantEnergySource, 0, 6);
        formGrid.add(txtPlantEnergySource, 1, 6);

        //Form Field
        Label lblJungleRatio = new Label("JungleRatio");
        TextField txtJungleRatio = new TextField("0.5");
        formGrid.add(lblJungleRatio, 0, 7);
        formGrid.add(txtJungleRatio, 1, 7);


        Button parametersBtn = new Button("Set");
        formGrid.add(parametersBtn, 1, 8);


        //Main scene
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);

        VBox buttons = new VBox();
        Button changeScene = new Button("Set parameters");
        buttons.getChildren().add(changeScene);
        buttons.getChildren().add(play);
        buttons.getChildren().add(pause);
        buttons.getChildren().add(play2);
        buttons.getChildren().add(pause2);
        buttons.getChildren().add(off);
        buttons.getChildren().add(daysLabel);
        buttons.getChildren().add(daysLabel2);
        root.getChildren().add(grid1);
        root.getChildren().add(buttons);
        root.getChildren().add(grid2);
        Scene scene = new Scene(root, 1000, 1000);

        play.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine.start();
            }
        });
        pause.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
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
            }
        });
        off.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                engine.shutdown();
                engine2.shutdown();
            }
        });



        changeScene.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    engine.stop();
                    engine2.stop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.setScene(scene2);
            }
        });
        parametersBtn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                try {
                    window.setScene(scene);
                    stop();
                    engine = new SimulationEngine(Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtStartEnergy.getText()), Integer.parseInt(txtMoveEnergy.getText()), Integer.parseInt(txtAnimalsAmount.getText()), Double.parseDouble(txtJungleRatio.getText()), Integer.parseInt(txtPlantEnergySource.getText()),false);
                    engine2 = new SimulationEngine(Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtStartEnergy.getText()), Integer.parseInt(txtMoveEnergy.getText()), Integer.parseInt(txtAnimalsAmount.getText()), Double.parseDouble(txtJungleRatio.getText()), Integer.parseInt(txtPlantEnergySource.getText()),true);
//                    engine = new SimulationEngine(Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtStartEnergy.getText()), Integer.parseInt(txtMoveEnergy.getText()), Integer.parseInt(txtAnimalsAmount.getText()), Double.parseDouble(txtJungleRatio.getText()), Integer.parseInt(txtPlantEnergySource.getText()),true);
                    renderGrid(grid1,engine);
                    renderGrid(grid2, engine2);
                    engine.addMapChangeListener(new IMapChangeObserver() {
                        @Override
                        public void refreshMap() throws InterruptedException {
                            updateMap();
                        }
                    });
                    engine2.addMapChangeListener(new IMapChangeObserver() {
                        @Override
                        public void refreshMap() throws InterruptedException {
                            updateMap();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        window.setScene(scene2);

        window.show();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(engine != null) {
                    engine.shutdown();
                }
                if(engine2 != null) {
                    engine2.shutdown();
                }
            }
        });
    }

    private void renderGrid(GridPane grid, SimulationEngine engine) throws InterruptedException {

        grid.getChildren().clear();
        AbstractMap map = engine.getMap();
        Vector2d size = map.getSize();

        for (int i = 0; i < size.x + 1; i++) {
            for (int j = 0; j < size.y + 1; j++) {
                Vector2d position = new Vector2d(i, j);
                Button b = new Button();
                b.setMinSize(20, 20);
                if (!map.animalsAt(position).isEmpty()) {
                    double percentage = map.strongestAnimal(map.animalsAt(position)).getEnergyPercentage();

                    if(percentage < 0.3){
                        b.setStyle("-fx-background-color: #e796e7; ");
                    }
                    else if(percentage < 0.6){
                        b.setStyle("-fx-background-color: #e764b7; ");
                    }
                    else if(percentage < 0.9){
                        b.setStyle("-fx-background-color: #e728a7; ");
                    }

                    else{
                        b.setStyle("-fx-background-color: #b30255; ");
                    }


                } else if (map.isPlantOnField(position)) {
                    b.setStyle("-fx-background-color: #08ff00; ");
                } else if (map.isJungle(position)) {
                    b.setStyle("-fx-background-color: #4c771f; ");
                } else {
                    b.setStyle("-fx-background-color: #dbcf85; ");
                }
                grid.add(b, i, j, 1, 1);
            }
        }
    }

    private void updateMap() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                daysLabel.setText(String.valueOf("current day on Map1: " + engine.getDate()));
                daysLabel2.setText(String.valueOf("current day on Map2: " + engine2.getDate()));
                try {
                    renderGrid(grid1, engine);
                    renderGrid(grid2, engine2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
