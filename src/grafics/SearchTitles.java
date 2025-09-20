package grafics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mainLogic.Core;

public class SearchTitles {
    
    public static void scanning(String name, BorderPane parent, Core client) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            String response = client.searchAnime(name);
            JsonNode rootNode = mapper.readTree(response).path("response");
            
            // Создаем контейнер для результатов
            VBox resultsContainer = new VBox(20);
            resultsContainer.setPadding(new Insets(20));
            resultsContainer.setStyle("-fx-background-color: #2d2d2d;");
            
            // Заголовок результатов с кнопкой закрытия
            HBox headerBox = new HBox();
            headerBox.setAlignment(Pos.CENTER_LEFT);
            headerBox.setPadding(new Insets(0, 0, 20, 0));
            
            Label titleLabel = new Label("Результаты поиска: \"" + name + "\"");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            titleLabel.setTextFill(Color.WHITE);
            HBox.setHgrow(titleLabel, Priority.ALWAYS);
            
            // Кнопка закрытия
            Button closeButton = new Button("✕");
            closeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; " +
                               "-fx-background-radius: 15; -fx-min-width: 30; -fx-min-height: 30;");
            closeButton.setOnAction(e -> {
                parent.setRight(null); // Убираем панель поиска
            });
            
            headerBox.getChildren().addAll(titleLabel, closeButton);
            
            if (rootNode.isArray() && rootNode.size() > 0) {
                for (JsonNode animeNode : rootNode) {
                    resultsContainer.getChildren().add(createAnimeCard(animeNode));
                }
            } else {
                Label noResults = new Label("Ничего не найдено");
                noResults.setFont(Font.font("Arial", 18));
                noResults.setTextFill(Color.GRAY);
                resultsContainer.getChildren().add(noResults);
            }
            
            // Создаем ScrollPane для прокрутки
            ScrollPane scrollPane = new ScrollPane(resultsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: #2d2d2d; -fx-border-color: #2d2d2d;");
            
            VBox mainContainer = new VBox(10);
            mainContainer.getChildren().addAll(headerBox, scrollPane);
            mainContainer.setPadding(new Insets(10));
            mainContainer.setStyle("-fx-background-color: #2d2d2d;");
            
            // Устанавливаем максимальную ширину для панели поиска
            mainContainer.setMaxWidth(500);
            mainContainer.setMinWidth(400);
            
            parent.setRight(mainContainer);
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка при загрузке данных: " + e.getMessage());
            errorLabel.setTextFill(Color.RED);
            
            VBox errorContainer = new VBox();
            errorContainer.setAlignment(Pos.CENTER);
            errorContainer.getChildren().add(errorLabel);
            
            // Добавляем кнопку закрытия и для ошибки
            Button closeButton = new Button("✕");
            closeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; " +
                               "-fx-background-radius: 15; -fx-min-width: 30; -fx-min-height: 30;");
            closeButton.setOnAction(q -> parent.setRight(null));
            
            HBox errorHeader = new HBox();
            errorHeader.setAlignment(Pos.CENTER_RIGHT);
            errorHeader.getChildren().add(closeButton);
            
            VBox errorBox = new VBox(10, errorHeader, errorContainer);
            errorBox.setPadding(new Insets(10));
            errorBox.setMaxWidth(400);
            
            parent.setRight(errorBox);
        }
    }
    
    private static HBox createAnimeCard(JsonNode animeNode) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 10; -fx-border-radius: 10;");
        
        // poster
        ImageView posterView = createPoster(animeNode.path("poster").path("medium").asText());
        
        // info
        VBox infoBox = new VBox(8);
        infoBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        
        // name
        Label titleLabel = new Label(animeNode.path("title").asText());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setWrapText(true);
        
        // years and type
        HBox metaBox = new HBox(10);
        Label yearLabel = new Label("Год: " + animeNode.path("year").asText());
        Label typeLabel = new Label("Тип: " + animeNode.path("type").path("name").asText());
        yearLabel.setTextFill(Color.LIGHTGRAY);
        typeLabel.setTextFill(Color.LIGHTGRAY);
        metaBox.getChildren().addAll(yearLabel, typeLabel);
        
        // rating
        Label ratingLabel = new Label("Рейтинг: " + String.format("%.2f", animeNode.path("rating").path("average").asDouble()) + " ★");
        ratingLabel.setTextFill(Color.GOLD);
        ratingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // desc
        Label descriptionLabel = new Label(animeNode.path("description").asText());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(Color.LIGHTGRAY);
        descriptionLabel.setFont(Font.font("Arial", 12));
        descriptionLabel.setMaxWidth(400);
        
        // status
        Label statusLabel = new Label("Статус: " + animeNode.path("anime_status").path("title").asText());
        statusLabel.setTextFill(animeNode.path("anime_status").path("value").asInt() == 0 ? 
                               Color.LIGHTGREEN : Color.ORANGE);
        
        infoBox.getChildren().addAll(titleLabel, metaBox, ratingLabel, descriptionLabel, statusLabel);
        
        card.getChildren().addAll(posterView, infoBox);
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #4d4d4d; -fx-background-radius: 10; -fx-border-radius: 10;");
            card.setCursor(javafx.scene.Cursor.HAND);
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 10; -fx-border-radius: 10;");
        });
        
        return card;
    }
    
    private static ImageView createPoster(String posterUrl) {
        String fullPosterUrl = "https:" + posterUrl;
        
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        
        try {
            Image image = new Image(fullPosterUrl, true);
            imageView.setImage(image);
            
            Rectangle placeholder = new Rectangle(120, 170, Color.GRAY);
            StackPane imageContainer = new StackPane(placeholder, imageView);
            
        } catch (Exception e) {
            Rectangle errorRect = new Rectangle(120, 170, Color.DARKGRAY);
            Label errorLabel = new Label("No Image");
            errorLabel.setTextFill(Color.WHITE);
            StackPane errorPane = new StackPane(errorRect, errorLabel);
        }
        
        return imageView;
    }
}