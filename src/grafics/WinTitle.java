package grafics;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class WinTitle extends Stage {    
    private Image image;
    private String name;
    private String desc;
    private JsonNode data;
    private BorderPane root;
    private MediaPlayer mediaPlayer;

    public WinTitle(Image image, String name, String desc, JsonNode data){
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.data = data;
    }
        
    public BorderPane createWin() {
        BorderPane root = new BorderPane();
        root.setMinSize(1920, 1080);
        
        StackPane mainContainer = new StackPane();
    
        Rectangle blurBackground = new Rectangle(1920, 1080);
        blurBackground.setFill(Color.rgb(100, 100, 100, 0.1));
        GaussianBlur blur = new GaussianBlur(50);
        blurBackground.setEffect(blur);
        
        Rectangle darkOverlay = new Rectangle(1920, 1080);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.7));
        darkOverlay.setArcHeight(20);
        darkOverlay.setArcWidth(20);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setMaxSize(1600, 900);
        contentPane.setStyle("""
            -fx-background-color: rgba(30, 30, 40, 0.95);
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255, 255, 255, 0.1);
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 20, 0, 0, 5);
        """);
        
        // top panel
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        topPanel.setPadding(new Insets(20));
        topPanel.setStyle("-fx-background-color: linear-gradient(to right, rgba(25, 25, 35, 0.9), rgba(35, 35, 45, 0.9));");
        
        Label titleLabel = new Label(name);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 10, 0, 0, 2);");
        
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 16px;
            -fx-min-width: 40px;
            -fx-min-height: 40px;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-cursor: hand;
        """);
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(closeBtn.getStyle() + "-fx-background-color: #c0392b;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(closeBtn.getStyle() + "-fx-background-color: #e74c3c;"));
        
        topPanel.getChildren().addAll(titleLabel, closeBtn);
        
        HBox centerContent = new HBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_CENTER);
        
        // info panel
        VBox infoPanel = new VBox(15);
        infoPanel.setMaxWidth(300);
        infoPanel.setPadding(new Insets(15));
        infoPanel.setStyle("""
            -fx-background-color: rgba(40, 40, 50, 0.8);
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);
        
        // poster
        ImageView poster = new ImageView(image);
        poster.setFitWidth(250);
        poster.setFitHeight(350);
        poster.setPreserveRatio(true);
        poster.setStyle("""
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 15, 0, 0, 3);
            -fx-background-radius: 10;
        """);
        
        // genres
        String genres = "";
        JsonNode nodeGenres = data.path("genres");
        if (nodeGenres.isArray()) {
            for(JsonNode genre: nodeGenres)
                genres += (genre.path("title").asText("None")) + " ";
        }
        
        Label genresLabel = new Label("Жанры: " + genres);
        genresLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        genresLabel.setTextFill(Color.LIGHTGRAY);
        genresLabel.setWrapText(true);
        genresLabel.setMaxWidth(250);
        
        // description
        Label descLabel = new Label(desc);
        descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descLabel.setTextFill(Color.WHITE);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(250);
        descLabel.setTextAlignment(TextAlignment.JUSTIFY);
        
        infoPanel.getChildren().addAll(poster, genresLabel, descLabel);
        
        // video player and choose series
        VBox videoPanel = new VBox(15);
        videoPanel.setAlignment(Pos.TOP_CENTER);
        
        // video player container
        StackPane videoPlayerContainer = new StackPane();
        videoPlayerContainer.setMinSize(900, 500);
        videoPlayerContainer.setStyle("""
            -fx-background-color: rgba(0, 0, 0, 0.9);
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: rgba(255, 255, 255, 0.1);
        """);
        
        // placeholder label
        Label videoPlaceholder = new Label("Выберите серию для просмотра");
        videoPlaceholder.setTextFill(Color.GRAY);
        videoPlaceholder.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // MediaView
        MediaView mediaView = new MediaView();
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(450);
        mediaView.setPreserveRatio(true);
        mediaView.setVisible(false);
        videoPlayerContainer.getChildren().addAll(videoPlaceholder, mediaView);
        
        // series container
        VBox seriesContainer = new VBox(8);
        seriesContainer.setPadding(new Insets(10));
        seriesContainer.setStyle("-fx-background-color: rgba(35, 35, 45, 0.8); -fx-background-radius: 10;");
        
        ArrayList<Integer> series = new ArrayList<>();
        for (int i = 1; i <= 55; i++) {
            series.add(i);
        }
        
        for(int s: series) {
            Button episodeBtn = new Button("Серия " + s);
            episodeBtn.setStyle("""
                -fx-background-color: rgba(60, 60, 80, 0.8);
                -fx-text-fill: white;
                -fx-font-size: 12px;
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-cursor: hand;
                -fx-padding: 8 15;
            """);
            episodeBtn.setOnMouseEntered(e -> episodeBtn.setStyle("""
                -fx-background-color: rgba(80, 80, 100, 0.9);
                -fx-text-fill: white;
                -fx-font-size: 12px;
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-cursor: hand;
                -fx-padding: 8 15;
            """));
            episodeBtn.setOnMouseExited(e -> episodeBtn.setStyle("""
                -fx-background-color: rgba(60, 60, 80, 0.8);
                -fx-text-fill: white;
                -fx-font-size: 12px;
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-cursor: hand;
                -fx-padding: 8 15;
            """));
            
            // Обработчик выбора серии
            episodeBtn.setOnAction(e -> {
                loadVideoForEpisode(s, mediaView, videoPlaceholder);
            });
            
            seriesContainer.getChildren().add(episodeBtn);
        }
        
        ScrollPane seriesScroll = new ScrollPane(seriesContainer);
        seriesScroll.setPrefHeight(400);
        seriesScroll.setFitToWidth(true);
        seriesScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        seriesScroll.setStyle("""
            -fx-background-color: transparent;
            -fx-background: transparent;
            -fx-border-color: transparent;
        """);
        
        videoPanel.getChildren().addAll(videoPlayerContainer, seriesScroll);
        
        centerContent.getChildren().addAll(infoPanel, videoPanel);
        
        contentPane.setTop(topPanel);
        contentPane.setCenter(centerContent);
        
        mainContainer.getChildren().addAll(blurBackground, darkOverlay, contentPane);
        root.setCenter(mainContainer);
        
        // Close action
        closeBtn.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            Node source = (Node) e.getSource();
            Scene scene = source.getScene();
            
            if (scene != null) {
                Parent rootParent = scene.getRoot();
                if (rootParent instanceof BorderPane) {
                    BorderPane mainRoot = (BorderPane) rootParent;
                    mainRoot.getChildren().remove(root);
                }
            }
        });
       
        return root;
    }
    
    private void loadVideoForEpisode(int episode, MediaView mediaView, Label placeholder) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            
            String videoUrl = getVideoUrlForEpisode(episode);
            
            if (videoUrl == null || videoUrl.isEmpty()) {
                placeholder.setText("Видео для серии " + episode + " не найдено");
                placeholder.setVisible(true);
                mediaView.setVisible(false);
                return;
            }
            
            Media media = new Media(videoUrl);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            mediaPlayer.setOnReady(() -> {
                placeholder.setVisible(false);
                mediaView.setVisible(true);
                mediaPlayer.play();
            });
            
            mediaPlayer.setOnError(() -> {
                placeholder.setText("Ошибка загрузки видео: " + mediaPlayer.getError().getMessage());
                placeholder.setVisible(true);
                mediaView.setVisible(false);
            });
            
        } catch (Exception e) {
            placeholder.setText("Ошибка: " + e.getMessage());
            placeholder.setVisible(true);
            mediaView.setVisible(false);
        }
    }
    
    private String getVideoUrlForEpisode(int episode) {
        if (episode == 1) {
            return "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        }
        
        return null;
    }
    
    public String getRealVideoUrl(String iframeUrl) {
        try {
            String fullUrl = iframeUrl.startsWith("//") ? "https:" + iframeUrl : iframeUrl;
            
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setInstanceFollowRedirects(false);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || 
                responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String realUrl = connection.getHeaderField("Location");
                return realUrl != null ? realUrl : fullUrl;
            }
            
            return fullUrl;
        } catch (Exception e) {
            return iframeUrl;
        }
    }
}