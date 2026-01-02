package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherRadarApp extends Application {
    private static final String API_KEY = "Your own API_KEY";
    private Label cityLabel = new Label();
    private Label tempLabel = new Label();
    private Label conditionLabel = new Label();
    private WebView webView;

    @Override
    public void start(Stage stage) {
        TextField cityInput = new TextField();
        cityInput.setPromptText("Type the city name");

        Button fetchButton = new Button("Search");
        fetchButton.setOnAction(e -> {
            String city = cityInput.getText().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
                updateMap(city);
            }
        });

        VBox infoBox = new VBox(5, cityLabel, tempLabel, conditionLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox controls = new VBox(10, cityInput, fetchButton, infoBox);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        webView = new WebView();
        webView.setPrefHeight(400);

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(webView);

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Weather Radar");
        stage.setScene(scene);
        stage.show();
    }

    private void fetchWeather(String city) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=pt_br",
                encodedCity, API_KEY
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    if (json.has("main")) {
                        double temp = json.getAsJsonObject("main").get("temp").getAsDouble();
                        String condition = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

                        Platform.runLater(() -> {
                            cityLabel.setText("City: " + city);
                            tempLabel.setText("Temp: " + temp + "°C");
                            conditionLabel.setText("Condition: " + condition);
                        });
                    } else {
                        Platform.runLater(() -> {
                            cityLabel.setText("City not found");
                            tempLabel.setText("");
                            conditionLabel.setText("");
                        });
                    }
                });
    }

    private double[] getCoordinates(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedCity;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "WeatherRadarApp")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body())
                    .getAsJsonArray().get(0).getAsJsonObject();

            double lat = json.get("lat").getAsDouble();
            double lon = json.get("lon").getAsDouble();
            return new double[]{lat, lon};
        } catch (Exception e) {
            System.out.println("Error when searching for coordinates: " + e.getMessage());
            return new double[]{0, 0};
        }
    }

    private void updateMap(String city) {
        double[] coords = getCoordinates(city);
        double lat = coords[0];
        double lon = coords[1];

        if (lat == 0 && lon == 0) {
            webView.getEngine().loadContent("<html><body><h3>City not found</h3></body></html>");
            return;
        }

        try (InputStream input = getClass().getResourceAsStream("/map.html")) {
            String template = new Scanner(input, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            String html = template
                    .replace("{{lat}}", String.valueOf(lat))
                    .replace("{{lon}}", String.valueOf(lon))
                    .replace("{{city}}", city);

            webView.getEngine().setJavaScriptEnabled(true);
            webView.getEngine().loadContent(html);
        } catch (Exception e) {
            webView.getEngine().loadContent("<html><body><h3>Error loading map</h3></body></html>");
            System.out.println("Error loading map: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}