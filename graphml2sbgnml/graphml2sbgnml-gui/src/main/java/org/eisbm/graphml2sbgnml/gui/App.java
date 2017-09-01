package org.eisbm.graphml2sbgnml.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eisbm.graphml2sbgnml.cli.Graphml2sbgnml;
import org.eisbm.graphml2sbgnml.cli.Sbgnml2graphml;

import java.io.*;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graphml <-> Sbgnml");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().add(grid);

        /*Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);*/

        // --- 0th row --- //
        Label directionLabel = new Label("Convertion Direction:");
        grid.add(directionLabel, 0, 0);

        ChoiceBox directionChoice = new ChoiceBox(FXCollections.observableArrayList(
                "GraphML -> SBGN-ML", "SBGN-ML -> GraphML")
        );
        grid.add(directionChoice, 1, 0);
        directionChoice.getSelectionModel().selectFirst(); // set first as default

        // --- 1st row --- //
        Label inputFileLabel = new Label("Input File:");
        grid.add(inputFileLabel, 0, 1);

        TextField inputFileText = new TextField();
        grid.add(inputFileText, 1, 1);

        FileChooser inputFileChooser = new FileChooser();

        Button inputFileOpenButton = new Button("Choose file");
        grid.add(inputFileOpenButton, 2, 1);
        inputFileOpenButton.setOnAction(
                e -> {
                    File file = inputFileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        inputFileText.setText(file.getAbsolutePath());
                    }
                });

        // --- 2nd row --- //
        Label outputFileLabel = new Label("Output File:");
        grid.add(outputFileLabel, 0, 2);

        TextField outputFileText = new TextField();
        grid.add(outputFileText, 1, 2);

        FileChooser outputFileChooser = new FileChooser();

        Button outputFileOpenButton = new Button("Save to");
        grid.add(outputFileOpenButton, 2, 2);
        outputFileOpenButton.setOnAction(
                e -> {
                    File file = outputFileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        outputFileText.setText(file.getAbsolutePath());
                    }
                });

        // --- 3rd row --- //
        Label configFileLabel = new Label("Configuration File:");
        grid.add(configFileLabel, 0, 3);

        TextField configFileText = new TextField();
        grid.add(configFileText, 1, 3);

        FileChooser configFileChooser = new FileChooser();

        Button configFileOpenButton = new Button("Choose file");
        grid.add(configFileOpenButton, 2, 3);
        configFileOpenButton.setOnAction(
                e -> {
                    File file = configFileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        configFileText.setText(file.getAbsolutePath());
                    }
                });

        // --- final row --- //
        Button convertButton = new Button("Convert");
        grid.add(convertButton, 1, 4, 3,1);
        convertButton.setOnAction(e -> {

            // check arguments
            if(inputFileText.getText().isEmpty()) {
                throw new IllegalArgumentException("No input file provided");
            }
            if(outputFileText.getText().isEmpty()) {
                throw new IllegalArgumentException("No output file provided");
            }

            if(directionChoice.getValue().equals("SBGN-ML -> GraphML")) {
                Sbgnml2graphml.convert(inputFileText.getText(), outputFileText.getText());
            }
            else {
                if(configFileText.getText().isEmpty()) {
                    throw new IllegalArgumentException("No configuration file provided");
                }

                Graphml2sbgnml.convert(inputFileText.getText(),
                        outputFileText.getText(), configFileText.getText());
            }
        });

        // --- console --- //
        TextArea console = new TextArea();
        console.setEditable(false);
        //grid.add(console, 0, 5, 4, 1);
        vbox.getChildren().add(console);
        PrintStream printStream = new PrintStream(new TextOutputStream(console));
        System.setOut(printStream);
        System.setErr(printStream);

        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setScene(scene);


        // other events
        // react to change to disable configuration line if needed
        directionChoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue.equals("SBGN-ML -> GraphML")) {
                        configFileText.setDisable(true);
                        configFileOpenButton.setDisable(true);
                        configFileLabel.setDisable(true);
                    }
                    else {
                        configFileText.setDisable(false);
                        configFileOpenButton.setDisable(false);
                        configFileLabel.setDisable(false);
                    }
                }
        );

        primaryStage.show();
    }

    public class TextOutputStream extends OutputStream {
        TextArea textArea;

        public TextOutputStream(TextArea textArea){
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.appendText(String.valueOf((char)b));
            // scrolls the text area to the end of data
            textArea.positionCaret(textArea.getText().length());
        }
    }
}
