package mainLogic;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.regex.*;

public class PreparationTitles {
	public static String editionLine(String input) {
        return input.replaceAll("[^a-zA-Zа-яА-ЯёЁ0-9\\s]", "").trim().replaceAll("^[0-9]+", "");
	}
	
	
	public static List readFile() {
		String filePath = "resources/titles.txt";
		List<String> list_titles = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			
			while ((line = reader.readLine()) != null) {
				line = editionLine(line);
				list_titles.add(line);
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		return list_titles;
	}

}