package grafics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class test1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            WebView webView = new WebView();
            
            // Создаем HTML с iframe для видео
            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body, html {
                            margin: 0;
                            padding: 0;
                            width: 100%;
                            height: 100%;
                            overflow: hidden;
                        }
                        iframe {
                            width: 100%;
                            height: 100%;
                            border: none;
                        }
                    </style>
                </head>
                <body>
                    <iframe src="https://cloud.kodik-storage.com/useruploads/67b6e546-e51d-43d2-bb11-4d8bfbedc2d7/d6f4716bc90bd30694cf09b0062d07a2:2024062705/720.mp4" 
                            allowfullscreen></iframe>
                </body>
                </html>
                """;
            
            webView.getEngine().loadContent(htmlContent);
            
            StackPane root = new StackPane();
            root.getChildren().add(webView);
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Kodik Video Player");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}