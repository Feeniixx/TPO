package zad1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class GUI extends Application {
    public GUI() {
    }


        public static void launch(String[] args){
            Application.launch(args);
    }

    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest((e) -> {
            Platform.exit();
            System.exit(0);
        });

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800,30));

        JTextField countryTextArea = new JTextField("Poland");
        countryTextArea.setPreferredSize(new Dimension(100,20));

        JTextField cityTextArea = new JTextField("Warsaw");
        cityTextArea.setPreferredSize(new Dimension(100,20));

        JTextField currencyTextArea = new JTextField("USD");
        currencyTextArea.setPreferredSize(new Dimension(100,20));




        FlowPane root = new FlowPane(Orientation.VERTICAL, 30.0D, 30.0D);
        final ChoiceBox<String> WyborKraju = new ChoiceBox(FXCollections.observableArrayList(Service.dostepneKraje()));
        root.getChildren().add(WyborKraju);
        final TextField cityTextField = new TextField();
        root.getChildren().add(cityTextField);
        Button button = new Button("Send");
        root.getChildren().add(button);
        final Label weatherValueLabel = new Label();
        final Label rateForValueLabel = new Label();
        final Label nbrRateValueLabel = new Label();
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String country = (String)WyborKraju.getValue();
                String city = cityTextField.getText();
                if (country != null && city != null) {
                    Service s = new Service(country);
                    weatherValueLabel.setText(s.getWeather(city));
                    rateForValueLabel.setText(s.getRateFor("USD").toString());
                    nbrRateValueLabel.setText(s.getNBPRate().toString());
                }

            }
        });
        root.getChildren().add(new Label("Weather: "));
        root.getChildren().add(weatherValueLabel);
        root.getChildren().add(new Label("RateFor: "));
        root.getChildren().add(rateForValueLabel);
        root.getChildren().add(new Label("NBPRate: "));
        root.getChildren().add(nbrRateValueLabel);
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        Button wikiButton = new Button("Wikipedia");
        root.getChildren().add(wikiButton);
        wikiButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (!cityTextField.getText().equals("")) {
                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().add(browser);
                    Scene newScene = new Scene(secondaryLayout, 720, 480);
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Wiki");
                    newWindow.setScene(newScene);
                    newWindow.initModality(Modality.WINDOW_MODAL);
                    newWindow.initOwner(primaryStage);
                    newWindow.setX(primaryStage.getX() + 400.0D);
                    newWindow.setY(primaryStage.getY() + 250.0D);
                    newWindow.show();
                    String url = "https://pl.wikipedia.org/wiki/" + cityTextField.getText();
                    webEngine.load(url);
                }

            }
        });
        primaryStage.setTitle("WEBCLIENTS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
