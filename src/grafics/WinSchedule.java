package grafics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mainLogic.Core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class WinSchedule {
	private BorderPane main_parent;
    private BorderPane parent;
    private Core client;
    
    public WinSchedule(BorderPane main_parent, BorderPane parent, Core client) {
    	this.main_parent = main_parent;
        this.parent = parent;
        this.client = client;
    }

    public void createWin() {
        parent.getChildren().clear();

        BorderPane mainContainer = new BorderPane();
        
        try {
            String response = client.getSchedule();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response).path("response");
            
            VBox scheduleContent = createScheduleContent(rootNode);
            scheduleContent.setMaxWidth(500);
            scheduleContent.setStyle("-fx-background-color: rgba(200, 25, 0, 0.2); -fx-background-radius: 25px;");
            mainContainer.setLeft(scheduleContent);
            
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка загрузки расписания");
            mainContainer.setCenter(errorLabel);
        }
        
        parent.setCenter(mainContainer);
        parent.setMargin(mainContainer, new Insets(5));
    }
    
    
    private VBox createScheduleContent(JsonNode animeArray) {
        VBox daysContainer = new VBox(15);
        daysContainer.setPadding(new Insets(10));
        
        List<List<JsonNode>> daysAnime = groupAnimeByDay(animeArray);
        
        String[] dayNames = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        
        for (int i = 0; i < 7; i++) {
            VBox dayPanel = createDayPanel(dayNames[i], daysAnime.get(i));
            VBox.setMargin(dayPanel, new Insets(0, 0, 10, 0));
            daysContainer.getChildren().add(dayPanel);
        }
        
        return daysContainer;
    }
    
    private List<List<JsonNode>> groupAnimeByDay(JsonNode animeArray) {
        List<List<JsonNode>> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(new ArrayList<>());
        }
        
        for (JsonNode anime : animeArray) {
            long nextDateTimestamp = anime.path("episodes").path("next_date").asLong();
            if (nextDateTimestamp > 0) {
                int dayOfWeek = getDayOfWeekFromTimestamp(nextDateTimestamp);
                if (dayOfWeek >= 0 && dayOfWeek < 7) {
                    days.get(dayOfWeek).add(anime);
                }
            }
        }
        
        return days;
    }
    
    private int getDayOfWeekFromTimestamp(long timestamp) {
        try {
            Instant instant = Instant.ofEpochSecond(timestamp);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            int javaDayOfWeek = dateTime.getDayOfWeek().getValue();
            return javaDayOfWeek - 1;
        } catch (Exception e) {
            return -1;
        }
    }
    
    private VBox createDayPanel(String dayName, List<JsonNode> animeList) {
        VBox dayPanel = new VBox();
        dayPanel.setMinHeight(50);
        dayPanel.setPrefHeight(50);
        dayPanel.setStyle("-fx-background-color: green");
        dayPanel.setPadding(new Insets(5));
        
        Label dayLabel = new Label(dayName);
        dayLabel.setPadding(new Insets(0, 0, 5, 0));
        dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        HBox animeContainer = new HBox();
        animeContainer.setSpacing(10);
        animeContainer.setAlignment(Pos.TOP_LEFT);
        
        if (animeList.isEmpty()) {
            Label noAnimeLabel = new Label("На этот день аниме не запланировано");
            noAnimeLabel.setPadding(new Insets(5));
            noAnimeLabel.setAlignment(Pos.CENTER);
            animeContainer.getChildren().add(noAnimeLabel);
        } else {
            for (JsonNode anime : animeList) {
                Button animeButton = createAnimeButton(anime);
                animeContainer.getChildren().add(animeButton);
            }
        }
        
        ScrollPane scrollPane = new ScrollPane(animeContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(0));
        scrollPane.setVisible(false);
        
        dayPanel.getChildren().addAll(dayLabel, scrollPane);
        
        VBox containerWithBuffer = new VBox();
        containerWithBuffer.setSpacing(0);
        
        Region topBuffer = new Region();
        topBuffer.setMinHeight(20);
        topBuffer.setPrefHeight(20);
        topBuffer.setStyle("-fx-background-color: transparent");
        
        containerWithBuffer.getChildren().addAll(topBuffer, dayPanel);
        
        final SimpleBooleanProperty isExpanded = new SimpleBooleanProperty(false);
        
        Node[] hoverNodes = {topBuffer, dayPanel};
        
        for (Node node : hoverNodes) {
            node.setOnMouseEntered(e -> {
                if (!isExpanded.get()) {
                    dayPanel.setPrefHeight(Region.USE_COMPUTED_SIZE);
//                    scrollPane.setMinHeight(220);
                	scrollPane.setVisible(true);
                    isExpanded.set(true);
                }
            });
            
            node.setOnMouseExited(e -> {
                PauseTransition pause = new PauseTransition(Duration.millis(300));
                pause.setOnFinished(event -> {
                    boolean stillHovered = false;
                    for (Node n : hoverNodes) {
                        if (n.isHover()) {
                            stillHovered = true;
                            break;
                        }
                    }
                    
                    if (!stillHovered && isExpanded.get()) {
                        dayPanel.setPrefHeight(50);
                        scrollPane.setVisible(false);
                        isExpanded.set(false);
                    }
                });
                pause.play();
            });
        }
        return containerWithBuffer;
    }
    
    private Button createAnimeButton(JsonNode anime) {
        String img = "https:" + anime.path("poster").path("fullsize").asText();
        String title = anime.path("title").asText("Без названия");
        int airedEpisodes = anime.path("episodes").path("aired").asInt();
        int totalEpisodes = anime.path("episodes").path("count").asInt();
        
        Button button = new Button();
        button.setContentDisplay(ContentDisplay.TOP);
        
        String buttonText = title;
        if (airedEpisodes > 0) {
            buttonText += "\nЭп: " + airedEpisodes;
            if (totalEpisodes > 0) {
                buttonText += "/" + totalEpisodes;
            }
        }
        
        button.setText(buttonText);
        
        ImageView sharpImageView = new ImageView(new Image(img, 100, 150, true, true, true));
        sharpImageView.setFitWidth(100);
        sharpImageView.setFitHeight(150);
        sharpImageView.setPreserveRatio(true);
        Rectangle clip = new Rectangle(100, 150);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        sharpImageView.setClip(clip);
        
        button.setGraphic(sharpImageView);
        
        button.setMinWidth(120);
        button.setPrefWidth(120);
        button.setMaxWidth(120);
        
        button.setMinHeight(200);
        button.setPrefHeight(200);
        button.setMaxHeight(200);
        
        button.setWrapText(true);
        
button.setAlignment(Pos.CENTER);
        
        button.setOnAction(e -> {
            int animeId = anime.path("anime_id").asInt();
            openAnimeDetails(animeId);
        });
        
        return button;
    }
    
    private void openAnimeDetails(int animeId) {
    	WinTitle win = new WinTitle(animeId, client);
    	win.createWin();
    	main_parent.getChildren().add(win.createWin());
    }
}



