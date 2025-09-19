package mainLogic;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;


public class Core {
    private final HttpClient client;
    private final String baseUrl;
    private final String apiKey;
    private String themeParam;

    public Core(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    
    private HttpRequest requests(String url) {
    	HttpRequest requests = HttpRequest.newBuilder()
    			.uri(URI.create(url))
	            .header("X-Application", apiKey)
	            .header("Accept", "application/json")
//	            .header("Accept", "image/avif,image/webp")
	            .GET()
	            .build();
    	return requests;
    }
    
    
    public String getAnimeList(int limit, int offset) throws Exception {
    	themeParam = "anime";
        String queryParams = String.format("limit=%d&offset=%d", limit, offset);
        String url = String.format("%s/%s?%s", baseUrl, themeParam, queryParams);
    	

    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String searchAnime(String name, int limit, int offset) throws Exception{
    	themeParam = "search";
    	String queryParams = String.format("q=%s&limit=%d&offset=%d", name, limit, offset);
    	String url = String.format("%s/%s?%s", baseUrl, themeParam, queryParams);

    	
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String getVideoAnime(String id) throws Exception{
    	themeParam = "anime";
    	String url = String.format("%s/%s/%s/%s", baseUrl, themeParam, id, "videos");
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String getFeed() throws Exception{
    	themeParam = "feed";
    	String url = String.format("%s/%s", baseUrl, themeParam);
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String getCaralog() throws Exception{
    	themeParam = "anime/catalog";
    	String url = String.format("%s/%s", baseUrl, themeParam);
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String getSchedule() throws Exception{
    	themeParam = "anime/catalog";
    	String url = String.format("%s/%s", baseUrl, themeParam);
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    
    public String getOngoing() throws Exception{
    	themeParam = "anime?status=ongoing&sort_forward=true&sort=top&offset=0&limit=20";
    	String url = String.format("%s/%s", baseUrl, themeParam);
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
    
    public String getSeriasAnime(int id) throws Exception{
    	themeParam = String.format("anime/%d/videos", id);
    	String url = String.format("%s/%s", baseUrl, themeParam);
    	HttpResponse<String> response = client.send(requests(url), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) 
            throw new RuntimeException("API request failed: " + response.statusCode());
    	return response.body();
    }
}

























