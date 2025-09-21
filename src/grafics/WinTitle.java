package grafics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mainLogic.Core;

public class WinTitle extends Stage {    
    private String image;
    private String name;
    private int id;
    private String desc;
    private JsonNode Jgenres;
    private Core client;
    private WebView webView;
    private ArrayList<String> episodeUrls = new ArrayList<>();
    private Map<String, Map<String, List<JsonNode>>> dubbingPlayerEpisodesMap = new TreeMap<>();
    private String currentDubbing = "";
    private String currentPlayer = "";
    private VBox seriesContainer;
    private ComboBox<String> dubbingComboBox;
    private ComboBox<String> playerComboBox;
    private HBox filterPanel;

    public WinTitle(int id, Core client){
    	this.client = client;
    	this.id = id;
    	ObjectMapper mapper = new ObjectMapper();
    	    	
		try {
			String response = client.searchAnimeId(id, false);
			JsonNode data = new ObjectMapper().readTree(response).path("response");
	        this.image = "https:" + data.path("poster").path("fullsize").asText();
	        this.name = data.path("title").asText();
	        this.desc = data.path("description").asText();
	        this.Jgenres = data.path("genres");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
        
    public BorderPane createWin() {
        BorderPane root = new BorderPane();
              
        StackPane mainContainer = new StackPane();
    
        // background effect
        Rectangle blurBackground = new Rectangle(1920, 1080);
        blurBackground.setFill(Color.rgb(100, 100, 100, 0.1));
        GaussianBlur blur = new GaussianBlur(50);
        blurBackground.setEffect(blur);
        
        Rectangle darkOverlay = new Rectangle(1920, 1080);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.7));
        
        BorderPane contentPane = new BorderPane();
        contentPane.setMaxSize(1600, 900);
        contentPane.setStyle("-fx-background-color: rgba(30, 30, 40, 0.95); -fx-background-radius: 20px; -fx-border-radius: 20px;");
        
        // top content
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        topPanel.setPadding(new Insets(20));
        topPanel.setStyle("-fx-background-color: rgba(25, 25, 35, 0.9); -fx-background-radius: 20px; -fx-border-radius: 20px;");
        
        Label titleLabel = new Label(name);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);
        
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("""
                -fx-background-color: #e74c3c;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 16px;
                -fx-min-width: 40px;
                -fx-min-height: 40px;
                -fx-background-radius: 20px;
                -fx-border-radius: 20px;
                -fx-cursor: hand;
            """);
        closeBtn.setOnAction(e -> closeWindow(root));
        
        topPanel.getChildren().addAll(titleLabel, closeBtn);
        
        HBox centerContent = new HBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_CENTER);
        
        // info panel
        VBox infoPanel = new VBox(15);
        infoPanel.setMaxWidth(300);
        infoPanel.setPadding(new Insets(15));
        infoPanel.setStyle("-fx-background-color: rgba(40, 40, 50, 0.8); -fx-background-radius: 20px;");
        
        ImageView poster = new ImageView(image);
        poster.setFitWidth(250);
        poster.setFitHeight(350);
        poster.setPreserveRatio(true);
        
        // genres
        String genres = "";
        if (Jgenres.isArray()) {
            for(JsonNode genre: Jgenres)
                genres += genre.path("title").asText("None") + " ";
        }
        
        Label genresLabel = new Label("Жанры: " + genres);
        genresLabel.setFont(Font.font("Arial", 14));
        genresLabel.setTextFill(Color.LIGHTGRAY);
        genresLabel.setWrapText(true);
        
        Label descLabel = new Label(desc);
        descLabel.setFont(Font.font("Arial", 14));
        descLabel.setTextFill(Color.WHITE);
        descLabel.setWrapText(true);
        
        infoPanel.getChildren().addAll(poster, genresLabel, descLabel);
        
        VBox videoPanel = new VBox(15);
        videoPanel.setAlignment(Pos.TOP_CENTER);
        
        // player
        StackPane videoPlayerContainer = new StackPane();
        videoPlayerContainer.setMinSize(900, 500);
        videoPlayerContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 20px; -fx-border-radius: 20px");
        
        webView = new WebView();
        webView.setPrefSize(900, 500);
        
        videoPlayerContainer.getChildren().add(webView);
        
        seriesContainer = new VBox(8);
        seriesContainer.setPadding(new Insets(10));
        seriesContainer.setStyle("-fx-background-color: rgba(35, 35, 45, 0.8); -fx-background-radius: 20px; -fx-border-radius: 20px");
        
        loadEpisodesData(seriesContainer);
        
        //scroll for series
        ScrollPane seriesScroll = new ScrollPane(seriesContainer);
        seriesScroll.setPrefHeight(400);
        seriesScroll.setFitToWidth(true);
        
        // constract
        videoPanel.getChildren().addAll(videoPlayerContainer, seriesScroll);
        centerContent.getChildren().addAll(infoPanel, videoPanel);
        contentPane.setTop(topPanel);
        contentPane.setCenter(centerContent);
        mainContainer.getChildren().addAll(blurBackground, darkOverlay, contentPane);
        root.setCenter(mainContainer);
        
        return root;
    }
    
    private HBox createFilterPanel() {
        filterPanel = new HBox(15);
        filterPanel.setAlignment(Pos.CENTER_LEFT);
        filterPanel.setPadding(new Insets(10));
        filterPanel.setStyle("-fx-background-color: rgba(45, 45, 55, 0.8); -fx-background-radius: 10px;");
        
        // box sub
        Label dubbingLabel = new Label("Озвучка:");
        dubbingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        dubbingLabel.setTextFill(Color.WHITE);
        
        dubbingComboBox = new ComboBox<>();
        dubbingComboBox.setStyle("""
            -fx-background-color: rgba(60, 60, 80, 0.8);
            -fx-text-fill: white;
            -fx-pref-width: 200px;
        """);
        
        // box player
        Label playerLabel = new Label("Плеер:");
        playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        playerLabel.setTextFill(Color.WHITE);
        
        playerComboBox = new ComboBox<>();
        playerComboBox.setStyle("""
            -fx-background-color: rgba(60, 60, 80, 0.8);
            -fx-text-fill: white;
            -fx-pref-width: 150px;
        """);
        
        dubbingComboBox.getItems().addAll(dubbingPlayerEpisodesMap.keySet());
        
        if (!dubbingPlayerEpisodesMap.isEmpty()) {
            String firstDubbing = dubbingPlayerEpisodesMap.keySet().iterator().next();
            dubbingComboBox.setValue(firstDubbing);
            currentDubbing = firstDubbing;
            
            updatePlayerComboBox();
        }
        
        // action
        dubbingComboBox.setOnAction(e -> {
            String selectedDubbing = dubbingComboBox.getValue();
            if (selectedDubbing != null && !selectedDubbing.equals(currentDubbing)) {
                currentDubbing = selectedDubbing;
                updatePlayerComboBox();
                updateEpisodesList();
            }
        });
        
        playerComboBox.setOnAction(e -> {
            String selectedPlayer = playerComboBox.getValue();
            if (selectedPlayer != null && !selectedPlayer.equals(currentPlayer)) {
                currentPlayer = selectedPlayer;
                updateEpisodesList();
            }
        });
        
        filterPanel.getChildren().addAll(dubbingLabel, dubbingComboBox, playerLabel, playerComboBox);
        return filterPanel;
    }
    
    private void updatePlayerComboBox() {
        playerComboBox.getItems().clear();
        
        if (dubbingPlayerEpisodesMap.containsKey(currentDubbing)) {
            Map<String, List<JsonNode>> playersMap = dubbingPlayerEpisodesMap.get(currentDubbing);
            playerComboBox.getItems().addAll(playersMap.keySet());
            
            if (!playersMap.isEmpty()) {
                String firstPlayer = playersMap.keySet().iterator().next();
                playerComboBox.setValue(firstPlayer);
                currentPlayer = firstPlayer;
            }
        }
    }
    
    private void loadEpisodesData(VBox container) {
        this.seriesContainer = container;
        
        try {
            String response = client.getSeriasAnime(id);
            JsonNode rootNode = new ObjectMapper().readTree(response).path("response");
            
            if (rootNode.isEmpty()) {
                Label noEpisodesLabel = new Label("Эпизоды не найдены");
                noEpisodesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                container.getChildren().add(noEpisodesLabel);
                return;
            }
            
            // groups sub and player
            for(JsonNode video : rootNode) {
                String dubbing = video.path("data").path("dubbing").asText("Неизвестно");
                String player = video.path("data").path("player").asText("Неизвестно");
                
                if (!dubbingPlayerEpisodesMap.containsKey(dubbing)) {
                    dubbingPlayerEpisodesMap.put(dubbing, new TreeMap<>());
                }
                
                Map<String, List<JsonNode>> playersMap = dubbingPlayerEpisodesMap.get(dubbing);
                if (!playersMap.containsKey(player)) {
                    playersMap.put(player, new ArrayList<>());
                }
                
                playersMap.get(player).add(video);
            }
            
            if (!dubbingPlayerEpisodesMap.isEmpty()) {
                container.getChildren().add(createFilterPanel());
            }
            
            if (!dubbingPlayerEpisodesMap.isEmpty()) {
                currentDubbing = dubbingPlayerEpisodesMap.keySet().iterator().next();
                Map<String, List<JsonNode>> playersMap = dubbingPlayerEpisodesMap.get(currentDubbing);
                if (!playersMap.isEmpty()) {
                    currentPlayer = playersMap.keySet().iterator().next();
                    updateEpisodesList();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка загрузки эпизодов");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            container.getChildren().add(errorLabel);
        }
    }
    
    private void updateEpisodesList() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : seriesContainer.getChildren()) {
            if (node instanceof Button || (node instanceof Label && !(node.getParent() instanceof HBox))) {
                nodesToRemove.add(node);
            }
        }
        seriesContainer.getChildren().removeAll(nodesToRemove);
        
        if (dubbingPlayerEpisodesMap.containsKey(currentDubbing)) {
            Map<String, List<JsonNode>> playersMap = dubbingPlayerEpisodesMap.get(currentDubbing);
            
            if (playersMap.containsKey(currentPlayer)) {
                List<JsonNode> episodes = playersMap.get(currentPlayer);
                
                if (episodes != null && !episodes.isEmpty()) {
                    episodes.sort((e1, e2) -> {
                        int num1 = e1.path("number").asInt();
                        int num2 = e2.path("number").asInt();
                        return Integer.compare(num1, num2);
                    });
                    
                    for (JsonNode video : episodes) {
                        String iframeUrl = video.path("iframe_url").asText();
                        if (!iframeUrl.equals("None")) {
                            String fullUrl = iframeUrl.startsWith("//") ? "https:" + iframeUrl : iframeUrl;
                            
                            int episodeNumber = video.path("number").asInt();
                            String playerName = video.path("data").path("player").asText();
                            String dubbingName = video.path("data").path("dubbing").asText();
                            
                            Button episodeBtn = createEpisodeButton(episodeNumber, fullUrl, playerName, dubbingName);
                            seriesContainer.getChildren().add(episodeBtn);
                        }
                    }
                } else {
                    addNoEpisodesLabel();
                }
            } else {
                addNoEpisodesLabel();
            }
        } else {
            addNoEpisodesLabel();
        }
    }
    
    private void addNoEpisodesLabel() {
        Label noEpisodesLabel = new Label("Нет эпизодов для выбранных фильтров");
        noEpisodesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        seriesContainer.getChildren().add(noEpisodesLabel);
    }
    
    private Button createEpisodeButton(int episodeNumber, String videoUrl, String playerName, String dubbingName) {
        Button episodeBtn = new Button(String.format("Серия %d\n%s • %s", episodeNumber, playerName, dubbingName));
        episodeBtn.setStyle("""
            -fx-background-color: rgba(60, 60, 80, 0.8);
            -fx-text-fill: white;
            -fx-cursor: hand;
            -fx-padding: 10 15;
            -fx-alignment: center-left;
            -fx-min-width: 250px;
            -fx-min-height: 50px;
            -fx-font-size: 12px;
        """);
        
        episodeBtn.setOnMouseEntered(e -> {
            episodeBtn.setStyle("""
                -fx-background-color: rgba(80, 80, 100, 0.9);
                -fx-text-fill: white;
                -fx-cursor: hand;
                -fx-padding: 10 15;
                -fx-alignment: center-left;
                -fx-min-width: 250px;
                -fx-min-height: 50px;
                -fx-font-size: 12px;
            """);
        });
        
        episodeBtn.setOnMouseExited(e -> {
            episodeBtn.setStyle("""
                -fx-background-color: rgba(60, 60, 80, 0.8);
                -fx-text-fill: white;
                -fx-cursor: hand;
                -fx-padding: 10 15;
                -fx-alignment: center-left;
                -fx-min-width: 250px;
                -fx-min-height: 50px;
                -fx-font-size: 12px;
            """);
        });
        
        episodeBtn.setOnAction(e -> loadVideoInWebView(videoUrl));
        
        return episodeBtn;
    }
    
    private void loadVideoInWebView(String videoUrl) {
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body, html {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                        background: #000;
                    }
                    .container {
                        width: 100%;
                        height: 100%;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <iframe src=""" + videoUrl + """
                        allowfullscreen
                        allow="autoplay; encrypted-media"
                        scrolling="no">
                    </iframe>
                </div>
            </body>
            </html>
        """;
        
        webView.getEngine().loadContent(htmlContent);
    }
    
    private void closeWindow(BorderPane root) {
        if (webView != null) {
            webView.getEngine().load(null);
        }
        
        Scene scene = root.getScene();
        if (scene != null) {
            Parent rootParent = scene.getRoot();
            if (rootParent instanceof BorderPane) {
                ((BorderPane) rootParent).getChildren().remove(root);
            }
        }
    }
}