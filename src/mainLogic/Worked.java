//package mainLogic;
//
//import java.util.ArrayList;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class Worked {
//	public static void worked() {
//	    ObjectMapper mapper = new ObjectMapper();
//
//	    ArrayList<String> arrayList = DataBaseManager.getNameTitles();
//	    Core client = new Core(baseUrl, apiKey);
//	    for (String title : arrayList) {
//	        try {
//        		
//				String response = client.searchAnime(title, 20, 0);
//	            JsonNode rootNode = mapper.readTree(response);
//	            
//	            JsonNode responseNode = rootNode.get("response");
//	            if (responseNode == null || !responseNode.isArray() || responseNode.size() == 0) {
//	                System.err.println("Не найдено результатов для: " + title);
//	                continue;
//	            }
//	            
//	            JsonNode firstResult = responseNode.get(0);
//	            if (firstResult == null) {
//	                System.err.println("Пустой результат для: " + title);
//	                continue;
//	            }
//	            
//	            int id_yummi = firstResult.path("anime_id").asInt(0);
//	            String name_original = firstResult.path("title").asText(title);
//	            String name_english = firstResult.path("english").asText("");
//	            String image = "http:" + firstResult.path("poster").path("fullsize").asText("");
//	            String description = firstResult.path("description").asText("");
//	            String grade = firstResult.path("rating").path("average").asText("0");
//	            String type_format = firstResult.path("type").path("name").asText("");
//	            String release_date = firstResult.path("year").asText("");
//	            
//	            DataBaseManager.setInfoTitle(title,
//	                id_yummi, name_original, name_english, 
//	                image, description, grade, 
//	                release_date, type_format
//	            );
//	            
//	        } catch (JsonProcessingException e) {
//	            System.err.println("Ошибка парсинга JSON для '" + title + "': " + e.getMessage());
//	        } catch (Exception e) {
//	            System.err.println("Общая ошибка при обработке '" + title + "': " + e.getMessage());
//	            e.printStackTrace();
//	        }
//	    }
//	}
//}
