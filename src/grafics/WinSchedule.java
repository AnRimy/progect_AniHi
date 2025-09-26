package grafics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mainLogic.Core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class WinSchedule {
    private BorderPane parent;
    private Core client;
    
    public WinSchedule(BorderPane parent, Core client) {
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
            mainContainer.setCenter(scheduleContent);
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка загрузки расписания");
            mainContainer.setCenter(errorLabel);
        }
        
        parent.setCenter(mainContainer);
    }
    
    private VBox createScheduleContent(JsonNode animeArray) {
        VBox daysContainer = new VBox();
        
        List<List<JsonNode>> daysAnime = groupAnimeByDay(animeArray);
        
        String[] dayNames = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        
        for (int i = 0; i < 7; i++) {
            VBox dayPanel = createDayPanel(dayNames[i], daysAnime.get(i));
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
        
        Label dayLabel = new Label(dayName);
        
        HBox animeContainer = new HBox();
        
        if (animeList.isEmpty()) {
            Label noAnimeLabel = new Label("На этот день аниме не запланировано");
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
        
        dayPanel.getChildren().addAll(dayLabel, scrollPane);
        return dayPanel;
    }
    
    private Button createAnimeButton(JsonNode anime) {
        String title = anime.path("title").asText("Без названия");
        int airedEpisodes = anime.path("episodes").path("aired").asInt();
        int totalEpisodes = anime.path("episodes").path("count").asInt();
        long nextDate = anime.path("episodes").path("next_date").asLong();
        
        Button button = new Button();
        
        String buttonText = title;
        if (airedEpisodes > 0) {
            buttonText += "\nЭп: " + airedEpisodes;
            if (totalEpisodes > 0) {
                buttonText += "/" + totalEpisodes;
            }
        }
        
        button.setText(buttonText);
        
        button.setOnAction(e -> {
            int animeId = anime.path("anime_id").asInt();
            openAnimeDetails(animeId);
        });
        
        return button;
    }
    
    private void openAnimeDetails(int animeId) {
    }
}